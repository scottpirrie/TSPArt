package LloydsAlgorithm;

import TSP_Solver.Edge;
import TSP_Solver.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class LloydsAlgoMain {

    static int height=40;
    static int width=40;

    public static void main(String[] args) {
//        HashSet<Node> nodes = generateRandomNodes();
        HashSet<Node> nodes = generateNodes();
        HashSet<Cell> cells = createDiagram(nodes);

        display(cells);
    }

    private static HashSet<Node> generateRandomNodes() {
        HashSet<Node> nodes = new HashSet<>();
        Random rnd = new Random();
        for (int x = 0; x < 5; x++) {
            nodes.add(new Node(rnd.nextInt(height), rnd.nextInt(width)));
        }
        return nodes;
    }

    private static HashSet<Node> generateNodes(){
        HashSet<Node> nodes = new HashSet<>();
        nodes.add(new Node(39,7));
        nodes.add(new Node(6,39));
        nodes.add(new Node(30,30));
        nodes.add(new Node(0,35));
        return nodes;
    }

    private static HashSet<Cell> createDiagram(HashSet<Node> sites){
        HashSet<Cell> cells = new HashSet<>();

        Cell cell1 = new Cell(new Node(-width,-height));
        Cell cell2 = new Cell(new Node(2*width,-height));
        Cell cell3 = new Cell(new Node(2*width,2*height));
        Cell cell4 = new Cell(new Node(-width, 2*height));

        Node nodeMiddle = new Node(width/2,height/2);
        Node nodeTop = new Node(width/2,-10*height);
        Node nodeBottom = new Node(width/2,10*height);
        Node nodeLeft = new Node(-10*width,height/2);
        Node nodeRight = new Node(10*width,height/2);

        cell1.addEdge(new Edge(nodeMiddle,nodeTop));
        cell1.addEdge(new Edge(nodeTop,nodeLeft));
        cell1.addEdge(new Edge(nodeLeft,nodeMiddle));

        cell2.addEdge(new Edge(nodeMiddle,nodeTop));
        cell2.addEdge(new Edge(nodeTop,nodeRight));
        cell2.addEdge(new Edge(nodeRight,nodeMiddle));

        cell3.addEdge(new Edge(nodeMiddle,nodeBottom));
        cell3.addEdge(new Edge(nodeBottom,nodeRight));
        cell3.addEdge(new Edge(nodeRight,nodeMiddle));

        cell4.addEdge(new Edge(nodeMiddle,nodeBottom));
        cell4.addEdge(new Edge(nodeBottom,nodeLeft));
        cell4.addEdge(new Edge(nodeLeft,nodeMiddle));

        cells.add(cell1);
        cells.add(cell2);
        cells.add(cell3);
        cells.add(cell4);

        for (Node site: sites){
            System.out.println("Adding cell with site: "+site.toString());
            Cell tempCell = new Cell(site);
            for(Cell cell: cells){
                Node pbMid = new Node((site.getXpos()+cell.getSite().getXpos())/2,(site.getYpos()+cell.getSite().getYpos())/2);
                double grad = ((site.getYpos()-cell.getSite().getYpos())/(site.getXpos()-cell.getSite().getXpos()));
                double pbGrad;
                if(grad==0){
                    pbGrad=1/0;
                }else if(Double.isInfinite(grad)){
                    pbGrad=0;
                }else{
                    pbGrad=-1/grad;
                }
                double yIntercept = pbMid.getYpos()-(pbGrad*pbMid.getXpos());
                Node pbPoint1 = new Node((-10*width),(pbGrad*(-10*width))+yIntercept);
                Node pbPoint2 = new Node(10*width,(pbGrad*(10*width))+yIntercept);
                Line2D.Double pb = new Line2D.Double(pbPoint1.getXpos(),pbPoint1.getYpos(),pbPoint2.getXpos(),pbPoint2.getYpos());
                ArrayList<Node> intersectionPoints = new ArrayList<>();
                ArrayList<Edge> intersectedEdges = new ArrayList<>();

                for(Edge edge: cell.getEdges()){
                    Line2D.Double edgeLine = new Line2D.Double(edge.getStart().getXpos(),edge.getStart().getYpos(),edge.getEnd().getXpos(),edge.getEnd().getYpos());
                    if (edgeLine.intersectsLine(pb)){
                        intersectedEdges.add(edge);
                        Node intersect;
                        double edgeGrad = (edge.getEnd().getYpos()-edge.getStart().getYpos())/(edge.getEnd().getXpos()-edge.getStart().getXpos());
                        if(edgeGrad==0){
                            intersect = new Node(((edge.getStart().getYpos()-yIntercept)/pbGrad),edge.getStart().getYpos());
                        }else if(Double.isInfinite(edgeGrad)){
                            intersect = new Node(edge.getStart().getXpos(),(pbGrad*edge.getStart().getXpos())+yIntercept);
                        }else{
                            double edgeYIntercept = edge.getStart().getYpos()-(edgeGrad*edge.getStart().getXpos());
                            double x = (yIntercept-edgeYIntercept)/(edgeGrad-pbGrad);
                            double y = (edgeGrad*x)+edgeYIntercept;
                            intersect = new Node(x,y);
                        }
                        intersectionPoints.add(intersect);
                    }
                }
                if(intersectionPoints.size()==2) {
                    Edge tempEdge;
                    for (int x = 0; x < intersectionPoints.size(); x++) {
                        if (getDistance(intersectedEdges.get(x).getStart(), site) > getDistance(intersectedEdges.get(x).getEnd(), site)) {
                            tempEdge = new Edge(intersectedEdges.get(x).getStart(), intersectionPoints.get(x));
                        } else {
                            tempEdge = new Edge(intersectedEdges.get(x).getEnd(), intersectionPoints.get(x));
                        }
                        cell.addEdge(tempEdge);
                        cell.removeEdge(intersectedEdges.get(x));
                        //System.out.println("Edge from cell "+cell.getSite().toString()+" removed. Edge: "+intersectedEdges.get(x).getStart().toString()+" to "+intersectedEdges.get(x).getEnd().toString()+" removed.");
                    }
                    tempEdge = new Edge(intersectionPoints.get(0), intersectionPoints.get(1));
                    cell.addEdge(tempEdge);
                    tempCell.addEdge(tempEdge);
                }
//----------------------------------------------------------------------------------------------------------
                Boolean unattachedEdges = true;
                //System.out.println(cell.getSite().toString());
                while (unattachedEdges) {
                    HashSet<Edge> edgesToRemove = new HashSet<>();
                    for (Edge edge : cell.getEdges()) {
                        Boolean startConnected = false;
                        Boolean endConnected = false;
                        for (Edge otherEdge : cell.getEdges()) {
                            if (edge != otherEdge) {
                                if (otherEdge.hasNode(edge.getStart())) {
                                    startConnected = true;
                                }
                                if (otherEdge.hasNode(edge.getEnd())) {
                                    endConnected = true;
                                }
                            }
                        }
                        if (!(startConnected && endConnected)) {
//                            System.out.println(cell.getSite().toString() + " has unconnected edge from "+edge.getStart().toString()+" to "+edge.getEnd().toString());
                            edgesToRemove.add(edge);
                            unattachedEdges = true;
                        } else {
                            unattachedEdges = false;
                        }
                    }
                    for (Edge edge : edgesToRemove) {
//                        System.out.println("Edge from cell "+cell.getSite().toString()+" removed. Edge: "+edge.getStart().toString()+" to "+edge.getEnd().toString()+" removed.");
                        cell.removeEdge(edge);
                    }
                }
            }

            cells.add(tempCell);
        }

//        cells.remove(cell1);
//        cells.remove(cell2);
//        cells.remove(cell3);
//        cells.remove(cell4);
        return cells;
    }

    private static double getDistance(Node n1, Node n2){
        double a = Math.abs(n1.getXpos()-n2.getXpos());
        double b = Math.abs(n1.getYpos()-n2.getYpos());
        return Math.sqrt((a*a)+(b*b));
    }

    private static void display(HashSet<Cell> cells){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                for(Cell cell: cells){
                    if(cell.getSite().getXpos()==6){
                        g2.setStroke(new BasicStroke(1));
                    }else{
                        g2.setStroke(new BasicStroke(1));
                    }
                    g.fillOval((int)cell.getSite().getXpos()-2+(500),(int)cell.getSite().getYpos()-2+(500),4,4);
                    for(Edge edge: cell.getEdges()){
                        g2.drawLine((int)edge.getStart().getXpos()+500, (int)edge.getStart().getYpos()+500, (int)edge.getEnd().getXpos()+500, (int)edge.getEnd().getYpos()+500);
                    }
                }

            }
        };
        for(Cell cell: cells){
            System.out.println("("+cell.getSite().getXpos()+","+cell.getSite().getYpos()+")    Edges: "+cell.getEdges().size());
        }

        frame.add(panel);
        panel.setSize(new Dimension(width,height));
        panel.setBorder(BorderFactory.createLineBorder(Color.black));
        frame.setSize(new Dimension(width+1000,height+1000));
        frame.setVisible(true);
    }

}
