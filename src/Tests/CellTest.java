package Tests;

import DataClasses.Cell;
import DataClasses.Edge;
import DataClasses.Node;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CellTest {

    @Test
    public void cellReturnsCorrectSite(){
        Node site = new Node(10,10);
        Cell cell = new Cell(site);
        assertEquals(site,cell.getSite());
    }

    @Test
    public void cellAddsEdge(){
        Node site = new Node(10,10);
        Cell cell = new Cell(site);
        Node node1 = new Node(20,20);
        Node node2 = new Node(20,30);
        Edge edge1 = new Edge(node1,node2);
        cell.addEdge(edge1);
        assertEquals(1,cell.getEdges().size());
    }

    @Test
    public void cellReturnsCorrectSizeEdges(){
        Node site = new Node(10,10);
        Cell cell = new Cell(site);
        Node node1 = new Node(20,20);
        Node node2 = new Node(20,30);
        Node node3 = new Node(30,20);
        Node node4 = new Node(30,30);
        Edge edge1 = new Edge(node1,node2);
        Edge edge2 = new Edge(node2,node3);
        Edge edge3 = new Edge(node3,node4);
        Edge edge4 = new Edge(node4,node1);
        cell.addEdge(edge1);
        cell.addEdge(edge2);
        cell.addEdge(edge3);
        cell.addEdge(edge4);
        assertEquals(4,cell.getEdges().size());
    }

    @Test
    public void cellReturnsCorrectEdges(){
        Node site = new Node(10,10);
        Cell cell = new Cell(site);
        Node node1 = new Node(20,20);
        Node node2 = new Node(20,30);
        Edge edge1 = new Edge(node1,node2);
        cell.addEdge(edge1);
        assertTrue(cell.getEdges().contains(edge1));
    }

    @Test
    public void cellRemovesEdge(){
        Node site = new Node(10,10);
        Cell cell = new Cell(site);
        Node node1 = new Node(20,20);
        Node node2 = new Node(20,30);
        Edge edge1 = new Edge(node1,node2);
        cell.addEdge(edge1);
        cell.removeEdge(edge1);
        assertEquals(0,cell.getEdges().size());
    }

    @Test
    public void cellReturnsCorrectCentre(){
        Node site = new Node(10,10);
        Cell cell = new Cell(site);
        Node node1 = new Node(20,20);
        Node node2 = new Node(20,30);
        Node node3 = new Node(30,20);
        Node node4 = new Node(30,30);
        Edge edge1 = new Edge(node1,node2);
        Edge edge2 = new Edge(node2,node3);
        Edge edge3 = new Edge(node3,node4);
        Edge edge4 = new Edge(node4,node1);
        cell.addEdge(edge1);
        cell.addEdge(edge2);
        cell.addEdge(edge3);
        cell.addEdge(edge4);

        Node centre = new Node(25,25);

        assertEquals(centre, cell.getCentre());
    }
}