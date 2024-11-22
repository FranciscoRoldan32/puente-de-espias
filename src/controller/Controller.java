package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.interfaces.ICoordinate;

import grafo.Edge;
import grafo.Graph;
import grafo.Vertex;
import logica.MinimumGeneratingTree;
import lugar.Coordinates;
import lugar.Mapa_Manager;

import interfaz.Interfaz;
import interfaz.Interfaz_Principal;

public class Controller {
	private Interfaz interfaz;
    private Interfaz_Principal interfazPrincipal;
    private Mapa_Manager manejo;
    private Graph grafo;
    private MinimumGeneratingTree kruskal;

    private List<Edge> arbolGeneradorMinimo;
    private List<String> aggEspia;

    private JButton bottonAddSpy;
    private JButton buttonKruskal;
    private JButton bottonAddSpyConnectionGraph ;
    private JButton buttonDivideSpies;
   
    private JComboBox<Integer> comboBoxBestConection;
    
    private List<String> ListSpyEdges;
    
    private List<JComboBox<String>> listComboBoxSpy;
    private List<JComboBox<Integer>> listComboBoxWeight;
    
    private Map<String, Coordinates> spyNameLocations = new HashMap<>();

	public Controller(Interfaz_Principal interfazPrincipal, Interfaz interfaz, Graph grafo, MinimumGeneratingTree kruskal) {
		this.interfazPrincipal= interfazPrincipal;
		this.interfaz = interfaz;
		this.grafo = grafo;
		this.kruskal = kruskal;
		this.manejo = new Mapa_Manager(interfaz, grafo, this);
		
	
		 attachListenersForAddingSpies();

	}

	 private void attachListenersForAddingSpies() {
	        bottonAddSpy = interfaz.getBottonAddSpy();

	        bottonAddSpy.addActionListener(e -> {
	            aggEspia = new ArrayList<>();
	            String text = interfaz.getTextAreaVertices().getText();
	            if (text != null && !text.trim().isEmpty()) {
	                String[] vertices = text.split(",");
	                for (String vertex : vertices) {
	
	                    vertex = vertex.trim();
	                    if (!vertex.isEmpty()) {
	                        grafo.addVertex(vertex);
	                        aggEspia.add(vertex);
	                    }
	                }
	                
	                bottonAddSpy.setEnabled(false);
	               
	                if (!aggEspia.isEmpty()) {
	                    JOptionPane.showMessageDialog(null, "Haga clic en el mapa para agregar la ubicación de los espías.");
	                    interfaz.generSpyEdge(aggEspia);  
	                    manejo.activarModoAgregarUbicaciones(aggEspia);
	                    activarModoAgregarConexiones();
	                }
	            } else {
	                JOptionPane.showMessageDialog(null, "Por favor, ingrese al menos un vértice.", "Error", JOptionPane.ERROR_MESSAGE);
	            }
	        });
	    }
	 
	 private void activarModoAgregarUbicaciones(List<String> espias) {
	        manejo.activarModoAgregarUbicaciones(espias);
	        activarModoAgregarConexiones();
	        
	 }
	
	 
	private void activarModoAgregarConexiones() {
	    listComboBoxWeight = interfaz.getListComboBoxWeight();
	    listComboBoxSpy = interfaz.getListComboBoxSpy();
	    
	    buttonKruskal =interfaz.getBottonKruskal();
	    
	    bottonAddSpyConnectionGraph = interfaz.getBottonAddSpyConnectionGraph();
//	    if (listComboBoxSpy == null || listComboBoxWeight == null || aggEspia == null) {
//	        System.out.println("Una de las listas es null: ");
//	        System.out.println("listComboBoxSpy: " + listComboBoxSpy);
//	        System.out.println("listComboBoxWeight: " + listComboBoxWeight);
//	        System.out.println("aggEspia: " + aggEspia);
//	        return;
//	    }
//	    System.out.println("todo bien ");
//	    if (listComboBoxSpy.size() != listComboBoxWeight.size() || listComboBoxSpy.size() != aggEspia.size()) {
//	        System.out.println("Las listas no tienen el mismo tamaño.");
//	        System.out.println("listComboBoxSpy.size(): " + listComboBoxSpy.size());
//	        System.out.println("listComboBoxWeight.size(): " + listComboBoxWeight.size());
//	        System.out.println("aggEspia.size(): " + aggEspia.size());
//	        return;
//	    }
	    System.out.println("todo bien ");
	    System.out.println("Estado del botón: " + bottonAddSpyConnectionGraph.isEnabled());
	    bottonAddSpyConnectionGraph.addActionListener(e -> {
//	        for (int i = 0; i < listComboBoxWeight.size(); i++) {
//	            if (!listComboBoxSpy.get(i).getSelectedItem().toString().equals("No seleccionado")) {
//	                String src = aggEspia.get(i);  
//	                String dest = listComboBoxSpy.get(i).getSelectedItem().toString();  
//	                Integer weight = listComboBoxWeight.get(i).getSelectedIndex() + 1; 
//	                System.out.println(src);
//	                System.out.println(dest);
//	                grafo.addEdge(grafo.getVertex(src), grafo.getVertex(dest), weight);
//	  
//	            }
//	        }
	        System.out.println("Estado del botón: " + bottonAddSpyConnectionGraph.isEnabled());
	        
//	        
//	        List<Edge> mst = grafo.getAllEdges();
//	        
//	        ListSpyEdges = grafo.getAllTheEdgesInStrings();
//	        buttonKruskal.setEnabled(true);
//	        interfaz.removePreviewsMappolygons();
//	        spyNameLocations = manejo.obtenerUbicaciones();
//	        
//	       for (Edge edge : mst) {
//	    	   System.out.println(edge.toString());
//	    	   Coordinates src = spyNameLocations.get(edge.getSrc().getLabel());
//	    	   Coordinates dest = spyNameLocations.get(edge.getDest().getLabel());
//	    	   
//	    	   Coordinate srcCoords = new Coordinate(src.getLatitude(), src.getLongitude());
//               Coordinate destCoords = new Coordinate(dest.getLatitude(), dest.getLongitude());
//               
//               List<Coordinate> route = Arrays.asList(srcCoords, destCoords, destCoords, srcCoords);
//               interfaz.createMapPoligonMark2(route);
//               
//	       }
//	        
	    });
	    buttonKruskal.addActionListener(e -> {
	    	System.out.println("mmm");
//	    	spyNameLocations = interfaz.getSpyNameLocations();
//	    	
//	    	arbolGeneradorMinimo = kruskal.minimumSpanningTree(grafo);
//	    	spyNameLocations = manejo.obtenerUbicaciones();
//	    	if(arbolGeneradorMinimo !=null ){
//	    		interfaz.removeCheckBoxElements();
//	    		interfaz.createCheckboxDivideCountry(arbolGeneradorMinimo.size());
//	    		buttonKruskal.setEnabled(false);
//	    		bottonAddSpyConnectionGraph.setEnabled(false);
//	    		
//	    		}  else{
//	                JOptionPane.showMessageDialog(null, "Recuerde que el grafo tiene que estar conectado", "El grafo no es Conexo: ", JOptionPane.ERROR_MESSAGE);
//	            }
	    });
	 
	}
	    
}

