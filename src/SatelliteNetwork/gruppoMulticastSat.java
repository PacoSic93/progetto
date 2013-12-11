/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package SatelliteNetwork;

import base_simulator.Grafo;
import base_simulator.Nodo;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author utente
 */
public class gruppoMulticastSat {
    
    private Servizi servizio;
    private LinkedList nodiOnTree;
    private Grafo g;

    public gruppoMulticastSat(Servizi servizio,int dimensioneRete) throws CloneNotSupportedException {
        this.servizio = (Servizi) servizio.clone();
        g = new Grafo(dimensioneRete);
        nodiOnTree = new LinkedList();
    }
    public void addNodoOnTree(Nodo n){
        nodiOnTree.add(n);
    }
    
    public void setGrafo(Grafo g){
        this.g=g;
    }
    public Grafo getGrafo(){
        return g;
    }
    
    public boolean nodoOnTree(Nodo n){
        boolean trovato = false;
        Iterator i = nodiOnTree.iterator();
        while (i.hasNext() && !trovato){
            Nodo n1 = (Nodo)i.next();
            if(n1.getId() == n.getId())
                trovato = true;
        }
        return trovato;
    }
    
    public int getIdServizio(){
        
        return this.servizio.getId_servizio();
    }
}
