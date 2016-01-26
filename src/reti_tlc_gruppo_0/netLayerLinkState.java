/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reti_tlc_gruppo_0;

import base_simulator.Grafo;
import base_simulator.Messaggi;
import base_simulator.Nodo;
import base_simulator.layers.NetworkLayer;
import base_simulator.scheduler;
import base_simulator.tabellaRouting;
import java.util.ArrayList;

/**
 *
 * @author afsantamaria
 */
public class netLayerLinkState extends NetworkLayer {

    final String HELLO_GREETINGS = "hello_pckt";
    final String HELLO_TIMEOUT_MSG = "hello_timeout";
    final int HELLO_TIMEOUT = 15000;
    int TTL_LSA = 1;
    int seq_no = 0;
    private String LSA_MSG = "LSA";

    public int getTTL_LSA() {
        return TTL_LSA;
    }

    public void setTTL_LSA(int TTL_LSA) {
        this.TTL_LSA = TTL_LSA;
    }

    public boolean isHELLO_ENABLED() {
        return HELLO_ENABLED;
    }

    public void setHELLO_ENABLED(boolean HELLO_ENABLED) {
        this.HELLO_ENABLED = HELLO_ENABLED;
    }
    boolean HELLO_ENABLED = false;

    public netLayerLinkState(scheduler s, double tempo_di_processamento, Grafo grafo) {
        super(s, tempo_di_processamento, grafo);
        
        if(HELLO_ENABLED){
          initializeProtocol();
        }

    }

    /**
     *
     * Viene gestito dalla classe che deve estendere il livello rete deve
     * gestire i messaggi di protocollo
     *
     * @param m
     */
    @Override
    public void gestisciPacchettoProtocollo(Messaggi m) {
        if(m.getTipo_Messaggio().equals(HELLO_TIMEOUT_MSG))
        {
            System.out.println("D:"+((Nodo)super.nodo).getTipo()+": T:"+ s.orologio.getCurrent_Time()+": Arrivato messagio di generazione HELLO");
            sendHelloGreetingMessage();
        }
        else if(m.getTipo_Messaggio().equals(this.HELLO_GREETINGS))
        {            
            System.out.println("D:"+((Nodo)super.nodo).getTipo()+": ID :"+((Nodo)super.nodo).getId()+" T:"+ s.orologio.getCurrent_Time()+":Arrivato messaggio di HELLO");            
            //GESTIONE HELLO
        }
        
        else if (m.getTipo_Messaggio().equals("controlla coda")) {
            System.out.println("Messaggio di controlla coda");
        }
        
        
        else {
            super.nr_pkt_prt++;
            super.delay_medio_pkt_prt += (s.orologio.getCurrent_Time() - m.getTempo_di_partenza());
        }
    }

    private void sendHelloGreetingMessage() {
        ArrayList<Integer> nodes = super.myRoutingTable.getNeighbours();
        for (Object n : nodes) {
            Nodo node = ((Nodo) super.nodo).getInfo().getNodo((Integer) n);
            Messaggi m = new Messaggi(HELLO_GREETINGS, this, super.linkLayer, node, s.orologio.getCurrent_Time());
            m.setNodoSorgente(nodo);
            m.saliPilaProtocollare = false;
            m.setNextHop(node);
            m.setNextHop_id(node.getId());
            m.shifta(super.tempo_di_processamento);
            m.isData = false;

            s.insertMessage(m);
        }
        
        //prepare next messagge of hello
        Messaggi m1 = new Messaggi(HELLO_TIMEOUT_MSG, this, this, null, s.orologio.getCurrent_Time());
        m1.isData=false;
        m1.shifta(HELLO_TIMEOUT);
        m1.saliPilaProtocollare = false;
        s.insertMessage(m1);

    }

    private void initializeProtocol() {
        Messaggi m1 = new Messaggi(HELLO_TIMEOUT_MSG, this, this, null, s.orologio.getCurrent_Time());
        m1.isData=false;
        m1.shifta(HELLO_TIMEOUT);
        m1.saliPilaProtocollare = false;
        s.insertMessage(m1);
        
        sendLSA();

    }
    
    @Override
    public void Handler(Messaggi m)
    {
        if(!m.isData)
        {
            gestisciPacchettoProtocollo(m);
        }
        else
        {
            super.Handler(m);
        }
    }

    void enableFullOSPF() {
        HELLO_ENABLED = true;
    }

    private void sendLSA() {
        seq_no++;
        Nodo myNodo = ((Nodo)this.nodo);
        LSA_packet lsa = new LSA_packet(seq_no,((Nodo)this.nodo).getId(),
                myNodo.getId(),
                this.TTL_LSA,
                super.myRoutingTable);
               
//Devo inviare il messaggio in flooding sui nodi adiacenti
        ArrayList<Integer> nodes =  super.myRoutingTable.getNeighbours();
        for(Object node_id : nodes)
        {
            Nodo n = myNodo.getInfo().getNodo((Integer)node_id);
            Messaggi m = new Messaggi(LSA_MSG,this,super.linkLayer,n,s.orologio.getCurrent_Time());
            m.setNextHop_id((Integer)node_id);
            m.shifta(this.tempo_di_processamento);
            m.setData(lsa);
            m.isData = false;
            m.saliPilaProtocollare = false;
            
            s.insertMessage(m);
        }
    }
//TODO: Aggiungere gestione LSA nel gestisci pacchetto protocollo
}
