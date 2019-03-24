package DataClasses;

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

    public void removeEdge(Edge edge){
        if(edges.contains(edge)) {
            edges.remove(edge);
        }
    }

    public HashSet<Edge> getEdges() {
        return edges;
    }

    public Node getCentre(){
        Node centre;

        HashSet<Node> points = new HashSet<>();

        for(Edge edge: edges){
            if(!(Double.isNaN(edge.getStart().getXpos()) || Double.isNaN(edge.getStart().getYpos()))){
                points.add(edge.getStart());
            }
            if(!(Double.isNaN(edge.getEnd().getXpos()) || Double.isNaN(edge.getEnd().getYpos()))){
                points.add(edge.getEnd());
            }
        }

        double totalX=0;
        double totalY=0;
        for(Node point: points){
            totalX+=point.getXpos();
            totalY+=point.getYpos();
        }
        double averageX=totalX/points.size();
        double averageY=totalY/points.size();
        centre = new Node(averageX,averageY);

        return centre;
    }
}
