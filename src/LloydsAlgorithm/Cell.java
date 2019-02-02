package LloydsAlgorithm;

import TSP_Solver.Edge;
import TSP_Solver.Node;

import java.util.HashSet;

public class Cell {

    Node site;
    HashSet<Edge> edges;

    public Cell(Node site){
        this.site = site;
        this.edges = new HashSet<>();
    }

    public Node getSite() {
        return site;
    }

    public void addEdge(Edge edge){
        edges.add(edge);
    }

    public HashSet<Edge> getEdges() {
        return edges;
    }
}
