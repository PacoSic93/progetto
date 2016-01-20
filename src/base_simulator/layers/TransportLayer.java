/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package base_simulator.layers;

import base_simulator.Entita;
import base_simulator.Messaggi;
import base_simulator.scheduler;
import java.util.ArrayList;

/**
 *
 * @author afsantamaria
 */
public class TransportLayer extends Entita{
    protected scheduler s;
    protected NetworkLayer networkLayer;
    protected double tempo_di_processamento;
    protected Object nodo;
    protected ArrayList<Integer> enabled_ports;
    
    private int droppedPacket = 0;

    public TransportLayer(scheduler s, double tempo_di_processamento) {
        super(s, "Transport Layer");
        this.tempo_di_processamento = tempo_di_processamento;
    }

    public void connectTransportLayer(NetworkLayer networkLayer,Object nodo){
        this.networkLayer = networkLayer;
        this.nodo = nodo;
    }
    
    public void enablePort(int port)
    {
        enabled_ports.add(port);
    }
    
    @Override
    public void Handler(Messaggi m)
    {
       
    }
    
    
    
    
}
