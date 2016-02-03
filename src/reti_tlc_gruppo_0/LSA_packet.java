/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reti_tlc_gruppo_0;

import base_simulator.RoutingRow;
import base_simulator.tabellaRouting;
import java.util.ArrayList;

/**
 *
 * @author franco
 */
public class LSA_packet {
    int seq_no;
    int sorgente;
    int rilancio;
    int ttl;
    tabellaRouting tr;   

    public int getSeq_no() {
        return seq_no;
    }

    public void setSeq_no(int seq_no) {
        this.seq_no = seq_no;
    }

    public int getSorgente() {
        return sorgente;
    }

    public void setSorgente(int sorgente) {
        this.sorgente = sorgente;
    }

    public int getRilancio() {
        return rilancio;
    }

    public void setRilancio(int rilancio) {
        this.rilancio = rilancio;
    }

    public int getTtl() {
        return ttl;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }

    public tabellaRouting getTr() {
        return tr;
    }

    public void setTr(tabellaRouting tr) {
        copia_tr(tr);
    }

    public LSA_packet(int seq_no, int sorgente, int rilancio, int ttl, tabellaRouting tr) {
        this.seq_no = seq_no;
        this.sorgente = sorgente;
        this.rilancio = rilancio;
        this.ttl = ttl;
        copia_tr(tr);
    }
    
    protected void copia_tr(tabellaRouting tr)
    {
        ArrayList<RoutingRow> appo = tr.getEntries();
        
        for(Object entry : appo)
        {
            tr.addEntry(((RoutingRow)entry).getNodoDestinazione(), 
                    ((RoutingRow)entry).getNextHop(), 
                    ((RoutingRow)entry).getCosto());
        }
    }
    
    
    
}
