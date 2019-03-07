package Final;

import TSP_Solver.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;

public class TSPArt {

    private File file;
    private boolean boostContrast;
    private int boostFactor;
    private int gridSize;
    private boolean limitNodeCount;
    private int maxNodes;
    private boolean voronoiRedistribute;
    private int voronoiIterations;
    private boolean farthestInsertion;
    private boolean nearestInsertion;

    public TSPArt(File fileChooser, Boolean boostContrast, int boostFactor, int gridSize, boolean limitNodeCount, int maxNodes, boolean voronoiRedistribute, int voronoiIterations, boolean farthestInsertion, boolean nearestInsertion){
        this.file=fileChooser;
        this.boostContrast=boostContrast;
        this.boostFactor=boostFactor;
        this.gridSize=gridSize;
        this.limitNodeCount=limitNodeCount;
        this.maxNodes=maxNodes;
        this.voronoiRedistribute=voronoiRedistribute;
        this.voronoiIterations=voronoiIterations;
        this.farthestInsertion=farthestInsertion;
        this.nearestInsertion=nearestInsertion;

        try {
            createTSP();
        }catch(Exception e){
            System.out.println(e);
        }
    }

    private void createTSP() throws IOException {
        ImageHandler ih = new ImageHandler(file,gridSize);

        HashSet<JPanel> panels = new HashSet<>();
        PanelCreator pc = new PanelCreator(ih.getImage());
        BufferedImage originalImage = cloneImage(ih.getImage());
        panels.add(pc.createImagePanel(originalImage, "Original Image"));

        BufferedImage image = ih.getBlackAndWhite();
        BufferedImage blackAndWhiteImage = cloneImage(image);
        panels.add(pc.createImagePanel(blackAndWhiteImage, "Halftoned Image"));

        if(boostContrast){
            ContrastBooster cb = new ContrastBooster(image);
            image = cb.getBoostedImage(boostFactor);
            BufferedImage boostedImage = cloneImage(image);
            panels.add(pc.createImagePanel(boostedImage, "Boosted Contrast"));
        }

        HashSet<Node> nodes;
        ImageToNodes itn = new ImageToNodes(image,gridSize);
        nodes = itn.getNodes();
        HashSet<Node> cityDistribution = (HashSet<Node>) nodes.clone();
        panels.add(pc.createNodePanel(cityDistribution, "City Distribution"));

        if(limitNodeCount){
            NodeReducer nr = new NodeReducer(nodes, maxNodes);
            nodes = nr.getNodes();
            HashSet<Node> reducedNodes = (HashSet<Node>) nodes.clone();
            panels.add(pc.createNodePanel(reducedNodes, "Reduced Nodes"));
        }

        if(voronoiRedistribute){
            Voronoi voro = new Voronoi(nodes,image);
            for(int x=0; x<voronoiIterations; x++) {
                voro.redestribute();
                System.out.println(voro.getNodes().size());
                System.out.println(x + " ----------------------------------------------------------------------------------------------------------------");
            }
            voro.removeWhiteNodes();
            nodes = voro.getNodes();
            HashSet<Node> voronoiDistribution = (HashSet<Node>) nodes.clone();
            panels.add(pc.createNodePanel(voronoiDistribution,"Voronoi Distribution"));
        }

        System.out.println("Post voronoi size " + nodes.size());

        if(farthestInsertion){

        }else{
            //do nearest insertion
        }

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JTabbedPane tabs = new JTabbedPane();
        for(JPanel panel: panels) {
            tabs.add(panel.getName(),panel);
        }
        frame.add(tabs);
        frame.pack();
        frame.setVisible(true);






    }

    static BufferedImage cloneImage(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }


}
