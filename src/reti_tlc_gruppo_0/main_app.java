/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reti_tlc_gruppo_0;

import base_simulator.NetworkInterface;
import base_simulator.Infos;
import base_simulator.Applicazione;
import base_simulator.Grafo;
import base_simulator.canale;
import base_simulator.layers.LinkLayer;
import base_simulator.layers.NetworkLayer;
import base_simulator.layers.TransportLayer;
import base_simulator.layers.physicalLayer;
import base_simulator.link_extended;
import base_simulator.scheduler;

import java.io.File;

import java.io.IOException;
import java.util.List;
import javax.swing.JFileChooser;


import org.jdom2.Document;

import org.jdom2.Element;

import org.jdom2.JDOMException;

import org.jdom2.input.SAXBuilder;

/**
 *
 * @author afsantamaria
 */
public class main_app extends javax.swing.JFrame {

    private static scheduler s;

    private static void init_sim_parameters() {
        s = new scheduler(100000, false);
    }

    private String conf_file_path;

    /**
     * Creates new form main_app
     */
    public main_app() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton2 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jCheckBox1 = new javax.swing.JCheckBox();
        jButton3 = new javax.swing.JButton();

        jButton2.setText("jButton2");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Applicazione SImulatore");
        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(3, 169, 244));

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 3, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Progetto Reti TLC - Gruppo 0");

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Descrizione breve Progetto");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 139, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(131, 131, 131))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(182, 182, 182))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jButton1.setBackground(new java.awt.Color(205, 220, 57));
        jButton1.setForeground(new java.awt.Color(3, 169, 244));
        jButton1.setText("START");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabel3.setText("Stato Configurazione");

        jTextField1.setText("conf.xml");
        jTextField1.setBorder(null);

        jCheckBox1.setForeground(new java.awt.Color(153, 255, 51));
        jCheckBox1.setEnabled(false);

        jButton3.setBackground(new java.awt.Color(255, 255, 255));
        jButton3.setForeground(new java.awt.Color(51, 153, 255));
        jButton3.setText("...");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(91, 91, 91)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE))
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCheckBox1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(53, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(243, 243, 243))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jCheckBox1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        //1 . Load config file, then check its validility

        File conf_file = new File(jTextField1.getText());
        boolean res =false;
        if (conf_file.exists()) {
             res = startParsing(conf_file);
        } else if (conf_file_path != null) {
            conf_file = new File(conf_file_path);
            res = startParsing(conf_file);
        } else {
            System.out.println("File non esistente");
        }

        //2 . File is valid! Create Network
        if(res == true)
        {
            this.jCheckBox1.setSelected(true);
        }
        //3 . Ready to start simulation
        new Thread(s).start();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        File f = new File(".");
        final JFileChooser fc = new JFileChooser();
        fc.setDialogType(JFileChooser.OPEN_DIALOG);
        fc.setCurrentDirectory(f);
//In response to a button click:
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            this.jTextField1.setText(fc.getSelectedFile().getName());
            conf_file_path = fc.getSelectedFile().getAbsolutePath();

        }
    }//GEN-LAST:event_jButton3ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(main_app.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(main_app.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(main_app.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(main_app.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        init_sim_parameters();
        
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new main_app().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables

    private Infos info = new Infos();
    
    

    private boolean startParsing(File xmlFile) {
        SAXBuilder saxBuilder = new SAXBuilder();
        boolean res = false;
        try {

            Document document = (Document) saxBuilder.build(xmlFile);

            Element rootElement = document.getRootElement();

            
            List listElement = rootElement.getChildren("project");
            
            for(Object o : listElement)
            {
                Element node = (Element) o;
                List listDescrizione = node.getChildren("descrizione");
                for(Object element:listDescrizione)
                {
                    Element desc = (Element)element;
                    String t = desc.getText();
                    this.jLabel4.setText(t);
                }
            }
            
            
            listElement = rootElement.getChildren("canali");

            for (int i = 0; i < listElement.size(); i++) {
                /*
<canale id="0" tipo="WIRED" capacita="5000000" dim_pacchetto = "1526" tempo_propagazione = "10"></canale> 
                 */
                Element node = (Element) listElement.get(i);

                List listElement1 = node.getChildren("canale");
                for (Object listElement2 : listElement1) {

                    int id = Integer.valueOf(((Element) listElement2).getAttributeValue("id"));
                    double capacita = Double.valueOf(((Element) listElement2).getAttributeValue("capacita"));
                    System.out.println("tipo: "
                            + ((Element) listElement2).getAttributeValue("tipo"));
                    double dim_pckt = Double.valueOf(((Element) listElement2).getAttributeValue("dim_pacchetto"));

                    double tempo_prop = Double.valueOf(((Element) listElement2).getAttributeValue("tempo_propagazione"));

                    canale c = new canale(s, id, capacita, dim_pckt, tempo_prop);

                    info.addCanale(c);
                }
            }

            listElement = rootElement.getChildren("nodo_host");

            for (Object nodo : listElement) {
                
                int id = Integer.valueOf(((Element) nodo).getAttributeValue("id"));
                int gateway = Integer.valueOf(((Element) nodo).getAttributeValue("gateway"));
                int numero_nodi = Integer.parseInt(((Element) nodo).getAttributeValue("net_size"));
                Grafo grafo = new Grafo(numero_nodi);
                
                physicalLayer pl = new physicalLayer(s, 0.0);
                LinkLayer ll = new LinkLayer(s, 5.0);
                netLayerLinkState nl = new netLayerLinkState(s, 5.0,grafo);
                tcpTransportLayer tl = new tcpTransportLayer(s,5.0);

                
                nodo_host nh = new nodo_host(s, id, pl, ll, nl,tl, null, "nodo_host", gateway);

                pl.connectPhysicalLayer(ll, nh);
                ll.connectLinkLayer(pl, nl, nh);
                nl.connectNetworkLayer(tl,ll, nh);
                tl.connectTransportLayer(nl, nh);
                
                //Setto gli elementi relativi al networking
                nl.setDefaultGateway(gateway);

//                Element node = (Element) listElement.get(i);
//                List listElement1 = node.getChildren("canale");
                List listElement1 = ((Element) nodo).getChildren("interfaces");
                
                
                for (Object interfaces_list : listElement1) {
                    
                    List intertace_list = ((Element) interfaces_list).getChildren("interface");
                                        
                    for (Object obj_interfaccia : intertace_list) {
                        System.out.println("idinterfaccia:" + ((Element) obj_interfaccia).getAttributeValue("id"));
                        int if_id = Integer.valueOf(((Element) obj_interfaccia).getAttributeValue("id"));
                        String IP = ((Element) obj_interfaccia).getAttributeValue("IP");
                        int dest = Integer.valueOf(((Element) obj_interfaccia).getAttributeValue("dest"));
                        int channelId = Integer.valueOf(((Element) obj_interfaccia).getAttributeValue("canale"));
                        double metrica = Double.valueOf(((Element) obj_interfaccia).getAttributeValue("metrica"));
                        NetworkInterface nic = new NetworkInterface(if_id, IP, dest,channelId,metrica);
                        nh.addNIC(nic);
 //TODO:Da fare inserimento statico delle entry nelle tabelle di routing, sulle interfacce dei vicini 
 /**
  * La entry dovrebbe essere del tipo
  * DEST NEXTHOP COSTO
  * dest dest    metrica
  */
                        nl.addRoutingTableEntry(dest,dest,metrica);
                        
                        grafo.setCosto(nh.getId(), dest, metrica,0.0);
                        grafo.setCosto(dest,nh.getId(), metrica,0.0);
                    }
                }
                
                listElement1 = ((Element) nodo).getChildren("application");

                for (Object application_element : listElement1) {
                    Element app_item = (Element)application_element;
                    String tipo = app_item.getAttributeValue("type");
                    
                    Double rate = Double.valueOf(app_item.getAttributeValue("rate"));
                    int TON = Integer.valueOf(app_item.getAttributeValue("TON"));
                    int TOFF = Integer.valueOf(app_item.getAttributeValue("TOFF"));
                    int port = Integer.valueOf(app_item.getAttributeValue("port"));
                    int dest = Integer.valueOf(app_item.getAttributeValue("dest"));
                    double size = Double.valueOf(app_item.getAttributeValue("size"));
                    int start = Integer.valueOf(app_item.getAttributeValue("start"));
                    double pckt_size = Double.valueOf(app_item.getAttributeValue("pckt_size"));
                    String payload = app_item.getAttributeValue("payload");
                    String filePath = app_item.getAttributeValue("file");
                    int availableSpaceForSession = Integer.valueOf(app_item.getAttributeValue("availableSpaceForSession"));
                    Applicazione app = new Applicazione(rate,TON,TOFF,port,dest,size,pckt_size,tipo,start,payload,filePath); 
                    
                    if(app.getTipo().equals(app.SIMPLE_APP_TCP))
                    {
                        //1. Aggiungo applicazione solo se sorgente abilitando la porta
                        //2. Sulla destinazione sarà aperta la porta solo su esplicita richiesta da parte di una sorgente
                        nh.addApplicazione(app);
                    }
                    
                    tl.setAvailableSpaceForSession(availableSpaceForSession * (int)pckt_size);
                            
                }
                

                System.out.println("Creato nodo_host con id: " + nh.getId() + " e gateway:" + nh.getGTW());

                info.addNodo(nh);

            }

            listElement = rootElement.getChildren("router");

            for (Object routers_list : listElement) {
                
                
                int node_id = Integer.valueOf(((Element) routers_list).getAttributeValue("id"));
                int gateway = Integer.valueOf(((Element) routers_list).getAttributeValue("gateway"));
                int numero_nodi = Integer.parseInt(((Element) routers_list).getAttributeValue("net_size"));
                Grafo grafo = new Grafo(numero_nodi);
                
                physicalLayer pl = new physicalLayer(s, 0.0);
                LinkLayer ll = new LinkLayer(s, 5.0);
                netLayerLinkState nl = new netLayerLinkState(s, 5.0, grafo);
                TransportLayer tl = new TransportLayer(s,5.0);
                
                
                nodo_router nr = new nodo_router(s, node_id, pl, ll, nl, tl,null, "nodo_router", 0);
//PHY
                pl.connectPhysicalLayer(ll, nr);
//LL                
                ll.connectLinkLayer(pl, nl, nr);
//NET                
                nl.connectNetworkLayer(tl,ll, nr);                
                nl.setDefaultGateway(gateway);
//TRASP                
                tl.connectTransportLayer(nl, nr);
                
                System.out.println("Ho aggiunto un " + nr.getTipo() + " con id..:" + nr.getId());

                List protocol_list = ((Element) routers_list).getChildren("protocol");

                for (Object protocol_element : protocol_list) {
                    
                    Element item = (Element)protocol_element;

                    String tipo = item.getAttributeValue("tipo");
                    int TTL = 0;
                    
                    if (tipo.equals("OSPF")) {
                        TTL = Integer.valueOf(item.getAttributeValue("TTL"));
                        
                        nl.enableFullOSPF();
                        nl.setTTL_LSA(TTL);
                    }

                    String routing = item.getAttributeValue("ROUTING");
/**
 * TODO : These parameters may be erased, The routing rules are moved to nl specialization
 */
                    nr.setProtocol(tipo);
                    nr.setRouting(routing);
                    nr.setTTL(TTL);
                }
                
                
                List listElement1 = ((Element) routers_list).getChildren("interfaces");
                
                
                
                
                //Faccio la clear dei rami allocati e alloco una nuova struttura dati
                //popolo il grafo solo con i propri vicini                
                
                
                for (Object interfaces_list : listElement1) {
                    List intertace_list = ((Element) interfaces_list).getChildren("interface");

                    for (Object obj_interfaccia : intertace_list) {
                        System.out.println("idinterfaccia:" + ((Element) obj_interfaccia).getAttributeValue("id"));
                        int if_id = Integer.valueOf(((Element) obj_interfaccia).getAttributeValue("id"));
                        String IP = ((Element) obj_interfaccia).getAttributeValue("IP");
                        int channelId = Integer.valueOf(((Element) obj_interfaccia).getAttributeValue("canale"));
                        int dest = Integer.valueOf(((Element) obj_interfaccia).getAttributeValue("dest"));
                        double metrica = Double.valueOf(((Element) obj_interfaccia).getAttributeValue("metrica"));
                        NetworkInterface nic = new NetworkInterface(if_id, IP, dest,channelId,metrica);
                        nr.addNIC(nic);
                        
                        //Inserimento dati di routing
 /**
  * La entry dovrebbe essere del tipo
  * DEST NEXTHOP COSTO
  * dest dest    metrica
  */
                        nl.addRoutingTableEntry(dest,dest,metrica);
                        
//Popolazione iniziale topologia                        
                        grafo.setCosto(nr.getId(), dest, metrica,0.0);
                        grafo.setCosto(dest,nr.getId(), metrica,0.0);
                        
                    }
                }

                info.addNodo(nr);
                
                

            }
            
            listElement = rootElement.getChildren("network");
            
            for(Object network_list : listElement)
            {
                List branches = ((Element)network_list).getChildren("ramo");
                for(Object branch_element : branches)
                {
                    Element branch = ((Element)branch_element);
                    
                    
                    double metric = Double.valueOf(branch.getAttributeValue("metrica"));
                    int nodo_iniziale = Integer.valueOf(branch.getAttributeValue("start"));
                    int nodo_finale = Integer.valueOf(branch.getAttributeValue("end"));
                    String tipo = branch.getAttributeValue("tipo");
                    link_extended l = new link_extended(nodo_iniziale,nodo_finale,metric);
                    info.addLink(l);
                    
                    if(tipo.equals("full"))
                    {
                        link_extended l1 = new link_extended(nodo_finale,nodo_iniziale,metric);
                        info.addLink(l1);
                        
                    }
                }
            }
            
            s.setInfo(info);

        } catch (IOException io) {
            System.out.println(io.getMessage());
        } catch (JDOMException jdomex) {

            System.out.println(jdomex.getMessage());

        }
        return res;
    }
    
}
