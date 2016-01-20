/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package base_simulator;

/**
 *
 * @author afsantamaria
 */
public class link_extended extends Link{

    private int source;
    private int dest;
    private int id;
    public link_extended() {
        super();
    }

    public link_extended(int source, int dest,double metric) {
        super();
        this.source = source;
        this.dest = dest;
        super.pesi.add(metric);
    }

   
    
    
    
    
    
}
