import GUI.MyGraph_GUI;
import api.*;

import java.io.FileNotFoundException;

/**
 * This class is the main class for Ex2 - your implementation will be tested using this class.
 */
public class RunBrooksAlgorithm {
    /**
     * This static function will be used to test your implementation
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     * @return
     */
    public static DirectedWeightedGraph getGraph(String json_file) {
        try {
            ParseToGraph pd = new ParseToGraph(json_file);
            return new MyGraph(pd);
        }
        catch (FileNotFoundException e){
            System.err.println("File not found!");
            e.printStackTrace();
        }
        return null;
    }
    /**
     * This static function will be used to test your implementation
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     * @return
     */
    public static DirectedWeightedGraphAlgorithms getGrapgAlgo(String json_file) {
        DirectedWeightedGraphAlgorithms ans = new MyGraphAlgo();
        ans.load(json_file);
        return ans;
    }
    /**
     * This static function will run your GUI using the json fime.
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     *
     */
    public static void runGUI(String json_file) {
        DirectedWeightedGraph graph = getGraph(json_file);
        new MyGraph_GUI(graph);
    }

    public static void main(String[] args) throws FileNotFoundException {

        MyGraphAlgo g = new MyGraphAlgo();
        g.init(GraphGen.generate_connected_graph(20));
        new MyGraph_GUI(g.getGraph());

    }
}