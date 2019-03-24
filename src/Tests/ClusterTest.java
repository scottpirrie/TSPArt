package Tests;

import DataClasses.Cluster;
import DataClasses.Node;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClusterTest {

    @Test
    public void clusterReturnsSite(){
        Node site = new Node(10,10);
        Cluster cluster = new Cluster(site);
        assertEquals(site,cluster.getSite());
    }

    @Test
    public void clusterAddsNode(){
        Node site = new Node(10,10);
        Cluster cluster = new Cluster(site);
        Node node1 = new Node(20,20);
        cluster.addNode(node1);
        assertEquals(1,cluster.getNodes().size());
    }

    @Test
    public void clusterGetsNodes(){
        Node site = new Node(10,10);
        Cluster cluster = new Cluster(site);
        Node node1 = new Node(20,20);
        cluster.addNode(node1);
        assertTrue(cluster.getNodes().contains(node1));
    }

    @Test
    public void clusterClearsNodes(){
        Node site = new Node(10,10);
        Cluster cluster = new Cluster(site);
        cluster.clearNodes();
        assertEquals(0,cluster.getNodes().size());
    }

}