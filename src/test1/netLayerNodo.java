/*
 * netLayerNodo.java
 *
 * Created on 10 October 2007, 21:44
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test1;

import base_simulator.layers.NetworkLayer;
import base_simulator.*;

/**
 *
 * @author franco
 */
public class netLayerNodo extends NetworkLayer{
    
    /** Creates a new instance of netLayerNodo */
    public netLayerNodo(scheduler s,double tempo_di_processamento) {
        super(s,tempo_di_processamento);
    }
    public void Handler(Messaggi m){
        //System.out.println("\nMessaggio giunto al NetworkLayer");
        if(m.isData)
            gestisciPacchettoDati(m);
        else
            gestisciPacchettoProtocollo(m);
    }
}
