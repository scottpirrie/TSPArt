package ReductionAlgorithm;

import TSP_Solver.Node;

import java.util.HashSet;

public class Cluster {

    private Node site;
    private HashSet<Node> nodes;

    public Cluster(Node centre){
        this.site=centre;
        this.nodes = new HashSet<>();
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

    public Node getSite() {
        return site;
    }
}
