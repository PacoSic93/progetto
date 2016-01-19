/**************************
 * @author Amilcare Francesco Santamaria
 **************************/
 
package base_simulator;


import java.io.IOException;
import java.util.*;
import base_simulator.layers.*;






public class Nodo extends Entita{
    
    /*Scheduler del simulatore*/
    public scheduler  s;
    /*Inizio variabili proprie del nodo*/
    protected int id_nodo;
    
    //Canali connessi alle interfacce
    protected Vector<canale> myChannels;
    
    
    protected physicalLayer myPhyLayer;
    protected LinkLayer myLinkLayer;
    protected NetworkLayer myNetLayer;
    
    //Grafo della rete
    protected Grafo network;
    
    public Nodo(scheduler s,int id_nodo,physicalLayer myPhyLayer,LinkLayer myLinkLayer,NetworkLayer myNetLayer,Grafo network,String tipo){
        super(s,tipo);
        this.s = s;
        this.id_nodo = id_nodo;
        myChannels = new Vector<canale>();
        this.network = network;
       
        
        this.myNetLayer = myNetLayer;
        this.myLinkLayer = myLinkLayer;
        this.myPhyLayer = myPhyLayer;
        
        /*Connetto i tre livelli della pila tra loro e con il nodo*/
        this.myPhyLayer.connectPhysicalLayer(this.myLinkLayer,this);
        this.myLinkLayer.connectLinkLayer(this.myPhyLayer,this.myNetLayer,this);
        this.myNetLayer.connectNetworkLayer(this.myLinkLayer,this);
    }
    
    /**Questo netodo andrà esteso dalle classi figlie e serve per gestire
       i comportamenti del nodo all'arrivo di un messaggio*/
    public void Handler(Messaggi m){
               
    }
    
    public int returnID(){
        return this.id_nodo;
    }
    public void setId(int id) {
        this.id_nodo = id;
    }
    public int getId(){
        return this.id_nodo;
    }
    
    public String toString(){
        return ""+this.tipo+"["+this.id_nodo+"]";
    }
    
    /**setto i canali di uscita del nodo
     */
    public void setChannels(Vector<canale> channels) {
        for(int i = 0;i<=channels.size();i++)
            this.myChannels.addElement(channels.elementAt(i));
    }
    
    private void inviaMessaggioACanale(Messaggi m) {
        
    }
    
    private void inviaMessaggioAPhyLayer(Messaggi m) {
       
    }
    public Grafo getNetwork(){
        return this.network;
    }

    
    
    
    
    
    
    
    
}//class
