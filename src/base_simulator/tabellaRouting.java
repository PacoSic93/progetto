package base_simulator;

import java.util.ArrayList;




public class tabellaRouting
{
	private ArrayList<RoutingRow> entries;
	
	public tabellaRouting()
	{
		entries = new ArrayList<RoutingRow>();
	}
	
	/**
	 * 
	 * @param nodo_destinazione Nodo-Id  - Identificativo del nodo destinazione (Univoco)
	 * @param next_hop Next-Hop - Identificativo del prossimo nodo per raggiungere dest
	 * @param costo - Costo per raggiungere il nodo_destinazione
	 * @brief Aggiunge un entry alla tabella di routing 
	 */
	public void addEntry(int nodo_destinazione, int next_hop, double costo)
	{
		RoutingRow entry = new RoutingRow(nodo_destinazione,next_hop,costo);
		
		int pos = controllaPresenzaLinea(nodo_destinazione,next_hop);
		
		if(pos == -1)
		{
			entries.add(entry);
		}
		else
		{
			if((entries.get(pos)).getCosto() != costo)
			{
				entries.get(pos).setCosto(costo);
			}
		}
	}
	
	/**
	 * 
	 * @param dest : Nodo-Id  - Identificativo del nodo destinazione (Univoco)
	 * @param next : Next-Hop - Identificativo del prossimo nodo per raggiungere dest
	 * @return ritorna la posizione (Linea) nella tabella di routing se presente altrimenti -1 
	 */
	public int controllaPresenzaLinea(int dest,int next)
	{
		boolean found = false;
		int pos = -1;
		for(int i = 0; i<entries.size() && !found;i++)
		{
			if(entries.get(i).getNodoDestinazione() == dest && entries.get(i).getNextHop()==next)
			{
				pos = i;
				found = true;
			}
		}
		return pos;
	}

    /**
     * Ritorna il next hop di una destinazione aggiunta sia tramite informazioni statiche (Conf.xml) che attraverso
     * informazioni di routing dinamiche (Protocollo di routing)
     * @param dest - Nodo destinazine da raggingere
     * @return
     */
    public int getNextHop(int dest) {
        int res = -1;
        for(Object linea : entries)
        {
            if(dest == ((RoutingRow)linea).getNodoDestinazione())
            {
                res = ((RoutingRow)linea).getNextHop();
                break;
            }
        }
        return res;
    }

    void removeEntries() {
        this.entries.clear();
    }
    
    
    public ArrayList<Integer> getNeighbours()
    {
        ArrayList<Integer> nodes = new ArrayList<Integer>();
        for(Object linea : entries)
        {
            if(((RoutingRow)linea).getNodoDestinazione() == ((RoutingRow)linea).getNextHop())
            {
                nodes.add(((RoutingRow)linea).getNextHop());
            }
        }
        return nodes;
        
    }
    
    public ArrayList<RoutingRow> getEntries()
    {
        return this.entries;
    }
}