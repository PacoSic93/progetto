/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author franco
 */
package base_simulator;
import java.util.*;

public class Link {
    protected Vector<Double> pesi;
    public Link() {
        pesi = new Vector<Double>();
    }

    public Link(Vector<Double> pesi) {
        for(int i = 0;i<pesi.size();i++)
            this.pesi.add(pesi.elementAt(i));
    }

    public void setPesi(Vector<Double> pesi) {
        this.pesi = new Vector<Double>();
        for(int i = 0;i<pesi.size();i++)
            this.pesi.add(pesi.elementAt(i));
    }

    public Vector<Double> getPesi() {
        return pesi;
    }
    
    
}
