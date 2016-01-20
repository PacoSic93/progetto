package base_simulator;
import java.util.*;

/**************************
 * @author Franco/Gabriel
 **************************/

public class Messaggi implements Cloneable {
    
    private String tipo_Messaggio;    
    private Object data;
    
    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
    
    private Object sorgente;
    private Object destinazione;
    
    /**Indica il nodo destinazione finale*/
    private Object nodoDestinazione;
    /**Indica il nodo che ha genrato il messaggio*/
    private Object nodoSorgente;
    /**Indica il prossimo nodo verso il quale il messaggio deve essere inviato*/
    private Object nextHop;
    
    /*Tempo in cui il pacchetto parte dal nodo intermedio*/
    private Timer tempo_spedizione;
    /*Tempo iniziale dove il pacchetto è stato generato*/
    private double tempo_di_partenza;
    
    private int sessione;
    private Object dati;
    
    public Vector<Object> lista_precedenti;
    public Vector<Object> lista_successivi;
    
    public String classe_di_traffico;
       
    public double rate;
    
    public int ID;
    
    /*******************
     * AGGIUNTO 01/02/2007
     * aggiungo questo campo nel msg che mi serve per la propagazione dei msg di BID, per limintare
     * la Local Search previsto nel protocollo QoSMIC
     *******************/
    private int TTL;
    private Grafo topology;
    
    
    public double tInBuffer;
    public double permanenzaMax;
    public double permanenza;
    public int countPermanenza;
    
    public int id_multicast;
    /**indica se il messaggio deve salire nella pila o scendere se true il messaggio è ricevuto e
      e quindi salire nella pila*/
    public boolean saliPilaProtocollare = false;
    
    /*Se false indica che il messaggio è di protocollo*/
    public boolean isData = false;
    /**Indica il tipo di oggetto su cui lanciare l'handler*/
    
    /**Indica se il messaggio è di tipo networking oppure di tipo movimento*/
    boolean isNetworkingMessage = false;
    /**Indica se il messaggio è di tipo broadcast*/
    boolean BroadCast = false;
    
    /*Indica quali sono gli spots che deveno ricevere il messaggio*/
    Vector<Object> spots;
    /**
     *Quando il messaggio vine creato deve essere indicato anche il nodo destinazione e non solo 
     *l'oggetto della pila protocollare
     */
    public Messaggi(String tipo_Messaggio, Object sorgente, Object destinazione,Object nodoDestinazione,double start) {
        super();
        isNetworkingMessage = false;
        BroadCast = false;
        this.tipo_Messaggio = tipo_Messaggio;
        this.sorgente = sorgente;
        this.destinazione = destinazione;
        this.nodoDestinazione = nodoDestinazione;
        
        this.tempo_spedizione = new Timer();
        this.tempo_spedizione.setCurrent_Time(start);
        this.tempo_di_partenza = start;
        
        this.lista_precedenti = new Vector<Object>();
        this.lista_successivi = new Vector<Object>();
        classe_di_traffico = "";
        this.rate = 0;
        this.ID = 0;
        this.TTL=0;// conviene inizializarlo a zero o ad un valore alto? visto che viene decrementato
        this.topology = new Grafo(1);

        
        tInBuffer       = 0.0;
        permanenzaMax   = 0.0;
        permanenza      = 0.0;
        countPermanenza = 0;
    }
    

    
    /** 
     * @brief : Questo metodo mi permette di aggiumgere una lista di spot ai quali passare i messaggi
     * il campo spots può essere utilizzato sia per il broadcast che per altri tipi di paradigmi di funzionamento
     * @param spot
     */
    public boolean isBroadCast(){ return this.BroadCast;}
    
    
    public void setSorgente(Object sorgente){
        this.sorgente = sorgente;
    }
    public void setDestinazione(Object destinazione){
        this.destinazione = destinazione;
    }
    public void setNodoDestinazione(Object nodo){
        this.nodoDestinazione = nodo;
    }
   /**
    * Ritorna il nodo finale che è destinatario del messaggio
    * @return ritorna l'oggetto destinatario del messaggio
    */
    public Object getNodoDestinazione() {
        return this.nodoDestinazione;
    }
    
    public void setNodoSorgente(Object nodo){
        this.nodoSorgente = nodo;
    }
    public void setNextHop(Object nodo){
        this.nextHop = nodo;
    }
    
    /**
     * Questo metodo ritorna il nodo che ha generato per prima il messaggio
     * @return Oggetto che ha generato il messaggio
     */
    public Object getNodoSorgente(){
        return this.nodoSorgente;
    }
    public Object getNextHop(){
        return this.nextHop;
    }
    
    public String getTipo_Messaggio(){
        return this.tipo_Messaggio;
    }
    
    public void shifta(double mms)
    {
        this.tempo_spedizione.shifta(mms);
    }
   /**
    * Ritorna l'oggetto che ha spedito il messaggio
    * @return
    */
    public Object getSorgente(){
        return this.sorgente;
    }
    /**
     * Ritorna l'oggetto destinatario del messaggio
     * @return
     */
    public Object getDestinazione(){
        return this.destinazione;
    }
    
    public void setTipo_Messaggio(String tipo_Messaggio) {
        this.tipo_Messaggio = tipo_Messaggio;
    }
    
    
    public int getSessione() {
        return this.sessione;
    }
    
    public void setSessione(int sessione) {
        this.sessione = sessione;
    }
    
        /* (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((tipo_Messaggio == null) ? 0 : tipo_Messaggio.hashCode());
        return result;
    }
    
      
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Messaggi other = (Messaggi) obj;
        if (tipo_Messaggio == null) {
            if (other.tipo_Messaggio != null)
                return false;
        } else if (!tipo_Messaggio.equals(other.tipo_Messaggio))
            return false;
        return true;
    }
    
    @Override
    public String toString(){
        String s="";
        s+="\nid del messaggio :"+this.ID +" di tipo"+this.tipo_Messaggio;
        return s;       
    }
    
    public Timer getTempo_spedizione() {
        return tempo_spedizione;
    }
    
    public void setTempo_spedizione(Timer tempo_spedizione) {
        this.tempo_spedizione = (Timer) tempo_spedizione.clone();
    }
    
    public double getTempo_di_partenza() {
        return tempo_di_partenza;
    }
    
    public void setTempo_di_partenza(double tempo_di_partenza) {
        this.tempo_di_partenza = tempo_di_partenza;
    }
    
    
    
    /*******************
     * AGGIUNTO 01/02/2007
     * metodi per settare e verificare valore del campo int TTL del msg.
     *******************/
    public int getTTL(){
        return TTL;
    }
    
    public void setTTL(int maxHOP){
        TTL = maxHOP;
    }
    /*******************************
     *fine modifica
     ******************************/
    
    public void setGrafo(Grafo g){
        this.topology=g;
    }
    
    
    public Grafo getGrafo(){
        return this.topology;
    }
    
    public Object clone(){
        try{
            Messaggi m;
            m=(Messaggi) super.clone();
            m.tempo_di_partenza=this.tempo_di_partenza;
            m.tempo_spedizione=(Timer )this.tempo_spedizione.clone();
            m.lista_precedenti=(Vector<Object>)this.lista_precedenti.clone();
            m.id_multicast = this.id_multicast;
            m.data = this.data;
            m.dati = this.dati;
            m.rate = this.rate;
            return m;
        }catch(CloneNotSupportedException e){return null;}
    }
    
    /**
     * Il metodo permette di ottenere l'informazione dati contenuta nel messaggio
     * @return
     */
    public Object getDati(){
        return this.dati;
    }
    /**
     * Il metodo setta l'informazione dati del messaggio
     * @param dati
     */
    public void setDati(Object dati){
        this.dati = dati;
    }
    
    
    
    
}