package api;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.HashMap;


public class ParseToGraph {
    private HashMap<Integer, Node> Nodes;
    private HashMap<String, Edge> Edges;
    private ParserFromJson p;
    public int size = 0;

    public ParseToGraph()
    {

    }
    public ParseToGraph(String file_name) throws FileNotFoundException {
        Gson gson = new Gson();
            Reader reader = new FileReader(file_name);
            this.p = gson.fromJson(reader, ParserFromJson.class);
            if (this.p != null) {
                this.Nodes = makeNodes(p.getNodes());
                this.Edges = makeEdges(p.getEdges(), this.Nodes);
            }
    }

    /**
     *
     * @param Nodes
     * @return
     */
    public HashMap<Integer, Node> makeNodes(HashMap<String, String>[] Nodes){
        if(Nodes != null) {
            HashMap<Integer, Node> nodes = new HashMap<>();
            String[] keys;
            for (HashMap<String, String> node : Nodes) {
                keys = node.keySet().toArray(new String[0]);
                double x = 0;
                double y = 0;
                int id = 0;
                for (String key : keys) {
                    if (key.equals("pos")) {
                        String[] coordinates = node.get(key).split(",");
                        x = Double.parseDouble(coordinates[0]);
                        y = Double.parseDouble(coordinates[1]);
                    } else { // key == "id"
                        id = Integer.parseInt(node.get(key));
                    }
                }
                Node Node = new Node(x, y, id);
                nodes.put(id, Node);
            }
            return nodes;
        }
        return  null;
    }

    /**
     *
     * @param Edges
     * @param nodes
     * @return
     */
    public HashMap<String, Edge> makeEdges(HashMap<String, String>[] Edges, HashMap<Integer, Node> nodes){
        if(Edges != null) {
            HashMap<String, Edge> edges = new HashMap<>();
            String[] keys;
            for(HashMap<String, String> edge: Edges){
                keys = edge.keySet().toArray(new String[0]);
                int src = 0;
                int dest = 0;
                double weight = 0;
                for(String key: keys){
                    if(key.equals("src")){
                        src = Integer.parseInt(edge.get(key));
                    }
                    else if(key.equals("w")){
                        weight = Double.parseDouble(edge.get(key));
                    }
                    else{ // key == dest
                        dest = Integer.parseInt(edge.get(key));
                    }
                }
                Edge Edge1 = new Edge(src, dest, weight);
                nodes.get(src).addEdge(Edge1);
                nodes.get(dest).addInEdge(src);

                Edge Edge2 = new Edge(dest, src, weight);
                nodes.get(dest).addEdge(Edge2);
                nodes.get(src).addInEdge(dest);
                size += 2;
                edges.put(Edge1.toString(), Edge1);
                edges.put(Edge2.toString(), Edge2);
            }
            return edges;
        }
        return null;
    }


    public HashMap<Integer, Node> getNodes() {
        return this.Nodes;
    }

    public HashMap<String, Edge> getEdges() {
        return this.Edges;
    }

//    public static void main(String[]args){
//        ParseData pd = new ParseData("C:\\Users\\netan\\IdeaProjects\\Weighted_Graph_Algorithms\\data\\G1.json");
//        System.out.println(pd.getEdges().values());
//        System.out.println();
//        System.out.println(pd.getNodes().values());
//        DirectedWeightedGraph g = new MyGraph(pd.getNodes(), pd.getEdges());
//        System.out.println();
//    }
}


class ParserFromJson {
    private HashMap<String, String>[] Edges;
    private HashMap<String, String>[] Nodes;
    public ParserFromJson(){}
    public HashMap<String, String>[] getEdges() {
        return this.Edges;
    }
    public HashMap<String, String>[] getNodes() {
        return this.Nodes;
    }

}


