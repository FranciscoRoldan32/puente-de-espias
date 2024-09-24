package interfaz;

import java.util.Scanner;
import grafo.Graph;

import javax.swing.*;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

	    private Coordinate argentina;

	    public Interfaz() {
	        setTitle("Puente de Espias");
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        setBounds(100, 100, width, height); // Actualiza el tamaño de la ventana
	        getContentPane().setLayout(null);

	        // Etiqueta para cantidad de vértices
	        JLabel lblCantidad = new JLabel("Cantidad de Espias en el Area:");
	        lblCantidad.setBounds(10, 10, 179, 20);
	        getContentPane().add(lblCantidad);
	        
	        /*
	        // Botón para confirmar cantidad de vértices
	        JButton btnIngresarVertices = new JButton("Confirmar cantidad");
	        btnIngresarVertices.setBounds(259, 10, 150, 20);
	        getContentPane().add(btnIngresarVertices);
	        btnIngresarVertices.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                int cantidadVertices = Integer.parseInt(lblCantidadVertices.getText());
	                JOptionPane.showMessageDialog(null,
	                        "Se han introducido " + cantidadVertices + " Espias");
	            }
	        });
	        */

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

	        // Botón para procesar los nombres de los vértices
	        JButton btnProcesarVertices = new JButton("Procesar Espias");
	        btnProcesarVertices.setBounds(10, 161, 150, 20);
	        getContentPane().add(btnProcesarVertices);
	        
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
	        ImageIcon imageIcon = new ImageIcon(getClass().getResource("/img/narrow_res.PNG")); // Ruta de la imagen
	        Image image = imageIcon.getImage();
	        Image scaledImage = image.getScaledInstance(42, 31, Image.SCALE_SMOOTH);
	        ImageIcon scaledIcon = new ImageIcon(scaledImage);
	        btnRestart.setIcon(scaledIcon);
	        btnRestart.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					vertices=null;
					textAreaVertices.setText("");
					lblCantidadVertices.setText("");
					if (panelMap != null && panelMap.isVisible()) {
						getContentPane().remove(panelMap); // Elimina el panel del mapa
						panelMap = null; // Lo establece como null para poder regenerarlo si es necesario
						revalidate(); // Refresca el contenedor para aplicar los cambios
						repaint(); // Vuelve a pintar la ventana
					}
				}
	        });
	        btnRestart.setBounds(10, 192, 42, 31);
	        getContentPane().add(btnRestart);

	        btnProcesarVertices.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                procesarVertices();
	                lblCantidadVertices.setText(String.valueOf(vertices.length));
	            }
	        });
	    }

	    // Procesa los nombres de los vértices ingresados
	    private void procesarVertices() {
	        String nombres = textAreaVertices.getText();
	        vertices = nombres.split(","); 

	        StringBuilder result = new StringBuilder("Espias ingresados:\n");
	            for (String vertice : vertices) {
	                result.append(vertice.trim()).append("\n"); // Eliminar espacios en blanco
	            }
	            JOptionPane.showMessageDialog(null, result.toString());
	            
	            // Mostrar el mapa después de confirmar los vértices
	            generatedMapPanel();
	    }
	    
	    
	    private void generatedMapPanel() {
	        if (panelMap == null) { // Solo generamos el panel si aún no existe
	            panelMap = new JPanel();
	            positionPanelX = 450; // Mueve el panel del mapa más a la derecha
	            postionPanelY = 10;
	            panelMap.setBounds(positionPanelX, postionPanelY, width/2, height-50);
	            panelMap.setBackground(Color.GREEN);

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
}
