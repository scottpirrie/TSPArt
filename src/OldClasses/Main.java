package OldClasses;

import TSP_Solver.Edge;
import TSP_Solver.Node;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Random;

public class Main {

    private static HashSet<Edge> edges;
    private static HashSet<Node> nodes;
    private static Node farthestStart = null;
    private static Node farthestEnd = null;

    public static void main(String[] args) {

        nodes=randomNodeMap();
        edges=plotRoute(nodes);
        HashSet<Edge> edges2;
        edges2=plotRoute2(nodes);
        displayTSP(edges);
        displayTSP(edges2);
    }

    private static HashSet<Node> createStarNodeMap(){
        HashSet<Node> nodes = new HashSet<>();

        Node node0 = new Node(50,50);
        Node node1 = new Node(20,80);
        Node node2 = new Node(80,80);
        Node node3 = new Node(10,30);
        Node node4 = new Node(90,30);
        Node node5 = new Node(50,10);
        Node node6 = new Node(50,90);
        Node node7 = new Node(55,85);

        nodes.add(node0);
        nodes.add(node1);
        nodes.add(node2);
        nodes.add(node3);
        nodes.add(node4);
        nodes.add(node5);
        nodes.add(node6);
        nodes.add(node7);

        return nodes;
    }

    private static HashSet<Node> randomNodeMap(){
        HashSet<Node> nodes = new HashSet<>();
        Random rnd = new Random();
        for(int x=0; x<25; x++){
            nodes.add(new Node(rnd.nextInt(256),rnd.nextInt(256)));
        }
        return nodes;
    }

    private static HashSet<Edge> plotRoute(HashSet<Node> nodes){
        HashSet<Node> unvisited = new HashSet<>(nodes);
        HashSet<Node> visited = new HashSet<>();

        double farthest = 0;
        for(Node n1: nodes){
            for(Node n2: nodes){
                if(getDistance(n1,n2)>farthest){
                    farthestStart=n1;
                    farthestEnd=n2;
                    farthest=getDistance(n1,n2);
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
                        minDist = getDistance(unvisitedNode, visitedNode);
                    }else if(getDistance(unvisitedNode, visitedNode) < minDist){
                        minDist = getDistance(unvisitedNode, visitedNode);
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
                //Was checking only distance to start node and not to end node
                double distDiff=getDistance(edge.getStart(),farthestNode)+getDistance(farthestNode,edge.getEnd())-edge.getWeight();
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

    private static HashSet<Edge> plotRoute2(HashSet<Node> nodes){
        HashSet<Node> unvisited = new HashSet<>(nodes);
        HashSet<Node> visited = new HashSet<>();

        double farthest = 0;
        for(Node n1: nodes){
            for(Node n2: nodes){
                if(getDistance(n1,n2)>farthest){
                    farthestStart=n1;
                    farthestEnd=n2;
                    farthest=getDistance(n1,n2);
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
                        minDist = getDistance(unvisitedNode, visitedNode);
                    }else if(getDistance(unvisitedNode, visitedNode) < minDist){
                        minDist = getDistance(unvisitedNode, visitedNode);
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
                //Was checking only distance to start node and not to end node
                double distDiff=getDistance(edge.getStart(),farthestNode)+getDistance(farthestNode,edge.getStart())-edge.getWeight();
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

    private static HashSet<Edge> solveTSPNearest(HashSet<Node> nodes){
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
            //this was set to double.max
            double nearestDist = Double.MAX_VALUE;
            for (Node unvisitedNode : unvisited) {
                double maxDist = 0;
                for (Node visitedNode : visited) {
                    if(nearestNode==null){
                        nearestNode=unvisitedNode;
                        maxDist = unvisitedNode.distanceTo(visitedNode);
                    }else if(unvisitedNode.distanceTo(visitedNode) > maxDist){
                        maxDist = unvisitedNode.distanceTo(visitedNode);
                    }
                }
                if (maxDist < nearestDist) {
                    nearestDist = maxDist;
                    nearestNode = unvisitedNode;
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

    private static double getDistance(Node n1, Node n2){
        double a = Math.abs(n1.getXpos()-n2.getXpos());
        double b = Math.abs(n1.getYpos()-n2.getYpos());
        return Math.sqrt((a*a)+(b*b));
    }

    private static void displayTSP(HashSet<Edge> edges){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                for(Node node : nodes){
                    g.fillOval((int)node.getXpos()-2,(int)node.getYpos()-2,4,4);
                }
                for(Edge edge : edges){
                    g2.drawLine((int)edge.getStart().getXpos(), (int)edge.getStart().getYpos(), (int)edge.getEnd().getXpos(), (int)edge.getEnd().getYpos());
                }
            }
        };
        frame.add(panel);
        panel.setPreferredSize(new Dimension(256,256));
        frame.pack();
        frame.setVisible(true);
    }
}