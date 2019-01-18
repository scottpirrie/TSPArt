package Image_To_Nodes;

import TSP_Solver.Edge;
import TSP_Solver.Node;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.HashSet;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws IOException {
        File inputImage = new File("black with circle.png");
        BufferedImage image = ImageIO.read(inputImage);
        BufferedImage greyImage = convertToGreyscale(image);
        HashSet<Node> nodes = new HashSet();

        displayImage(greyImage);
        nodes = displayImageAsNodes(greyImage);

        drawNodes(greyImage,nodes);

    }

    private static BufferedImage convertToGreyscale(BufferedImage image){
        for(int x=0; x<image.getWidth(); x++){
            for(int y=0; y<image.getHeight(); y++){
                Color tempColor = new Color(image.getRGB(x,y));
                int avg = (tempColor.getRed()+tempColor.getGreen()+tempColor.getBlue())/3;
                tempColor = new Color(avg,avg,avg);
                image.setRGB(x,y,tempColor.getRGB());
            }
        }
        return image;
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
        int gridSize = 5;
        for(int x=0; x<image.getWidth()-(image.getWidth()%gridSize); x=x+gridSize){
            for(int y=0; y<image.getHeight()-(image.getHeight()%gridSize); y=y+gridSize){
                int gridTotal = 0;
                for(int i=0; i<gridSize; i++){
                    for (int j=0; j<gridSize; j++){
                        gridTotal += imageArray[x+i][y+j];
                    }
                }
                int gridAverage = gridTotal/(gridSize*gridSize);
                int gridNodeNum = (gridAverage*(gridSize*gridSize))/255;
                //System.out.println(gridNodeNum);
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
        //panel.setSize(new Dimension(800,800));
        panel.setBorder(BorderFactory.createLineBorder(Color.black));
        frame.setSize(new Dimension(image.getWidth()+16,image.getHeight()+40));
        frame.setVisible(true);
    }
}