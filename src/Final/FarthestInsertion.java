package Final;

import TSP_Solver.Edge;
import TSP_Solver.Node;

import java.util.HashSet;

public class FarthestInsertion {

    HashSet<Node> nodes;

    public FarthestInsertion(HashSet<Node> nodes){
        this.nodes=nodes;
    }

    public HashSet<Edge> solveTSP(){
        Node farthestStart=null;
        Node farthestEnd=null;
        HashSet<Node> unvisited = new HashSet<>(nodes);
        HashSet<Node> visited = new HashSet<>();

        double farthest = 0;
        for(Node n1: nodes){
            for(Node n2: nodes){
                if(n1.distanceTo(n2)>farthest){
                    farthestStart=n1;
                    farthestEnd=n2;
                    farthest=n1.distanceTo(n2);
                }
            }
        }

        visited.add(farthestStart);
        unvisited.remove(farthestStart);
        visited.add(farthestEnd);
        unvisited.remove(farthestEnd);

        HashSet<Edge> edges = new HashSet<>();

        edges.add(new Edge(farthestStart,farthestEnd));
        edges.add(new Edge(farthestEnd,farthestStart));

        while(!unvisited.isEmpty()) {
            Node farthestNode = null;
            //this was set to double.max
            double farthestDist = 0;
            for (Node unvisitedNode : unvisited) {
                double minDist = Double.MAX_VALUE;
                for (Node visitedNode : visited) {
                    if(farthestNode==null){
                        farthestNode=unvisitedNode;
                        minDist = unvisitedNode.distanceTo(visitedNode);
                    }else if(unvisitedNode.distanceTo(visitedNode) < minDist){
                        minDist = unvisitedNode.distanceTo(visitedNode);
                    }
                }
                if (minDist > farthestDist) {
                    farthestDist = minDist;
                    farthestNode = unvisitedNode;
                }
            }

            double smallestChange=Double.MAX_VALUE;
            Edge smallestEdge=null;
            for(Edge edge : edges){
                double distDiff=edge.getStart().distanceTo(farthestNode)+farthestNode.distanceTo(edge.getEnd())-edge.getWeight();
                if(distDiff<smallestChange){
                    smallestChange=distDiff;
                    smallestEdge=edge;
                }

            }
            edges.add(new Edge(smallestEdge.getStart(),farthestNode));
            edges.add(new Edge(smallestEdge.getEnd(),farthestNode));
            edges.remove(smallestEdge);

            visited.add(farthestNode);
            unvisited.remove(farthestNode);
        }

        return edges;
    }
}
