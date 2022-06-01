package GUI;
import api.*;

import java.io.FileNotFoundException;


public class MyGraph_GUI{
    private MyFrame frame;
    private MyPanel panel;
    private DirectedWeightedGraph graph;



    public MyGraph_GUI(DirectedWeightedGraph g) {
        this.graph = g;
        this.frame = new MyFrame(this.graph);
        this.panel = new MyPanel(this.graph);
    }
    public MyGraph_GUI(DirectedWeightedGraph g, String log) {
        this.graph = g;
        this.frame = new MyFrame(this.graph);
        this.panel = new MyPanel(this.graph);
        this.frame.setOutputText(log);
    }

    public static void main(String[] args) throws FileNotFoundException {
        ParseToGraph pd = new ParseToGraph("data/G2.json");
        MyGraph mg = new MyGraph(pd);
        new MyGraph_GUI(mg);
    }
}
