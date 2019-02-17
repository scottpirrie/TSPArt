package LloydsAlgorithm;

import TSP_Solver.Edge;
import TSP_Solver.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class LloydsAlgoMain {

    static int height;
    static int width;
    static int scaleFactor=1;

    public static void main(String[] args) {
        height=800;
        width=800;
//        HashSet<Node> nodes = generateRandomNodes();
//        HashSet<Node> nodes = generateNodes();
        HashSet<Node> nodes = getNodesFromFile("Nodes w800 h800 n3000");
        HashSet<Cell> cells = createDiagram(nodes);
//        cells = cropDiagram(cells);
        display(cells);

    }

    private static HashSet<Node> generateRandomNodes() {
        HashSet<Node> nodes = new HashSet<>();
        Random rnd = new Random();
        double x;
        double y;
        int nodeCount=0;
        while (nodeCount<3000) {
            x=rnd.nextInt(width);
            y=rnd.nextInt(height);
            boolean exists=false;
            for(Node node: nodes){
                if(node.getXpos()==x && node.getYpos()==y){
                    exists=true;
                }
            }
            if(!exists) {
                nodes.add(new Node(x, y));
                nodeCount++;
            }
        }
        //save random distribution to file
        try {
            PrintWriter writer = new PrintWriter("Nodes w" + width + " h" + height + " n" + nodes.size(), "UTF-8");
            for(Node node: nodes){
                writer.println(node.getXpos());
                writer.println(node.getYpos());
            }
            writer.close();
        }catch (Exception e){
            System.out.println(e);
        }
        return nodes;
    }

    public LloydsAlgoMain(int width, int height){
        this.width=width;
        this.height=height;
    }

    public static HashSet<Node> voronoiRedistribute(HashSet<Node> nodes){
        HashSet<Node> output = new HashSet<>();
        HashSet<Cell> cells = createDiagram(nodes);
        cells = cropDiagram(cells);
        for(Cell cell: cells){
            if(!Double.isNaN(cell.getCentre().getXpos()) && !Double.isNaN(cell.getCentre().getYpos())) {
                double x=cell.getCentre().getXpos();
                double y=cell.getCentre().getYpos();
                boolean exists=false;
                for(Node node: output){
                    if(node.getXpos()==x && node.getYpos()==y){
                        exists=true;
                    }
                }
                if(!exists) {
                    output.add(new Node(x, y));
                }
            }
        }
        return output;
    }

    private static HashSet<Node> getNodesFromFile(String filepath){
        HashSet<Node> nodes = new HashSet<>();
        try {
            FileReader fr = new FileReader(filepath);
            BufferedReader reader = new BufferedReader(fr);

            String line = reader.readLine();

            double x;
            double y;
            while(!line.equals("")){
                x=Double.parseDouble(line);
                line=reader.readLine();
                y=Double.parseDouble(line);
                line=reader.readLine();
                nodes.add(new Node(x,y));
            }
            reader.close();
        }catch(Exception e){
            System.out.println("Exception while loading from file");
            System.out.println(e);
        }


        return nodes;
    }





    private static HashSet<Node> generateNodes(){
        HashSet<Node> nodes = new HashSet<>();

        nodes.add(new Node(470,430));
        nodes.add(new Node(430,410));
        nodes.add(new Node(430,400));
        nodes.add(new Node(400,400));
        nodes.add(new Node(440,420));
        nodes.add(new Node(430,480));
        nodes.add(new Node(400,460));
        nodes.add(new Node(440,460));
        nodes.add(new Node(490,460));
        nodes.add(new Node(420,400));
        nodes.add(new Node(790,790));

        nodes.add(new Node(425,415));
        nodes.add(new Node(425,411));
        nodes.add(new Node(427,413));
        nodes.add(new Node(429,417));
        nodes.add(new Node(424,413));
        nodes.add(new Node(410,410));






        return nodes;
    }

    public static HashSet<Cell> createDiagram(HashSet<Node> sites){
        HashSet<Cell> cells = new HashSet<>();

        Cell cell1 = new Cell(new Node((-width)*scaleFactor,-height*scaleFactor));
        Cell cell2 = new Cell(new Node((2*width)*scaleFactor,(-height)*scaleFactor));
        Cell cell3 = new Cell(new Node((2*width)*scaleFactor,(2*height)*scaleFactor));
        Cell cell4 = new Cell(new Node((-width)*scaleFactor, (2*height)*scaleFactor));

        Node nodeMiddle = new Node((width/2)*scaleFactor,(height/2)*scaleFactor);
        Node nodeTop = new Node((width/2)*scaleFactor,(-10*height)*scaleFactor);
        Node nodeBottom = new Node((width/2)*scaleFactor,(10*height)*scaleFactor);
        Node nodeLeft = new Node((-10*width)*scaleFactor,(height/2)*scaleFactor);
        Node nodeRight = new Node((10*width)*scaleFactor,(height/2)*scaleFactor);

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

        for (Node site: sites) {
            try {
                site.setXpos(site.getXpos() * scaleFactor);
                site.setYpos(site.getYpos() * scaleFactor);
                Cell tempCell = new Cell(site);
                for (Cell cell : cells) {
                    Node pbMid = new Node((site.getXpos() + cell.getSite().getXpos()) / 2, (site.getYpos() + cell.getSite().getYpos()) / 2);
                    double grad = ((site.getYpos() - cell.getSite().getYpos()) / (site.getXpos() - cell.getSite().getXpos()));
                    double pbGrad;
                    if (grad == 0) {
                        pbGrad = Double.POSITIVE_INFINITY;
                    } else if (Double.isInfinite(grad)) {
                        pbGrad = 0;
                    } else {
                        pbGrad = -1 / grad;
                    }
                    Node pbPoint1;
                    Node pbPoint2;
                    double yIntercept = 0;
                    if (!Double.isInfinite(pbGrad)) {
                        yIntercept = pbMid.getYpos() - (pbGrad * pbMid.getXpos());
                        pbPoint1 = new Node((-10 * width) * scaleFactor, ((pbGrad * ((-10 * width) * scaleFactor)) + yIntercept));
                        pbPoint2 = new Node((10 * width) * scaleFactor, ((pbGrad * ((10 * width) * scaleFactor)) + yIntercept));
                    } else {
                        pbPoint1 = new Node(pbMid.getXpos(), (-10 * height) * scaleFactor);
                        pbPoint2 = new Node(pbMid.getXpos(), (10 * height) * scaleFactor);
                    }
                    Line2D.Double pb = new Line2D.Double(pbPoint1.getXpos(), pbPoint1.getYpos(), pbPoint2.getXpos(), pbPoint2.getYpos());

                    ArrayList<Node> intersectionPoints = new ArrayList<>();
                    ArrayList<Edge> intersectedEdges = new ArrayList<>();

                    for (Edge edge : cell.getEdges()) {
                        Line2D.Double edgeLine = new Line2D.Double(edge.getStart().getXpos(), edge.getStart().getYpos(), edge.getEnd().getXpos(), edge.getEnd().getYpos());
                        if (edgeLine.intersectsLine(pb)) {
                            intersectedEdges.add(edge);
                            Node intersect;
                            double edgeGrad = (edge.getEnd().getYpos() - edge.getStart().getYpos()) / (edge.getEnd().getXpos() - edge.getStart().getXpos());
                            double edgeYIntercept;
                            if (Double.isInfinite(pbGrad)) {
                                edgeYIntercept = edge.getStart().getYpos() - (edgeGrad * edge.getStart().getXpos());
                                intersect = new Node(pbMid.getXpos(), (edgeGrad * pbMid.getXpos()) + edgeYIntercept);
                            } else if (Double.isInfinite(edgeGrad)) {
                                intersect = new Node(edge.getStart().getXpos(), (pbGrad * edge.getStart().getXpos()) + yIntercept);
                            } else if (edgeGrad == 0) {
                                intersect = new Node(((edge.getStart().getYpos() - yIntercept) / pbGrad), edge.getStart().getYpos());
                            } else {
                                edgeYIntercept = edge.getStart().getYpos() - (edgeGrad * edge.getStart().getXpos());
                                double x = (yIntercept - edgeYIntercept) / (edgeGrad - pbGrad);
                                double y = (edgeGrad * x) + edgeYIntercept;
                                intersect = new Node(x, y);
                            }
                            intersectionPoints.add(intersect);
                        }
                    }

//                    if(intersectionPoints.size()>2){
//                        System.out.println(site.toString() + " has "+intersectionPoints.size()+ " intersection points with cell "+cell.getSite().toString());
//                    }

                    if (intersectionPoints.size() == 4) {
                        //2 of the edges will share a point. find that point
                        // use has edge to find which edges have that point
                        // shared point will be same distance from point to cell and site. check to see which of the other ends of edges is closest to cell and site
                        ArrayList<Node> duplicatePoints = new ArrayList<>();
                        if (intersectionPoints.get(0).equalss(intersectionPoints.get(1))) {
                            duplicatePoints.add(intersectionPoints.get(0));
                            duplicatePoints.add(intersectionPoints.get(2));
                        } else if (intersectionPoints.get(0).equalss(intersectionPoints.get(2))) {
                            duplicatePoints.add(intersectionPoints.get(0));
                            duplicatePoints.add(intersectionPoints.get(1));
                        } else {
                            duplicatePoints.add(intersectionPoints.get(0));
                            duplicatePoints.add(intersectionPoints.get(1));
                        }

                        ArrayList<Edge> edgesToRemove = new ArrayList<>();
                        for (Node duplicatePoint : duplicatePoints) {
                            ArrayList<Edge> edgesWithDuplicatePoint = new ArrayList<>();
                            for (Edge edge : intersectedEdges) {
                                if (edge.hasNode(duplicatePoint)) {
                                    edgesWithDuplicatePoint.add(edge);
                                }
                            }
                            Edge badEdge;
//                            if (edgesWithDuplicatePoint.size() == 0) {
//                                System.out.println(cell.getSite().toString());
//                                System.out.println(site.toString());
//                                for (Edge edge2 : cell.getEdges()) {
//                                    System.out.println(edge2.getStart().toString() + " | " + edge2.getEnd().toString());
//                                }
//                                System.out.println("Intersection points");
//                                System.out.println(intersectionPoints.get(0).toString());
//                                System.out.println(intersectionPoints.get(1).toString());
//                                System.out.println(intersectionPoints.get(2).toString());
//                                System.out.println(intersectionPoints.get(3).toString());
//                            }

                            if (edgesWithDuplicatePoint.get(0).getStart().equalss(duplicatePoint)) {
                                if (getDistance(edgesWithDuplicatePoint.get(0).getEnd(), cell.getSite()) < getDistance(edgesWithDuplicatePoint.get(0).getEnd(), site)) {
                                    badEdge = edgesWithDuplicatePoint.get(1);
                                } else {
                                    badEdge = edgesWithDuplicatePoint.get(0);
                                }
                            } else {
                                if (getDistance(edgesWithDuplicatePoint.get(0).getStart(), cell.getSite()) < getDistance(edgesWithDuplicatePoint.get(0).getStart(), site)) {
                                    badEdge = edgesWithDuplicatePoint.get(1);
                                } else {
                                    badEdge = edgesWithDuplicatePoint.get(0);
                                }
                            }
                            edgesToRemove.add(badEdge);
                        }
                        intersectionPoints.removeAll(duplicatePoints);
                        intersectedEdges.removeAll(edgesToRemove);
                        cell.getEdges().removeAll(edgesToRemove);
//                        if(edgesToRemove.size()==2){
//                            System.out.println("All bad edges successfully removed");
//                        }
                    } else if (intersectionPoints.size() == 3) {
                        //2 of the edges will share a point. find that point
                        // use has edge to find which edges have that point
                        // shared point will be same distance from point to cell and site. check to see which of the other ends of edges is closest to cell and site
                        Node duplicatePoint;
                        if (intersectionPoints.get(0).equalss(intersectionPoints.get(1))) {
                            duplicatePoint = intersectionPoints.get(0);
                        } else {
                            duplicatePoint = intersectionPoints.get(2);
                        }
                        ArrayList<Edge> edgesWithDuplicatePoint = new ArrayList<>();
                        for (Edge edge : intersectedEdges) {
                            if (edge.hasNode(duplicatePoint)) {
                                edgesWithDuplicatePoint.add(edge);
                            }
                        }
                        Edge badEdge;
                        if (edgesWithDuplicatePoint.get(0).getStart().equalss(duplicatePoint)) {
                            if (getDistance(edgesWithDuplicatePoint.get(0).getEnd(), cell.getSite()) < getDistance(edgesWithDuplicatePoint.get(0).getEnd(), site)) {
                                badEdge = edgesWithDuplicatePoint.get(1);
                            } else {
                                badEdge = edgesWithDuplicatePoint.get(0);
                            }
                        } else {
                            if (getDistance(edgesWithDuplicatePoint.get(0).getStart(), cell.getSite()) < getDistance(edgesWithDuplicatePoint.get(0).getStart(), site)) {
                                badEdge = edgesWithDuplicatePoint.get(1);
                            } else {
                                badEdge = edgesWithDuplicatePoint.get(0);
                            }
                        }
                        intersectionPoints.remove(intersectedEdges.indexOf(badEdge));
                        intersectedEdges.remove(badEdge);
                        cell.getEdges().remove(badEdge);
                        //System.out.println("Bad edge successfully removed");
                    }

                    HashSet<Node> badNodes = new HashSet<>();
                    if (intersectionPoints.size() == 2) {
                        Edge tempEdge;
                        for (int x = 0; x < intersectionPoints.size(); x++) {
                            if (getDistance(intersectedEdges.get(x).getStart(), cell.getSite()) < getDistance(intersectedEdges.get(x).getStart(), site)) {
                                tempEdge = new Edge(intersectedEdges.get(x).getStart(), intersectionPoints.get(x));
                                if(!intersectionPoints.get(x).equalss(intersectedEdges.get(x).getEnd())){
                                    badNodes.add(intersectedEdges.get(x).getEnd());
                                }
                            } else {
                                tempEdge = new Edge(intersectedEdges.get(x).getEnd(), intersectionPoints.get(x));
                                if(!intersectionPoints.get(x).equalss(intersectedEdges.get(x).getStart())){
                                    badNodes.add(intersectedEdges.get(x).getStart());
                                }
                            }
                            cell.addEdge(tempEdge);
                            cell.removeEdge(intersectedEdges.get(x));
                        }
                        tempEdge = new Edge(intersectionPoints.get(0), intersectionPoints.get(1));
                        cell.addEdge(tempEdge);
                        tempCell.addEdge(tempEdge);
                    }else if(intersectionPoints.size()>0 && intersectionPoints.size()!=2){
                        System.out.println("Intersected edges in cell "+cell.getSite() + " when inserting "+site.toString() + " is more than 0 but not 2 therefor intersection points not added and intersected lines not broken " +intersectionPoints.size());
                    }

//                    HashSet<Edge> zeroEdges = new HashSet<>();
//                    for (Edge edge : cell.getEdges()) {
//                        if (edge.getStart().equalss(edge.getEnd())) {
//                            System.out.println("zero edge removed from cell "+cell.getSite().toString()+ " "+ edge.toString());
//                            zeroEdges.add(edge);
//                        }
//                    }
//                    if (!zeroEdges.isEmpty()) {
//                        cell.getEdges().removeAll(zeroEdges);
//                    }
//                    zeroEdges.clear();
//                    for (Edge edge : tempCell.getEdges()) {
//                        if (edge.getStart().equalss(edge.getEnd())) {
//                            System.out.println("zero edge removed from site "+site.toString()+" "+ edge.toString());
//                            zeroEdges.add(edge);
//                        }
//                    }
//                    if (!zeroEdges.isEmpty()) {
//                        tempCell.getEdges().removeAll(zeroEdges);
//                    }

//----------------------------------------------------------------------------------------------------------
                    Boolean unattachedEdges = true;
                    while (unattachedEdges) {
                        HashSet<Edge> edgesToRemove = new HashSet<>();
                        HashSet<Node> addToBad = new HashSet<>();
                        for (Node badNode : badNodes) {
                            for (Edge edge : cell.getEdges()) {
                                if (edge.hasNode(badNode)) {
                                    edgesToRemove.add(edge);
                                    if (edge.getStart().equalss(badNode)) {
                                        addToBad.add(edge.getEnd());
                                    } else {
                                        addToBad.add(edge.getStart());
                                    }
                                }
                            }
                        }
                        badNodes.clear();
                        badNodes.addAll(addToBad);
                        if (edgesToRemove.isEmpty()){
                            unattachedEdges=false;
                        }else{
                            cell.getEdges().removeAll(edgesToRemove);
                        }
                    }

//                    Boolean unattachedEdges = true;
//                    while (unattachedEdges) {
//                        HashSet<Edge> edgesToRemove = new HashSet<>();
//                        for (Edge edge : cell.getEdges()) {
//                            Boolean startConnected = false;
//                            Boolean endConnected = false;
//                            for (Edge otherEdge : cell.getEdges()) {
//                                if (edge != otherEdge) {
//                                    if (otherEdge.hasNode(edge.getStart())) {
//                                        startConnected = true;
//                                    }
//                                    if (otherEdge.hasNode(edge.getEnd())) {
//                                        endConnected = true;
//                                    }
//                                }
//                            }
//                            if (!(startConnected && endConnected)) {
//                                edgesToRemove.add(edge);
//                            }
//                        }
//                        for (Edge edge : edgesToRemove) {
//                            cell.removeEdge(edge);
//                        }
//                        if (edgesToRemove.size() == 0) {
//                            unattachedEdges = false;
//                        } else {
//                            unattachedEdges = true;
//                        }
//                        edgesToRemove.clear();
//                    }
                }


                cells.add(tempCell);
            }catch(IndexOutOfBoundsException e){
                System.out.println(e);
            }
        }

        cells.remove(cell1);
        cells.remove(cell2);
        cells.remove(cell3);
        cells.remove(cell4);

        for(Cell cell:cells){
            cell.getSite().setXpos(cell.getSite().getXpos()/scaleFactor);
            cell.getSite().setYpos(cell.getSite().getYpos()/scaleFactor);
            for(Edge edge: cell.getEdges()){
                edge.getStart().setXpos(edge.getStart().getXpos()/Math.cbrt(scaleFactor));
                edge.getStart().setYpos(edge.getStart().getYpos()/Math.cbrt(scaleFactor));
                edge.getEnd().setXpos(edge.getEnd().getXpos()/Math.cbrt(scaleFactor));
                edge.getEnd().setYpos(edge.getEnd().getYpos()/Math.cbrt(scaleFactor));
            }
        }

//        for(Cell cell: cells){
//            System.out.println("Site: "+cell.getSite());
//            for(Edge edge: cell.getEdges()){
//                System.out.println(edge.toString());
//            }
//        }

//        for(Cell cell: cells){
//            if (cell.getEdges().size()==0){
//                System.out.println(cell.getSite().toString()+ " has no edges.");
//            }
//        }

        return cells;
    }

    private static double getDistance(Node n1, Node n2){
        double a = Math.abs(n1.getXpos()-n2.getXpos());
        double b = Math.abs(n1.getYpos()-n2.getYpos());
        return Math.sqrt((a*a)+(b*b));
    }

    public static void display(HashSet<Cell> cells){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                for(Cell cell: cells){
                    if(cell.getSite().getXpos()==440 && cell.getSite().getYpos()==420){
                        g2.setStroke(new BasicStroke(1));
                    }else{
                        g2.setStroke(new BasicStroke(1));
                    }
                    g.fillOval((int)cell.getSite().getXpos()-2,(int)cell.getSite().getYpos()-2,4,4);
                    for(Edge edge: cell.getEdges()){
                        g2.drawLine((int)edge.getStart().getXpos(), (int)edge.getStart().getYpos(), (int)edge.getEnd().getXpos(), (int)edge.getEnd().getYpos());
                    }
//                    g.fillOval((int)cell.getCentre().getXpos()-1,(int)cell.getCentre().getYpos()-1,2,2);
                }

            }
        };
        panel.setPreferredSize(new Dimension(width,height));
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    private static HashSet<Cell> cropDiagram(HashSet<Cell> cells){
        Line2D.Double top = new Line2D.Double(0,0,width,0);
        Line2D.Double bottom = new Line2D.Double(0,height,width,height);
        Line2D.Double left = new Line2D.Double(0,0,0,height);
        Line2D.Double right = new Line2D.Double(width,0,width,height);

        for(Cell cell: cells){
            HashSet<Edge> edgesToRemove = new HashSet<>();
            HashSet<Edge> edgesToAdd = new HashSet<>();
            ArrayList<Node> intersectionPoints = new ArrayList<>();
            for(Edge edge: cell.getEdges()){
                Line2D.Double edgeLine = new Line2D.Double(edge.getStart().getXpos(),edge.getStart().getYpos(),edge.getEnd().getXpos(),edge.getEnd().getYpos());
                double edgeGrad = (edge.getEnd().getYpos()-edge.getStart().getYpos())/(edge.getEnd().getXpos()-edge.getStart().getXpos());
                double edgeYIntercept=0;
                if (!Double.isInfinite(edgeGrad)){
                    edgeYIntercept = edge.getStart().getYpos() - (edgeGrad * edge.getStart().getXpos());
                }
                Node intersection;
                Edge newEdge;
                if(!(edge.getStart().getXpos()>=0 && edge.getStart().getXpos()<=width)) {
                    if (!(edge.getStart().getYpos() >= 0 && edge.getStart().getYpos() <= height)) {
                        if (!(edge.getEnd().getXpos() >= 0 && edge.getEnd().getXpos() <= height)) {
                            if (!(edge.getEnd().getYpos() >= 0 && edge.getEnd().getYpos() <= height)) {
                                edgesToRemove.add(edge);
                            }
                        }
                    }
                }

                if((edge.getStart().getXpos()>width && edge.getEnd().getXpos()>width) || (edge.getStart().getYpos()>height && edge.getEnd().getYpos()>height)){
                    edgesToRemove.add(edge);
                }else if((edge.getStart().getXpos()<0 && edge.getEnd().getXpos()<0) || (edge.getStart().getYpos()>height && edge.getEnd().getYpos()>height)){
                    edgesToRemove.add(edge);
                }else if((edge.getStart().getXpos()<0 && edge.getEnd().getXpos()<0) || (edge.getStart().getYpos()<0 && edge.getEnd().getYpos()<0)){
                    edgesToRemove.add(edge);
                }else if((edge.getStart().getXpos()>width && edge.getEnd().getXpos()>width) || (edge.getStart().getYpos()<0 && edge.getEnd().getYpos()<0)){
                    edgesToRemove.add(edge);
                }

                if((edge.getStart().getXpos()>width || edge.getEnd().getXpos()>width) && (edge.getStart().getYpos()>height || edge.getEnd().getYpos()>height)){
                    edgesToRemove.add(edge);
                }else if((edge.getStart().getXpos()<0 || edge.getEnd().getXpos()<0) && (edge.getStart().getYpos()>height || edge.getEnd().getYpos()>height)){
                    edgesToRemove.add(edge);
                }else if((edge.getStart().getXpos()<0 || edge.getEnd().getXpos()<0) && (edge.getStart().getYpos()<0 || edge.getEnd().getYpos()<0)){
                    edgesToRemove.add(edge);
                }else if((edge.getStart().getXpos()>width || edge.getEnd().getXpos()>width) && (edge.getStart().getYpos()<0 || edge.getEnd().getYpos()<0)){
                    edgesToRemove.add(edge);
                }

                if(edgeLine.intersectsLine(top)){
                    if(Double.isInfinite(edgeGrad)){
                        intersection = new Node(edge.getStart().getXpos(),0);
                        intersectionPoints.add(intersection);
                    }else{
                        double x = (0-edgeYIntercept)/edgeGrad;
                        intersection = new Node(x, 0);
                        intersectionPoints.add(intersection);
                    }
                    edgesToRemove.add(edge);
                    if(edge.getEnd().getYpos()<0){
                        newEdge = new Edge(edge.getStart(),intersection);
                    }else{
                        newEdge = new Edge(edge.getEnd(),intersection);
                    }
                    edgesToAdd.add(newEdge);
                }else if(edgeLine.intersectsLine(bottom)){
                    if(Double.isInfinite(edgeGrad)){
                        intersection = new Node(edge.getStart().getXpos(),height);
                        intersectionPoints.add(intersection);
                    }else{
                        double x = (height-edgeYIntercept)/edgeGrad;
                        intersection = new Node(x, height);
                        intersectionPoints.add(intersection);
                    }
                    edgesToRemove.add(edge);
                    if(edge.getEnd().getYpos()>height){
                        newEdge = new Edge(edge.getStart(),intersection);
                    }else{
                        newEdge = new Edge(edge.getEnd(),intersection);
                    }
                    edgesToAdd.add(newEdge);
                }else if(edgeLine.intersectsLine(left)){
                    if(edgeGrad==0){
                        intersection = new Node(0,edge.getStart().getYpos());
                        intersectionPoints.add(intersection);
                    }else{
                        double y = (edgeGrad*0)+edgeYIntercept;
                        intersection = new Node(0, y);
                        intersectionPoints.add(intersection);
                    }
                    edgesToRemove.add(edge);
                    if(edge.getEnd().getXpos()<0){
                        newEdge = new Edge(edge.getStart(),intersection);
                    }else{
                        newEdge = new Edge(edge.getEnd(),intersection);
                    }
                    edgesToAdd.add(newEdge);
                }else if(edgeLine.intersectsLine(right)){
                    if(edgeGrad==0){
                        intersection = new Node(width,edge.getStart().getYpos());
                        intersectionPoints.add(intersection);
                    }else{
                        double y = (edgeGrad*width)+edgeYIntercept;
                        intersection = new Node(width, y);
                        intersectionPoints.add(intersection);
                    }
                    edgesToRemove.add(edge);
                    if(edge.getEnd().getXpos()>width){
                        newEdge = new Edge(edge.getStart(),intersection);
                    }else{
                        newEdge = new Edge(edge.getEnd(),intersection);
                    }
                    edgesToAdd.add(newEdge);
                }
            }
            if(intersectionPoints.size()==2){
                edgesToAdd.add(new Edge(intersectionPoints.get(0),intersectionPoints.get(1)));
            }
            for(Edge edge: edgesToRemove){
                cell.removeEdge(edge);
            }
            for(Edge edge: edgesToAdd){
                cell.addEdge(edge);
            }
        }
        return cells;
    }
}
