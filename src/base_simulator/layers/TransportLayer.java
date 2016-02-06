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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Questa classe permette di memorizzare i dati che arrivano o devono andare al 
 * livello applicazione. In particolare, è molto utile nella ricostruzione dei frammenti
 * @author afsantamaria
 */
class Applicazione {

    int port = 0;
    byte message[] = new byte[0];
    int status = -1;

    public Applicazione() {
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public byte[] getMessage() {
        return message;
    }

    /**
     * Metodo per la ricostruzione dei frammenti della sessione applicazione
     * i dati arrivano attraverso la rete e poi vengono ricostruiti da questo metodo
     * 
     * @param message - byte array contenente frammenti dell'informazione da utilizzare
     * a livello applicazione
     */
    public void setMessage(byte message[]) {
        
        int new_size = this.message.length + message.length;        
        
        byte appo[] = new byte[this.message.length];
        System.arraycopy(this.message, 0, appo, 0, this.message.length);
        
        this.message = new byte[new_size];        
        System.arraycopy(appo, 0, this.message, 0, appo.length);
        
        System.arraycopy(message, 0, this.message, appo.length, message.length);

    }

    int getStatus() {
        return this.status;
    }

    void setStatus(int stato) {
        this.status = stato;
    }

}

/**
 * Livello Trasporto : Tutti i dati sono trasferiti utilizzando connessioni non sicure
 * non è implementato in questa classe il controllo di congestione e di flusso del TCP
 * 
 * I dati sono inviati utilizzando delle connessioni UDP-Like.
 * 
 * @author afsantamaria
 */
public class TransportLayer extends Entita {

    protected NetworkLayer networkLayer;
    protected double tempo_di_processamento;
    protected Object nodo;
    protected ArrayList<Integer> enabled_ports = new ArrayList<Integer>();
    protected int header_size = 20; //Byte
    protected String TCP = "tcp";
    protected String UDP = "udp";

    protected ArrayList<Applicazione> apps = new ArrayList<Applicazione>();

    protected String OPEN_CONNECTION = "open connection";
    protected String OPEN_CONNECTION_ANSWER = "open connection answer";
    protected String CLOSE_CONNECTION = "close connection";
    protected String CLOSE_CONNECTION_ACK = "close connection ack";

    protected String SVUOTA_CODA = "svuota coda";

    protected int connection_refused = 0;
    protected int connection_accepted = 0;
    protected int droppedPacket = 0;
    protected int packet_refused = 0;
    protected int active_session = 0;

    protected int WAITING = 1;
    protected int ACCEPTED = 2;
    protected int REFUSED = 0;

    protected ArrayList<Messaggi> buffer;
    protected boolean wating_for_send_messages = false;

    public int getHeader_size() {
        return header_size;
    }

    public void setHeader_size(int header_size) {
        this.header_size = header_size;
    }

    public TransportLayer(scheduler s, double tempo_di_processamento) {
        super(s, "Transport Layer");
        this.tempo_di_processamento = tempo_di_processamento;
        buffer = new ArrayList<Messaggi>();

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
                    //TODO : I messaggi vanno cmq messi nel buffer e poi inviati rischio di mandare fuori ordine già dalla sorgente
                    //SOLUZIONE CON UDP metto nel buffer ed invio tutto a partire dal primo pacchetto presente nel buffer, altrimenti si invierà allo scadere del timer
                    if (sessionActive(m) == ACCEPTED) {
                        //Inserisco il messaggio in coda ma devo cmq inserirlo nel buffer e forzare uno svuotamento
                        buffer.add(m);
                        Messaggi m1 = new Messaggi(this.SVUOTA_CODA, this, this, nodo, s.orologio.getCurrent_Time());
                        s.insertMessage(m1);

                    } else if (sessionActive(m) == WAITING) {
                        //TODO : devo cambiare il metodo mettendo pacchetto nel buffer e generando un metodo svuota buffer

                        buffer.add(m);
                        if (!wating_for_send_messages) {
                            wating_for_send_messages = true;
                            Messaggi m1 = new Messaggi(this.SVUOTA_CODA, this, this, nodo, s.orologio.getCurrent_Time() + 1000);
                            s.insertMessage(m1);
                        }
                    } else {
                        packet_refused++;
                    }

                } else {
                    //Il pacchetto è stato ricevuto
                    m.removeHeader(this.header_size);
                    storePayload(m);
                    if (m.getAckType() == m.WITH_ACK) {
                        //TODO : Devo inviare ack indietro alla sorgente
                    }

                }
            }
        } else if (m.getTipo_Messaggio().toLowerCase().equals(SVUOTA_CODA)) {
//Gestione tipo UDP ; Per la gestione TCP implementare meccanismo di Congestion Avoidance            
            wating_for_send_messages = false;
            double tempo = 0;
            for (Object obj : buffer) {
                Messaggi m1 = (Messaggi) obj;
                try {
                    //Da inviare pacchetto al networkLayer
                    m1.addHeader(this.header_size);
                    m1.setNodoSorgente(nodo);
                    m1.setSorgente(this);
                    m1.setDestinazione(this.networkLayer);
                    m1.shifta(tempo_di_processamento);
                    s.insertMessage(m1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                buffer.remove(obj);
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
                a.setStatus(this.ACCEPTED);
                apps.add(a);
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

            //ripulire la connessione sul nodo liberando risorse porta e app
            if (m.saliPilaProtocollare == false) {
                if (sessionActive(m) == ACCEPTED) {
                    m.setSorgente(this);
                    m.setDestinazione(this.networkLayer);
                    m.addHeader(header_size);
                    m.shifta(tempo_di_processamento);
                    m.setNodoSorgente(nodo);
                    s.insertMessage(m);
                } else if (sessionActive(m) == WAITING) {
                    m.shifta(1000);
                    s.insertMessage(m);
                } else {
                    this.packet_refused++;
                }
            } else {
                //Il messaggio è arrivato dalla sorgente sulla destinazione
                //Possiamo stampare il messaggio completo ricevuto
                System.out.println("L'applicazione ha terminato l'invio e ho ricevuto dal nodo :" + ((Nodo) m.getNodoSorgente()).getId() + "il seguente messaggio");
                Applicazione a = null;
                for (Object obj : apps) {
                    a = (Applicazione) obj;
                    if (a.getPort() == m.getApplication_port()) {
                        FileOutputStream OutFile = null;
                        try {
//Scrivo i byte su un file di uscita
                            OutFile = new FileOutputStream(
                                    "test.txt");
                            OutFile.write(a.getMessage());
                            OutFile.flush();
                            OutFile.close();
                            break;
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(TransportLayer.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(TransportLayer.class.getName()).log(Level.SEVERE, null, ex);
                        } finally {
                            try {
                                OutFile.close();
                            } catch (IOException ex) {
                                Logger.getLogger(TransportLayer.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }
                if (a != null) {
                    apps.remove(a);
                }

                //Chiudiamo la porta
                closePort(m.getApplication_port());
                m.setDestinazione(this.networkLayer);
                m.saliPilaProtocollare = false;
                m.setNodoDestinazione(m.getNodoSorgente());
                m.setNodoSorgente(this.nodo);
                m.shifta(tempo_di_processamento);
                m.setTipo_Messaggio(CLOSE_CONNECTION_ACK);
                s.insertMessage(m);

            }
        } else if (m.getTipo_Messaggio().toLowerCase().equals(this.CLOSE_CONNECTION_ACK)) {
            //Alla ricezione dell'ack possiamo distruggere applicazione e liberare la porta anche sulla sorgente
            Applicazione a = null;
            for (Object obj : apps) {
                a = (Applicazione) obj;
                if (a.getPort() == m.getApplication_port()) {
                    break;
                }
            }
            if (a != null) {
                apps.remove(a);
            }

            //Chiudiamo la porta
            closePort(m.getApplication_port());
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

    private boolean closePort(int application_port) {
        boolean res = false;
        int count = -1;
        for (Object port : enabled_ports) {
            count++;
            if (application_port == ((Integer) port)) {
                res = true;
                break;
            }
        }
        if (res == true) {
            enabled_ports.remove(count);
        }
        return res;
    }

    private void storePayload(Messaggi m) {
        if (m.getData() != null) {
            for (Object obj : apps) {
                if (((Applicazione) obj).getPort() == m.getApplication_port()) {
                    ((Applicazione) obj).setMessage((byte[]) m.getData());
                }
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
            status = WAITING;
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
            m1.setSize(m.getSize());

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
