/*
 * TransportLayer.java
 *
 * Created on 10 ottobre 2007, 8.45
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
/**
 *
 * @author franco
 *
 */
package base_simulator.layers;



import base_simulator.*;

import java.util.LinkedList;
import java.util.ListIterator;



/** il seguent e livello ha il compito di instradare i pacchetti e di occuparsi del routing*/

public class NetworkLayer extends Entita{
    protected scheduler s;
    protected LinkLayer linkLayer;
    protected double tempo_di_processamento;
    protected Object nodo;
    protected TransportLayer transportLayer;
    
    //Tabella di instradamento
    protected tabellaRouting myRoutingTable;
    
    //Da utilizzare per calcolare i percorsi per raggiungere le destinazioni
    protected Decisioner decisioner; 
    
    /*Inizio variabili statistiche*/      
    private float nr_richieste_accettate = 0;
    private float nr_richieste_rifiutate = 0;
    private float nr_servizi_chiusi = 0;
    private float nr_servizi_aperti = 0;
    
    private int nr_pkt_prt = 0;
    private int nr_pkt_dati = 0;
    
    private float delay_medio_pkt_prt = 0;
    private float delay_medio_pkt_dati = 0;
    private float jitter_medio = 0;
    private double arrivo_pkt_prec;
    
    private int terminali_disconnessi = 0;
    private int terminali_attivati = 0;
    
    
    /** Creates a new instance of TransportLayer */
    public NetworkLayer(scheduler s,double tempo_di_processamento) {
        super(s,"Network Layer");
        this.s = s;
        this.tempo_di_processamento = tempo_di_processamento;
                
        this.myRoutingTable = new tabellaRouting();
    }
    
    /*Gestisce il pacchetto e il comportamento del nodo*/
    public void Handler(Messaggi m){
        //System.out.println("\nMessaggio giunto al NetworkLayer");
        if(m.isData)
            gestisciPacchettoDati(m);
        else
            gestisciPacchettoProtocollo(m);
    }
    
    /**Utilizzo questo metodo per connettere la pila protocollare*/
    public void connectNetworkLayer(TransportLayer transportLayer,LinkLayer linkLayer,Object nodo){
        this.linkLayer = linkLayer;
        this.nodo = nodo;
        this.transportLayer = transportLayer;
    }

    /**Viene gestito dalla classe che deve estendere il livello rete
       deve gestire la ricezione dei pacchetti dati*/
    public void gestisciPacchettoDati(Messaggi m) {
        System.out.println("\nE' arrivato un messaggio dati nel nodo a livello di rete");
        
        //Aggiorna Statistiche
        this.nr_pkt_dati++;
        this.delay_medio_pkt_dati +=Float.valueOf(""+(s.orologio.getCurrent_Time()-m.getTempo_di_partenza())).floatValue();
        this.jitter_medio +=s.orologio.getCurrent_Time()-arrivo_pkt_prec;
        this.arrivo_pkt_prec = s.orologio.getCurrent_Time();
        
        //TODO : Invia il pacchetto al livello superiore 
    }

    /**
     * 
     * Viene gestito dalla classe che deve estendere il livello rete
       deve gestire i messaggi di protocollo
    */
    public void gestisciPacchettoProtocollo(Messaggi m) {
        if (m.getTipo_Messaggio().equals("controlla coda")) {
            System.out.println("Messaggio di controlla coda");
        }
        else 
        {
            this.nr_pkt_prt++;
            this.delay_medio_pkt_prt +=(s.orologio.getCurrent_Time()-m.getTempo_di_partenza());            
        }
    }

           
    @Override
    public String getStat(){
    	
    	//TODO : Da aggiornare le statistiche del nodo
    	
        String s ="\n--->Statistiche network Layer";

        s+="\nNumero di richieste accettate (Nr):"+this.nr_richieste_accettate;
        s+="\nNumero di richieste rifiutate (Nr):"+this.nr_richieste_rifiutate;
        s+="\nNumero di terminali attivati (Nr):"+this.terminali_attivati;
        s+="\nNumero di terminali disconnessi (Nr):"+this.terminali_disconnessi;
        s+="\nNumero di servizi aperti (Nr) :"+this.nr_servizi_aperti;
        s+="\nNumero di servizi chiusi (Nr) :"+this.nr_servizi_chiusi;
        
        int pkt_tot = this.nr_pkt_prt+this.nr_pkt_dati;
        
        s+="\nNumero di pacchetti totali gestiti (Nr) :";
        s+="\nNumero di pacchetti prt gestiti (Nr) :"+this.nr_pkt_prt;
        s+="\nNumero di pacchetti dati gestiti (Nr) :"+this.nr_pkt_dati;
        if(pkt_tot>0)
            s+="\nPercentuale di OverHead (%) :"+(nr_pkt_prt/pkt_tot)*100;
        if(nr_pkt_prt>0)
            s+="\nDelay medio pkt protocolo (ms) :"+this.delay_medio_pkt_prt/nr_pkt_prt;
        if(nr_pkt_dati>1){
            s+="\nDelay medio pkt dati (ms) :"+this.delay_medio_pkt_dati/nr_pkt_dati;
            s+="\nDelay jitter Medio pkt dati (ms) :"+this.jitter_medio/(nr_pkt_dati-1);
        }
        return s;
    }   
}
