package Image_To_Nodes;

import LloydsAlgorithmTest.Cluster;
import TSP_Solver.Edge;
import TSP_Solver.Node;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class Main {

    static BufferedImage image = null;

    static int contrastFactor=2;

    static int gridSize = 5;

    static double stipplingReductionFactor=1.5;
    static int iterations=2;


    public static void main(String[] args) throws IOException {
        File inputImage = new File("mona-closeup.jpg");
        HashSet<Node> nodes;
        HashSet<Edge> edges;
        HashSet<Node> centres;

        image = ImageIO.read(inputImage);
        image = convertToGreyscale(image);
        image = boostContrast(image);

        displayImage(image);

        nodes = displayImageAsNodes(image);
        centres=findCentres(nodes);

        drawNodes(image,nodes);
        drawNodes(image,centres);

//        edges=plotRoute(centres);
//        displayTSP(edges);

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
        frame.add(panel);
        frame.setVisible(true);
        frame.setSize(new Dimension(image.getWidth()+16,image.getHeight()+40));
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
                int gridNodeNum = ((gridAverage*(gridSize*gridSize))/(255*gridSize));
                for(int i=0; i<gridNodeNum; i++){
                    nodes.add(new Node(rnd.nextInt(gridSize)+x,rnd.nextInt(gridSize)+y));
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
        panel.setBorder(BorderFactory.createLineBorder(Color.black));
        frame.setSize(new Dimension(image.getWidth()+16,image.getHeight()+40));
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

            if(visited.size()%100==0){
                System.out.println("Current node count: "+visited.size());
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
                    int thickness;
                    if(edge.getWeight()<10){
                        thickness=2;
//                    }else if(edge.getWeight()<50){
//                        thickness=2;
                    }else{
                        thickness=1;
                    }
                    g2.setStroke(new BasicStroke(thickness));
                    g2.drawLine((int)edge.getStart().getXpos(), (int)edge.getStart().getYpos(), (int)edge.getEnd().getXpos(), (int)edge.getEnd().getYpos());
                }
            }
        };
        frame.add(panel);
        panel.setSize(new Dimension(image.getWidth(),image.getHeight()));
        panel.setBorder(BorderFactory.createLineBorder(Color.black));
        frame.setSize(new Dimension(image.getWidth()+50,image.getHeight()+50));
        frame.setVisible(true);
    }

    private static HashSet<Node> findCentres(HashSet<Node> input){
        double numberOfCentres=input.size()/stipplingReductionFactor;
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
            System.out.println("Iterating "+x+"/"+iterations+" complete");
        }

        for(Cluster cluster: clusters){
            centres.add(cluster.getCentre());
        }
        System.out.println("Total number of nodes after Lloyd's algorithm: "+centres.size());
        return centres;
    }
}