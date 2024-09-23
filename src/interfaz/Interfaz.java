package interfaz;

import java.util.Scanner;
import grafo.Graph;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Interfaz extends JFrame {
	private JTextField textCantidadVertices;
	private JTextArea textAreaVertices;
	private String[] vertices;

	public Interfaz() {

		setTitle("Puente de Espias");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
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
		// ingresada
		int cantidadVertices = Integer.parseInt(textCantidadVertices.getText());
		if (vertices.length != cantidadVertices) {
			JOptionPane.showMessageDialog(null, "El número de nombres no coincide con la cantidad de Espias.");
		} else {
			// Mostrar los nombres procesados (puedes añadir más funcionalidad aquí)
			StringBuilder result = new StringBuilder("Vértices ingresados:\n");
			for (String vertice : vertices) {
				result.append(vertice.trim()).append("\n"); // Eliminar espacios en blanco
			}
			JOptionPane.showMessageDialog(null, result.toString());
		}
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
