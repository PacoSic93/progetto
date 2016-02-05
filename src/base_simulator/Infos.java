/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package base_simulator;

import base_simulator.layers.NetworkLayer;
import java.util.ArrayList;

/**
 *
 * @author afsantamaria
 */
public class Infos {
    private ArrayList<canale> channels;
    private ArrayList<Nodo> nodes;
    private ArrayList<Link> links;
    
    public Infos()
    {
        channels = new ArrayList<canale>();
        nodes = new ArrayList<Nodo>();
        links  = new ArrayList<Link>();
    }
    
    public void addCanale(canale c)
    {
        channels.add(c);
    }
    
    public void addNodo(Nodo n)
    {
        n.setInfo(this);
        nodes.add(n);
    }
    
    public void addLink(Link l)
    {
        links.add(l);
    }
    
    public Nodo getNodo(int id)
    {
        Nodo res = null;
        for(Object n : nodes)
        {
            Nodo item = (Nodo)n;
            if(item.getId() == id){
                res = item;
                break;
            }                                    
        }
        return res;
    }
    
    public canale getCanale(int id)
    {
        canale res = null;
        for(Object c : channels)
        {
            canale item = (canale)c;
            if(item.getId() == id){
                res = item;
                break;
            }                                    
        }
        return res;
    }
    
    public link_extended getLink(int start,int end)
    {
        link_extended res = null;
        for(Object l : links)
        {
            link_extended item = (link_extended)l;
            if(item.getSource() == start && item.getDest()==end){
                res = item;
                break;
            }                                    
        }
        return res;
    }

    /**
     * Stampa statistiche nodo Host
     */
    void stampaStatisticheNodo() {
        for(Object obj : this.nodes)
        {
            Nodo n = (Nodo)obj;
            System.out.println("=====STAMPA STATISTICHE NODO NETWORK LAYER====");
            NetworkLayer nl = n.myNetLayer;
            String s = nl.getStat();
            System.out.println(s);
            System.out.println("=====FINE====");
        }
    }

    

    
    
    
    
    
    
}
