/*
 * nodo_hap.java
 *
 * Created on 9 ottobre 2007, 23.24
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   nodo_hap.java

package test1;

import SatelliteNetwork.NodoSatellite;
import base_simulator.*;
import base_simulator.layers.*;
import base_simulator.scheduler;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Vector;

public class nodo_hap extends Nodo
{

    public canale getMyChannels(int index) {
        return myChannels.get(index);
    }

    public void setMyChannels(ArrayList<canale> myChannels) {
        this.myChannels = myChannels;
    }

    
    public void addCanaleUplinkHost(canale hapUplink) {
        this.myChannels.add(hapUplink); //Avra'� indice 2 e indichera' il canale condiviso di upLink per gli host
    }

    public int getMySatIdSpot()
    {
        return mySatIdSpot;
    }

    public void setMySatIdSpot(int mySatIdSpot)
    {
        this.mySatIdSpot = mySatIdSpot;
    }

    public nodo_hap(scheduler s, int id_nodo, 
            physicalLayer myPhyLayer, LinkLayer myLinkLayer, NetworkLayer myNetLayer, TransportLayer myTransportLayer,
            Grafo network, NodoSatellite sat, 
            canale hapSat,double capacita,double dim_pacchetto,double tempo_propagazione)
    {
        super(s, id_nodo, myPhyLayer, myLinkLayer, myNetLayer, myTransportLayer,network, "Nodo hap");
        id_canale = 1;
        vicini = new Vector();
        this.sat = sat;
        r = new Random();
        this.capacita= capacita;
        this.dim_pacchetto = dim_pacchetto;
        this.tempo_propagazione = tempo_propagazione;
        tempo_Trasmissione = (dim_pacchetto * 8D) / capacita;
        Vector nodo = new Vector();
        Vector host = new Vector();
        canale c = hapSat;
        id_canale++;
        tempo_propagazione = 1.0D;
        canale c1 = new canale(s, id_canale, capacita, this, vicini, dim_pacchetto, tempo_propagazione);
        id_canale++;
        
        
        myChannels.add(c); //Canale di uplink condiviso con altre hap - indice 0
        myChannels.add(c1); //Canale di IHL mediante il quale parlo con le altre hap - indice 1
        
    }

    public void Handler(Messaggi m)
    {
        if(m.saliPilaProtocollare)
            inviaMessaggioAPhyLayer(m);
        else
            inviaMessaggioACanale(m);
    }

    public void aggiungiVicinoHap(Object hap)
    {
        vicini.addElement(hap);
        ((canale)myChannels.get(1)).addNodoalCanale(hap);
    }

    public void aggiungiVicinoTerminaleTerrestre(Object nodo)
    {
        vicini.addElement(nodo);
        ((canale)myChannels.get(2)).addNodoalCanale(nodo);
    }
    /**
     * Questo metodo mi permette di allocare sul link hap-satellite anche nel caso ci sia stata 
     * gia'� un registrazione precedente
     * @param startTime
     * @param rateDaAllocare
     */ 
    public void registraAlSatellite(double startTime,double rateDaAllocare)
    {
        Messaggi m = new Messaggi("msgHapSat", this, myNetLayer, sat, startTime);
        m.setTempo_di_partenza(startTime);
        m.isData = false;
        m.rate = rateDaAllocare; 
        m.saliPilaProtocollare = false;
        s.insertMessage(m);
    }

    public NodoSatellite getSat() {
        return sat;
    }
    
    

    private void inviaMessaggioACanale(Messaggi m)
    {
        int ch[] = trovaCanale(m.getNextHop());
        if(m.getNextHop() == null)
            System.out.println("nullo");
        if(m.lista_successivi.size() > 0)
            m.lista_successivi.removeElementAt(0);
        if(ch[0] >= 0)
        {
            m.shifta(tempo_propagazione);
            m.setSorgente(this);
            int pos = ch[1];
            m.setDestinazione(myChannels.get(ch[0]));
            m.saliPilaProtocollare = true;
            s.insertMessage(m);
        }
    }

    /**
     * Da utilizzare quando ricevo un messaggio e lo devo inviare su per la pila protocollare
     * @param m messaggio da gestire
     */
    private void inviaMessaggioAPhyLayer(Messaggi m)
    {
        m.shifta(0.0D);
        m.setSorgente(this);
        m.setDestinazione(myPhyLayer);
        s.insertMessage(m);
    }

    private int[] trovaCanale(Object object)
    {
        boolean trovato = false;
        int k = 0;
        int ch[] = new int[2];
        ch[0] = -1;
        ch[1] = -1;
        for(int i = 0; i < myChannels.size() && !trovato; i++)
        {
            canale c = (canale)myChannels.get(i);
            for(int count = 0; !trovato && count < c.getSizeNodo2();)
                if(c.getNodo2at(count).equals(object))
                {
                    trovato = true;
                    ch[1] = count;
                    k = i;
                } else
                {
                    count++;
                }

        }

        if(trovato)
        {
            ch[0] = k;
            return ch;
        } else
        {
            System.out.println("\nCanale non trovato");
            return null;
        }
    }

    public int getId()
    {
        return super.getId();
    }

    public String getStat()
    {
        String s = myNetLayer.getStat();
        return s;
    }

    public int getNumberOfMyTerminals()
    {
        return ((canale)myChannels.get(2)).getSizeNodo2();
    }
    
    public ArrayList getMyHost(){
        ArrayList myHost = new ArrayList();
        System.out.println("Nodo HAP -->"+this.id_nodo);
        myHost = ((canale)myChannels.get(2)).getNodiSorgenti();
        return myHost;
    }

    private Vector vicini;
    private int id_canale;
    private double capacita;
    private double dim_pacchetto;
    private double tempo_propagazione;
    private double tempo_Trasmissione;
    private NodoSatellite sat;
    private int mySatIdSpot;
    Random r;
}
