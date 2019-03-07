package Final;

import TSP_Solver.Node;

import java.awt.*;
import java.awt.image.BufferedImage;
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
                int gridNodeNum = (gridAverage*gridSize)/255;
                int nodeCount=0;
                while (nodeCount<gridNodeNum){
                    Node tempNode = new Node(rnd.nextInt(gridSize)+x,rnd.nextInt(gridSize)+y);
                    if(!nodes.contains(tempNode)){
                        nodes.add(tempNode);
                        nodeCount++;
                    }
                }
            }
        }
    }

    public HashSet<Node> getNodes(){
        System.out.println("Total number of nodes: "+nodes.size());
        return nodes;
    }
}
