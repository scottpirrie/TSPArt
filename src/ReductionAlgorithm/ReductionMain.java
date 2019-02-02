package ReductionAlgorithm;

import TSP_Solver.Node;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class ReductionMain {
    public static void main(String[] args) {
//        HashSet<Node> nodes = generatePoints();
        HashSet<Node> nodes = createStarNodeMap();
        HashSet<Node> centres = findCentres(nodes);
        drawNodes(nodes);
        drawNodes(centres);
    }

    private static HashSet<Node> generatePoints(){
        int numberOfNodes = 500;
        HashSet<Node> nodes = new HashSet<>();
        Random rnd = new Random();
        for(int x=0; x<numberOfNodes; x++){
            nodes.add(new Node(rnd.nextInt(500),rnd.nextInt(500)));
        }
        return nodes;
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

    private static HashSet<Node> findCentres(HashSet<Node> input){
        int numberOfCentres=2;
        int iterations=1;
        HashSet<Node> centres = new HashSet();
        HashSet<Cluster> clusters = new HashSet<>();
        ArrayList<Node> inputList = new ArrayList<>(input);
        Random rnd = new Random();
        while(clusters.size()!=numberOfCentres){
            int num = rnd.nextInt(inputList.size());
            Node node = new Node(inputList.get(num).getXpos(),inputList.get(num).getYpos());
            clusters.add(new Cluster(node));
        }

        for(int x=0; x<iterations; x++){
            //assign nodes to centre to form clusters
            for(Node node: input){
                Node closestCentre=null;
                for(Cluster cluster: clusters){
                    if (closestCentre==null){
                        closestCentre=cluster.getCentre();
                    }else if(getDistance(node,cluster.getCentre())<getDistance(node,closestCentre)){
                        closestCentre=cluster.getCentre();
                    }
                }
                for(Cluster cluster: clusters){
                    if(cluster.getCentre()==closestCentre){
                        cluster.addNode(node);
                    }
                }
            }

            //move centre to CoM of cluster
            for(Cluster cluster: clusters){
                double totalX=0;
                double totalY=0;
                for(Node node: cluster.getNodes()){
                    totalX+=node.getXpos();
                    totalY+=node.getYpos();
                }
                double averageX=totalX/cluster.getNodes().size();
                double averageY=totalY/cluster.getNodes().size();
                cluster.getCentre().setXpos(averageX);
                cluster.getCentre().setYpos(averageY);
                cluster.clearNodes();
            }
        }

        for(Cluster cluster: clusters){
            centres.add(cluster.getCentre());
        }
        return centres;
    }

    private static double getDistance(Node n1, Node n2){
        double a = Math.abs(n1.getXpos()-n2.getXpos());
        double b = Math.abs(n1.getYpos()-n2.getYpos());
        return Math.sqrt((a*a)+(b*b));
    }
    private static void drawNodes(HashSet<Node> nodes){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for(Node node : nodes){
                    g.fillOval((int)node.getXpos()-3, (int)node.getYpos()-3,6, 6 );
                }
            }
        };
        frame.add(panel);
        panel.setBorder(BorderFactory.createLineBorder(Color.black));
        frame.setSize(new Dimension(500+16,500+40));
        frame.setVisible(true);
    }
}
