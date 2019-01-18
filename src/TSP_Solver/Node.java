package TSP_Solver;

public class Node {

    private double xpos;
    private double ypos;

    public Node(double xpos, double ypos){
        this.xpos = xpos;
        this.ypos = ypos;
    }

    public double getXpos() {
        return xpos;
    }

    public void setXpos(double xpos) {
        this.xpos = xpos;
    }

    public double getYpos() {
        return ypos;
    }

    public void setYpos(double ypos) {
        this.ypos = ypos;
    }

}