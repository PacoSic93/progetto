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
import java.util.ArrayList;

/**
 *
 * @author Ing. Amilcare Francesco Santamaria
 */
public class netLayerLinkState extends NetworkLayer {

    final String HELLO_GREETINGS = "hello_pckt";
    final String HELLO_TIMEOUT_MSG = "hello_timeout";
    final int HELLO_TIMEOUT = 15000;

    final String UPDATE_RT_MSG = "update_routing_table";
    final int UPDATE_ROUTING_TABLE_COLLECTING_TIME = 2000;
    protected boolean tableIsChanged = false;

    public netLayerLinkState(scheduler s, double tempo_di_processamento, Grafo grafo) {
        super(s, tempo_di_processamento, grafo);
        initializeProtocol();

    }

    private void generateCollectingMessage() {
        Messaggi m = new Messaggi(UPDATE_RT_MSG, this, this, null, s.orologio.getCurrent_Time());
        m.isData = false;
        m.shifta(UPDATE_ROUTING_TABLE_COLLECTING_TIME);
        m.saliPilaProtocollare = false;

        s.insertMessage(m);
    }

    /**
     *
     * Viene gestito dalla classe che deve estendere il livello rete deve
     * gestire i messaggi di protocollo
     *
     * In questa versione gli hello_greetings sono anche utilizzati per
     * aggiornare la metrica del nodo In particolare misurando il loro ritardo
     * E2E sarà possibile aggiornare il peso nella tabella di routing
     *
     * @param m
     */
    @Override
    public void gestisciPacchettoProtocollo(Messaggi m) {
        if (m.getTipo_Messaggio().equals(HELLO_TIMEOUT_MSG)) {
            System.out.println("D:" + ((Nodo) super.nodo).getTipo() + ": T:" + s.orologio.getCurrent_Time() + ": Arrivato messagio di generazione HELLO");
            sendHelloGreetingMessage();
        } else if (m.getTipo_Messaggio().equals(this.HELLO_GREETINGS)) {
            System.out.println("D:" + ((Nodo) super.nodo).getTipo() + ": ID :" + ((Nodo) super.nodo).getId() + " T:" + s.orologio.getCurrent_Time() + ":Arrivato messaggio di HELLO");

            super.myRoutingTable.printTR();
            int id_nodo_sorgente = ((Nodo) m.getNodoSorgente()).getId();

            if (super.myRoutingTable.controllaPresenzaLinea(id_nodo_sorgente, id_nodo_sorgente) >= 0) {
                //Vuol dire che la linea è gia presente, dobbiamo aggiornare il peso
                double new_peso = super.s.orologio.getCurrent_Time() - m.getTempo_di_partenza();

                boolean esito = super.myRoutingTable.setPeso(id_nodo_sorgente, id_nodo_sorgente, new_peso);
                if(esito == true)
                   tableIsChanged = true;

            }

        } else if (m.getTipo_Messaggio().equals(UPDATE_RT_MSG)) {
            System.out.println("D:"+s.orologio.getCurrent_Time()+" Il nodo "+((Nodo)nodo).getId()+" è pronto ad eseguire algoritmo per aggiornare le TR ");
            if (tableIsChanged == true) {
                //Update routing table executing routing algorithm
                System.out.println("TR Aggiornata");
                tableIsChanged = false;
            }

            generateCollectingMessage();

        } else {
            System.out.println("Messaggio non gestibile da Questo Net invio alla classe super");
            super.gestisciPacchettoDati(m);
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
        m1.isData = false;
        m1.shifta(HELLO_TIMEOUT);
        m1.saliPilaProtocollare = false;
        s.insertMessage(m1);

    }

    private void generateHelloMessage() {
        Messaggi m1 = new Messaggi(HELLO_TIMEOUT_MSG, this, this, null, s.orologio.getCurrent_Time());
        m1.isData = false;
        m1.shifta(HELLO_TIMEOUT);
        m1.saliPilaProtocollare = false;
        s.insertMessage(m1);
    }

    private void initializeProtocol() {

        generateHelloMessage();
        generateCollectingMessage();

    }

    @Override
    public void Handler(Messaggi m) {
        if (!m.isData) {
            gestisciPacchettoProtocollo(m);
        } else {
            super.Handler(m);
        }
    }

}
