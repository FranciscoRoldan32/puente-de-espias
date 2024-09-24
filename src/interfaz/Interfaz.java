package interfaz;

import java.util.Scanner;
import grafo.Graph;


import javax.swing.*;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Interfaz extends JFrame {
	
	 private int positionPanelX;
	    private int postionPanelY;

	    private int width = 900; // Aumenta el tamaño del ancho de la ventana
	    private int height = 600; // Aumenta el tamaño de la ventana

	    private JMapViewer mapViewer;
	    private JPanel panelMap;
	    
	    private JTextField textCantidadVertices;
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
	        lblCantidad.setBounds(10, 10, 150, 20);
	        getContentPane().add(lblCantidad);

	        // Campo de texto para cantidad de vértices
	        textCantidadVertices = new JTextField();
	        textCantidadVertices.setBounds(170, 10, 50, 20);
	        getContentPane().add(textCantidadVertices);
	        textCantidadVertices.setColumns(10);

	        // Botón para confirmar cantidad de vértices
	        JButton btnIngresarVertices = new JButton("Confirmar cantidad");
	        btnIngresarVertices.setBounds(230, 10, 150, 20);
	        getContentPane().add(btnIngresarVertices);

	        // Área de texto para ingresar los nombres de los vértices
	        textAreaVertices = new JTextArea();
	        textAreaVertices.setLineWrap(true); // Para que el texto se ajuste a varias líneas
	        textAreaVertices.setBounds(10, 50, 400, 100);
	        JScrollPane scrollPane = new JScrollPane(textAreaVertices);
	        scrollPane.setBounds(10, 50, 400, 100); // Añadimos un scroll en caso de texto largo
	        getContentPane().add(scrollPane);

	        // Botón para procesar los nombres de los vértices
	        JButton btnProcesarVertices = new JButton("Procesar Espias");
	        btnProcesarVertices.setBounds(10, 160, 150, 20);
	        getContentPane().add(btnProcesarVertices);

	        // Acción del botón para ingresar la cantidad de vértices
	        btnIngresarVertices.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                int cantidadVertices = Integer.parseInt(textCantidadVertices.getText());
	                JOptionPane.showMessageDialog(null,
	                        "Introduce los nombres de los " + cantidadVertices + " Espias, separados por comas.");
	            }
	        });

	        // Acción del botón para procesar los nombres
	        btnProcesarVertices.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                procesarVertices();
	            }
	        });
	    }

	    // Procesa los nombres de los vértices ingresados
	    private void procesarVertices() {
	        String nombres = textAreaVertices.getText();
	        vertices = nombres.split(","); // Puedes cambiar el delimitador si prefieres otro

	        // Verificar si la cantidad de nombres coincide con la cantidad de vértices
	        int cantidadVertices = Integer.parseInt(textCantidadVertices.getText());
	        if (vertices.length != cantidadVertices) {
	            JOptionPane.showMessageDialog(null, "El número de nombres no coincide con la cantidad de Espias.");
	        } else {
	            // Mostrar los nombres procesados (puedes añadir más funcionalidad aquí)
	            StringBuilder result = new StringBuilder("Espias ingresados:\n");
	            for (String vertice : vertices) {
	                result.append(vertice.trim()).append("\n"); // Eliminar espacios en blanco
	            }
	            JOptionPane.showMessageDialog(null, result.toString());
	            
	            // Mostrar el mapa después de confirmar los vértices
	            generatedMapPanel();
	        }
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

	    public static void main(String[] args) {
	        SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	                try {
	                    Interfaz frame = new Interfaz();
	                    frame.setVisible(true);
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
	            }
	        });
	    }
}
