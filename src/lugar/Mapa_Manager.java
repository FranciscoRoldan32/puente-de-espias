package lugar;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JOptionPane;

import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.interfaces.ICoordinate;

import controller.Controller;
import grafo.Graph;
import interfaz.Interfaz;

public class Mapa_Manager{
private Interfaz interfaz;
private List<String> espiasParaAgregar;
private Controller control;
private Map<String, Coordinates> spyNameLocations;
private MouseListener agregarUbicacionesListener;

public Mapa_Manager(Interfaz interfaz, Graph graf, Controller control) {
    this.interfaz = interfaz;
    this.control=control;
    this.espiasParaAgregar = new ArrayList<>();
    this.spyNameLocations = new HashMap<>();
}

public void activarModoAgregarUbicaciones(List<String> espias) {
    espiasParaAgregar = new ArrayList<>(espias);
    agregarUbicacionesListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            String nombreEspia = obtenerSiguienteEspiaSinUbicacion(espiasParaAgregar);

            if (nombreEspia != null) {
                Coordinates coords = obtenerCoordenadasDesdeEvento(e);
                asignarUbicacionEspia(nombreEspia, coords);
              
                espiasParaAgregar.remove(nombreEspia);

                
                if (espiasParaAgregar.isEmpty()) {
                    desactivarModoAgregarUbicaciones();
                }
            }
        }
    };
    interfaz.getMapViewer().addMouseListener(agregarUbicacionesListener);
}

public void desactivarModoAgregarUbicaciones() {
    if (agregarUbicacionesListener != null) {
        interfaz.getMapViewer().removeMouseListener(agregarUbicacionesListener);
        agregarUbicacionesListener = null; 
    }
}
private Coordinates obtenerCoordenadasDesdeEvento(MouseEvent e) {
    int x = e.getX();
    int y = e.getY();
    JMapViewer mapViewer = interfaz.getMapViewer();
    ICoordinate geoCoords = mapViewer.getPosition(x, y);
    return new Coordinates(geoCoords.getLat(), geoCoords.getLon());
}



private String obtenerSiguienteEspiaSinUbicacion(List<String> espias) {
    for (String espia : espias) {
        if (!spyNameLocations.containsKey(espia)) {
            return espia;
        }
    }
    return null;
}
// Asignar ubicación a un espía
public void asignarUbicacionEspia(String nombreEspia, Coordinates coordenadas) {
    if (nombreEspia != null && !nombreEspia.isEmpty() && coordenadas != null) {
        spyNameLocations.put(nombreEspia, coordenadas);
    } else {
        JOptionPane.showMessageDialog(null, "Error: El nombre del espía o las coordenadas son inválidos", "Error", JOptionPane.ERROR_MESSAGE);
    }
}
//private Coordinates obtenerCoordenadasDesdeClick(Point point) {
//    // Suponiendo que la interfaz tiene un componente JMapViewer o similar
//    JMapViewer mapViewer = interfaz.getMapViewer();
//
//    // Convertir el punto de clic (x, y) en coordenadas de latitud y longitud
//    ICoordinate coordenadas = mapViewer.getPosition(point);
//
//    // Crear una instancia de Coordinate con las latitud y longitud obtenidas
//    Coordinates resultado = new Coordinates( coordenadas.getLat(), coordenadas.getLon());
//
//    return resultado;
//}

public  Map<String, Coordinates> obtenerUbicaciones(){
		
	return spyNameLocations;
}
public boolean verificarSiUbicacionesCompletas(Map<String, Coordinates> spyNameLocations, List<String> aggEspia) {
    for (String espia : aggEspia) {
        if (!spyNameLocations.containsKey(espia)) {
            return false;  // Si un espía no tiene ubicación, retorna falso
        }
    }
    return true;  // Todos los espías tienen ubicación
}
}

