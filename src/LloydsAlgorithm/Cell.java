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

    public void removeEdge(Edge edge){
        edges.remove(edge);
    }

    public HashSet<Edge> getEdges() {
        return edges;
    }


    public void setSite(Node site) {
        this.site = site;
    }

    public Node getCentre(){
        Node centre;

        HashSet<Node> points = new HashSet<>();



        for(Edge edge: edges){
            Boolean startExists=false;
            Boolean endExists=false;
            for(Node point: points){
                if(point.equalss(edge.getStart())){
                    startExists=true;
                }
                if(point.equalss(edge.getEnd())){
                    endExists=true;
                }
            }
            if(!startExists){
                points.add(edge.getStart());
            }
            if(!endExists){
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
