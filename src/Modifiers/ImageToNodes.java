package Modifiers;

import DataClasses.Node;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class ImageToNodes {

    BufferedImage image;
    int gridSize;
    HashSet<Node> nodes;

    public ImageToNodes(BufferedImage image, int gridSize){
        this.image=image;
        this.gridSize=gridSize;
        createNodes();
    }

    public void createNodes(){
        nodes = new HashSet<>();
        int[][] imageArray = new int[image.getWidth()][image.getHeight()];
        Random rnd = new Random();

        for(int x=0; x<image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                imageArray[x][y] = new Color(image.getRGB(x, y)).getBlue();
            }
        }
        for(int x=0; x<image.getWidth(); x=x+gridSize){
            for(int y=0; y<image.getHeight(); y=y+gridSize){
                int gridTotal = 0;
                ArrayList<Node> possibleNodes = new ArrayList<>();
                for(int i=0; i<gridSize; i++){
                    for (int j=0; j<gridSize; j++){
                        gridTotal += 255-imageArray[x+i][y+j];
                        possibleNodes.add(new Node(x+i,y+j));
                    }
                }
                double gridAverage = gridTotal/(gridSize*gridSize);
                double gridNodeNum = Math.floor((gridAverage/255)*((gridSize*gridSize)/2));

                int nodeCount=0;
                while (nodeCount<gridNodeNum){
                    int random = rnd.nextInt(possibleNodes.size());
                    nodes.add(possibleNodes.get(random));
                    possibleNodes.remove(random);
                    nodeCount++;
                }
            }
        }
    }

    public HashSet<Node> getNodes(){
        System.out.println("Total nodes in graph "+nodes.size());
        return nodes;
    }
}
