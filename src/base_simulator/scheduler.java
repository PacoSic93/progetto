/*
 * scheduler.java
 *
 * Created on 8 ottobre 2007, 14.07
 * Last Edit : 15.11.2013
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package base_simulator;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.io.*;
import test1.*;
import base_simulator.layers.*;
import java.lang.reflect.Method;
import java.util.Date;

import javax.swing.JDialog;
import javax.swing.JProgressBar;
/**
 *
 * @author franco
 */
public class scheduler implements Runnable{
    
    /** Creates a new instance of scheduler */        
    //Tutti i messaggi devono essere immessi in questo buffer per poter essere gestiti
    private Vector<Messaggi> buffer; // Eventi da gestire
    
    /*Cicla viene utilizzato per mettere in pausa la simulazione*/
    private boolean cicla;        
    /*Indica se si vuole avere il trace della simulazione*/
    private boolean traced;
    /*L'oggetto che scrive il trace della simulazione*/
    private trace Trace;
    /*Orologio del simulatore*/
    public Timer orologio;
    /*Indica l'orizzonte temporale della simulazione*/
    double max_Time;
    
    /**Serve per risolvere gli indirizzi tipo DNS*/
    public Vector<appartenenzaHostNodi> appartenenze;
    
    private int id_servizio = 0;
    private int id_Multicast;
        
    private Date d;
    
    private JProgressBar progressBar;
    private JDialog endedSim;
    
    private Grafo grafo;

    public Grafo getGrafo() {
        return grafo;
    }

    public void setGrafo(Grafo grafo) {
        this.grafo = grafo;
    }
    
    

    public int getId_servizio() {
        id_servizio++;
        return id_servizio;
    }

    public void setId_servizio(int id_servizio) {
        this.id_servizio = id_servizio;
    }

    
    
    public scheduler(double orizzonte_temporale,boolean traced,Vector<appartenenzaHostNodi> appartenenze) {
        //Inizializza il multicast ID
                        
        id_Multicast = 1;        
        buffer = new Vector<Messaggi>();
        this.traced = traced;
        cicla = true;
        orologio = new Timer();
        this.max_Time = orizzonte_temporale;
        this.appartenenze = appartenenze;
        if(traced)
            Trace = new trace(this);        
    }
    
    public void addMulticastSession(){
        this.id_Multicast++;
    }
    public int getId_Multicast() {
        return id_Multicast;
    }
    
    public void run() {
        System.out.println("La simulazione sta per iniziare");
        System.out.println("All' ora...:"+getData());
        
        while(orologio.getCurrent_Time()<=max_Time && buffer.size()>0) {
            
            if(cicla) {
                Messaggi m = buffer.elementAt(0);
                if(traced) { 
                    String s =null;
                    try {
                        s = Trace.getTrace(m);
                    } catch (NoSuchMethodException ex) {
                        ex.printStackTrace();
                    }
                    System.out.println(s);
                }
                
                
                buffer.removeElementAt(0);    
                //Avanzo l'orologio del simulatore'
                orologio.setCurrent_Time(m.getTempo_spedizione().getCurrent_Time());
                //Faccio scattare l'evento sul nodo destinazione'
                lanciaHandler(m);
                this.progressBar.setValue((int) this.orologio.getCurrent_Time());
            }
            
        }
        
        //Solo aggiornamento interfaccia grafica
        while(buffer.size()==0 && orologio.getCurrent_Time()<=max_Time){
            this.orologio.shifta(10000);
            this.progressBar.setValue((int) this.orologio.getCurrent_Time());
        }
        
        System.out.println("\nLa simulazione � terminata il :"+getData());
        this.endedSim.show();
    }
    
    
    public void setCicla()
    {
        if(cicla)
            cicla = false;
        else cicla = true;
    }
    
    public synchronized void insertMessage(Messaggi msg) {
        int posto=-1;
        boolean trovato=false;
        for(int i=0; i< buffer.size() && !trovato; i++){
            if(buffer.elementAt(i).getTempo_spedizione().getCurrent_Time() > msg.getTempo_spedizione().getCurrent_Time()){
                trovato=true;
                posto=i;
            }
        }
        if(trovato)
            buffer.insertElementAt(msg, posto);
        else buffer.addElement(msg);
    }//metodo insertMessage(Messaggi msg)

    void addProgressBar(JProgressBar progressBar) {
       this.progressBar = progressBar;
       this.progressBar.setMinimum(0);
       this.progressBar.setMaximum((int) this.max_Time);
    }

    void setEndedSim(JDialog endedSim) {
        this.endedSim=endedSim;
    }
    
    
    /**Utilizzo della reflection*/ 
    // Attraverso questo metodo � possibile andare sul nodo destinazione e lanciare il 
    // Metodo Handler
    private void lanciaHandler(Messaggi m) {
        Method m2 = null;
        try {
            Class[] args1 = new Class[1];
            args1[0] = Messaggi.class;
            m2 = m.getDestinazione().getClass().getDeclaredMethod("Handler",args1);    
            
        } catch (SecurityException ex) {
            ex.printStackTrace();
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        }
        
        try {
            
            m2.invoke(m.getDestinazione(),m);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        }
        
        
    }

    /**Questo metodo ritorna il nodo di appartenenza di un host*/
    public Object trovaNodoApp(int idD) {
        boolean trovato = false;
        Object nodo = null;
        for(int i = 0;i<this.appartenenze.size() && !trovato;i++){
            if((((nodo_host)this.appartenenze.elementAt(i).getDestinazioneHost()).getId()) == idD){
                trovato = true;
                nodo = this.appartenenze.elementAt(i).getNodo();
            }
        }
        return nodo;
    }
    
    //Ritorna tutti gli host appartenenti alla sottorete di un router
    public Vector<Object> trovaHosts(Object n){
        Vector<Object> myHosts = new Vector<Object>();
        for(int i = 0;i<this.appartenenze.size();i++){
            if(appartenenze.elementAt(i).getNodo() == n)
                myHosts.addElement(appartenenze.elementAt(i).getDestinazioneHost());
        }
        return myHosts;
    }
            

    private String getData() {
        d = new Date();
        
        int giorno = d.getDate();
        int mese = d.getMonth();
        int anno = d.getYear()+1900;
        
        int ora = d.getHours();
        int minuti = d.getMinutes();
        int secondi = d.getSeconds();
        String s = giorno+"/"+mese+"/"+anno+ " ora "+ora+":"+minuti+":"+secondi;
        return s;
    }
    
}