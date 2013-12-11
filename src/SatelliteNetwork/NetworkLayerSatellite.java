/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package SatelliteNetwork;
import base_simulator.*;
import base_simulator.layers.*;
import java.util.*;
import test1.nodo_hap;
import test1.nodo_host;

/**
 *
 * @author franco
 * Questa classe viene utilizzata per simulare il livello network del satellite che si dovrà occupare della trasmissione dei pacchetti all'interno del sistema
 * e della gestione del protocollo di routing nonchè di comunicazione con i diversi nodi della rete
 */
public class NetworkLayerSatellite extends NetworkLayer{
    private Vector<Spot> spots;
   
    private float periodo_controlla_coda;
    private LinkedList ListaServizi;
    private LinkedList gruppiMulticast;
    
    //Inizio variabili statistiche richieste
    float numero_richieste = 0;
    float numero_richieste_accettate = 0;
    float numero_richieste_rifiutate = 0;
    //Inizio variabili statistiche pkts
    float numero_pkt_prt = 0;
    float numero_pkt_dati = 0;
    float delay_pkt_prt =0;
    float delay_pkt_dati = 0;
    private Vector<Integer> hapAttive;
    private int servizi_disponibili = 0;
    private int servizi_terminati = 0;
 
    private boolean updated = false;
    private double periodoInfoServices;

    public void setPeriodoInfoServices(double periodoInfoServices) {
        this.periodoInfoServices = periodoInfoServices;
    }
    
    
    
    public NetworkLayerSatellite(scheduler s,double tempo_di_processamento,double periodoInfoServices){
        super(s,tempo_di_processamento);
        this.periodoInfoServices=periodoInfoServices;
        this.periodo_controlla_coda = 1000; //1 secondo
        this.hapAttive = new Vector<Integer>();
        //Genero il messaggio di controlla coda
        Messaggi m = new Messaggi("controlla_coda",this,this,this,s.orologio.getCurrent_Time()+this.periodo_controlla_coda);
        s.insertMessage(m);
        Messaggi m1 = new Messaggi("infoServices",this,this,this,s.orologio.getCurrent_Time()+periodoInfoServices);
        s.insertMessage(m1);
        this.ListaServizi = new LinkedList();
        gruppiMulticast = new LinkedList();
        
        /* Possiamo accedere a tutti i terminali del satellite coperti dai diversi spot*/
    }
    
    public void addSpots(Vector<Spot> spots){
        this.spots=spots;
    }
    
    /**
     * Questo metodo mi permette di inviare un determinato messaggio verso tutti i terminali connessi al satellite
     * @param id_spot
     * @param m
     */
    public void inviaBroadcastSuSpot(int id_spot,Messaggi m){
        
        boolean trovato=false;
        for(int i = 0;i<spots.size() && !trovato;i++){
            //controllo se lo spot che stavo ricercando per inviare i messaggi è il corrente
            if(spots.elementAt(i).id_spot ==id_spot){
                trovato = true;
                Vector<Nodo> nodi = spots.elementAt(i).getcoveredNodes();
                //Invio verso tutti i nodi che lo spot copre il messaggio in broadcast
                for(int j = 0;j<nodi.size();j++){
                    Messaggi m1 =(Messaggi)m.clone();
                    m1.shifta(tempo_di_processamento*(j+1));
                    //Scendo di livello verso il link layer
                    m1.setDestinazione(this.linkLayer);
                    m1.saliPilaProtocollare=false;
                    /**E' importante settare questo nodo perch� sar� il nodo destinatario del messaggio
                    *Nel caso di routing e di multi-hop saranno dei nodi presenti nella lista successivi da settare con nextHop*/
                    m1.setNextHop(nodi.elementAt(j));
                    m1.setSorgente(this);
                    s.insertMessage(m1);
                }
            }
        }
    }
    
    //Invio in broadcast su tutti gli spot
    public void inviaBroadcast(Messaggi m) {
        for (int i = 0; i < spots.size(); i++) {
            //controllo se lo spot che stavo ricercando per inviare i messaggi è il corrente
            Vector<Nodo> nodi = spots.elementAt(i).getcoveredNodes();
            //Invio verso tutti i nodi che lo spot copre il messaggio in broadcast
            for (int j = 0; j < nodi.size(); j++) {
                Messaggi m1 = (Messaggi) m.clone();
                m1.shifta(tempo_di_processamento * (j + 1));
                //Scendo di livello verso il link layer
                m1.setDestinazione(this.linkLayer);
                m1.saliPilaProtocollare = false;
                /**E' importante settare questo nodo perch� sar� il nodo destinatario del messaggio
                 *Nel caso di routing e di multi-hop saranno dei nodi presenti nella lista successivi da settare con nextHop*/
                m1.setNextHop(nodi.elementAt(j));
                m1.setDestinazione(nodi.elementAt(j));
                m1.setSorgente(this);
                s.insertMessage(m1);
            }
        }
    }
    
    @Override
    public void Handler(Messaggi m){
        if(!m.isData){
            //Il messaggio e' di protocollo
            gestisciPacchettoProtocollo(m);
             if(!m.getTipo_Messaggio().equals("controlla_coda")){
                this.numero_pkt_prt++;
                this.delay_pkt_prt+=Float.valueOf(""+(s.orologio.getCurrent_Time()-m.getTempo_di_partenza())).floatValue();
             }
        }
        else{
            //E' un messaggio dati quindi mettiamo i messaggi in coda nel buffer
            gestisciPacchettoDati(m);
            this.numero_pkt_dati++;
            this.delay_pkt_dati+=Float.valueOf(""+(s.orologio.getCurrent_Time()-m.getTempo_di_partenza())).floatValue();
        }
    }
    
    @Override
    public void gestisciPacchettoProtocollo(Messaggi m){
        
        if(m.getTipo_Messaggio().equals("controlla_coda")){
            spedisciPacchetti();
        }
        else if(m.getTipo_Messaggio().equals("msgHapSat")){
            
            System.out.println("Messaggio "+m.getTipo_Messaggio()+" giunto al nodo satellite correttamente dal nodo sorgente :");
            
            int id_nodo =((Nodo)m.getNodoSorgente()).getId();
            int id_spot = ritornaIdSpot(id_nodo);
            
            /*--------------------------------------Inizializzo il messaggio di risposta all'HAP-----------------------------*/
            m.setTipo_Messaggio("msgSatHap");
            m.setDati(id_spot);
            m.shifta(tempo_di_processamento);
            m.setNodoDestinazione(m.getNodoSorgente());
            m.setDestinazione(this.linkLayer);
            m.saliPilaProtocollare=false;
            m.setSorgente(this);
            m.setNodoSorgente(nodo);
            //---------------------------------------------------------------------------------------------------------------------------------------
            
            //Controllo se posso accettare la connessione proveniente dall'hap che ne ha fatto richiesta
            if(esitoTestAvaiability(m.rate,id_spot)){
                m.setData("connessione accettata");
                numero_richieste++;
                numero_richieste_accettate++;    
                controlloHapGiaAttiva(id_nodo);
            }
            else{
                m.setData("connessione rifiutata");
                numero_richieste++;
                numero_richieste_rifiutate++;
            }
            s.insertMessage(m);
            
        }
        else if(m.getTipo_Messaggio().equals("infoServices")){
            //Questo messaggio � periodico e devo inviarlo nel caso ci siano modifiche alla mia tabella dei servizi
            if(updated){
                double startTime = s.orologio.getCurrent_Time();
                Messaggi m1 = new Messaggi(m.getTipo_Messaggio(),nodo,this.linkLayer,this,startTime);
                m1.isData=false;
                m1.isBroadCast();
                m1.setData(ListaServizi);
                this.inviaBroadcast(m1);
            }
            updated = false;
            m.shifta(periodoInfoServices);
            s.insertMessage(m);
        }
        else if(m.getTipo_Messaggio().equals("newServiceAvaiable")){
            //E' arrivato un messaggio di service avaiability da un hap devo a questo punto inserirlo nella lista dei servizi attivi
            Servizi sv = ((Servizi)m.getData());
            System.out.println("Un nuovo servizio � disponibile "+sv.getId_servizio()+" di tipo "+sv.getTipo()+" proveniente da host id "+sv.getSorgente().getId());
            ListaServizi.add(sv);
            this.servizi_disponibili++;
            updated = true;
        }
        else if(m.getTipo_Messaggio().equals("ServiceNotMoreAvaiable")){
            //E' arrivato un messaggio di service Not more avaiability da un hap devo a questo punto inserirlo nella lista dei servizi attivi
            Servizi sv = ((Servizi)m.getData());
            System.out.println("Un Servizio � Terminato "+sv.getId_servizio()+" di tipo "+sv.getTipo()+" proveniente da host id "+sv.getSorgente().getId());
            eliminaServizio(sv);
            this.servizi_terminati++;
            updated = true;
        }
        else if(m.getTipo_Messaggio().equals("globalSearch")){
            /*Devo gestire il messaggio di ricerca percorso per un determinato servizio
             *  Gestione non completa da completare con urgenza
             */ 
            
            Servizi srv = (Servizi)m.getData();
            System.out.println("Ricerca Globale per il tipo di servizio ....:"+srv.getTipo()+" Sessione multicast con ID:"+srv.getId_servizio());
            //Prendo le informazioni dal grafo presente nello scheduler
            Grafo g = s.getGrafo();
            nodo_hap hSorgente = ((nodo_hap)m.getNodoSorgente());
            int id_hap = hSorgente.getId();
            System.out.println("L'hap che ha inviato la richiesta ha ID ..:"+id_hap);
            /******************************TUTTO BENE***********************************/
            
            
            Grafo g_m = this.calcolaPercorso(srv, g);
            
            //Una volta ottenuto il grafo posso inviare il messaggio di bid verso i nodi che faranno parte del percorso
            
            Nodo nextNodo = getNextNodo(((NodoSatellite)this.nodo).getId(),g_m); //questo � da perfezionare non pienamente funzionante
            Messaggi m1 = new Messaggi("bid",this,this.linkLayer,nextNodo,s.orologio.getCurrent_Time());
            m1.setNextHop(nextNodo);
            s.insertMessage(m1);
        }
        
        else if(m.isBroadCast()){
                //Il messaggio e' di protocollo e deve essere inviato in broadcast verso gli utenti di uno o più spot
//TODO : COMMENTATO IN DATA 15.11.2013 cambiata gestione pacchetto        	
//                Vector<Object>spots_m = m.getSpots();
                /**Invio in broadcast verso tutti gli spot indicati nel messaggio m*/ 
        	//                for(int i = 0;i<spots_m.size();i++){
        	//      Spot s_m = (Spot)spots_m.elementAt(i);
        	//      this.inviaBroadcastSuSpot(s_m.id_spot, m);
        	//   }   
         }
        
         else{
                 //Il pacchetto deve essere inviato verso uno specifico terminale appartenente ad uno specifico spot
                int id_nodo_destinazione = ((Nodo)m.getNodoDestinazione()).returnID();
                int id_spot = this.ritornaIdSpot(id_nodo_destinazione);
                if(id_spot>=0){
                    this.inviaVersoNodo(id_spot, m);
                }
         }
    }
    
    @Override
    public void gestisciPacchettoDati(Messaggi m) {
        //Inseriamo il messaggio nella coda dei messaggi
        int id_nodo_destinazione = ((Nodo) m.getNodoDestinazione()).returnID();

        int id_spot = this.ritornaIdSpot(id_nodo_destinazione);
        
        Spot sp = null;
        if (id_spot >= 0) {
            sp = spots.elementAt(id_spot);
        }
        if (m.classe_di_traffico.equals("CBR")) {
            sp.addCBRpkt(m);
        } else if (m.classe_di_traffico.equals("VBR")) {
            sp.addVBRpkt(m);
        } else { // E' di tipo FCA
            sp.addFCApkt(m);
        }
    }

//Questo metodo � delicato e deve aiutarmi a scegliere al meglio il percorso per arrivare alla destinazione
//abbiamo cmq un problema legato al fatto dell'' albero parziale costruito in precedenza per raggiungere le altre destinazioni
//Una soluzione potrebbe essere quella di ricalcolare completamente il grafo, ma renderebbe pi� complicata la gestione del protocollo
//Una seconda soluzione � quella di costruire solo la parte mancante del grafo e quindi lavorare con meno nodi, ma anche qui la gestione non
//sarebbe semplice per la gestione dell'' alfgoritmo di routing
    
    private Grafo calcolaPercorso(Servizi srv, Grafo g) {
        Grafo gm = soluzioneIbrida(g,srv);
        nodo_host destinazione = srv.getDestinazioneDaAggiungere();
        //devo trovare l'albero che mi permetta di raggiungere il nodo destinazione
        return gm;
    }

    private void controlloHapGiaAttiva(int id_nodo) {
        boolean trovato = false;
        for(int i = 0;i<hapAttive.size();i++){
            if(hapAttive.elementAt(i)==id_nodo)
                trovato = true;
        }
        if(!trovato) {
            hapAttive.add(id_nodo);
        }
    }

    private void eliminaServizio(Servizi sv) {
        boolean trovato = false;
        int cont = 0;
        Iterator i = ListaServizi.iterator();
        while(i.hasNext() && !trovato){
            Servizi svLocal = (Servizi)i.next();
            if(svLocal.getId_servizio()==sv.getId_servizio()){
                trovato = true;
            }
            else cont++;
        }
        if(trovato)
            ListaServizi.remove(cont);
    }

    /**
     * Questo metodo pu� implementare il meccanismo di CAC, per adesso molto semplificato
     * @param rate
     * @param id_spot
     * @return
     */
    private boolean esitoTestAvaiability(double rate,int id_spot) {
       boolean e = false;
       double capacitaResidua = this.spots.elementAt(id_spot).upChannel.elementAt(0).returnCapacitaResidua()-rate;
       if(capacitaResidua>=0){
           e = true;
          //devo allocare la banda per la connessione accettata
           this.spots.elementAt(id_spot).upChannel.elementAt(0).allocaBanda(rate);
       }
       return e;
    }

    private int getHapAttive() {
        return this.hapAttive.size();
    }

    private float getHapMedieSpot() {
         float numeroHap = 0;
        for(int i = 0;i<this.spots.size();i++)
            numeroHap+=this.spots.elementAt(i).coveredTerminals.size();
         numeroHap/=this.spots.size();
         return numeroHap;
    }

    private int getHapTotali() {
        int numeroHap = 0;
        for(int i = 0;i<this.spots.size();i++)
            numeroHap+=this.spots.elementAt(i).coveredTerminals.size();
        return numeroHap;
    }

    private Nodo getNextNodo(int id, Grafo g_m) {
        return null;
        //Questo metodo ritorna l'id dell'ultimo nodo on-tree cos� da raggiungere i nodi non-on tree e allocare le necessarie risorse
        
    }

    

    /**
     * Ritorna il gruppo multicast dando come input l'id del servizio
     * @param id_servizio
     * @return
     */
    private gruppoMulticastSat giveMeMG(int id_servizio) {
        Iterator i = gruppiMulticast.iterator();
        
        while(i.hasNext()){
            gruppoMulticastSat gm = (gruppoMulticastSat)i.next();
            if(id_servizio == gm.getIdServizio()){        
                return gm;
            }
        }
        return null;
    }

    private boolean gruppoAttivo(int id_servizio) {
        boolean trovato = false;
        Iterator i = gruppiMulticast.iterator();
        while (i.hasNext() && !trovato){
            gruppoMulticastSat gm = (gruppoMulticastSat)i.next();
            if(id_servizio == gm.getIdServizio())
                trovato = true;
        }
        return trovato;
    }

    private int ritornaIdSpot(int id_nodo) {
        boolean trovato = false;
        int id_spot = -1;
        for (int i = 0; i < spots.size() && !trovato; i++) {
            Nodo n = spots.elementAt(i).getTerminal_id(id_nodo);
            if (n != null) {
                id_spot = i;
                trovato = true;
            }
        }
        return id_spot;
    }
    
    /**
     * Questo metodo e' complementare al metodo invia broadcast e permette di raggiungere uno specifico nodo
     * @param id_spot
     * @param m
     */
    private void inviaVersoNodo(int id_spot, Messaggi m) {
        Messaggi m1 = (Messaggi) m.clone();
        m1.shifta(tempo_di_processamento);
        //Scendo di livello verso il link layer
        m1.setDestinazione(this.linkLayer);
        m1.saliPilaProtocollare = false;
        /**E' importante settare questo nodo perchè sarà il nodo destinatario del messaggio
         *Nel caso di routing e di multi-hop saranno dei nodi presenti nella lista successivi da settare con nextHop*/
        m1.setNextHop(m.getNodoDestinazione());
        m1.setSorgente(this);
        s.insertMessage(m1);
    }

    /**
     * Questo metodo mi permette di generare un nuovo grafo composto nel seguente modo
     * parte del grafo viene creato utilizzando i link gi� presenti nella soluzione unendo i nodi on-tree
     * in modo da formare un albero; la seconda parte del grafo viene composta utilizzando tutti i link a disposizione per connettere
     * i nodi che non sono on-tree
     * @param g
     * @return
     */
    private Grafo soluzioneIbrida(Grafo g, Servizi srv) {
        Grafo ibrido = new Grafo(g.getN());
        int id_servizio = ((Servizi)srv).getId_servizio();
        if(gruppoAttivo(id_servizio)){//Vuol dire che il servizio � gi� attivo, e quanto meno, esiste gi� qualche nodo on-tree
            //Mi faccio restituire l'albero attivato mediante la linked list
            gruppoMulticastSat gmsat = giveMeMG(id_servizio);
            Grafo gMulticastCorrente = gmsat.getGrafo();
            //gmsat.nodoOnTree(n);
        }
        else //Vuol dire che nessun nodo ancora fa parte della soluzione quindi devo cotruire il grafo da zero 
            ibrido = s.getGrafo();
        return ibrido;
    }

    private void spedisciPacchetti() {
        //Devo calcolare la banda disponibile per spedire sullo spot
        System.out.println("Sono in network layer satellite e dovrei spedire un pacchetto dalla coda");
    }
    
    public String getStatisticheSatellite(){
        String s = "\n*************Inizio Elenco Dati Satellite NetworkLayer********************";
        s+="\nNumero di Spot (Nr): "+this.spots.size();
        s+="\nNumero di Hap Collegate totali  (Nr) : "+getHapTotali();
        s+="\nNumero di Hap Attivate (Nr):"+getHapAttive();
        s+="\nNumero di Hap medio collegate per spot (Nr): "+getHapMedieSpot();
        s+="\nServizi Attivati e disponibili (Nr): "+servizi_disponibili;
        s+="\nServizi Terminati (Nr): "+servizi_terminati;
        s+="\nNumero di Richieste (Nr): "+numero_richieste;
        s+="\nNumero di richieste Accettate (Nr): "+numero_richieste_accettate;
        s+="\nNumero di richieste rifiutate (Nr): "+numero_richieste_rifiutate;
        s+="\nPerc richieste Accettate (%): "+numero_richieste_accettate/numero_richieste;
        s+="\nPerc richieste Rifiutate (%): "+numero_richieste_rifiutate/numero_richieste;
        s+="\nNumero pkts totali (Nr): "+numero_pkt_prt+numero_pkt_dati;
        s+="\nNumero pkts Protocollo (Nr): "+numero_pkt_prt;
        s+="\nNumero pkts Dati (Nr): "+numero_pkt_dati;
        s+="\nOverHead (%): "+100*(numero_pkt_prt/(numero_pkt_dati+numero_pkt_prt));
        s+="\nDelay medio pkts di protocollo (ms): "+delay_pkt_prt/numero_pkt_prt;
        if(numero_pkt_dati>0){
            s+="\nDelay medio pkts di Dati (ms): "+delay_pkt_prt/numero_pkt_dati;
           
        }
        return s;
    }
}
