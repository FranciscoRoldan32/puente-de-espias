package grafo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {
    private int numVertices;
    private List<Vertex> vertices;

    private Map<Vertex, List<Edge>> adjacencyList;

    public Graph() {
        this.numVertices = 0;
        this.vertices = new ArrayList<>();
        this.adjacencyList = new HashMap<>();
    }

    public Vertex addVertex(String label) {
        for (Vertex existingVertex : vertices) {
            if (existingVertex.getLabel().equals(label)) {
                throw new IllegalArgumentException("Vertex with label '" + label + "' already exists");
            }
        }
        Vertex vertex = new Vertex(label);
        vertices.add(vertex);
        adjacencyList.put(vertex, new ArrayList<>());
        numVertices++;
        return vertex;
    }

    public void addEdge(Vertex source, Vertex destination, double weight) {
        if(weight <= 0){
            throw new IllegalArgumentException("La gráfica no puede ser igual o inferior a 0");
        }

        for (Edge edge : adjacencyList.get(source)) {
            if (edge.getDest().equals(destination)) {
                
                throw new IllegalArgumentException("Este Arista ya fue agregado al gráfico.");
            }
        }

        if(source.equals(destination)){
            throw new IllegalArgumentException("El gráfico no aceptará que el Origen y el Destino sean iguales");
        }

        if (!vertices.contains(source) || !vertices.contains(destination)) {
            throw new IllegalArgumentException("El vertice de origen o destino no existe en el gráfico");
        }
        
        adjacencyList.get(source).add(new Edge(source,destination, weight));
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public void setVertices(List<Vertex> vertices) {
        this.vertices = vertices;
    }

    public Vertex getVertex (String nameProvince){
        for (Vertex list : vertices) {
            if(list.getLabel() == nameProvince){
                return list;
            }
        }
        return null;
    }

    public Map<Vertex, List<Edge>> getAdjacencyList() {
        return adjacencyList;
    }

    public int getNumVertices() {
        return numVertices;
    }

    public List<Edge> deletedHeavieEdge(List<Edge> listEdges, int remove) {
    if (listEdges.isEmpty()) {
        throw new RuntimeException("La lista de aristas está vacía");
    }

    if (remove <= 0) {
        throw new IllegalArgumentException("El número de aristas a eliminar debe ser mayor que cero");
    }

    // Nos fijames de no eliminar elementos de mas
    int numberOfRemovals = Math.min(remove, listEdges.size());


    // Aqui removemos los elemntos que estan al final AKA los mas pesados
    for (int i = 0; i < numberOfRemovals; i++) {
        listEdges.remove(listEdges.size() - 1);
    }

    return listEdges;
}

    public List<Edge> getAllEdges() {
        List<Edge> allEdges = new ArrayList<>();
        for (Map.Entry<Vertex, List<Edge>> entry : adjacencyList.entrySet()) {
            allEdges.addAll(entry.getValue());
        }
        return allEdges;
    }


    public List<String> getAllTheEdgesInStrings() {
        List<String> representation = new ArrayList<>();
        
        for (Map.Entry<Vertex, List<Edge>> entry : adjacencyList.entrySet()){
            List<Edge> edges = entry.getValue();
            for (Edge edge : edges) {
                representation.add(edge.getSrc().getLabel());
                representation.add(edge.getDest().getLabel());
            }
        }
        return representation;
    }

    public List<String> generateAdjacencyMap() {
        List<String> representation = new ArrayList<>();

        for (Map.Entry<Vertex, List<Edge>> entry : adjacencyList.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                StringBuilder line = new StringBuilder();
                line.append(entry.getKey().getLabel()).append(" --> ");
                List<Edge> edges = entry.getValue();
                for (Edge edge : edges) {
                    line.append(edge.getDest().getLabel()).append("(").append(edge.getWeight()).append(") ");
                }
                representation.add(line.toString());
            }
        }

        return representation;
    }
   


    //Esto imprime el grafo de una manera ordenada, solo muestra los vertice con edges, si uno quedo solo no se imprime
    public void print() {
        for (Map.Entry<Vertex, List<Edge>> entry : adjacencyList.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                System.out.print(entry.getKey().getLabel() + " --> ");
                List<Edge> edges = entry.getValue();
                for (Edge edge : edges) {
                    System.out.print(edge.getDest().getLabel() + "(" + edge.getWeight() + ") ");
                }
                System.out.println();
            }
        }
    }
}