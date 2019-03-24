package GUI;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashSet;

public class GUI3 {

    ArrayList<JPanel> panels;

    public GUI3(ArrayList<JPanel> panels){
        this.panels=panels;
    }

    public void display(){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JTabbedPane tabs = new JTabbedPane();
        for (JPanel panel : panels) {
            tabs.add(panel.getName(), panel);
        }
        frame.add(tabs);
        frame.pack();
        frame.setVisible(true);
    }
}
