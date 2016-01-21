package base_simulator;

public class Decisioner {

    private tabellaRouting tr;
    private Grafo topology;
    private int default_gateway = 0;

    public Decisioner(tabellaRouting tr) {
        super();
        this.tr = tr;

        //TODO : Il grafo deve essere costruito mana mano con il protocollo
        this.topology = new Grafo(1);
    }

    public tabellaRouting getTr() {
        return tr;
    }

    public int getDefault_gateway() {
        return default_gateway;
    }
    
    public int getNextHop(int dest)
    {
        int next_hop = tr.getNextHop(dest);
        if(next_hop < 0)
           next_hop = default_gateway;
        
        return next_hop;
    }

    public void setDefault_gateway(int default_gateway) {
        this.default_gateway = default_gateway;
    }

    public void setTr(tabellaRouting tr) {
        this.tr = tr;
    }

    public tabellaRouting findPath(int source, int destination) {
        tabellaRouting tr = this.tr;
        // TODO : Calcolo della Tabella di Routing
        return tr;
    }

}
