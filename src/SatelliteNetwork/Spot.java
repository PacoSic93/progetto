/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SatelliteNetwork;

/**
 *
 * @author franco
 * @version 1.0
 * Questa classe gestisce il singolo spot di un oggetto Nodo che può essere ad esempio un satellite oppure un HAP
 */
import base_simulator.*;
import base_simulator.layers.*;
import java.util.*;

public class Spot extends Entita {

    /** Id dello spot appartenente al nodo*/
    int id_spot;
    /**Temrinali di tipo Nodo che vengono coperti dallo spot*/
    Vector<Nodo> coveredTerminals;
    /**Il vettore dei canali in downlink*/
    Vector<canale> myChannels;
    /**Vettore canale Uplink*/
    Vector<canale> upChannel;
    private Vector<Messaggi> pacchettiCBR;
    private Vector<Messaggi> pacchettiVBR;
    private Vector<Messaggi> pacchettiFCA;
    /**Nodo di appartenenza dello spot, può essere utilizzato per indirizzare messaggi*/
    Nodo my_parent;
    /**Variabili per il canale*/
    double capacita = 1024000; //bps
    double dim_pacchetto = 1024; //byte
    double tempo_propagazione = 134;//mms
    double tempo_Trasmissione = (dim_pacchetto * 8) / capacita;

    public Spot(scheduler s, String tipo, int id_spot, Nodo my_parent) {
        super(s, tipo);
        this.id_spot = id_spot;
        myChannels = new Vector<canale>();
        upChannel = new Vector<canale>();
        this.coveredTerminals = new Vector<Nodo>();
        this.my_parent = my_parent;
        this.pacchettiCBR = new Vector<Messaggi>();
        this.pacchettiVBR = new Vector<Messaggi>();
        this.pacchettiFCA = new Vector<Messaggi>();

        //Devo Gestire il canale nello spot poichè ogni spot ha un canale di uplink e uno di downlink
       /*Utilizzo il canale di default*/

        Vector<Object> nodo = new Vector<Object>();
        //Canale tra routers
        int id_canale = 1;
        canale c = new canale(s, id_canale, capacita, this, nodo, dim_pacchetto, tempo_propagazione);
        this.myChannels.addElement(c);
    }

    public void addCBRpkt(Messaggi m) {
        this.pacchettiCBR.add(m);
    }

    public void addFCApkt(Messaggi m) {
        this.pacchettiFCA.add(m);
    }

    public void addVBRpkt(Messaggi m) {
        this.pacchettiVBR.add(m);
    }

    public void addTerminals(Nodo t) {
        this.coveredTerminals.add(t);
        this.myChannels.elementAt(0).addNodoalCanale(t);
    }

    /**
     * 
     * @param id_nodo
     * @return ritorna l'oggetto coperto che per id il valore id_nodo altrimenti ritornerà un oggetto nullo
     */
    public Nodo getTerminal_id(int id_nodo) {
        boolean trovato = false;
        Nodo n = null;
        for (int i = 0; i < coveredTerminals.size() && !trovato; i++) {
            if (coveredTerminals.elementAt(i).returnID() == id_nodo) {
                trovato = true;
                n = coveredTerminals.elementAt(i);
            }
        }
        return n;
    }

    /**
     * 
     * @param id_nodo
     * @return se il nodo è stato trovato o meno e quindi cancellato dalla lista
     * Questo metodo permette di rimuovere dalla lista dei nodi che sono coperti il nodo che ha id pari a id_nodo
     * se il nodo è stato trovato esse verrà rimosso e il meto ritornerà true, altrimenti se il metodo ritorna false
     * questo indica che il nodo non era coperto dallo spot
     */
    public boolean removeTerminal(int id_nodo) {
        boolean trovato = false;
        for (int i = 0; i < coveredTerminals.size() && !trovato; i++) {
            if (coveredTerminals.elementAt(i).returnID() == id_nodo) {
                trovato = true;
                this.coveredTerminals.removeElementAt(i);
            }
        }
        return trovato;
    }

    public Vector<Nodo> getcoveredNodes() {
        return this.coveredTerminals;
    }

    public void inviaMessaggioACanale(Messaggi m) {
            m.shifta(this.tempo_Trasmissione+this.tempo_propagazione);
            m.setSorgente(this);
            m.setDestinazione(myChannels.elementAt(0));
            m.saliPilaProtocollare = true;
            s.insertMessage(m);
    }

    /**
     * Ritorna l'indice del canale dove è collegato il nodo destinazione e 
     * la posizione nel canale dove è collegato
     */
    private int[] trovaCanale(Object object) {
        boolean trovato = false;
        int k = 0;
        int[] ch = new int[2];
        ch[0] = -1;
        ch[1] = -1;
        for (int i = 0; i < this.myChannels.size() && !trovato; i++) {
            canale c = myChannels.elementAt(i);
            int count = 0;
            while (!trovato && count < c.getSizeNodo2()) {
                if ((c.getNodo2at(count).equals(object))) {
                    trovato = true;
                    ch[1] = count;
                    k = i;
                } else {
                    count++;
                }
            }
        }
        if (trovato) {
            ch[0] = k;
            return ch;
        } else {
            System.out.println("\nCanale non trovato");
            return null;
        }
    }
}
