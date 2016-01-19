package base_simulator;



public class Decisioner {
	private tabellaRouting tr;
	private Grafo topology;

	public Decisioner(tabellaRouting tr) {
		super();
		this.tr = tr;
		
		//TODO : Il grafo deve essere costruito mana mano con il protocollo
		this.topology = new Grafo(1);
	}

	public tabellaRouting getTr() {
		return tr;
	}

	public void setTr(tabellaRouting tr) {
		this.tr = tr;
	}
	
	public tabellaRouting findPath(int source, int destination)
	{
		tabellaRouting tr = this.tr;
		// TODO : Calcolo della Tabella di Routing
		return tr;
	}
	
}
