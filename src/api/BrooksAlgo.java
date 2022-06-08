package api;

import java.util.*;

public class BrooksAlgo
{
    private DirectedWeightedGraph graph;
    private DirectedWeightedGraphAlgorithms graph_algo;
    private ArrayList<DirectedWeightedGraph> graphsArray = new ArrayList<>();
    private DirectedWeightedGraph original_graph;

    public BrooksAlgo(DirectedWeightedGraph OrigGraph)
    {
        SCC(OrigGraph);
    }

    /**
     * create components and subgraphs from one graph
     * @param OrigGraph
     */
    private void SCC(DirectedWeightedGraph OrigGraph)
    {
        original_graph = OrigGraph;
        graph = OrigGraph;
        graph_algo = new MyGraphAlgo();
        graph_algo.init(OrigGraph);
        // get all the graph components
        graphsArray = graph_algo.getGraphsFromBreaker(null);
    }

    /**
     * reset the coloring for the graph and the subgraphs
     */
    public void resetColoring()
    {
        if (original_graph == null)
            return;
        // reset the color of the graph
        original_graph.resetColors();

        // reset the colors for all the components
        for (DirectedWeightedGraph g : graphsArray)
        {
            if (g != null)
                g.resetColors();
        }
    }

    /**
     * the main function of the class - combines all the functions and set the min number of colors according to Brooks Algo
     */
    public void BrooksColoring()
    {
        resetColoring();
        // for every sub graph mke the coloring
        for (DirectedWeightedGraph graphs: graphsArray)
        {
            // set the graph to current
            this.graph = graphs;
            // set the algorithm and its graph
            this.graph_algo = new MyGraphAlgo();
            graph_algo.init(graph);

            int delta = this.graph_algo.getMaxDegree();
            // if the graph is only one node then set its color
            if (graphs.nodeSize() == 1)
            {
                NodeData node = graphs.getSomeNode();
                node.setColor(1);
                node.set_is_colored(true);
            }
            else if (delta <= 2)
            {
                // if the graph is odd cycle then make arbitrary order
                if (this.graph_algo.is_Odd_cycle())
                    greedyColor(arbitraryOrder());

                else if (this.graph_algo.is_path())     // if the graph is path then create its order
                    greedyColor(createPathOrder());

                else                                        // else build the circle order
                    greedyColor(buildEvenCircleOrder());
            }
            // if the graph is clique then
            else if (this.graph_algo.is_clique())
            {
                greedyColor(arbitraryOrder());
            }
            else if (this.graph_algo.is_Kregular()) // if the graph is k regular perform the xyz trio
            {
                int[] xyz = this.graph_algo.find_xyz();
                ArrayList<Integer> order = XYZ_Case(xyz);
                greedyColor(order);
            }
            else if (this.graph_algo.isOneConnected())  // if the graph is one connected
            {
                NodeData breaker = this.graph_algo.isOneConnectedNode();    // get the node that makes the break
                // get the graphs that become after the removal of the breaker node
                ArrayList<DirectedWeightedGraph> graph_list = this.graph_algo.getGraphsFromBreaker(breaker);
                oneConCase(graph_list, breaker);    // set it to the case
            }
            else
            {
                greedyColor(this.graph_algo.spanTree(this.graph_algo.KeyToStart()));
            }

        }
        // set the colors of the nodes colors to the original
        setColorsToOriginal(graphsArray, original_graph);
        printColorsOfOriginal();

    }

    /**
     * the function is the case of one connected graph - takes a list of graph and the breaker node and
     * paint the nodes by greedy algorithm
     * @param graph_list the graphs after the split of the breaker
     * @param braker the node that makes 2 or more components of graphs
     */
    public void oneConCase(ArrayList<DirectedWeightedGraph> graph_list, NodeData braker){
        if (graph_list == null || braker == null)
            return;

        for (DirectedWeightedGraph curr_g : graph_list)
        {
            // init the graph algo to this graph
            this.graph_algo.init(curr_g);
            // print it
            greedyColor(this.graph_algo.spanTree(braker.getKey()));
        }

        // init to the original current graph
        this.graph_algo.init(graph);
    }

    /**
     * perform the case of the trio
     * @param xyz
     * @return
     */
    public ArrayList<Integer> XYZ_Case(int[] xyz)
    {
        this.graph.getNode(xyz[1]).setColor(1); //y and z are not neighbors so they can be colored the same color
        this.graph.getNode(xyz[1]).set_is_colored(true);
        this.graph.getNode(xyz[2]).setColor(1);
        this.graph.getNode(xyz[2]).set_is_colored(true);

        DirectedWeightedGraph copy = graph_algo.copy();
        this.graph_algo.init(copy);

        // the spanning tree will skip y,z
        ArrayList<Integer> order = this.graph_algo.spanTree(xyz[0]);
        this.graph_algo.init(this.graph);
        return order;
    }

    public DirectedWeightedGraph G_withoutYZ(int[] yz){
        DirectedWeightedGraph ans = graph_algo.copy();
        ans.removeNode(yz[0]);
        ans.removeNode(yz[1]);
        return ans;
    }

    /**
     * by giving array of the id nodes the function will color the current graph by the greedy coloring technique
     * @param nodes_key_order
     */
    public void greedyColor(ArrayList<Integer> nodes_key_order) {

        for (int nodeNum : nodes_key_order)
        {
            // get the node and its
            Node node = (Node) this.graph.getNode(nodeNum);
            HashSet<Integer> neighbors_color = new HashSet<>();
            //for each neighbor get its color
            for (Integer key : node.getNeighbours())
            {
                Node neighbor = (Node) this.graph.getNode(key);
                // if the neighbor has color then set it to the colored
                if (neighbor.get_is_colored())
                    neighbors_color.add(neighbor.getColor());
            }
            // the color starts from 1
            int color = 1;
            // if the color is existed then try the next until number of color available
            while (neighbors_color.contains(color))
            {
                color++;
            }
            // set the color and the flag of coloring
            node.setColor(color);
            node.set_is_colored(true);
        }
    }

    /**
     * get an arbitary path from arbitary node
     * @return
     */
    public ArrayList<Integer> arbitraryOrder()
    {
        ArrayList<Integer> order = new ArrayList<Integer>();
        Iterator<NodeData> iter = graph.nodeIter();
        while (iter.hasNext()) {
            order.add(iter.next().getKey());
        }
        return order;
    }

    /**
     * take the graph that is path and make from it the list of occurrences (applying BFS on the graph)
     * @return
     */
    public ArrayList<Integer> createPathOrder() {
        ArrayList<Integer> path = new ArrayList<Integer>();
        int first = -1;
        Iterator<NodeData> iter = this.graph_algo.getGraph().nodeIter();
        // find the point that is the start of it all - its degree must be 1
        while(iter.hasNext())
        {
            NodeData node = iter.next();
            if(node.getDegree() == 1)
            {
                first = node.getKey();
                break;
            }
        }
        // return the BFS order from this point
        return graph_algo.spanTree(first);
    }

    /**
     * by giving a circle build the DFS order because it gives the circle as in entering order
     * @return
     */
    public ArrayList<Integer> buildEvenCircleOrder() {
        return DFS(this.graph, graph.getSomeNode());
    }

    /**
     * DFS order according to a given node
     * @param curr_graph the graph that dfs is on
     * @param node the node that DFS runs from
     * @return
     */
    private ArrayList<Integer> DFS(DirectedWeightedGraph curr_graph, NodeData node) {
        Stack<NodeData> keys = new Stack<>();
        ArrayList<Integer> order_of_tree = new ArrayList<>();
        keys.push(node);

        for (int node_id : MyGraphAlgo.nodesListInt(curr_graph))
        {
            curr_graph.getNode(node_id).setVisited(false);
        }

        while (!keys.isEmpty())
        {
            NodeData curr_node = keys.pop();
            if (curr_node.isVisited())
                continue;
            curr_node.setVisited(true);
            order_of_tree.add(curr_node.getKey());
            for (int neighbour: curr_node.getNeighbours()){
                if(!curr_graph.getNode(neighbour).isVisited()){
                    keys.push(curr_graph.getNode(neighbour));
                }
            }
        }
        return order_of_tree;
    }

    /**
     * by giving a list of graphs the function sets the colors to the other graph the color states
     * @param graph_list the graphs
     * @param g - the original graph
     */
    public void setColorsToOriginal(ArrayList<DirectedWeightedGraph> graph_list, DirectedWeightedGraph g)
    {
        for (DirectedWeightedGraph temp_graph : graph_list)
        {
            this.graph_algo.init(temp_graph);

            // for each subgraph
            for (int nodeId: MyGraphAlgo.nodesListInt(temp_graph))
            {
                NodeData currNode = temp_graph.getNode(nodeId);
                if (currNode == null)
                    continue;
                NodeData ogNode = g.getNode(nodeId);
                if (ogNode == null)
                    continue;

                // copy the coloring data to the node in the original graph
                ogNode.setVisited(currNode.isVisited());
                ogNode.setColor(currNode.getColor());
            }
        }
    }

    /**
     * prints the colors of each node's graph
     */
    public void printColorsOfOriginal()
    {
        Iterator<NodeData> nodes = this.original_graph.nodeIter();
        System.out.println();
        System.out.println("number of colors:\t" + getNumberOfColors());
        System.out.println("*".repeat(20));

        while (nodes.hasNext())
        {
            NodeData nd = nodes.next();
            if (nd == null)
                continue;
            System.out.println(nd.getKey() + "\t===>\t" + nd.getColor());
        }

        System.out.println("*".repeat(20));
        System.out.println();
    }

    /**
     * returns how many colors does the graph have
     * @return number of colors
     */
    public int getNumberOfColors() {
        int max_color = 1;
        Iterator<NodeData> node_iter = this.original_graph.nodeIter();
        while (node_iter.hasNext()) {
            Node node = (Node) node_iter.next();
            max_color = Math.max(node.getColor(), max_color);
        }
        return max_color;
    }

}
