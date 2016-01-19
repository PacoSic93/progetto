/*
 * physicalLayer.java
 *
 * Created on 10 ottobre 2007, 8.42
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author Amilcare Francesco Santamaria
 * @date 15.11.2013
 * 
 */

package base_simulator.layers;
import base_simulator.*;

public class physicalLayer extends Entita {
    
    protected scheduler s;
    protected double tempo_processamento_bit;
    protected LinkLayer linkLayer;
    /*Oggetto proprietario della pila che mi interfaccierà con il mondo esterno*/
    Object nodo;
    
    /** Creates a new instance of physicalLayer */
    public physicalLayer(scheduler s,double tempo_processamento_bit) {
        super(s,"Physical Layer");
        this.s = s;
        this.tempo_processamento_bit = tempo_processamento_bit;
        this.nodo = nodo;
    }
    
    public void connectPhysicalLayer(LinkLayer linkLayer,Object nodo) {
        this.linkLayer = linkLayer;
        this.nodo = nodo;
    }
    /**Metodo di default del phy layer
     Per gestire la correzzione di errore e la modulazione codifica etc
     basterà sovrascrivere i metodi di passaggio ai livelli successivi quindi
     *inviaAlinkLayer(Messaggi m)
     *inviaAnodo(Messaggi m)
     */
    public void Handler(Messaggi m)
    {
        //System.out.println("\nIl messaggio è arrivato a livello fisico");
        if(m.saliPilaProtocollare)
            inviaAlinkLayer(m);
        else inviaAnodo(m);
    }
    
    /*Invio il pacchetto al LinkLayer*/
    private void inviaAlinkLayer(Messaggi m){
        m.shifta(tempo_processamento_bit);
        m.setSorgente(this);
        m.setDestinazione(linkLayer);
        s.insertMessage(m);
    }

    /*La gestione interna è terminata posso passare il messaggio all'oggetto nodo che lo metterà sul canale*/
    private void inviaAnodo(Messaggi m) {
        m.shifta(0);
        m.setSorgente(this);
        //Invio il messaggio al box esterno che provvederà a mettere il messaggio sul canale fisico il quale poi invierà verso il nodo destinazione
        m.setDestinazione(nodo);
        m.saliPilaProtocollare = false;
        m.setNextHop(m.getNodoDestinazione());
        s.insertMessage(m);
    }
    
}
