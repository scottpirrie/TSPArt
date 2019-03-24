package Modifiers;

import DataClasses.Edge;
import DataClasses.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashSet;

public class PanelCreator {

    int height;
    int width;

    public PanelCreator(BufferedImage image){
        this.height=image.getHeight();
        this.width = image.getWidth();
    }

    public JPanel createImagePanel(BufferedImage image, String name){
        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image,0,0,null);
            }
        };
        panel.setPreferredSize(new Dimension(image.getWidth(),image.getHeight()));
        panel.setName(name);
        return panel;
    }

    public JPanel createNodePanel(HashSet<Node> nodes, String name){
        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for(Node node : nodes){
                    g.fillOval((int)node.getXpos()-1, (int)node.getYpos()-1,2, 2 );
                }
            }
        };
        panel.setPreferredSize(new Dimension(width,height));
        panel.setName(name);
        return panel;
    }

    public JPanel createEdgePanel(HashSet<Edge> edges, String name){
        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for(Edge edge : edges){
                    g.drawLine((int)edge.getStart().getXpos(), (int)edge.getStart().getYpos(), (int)edge.getEnd().getXpos(), (int)edge.getEnd().getYpos());
                }
            }
        };
        panel.setPreferredSize(new Dimension(width,height));
        panel.setName(name);
        return panel;
    }
}
