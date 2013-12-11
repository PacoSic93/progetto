/*
 * Entita.java
 *
 * Created on 10 ottobre 2007, 12.48
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package base_simulator;

/**
 *
 * @author Ing. Amilcare-Francesco Santamaria
 */
public class Entita {
    protected scheduler s;
    protected String tipo;
    /** Creates a new instance of Entita */
    public Entita(scheduler s,String tipo) {
        this.tipo = tipo;
        this.s = s;        
    }
    
    public void Handler(Messaggi m){
        
    }
    
    public String toString(){
        return this.tipo;
    }
    
    public String getTipo(){
        return this.tipo;
    }
    
    public String getStat(){
        String s = "\nEntita";
        return s;
    }
    
}
