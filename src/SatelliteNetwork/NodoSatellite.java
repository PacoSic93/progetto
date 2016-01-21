/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package SatelliteNetwork;

import base_simulator.*;
import base_simulator.layers.*;
import java.util.*;

/**
 *
 * @author franco
 */


public class NodoSatellite extends Nodo{
    /*Inizio Dichiarazioni varibilli globali del sistema*/    
    public int numero_spot;
    Vector<Spot> my_spot;    
    double tempo_trasmissione;
    
    public NodoSatellite(scheduler s,int id_nodo,
            physicalLayer myPhyLayer,LinkLayer myLinkLayer,NetworkLayerSatellite myNetLayer,TransportLayer myTransportLayer,
            Grafo network,String tipo,int numero_spot,double periodoInfoServices){
    
            super(s,id_nodo,myPhyLayer,myLinkLayer,myNetLayer,myTransportLayer,network,tipo);
            this.numero_spot = numero_spot;
            my_spot = new Vector<Spot>();
            /*Create the needed spots and put them into the spot tail*/ 
            for(int i = 0;i<numero_spot;i++){
                Spot spot = new Spot(s,"spot",i,this);
                my_spot.add(spot);
            }
            ((NetworkLayerSatellite)this.myNetLayer).addSpots(my_spot);
            ((NetworkLayerSatellite)this.myNetLayer).setPeriodoInfoServices(periodoInfoServices);
            //viene messo nullo perchè lo calcolo nel canale
            tempo_trasmissione = 0;
    }
    
    /**
     * Questo metodo permette di gestire l'handeler del nodo satellitare
     * @param m
     */
    
    @Override
    public void Handler(Messaggi m){
        if(m.saliPilaProtocollare){
            /*devo inviare il messaggio ai livelli superiori della mia pila*/
            inviaMessaggioAPhyLayer(m);
        } else{
            /*devo inviare il messaggio sul canale*/
            int id_nodo1 = ((Nodo) m.getNextHop()).getId();
            boolean trovato = false;
            int idS = 0;
            for(int i = 0;i<my_spot.size() && !trovato;i++){
                if(my_spot.elementAt(i).getTerminal_id(id_nodo1)!=null){
                    trovato = true; 
                    idS = i;
                }
            }
            if(trovato){
                inviaMessaggioACanale(m,idS);
            }
            else {System.out.println("****************************\nIl nodo ricercato non è coperto dai miei spot\n*****************************\n");}
        }
    }

    public void addCanaleUpLink(canale satUplink1, int i) {
         this.my_spot.elementAt(i).upChannel.add(satUplink1);
    }

    private void inviaMessaggioACanale(Messaggi m,int id_spot) {
        this.my_spot.elementAt(id_spot).inviaMessaggioACanale(m);
    }

    private void inviaMessaggioAPhyLayer(Messaggi m){
        m.shifta(0);
        m.setSorgente(this);
        m.setDestinazione(this.myPhyLayer);
        s.insertMessage(m);
    }
    
   
    /*aggiungo un nodo router vicino*/
    public void aggiungiVicinoRouter(Nodo nodo,int id_spot){
        //Aggiungo anche al canale che è contenuto nello spot
        this.my_spot.elementAt(id_spot).coveredTerminals.add(nodo);
    }
    
    @Override
    public String getStat(){
        String s = ((NetworkLayerSatellite)this.myNetLayer).getStatisticheSatellite();
        return s;
    }
}