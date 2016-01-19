/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package base_simulator;

import SatelliteNetwork.Servizi;
import java.util.Iterator;
import java.util.LinkedList;
import test1.nodo_host;

/**
 *
 * @author    : Amilcare Francesco Santamaria
 * @Last Edit : 15.11.2013
 * @Brief     : Gestione delle Tabelle di Routing dei nodi (Router e Host) per servizi Multicast
 */
public class tabellaRoutingMulticast {
     private Servizi servizio;
     private LinkedList hostsRiceventi;
     

    public void addNodoHost(nodo_host n) {
        hostsRiceventi.add(n);
    }

    public LinkedList getHostsRiceventi() {
        return hostsRiceventi;
    }

    public void setHostsRiceventi(LinkedList hostsRiceventi) {
        this.hostsRiceventi = (LinkedList) hostsRiceventi.clone();
    }

    public Servizi getServizio() {
        return servizio;
    }

    public void setServizio(Servizi servizio) {
        this.servizio = servizio;
    }

    public tabellaRoutingMulticast(Servizi servizio) {
        this.servizio = servizio;
        this.hostsRiceventi = new LinkedList();
    }

    public tabellaRoutingMulticast(Servizi servizio, LinkedList hostsRiceventi) {
        this.servizio = servizio;
        this.hostsRiceventi = (LinkedList) hostsRiceventi.clone();
    }
     
    /**
     * Questo metodo permette di elidere un host che sta ricevendo il servizio
     * @param id_nodo
     */
    public void eliminaHost(int id_nodo) {
        Iterator i = hostsRiceventi.iterator();
        boolean trovato = false;
        while(i.hasNext() && !trovato){
            nodo_host n = (nodo_host)i.next();
            if(n.getId()==id_nodo)
                trovato = true;
        }
        i.remove(); //rimuovo l'elemento appena ritornato con il comando next
    }
}
