/*
 * dataLinkLayer.java
 *
 * Created on 10 ottobre 2007, 8.44
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
/**
 *
 * @author franco
 */
package base_simulator.layers;

import base_simulator.*;


public class LinkLayer extends Entita{
    
    /** Creates a new instance of dataLinkLayer */
    protected scheduler s;
    protected double tempo_processamento;
    protected NetworkLayer netLayer;
    protected physicalLayer phyLayer;
    protected Object nodo;
    
    public LinkLayer(scheduler s,double tempo_processamento) {
        super(s,"Link Layer");
        this.s = s;
        this.tempo_processamento = tempo_processamento;
        this.netLayer = null;
    }
    public void connectLinkLayer(physicalLayer phyLayer,NetworkLayer netLayer,Object nodo){
        this.nodo = nodo;
        this.phyLayer = phyLayer;
        this.netLayer = netLayer;
    }
            
    public void setlinkLayer(physicalLayer phyLayer){
        this.phyLayer = phyLayer;
    }
    public void Handler(Messaggi m){
        //System.out.println("\nMessaggio arrivato a LinkLayer");
        if(m.saliPilaProtocollare)
            inviaANetworkLayer(m);
        else inviaAphysicalLayer(m);
    }

    private void inviaANetworkLayer(Messaggi m) {
        //System.out.println("   Dal LinkLayer ---> network");
        m.shifta(tempo_processamento);
        m.setSorgente(this);
        m.setDestinazione(netLayer);
        s.insertMessage(m);
    }
    
    private void inviaAphysicalLayer(Messaggi m) {
        //System.out.println("   Dal LinkLayer ---> fisico");
        m.shifta(tempo_processamento);
        m.setSorgente(this);
        m.setDestinazione(phyLayer);
        s.insertMessage(m);
    }
    
    
}
