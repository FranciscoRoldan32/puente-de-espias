package logica;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import grafo.Vertex;

public class FindUnion {


    private Map<Vertex, Vertex> parentMap = new HashMap<>();


    public FindUnion(Set<Vertex> arrayList) {

        for (Vertex vertice : arrayList) {
            parentMap.put(vertice, vertice);
        }

    }

    public Vertex find(Vertex vertex) {
        if (parentMap.get(vertex) != vertex) {
            parentMap.put(vertex, find(parentMap.get(vertex)));
        }
        return parentMap.get(vertex);
    }

    public void union(Vertex src, Vertex dest) {
        Vertex srcParent = find(src);
        Vertex destParent = find(dest);
        if (!srcParent.equals(destParent)) {
            parentMap.put(srcParent, destParent); // Ahora u está bajo el conjunto de v
        }

    }

    public boolean connected(Vertex vertex1, Vertex vertex2) {
        Vertex fatherSrc = find(vertex1);
        Vertex fatherDest = find(vertex2);

        // Verifica si los padres son iguales
        return fatherSrc.equals(fatherDest);
    }

}