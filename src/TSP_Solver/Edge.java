package TSP_Solver;

public class Edge {

    private Node start;
    private Node end;
    private double weight;

    public Edge(Node start, Node end){
        this.start=start;
        this.end=end;
        this.weight = start.distanceTo(end);
    }

    public Node getStart() {
        return start;
    }

    public Node getEnd() {
        return end;
    }

    public double getWeight() {
        return weight;
    }

    public Boolean hasNode(Node node){
        if (start.equals(node) || end.equals(node)){
            return true;
        }
        return false;
    }

    public String toString(){
        return start+" | "+end;
    }
}
