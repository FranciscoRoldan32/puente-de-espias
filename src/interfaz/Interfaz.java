package interfaz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


import grafo.Edge;
import lugar.Coordinates;



import javax.swing.*;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;
import org.openstreetmap.gui.jmapviewer.interfaces.ICoordinate;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Path2D;
import java.io.File;



public class Interfaz extends JPanel {
	
	private int width; // ancho
	private int height; // largo
	
	private int positionPanelX, postionPanelY;
	
	
 	private JPanel panelElementsLeft;
 	
 	private JPanel title;
    private JTextField JTextTitulo;

    private JMapViewer mapViewer;
    private JPanel panelMap;
    
    private JButton bottonAddSpyConnectionGraph;
    private JButton bottonKruskal;
   
    private JPanel panelCheckBox;
    private JPanel panelConectionEdges;
    private static List<JCheckBox> checkBoxList;
    private List<String> vertexList = new ArrayList<>();
    private JPanel panelBottons;
    private JTextArea textAreaVertices;
   
    private String[] vertices;
    private Map<String, Coordinates> espias = new HashMap <>();
   
    private JButton bottonAddEspia, buttonBestLines ;
    private Panel panelDivideSpies;
    
    private String rutaImg2="/img/espiaIcono.png",rutaImg1="/img/narrow_res.PNG";
    
 	private Archivo arch;
 	
 	private List<JComboBox<String>> listComboBoxProvince;
    private List<JComboBox<Integer>> listComboBoxWeight;
    private JComboBox<Integer> comboBoxDivideSpies;
     
    private Coordinate argentina;
	
	private boolean espiasRepetidos() {
	    HashSet<String> set = new HashSet<>();
	    for (String nombre : vertices) {
	        // Si el nombre ya está en el set, es un duplicado
	        if (!set.add(nombre)) {
	            return true; // Hay al menos un repetido
	        }
	    }
	    return false; 
	}
	
	private boolean cantidadJustaEspias() {
		return vertices.length>=2;
	}

	private boolean ceroEspias() {
		return vertices==null || vertices.length==0;
	}
	
    public Interfaz(int width, int height) {
    	this.width = width;
        this.height = height;
        initialize();
    }
    private void initialize() {
        setLayout(null);
        setPreferredSize(new Dimension (width, height));
        setBackground(Color.GRAY);
        
        //Esto genera el panel y el mapa
        generatedMapPanel();

        generatedPanel();
        generatedTitle();
        generatedSpyCheckBox(); 

    }
    
    private void generatedPanel() {
        panelElementsLeft = new JPanel();
        panelElementsLeft.setBackground(Color.black);
        panelElementsLeft.setLayout(null);
        panelElementsLeft.setBounds(width-415,0,500,height);
        add(panelElementsLeft);
    }
    
    private void generatedMapPanel() {
        panelMap = new JPanel();
        positionPanelX = 20;
        postionPanelY = -5;
        panelMap.setBounds(positionPanelX, postionPanelY, width/2, height);
        panelMap.setBackground(Color.GREEN);


        generatedMap();
        panelMap.add(mapViewer);

        add(panelMap);
    }
    private void generatedMap() {
    	mapViewer = new JMapViewer();
        mapViewer.setBorder(null);
        mapViewer.setZoomControlsVisible(false);
        mapViewer.setPreferredSize(new Dimension(width / 2, height));
        mapViewer.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        argentina = new Coordinate(-40.2, -63.616);
        mapViewer.setDisplayPosition(argentina, 5);

        // Agregar MouseListener al mapViewer para capturar los clics
        mapViewer.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                Coordinate clickCoordinate = (Coordinate) mapViewer.getPosition(e.getPoint());
                addVertexToMap(clickCoordinate);
            }
        });
    }
    
   
    private void generatedTitle() {
		title = new JPanel();
		title.setBounds(0, 5, 400, 50);
		JTextTitulo = new JTextField();
		JTextTitulo.setFont(new Font("Unispace", Font.BOLD, 27));
		JTextTitulo.setText("Puente de Espias");
		
		JTextTitulo.setEditable(false);
		JTextTitulo.setBorder(null);
		title.add(JTextTitulo);
		panelElementsLeft.add(title);
    }
    

    private void generatedSpyCheckBox() {
        panelCheckBox = new JPanel();
        panelCheckBox.setBounds(0, 60, 400, 300);
        panelCheckBox.setLayout(new BorderLayout());
        
        JLabel labelInstructions = new JLabel("Ingrese los nombres de los vértices separados por comas:");
        panelCheckBox.add(labelInstructions, BorderLayout.NORTH);
        
        textAreaVertices = new JTextArea();
        panelCheckBox.add(new JScrollPane(textAreaVertices), BorderLayout.CENTER);

        bottonAddEspia = new JButton("Agregar Espías al Mapa");
        panelCheckBox.add(bottonAddEspia, BorderLayout.SOUTH);

        panelElementsLeft.add(panelCheckBox);
        
        // Agregar acción al botón para generar los espías en el mapa
        bottonAddEspia.addActionListener(e -> addVerticesFromTextArea());
    }
    
    private void addVerticesFromTextArea() {
        String text = textAreaVertices.getText();
        vertexList.clear();
        if (!text.isEmpty()) {
            String[] vertices = text.split(",");
            for (String vertex : vertices) {
                vertexList.add(vertex.trim());
            }
        }
    }

    private void addVertexToMap(Coordinate coordinate) {
        if (!vertexList.isEmpty()) {
            String vertexName = vertexList.remove(0);
            MapMarkerDot marker = new MapMarkerDot(vertexName, coordinate);
            marker.getStyle().setBackColor(Color.RED);
            marker.getStyle().setColor(Color.BLUE);
            mapViewer.addMapMarker(marker);

            // Actualizar mapa
            mapViewer.revalidate();
            mapViewer.repaint();
        }
    }
    
    public void generSpyEdge(List<String> selectSpy){
        usedListForSpyEdges(selectSpy);
    }
    private void usedListForSpyEdges(List<String> selectSpy){
        panelConectionEdges = new JPanel();
        panelConectionEdges.setBounds(0,365,400,528);
        panelElementsLeft.add(panelConectionEdges);
        panelConectionEdges.setLayout(null); 

        //Esto para que la position del panel quede bien

        int positonX = 0;

        listComboBoxProvince = new ArrayList<>();
        listComboBoxWeight = new ArrayList<>();

        for (String nameSpy : selectSpy) {
            double latitude = espias.get(nameSpy).getLatitude();
            double longitude = espias.get(nameSpy).getLongitude();

            MapMarkerDot marker = new MapMarkerDot(nameSpy, new Coordinate(latitude, longitude));

            marker.getStyle().setBackColor(Color.RED);
            marker.getStyle().setColor(Color.BLUE);

            mapViewer.addMapMarker(marker);
            JPanel rowPanel = new JPanel(new GridLayout(1, 1));
            rowPanel.setBounds(0,positonX,400,20);
            rowPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            positonX = positonX + 22;
            JLabel label = new JLabel(nameSpy);
            rowPanel.add(label);
        
            JComboBox<String> comboBox1 = new JComboBox<>(createComboBoxSpy(selectSpy, nameSpy));
            rowPanel.add(comboBox1);
        
            JComboBox<Integer> comboBox2 = new JComboBox<>(createComboBoxModel(10)); 
            rowPanel.add(comboBox2); 
            

            listComboBoxProvince.add(comboBox1);
            listComboBoxWeight.add(comboBox2);

            panelConectionEdges.add(rowPanel);
            
        }
        generatedBottonsGraph();
        panelConectionEdges.revalidate();
        panelConectionEdges.repaint();

        panelElementsLeft.revalidate();
        panelElementsLeft.repaint();

    }
    
    private DefaultComboBoxModel<String> createComboBoxSpy(List<String> selectSpy, String nameSpy) {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("No seleccionado");
        for (String spy : selectSpy ) {
            if(nameSpy != spy){
                model.addElement(spy);
            }
        }
        return model;
    }
    
    private DefaultComboBoxModel<Integer> createComboBoxModel(int limited) {
        DefaultComboBoxModel<Integer> model = new DefaultComboBoxModel<>();
        for (int i = 1; i <= limited; i++) {
            model.addElement(i);
        }
        return model;
    }
    
    private DefaultComboBoxModel<Integer> createComboBoxBestConnections(int limited) {
        DefaultComboBoxModel<Integer> model = new DefaultComboBoxModel<>();

        if(limited == 1){
            model.addElement(1);
        } else{
            for (int i = 2; i <= limited; i++) {
                model.addElement(i);
            }
        }
        return model;
    }
    
    private void generatedBottonsGraph() {
        panelBottons = new JPanel();
        panelBottons.setBounds(0,900,400,40);
        panelElementsLeft.add(panelBottons);

        bottonAddSpyConnectionGraph = new JButton("Agregar la conexión");
        panelBottons.add(bottonAddSpyConnectionGraph);

        bottonKruskal = new JButton("Ejecutar algoritmo");
        bottonKruskal.setEnabled(false);
        panelBottons.add(bottonKruskal);

    }
    
    public void removeCheckBoxElements(){
        panelCheckBox.removeAll();
        revalidate();
        repaint();
    }
    public void createCheckboxDivideCountry(int limited){

        panelCheckBox.setLayout(null); 
        panelCheckBox.setBounds(0,60,400,300);


        panelDivideSpies = new Panel();
        panelDivideSpies.setBounds(0,10,400,70);
        panelCheckBox.add(panelDivideSpies);

        JLabel headerLabel = new JLabel("¿En cuantas regiones queres separar el pais? ");
        panelDivideSpies.add(headerLabel);


        comboBoxDivideSpies = new JComboBox<>(createComboBoxBestConnections(limited)); 

        panelDivideSpies.add(comboBoxDivideSpies); 


        buttonBestLines = new JButton("Generar el grafo y texto");
        panelDivideSpies.add(buttonBestLines);

        

    }


    public void removePreviewsMappolygons(){
        mapViewer.removeAllMapPolygons();
        mapViewer.revalidate();
        mapViewer.repaint();
    }

    public void createMapPoligonMark2(List<Coordinate> route ){
        mapViewer.addMapPolygon(new MapPolyLine(route));
        mapViewer.addMapPolygon(new MapPolyLine(route));
        mapViewer.revalidate();
        mapViewer.repaint();
    }

    public class MapPolyLine extends MapPolygonImpl {
        public MapPolyLine(List<? extends ICoordinate> points) {
            super(null, null, points);
        }
    
        @Override
        public void paint(Graphics g, List<Point> points) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(getColor());
            g2d.setStroke(getStroke());
            Path2D path = buildPath(points);
            g2d.draw(path);
            g2d.dispose();
        }
    
        private Path2D buildPath(List<Point> points) {
            Path2D path = new Path2D.Double();
            if (points != null && points.size() >= 1) {
                Point firstPoint = points.get(0);
                path.moveTo(firstPoint.getX(), firstPoint.getY());
                for (int i = 0; i < points.size(); i++) { 
                    Point p = points.get(i);
                    path.lineTo(p.getX(), p.getY());
                }
            }
            return path;
        }
    }

    public void createStringOfTheGraph(List<String> kruskal, List<String> originalGraph) {
        panelConectionEdges.removeAll();
        panelConectionEdges.revalidate();
        panelConectionEdges.repaint();
        // Esto es para mostrar el grafo Original
        addGraphToPanel(originalGraph, "Este es el gráfico original.");

        // Esto es para mostrar el grafo de Kruskal
        addGraphToPanel(kruskal, "Este es el gráfico de Kruskal.");


        panelConectionEdges.revalidate();
        panelConectionEdges.repaint();
    }

    
    private void addGraphToPanel(List<String> graphRepresentation, String header) {
    
        int positionY = panelConectionEdges.getComponentCount() * 20;; // Posición inicial para la primera fila.
    
        JPanel headerPanel = new JPanel(new GridLayout(1, 1));
        JLabel headerLabel = new JLabel(header);
        headerPanel.add(headerLabel);
        headerPanel.setVisible(true);
        headerPanel.setBounds(0, positionY, 400, 20);
        panelConectionEdges.add(headerPanel);
    
        positionY += 20; // Se incrementa para que el texto quede bien
    
        for (String line : graphRepresentation) {
            JPanel rowPanel = new JPanel(new GridLayout(1, 1));
            JLabel label = new JLabel(line.toString());
            rowPanel.setBounds(0, positionY, 400, 20);
            rowPanel.add(label);
            panelConectionEdges.add(rowPanel);
            positionY += 20; // Incrementa la position para la siguiente fila
        }
    
        panelConectionEdges.revalidate(); // RRevalidamos los componetes para que se vean
        panelConectionEdges.repaint(); // Repintamos del panel para que se vea
    }
    public void agregarMarcadorMapa(Coordinates coordenadas, String nombreEspia) {
        if (coordenadas != null) {
            MapMarkerDot marcador = new MapMarkerDot(coordenadas.getLatitude(), coordenadas.getLongitude());
            marcador.setBackColor(Color.RED);
            mapViewer.addMapMarker(marcador);  // Agrega el marcador al mapa
            
            marcador.setName(nombreEspia);  // Asigna un nombre o tooltip al marcador
        } else {
            JOptionPane.showMessageDialog(null, "Error: Coordenadas no válidas", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
   
    public JButton getBottonAddSpy() {
        return bottonAddEspia;
    }

    public List<JComboBox<String>> getListComboBoxProvince() {
        return listComboBoxProvince;
    }

    public List<JComboBox<Integer>> getListComboBoxWeight() {
        return listComboBoxWeight;
    }

    public JButton getBottonAddSpyConnectionGraph() {
        return bottonAddSpyConnectionGraph;
    }

    public JButton getBottonKruskal() {
        return bottonKruskal;
    }

    public Map<String, Coordinates> getSpyNameLocations() {
        return espias;
    }

    public JButton getBottonDivideCountry() {
        return buttonBestLines;
    }

    public JComboBox<Integer> getComboBoxDivideCountry() {
        return comboBoxDivideSpies;
    }
    public JTextArea getTextAreaVertices() {
        return textAreaVertices;
    }
    public JMapViewer getMapViewer() {
        return this.mapViewer; 
    }
    public Coordinates getCoordenadasEspia(String nombreEspia) {
        return espias.get(nombreEspia);  // Donde 'espias' es un Map que contiene los nombres de los espías y sus coordenadas.
    }
	
}
