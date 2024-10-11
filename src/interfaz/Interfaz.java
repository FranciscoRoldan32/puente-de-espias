package interfaz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import grafo.Edge;
import grafo.Graph;
import grafo.Vertex;
import logica.MinimumGeneratingTree;
import lugar.Coordinates;

import javax.swing.*;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;


import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

public class Interfaz extends JFrame {
	
	private int positionPanelX, postionPanelY;
 	private Archivo arch;
    private int width = 900; //ancho
    private int height = 600; //largo

    private JMapViewer mapViewer;
    private JPanel panelMap;
    private JTextArea textAreaVertices;
    private String[] vertices;
    private Map<String,Coordinate> espias;
    private List<JCheckBox> vertexCheckBoxes = new ArrayList<>();
    private List<Edge> aristas = new ArrayList<>();
    private String rutaImg2="/img/espiaIcono.png",rutaImg1="/img/narrow_res.PNG";

    private Coordinate argentina;
    private JButton btnSumar_aristas, btnMandar_msm, btnProcesarVertices;
	private ArrayList<Coordinate> _lasCoordenadas;
	private MapPolygonImpl _poligono;
	
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
	
    public Interfaz() {
        setTitle("Puente de Espias");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, width, height); // Actualiza el tamaño de la ventana
        getContentPane().setLayout(null);
     
        // Cargar y establecer el ícono de la ventana
        ImageIcon icon = new ImageIcon(getClass().getResource(rutaImg2));
        setIconImage(icon.getImage());

        // Etiqueta para cantidad de vértices
        JLabel lblCantidad = new JLabel("Cantidad de Espias en el Area:");
        lblCantidad.setBounds(10, 10, 179, 20);
        getContentPane().add(lblCantidad);

        // Área de texto para ingresar los nombres de los vértices
        textAreaVertices = new JTextArea();
        textAreaVertices.setLineWrap(true); // Para que el texto se ajuste a varias líneas
        textAreaVertices.setBounds(10, 50, 400, 100);
        JScrollPane scrollPane = new JScrollPane(textAreaVertices);
        scrollPane.setBounds(10, 50, 400, 100); // Añadimos un scroll en caso de texto largo
        getContentPane().add(scrollPane);
        
        JLabel lblCantidadVertices = new JLabel("0");
        lblCantidadVertices.setBounds(215, 10, 34, 20);
        getContentPane().add(lblCantidadVertices);
        
        JLabel lblAclaracion = new JLabel("Escribirlos separados por coma (tambien vale para el archivo.txt)");
        scrollPane.setColumnHeaderView(lblAclaracion);
        
        JButton btnAgregarArista = new JButton("Agregar Arista");
        btnAgregarArista.setBounds(10, 230, 150, 20);
        getContentPane().add(btnAgregarArista);
        
        btnAgregarArista.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarAristaConPeso();
            }
        });
        
        JPanel panel = new JPanel();
    	panel.setBounds(0, 433, 138, 128);
    	getContentPane().add(panel);
    	btnSumar_aristas = new JButton("Sumar aristas");
    	btnSumar_aristas.addActionListener(new ActionListener() {
    	    public void actionPerformed(ActionEvent arg0) {
    	        if (_lasCoordenadas == null || _lasCoordenadas.size() < 2) {
    	            JOptionPane.showMessageDialog(null, "Debes asignar al menos dos espías antes de sumar aristas.");
    	            return;
    	        }
    	        btnSumar_aristas.setEnabled(false);
    	        _poligono = new MapPolygonImpl(_lasCoordenadas);
    	        mapViewer.addMapPolygon(_poligono);
    	    }
    	});

        btnMandar_msm = new JButton("Mandar msm");
        

        // Botón para procesar los nombres de los vértices
      
        btnProcesarVertices = new JButton("Procesar Espias");
        btnProcesarVertices.setBounds(10, 161, 150, 20);
        getContentPane().add(btnProcesarVertices);
        btnProcesarVertices.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	procesarVertices();
                lblCantidadVertices.setText(String.valueOf(vertices.length));
                if(!ceroEspias() && cantidadJustaEspias() && !espiasRepetidos()) {
            	JOptionPane.showMessageDialog(null, "Haga click sobre el territorio para asignar la posición de los espías");
                // Regenerar el panel del mapa si se ha reiniciado
                generatedMapPanel();
                if (panel.getComponentCount() == 0) {
                    panel.add(btnMandar_msm);
                    panel.add(btnSumar_aristas);
                    detectarCoordenadas();
                    panel.revalidate();
                    panel.repaint();
                }
            }
            }
        });
        
        JButton btnIngresarArchivo = new JButton("Subir archivo.txt");
        btnIngresarArchivo.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		JOptionPane.showMessageDialog(null,"Los nombres deben ir separados por comas.");
        		JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Selecciona un archivo .txt");
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    JOptionPane.showMessageDialog(null, "Has seleccionado: " + selectedFile.getName());
                    // Mandar el archivo a la clase que lo procesa y obtener el arreglo de nombres
                    arch= new Archivo();
                    vertices = arch.leerArchivo(selectedFile.toString());   
                    // Mostrar los nombres en una ventana de diálogo
                    if (vertices != null) {
                        JOptionPane.showMessageDialog(null, "Nombres de los vértices: " + String.join(", ", vertices));
                        lblCantidadVertices.setText(String.valueOf(vertices.length));
                        String nombres = String.join(", ", vertices);
                        textAreaVertices.setText(nombres);
                        
                        JOptionPane.showMessageDialog(null, "Nombres de los vértices: " + nombres);
                    } else {
                        JOptionPane.showMessageDialog(null, "El archivo está vacío o hubo un error al procesarlo.");
                    }
                }
            }
        });
        btnIngresarArchivo.setBounds(259, 161, 150, 20);
        getContentPane().add(btnIngresarArchivo);
        
        JButton btnRestart = new JButton("");
        btnRestart.setBackground(new Color(255, 255, 255));
        ImageIcon imageIcon = new ImageIcon(getClass().getResource(rutaImg1));
        Image image = imageIcon.getImage();
        Image scaledImage = image.getScaledInstance(42, 31, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        btnRestart.setIcon(scaledIcon);
        btnRestart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vertices = null;
                textAreaVertices.setText("");
                lblCantidadVertices.setText("0");
                btnSumar_aristas.setEnabled(true);
                panel.setEnabled(false);
                // Si el panel del mapa existe, eliminarlo
                if (panelMap != null && panelMap.isVisible()) {
                    mapViewer.removeAllMapMarkers();  // Limpiar los marcadores previos
                    mapViewer.removeAllMapPolygons(); // Limpiar los polígonos previos
                    _lasCoordenadas.clear();          // Limpiar las coordenadas previas

                    // Limpiar los listeners previos
                    for (MouseListener ml : mapViewer.getMouseListeners()) {
                        mapViewer.removeMouseListener(ml);
                    }

                    getContentPane().remove(panelMap);
                    panelMap = null; // Establecer a null para poder regenerarlo si es necesario
                    mapViewer = null; // Asegúrate de reiniciar el mapa también
                    revalidate();
                    repaint();
                }
            }
        });
        btnRestart.setBounds(10, 192, 42, 31);
        getContentPane().add(btnRestart);
        
        JPanel checkboxPanel = new JPanel();
        checkboxPanel.setLayout(new BoxLayout(checkboxPanel, BoxLayout.Y_AXIS));
        checkboxPanel.setBounds(10, 300, 400, 150); // Posición y tamaño del panel
        getContentPane().add(checkboxPanel);

        // Botón para crear la conexión
        JButton btnCrearConexion = new JButton("Crear Conexión");
        btnCrearConexion.setBounds(10, 470, 150, 20);
        getContentPane().add(btnCrearConexion);

        btnCrearConexion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                crearConexionEntreEspias();
            }
        });
        
        // El botón "Procesar Espías" también genera los checkboxes
        btnProcesarVertices.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                procesarVertices();
                generarCheckBoxesVertices(checkboxPanel);
                lblCantidadVertices.setText(String.valueOf(vertices.length));
            }
        });
       
    }
   

    private void procesarVertices() {
        String nombres = textAreaVertices.getText();
        // Manejo de entradas con espacios y comas consecutivas
        vertices = nombres.split(",");
        ArrayList<String> verticesLimpios = new ArrayList<>();

        for (String vertice : vertices) {
            String nombre = vertice.trim();
            if (!nombre.isEmpty()) {
                verticesLimpios.add(nombre);
            }
        }
        
        if (verticesLimpios.isEmpty()) {
            mostrar_msmEspecial("Error", "No se ha ingresado ningún espía.");
            return; // Detener el flujo si no se ingresaron nombres
        }if (verticesLimpios.size() < 2) {
            mostrar_msmEspecial("Advertencia", "Debe haber al menos dos espías para procesar.");
            return; // Detener el flujo si no hay suficientes espías
        }if (espiasRepetidos()) {
            mostrar_msmEspecial("Error", "Hay nombres de espías repetidos. Por favor, verifique los nombres.");
            return;
        }

        vertices = verticesLimpios.toArray(new String[0]); // Convertir de nuevo a array
        StringBuilder result = new StringBuilder("Espías ingresados:\n");
        for (String vertice : vertices) {
            result.append(vertice).append("\n");
        }
        JOptionPane.showMessageDialog(null, result.toString());

        // Actualizar el mapa con los nuevos vértices
        generatedMapPanel();
        detectarCoordenadas();
    }
    private void generarCheckBoxesVertices(JPanel checkboxPanel) {
        checkboxPanel.removeAll(); // Limpiar panel antes de agregar nuevos Checkboxes
        vertexCheckBoxes.clear();  // Limpiar la lista de checkboxes
        for (String vertice : vertices) {
            JCheckBox checkBox = new JCheckBox(vertice);
            checkboxPanel.add(checkBox);
            vertexCheckBoxes.add(checkBox); // Guardar el checkbox en la lista
        }
        checkboxPanel.revalidate();
        checkboxPanel.repaint();
    }

    private void crearConexionEntreEspias() {
        // Validar que se han seleccionado dos vértices
        List<String> selectedVertices = new ArrayList<>();
        for (JCheckBox checkBox : vertexCheckBoxes) {
            if (checkBox.isSelected()) {
                selectedVertices.add(checkBox.getText());
            }
        }

        if (selectedVertices.size() != 2) {
            JOptionPane.showMessageDialog(null, "Debes seleccionar exactamente dos espías.");
            return;
        }

        // Obtener los vértices seleccionados
        String espiaOrigen = selectedVertices.get(0);
        String espiaDestino = selectedVertices.get(1);

        // Pedir el peso de la arista
        String pesoStr = JOptionPane.showInputDialog(null, "Ingresa el peso de la arista:");
        int peso;
        try {
            peso = Integer.parseInt(pesoStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Debes ingresar un número válido para el peso.");
            return;
        }

        // Crear la arista y agregarla a la lista
        Edge arista = new Edge(new Vertex(espiaOrigen), new Vertex(espiaDestino), peso);
        aristas.add(arista);

        // Dibujar la arista en el mapa
        dibujarAristaEnMapa(new Vertex(espiaOrigen), new Vertex(espiaDestino));
    }

    private void generatedMapPanel() {
        if (panelMap == null) { // Solo generamos el panel si aún no existe
            panelMap = new JPanel();
            positionPanelX = 450; // Mueve el panel del mapa más a la derecha
            postionPanelY = 10;
            panelMap.setBounds(positionPanelX, postionPanelY, width/2, height-50);
            panelMap.setBackground(Color.DARK_GRAY);

            generatedMap();
            panelMap.add(mapViewer);

            getContentPane().add(panelMap);
            revalidate();
            repaint();
        }
    }
    
    private void generatedMap() {
        mapViewer = new JMapViewer();
        mapViewer.setBorder(null);
        mapViewer.setZoomControlsVisible(true); // Muestra los controles de zoom
        mapViewer.setPreferredSize(new Dimension(width / 2, height - 50));

        mapViewer.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Coordenadas de Argentina
        argentina = new Coordinate(-40.2, -63.616);
        mapViewer.setDisplayPosition(argentina, 5);
    }
    
 // Método para detectar coordenadas y agregar marcadores
    private void detectarCoordenadas() {
        _lasCoordenadas = new ArrayList<Coordinate>();
        final int[] vertexIndex = {0};

        mapViewer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    Coordinate markeradd = (Coordinate) mapViewer.getPosition(e.getPoint());
                    _lasCoordenadas.add(markeradd);

                    if (vertexIndex[0] < vertices.length) {
                        // Crear un marcador de imagen
                        DotImg img = new DotImg(vertices[vertexIndex[0]], markeradd, rutaImg2);
                        mapViewer.addMapMarker(img);  // Agregar el marcador personalizado
                        mapViewer.repaint();          // Forzar la actualización del mapa
                        
                        //MapMarkerDot marker = new MapMarkerDot(vertices[vertexIndex[0]], markeradd);
                        //marker.setBackColor(Color.BLUE);
                        //mapViewer.addMapMarker(marker);
                        vertexIndex[0]++;
                    } else {
                        JOptionPane.showMessageDialog(null, "Ya has asignado todos los espías.");
                    }
                }
            }
        });
    }

	private void mostrar_msmEspecial(String msm,String tit) {
		JOptionPane optionPane = new JOptionPane(
				    tit,
				    JOptionPane.INFORMATION_MESSAGE,
				    JOptionPane.DEFAULT_OPTION,
				    null,
				    new Object[]{},  // Arreglo vacío para no mostrar botones
				    null
				);

				// Crear el cuadro de diálogo sin botones
				JDialog dialog = optionPane.createDialog(msm);
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

				dialog.setVisible(true);
	}
	
	private void agregarAristaConPeso() {
	    // Verificar que se hayan asignado las coordenadas a los espías
	    if (_lasCoordenadas == null || _lasCoordenadas.size() < 2) {
	        JOptionPane.showMessageDialog(null, "Debes asignar las coordenadas a al menos dos espías.");
	        return;
	    }

	    // Mostrar un cuadro de diálogo para seleccionar los espías
	    Vertex espiaOrigen = (Vertex) JOptionPane.showInputDialog(null, "Selecciona el espía de origen:",
	            "Espía Origen", JOptionPane.PLAIN_MESSAGE, null, vertices, vertices[0]);

	    Vertex espiaDestino = (Vertex) JOptionPane.showInputDialog(null, "Selecciona el espía de destino:",
	            "Espía Destino", JOptionPane.PLAIN_MESSAGE, null, vertices, vertices[0]);

	    if (espiaOrigen == null || espiaDestino == null || espiaOrigen.equals(espiaDestino)) {
	        JOptionPane.showMessageDialog(null, "Debes seleccionar dos espías diferentes.");
	        return;
	    }

	    // Pedir el peso de la arista
	    String pesoStr = JOptionPane.showInputDialog(null, "Ingresa el peso de la arista:");
	    int peso;
	    try {
	        peso = Integer.parseInt(pesoStr);
	    } catch (NumberFormatException ex) {
	        JOptionPane.showMessageDialog(null, "Debes ingresar un número válido para el peso.");
	        return;
	    }

	    // Crear la arista y agregarla a la lista
	    Edge arista = new Edge(espiaOrigen, espiaDestino, peso);
	    aristas.add(arista);

	    // Dibujar la arista en el mapa
	    dibujarAristaEnMapa(espiaOrigen, espiaDestino);
	}
	private void dibujarAristaEnMapa(Vertex espiaOrigen, Vertex espiaDestino) {
	    // Obtener las coordenadas de los espías
	    Coordinate coordOrigen = obtenerCoordenadaEspia(espiaOrigen);
	    Coordinate coordDestino = obtenerCoordenadaEspia(espiaDestino);

	    if (coordOrigen != null && coordDestino != null) {
	        // Crear una línea entre las coordenadas
	        List<Coordinate> ruta = new ArrayList<>();
	        ruta.add(coordOrigen);
	        ruta.add(coordDestino);
	        MapPolygonImpl linea = new MapPolygonImpl(ruta);
	        mapViewer.addMapPolygon(linea);
	        mapViewer.repaint();
	    }
	}

	private Coordinate obtenerCoordenadaEspia(Vertex espia) {
	    int index = -1;
	    for (int i = 0; i < vertices.length; i++) {
	        if (vertices[i].equals(espia)) {
	            index = i;
	            break;
	        }
	    }
	    if (index != -1 && index < _lasCoordenadas.size()) {
	        return _lasCoordenadas.get(index);
	    }
	    return null;
	}
	
	
}
