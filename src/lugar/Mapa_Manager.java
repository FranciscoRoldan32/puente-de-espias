package lugar;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.interfaces.ICoordinate;

import controller.Controller;
import grafo.Graph;
import interfaz.Interfaz;

public class Mapa_Manager{
private Interfaz interfaz;
private List<String> espiasParaAgregar;
private Controller control;

public Mapa_Manager(Interfaz interfaz, Graph grafo) {
    this.interfaz = interfaz;
    this.control = control;
}

public void activarModoAgregarUbicaciones(List<String> espias) {
    this.espiasParaAgregar = new ArrayList<>(espias);
    interfaz.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (!espiasParaAgregar.isEmpty()) {
                Coordinates coordenadas = obtenerCoordenadasDesdeClick(e.getPoint());
                String espiaActual = espiasParaAgregar.remove(0);
                control.asignarUbicacionEspia(espiaActual, coordenadas);
                interfaz.agregarMarcadorMapa(coordenadas, espiaActual);

                if (espiasParaAgregar.isEmpty()) {
                    interfaz.removeMouseListener(this);
                }
            }
        }
    });


}
private Coordinates obtenerCoordenadasDesdeClick(Point point) {
    // Suponiendo que la interfaz tiene un componente JMapViewer o similar
    JMapViewer mapViewer = interfaz.getMapViewer();

    // Convertir el punto de clic (x, y) en coordenadas de latitud y longitud
    ICoordinate coordenadas = mapViewer.getPosition(point);

    // Crear una instancia de Coordinate con las latitud y longitud obtenidas
    Coordinates resultado = new Coordinates(null, coordenadas.getLat(), coordenadas.getLon());

    return resultado;
}
}

