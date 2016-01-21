/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reti_tlc_gruppo_0;

import base_simulator.Infos;
import base_simulator.Applicazione;
import base_simulator.Grafo;
import base_simulator.Messaggi;
import base_simulator.Nodo;
import base_simulator.canale;
import base_simulator.layers.LinkLayer;
import base_simulator.layers.NetworkLayer;
import base_simulator.layers.TransportLayer;
import base_simulator.layers.physicalLayer;
import base_simulator.scheduler;
import java.util.ArrayList;

/**
 *
 * @author afsantamaria
 */
public class nodo_host extends Nodo {

    
    private int gatewayId;
    
    private int current_seq_no = 0;

    private Infos info = null;

    public Infos getInfo() {
        return info;
    }

    public void setInfo(Infos info) {
        this.info = info;
    }
    private ArrayList<NetworkInterface> nics = new ArrayList<NetworkInterface>();

    public nodo_host(scheduler s, int id_nodo,
            physicalLayer myPhyLayer, LinkLayer myLinkLayer, NetworkLayer myNetLayer, TransportLayer myTransportLayer,
            Grafo network, String tipo, int gtw) {
        super(s, id_nodo, myPhyLayer, myLinkLayer, myNetLayer, myTransportLayer, network, tipo);

        gatewayId = gtw;

        Messaggi m = new Messaggi("inizializza_attivita_nodo", this, this, this, 0.0);
        m.shifta(0);

        s.insertMessage(m);
    }

    private void inviaMessaggioACanale(Messaggi m) {
        int channel_id = 0;
        NetworkInterface i = null;
        for(Object interface_element : nics)
        {
             i = (NetworkInterface)interface_element;
            if(i.getDest() == m.getNextHop_id())
            {
                channel_id = i.getChannel_idx();
                break;
            }                         
        }
        
         
        canale current_channel = info.getCanale(channel_id);

        //Invio il messaggio al canale
        m.shifta(0);
        m.setSorgente(this);        
        m.setNextHop(info.getNodo(m.getNextHop_id()));
        m.setDestinazione(current_channel);
        s.insertMessage(m);
        
        System.out.println("I : "+this.getTipo()+" con ID "+((Nodo)this).getId()+" con IP :"+i.getIpv4()+" Invia su canale "+i.getChannel_idx());
    }

    private void inviaMessaggioAPhyLayer(Messaggi m) {
        m.shifta(0);
        m.setSorgente(this);
        m.setDestinazione(this.myPhyLayer);
        s.insertMessage(m);
    }

    @Override
    public void Handler(Messaggi m) {
        if (m.getTipo_Messaggio().equals("inizializza_attivita_nodo")) {
            //Controlla se ci sono applicazioni e le schedula
            this.generaEventiApplicazione();
        } else if (m.saliPilaProtocollare == true) {
            //il messaggio entra nel nodo e sarà elaborato al suo interno
            inviaMessaggioAPhyLayer(m);
        } else {
            //il messaggio dovrà essere propagato all'esterno
            inviaMessaggioACanale(m);
        }
    }

    public void addNIC(NetworkInterface nic) {
        this.nics.add(nic);
    }

    public NetworkInterface getNic(int idx) {
        return nics.get(idx);
    }

    int getGTW() {
        return this.gatewayId;
    }

    //Il nodo genera gli eventi di Applicazione che scenderanno al livello trasporto
    public void generaEventiApplicazione() {
        for (Object app : this.apps) {

            Applicazione item = (Applicazione) app;
            gestisciApplicazione(item);

        }
    }

    private void gestisciApplicazione(Applicazione app) {
        Nodo dest = info.getNodo(app.getDest());
        double file_size = app.getSize() * 8.0 * 1000000; //Dimensione del file in Mbyte -> lo porto in bit
        double pckt_size = app.getPacket_size() * 8; //porto il valore in bit
        int numero_pckt = (int) Math.ceil(file_size / pckt_size); // Ritorna Intero superiore
        double rate = app.getRate() * 1000; //Il rate solitamente è fornito in Kbit/sec
        int packet_inter_delay = (int)((pckt_size/rate) * 1000); //riporto interdelay in ms
        if (app.getTipo().toLowerCase().contains("source")) {
            //TODO : CONTROLLARE TIPO TRASPORTO SE TCP INIZIALIZZARE LA TRASMISSIONE CON PROTOCOLLO TCP
System.out.println("I:"+this.getTipo()+" : INIZIO GENERAZIONE PACCHETTI ("+app.getPacket_size()+"Byte) : Da generare:"+numero_pckt);
            //PREPARARE IL TRASPORTO : FRAMMENTAZIONE INFORMAZIONE IN MSS
            int tempo = app.getStart();
            for(int i = 0; i< numero_pckt;i++)
            {
                Messaggi m = new Messaggi("applicazione", this, this.myTransportLayer, dest, tempo);
                current_seq_no++;
                m.ID = current_seq_no;
                m.isData = true;
                m.setNodoSorgente(this);
                m.setApplication_port(app.getPort());
                m.setSize(app.getPacket_size());
                m.saliPilaProtocollare = false; //Il messaggio deve partire dal livello traporto e scendere nella pila
                tempo = tempo + packet_inter_delay; //tempo di lancio del prossimo paccketto in ms
                s.insertMessage(m);
            }
System.out.println("I:"+this.getTipo()+" : FINE GENERAZIONE PACCHETTI ("+app.getPacket_size()+"Byte) : Generati:"+numero_pckt);            
        }
    }

}
