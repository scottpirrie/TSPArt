package Final;

import GUI.GUI2;
import TSP_Solver.Edge;
import TSP_Solver.Node;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.TimeZone;

public class TSPArt implements Runnable{

    private File file;
    private boolean boostContrast;
    private double boostFactor;
    private int gridSize;
    private boolean limitNodeCount;
    private int maxNodes;
    private boolean voronoiRedistribute;
    private int voronoiIterations;
    private boolean farthestInsertion;
    private boolean nearestInsertion;

    public TSPArt(File fileChooser, Boolean boostContrast, double boostFactor, int gridSize, boolean limitNodeCount, int maxNodes, boolean voronoiRedistribute, int voronoiIterations, boolean farthestInsertion, boolean nearestInsertion) {
        this.file = fileChooser;
        this.boostContrast = boostContrast;
        this.boostFactor = boostFactor;
        this.gridSize = gridSize;
        this.limitNodeCount = limitNodeCount;
        this.maxNodes = maxNodes;
        this.voronoiRedistribute = voronoiRedistribute;
        this.voronoiIterations = voronoiIterations;
        this.farthestInsertion = farthestInsertion;
        this.nearestInsertion = nearestInsertion;

    }

    public void run(){
        try {
            createTSP();
        } catch (Exception e) {
            System.out.println("Exception in TSPArt "+e);
        }
    }

    private void createTSP() throws IOException {
        long startTime = System.nanoTime();
        GUI2 gui2 = new GUI2(limitNodeCount, voronoiRedistribute);
        gui2.createGUI();

        ImageHandler ih = new ImageHandler(file, gridSize);

        HashSet<JPanel> panels = new HashSet<>();
        PanelCreator pc = new PanelCreator(ih.getImage());

        gui2.setStipplingMin(0);
        gui2.setStipplingMax(3);

        BufferedImage originalImage = cloneImage(ih.getImage());
        panels.add(pc.createImagePanel(originalImage, "Original Image"));
        gui2.setStipplingProgress(1);

        BufferedImage image = ih.getBlackAndWhite();
        BufferedImage blackAndWhiteImage = cloneImage(image);
        panels.add(pc.createImagePanel(blackAndWhiteImage, "Halftoned Image"));
        gui2.setStipplingProgress(2);

        if (boostContrast) {
            ContrastBooster cb = new ContrastBooster(image);
            image = cb.getBoostedImage(boostFactor);
            BufferedImage boostedImage = cloneImage(image);
            panels.add(pc.createImagePanel(boostedImage, "Boosted Contrast"));
        }

        HashSet<Node> nodes;
        ImageToNodes itn = new ImageToNodes(image, gridSize);
        nodes = itn.getNodes();
        HashSet<Node> cityDistribution = (HashSet<Node>) nodes.clone();
        panels.add(pc.createNodePanel(cityDistribution, "City Distribution"));
        gui2.setStipplingProgress(3);

        if (limitNodeCount) {
//            gui2.setReductionMin(0);
//            gui2.setReductionMax(1);
            NodeReducer nr = new NodeReducer(nodes, maxNodes, gui2);
            nodes = nr.getNodes();
            HashSet<Node> reducedNodes = (HashSet<Node>) nodes.clone();
            panels.add(pc.createNodePanel(reducedNodes, "Reduced Nodes"));
//            gui2.setReductionProgress(1);
        }

        if (voronoiRedistribute) {
            gui2.setVoronoiMin(0);
            gui2.setVoronoiMax(voronoiIterations);
            System.out.println("node size before voronoi " + nodes.size());
            Voronoi voro = new Voronoi(nodes, image);
            for (int x = 0; x < voronoiIterations; x++) {
                voro.redestribute();
                gui2.setVoronoiProgress(x + 1);
                System.out.println("Voronoi iterations completed "+(x+1)+"/"+voronoiIterations);
                System.out.println(voro.getNodes().size());
            }
            voro.removeWhiteNodes();
            nodes = voro.getNodes();
            HashSet<Node> voronoiDistribution = (HashSet<Node>) nodes.clone();
            panels.add(pc.createNodePanel(voronoiDistribution, "Voronoi Distribution"));
            System.out.println("Post voronoi size " + nodes.size());
        }


        HashSet<Edge> edges;
        if (farthestInsertion) {
            FarthestInsertion fi = new FarthestInsertion(nodes, gui2);
            edges = fi.solveTSP();
        } else {
            NearestInsertion ni = new NearestInsertion(nodes, gui2);
            edges = ni.solveTSP();
        }

        panels.add(pc.createEdgePanel(edges, "Solved TSP"));

        gui2.close();

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JTabbedPane tabs = new JTabbedPane();
        for (JPanel panel : panels) {
            tabs.add(panel.getName(), panel);
        }
        frame.add(tabs);
        frame.pack();
        frame.setVisible(true);

        long endTime = System.nanoTime();
        long totalTime = endTime-startTime;

        totalTime = (int)Math.floor(totalTime/1000000);

        Date date = new Date(totalTime);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss:SSS");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        String time = format.format(date);

        if(boostContrast){
            System.out.println("Contrast boost: "+boostFactor);
        }
        System.out.println("Grid size: "+gridSize);
        System.out.println("Time taken to generate TSP Art " +time);

    }

    static BufferedImage cloneImage(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

}
