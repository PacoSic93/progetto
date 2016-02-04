package base_simulator;

import java.util.ArrayList;


/**
 * Questa classe è una rappresentazione astratta della rete:
 * Il grafo deve essere istanziato su ogni nodo (preferibilmente a livello network)
 * Sarà utilizzato dai protocolli e algoritmi di routing per effettuare le dovute operazioni
 * @author afsantamaria
 */
public class Grafo
{

    //il parametro n indica di quanti nodi è composto il grafo
    protected int n;
    // Rappresenta la matrice delle adiacenze in particolare utilizzando un valore diverso da 0
    //      
    ArrayList<ArrayList<Double>> rami;

    public Grafo(int n) {
        this.n = n;
        rami = new ArrayList<ArrayList<Double>>();
        for(int i = 0;i<n;i++)
        {
            ArrayList<Double> riga = new ArrayList<Double>();
            for(int j=0;j<n;j++)
            {
                riga.add(0.0);
            }
            rami.add(riga);
        }
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
        rami.clear();
        rami = new ArrayList<ArrayList<Double>>();
        for(int i = 0;i<n;i++)
        {
            ArrayList<Double> riga = new ArrayList<Double>();
            for(int j=0;j<n;j++)
            {
                riga.add(0.0);
            }
            rami.add(riga);
        }
    }

    /**
     * Restituisce la lista dei nodi eccetto il nodo che passiamo come parametro
     * @param myId
     * @return 
     */
    ArrayList<Integer> getNodesExceptSource(int myId) {
        ArrayList<Integer> nodes = new ArrayList<Integer>();
        for(int i = 0; i<n;i++)
        {
            for(int j = 0;j<n;j++)
            {
                if(rami.get(i).get(j)>0.0 && j!=myId)
                {
                    if(!nodes.contains(j))
                        nodes.add(j);
                }
                    
            }
        }
        return nodes;
    }

    public double getCosto(int source, int dest) {
       double costo = rami.get(source).get(dest);
       return costo;
    }

    public void setCosto(int source, int dest,double costo) {
        rami.get(source).set(dest, costo);
    }

    int getPadre(int next_hop) {
        for(int i = 0; i<n;i++)
        {
            if(rami.get(i).get(next_hop)>0)
            {
                return i;
            }
        }
        return -1;
    }
    
    
}