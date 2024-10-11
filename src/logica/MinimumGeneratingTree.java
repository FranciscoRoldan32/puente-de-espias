package logica;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import grafo.Graph;
import grafo.Edge;
import grafo.Vertex;



public class MinimumGeneratingTree {
    
    //Esto es Aristas or Edges
    private ArrayList<Edge> sortedEdges;





    public MinimumGeneratingTree (){
    }



    public List<Edge> minimumSpanningTree(Graph graphOriginal) {
        sortedEdges = new ArrayList<>();
        List<Edge> mst = new ArrayList<>();


        //Agrega todo las aristas en una lista y colecta todo los Vertice
        for (List<Edge> aristas : graphOriginal.getAdjacencyList().values()) {
            sortedEdges.addAll(aristas);
        }
        Collections.sort(sortedEdges, Comparator.comparingInt(Edge::getWeight));
        int numEdges = 0;
        
       //Inicializar conjunto disjunto para detección de ciclo
       Set<Vertex> allVertices = graphOriginal.getAdjacencyList().keySet();


        FindUnion findUnion = new FindUnion(allVertices);


        //Iterar sobre Aristas ordenados
        for (Edge edge : sortedEdges) {
            Vertex src = edge.getSrc();;
            Vertex dest = edge.getDest();
            // int weight = edge.getWeight();

            // Comprobar si incluir esta arista forma un ciclo
            if (!findUnion.connected(src, dest)){

                //Se agrega el caso arista con su peso
                mst.add(edge);

                // System.out.println(src.getLabel() + " --> " + dest.getLabel() + " == " + edge.getWeight());
                numEdges++;

                //Unir los conjuntos de inicio y destino
                findUnion.union(src, dest);

            }
        }

        // Verificar si el Arbol tiene exactamente V-1 aristas
        // if (graphOriginal.getNumVertices() - 1 != numAristas) {
        //     return null;
        // }

        if (graphOriginal.getNumVertices() - 1 > numEdges) {
            return null;
        }

        //Retornamos el grafo todo armado con su arista corresponientes 
        return mst;

    }

}