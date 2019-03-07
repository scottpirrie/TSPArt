package Final;

import ReductionAlgorithm.Cluster;
import TSP_Solver.Node;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class NodeReducer {

    HashSet<Node> nodes;
    int maxNodes;

    public NodeReducer(HashSet<Node> nodes, int maxNodes){
        this.nodes=nodes;
        this.maxNodes=maxNodes;
        reduceNodes();
    }

    public void reduceNodes(){
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
                inputList.remove(num);
            }
            //assign nodes to centre to form clusters
            for (Node node : nodes) {
                Cluster closestCluster = null;
                for (Cluster cluster : clusters) {
                    if (closestCluster == null) {
                        closestCluster = cluster;
                    } else if (node.distanceTo(cluster.getCentre()) < node.distanceTo(closestCluster.getCentre())) {
                        closestCluster = cluster;
                    }
                }
                closestCluster.addNode(node);
            }

            //move centre to CoM of cluster
            for (Cluster cluster : clusters) {
                if (cluster.getNodes().size() != 0){
                    double totalX = 0;
                    double totalY = 0;
                    for (Node node : cluster.getNodes()) {
                        totalX += node.getXpos();
                        totalY += node.getYpos();
                    }
                    double averageX = totalX / cluster.getNodes().size();
                    double averageY = totalY / cluster.getNodes().size();
                    cluster.getCentre().setXpos(Math.floor(averageX+0.5));
                    cluster.getCentre().setYpos(Math.floor(averageY+0.5));
                    cluster.clearNodes();
                }
            }


            for (Cluster cluster : clusters) {
                centres.add(cluster.getCentre());
            }
            System.out.println("Total number of nodes after reduction algorithm: " + centres.size());
            nodes = centres;
        }
    }

    public HashSet<Node> getNodes(){
        return nodes;
    }
}
