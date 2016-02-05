/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package base_simulator.layers;

import base_simulator.Entita;
import base_simulator.Messaggi;
import base_simulator.Nodo;
import base_simulator.scheduler;
import java.util.ArrayList;

/**
 *
 * @author afsantamaria
 */
class Applicazione {

    int port = 0;
    String message = "";
    int status = -1;

    public Applicazione() {
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message += message;
    }

    int getStatus() {
        return this.status;
    }

    void setStatus(int stato) {
        this.status = stato;
    }

}

public class TransportLayer extends Entita {

    protected NetworkLayer networkLayer;
    protected double tempo_di_processamento;
    protected Object nodo;
    protected ArrayList<Integer> enabled_ports = new ArrayList<Integer>();
    protected int header_size = 20; //Byte
    private String TCP = "tcp";
    private String UDP = "udp";

    private ArrayList<Applicazione> apps = new ArrayList<Applicazione>();

    private String OPEN_CONNECTION = "open connection";
    private String OPEN_CONNECTION_ANSWER = "open connection answer";
    private String CLOSE_CONNECTION = "close connection";

    private int connection_refused = 0;
    private int connection_accepted = 0;
    private int droppedPacket = 0;
    private int packet_refused = 0;
    private int active_session = 0;

    final int WAITING = 1;
    final int ACCEPTED = 2;
    final int REFUSED = 0;

    public int getHeader_size() {
        return header_size;
    }

    public void setHeader_size(int header_size) {
        this.header_size = header_size;
    }

    public TransportLayer(scheduler s, double tempo_di_processamento) {
        super(s, "Transport Layer");
        this.tempo_di_processamento = tempo_di_processamento;
    }

    public void connectTransportLayer(NetworkLayer networkLayer, Object nodo) {
        this.networkLayer = networkLayer;
        this.nodo = nodo;
    }

    public void enablePort(int port) {
        enabled_ports.add(port);
    }

    @Override
    public void Handler(Messaggi m) {
        if (m.getTipo_Messaggio().toLowerCase().equals("applicazione")) {
            if (isAvailable(m.getApplication_port())) {
                System.out.println("I:" + this.getTipo() + " su nodo:" + ((Nodo) nodo).getId() + ": Arrivato messaggio applicazione con ID " + m.getID() + " Sulla porta :" + m.getApplication_port());
                if (m.saliPilaProtocollare == false) {
                    if (sessionActive(m) == ACCEPTED) {
                        try {
                            //Da inviare pacchetto al networkLayer
                            m.addHeader(this.header_size);
                            m.setNodoSorgente(nodo);
                            m.setSorgente(this);
                            m.setDestinazione(this.networkLayer);
                            m.shifta(tempo_di_processamento);
                            s.insertMessage(m);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (sessionActive(m) == WAITING) {
                        m.shifta(1000);
                        s.insertMessage(m);
                    } else {
                        //Il pacchetto è stato ricevuto
                        m.removeHeader(this.header_size);
                        storePayload(m);
                        if (m.getAckType() == m.WITH_ACK) {
                            //TODO : Devo inviare ack indietro alla sorgente
                        }
                    }

                } else {
                    packet_refused++;
                }
            }
        } else if (m.getTipo_Messaggio().toLowerCase().equals(OPEN_CONNECTION)) {
            boolean res = false;
            if (isAvailable(m.getApplication_port())) {
                //return REFUSED to request host
                connection_refused++;
            } else {
                res = true;
                this.enablePort(m.getApplication_port());
                connection_accepted++;
                //return ACCEPTED to request host and open an application receiver
                Applicazione a = new Applicazione();
                a.setPort(m.getApplication_port());
            }

            m.setTipo_Messaggio(OPEN_CONNECTION_ANSWER);
            m.saliPilaProtocollare = false;
            m.setData(res);
            m.setDestinazione(this.networkLayer);
            m.setSorgente(this);
            m.shifta(tempo_di_processamento);
            m.setNodoDestinazione(m.getNodoSorgente());
            m.setNodoSorgente(nodo);
            m.addHeader(this.getHeader_size());

            s.insertMessage(m);

        } else if (m.getTipo_Messaggio().toLowerCase().equals(OPEN_CONNECTION_ANSWER)) {
            if (((Boolean) m.getData()) == true) {
                active_session++;
                start_sending(m.getApplication_port());
            }
        } else if (m.getTipo_Messaggio().toLowerCase().equals(TCP)) {

        } else if (m.getTipo_Messaggio().toLowerCase().equals(UDP)) {

        } else if (m.getTipo_Messaggio().toLowerCase().equals(this.CLOSE_CONNECTION)) {
            //TODO: DA INVIARE MESSAGGIO DI CHIUSURA CONNESSIONE
            //ripulire la connessione sul nodo liberando risorse porta e app
        }

    }

    private boolean isAvailable(int application_port) {
        boolean res = false;
        for (Object port : enabled_ports) {
            if (application_port == ((Integer) port)) {
                res = true;
                break;
            }
        }
        return res;
    }

    private void storePayload(Messaggi m) {
        for (Object obj : apps) {
            if (((Applicazione) obj).getPort() == m.getApplication_port()) {
                ((Applicazione) obj).setMessage((String) m.getData());
            }
        }
    }

    private int sessionActive(Messaggi m) {
        int status = -1;
        Applicazione a = new Applicazione();
        for (Object obj : apps) {
            if (((Applicazione) obj).getPort() == m.getApplication_port()) {
                status = ((Applicazione) obj).getStatus();
            }
        }
        if (status == -1) {
            a.setStatus(WAITING);
            a.setPort(m.getApplication_port());
            apps.add(a);

            //Devo inviare messaggio al nodo destinazione
            Messaggi m1 = new Messaggi(this.OPEN_CONNECTION, this, this.networkLayer, m.getNodoDestinazione(), s.orologio.getCurrent_Time());
            m1.saliPilaProtocollare = false;
            m1.isData = true;
            m1.setNodoSorgente(nodo);
            m1.shifta(0);
            m1.setApplication_port(m.getApplication_port());

            s.insertMessage(m1);

        }
        return status;
    }

    private void start_sending(int application_port) {
        for (Object obj : apps) {
            Applicazione a = (Applicazione) obj;
            if (((Applicazione) obj).getPort() == application_port) {
                ((Applicazione) obj).setStatus(ACCEPTED);
            }
        }
    }

}
