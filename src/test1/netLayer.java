/*
 * netLayer.java
 *
 * Created on 10 ottobre 2007, 11.16
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test1;
import SatelliteNetwork.Servizi;
import base_simulator.*;
import base_simulator.layers.*;
import java.util.*;

/**
 *
 * @author franco
 */
public class netLayer extends NetworkLayer{
    
    
    /** Creates a new instance of netLayer */
    public netLayer(scheduler s, double tempo_processamento) {
        super(s,tempo_processamento);
    }
    
    public void Handler(Messaggi m){
        //System.out.println("\nMessaggio giunto al NetworkLayer del nodo host");
        if(m.isData)
            gestisciPktDati(m);
        else
            gestisciPktProtocollo(m);
    }

    private void gestisciMessaggioDisconnessione(Messaggi m) {
        if(m.saliPilaProtocollare){
            //Il messaggio è di ritorno quindi devo terminare il ciclo
            System.out.println("Il messaggio di disconnessione da hap ha raggiunto il nodo, posso terminare la sequenza");
        }
        else if(!m.saliPilaProtocollare){
            //Vuol dire che il messaggio sta partendo e deve raggiungere il nodo destinazione quindi la mia hap
            Messaggi m1 = (Messaggi)m.clone();
            m1.saliPilaProtocollare = false;
            m1.shifta(this.tempo_di_processamento);
            m1.setSorgente(this);
            m1.setDestinazione(this.linkLayer);
            m1.setNextHop(m.getNextHop());
            s.insertMessage(m1);
        }
    }
    
    /**gestisce la ricezione dei pacchetti dati*/
    
    private void gestisciPktDati(Messaggi m) {
        
    }

    /**gestisce i messaggi di protocollo*/
    
    private void gestisciPktProtocollo(Messaggi m) {
        //System.out.println(" Pacchetto di protocollo");
        if(m.getTipo_Messaggio().equals("msgTermHap")){
            gestisciPath(m);
        }
        else if(m.getTipo_Messaggio().equals("msgHapTerm")){
            gestisciEchoPath(m);
        }
        else if(m.getTipo_Messaggio().equals("msgDisconnectFromHap")){
            gestisciMessaggioDisconnessione(m);
        }
        else if(m.getTipo_Messaggio().equals("attivaNodoRicevente")){
            ((nodo_host)nodo).setAttivatoRicezione(true);
        }
        else if(m.getTipo_Messaggio().equals("infoServices")){
            System.out.println("nodo host"+((nodo_host)this.nodo).getId()+" ha ricevuto il msg di infoServices di tipo "+((nodo_host)this.nodo).getTipoHost());
            if(((nodo_host)nodo).isAttivatoRicezione()){
                //Gestione scelta servizi
                if(!((nodo_host)nodo).isEInRicezione()) //Controllo se il nodo ha gi� una connessione attivata o meno
                    sceltaServizi((LinkedList)m.getData());
                else System.out.println("Il nodo impegnato in una comunicazione multicast");
            }
            else if(!((nodo_host)nodo).isAttivatoRicezione()){
                //devo controllare il tempo corrente e il tempo di attivazione se maggiore allora attivo e
                System.out.println("Il nodo non puo ancora ricevere servizi");
           }
        }
    }

    private void gestisciPath(Messaggi m) {
        if(m.saliPilaProtocollare){
            System.out.println(" E' giunto il path finale");
            /*Indica che il messaggio di path è arrivato e devo rispondere*/
            
            m.saliPilaProtocollare = false; //Il messaggio verrà inviato sulla pila
            m.shifta(this.tempo_di_processamento);
            m.setTipo_Messaggio("msgHapTerm");
            m.setSorgente(this);
            m.setDestinazione(this.linkLayer);
            m.setNodoDestinazione(m.getNodoSorgente());
            m.setNodoSorgente(this.nodo);
            m.lista_successivi = new Vector<Object>();
            m.lista_precedenti = new Vector<Object>();            
            Object o =s.trovaNodoApp(((nodo_host)this.nodo).getId());
            System.out.println("\nnodo host :"+((nodo_host)this.nodo).getId());
            System.out.println("\nnodo Appartenenza :"+((nodo_hap)o).getId());
            m.setNextHop(o);
            m.isData = false;
            s.insertMessage(m);
        }
        else{
            /*devo procedere con l'invio del path sulla rete*/
            //System.out.println(" Invio un messaggio di PATH al link Layer");
            Messaggi m1 = (Messaggi)m.clone();
            m1.saliPilaProtocollare = false;
            m1.shifta(this.tempo_di_processamento);
            m1.setSorgente(this);
            m1.setDestinazione(this.linkLayer);
            m1.setNextHop(m.getNextHop());
            s.insertMessage(m1);
        }
            
    }
    
    private void gestisciEchoPath(Messaggi m){
        System.out.println("\nMessaggio di echo arrivato a destinazione");
    }

    /**
     * Questo metodo permette di selezionare un servizio tra quelli registrati 
     * @param linkedList lista contenente i servizi attivi
     */
    private void sceltaServizi(LinkedList linkedList) {
        
        //Implementazione del meccanismo che se gi� sto ricevendo non devo scegliere un altro servizio
        int dim_lista = linkedList.size();
        java.util.Random r = new java.util.Random();
        GregorianCalendar gc = new GregorianCalendar();
        r.setSeed(gc.getTimeInMillis());
        int service = Math.abs(r.nextInt()%dim_lista);
        Iterator it = linkedList.iterator();
        int cont=0;
        Servizi serv = null;
        
        while(it.hasNext() && cont<=service){
            cont++;
            serv = (Servizi)it.next();
        }
        
        System.out.println("Il nodo ha scelto il servizio con id :"+serv.getId_servizio());
        System.out.println("Di tipo :"+serv.getTipo());
        System.out.println("Rate    :"+serv.getRate());
        nodo_host source = (nodo_host)serv.getSorgente();
        System.out.println("Sorgente con id :"+source.getId()+" connessa all'hap :"+((nodo_hap)source.getMyRouter()).getId());
        ((nodo_host)nodo).setEInRicezione(true);
        
        /*Il nodo ha scelto il servizio deve ora assegnarsi al gruppoMulticast e quindi poter essere raggiunto dal flusso dati
         * 1. Invio la mia scelta alla mia hap e controllo se questa fa gia parte del gruppoMulticast (nodo_on_tree)
         *   1.a Se si mi collego direttamente al gruppo e inizio a ricevere in broadcast il servizio
         *   2.a Se la mia hap non � collegata al gruppo devo inviare la richiesta al nodo satellite il quale mi trover� la miglior strada possibile
         *      e alla fine alloco la banda sul link che va dall'hap a terra. Devo far attenzione anche al caso in cui potrebbe essere gia� arrivata una richiesta
         *      di connessione per il gruppoMulticast quindi devo aspettare ad aggiungermi ad una lista di waiting
         */
        Messaggi m = new Messaggi("join",this.nodo,this.linkLayer, ((nodo_host)this.nodo).getMyRouter(), s.orologio.getCurrent_Time());
        m.setNextHop(((nodo_host)this.nodo).getMyRouter());
        m.setNodoSorgente(nodo);
        m.setData(serv); //come data passo l'id del servizio che posso associare logicamente al gruppo multicast visto che si utilizza il paradigma uno-molti
        s.insertMessage(m); 
    }
    
    
}
