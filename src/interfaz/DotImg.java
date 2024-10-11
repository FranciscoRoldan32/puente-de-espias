package interfaz;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;

import javax.swing.*;
import java.awt.*;

public class DotImg extends MapMarkerDot {
    private Image image;

    public DotImg(String name, Coordinate coord, String imagePath) {
        super(name, coord);
        this.image = new ImageIcon(getClass().getResource(imagePath)).getImage(); // Carga la imagen desde recursos
    }

    @Override
    public void paint(Graphics g, Point position, int radius) {
        // Si la imagen se ha cargado correctamente, la dibuja en la coordenada especificada
        if (image != null) {
            int imgWidth = 30;  // Tamaño de la imagen
            int imgHeight = 30; // Tamaño de la imagen

            // Dibujar la imagen centrada en el marcador
            g.drawImage(image, position.x - imgWidth / 2, position.y - imgHeight / 2, imgWidth, imgHeight, null);
        } else {
            // Si no hay imagen, usa el método original para dibujar el punto
            super.paint(g, position, radius);
        }
    }
}
