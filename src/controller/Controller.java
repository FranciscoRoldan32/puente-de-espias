package controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;

import org.openstreetmap.gui.jmapviewer.Coordinate;

import grafo.Edge;
import grafo.Graph;
import logica.MinimumGeneratingTree;
import lugar.Coordinates;
import lugar.Zona_Espionaje;

import interfaz.Interfaz;


public class Controller {
	private Interfaz interfaz;
	
	private Graph grafo;
	
	private MinimumGeneratingTree kruskal;
	
	private JButton bottonKruskal;
	
	private List<String> aggEspia;
	
	 private List<String> espias;
	 
	 
	 public Controller (Interfaz interfaz,  Graph grafo, MinimumGeneratingTree kruskal){
	        this.interfaz = interfaz;
	        this.grafo = grafo;
	        this.kruskal = kruskal;
	   
	    }


	
}
