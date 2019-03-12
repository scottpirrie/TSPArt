package Final;

import TSP_Solver.Edge;
import TSP_Solver.Node;

import java.util.HashSet;

public class NearestInsertion {

    HashSet<Node> nodes;

    public NearestInsertion(HashSet<Node> nodes){
        this.nodes=nodes;
    }

    public HashSet<Edge> solveTSP(){
        Node nearestStart=null;
        Node nearestEnd=null;
        HashSet<Node> unvisited = new HashSet<>(nodes);
        HashSet<Node> visited = new HashSet<>();

        double nearest = Double.MAX_VALUE;
        for(Node n1: nodes){
            for(Node n2: nodes){
                if(n1.distanceTo(n2)<nearest){
                    nearestStart=n1;
                    nearestEnd=n2;
                    nearest=n1.distanceTo(n2);
                }
            }
        }

        visited.add(nearestStart);
        unvisited.remove(nearestStart);
        visited.add(nearestEnd);
        unvisited.remove(nearestEnd);

        HashSet<Edge> edges = new HashSet<>();

        edges.add(new Edge(nearestStart,nearestEnd));
        edges.add(new Edge(nearestEnd,nearestStart));

        while(!unvisited.isEmpty()) {
            Node nearestNode = null;
            for (Node unvisitedNode : unvisited) {
                double minDist = Double.MAX_VALUE;
                for (Node visitedNode : visited) {
                    if (unvisitedNode.distanceTo(visitedNode) < minDist || nearestNode==null){
                        nearestNode=unvisitedNode;
                        minDist = unvisitedNode.distanceTo(visitedNode);
                    }
                }
            }

            double smallestChange=Double.MAX_VALUE;
            Edge smallestEdge=null;
            for(Edge edge : edges){
                double distDiff=edge.getStart().distanceTo(nearestNode)+nearestNode.distanceTo(edge.getEnd())-edge.getWeight();
                if(distDiff<smallestChange){
                    smallestChange=distDiff;
                    smallestEdge=edge;
                }

            }
            edges.add(new Edge(smallestEdge.getStart(),nearestNode));
            edges.add(new Edge(smallestEdge.getEnd(),nearestNode));
            edges.remove(smallestEdge);

            visited.add(nearestNode);
            unvisited.remove(nearestNode);
        }

        return edges;
    }
}
