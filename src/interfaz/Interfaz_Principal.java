package interfaz;

import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Interfaz_Principal {

    private int width;
    private int height;

    private JFrame frame;


    private Interfaz interfaz;


    private URL image = getClass().getResource("/img/espiaIcono.png");

    public Interfaz_Principal(){
        initialize();
    }

    private void initialize() {

        height=950;
        width=900;

        frame = new JFrame();

        frame.setTitle("Puente de Espias");
        

        ImageIcon icon = new ImageIcon(image);
        frame.setIconImage(icon.getImage());


        frame.setVisible(true);
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        interfaz = new Interfaz(width, height);
        interfaz.setVisible(true);
        frame.add(interfaz);
        frame.pack();


        interfaz.requestFocus();
    }

    public Interfaz getDesigningRegions() {
        return interfaz;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }


}
