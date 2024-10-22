package controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import org.openstreetmap.gui.jmapviewer.Coordinate;

import grafo.Edge;
import grafo.Graph;
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

	List<Edge> arbolGeneradorMinimo;

	private JButton bottonAddSpy;

	private JButton bottonKruskal;

	private List<JCheckBox> checkBoxList;

	private List<String> aggEspia;

	private List<JComboBox<String>> listComboBoxSpy;
	private List<JComboBox<Integer>> listComboBoxWeight;
	private JButton bottonAddSpyConnectionGraph;
	@SuppressWarnings("unused")
	private List<String> listSpyEdges;

	private Map<String, Coordinates> spyNameLocations = new HashMap <>() ;

	private JButton bottonBestConections;

	private JComboBox<Integer> comboBoxBestConection;

	public Controller(Interfaz_Principal interfazPrincipal, Interfaz interfaz, Graph grafo, MinimumGeneratingTree kruskal) {
		this.interfazPrincipal= interfazPrincipal;
		this.interfaz = interfaz;
		this.grafo = grafo;
		this.kruskal = kruskal;
		this.manejo = new Mapa_Manager(interfaz, grafo);
		attachlistenersbutton();

	}

	private void attachlistenersbutton() {
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
		                JOptionPane.showMessageDialog(null, "Por favor, haga clic en el mapa para agregar la ubicación de los espías.");
		                manejo.activarModoAgregarUbicaciones(aggEspia); // Ahora llama al ManejadorMapa
		            }
		        } else {
		            JOptionPane.showMessageDialog(null, "Por favor, ingrese al menos un vértice.", "Error", JOptionPane.ERROR_MESSAGE);
		        }
		    });
	}

	
	private void attachListenersButtonGraph() {
		listComboBoxSpy = interfaz.getListComboBoxProvince();
	    listComboBoxWeight = interfaz.getListComboBoxWeight();

	    bottonKruskal = interfaz.getBottonKruskal();
	    bottonAddSpyConnectionGraph = interfaz.getBottonAddSpyConnectionGraph();

	    // Listener para agregar conexiones entre espías
	    bottonAddSpyConnectionGraph.addActionListener(e -> {
	        for (int i = 0; i < listComboBoxWeight.size(); i++) {
	            if (!listComboBoxSpy.get(i).getSelectedItem().toString().equals("No seleccionado")) {
	                String src = aggEspia.get(i); // Origen
	                String dest = listComboBoxSpy.get(i).getSelectedItem().toString(); // Destino
	                Integer weight = listComboBoxWeight.get(i).getSelectedIndex() + 1; // Peso

	                // Verificar que el origen y destino no sean iguales
	                if (!src.equals(dest)) {
	                    grafo.addEdge(grafo.getVertex(src), grafo.getVertex(dest), weight);
	                } else {
	                    JOptionPane.showMessageDialog(null, "El origen y el destino no pueden ser iguales.", "Error", JOptionPane.ERROR_MESSAGE);
	                }
	            }
	        }

	        List<Edge> mst = grafo.getAllEdges();
	        listSpyEdges = grafo.getAllTheEdgesInStrings();
	        bottonKruskal.setEnabled(true);
	        interfaz.removePreviewsMappolygons();

	        // Ahora las coordenadas de los espías se obtienen de la interfaz (clics en el mapa)
	        for (Edge edge : mst) {
	            Coordinates srcCoords = interfaz.getCoordenadasEspia(edge.getSrc().getLabel());
	            Coordinates destCoords = interfaz.getCoordenadasEspia(edge.getDest().getLabel());

	            if (srcCoords != null && destCoords != null) {
	                Coordinate srcCoordinate = new Coordinate(srcCoords.getLatitude(), srcCoords.getLongitude());
	                Coordinate destCoordinate = new Coordinate(destCoords.getLatitude(), destCoords.getLongitude());

	                List<Coordinate> route = Arrays.asList(srcCoordinate, destCoordinate);
	                interfaz.createMapPoligonMark2(route);
	            }
	        }
	    });

	    // Listener para ejecutar Kruskal y mostrar el árbol generador mínimo
	    bottonKruskal.addActionListener(e -> {
	        arbolGeneradorMinimo = kruskal.minimumSpanningTree(grafo);
	        if (arbolGeneradorMinimo != null) {
	            interfaz.removeCheckBoxElements();
	            interfaz.createCheckboxDivideCountry(arbolGeneradorMinimo.size());
	            bottonKruskal.setEnabled(false);
	            bottonAddSpyConnectionGraph.setEnabled(false);
	            attachListenersButtonBestConnection();
	        } else {
	            JOptionPane.showMessageDialog(null, "Recuerde que el grafo tiene que estar conectado", "El grafo no es Conexo: ", JOptionPane.ERROR_MESSAGE);
	        }
	    });

    }
	
	private void attachListenersButtonBestConnection(){
		bottonBestConections = interfaz.getBottonDivideCountry();
		comboBoxBestConection = interfaz.getComboBoxDivideCountry();

		bottonBestConections.addActionListener(e -> {
            interfaz.removePreviewsMappolygons();
            List<String> listaDeProvinciaArgentina = new ArrayList<>();
            if(comboBoxBestConection.getSelectedItem().hashCode() > 1){
                arbolGeneradorMinimo = grafo.deletedHeavieEdge(arbolGeneradorMinimo, comboBoxBestConection.getSelectedItem().hashCode() - 1);
            }
            for (Edge edge : arbolGeneradorMinimo) {
                listaDeProvinciaArgentina.add(edge.getSrc().getLabel() + " --> " + edge.getDest().getLabel() +  " ("+ edge.getWeight() + ") ");

                Coordinates src = spyNameLocations.get(edge.getSrc().getLabel());
                Coordinates dest = spyNameLocations.get(edge.getDest().getLabel());

                Coordinate srcCoordiante = new Coordinate(src.getLatitude(), src.getLongitude());
                Coordinate destCoordiante = new Coordinate(dest.getLatitude(), dest.getLongitude());


                List<Coordinate> route = Arrays.asList(srcCoordiante, destCoordiante, destCoordiante, srcCoordiante);
                interfaz.createMapPoligonMark2(route);

                }

                interfaz.createStringOfTheGraph(listaDeProvinciaArgentina, grafo.generateAdjacencyMap());
                bottonBestConections.setEnabled(false);
                comboBoxBestConection.setEnabled(false);
        });
        
    }
	 public void asignarUbicacionEspia(String nombreEspia, Coordinates coordenadas) {
	        if (nombreEspia != null && !nombreEspia.isEmpty() && coordenadas != null) {
	            interfaz.getSpyNameLocations().put(nombreEspia, coordenadas);  // Almacena las coordenadas
	        } else {
	            JOptionPane.showMessageDialog(null, "Error: El nombre del espía o las coordenadas son inválidos", "Error", JOptionPane.ERROR_MESSAGE);
	        }
	    }

	
}
