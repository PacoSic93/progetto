/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package base_simulator.layers;

import base_simulator.Entita;
import base_simulator.Messaggi;
import base_simulator.scheduler;
import java.util.ArrayList;

/**
 *
 * @author afsantamaria
 */
public class TransportLayer extends Entita {

    
    protected NetworkLayer networkLayer;
    protected double tempo_di_processamento;
    protected Object nodo;
    protected ArrayList<Integer> enabled_ports = new ArrayList<Integer>();
    protected int header_size = 20; //Byte

    public int getHeader_size() {
        return header_size;
    }

    public void setHeader_size(int header_size) {
        this.header_size = header_size;
    }

    private int droppedPacket = 0;

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
               System.out.println("I:"+this.getTipo()+" : Arrivato messaggio applicazione con ID "+m.getID()+" Sulla porta :"+m.getApplication_port());
               if(m.saliPilaProtocollare == false)
               {
                   try
                   {
                   //Da inviare pacchetto al networkLayer
                   m.addHeader(this.header_size);
                   m.setSorgente(this);
                   m.setDestinazione(this.networkLayer);
                   m.shifta(tempo_di_processamento);
                   s.insertMessage(m);
                   }
                   catch(Exception e)
                   {
                       e.printStackTrace();
                   }
               }
               else
               {
                   //Il pacchetto è stato ricevuto
                   m.removeHeader(this.header_size);
               }
            }
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

}
