package Tests;

import DataClasses.Node;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NodeTest {

    @Test
    public void nodeReturnsCorrectX(){
        Node node = new Node(30,40);
        assertEquals(30,node.getXpos());
    }

    @Test
    public void nodeReturnsCorrectY(){
        Node node = new Node(30,40);
        assertEquals(40,node.getYpos());
    }

    @Test
    public void nodeSetsCorrectX(){
        Node node = new Node(30,40);
        node.setXpos(50);
        assertEquals(50,node.getXpos());
    }

    @Test
    public void nodeSetsCorrectY(){
        Node node = new Node(30,40);
        node.setYpos(60);
        assertEquals(60,node.getYpos());
    }

    @Test
    public void nodeReturnsCorrectString(){
        Node node = new Node(30,40);
        assertTrue(node.toString().equals("(30.0,40.0)"));
    }

    @Test
    public void nodeReturnsCorrectDistanceTo(){
        Node node1 = new Node(0,0);
        Node node2 = new Node(30,40);
        assertEquals(50,node1.distanceTo(node2));
    }

    @Test
    public void nodeIsEqual(){
        Node node1 = new Node(30,40);
        Node node2 = new Node(30,40);
        assertTrue(node1.equals(node2));
    }

    @Test
    public void nodeIsEqualThreshold(){
        Node node1 = new Node(30,40);
        Node node2 = new Node(30,40.000000000000001);
        assertTrue(node1.equals(node2));
    }

    @Test
    public void nodeIsNotEqual(){
        Node node1 = new Node(30,40);
        Node node2 = new Node(30,41);
        assertFalse(node1.equals(node2));
    }




}