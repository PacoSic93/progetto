/*
 * tabellaRouting.java
 *
 * Created on 10 ottobre 2007, 11.30
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package base_simulator;

/**
 *
 * @author franco
 */
public class appartenenzaHostNodi {
    private Object destinazioneHost;
    private Object nextHop;
    /** Creates a new instance of tabellaRouting */
    public appartenenzaHostNodi(Object destinazioneHost,Object nextHop) {
        this.destinazioneHost = destinazioneHost;
        this.nextHop = nextHop;
    }
    public Object getNodo(){
        return nextHop;
    }
    public Object getDestinazioneHost(){
        return this.destinazioneHost;
    }
    
}
