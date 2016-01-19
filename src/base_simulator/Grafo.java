/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package base_simulator;
import java.util.*;
/**
 *
 * @author franco
 */
public class Grafo {
    protected int n;
    protected Vector<Vector<Link>> elementi;
    protected int sorgente;
    protected Vector<Integer> destinazioni;
    protected double []delay;
    protected double []costo;
    protected double []banda;
    private double costoMedio;
    private double maxDelay;
    private double delayMedio;
    private double bandaMedia;
    private double bandaMinima;

    public double getCostoMedio() {
        return costoMedio;
    }

    public double getMaxDelay() {
        return maxDelay;
    }

    
    public Vector<Integer> getDestinazioni() {
        Vector<Integer> destinations = new Vector<Integer>();
        for(int i = 0;i<destinazioni.size();i++)
            destinations.addElement(destinazioni.elementAt(i));
        return destinations;
    }

    public void setDestinazioni(Vector<Integer> destinazioni) {
        this.destinazioni = new Vector<Integer>();
        for(int i = 0;i<destinazioni.size();i++)
            this.destinazioni.addElement(destinazioni.elementAt(i));
    }

    public int getSorgente() {
        return sorgente;
    }

    public void setSorgente(int sorgente) {
        this.sorgente = sorgente;
    }
    
    public Grafo(int n) {
        this.n = n;
        elementi = new Vector<Vector<Link>>();
        for(int i = 0;i<n;i++){
            Vector<Link> ls = new Vector<Link>();
            for(int j = 0;j<n;j++){
                Link l = new Link();
                ls.add(l);
            }
            elementi.add(ls);
        }
        delay = new double[1];
        costo = new double[1];
        banda = new double[1];
        delayMedio = 0;
        bandaMedia = 0;
        bandaMinima = 0;
    }
    
    /**
     * Ritorna l'arco per copia profonda
     * @param i nodo iniziale
     * @param j nodo finale
     * @return la copia profonda del link
     */
    public Link getArco(int i,int j){
        Link l=new Link();
        l.setPesi(elementi.elementAt(i).elementAt(j).getPesi());
        return elementi.elementAt(i).elementAt(j);
                
    }
    
    /**
     * Setta con la copia profonda il link tra il nodo i e il nodo j
     * @param i nodo iniziale
     * @param j nodo finale
     * @param l pesi presenti sull'arco
     */
    public void setArco(int i,int j,Link l){
       elementi.elementAt(i).elementAt(j).setPesi(l.getPesi());
    }

    public void eliminaNodo(int i) {
        int k = getPadre(i);
        if(k>=0){
            Link l = new Link();
            this.setArco(k, i, l);
        }
        else System.out.print("\nil nodo non ha padre ...:e' gia sconnesso");
    }

    public int getN() {
        return this.n;
    }

    /**
     * Metodo che ritorna i figli del nodo i
     * @param i
     * @return i nodi figli del nodo passato
     */
    public Vector<Integer> getVicini(int i) {
        Vector<Integer> vicini = new Vector<Integer>();
        for(int j = 0;j<n;j++){
            if ( i!=j && elementi.elementAt(i).elementAt(j).getPesi().size() > 0)
                vicini.add(j);
        }    
        return vicini;
        
    }
    
    /**
     * Questo medoto ritorna il ritardo medio del grafo
     * dando come input una serie di destinazioni 
     * che sarà la metrica in posizione 1 del Link
     * @return il valore del ritardo medio
     */
    public double getAverageDelay(int radice,Vector<Integer> destinazioni){
        delay = new double[n];
        for(int i = 0;i<n;i++) {
            delay[i] = 0;
        }
        boolean visitati[] = new boolean[this.n];
        for(int i = 0;i<n;i++)
            visitati[i] = false;
        int nodo_corrente = radice;
        visitati[nodo_corrente] = true;
        
        Vector<Integer> vicini = new Vector<Integer>();
        Vector<Integer> neighboors = getVicini(nodo_corrente);
        for(int i = 0;i<neighboors.size();i++) {
            vicini.add(neighboors.elementAt(i));
        }
        
        while(vicini.size()>0){
            //System.out.println("La pila contine ancora oggetti  :"+vicini.size());
            for(int i = 0;i<neighboors.size();i++) {
               //System.out.print("\nvalore delay arco i,j "+nodo_corrente+","+neighboors.elementAt(i));
               //System.out.print(" Valore :"+this.elementi.elementAt(nodo_corrente).elementAt(neighboors.elementAt(i)).getPesi().elementAt(1));
               double r_0 = this.elementi.elementAt(nodo_corrente).elementAt(neighboors.elementAt(i)).getPesi().elementAt(1);
               double r = delay[nodo_corrente] + r_0;
               delay[neighboors.elementAt(i)] = r;
            }
           
           while(vicini.size()>0 && visitati[vicini.elementAt(0)])
               vicini.removeElementAt(0);
            if (vicini.size() > 0) {
                nodo_corrente = vicini.elementAt(0);
                visitati[nodo_corrente] = true;
                vicini.removeElementAt(0);
                //System.out.println("cerco i vicini del nodo corrente :"+nodo_corrente);
                neighboors.removeAllElements();
                neighboors = getVicini(nodo_corrente);
                for (int i = 0; i < neighboors.size(); i++) {
                    vicini.add(neighboors.elementAt(i));
                }
            }
           //System.out.println()
        }
        
        double averageDelay = 0;
        maxDelay = 0;
        for(int i = 0;i<n;i++) {
            if (eDestinazione(i, destinazioni)) { 
                averageDelay += delay[i];
                if(maxDelay<delay[i]) maxDelay = delay[i];
            }
           
        }
        //System.out.println("Somma delay..:"+averageDelay+"\nDestinazioni size :"+destinazioni.size());
        averageDelay /= destinazioni.size();
        delayMedio = averageDelay;
        return averageDelay;
    }
    
    /**
     * Questo metodo deve essere lanciato solo dopo aver lanciato 
     * il meodo averageDelay altrimenti ritornerà un double[1]
     * @return ritorna i singoli ritardi dei nodi
     */
    public double[] getDelay() {
        return delay;
    }
    
    
    /**
     * Ritorna il costo dell'albero
     * ricordandoti che la metrica sul costo è la prima dei Link
     * @param radice
     * @param destinazioni
     * @return
     */
    public double getCosto(int radice,Vector<Integer> destinazioni){
        costo = new double[n];
        for(int i = 0;i<n;i++) {
            costo[i] = 0;
        }
        boolean visitati[] = new boolean[this.n];
        for(int i = 0;i<n;i++)
            visitati[i] = false;
        int nodo_corrente = radice;
        visitati[nodo_corrente] = true;
        Vector<Integer> vicini = new Vector<Integer>();
        Vector<Integer> neighboors = getVicini(nodo_corrente);
        for(int i = 0;i<neighboors.size();i++) {
            vicini.add(neighboors.elementAt(i));
        }
        
        while(vicini.size()>0){
            
           for(int i = 0;i<neighboors.size();i++) {
                costo[neighboors.elementAt(i)] += costo[nodo_corrente] + this.elementi.elementAt(nodo_corrente).elementAt(neighboors.elementAt(i)).getPesi().elementAt(0);
            }
           
           
           while(vicini.size()>0 && visitati[vicini.elementAt(0)])
               vicini.removeElementAt(0);
            if (vicini.size() > 0) {
                nodo_corrente = vicini.elementAt(0);
                visitati[nodo_corrente] = true;
                vicini.removeElementAt(0);
                //System.out.println("cerco i vicini del nodo corrente :"+nodo_corrente);
                neighboors.removeAllElements();
                neighboors = getVicini(nodo_corrente);
                for (int i = 0; i < neighboors.size(); i++) {
                    vicini.add(neighboors.elementAt(i));
                }
            }        
        }
        
        double costi = 0;
        for(int i = 0;i<n;i++) {
            if (eDestinazione(i, destinazioni)) {
                costi += costo[i];
            }
        }
        
        costoMedio=costi/destinazioni.size();
        return costi;
    }

    public double getBandaMedia() {
        return bandaMedia;
    }

    public double getBandaMinima() {
        return bandaMinima;
    }
    
    /**
     * Ritorna la banda minima verso ogni nodo, ricordati che la banda deve essere
     * la minima presente sull'albero ed è quella con indice 2 nei Link
     * @param radice
     * @param destinazioni
     * @return
     */
    public double[] getBandwidth(int radice,Vector<Integer> destinazioni){
        double bandwidth = Double.MAX_VALUE;
        banda = new double[n];
        for(int i = 0;i<n;i++)
            banda[i] = 0;
        boolean visitati[] = new boolean[this.n];
        for(int i = 0;i<n;i++)
            visitati[i] = false;
        int nodo_corrente = radice;
        visitati[nodo_corrente] = true;
        banda[nodo_corrente]=0;
        
        Vector<Integer> vicini = new Vector<Integer>();
        Vector<Integer> neighboors = getVicini(nodo_corrente);
        for(int i = 0;i<neighboors.size();i++) {
            vicini.add(neighboors.elementAt(i));
        }
        
        while(vicini.size()>0){
             
           for(int i = 0;i<neighboors.size();i++) {
               int nodo_succ = 0;
                if (this.elementi.elementAt(nodo_corrente).elementAt(neighboors.elementAt(i)).getPesi().elementAt(2) < bandwidth) {
                    bandwidth = this.elementi.elementAt(nodo_corrente).elementAt(neighboors.elementAt(i)).getPesi().elementAt(2);    
                    nodo_succ = neighboors.elementAt(i);
                }
                banda[nodo_succ]=bandwidth;
            }
           
           while(vicini.size()>0 && visitati[vicini.elementAt(0)])
               vicini.removeElementAt(0);
           if (vicini.size() > 0) {
                nodo_corrente = vicini.elementAt(0);
                visitati[nodo_corrente] = true;
                vicini.removeElementAt(0);
                //System.out.println("cerco i vicini del nodo corrente :"+nodo_corrente);
                neighboors.removeAllElements();
                neighboors = getVicini(nodo_corrente);
                for (int i = 0; i < neighboors.size(); i++) {
                    vicini.add(neighboors.elementAt(i));
                }
            }
        }
        
        bandaMinima = bandwidth;
        bandaMedia = 0;
        int cont = 0;
        for(int i = 0;i<n;i++) {
            if (eDestinazione(i, destinazioni)) {
                bandaMedia += banda[i];
                if(banda[i]>0)
                    cont++;
            }
        }
        bandaMedia/=cont;
        return banda;
        
    }
    
    /**
     * Questo metodo ritorna il nodo padre del nodo i
     * @param i
     * @return il nodo padre di i
     */
    public int getPadre(int i) {
        boolean trovato = false;
        int padre = -1;
        for(int k = 0;k<this.n && !trovato;k++){
            if(!this.getArco(k, i).getPesi().isEmpty()){
                trovato = true;
                padre = k;
            }
        }
        return padre;    
    }
    
    public Vector<Integer> getPadri(int i){
        Vector<Integer> padri = new Vector<Integer>();
        for(int k = 0;k<this.n;k++){
            if(!this.getArco(k, i).getPesi().isEmpty()){
                padri.add(k);
            }
        }
        return padri;
    }

    /**
     * @Description Questo metodo stampa un grafico dell'albero
     * @param sorgente
     */
    public void print(int sorgente) {
        int nodo_corrente = sorgente;
        Vector<Integer> next_nodi = this.getVicini(nodo_corrente);
        
        while(next_nodi.size()>0){
            Vector<Integer> vicini = this.getVicini(nodo_corrente);
            System.out.println("\n"+nodo_corrente+"-->");
            while(vicini.size()>0){
                System.out.print(" "+vicini.elementAt(0)+" ");
                vicini.removeElementAt(0);
            }
            nodo_corrente = next_nodi.elementAt(0);
            next_nodi.removeElementAt(0);
            Vector<Integer> da_app = this.getVicini(nodo_corrente);
            for(int i = 0;i<da_app.size();i++)
                next_nodi.add(da_app.elementAt(i));
                        
        }
        
    }
    
    /**
     * @Description Questo metodo stampa la matrice di adiacenza dei nodi della rete
     */
    public void print(){
        System.out.println("----------------Stampo peso costo----------");
        for(int i = 0;i<this.n;i++){
            
            for(int j = 0;j<this.n;j++){
                if(elementi.elementAt(i).elementAt(j).getPesi().size()==0)
                    System.out.print(" 0");
                else System.out.print(" "+elementi.elementAt(i).elementAt(j).getPesi().elementAt(0));
            }
            System.out.println();
         }
         System.out.println("---------------Stampo peso ritardo---------");    
         for(int i = 0;i<this.n;i++){
            for(int j = 0;j<this.n;j++){
                if(elementi.elementAt(i).elementAt(j).getPesi().size()==0)
                    System.out.print(" 0");
                else System.out.print(" "+elementi.elementAt(i).elementAt(j).getPesi().elementAt(1));
            }
            System.out.println();
         }
         System.out.println("---------------Stampo peso banda-----------");
         for(int i = 0;i<this.n;i++){   
            for(int j = 0;j<this.n;j++){
                if(elementi.elementAt(i).elementAt(j).getPesi().size()==0)
                    System.out.print(" 0");
                else System.out.print(" "+elementi.elementAt(i).elementAt(j).getPesi().elementAt(2));
            }
            System.out.println();
        }
    }
    
    

    private boolean eDestinazione(int i, Vector<Integer> destinazioni) {
        boolean trovato = false;
        for(int k = 0;k<destinazioni.size() && !trovato;k++)
            if(destinazioni.elementAt(k)==i)
                trovato = true;
        return trovato;
    }
    
    
    
    /**
     * Questo medoto permette di controllare se tutte le destinazioni sono coperte
     * 
     * @return true se tutte le destinazioni sono raggiunte false altrimenti
     */
    public boolean destinazioniCoperte(int radice,Vector<Integer> destinazioni){
        
        boolean tutte_coperte = true;
        
        
        boolean visitati[] = new boolean[this.n];
        
        for(int i = 0;i<n;i++)
            visitati[i] = false;
        int nodo_corrente = radice;
        visitati[nodo_corrente] = true;
        
        Vector<Integer> vicini = new Vector<Integer>();
        Vector<Integer> neighboors = getVicini(nodo_corrente);
        
        for(int i = 0;i<neighboors.size();i++) {
            vicini.add(neighboors.elementAt(i));
        }
        
        while(vicini.size()>0){
            //System.out.println("La pila contine ancora oggetti  :"+vicini.size());
           while(vicini.size()>0 && visitati[vicini.elementAt(0)])
               vicini.removeElementAt(0);
            if (vicini.size() > 0) {
                nodo_corrente = vicini.elementAt(0);
                visitati[nodo_corrente] = true;
                vicini.removeElementAt(0);
                //System.out.println("cerco i vicini del nodo corrente :"+nodo_corrente);
                neighboors.removeAllElements();
                neighboors = getVicini(nodo_corrente);
                for (int i = 0; i < neighboors.size(); i++) {
                    vicini.add(neighboors.elementAt(i));
                }
            }
           //System.out.println()
        }
        
        for(int j = 0;j<destinazioni.size() && tutte_coperte;j++)
            if(!visitati[destinazioni.elementAt(j)])
                tutte_coperte = false;
        return tutte_coperte;
      }
    
    public Grafo Dijkstra(int sorgente){
        Grafo soluzione = new Grafo(this.n);
        boolean visitati[] = new boolean[this.n];
        int precedenti[] = new int[this.n];
        double pesi[] = new double[this.n];
        double infinity = Double.MAX_VALUE;
        for(int i = 0;i<this.n;i++)
        {
            visitati[i] = false;
            pesi[i] = infinity;
            precedenti[i] = -1;
        }
        
        pesi[sorgente] = 0;
        visitati[sorgente] = true;
        int nodo_corrente = sorgente;
        
        int iterazione = 0;        
        while(nodo_corrente>=0)
        {
            Vector<Integer> vicini = this.getVicini(nodo_corrente);
            
            for(int i = 0;i<vicini.size();i++){
                
                if((!visitati[vicini.elementAt(i)])&&(pesi[nodo_corrente] + (this.elementi.elementAt(nodo_corrente).elementAt(vicini.elementAt(i)).getPesi().elementAt(0)))<pesi[vicini.elementAt(i)])
                    pesi[vicini.elementAt(i)] = pesi[nodo_corrente] + (this.elementi.elementAt(nodo_corrente).elementAt(vicini.elementAt(i)).getPesi().elementAt(0));
                    precedenti[vicini.elementAt(i)] = nodo_corrente;               
            }
            
            //System.out.println();
            double pesoC = infinity;
            
            int next_nodo = -1;
            for(int i = 0;i<this.n;i++)
               if(i != sorgente && pesi[i]<pesoC && !visitati[i]){ 
                   next_nodo = i;
                   pesoC = pesi[i];
               }
            
           
            
            if(next_nodo>=0)
            {
                
                Link l = new Link();
                l.setPesi(this.elementi.elementAt(precedenti[next_nodo]).elementAt(next_nodo).getPesi());
                soluzione.setArco(precedenti[next_nodo], next_nodo, l);
                visitati[next_nodo]=true;
            }
            nodo_corrente = next_nodo;
            iterazione++;
        }
        
        return soluzione;
    }
    
    /**
     * Questo metodo va usato se in pesi il valore del delay è al secondo posto
     * @param sorgente
     * @return soluzione a delay minimo
     */
    public Grafo Dijkstra_Delay(int sorgente){
        Grafo soluzione = new Grafo(this.n);
        boolean visitati[] = new boolean[this.n];
        int precedenti[] = new int[this.n];
        double pesi[] = new double[this.n];
        double infinity = Double.MAX_VALUE;
        for(int i = 0;i<this.n;i++)
        {
            visitati[i] = false;
            pesi[i] = infinity;
            precedenti[i] = -1;
        }
        
        pesi[sorgente] = 0;
        visitati[sorgente] = true;
        int nodo_corrente = sorgente;
        
        int iterazione = 0;        
        while(nodo_corrente>=0)
        {
            Vector<Integer> vicini = this.getVicini(nodo_corrente);
            
            for(int i = 0;i<vicini.size();i++){
                
                if((!visitati[vicini.elementAt(i)])&&(pesi[nodo_corrente] + (this.elementi.elementAt(nodo_corrente).elementAt(vicini.elementAt(i)).getPesi().elementAt(1)))<pesi[vicini.elementAt(i)])
                    pesi[vicini.elementAt(i)] = pesi[nodo_corrente] + (this.elementi.elementAt(nodo_corrente).elementAt(vicini.elementAt(i)).getPesi().elementAt(1));
                    precedenti[vicini.elementAt(i)] = nodo_corrente;               
            }
            
            //System.out.println();
            double pesoC = infinity;
            
            int next_nodo = -1;
            for(int i = 0;i<this.n;i++)
               if(i != sorgente && pesi[i]<pesoC && !visitati[i]){ 
                   next_nodo = i;
                   pesoC = pesi[i];
               }
            
           
            
            if(next_nodo>=0)
            {
                
                Link l = new Link();
                l.setPesi(this.elementi.elementAt(precedenti[next_nodo]).elementAt(next_nodo).getPesi());
                soluzione.setArco(precedenti[next_nodo], next_nodo, l);
                visitati[next_nodo]=true;
            }
            nodo_corrente = next_nodo;
            iterazione++;
        }
        
        return soluzione;
    }
    
    /**
     * Questo metodo va usato se in pesi il valore della banda è al terzo posto
     * @param sorgente
     * @return soluzione a delay minimo
     */
    public Grafo Dijkstra_Banda(int sorgente){
        Grafo soluzione = new Grafo(this.n);
        boolean visitati[] = new boolean[this.n];
        int precedenti[] = new int[this.n];
        double pesi[] = new double[this.n];
        double infinity = Double.MAX_VALUE;
        for(int i = 0;i<this.n;i++)
        {
            visitati[i] = false;
            pesi[i] = -1;
            precedenti[i] = -1;
        }
        
        pesi[sorgente] = 0;
        visitati[sorgente] = true;
        int nodo_corrente = sorgente;
        
        int iterazione = 0;        
        while(nodo_corrente>=0)
        {
            Vector<Integer> vicini = this.getVicini(nodo_corrente);
            
            for(int i = 0;i<vicini.size();i++){
                
                if((!visitati[vicini.elementAt(i)])&&((this.elementi.elementAt(nodo_corrente).elementAt(vicini.elementAt(i)).getPesi().elementAt(2)))>pesi[vicini.elementAt(i)])
                    pesi[vicini.elementAt(i)] = (this.elementi.elementAt(nodo_corrente).elementAt(vicini.elementAt(i)).getPesi().elementAt(2));
                    precedenti[vicini.elementAt(i)] = nodo_corrente;               
            }
            
            //System.out.println();
            double pesoC = -1;
            
            int next_nodo = -1;
            for(int i = 0;i<this.n;i++)
               if(i != sorgente && pesi[i]>pesoC && !visitati[i]){ 
                   next_nodo = i;
                   pesoC = pesi[i];
               }
            
           
            
            if(next_nodo>=0)
            {
                Link l = new Link();
                l.setPesi(this.elementi.elementAt(precedenti[next_nodo]).elementAt(next_nodo).getPesi());
                soluzione.setArco(precedenti[next_nodo], next_nodo, l);
                visitati[next_nodo]=true;
            }
            nodo_corrente = next_nodo;
            iterazione++;
        }
        
        return soluzione;
    }

}
