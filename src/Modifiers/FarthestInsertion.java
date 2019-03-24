package Modifiers;

import GUI.GUI2;
import DataClasses.Edge;
import DataClasses.Node;

import java.util.HashSet;

public class FarthestInsertion {

    HashSet<Node> nodes;
    GUI2 gui2;
    public FarthestInsertion(HashSet<Node> nodes, GUI2 gui2){
        this.nodes=nodes;
        this.gui2=gui2;
    }

    public HashSet<Edge> solveTSP(){
        gui2.setTspMin(0);
        gui2.setTspMax(nodes.size());

        HashSet<Edge> edges = new HashSet<>();


        if(nodes.size()<2){
            gui2.setTspProgress(nodes.size());
            return edges;
        }

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

        edges.add(new Edge(farthestStart,farthestEnd));
        edges.add(new Edge(farthestEnd,farthestStart));

        while(!unvisited.isEmpty()) {
            Node farthestNode = null;
            double farthestDist = 0;
            for (Node unvisitedNode : unvisited) {
                double minDist = Double.MAX_VALUE;
                for (Node visitedNode : visited) {
                    if(unvisitedNode.distanceTo(visitedNode) < minDist){
                        minDist = unvisitedNode.distanceTo(visitedNode);
                    }
                }
                if (minDist > farthestDist || farthestNode == null) {
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

            gui2.setTspProgress(edges.size());
        }

        return edges;
    }
}
