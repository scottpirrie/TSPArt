package LloydsAlgorithmTest;

import TSP_Solver.Node;

import java.util.HashSet;

public class Cluster {

    private Node centre;
    private HashSet<Node> nodes = new HashSet<>();

    public Cluster(Node centre){
        this.centre=centre;
    }

    public void addNode(Node node){
        nodes.add(node);
    }

    public HashSet<Node> getNodes(){
        return nodes;
    }

    public void clearNodes(){
        nodes.clear();
    }

    public Node getCentre() {
        return centre;
    }

    public void setCentre(Node centre) {
        this.centre = centre;
    }
}
