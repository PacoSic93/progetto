package base_simulator;

import java.util.LinkedList;

class lineRouting
{
	private int nodo_destinazione;
	private int next_hop;
	private double costo;
	
	public lineRouting(int nodo_destinazione, int next_hop, double costo)
	{
		this.nodo_destinazione = nodo_destinazione;
		this.next_hop = next_hop;
		this.costo = costo;
	}
	
	public int getNodoDestinazione()
	{
		return this.nodo_destinazione;
	}
	
	public int getNextHop()
	{
		return this.next_hop;
	}
	
	public double getCosto()
	{
		return this.costo;
	}

	public void setCosto(double costo) {	
		this.costo = costo;
	}
}

public class tabellaRouting
{
	private LinkedList<lineRouting> entries;
	
	public tabellaRouting()
	{
		entries = new LinkedList<lineRouting>();
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
		lineRouting entry = new lineRouting(nodo_destinazione,next_hop,costo);
		
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
}