package GUI;

import javax.swing.*;
import java.awt.*;

public class GUI2{

    JFrame frame;

    JProgressBar stipplingProgress;
    JProgressBar reductionProgress;
    JProgressBar voronoiProgress;
    JProgressBar tspProgress;

    boolean reduction;
    boolean voronoi;

    public GUI2(boolean reduction, boolean voronoi) {
        this.reduction = reduction;
        this.voronoi = voronoi;
    }

    public void createGUI(){
        frame = new JFrame("TSP Art");
        frame.setLayout(new BoxLayout(frame.getContentPane(),BoxLayout.PAGE_AXIS));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel stipplingPanel = new JPanel();
        JLabel stipplingLabel = new JLabel("Stippling");
        stipplingPanel.add(stipplingLabel);
        stipplingProgress = new JProgressBar();
        stipplingPanel.add(stipplingProgress);
        frame.add(stipplingPanel);

        if(reduction){
            JPanel reductionPanel = new JPanel();
            JLabel reductionLabel = new JLabel("Node Reduction");
            reductionPanel.add(reductionLabel);
            reductionProgress = new JProgressBar();
            reductionPanel.add(reductionProgress);
            frame.add(reductionPanel);
        }

        if(voronoi){
            JPanel voronoiPanel = new JPanel();
            JLabel voronoiLabel = new JLabel("Voronoi Redistribution");
            voronoiPanel.add(voronoiLabel);
            voronoiProgress = new JProgressBar();
            voronoiPanel.add(voronoiProgress);
            frame.add(voronoiPanel);
        }

        JPanel tspPanel = new JPanel();
        JLabel tspLabel = new JLabel("Solving TSP");
        tspPanel.add(tspLabel);
        tspProgress = new JProgressBar();
        tspPanel.add(tspProgress);
        frame.add(tspPanel);

        frame.setSize(new Dimension(200,400));
        frame.setVisible(true);
    }

    public void setStipplingMax(int input){
        stipplingProgress.setMaximum(input);
    }

    public void setStipplingMin(int input){
        stipplingProgress.setMinimum(input);
    }

    public void setStipplingProgress(int input){
        stipplingProgress.setValue(input);
    }


    public void setReductionMax(int input){
        stipplingProgress.setMaximum(input);
    }

    public void setReductionMin(int input){
        stipplingProgress.setMinimum(input);
    }

    public void setReductionProgress(int input){
        reductionProgress.setValue(input);
    }


    public void setVoronoiMax(int input){
        stipplingProgress.setMaximum(input);
    }

    public void setVoronoiMin(int input){
        stipplingProgress.setMinimum(input);
    }

    public void setVoronoiProgress(int input){
        voronoiProgress.setValue(input);
    }


    public void setTspMax(int input){
        stipplingProgress.setMaximum(input);
    }

    public void setTspMin(int input){
        stipplingProgress.setMinimum(input);
    }

    public void setTspProgress(int input){
        tspProgress.setValue(input);
    }
}
