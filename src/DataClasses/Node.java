package DataClasses;

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

    public String toString(){
        return "("+xpos+","+ypos+")";
    }

    public double distanceTo(Node input){
        double a = Math.abs(xpos-input.getXpos());
        double b = Math.abs(ypos-input.getYpos());
        return Math.sqrt((a*a)+(b*b));
    }

    @Override
    public boolean equals(Object obj){
        if(obj instanceof Node) {
            if (Math.abs(xpos - ((Node) obj).getXpos()) < 0.00000000001) {
                if (Math.abs(ypos - ((Node) obj).getYpos()) < 0.00000000001) {
                    return true;
                }
            }
        }
        return false;
    }

}