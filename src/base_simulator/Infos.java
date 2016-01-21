/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package base_simulator;

import base_simulator.Link;
import base_simulator.Nodo;
import base_simulator.canale;
import base_simulator.link_extended;
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
        ArrayList<canale> channels = new ArrayList<canale>();
        ArrayList<Nodo> nodes = new ArrayList<Nodo>();
        ArrayList<Link> links  = new ArrayList<Link>();
    }
    
    public void addCanale(canale c)
    {
        channels.add(c);
    }
    
    public void addNodo(Nodo n)
    {
        n.addInfos(this);
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
    
    
    
    
    
}
