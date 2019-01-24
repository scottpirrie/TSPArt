package TSP_Solver;

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

        //nodes=createStarNodeMap();
        nodes=randomNodeMap();
        edges=plotRoute(nodes);
        displayTSP(edges);
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

        nodes.add(node0);
        nodes.add(node1);
        nodes.add(node2);
        nodes.add(node3);
        nodes.add(node4);
        nodes.add(node5);
        nodes.add(node6);

        return nodes;
    }

    private static HashSet<Node> randomNodeMap(){
        HashSet<Node> nodes = new HashSet<>();
        Random rnd = new Random();
        for(int x=0; x<1000; x++){
            nodes.add(new Node(rnd.nextInt(800),rnd.nextInt(800)));
        }
        return nodes;
    }

    private static HashSet<Edge> plotRoute(HashSet<Node> nodes){
        HashSet<Node> unvisited = new HashSet<>(nodes);
        HashSet<Node> visited = new HashSet<>();

        double farthest = 0;
        for(Node n1: nodes){
            for(Node n2: nodes){
//                System.out.println(n1.getXpos()+","+n1.getYpos());
//                System.out.println(n2.getXpos()+","+n2.getYpos());
//                System.out.println(getDistance(n1,n2));
//                System.out.println();
                if(getDistance(n1,n2)>farthest){
                    farthestStart=n1;
                    farthestEnd=n2;
                    farthest=getDistance(n1,n2);
                }
            }
        }
//        System.out.println(farthestStart.getXpos()+","+farthestStart.getYpos());
//        System.out.println(farthestEnd.getXpos()+","+farthestEnd.getYpos());
//        System.out.println(farthest);

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
                for(Node node : nodes){
                    g.fillOval((int)node.getXpos()-3,(int)node.getYpos()-3,6,6);
                }
                for(Edge edge : edges){
                    g.drawLine((int)edge.getStart().getXpos(), (int)edge.getStart().getYpos(), (int)edge.getEnd().getXpos(), (int)edge.getEnd().getYpos());
                }
            }
        };
        frame.add(panel);
        panel.setSize(new Dimension(800,800));
        panel.setBorder(BorderFactory.createLineBorder(Color.black));
        frame.setSize(new Dimension(850,850));
        frame.setVisible(true);
    }
}