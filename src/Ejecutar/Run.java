package Ejecutar;
import interfaz.Interfaz;
public class Run {
	public static void main(String[] args) {
		// Para que la interfaz gráfica se ejecute en el hilo de eventos de Swing
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// Crear una instancia de la clase Interfaz y hacerla visible
				Interfaz ventana = new Interfaz();
				ventana.setVisible(true); // Mostrar la ventana
			}
		});
	}
}
