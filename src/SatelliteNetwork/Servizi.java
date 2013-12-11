/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package SatelliteNetwork;

import base_simulator.Nodo;
import java.util.LinkedList;
import test1.nodo_host;

/**
 *
 * @author Utente
 */
public class Servizi implements Cloneable{
    private String tipo;
    private Nodo sorgente;
    private double rate;
    private int id_servizio;
    private LinkedList destinazioni;
    private nodo_host destinazioneDaAggiungere;

    public void setDestinazioneDaAggiungere(nodo_host n) {
        this.destinazioneDaAggiungere = n;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Servizi s = (Servizi)super.clone();
        s.id_servizio = this.id_servizio;
        s.sorgente = this.sorgente;
        s.rate = this.rate;
        s.id_servizio=this.id_servizio;
        s.destinazioni = (LinkedList) this.destinazioni.clone();
        return s;
    }

    
    public Servizi(String tipo, Nodo sorgente, double rate, int id_servizio) {
        this.tipo = tipo;
        this.sorgente = sorgente;
        this.rate = rate;
        this.id_servizio = id_servizio;
        this.destinazioni = new LinkedList();
    }

    public LinkedList getDestinazioni() {
        return destinazioni;
    }

    public void setDestinazioni(LinkedList destinazioni) {
        this.destinazioni = destinazioni;
    }

    public void addDestinazione (Nodo n){
        destinazioni.add(n);
    }
    public int getId_servizio() {
        return id_servizio;
    }

    public void setId_servizio(int id_servizio) {
        this.id_servizio = id_servizio;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public Nodo getSorgente() {
        return sorgente;
    }

    public void setSorgente(Nodo sorgente) {
        this.sorgente = sorgente;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public nodo_host getDestinazioneDaAggiungere() {
        return this.destinazioneDaAggiungere;
    }
    
}
