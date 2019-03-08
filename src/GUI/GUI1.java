package GUI;

import Final.TSPArt;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class GUI1 {

    JFrame frame;

    private JFileChooser fileChooser;
    private JCheckBox boostContrast;
    private JSpinner boostFactor;
    private JSpinner gridSize;
    private JCheckBox limitNodeCount;
    private JSpinner maxNodes;
    private JCheckBox voronoiRedistribute;
    private JSpinner voronoiIterations;
    private JRadioButton farthestInsertion;
    private JRadioButton nearestInsertion;

    public GUI1(){
        createGUI();
    }

    private void createGUI(){
        frame = new JFrame("TSP Art");
        frame.setLayout(new BoxLayout(frame.getContentPane(),BoxLayout.PAGE_AXIS));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JLabel label;

        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new GridLayout(5,1));
        imagePanel.setPreferredSize(new Dimension(400,200));

        JLabel imageHeading = new JLabel("Image");
        imageHeading.setBorder(new EmptyBorder(0,10,0,0));
        imagePanel.add(imageHeading);

        JPanel fileChooserPanel = new JPanel();
        fileChooserPanel.setLayout(new GridLayout(1,2));
        fileChooserPanel.setBorder(new EmptyBorder(0,30,0,20));
        JButton chooseFile = new JButton("Choose file");
        JLabel filePath = new JLabel("No file selected");
        chooseFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == chooseFile){
                    fileChooser = new JFileChooser();
                    if(fileChooser.showOpenDialog(chooseFile) == JFileChooser.APPROVE_OPTION){
                        filePath.setText(fileChooser.getSelectedFile().toString());
                    }
                }
            }
        });
        fileChooserPanel.add(chooseFile);
        fileChooserPanel.add(filePath);
        imagePanel.add(fileChooserPanel);

        JPanel boostContrastPanel = new JPanel();
        boostContrastPanel.setLayout(new GridLayout(1,2));
        boostContrastPanel.setBorder(new EmptyBorder(0,30,0,20));
        label = new JLabel("Boost Contrast");
        boostContrastPanel.add(label);
        boostContrast = new JCheckBox();
        boostContrastPanel.add(boostContrast);
        imagePanel.add(boostContrastPanel);

        JPanel boostFactorPanel = new JPanel();
        boostFactorPanel.setLayout(new GridLayout(1,2));
        boostFactorPanel.setBorder(new EmptyBorder(0,30,0,20));
        label = new JLabel("Contrast Boost Factor");
        boostFactorPanel.add(label);
        boostFactor = new JSpinner(new SpinnerNumberModel(2,1,5,1));
        boostFactor.setEnabled(false);
        boostFactorPanel.add(boostFactor);
        imagePanel.add(boostFactorPanel);

        boostContrast.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boostFactor.setEnabled(boostContrast.isSelected());
            }
        });


        JPanel stipplingPanel = new JPanel();
        stipplingPanel.setLayout(new GridLayout(7,1));
        stipplingPanel.setPreferredSize(new Dimension(400,280));

        JLabel stipplingHeading = new JLabel("Stippling");
        stipplingHeading.setBorder(new EmptyBorder(0,10,0,0));
        stipplingPanel.add(stipplingHeading);

        JPanel gridSizePanel = new JPanel();
        gridSizePanel.setLayout(new GridLayout(1,2));
        gridSizePanel.setBorder(new EmptyBorder(0,30,0,20));
        label = new JLabel("Grid Size");
        gridSizePanel.add(label);
        gridSize = new JSpinner(new SpinnerNumberModel(5,3,10,1));
        gridSizePanel.add(gridSize);
        stipplingPanel.add(gridSizePanel);

        JPanel limitNodeCountPanel = new JPanel();
        limitNodeCountPanel.setLayout(new GridLayout(1,2));
        limitNodeCountPanel.setBorder(new EmptyBorder(0,30,0,20));
        label = new JLabel("Limit Nodes");
        limitNodeCountPanel.add(label);
        limitNodeCount = new JCheckBox();
        limitNodeCountPanel.add(limitNodeCount);
        stipplingPanel.add(limitNodeCountPanel);

        JPanel maxNodesPanel = new JPanel();
        maxNodesPanel.setLayout(new GridLayout(1,2));
        maxNodesPanel.setBorder(new EmptyBorder(0,30,0,20));
        label = new JLabel("Max Node Count");
        maxNodesPanel.add(label);
        maxNodes = new JSpinner(new SpinnerNumberModel(3000,100,50000,100));
        maxNodes.setEnabled(false);
        maxNodesPanel.add(maxNodes);
        stipplingPanel.add(maxNodesPanel);

        limitNodeCount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                maxNodes.setEnabled(limitNodeCount.isSelected());
            }
        });

        JPanel voronoiRedistributePanel = new JPanel();
        voronoiRedistributePanel.setLayout(new GridLayout(1,2));
        voronoiRedistributePanel.setBorder(new EmptyBorder(0,30,0,20));
        label = new JLabel("Voronoi Redistribute");
        voronoiRedistributePanel.add(label);
        voronoiRedistribute = new JCheckBox();
        voronoiRedistributePanel.add(voronoiRedistribute);
        stipplingPanel.add(voronoiRedistributePanel);

        JPanel voronoiIterationsPanel = new JPanel();
        voronoiIterationsPanel.setLayout(new GridLayout(1,2));
        voronoiIterationsPanel.setBorder(new EmptyBorder(0,30,0,20));
        label = new JLabel("Voronoi Iterations");
        voronoiIterationsPanel.add(label);
        voronoiIterations = new JSpinner(new SpinnerNumberModel(5,1,50,1));
        voronoiIterations.setValue(5);
        voronoiIterations.setEnabled(false);
        voronoiIterationsPanel.add(voronoiIterations);
        stipplingPanel.add(voronoiIterationsPanel);

        voronoiRedistribute.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                voronoiIterations.setEnabled(voronoiRedistribute.isSelected());
            }
        });


        JPanel TSPPanel = new JPanel();
        TSPPanel.setLayout(new GridLayout(4,1));
        TSPPanel.setPreferredSize(new Dimension(400,160));

        JLabel TSPHeading = new JLabel("TSP");
        TSPHeading.setBorder(new EmptyBorder(0,10,0,0));
        TSPPanel.add(TSPHeading);

        ButtonGroup buttonGroup = new ButtonGroup();

        JPanel farthestInsertionPanel = new JPanel();
        farthestInsertionPanel.setLayout(new GridLayout(1,2));
        farthestInsertionPanel.setBorder(new EmptyBorder(0,30,0,20));
        label = new JLabel("Farthest Insertion");
        farthestInsertionPanel.add(label);
        farthestInsertion = new JRadioButton();
        farthestInsertion.setSelected(true);
        buttonGroup.add(farthestInsertion);
        farthestInsertionPanel.add(farthestInsertion);
        TSPPanel.add(farthestInsertionPanel);

        JPanel nearestInsertionPanel = new JPanel();
        nearestInsertionPanel.setLayout(new GridLayout(1,2));
        nearestInsertionPanel.setBorder(new EmptyBorder(0,30,0,20));
        label = new JLabel("Nearest Insertion");
        nearestInsertionPanel.add(label);
        nearestInsertion = new JRadioButton();
        buttonGroup.add(nearestInsertion);
        nearestInsertionPanel.add(nearestInsertion);
        TSPPanel.add(nearestInsertionPanel);

        JPanel buttonPanel = new JPanel();
//        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JButton button = new JButton("Create TSP Art");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createButtonPressed();
            }
        });
        buttonPanel.add(button);

        frame.add(imagePanel);
        frame.add(stipplingPanel);
        frame.add(TSPPanel);
        frame.add(buttonPanel);

        frame.pack();

//        frame.setSize(new Dimension(200,800));
        frame.setVisible(true);
    }

    private void createButtonPressed() {
        if (fileChooser != null) {
            if(fileChooser.getSelectedFile()!=null) {
                try {
                    if (ImageIO.read(new File(fileChooser.getSelectedFile().getPath())) != null) {
                        frame.dispose();
                        TSPArt tspArt = new TSPArt(
                                fileChooser.getSelectedFile(),
                                boostContrast.isSelected(),
                                (int) boostFactor.getValue(),
                                (int) gridSize.getValue(),
                                limitNodeCount.isSelected(),
                                (int) maxNodes.getValue(),
                                voronoiRedistribute.isSelected(),
                                (int) voronoiIterations.getValue(),
                                farthestInsertion.isSelected(),
                                nearestInsertion.isSelected()
                        );
                    } else {
                        JOptionPane.showMessageDialog(null,"Selected file must be an image");
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            }else{
                JOptionPane.showMessageDialog(null,"You must select an image to create TSP Art");
            }
        }else{
            JOptionPane.showMessageDialog(null,"You must select an image to create TSP Art");
        }
    }
}
