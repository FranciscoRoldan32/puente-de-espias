package Ejecutar;
import javax.swing.SwingUtilities;

import interfaz.Interfaz;
public class Run {
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
