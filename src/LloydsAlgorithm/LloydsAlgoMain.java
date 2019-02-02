package LloydsAlgorithm;

import TSP_Solver.Edge;
import TSP_Solver.Node;

import java.util.HashSet;
import java.util.Random;

public class LloydsAlgoMain {

    static int height=400;
    static int width=400;

    public static void main(String[] args) {
        HashSet<Node> nodes = generateNodes();
    }

    private static HashSet<Node> generateNodes() {
        HashSet<Node> nodes = new HashSet<>();
        Random rnd = new Random();
        for (int x = 0; x < 100; x++) {
            nodes.add(new Node(rnd.nextInt(height), rnd.nextInt(width)));
        }
        return nodes;
    }

    private static void createDiagram(HashSet<Node> sites){
        
    }

}
