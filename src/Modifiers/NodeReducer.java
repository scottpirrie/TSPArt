package Modifiers;

import GUI.GUI2;
import DataClasses.Cluster;
import DataClasses.Node;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class NodeReducer {

    HashSet<Node> nodes;
    int maxNodes;
    GUI2 gui2;

    public NodeReducer(HashSet<Node> nodes, int maxNodes, GUI2 gui2){
        this.nodes=nodes;
        this.maxNodes=maxNodes;
        this.gui2=gui2;
        reduceNodes();
    }

    public void reduceNodes(){
        int iterations = 5;
        gui2.setReductionMin(0);
        gui2.setReductionMax(iterations);

        HashSet<Node> centres = new HashSet<>();
        HashSet<Cluster> clusters = new HashSet<>();
        ArrayList<Node> inputList = new ArrayList<>(nodes);
        Random rnd = new Random();
        if(maxNodes<nodes.size()) {
            System.out.println("Performing reduction algorithm");
            while (clusters.size() != maxNodes) {
                int num = rnd.nextInt(inputList.size());
                Node node = new Node(inputList.get(num).getXpos(), inputList.get(num).getYpos());
                clusters.add(new Cluster(node));
//                inputList.remove(num);
            }
            for(int x = 0; x<iterations ; x++) {
            //assign nodes to centre to form clusters
                 for (Node node : nodes) {
                    Cluster closestCluster = null;
                    for (Cluster cluster : clusters) {
                        if (closestCluster == null) {
                            closestCluster = cluster;
                        } else if (node.distanceTo(cluster.getSite()) < node.distanceTo(closestCluster.getSite())) {
                            closestCluster = cluster;
                        }
                    }
                    closestCluster.addNode(node);
                }

                //move centre to CoM of cluster
                for (Cluster cluster : clusters) {
                    if (cluster.getNodes().size() != 0) {
                        double totalX = 0;
                        double totalY = 0;
                        for (Node node : cluster.getNodes()) {
                            totalX += node.getXpos();
                            totalY += node.getYpos();
                        }
                        double averageX = totalX / cluster.getNodes().size();
                        double averageY = totalY / cluster.getNodes().size();
                        cluster.getSite().setXpos(Math.floor(averageX + 0.5));
                        cluster.getSite().setYpos(Math.floor(averageY + 0.5));
                        cluster.clearNodes();
                    }
                }
                gui2.setReductionProgress(x+1);
            }

            for (Cluster cluster : clusters) {
                centres.add(cluster.getSite());
            }
            System.out.println("Total number of nodes after reduction algorithm: " + centres.size());
            nodes = centres;
        }else{
            gui2.setReductionProgress(iterations);
        }
    }

    public HashSet<Node> getNodes(){
        return nodes;
    }
}
