package grafo;

import java.util.Objects;

public class Edge {
    private Vertex src;
    private Vertex dest;
    private Integer weight;

    public Edge(Vertex source,Vertex destination, int weight) {
        this.src = source;
        this.dest = destination;
        this.weight = weight;
    }

    public Vertex getSrc() {
        return src;
    }

    public Vertex getDest() {
        return dest;
    }

    public int getWeight() {
        return weight;
    }

    @Override
	public int hashCode() {
		return Objects.hash(src, dest, weight);
	}

    @Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Edge other = (Edge) obj;
		return Objects.equals(dest, other.dest) && Objects.equals(src, other.src) && weight == other.weight;
	}
}

