package Image_To_Nodes;

import LloydsAlgorithm.Cell;
import LloydsAlgorithm.LloydsAlgoMain;
import ReductionAlgorithm.Cluster;
import TSP_Solver.Edge;
import TSP_Solver.Node;

import javax.imageio.ImageIO;
import javax.sound.midi.Soundbank;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class Main {

    static BufferedImage image = null;

    static int contrastFactor=2;

    static int gridSize = 5;

    static double maxNodes=5000;
    static int iterations=1;


    public static void main(String[] args) throws IOException {
        File inputImage = new File("linear_gradient.png");
        HashSet<Node> nodes;
        HashSet<Edge> edges;
        HashSet<Node> centres;

        image = ImageIO.read(inputImage);
//        displayImage(image);
        image = convertToGreyscale(image);
        image = boostContrast(image);

        displayImage(image);

        nodes = displayImageAsNodes(image);
//        nodes = threeClusters();
        centres= reduceNodes(nodes);

        drawNodes(image,centres);

        LloydsAlgoMain la = new LloydsAlgoMain(image.getWidth(),image.getHeight());
//        la.display(LloydsAlgoMain.createDiagram(centres));
        for(int x=0; x<2; x++) {
            centres = la.voronoiRedistribute(centres);
            System.out.println("Interation "+ (x+1) + " complete");
            System.out.println("Number of nodes in diagram: "+centres.size());
        }
        drawNodes(image,centres);



//        drawNodes(image,nodes);
//        drawNodes(image,centres);

//        edges=plotRoute(centres);
//        displayTSP(edges);

    }

    private static HashSet<Node> threeClusters(){
        HashSet<Node> nodes = new HashSet<>();

        Node node0 = new Node(50,80);
        Node node1 = new Node(80,60);
        Node node2 = new Node(60,30);
        Node node3 = new Node(30,30);
        Node node4 = new Node(20,60);

        Node node5 = new Node(450,80);
        Node node6 = new Node(480,60);
        Node node7 = new Node(460,30);
        Node node8 = new Node(430,30);
        Node node9 = new Node(420,60);

        Node node10 = new Node(250,280);
        Node node11 = new Node(280,260);
        Node node12 = new Node(260,230);
        Node node13 = new Node(230,230);
        Node node14 = new Node(220,260);

        nodes.add(node0);
        nodes.add(node1);
        nodes.add(node2);
        nodes.add(node3);
        nodes.add(node4);
        nodes.add(node5);
        nodes.add(node6);
        nodes.add(node7);
        nodes.add(node8);
        nodes.add(node9);
        nodes.add(node10);
        nodes.add(node11);
        nodes.add(node12);
        nodes.add(node13);
        nodes.add(node14);

        return nodes;
    }

    private static BufferedImage convertToGreyscale(BufferedImage image){
        BufferedImage tempImage = image;
        for(int x=0; x<image.getWidth(); x++){
            for(int y=0; y<image.getHeight(); y++){
                Color tempColor = new Color(image.getRGB(x,y));
                int avg = (tempColor.getRed()+tempColor.getGreen()+tempColor.getBlue())/3;
                tempColor = new Color(avg,avg,avg);
                tempImage.setRGB(x,y,tempColor.getRGB());
            }
        }
        return tempImage;
    }

    private static BufferedImage boostContrast(BufferedImage input){
        BufferedImage output=input;
        RescaleOp rescale = new RescaleOp(contrastFactor, 0, null);
        rescale.filter(input,output);
        return output;
    }

    private static void displayImage(BufferedImage image){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image,0,0,null);
            }
        };
        panel.setPreferredSize(new Dimension(image.getWidth(),image.getHeight()));
        frame.add(panel);
        frame.setVisible(true);
        frame.pack();
    }

    private static HashSet<Node> displayImageAsNodes(BufferedImage image){

        HashSet<Node> nodes = new HashSet<>();
        int[][] imageArray = new int[image.getWidth()][image.getHeight()];
        Random rnd = new Random();

        for(int x=0; x<image.getWidth(); x++){
            for(int y=0; y<image.getHeight(); y++){
                imageArray[x][y] = new Color(image.getRGB(x,y)).getBlue();
            }
        }
        for(int x=0; x<image.getWidth()-(image.getWidth()%gridSize); x=x+gridSize){
            for(int y=0; y<image.getHeight()-(image.getHeight()%gridSize); y=y+gridSize){
                int gridTotal = 0;
                for(int i=0; i<gridSize; i++){
                    for (int j=0; j<gridSize; j++){
                        gridTotal += 255-imageArray[x+i][y+j];
                    }
                }
                int gridAverage = gridTotal/(gridSize*gridSize);
                int gridNodeNum = (gridAverage*gridSize)/255;
                int nodeCount=0;
                while (nodeCount<gridNodeNum){
                    Node tempNode = new Node(rnd.nextInt(gridSize)+x,rnd.nextInt(gridSize)+y);
                    if(!nodes.contains(tempNode)){
                        nodes.add(tempNode);
                        nodeCount++;
                    }
                }
            }
        }


        System.out.println("Total number of nodes: "+nodes.size());
        return nodes;
    }

    private static void drawNodes(BufferedImage image, HashSet<Node> nodes){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for(Node node : nodes){
                    g.fillOval((int)node.getXpos()-1, (int)node.getYpos()-1,2, 2 );
                }
            }
        };
        frame.add(panel);
        panel.setPreferredSize(new Dimension(image.getWidth()-(image.getWidth()%gridSize),image.getHeight()-(image.getHeight()%gridSize)));
        frame.pack();
        frame.setVisible(true);
    }

    private static HashSet<Edge> plotRoute(HashSet<Node> nodes){
        Node farthestStart=null;
        Node farthestEnd=null;
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

            if(visited.size()%100==0){
                System.out.println("Current node count: "+visited.size()+"/"+maxNodes);
            }
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
                for(Edge edge : edges){
//                    int thickness;
//                    if(edge.getWeight()<10){
//                        thickness=2;
////                    }else if(edge.getWeight()<50){
////                        thickness=2;
//                    }else{
//                        thickness=1;
//                    }
//                    g2.setStroke(new BasicStroke(thickness));
                    g.drawLine((int)edge.getStart().getXpos(), (int)edge.getStart().getYpos(), (int)edge.getEnd().getXpos(), (int)edge.getEnd().getYpos());
                }
            }
        };
        frame.add(panel);
        panel.setPreferredSize(new Dimension(image.getWidth()-(image.getWidth()%gridSize),image.getHeight()-(image.getHeight()%gridSize)));
        frame.pack();
        frame.setVisible(true);
    }

    private static HashSet<Node> reduceNodes(HashSet<Node> input){
        double numberOfCentres=maxNodes;
        HashSet<Node> centres = new HashSet();
        HashSet<Cluster> clusters = new HashSet<>();
        ArrayList<Node> inputList = new ArrayList<>(input);
        Random rnd = new Random();
        if(maxNodes<input.size()) {
            System.out.println("Performing reduction algorithm");
            while (clusters.size() != numberOfCentres) {
                int num = rnd.nextInt(inputList.size());
                Node node = new Node(inputList.get(num).getXpos(), inputList.get(num).getYpos());
                clusters.add(new Cluster(node));
                inputList.remove(num);
            }
            //assign nodes to centre to form clusters
            for (Node node : input) {
                Node closestCentre = null;
                for (Cluster cluster : clusters) {
                    if (closestCentre == null) {
                        closestCentre = cluster.getCentre();
                    } else if (getDistance(node, cluster.getCentre()) < getDistance(node, closestCentre)) {
                        closestCentre = cluster.getCentre();
                    }
                }
                for (Cluster cluster : clusters) {
                    if (cluster.getCentre() == closestCentre) {
                        cluster.addNode(node);
                    }
                }
            }

            //move centre to CoM of cluster
            for (Cluster cluster : clusters) {
                if (cluster.getNodes().size() != 0){
                    double totalX = 0;
                    double totalY = 0;
                    for (Node node : cluster.getNodes()) {
                        totalX += node.getXpos();
                        totalY += node.getYpos();
                    }
                    double averageX = totalX / cluster.getNodes().size();
                    double averageY = totalY / cluster.getNodes().size();
                    cluster.getCentre().setXpos(averageX);
                    cluster.getCentre().setYpos(averageY);
                    cluster.clearNodes();
                }
            }


            for (Cluster cluster : clusters) {
                centres.add(cluster.getCentre());
            }
            System.out.println("Total number of nodes after reduction algorithm: " + centres.size());
            return centres;
        }else{
            return input;
        }
    }

}