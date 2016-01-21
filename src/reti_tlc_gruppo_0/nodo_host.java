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
public class nodo_host extends Nodo{
    private ArrayList<NetworkInterface> my_interface = null;
    private int gatewayId;
    
    private Infos info = null;

    public Infos getInfo() {
        return info;
    }

    public void setInfo(Infos info) {
        this.info = info;
    }
    private ArrayList<NetworkInterface> nics = new ArrayList<NetworkInterface>();
    public nodo_host(scheduler s, int id_nodo, 
            physicalLayer myPhyLayer, LinkLayer myLinkLayer, NetworkLayer myNetLayer,TransportLayer myTransportLayer,
            Grafo network, String tipo,int gtw) {
        super(s, id_nodo, myPhyLayer, myLinkLayer, myNetLayer,myTransportLayer, network, tipo);
        my_interface = new ArrayList<NetworkInterface>();
        gatewayId = gtw;
    }
    
    
    private void inviaMessaggioACanale(Messaggi m) {
        
        int interface_id = 0;//m.getOutcomingInterface(); //Devo settare interfaccia di uscita nel link layer?????
        int channel_id = my_interface.get(interface_id).getChannel_idx();
        canale current_channel = getChannel(channel_id);
        
        //Invio il messaggio al canale
        m.shifta(0);
        m.setSorgente(this);
        m.setDestinazione(current_channel);
        s.insertMessage(m);
        
        
    }
    
    private void inviaMessaggioAPhyLayer(Messaggi m) {
        m.shifta(0);
        m.setSorgente(this);
        m.setDestinazione(this.myPhyLayer);
        s.insertMessage(m);
    }
    
    public void Handler(Messaggi m){
       if(m.saliPilaProtocollare == true)
       {
           //il messaggio entra nel nodo e sarà elaborato al suo interno
           inviaMessaggioAPhyLayer(m);
       }
       else
       {
           //il messaggio dovrà essere propagato all'esterno
           inviaMessaggioACanale(m);
       }
    }

    public void addNIC(NetworkInterface nic) {
        this.nics.add(nic);
    }
    
    public NetworkInterface getNic(int idx)
    {
        return nics.get(idx);
    }

    int getGTW() {
        return this.gatewayId;
    }
    
    //Il nodo genera gli eventi di Applicazione che scenderanno al livello trasporto
    public void generaEventiApplicazione()
    {
        for(Object app : this.apps)
        {
            
            Applicazione item = (Applicazione)app;
            gestisciApplicazione(app);
            
        }
    }

    private void gestisciApplicazione(Applicazione app) {
        Nodo dest = info.getNodo(app.getDest);
        Messaggi m = new Messaggi("applicazione",this,dest,);
        m.setApplication_port();
    }
    
    
}
