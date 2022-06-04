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

    public static void main(String[] args) throws FileNotFoundException
    {
        int numNodes = 20;
        MyGraph myGraph = new MyGraph();
        boolean isOk = false;
        if (args.length == 2)
        {
            try
            {
                numNodes = Integer.parseInt(args[1]);
                // cannot be over 50 - overkill
                numNodes = Math.min(50, numNodes);
                if (args[0].equals("-r"))
                {
                    myGraph = GraphGen.generate_connected_graph(numNodes);
                    isOk = true;
                }
                else if (args[0].equals("-c"))
                {
                    myGraph = GraphGen.generate_perfect_graph(numNodes);
                    isOk = true;
                }
            }
            catch (Exception e)
            {
                System.err.println("second arg must be an integer");
            }
        }

        if (!isOk)
            myGraph = GraphGen.generate_connected_graph(numNodes);

        MyGraphAlgo g = new MyGraphAlgo();
        g.init(myGraph);
        new MyGraph_GUI(g.getGraph());

    }
}