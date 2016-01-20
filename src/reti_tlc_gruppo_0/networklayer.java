/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reti_tlc_gruppo_0;

import base_simulator.Messaggi;
import base_simulator.layers.NetworkLayer;
import base_simulator.scheduler;

/**
 *
 * @author afsantamaria
 */
public class networklayer extends NetworkLayer{
    
    public networklayer(scheduler s, double tempo_di_processamento) {
        super(s, tempo_di_processamento);
    }
    
    
    @Override
    public void gestisciPacchettoDati(Messaggi m)
    {
        if(m.saliPilaProtocollare)
        {
            sendDatoToTransportLayer(m);
        }
        else
        {
            sendDatoToLinkLayer(m);
        }
    }
    
    @Override
    public void gestisciPacchettoProtocollo(Messaggi m)
    {
        
    }
    
    
    
    /*Gestisce il pacchetto e il comportamento del nodo*/
    @Override
    public void Handler(Messaggi m){
        //System.out.println("\nMessaggio giunto al NetworkLayer");
        if(m.isData)
            gestisciPacchettoDati(m);
        else
            gestisciPacchettoProtocollo(m);
    }

    private void sendDatoToTransportLayer(Messaggi m) {
        m.shifta(tempo_di_processamento);
        m.setSorgente(this);
        m.setDestinazione(transportLayer);
        
        m.saliPilaProtocollare = false;
        s.insertMessage(m);
    }

    private void sendDatoToLinkLayer(Messaggi m) {
        
    }
    
}
