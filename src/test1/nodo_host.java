/*
 * nodo_host.java
 *
 * Created on 10 ottobre 2007, 10.39
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
public class nodo_host extends Nodo{
    
    /** Creates a new instance of nodo_host */
    private Object myRouter;
    /**mi deve indicare se e'� di tipo sorgente o destinazione*/
    private String tipoHost;
    /**Indica il tipo di servizio che distribuisce*/
    private String tipoServizioTX;
    private String tipoServizioRX;
    private Servizi servizio;
    private boolean attivatoRicezione = false; //Mi indica se il nodo puo'� ricevere o meno
    private boolean eInRicezione = false; //Indica che il  nodo e'� connesso o sta connettendosi ad un servizio multicast
    private double tempoAttivazione = 0;

    public boolean isEInRicezione() {
        return eInRicezione;
    }

    public void setEInRicezione(boolean eInRicezione) {
        this.eInRicezione = eInRicezione;
    }

    
    public boolean isAttivatoRicezione() {
        return attivatoRicezione;
    }

    public void setAttivatoRicezione(boolean attivatoRicezione) {
        this.attivatoRicezione = attivatoRicezione;
    }

    public double getTempoAttivazione() {
        return tempoAttivazione;
    }

    public void setTempoAttivazione(double tempoAttivazione) {
        this.tempoAttivazione = tempoAttivazione;
    }

    
    public String getTipoHost() {
        return tipoHost;
    }

    public void setTipoHost(String tipoHost) {
        this.tipoHost = tipoHost;
    }

    public String getTipoServizioTX() {
        return tipoServizioTX;
    }

    public void setTipoServizioTX(String tipoServizio) {
        this.tipoServizioTX = tipoServizio;
    }

    public String getTipoServizioRX() {
        return tipoServizioRX;
    }

    public void setTipoServizioRX(String tipoServizioRX) {
        this.tipoServizioRX = tipoServizioRX;
    }
    
    
    public Object getMyRouter() {
        return myRouter;
    }

    public void setMyRouter(Object myRouter) {
        this.myRouter = myRouter;
    }
    
    private double capacita; //= 1024000; //bps
    private double dim_pacchetto; //= 1024; //byte
    private double tempo_propagazione; //= 1; //mms
    private double rate;
    
    public nodo_host(scheduler s,int id_nodo,
            physicalLayer myPhyLayer,LinkLayer myLinkLayer,NetworkLayer myNetLayer,TransportLayer myTransportLayer,
            Object myRouter,canale canaleUplink,Grafo network,double rate) {
        
        super(s,id_nodo,myPhyLayer,myLinkLayer,myNetLayer,myTransportLayer,network,"nodo host");
        this.myRouter = myRouter;
        this.rate = rate;
        this.myChannels.add(canaleUplink);
    }
    
    public void Handler(Messaggi m){
        
        
        if(m.saliPilaProtocollare){
            /*devo inviare il messaggio ai livelli superiori della mia pila*/
            inviaMessaggioAPhyLayer(m);
        } else{
            /*devo inviare il messaggio sul canale*/
            inviaMessaggioACanale(m);
        }
    }
    
    public void inviaPath(double startAt,Object destinazioneFinale){
        
        Object nextHop = this.myRouter;
        //con questo tipo di messaggio invio un messaggio di allocazione risorsa pari al rate
        Messaggi m = new Messaggi("msgTermHap",this,this.myNetLayer,destinazioneFinale,startAt);
        m.setNodoSorgente(this);
        m.isData = false;
        m.saliPilaProtocollare = false;
        m.setNextHop(nextHop);
        m.setTempo_di_partenza(startAt);
        m.rate = rate;
        servizio = new Servizi(this.tipoServizioTX,this,this.rate,s.getId_servizio());
        m.setData(servizio); 
        s.insertMessage(m);
    }
    
    
    
    public void disconnettiTerminale(double startAt,Object destinazioneFinale){
        Object nextHop = this.myRouter;
        //Con questo messaggio disconnetto il teminale liberando la risorsa
        Messaggi m = new Messaggi("msgDisconnectFromHap",this,this.myNetLayer,destinazioneFinale,startAt);
        m.setNodoSorgente(this);
        m.setNodoDestinazione(destinazioneFinale);
        m.isData = false;
        m.saliPilaProtocollare = false;
        m.setNextHop(nextHop);
        m.setTempo_di_partenza(startAt);
        m.rate = rate;
        m.setData(servizio);
        s.insertMessage(m);
    }
    
    private void inviaMessaggioACanale(Messaggi m) {
        int ch = trovaCanale(m.getNextHop());
        //System.out.println("Il canale trovato si trova in posizione :"+ch+"/"+myChannels.size());
        if(ch>=0){
            m.shifta(this.tempo_propagazione);
            m.setSorgente(this);
            m.setDestinazione(myChannels.get(ch));
            m.saliPilaProtocollare = true; 
            
            s.insertMessage(m);
           
        }
    }
    
    private void inviaMessaggioAPhyLayer(Messaggi m) {
        //Shifto di zero perchè è l'interfaccia fisica che è collegata sul canale
        m.shifta(0);
        m.setSorgente(this);
        m.setDestinazione(this.myPhyLayer);
        s.insertMessage(m);
    }
    
    /**Ritorna l'indice del canale dove e' collegato il nodo destinazione*/
    private int trovaCanale(Object object) {
        boolean trovato = false;
        int k = 0;
        for(int i = 0;i<this.myChannels.size() && !trovato;i++)
        {
            canale c = myChannels.get(i);
            int count = 0;
            while(count<c.getSizeNodo2() && !trovato){
                if((c.getNodo2at(count).equals(object))){                    
                    trovato = true;
                    k = i;
                }
                else count++;
            }
        } 
        if(trovato)
            return k;
        else return -1;
    }
    
    public int getId(){
        return this.id_nodo;
    }
    
    
}
