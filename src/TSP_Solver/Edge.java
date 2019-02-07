package TSP_Solver;

public class Edge {

    private Node start;
    private Node end;
    private double weight;

    public Edge(Node start, Node end){
        this.start=start;
        this.end=end;
        double a = Math.abs(start.getXpos()-end.getXpos());
        double b = Math.abs(start.getYpos()-end.getYpos());
        this.weight = Math.sqrt((a*a)+(b*b));
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
        if (start.equalss(node) || end.equalss(node)){
            return true;
        }
        return false;
    }
}
