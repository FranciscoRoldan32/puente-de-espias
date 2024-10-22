package Ejecutar;
import controller.Controller;
import grafo.Graph;
import interfaz.Interfaz_Principal;
import logica.MinimumGeneratingTree;

public class Run {
	public static void main(String[] args) {

		// Crear una instancia de la clase Interfaz y hacerla visible
		Interfaz_Principal ventana = new Interfaz_Principal();
		Graph graph = new Graph();
		MinimumGeneratingTree Kruskal = new MinimumGeneratingTree();
		@SuppressWarnings("unused")
		Controller controller = new Controller(ventana,ventana.getDesigningRegions(), graph, Kruskal);

	}

}

