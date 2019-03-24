package Tests;

import DataClasses.Edge;
import DataClasses.Node;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EdgeTest {

    @Test
    public void edgeReturnsCorrectStart(){
        Node node1 = new Node(00,00);
        Node node2 = new Node(30,40);
        Edge edge = new Edge(node1,node2);
        assertEquals(edge.getStart(),node1);
    }

    @Test
    public void edgeReturnsCorrectEnd(){
        Node node1 = new Node(00,00);
        Node node2 = new Node(30,40);
        Edge edge = new Edge(node1,node2);
        assertEquals(edge.getEnd(),node2);
    }

    @Test
    public void edgeReturnsCorrectWeight(){
        Node node1 = new Node(00,00);
        Node node2 = new Node(30,40);
        Edge edge = new Edge(node1,node2);
        assertEquals(50,edge.getWeight());
    }

    @Test
    public void edgeHasStartNode(){
        Node node1 = new Node(00,00);
        Node node2 = new Node(30,40);
        Edge edge = new Edge(node1,node2);
        assertTrue(edge.hasNode(node1));
    }

    @Test
    public void edgeHasEndNode(){
        Node node1 = new Node(00,00);
        Node node2 = new Node(30,40);
        Edge edge = new Edge(node1,node2);
        assertTrue(edge.hasNode(node2));
    }
}