package api;

import java.util.*;

public class BrooksAlgo
{
    private DirectedWeightedGraph graph;
    private DirectedWeightedGraphAlgorithms graph_algo;
    private ArrayList<DirectedWeightedGraph> graphsArray = new ArrayList<>();
    private DirectedWeightedGraph original_graph;

    public BrooksAlgo(DirectedWeightedGraph OrigGraph) {
        SCC(OrigGraph);
    }

    public void BrooksColoring() {
        for (DirectedWeightedGraph graphs: graphsArray) {
            this.graph = graphs;
            this.graph_algo = new MyGraphAlgo();
            graph_algo.init(graph);

            int delta = this.graph_algo.getMaxDegree();
            if (graphs.nodeSize() == 1)
            {
                NodeData node = graphs.getSomeNode();
                node.setColor(1);
                node.set_is_colored(true);
            }
            else if (this.graph_algo.getMaxDegree() <= 2)
            {
                if (this.graph_algo.is_Odd_cycle())
                    greedyColor(arbitraryOrder());
                else if (this.graph_algo.is_path())
                {
                    greedyColor(createPathOrder());
                }
                else
                {
                    greedyColor(buildEvenCircleOrder());
                }

            }
            else if (this.graph_algo.is_clique())
            {
                greedyColor(arbitraryOrder());
            }
            else if (this.graph_algo.isOneConnected())
            {
                NodeData breaker = this.graph_algo.isOneConnectedNode();
                ArrayList<DirectedWeightedGraph> graph_list = this.graph_algo.getGraphsFromBreaker(breaker);
                oneConCase(graph_list, breaker);
            }
            else if (!this.graph_algo.is_Kregular())    // this is not a K-regular graph
            {
                greedyColor(this.graph_algo.spanTree(this.graph_algo.KeyToStart()));

            }
            else
            {
                int[] xyz = this.graph_algo.find_xyz();
                ArrayList<Integer> order = XYZ_Case(xyz);
                greedyColor(order);
            }
            Iterator<NodeData> nodes = this.graph_algo.getGraph().nodeIter();
            while (nodes.hasNext())
            {
                NodeData nd = nodes.next();
                if (nd == null)
                    continue;
                System.out.println(nd.getKey() + "\t===>\t" + nd.getColor());
            }

        }

        setColorsToOriginal(graphsArray, original_graph);
    }
    public void oneConCase(ArrayList<DirectedWeightedGraph> graph_list, NodeData braker){
        if (graph_list.size() == 0)
            return;

        this.graph_algo.init(graph_list.get(0));
        greedyColor(this.graph_algo.spanTree(braker.getKey()));
        int org_color = braker.getColor();

        for (int i = 1; i < graph_list.size(); i++)
        {
            this.graph_algo.init(graph_list.get(i));
            greedyColor(this.graph_algo.spanTree(braker.getKey()));
            int sec_color = braker.getColor();

            if(org_color!=sec_color) //in this case we need to switch colors
            {
                for (Iterator<NodeData> it = graph_algo.getGraph().nodeIter(); it.hasNext(); ) {
                    NodeData node = it.next();

                    if(node.getColor() == sec_color)
                        node.setColor(org_color);

                    else if(node.getColor() == org_color)
                        node.setColor(sec_color);

                }
            }
        }
        //setColorsToOriginal(graph_list, this.graph);
        this.graph_algo.init(graph);
    }

    public void setColorsToOriginal(ArrayList<DirectedWeightedGraph> graph_list, DirectedWeightedGraph g)
    {

        for (DirectedWeightedGraph temp_graph : graph_list)
        {
            this.graph_algo.init(temp_graph);

            for (int nodeId: MyGraphAlgo.nodesListInt(temp_graph))
            {
                NodeData currNode = temp_graph.getNode(nodeId);
                if (currNode == null)
                    continue;
                NodeData ogNode = g.getNode(nodeId);
                if (ogNode == null)
                    continue;

                ogNode.setVisited(currNode.isVisited());
                ogNode.setColor(currNode.getColor());
            }
        }
    }

    public ArrayList<Integer> XYZ_Case(int[] xyz){
        this.graph.getNode(xyz[1]).setColor(1); //y and z are not neighbors so they can be colored the same color
        this.graph.getNode(xyz[2]).setColor(1);
        DirectedWeightedGraph copy = graph_algo.copy();
        DirectedWeightedGraph org = graph_algo.copy();
        //remove y and z
        copy.removeNode(xyz[1]);
        copy.removeNode(xyz[2]);
        this.graph = copy;
        this.graph_algo.init(this.graph);
        ArrayList<Integer> order = this.graph_algo.spanTree(xyz[0]); // spanning Tree without y,z
        this.graph = org; //back to the original graph with y and z
        this.graph_algo.init(this.graph);
        return order;

    }
    public DirectedWeightedGraph G_withoutYZ(int[] yz){
        DirectedWeightedGraph ans = graph_algo.copy();
        ans.removeNode(yz[0]);
        ans.removeNode(yz[1]);
        return ans;
    }

    public void greedyColor(ArrayList<Integer> nodes_key_order) {
        for (int nodeNum : nodes_key_order)
        {
            Node node = (Node) this.graph.getNode(nodeNum);
            node.set_is_colored(false);
        }

        for (int nodeNum : nodes_key_order) {
            Node node = (Node) this.graph.getNode(nodeNum);
            HashSet<Integer> neighbors_color = new HashSet<>();
            for (Integer key : node.getNeighbours()) {
                Node neighbor = (Node) this.graph.getNode(key);
                if (neighbor.get_is_colored()) {
                    neighbors_color.add(neighbor.getColor());
                }
            }
            int color = 1;
            while (neighbors_color.contains(color)) {
                color++;
            }
            node.setColor(color);
            node.set_is_colored(true);
        }
    }

    public ArrayList<Integer> arbitraryOrder() {
        ArrayList<Integer> order = new ArrayList<Integer>();
        Iterator<NodeData> iter = graph.nodeIter();
        while (iter.hasNext()) {
            order.add(iter.next().getKey());
        }
        return order;
    }



    public ArrayList<Integer> createPathOrder() {
        ArrayList<Integer> path = new ArrayList<Integer>();
        int first = -1;
        Iterator<NodeData> iter = this.graph_algo.getGraph().nodeIter();
        while(iter.hasNext())
        {
            NodeData node = iter.next();
            if(node.getDegree() == 1)
            {
                first = node.getKey();
                break;
            }
        }

        return graph_algo.spanTree(first);
    }

    public ArrayList<Integer> buildEvenCircleOrder() {
        ArrayList<Integer> order = DFS(this.graph, graph.getSomeNode());
        for (int i : order)
            System.out.print(i + "   ");
        System.out.println();
        return order;
    }


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

    private DirectedWeightedGraphAlgorithms createSubGraph(DirectedWeightedGraph curr_graph, ArrayList<Integer> order_to_build) {
        DirectedWeightedGraphAlgorithms curr_component = new MyGraphAlgo();
        DirectedWeightedGraph temp_graph = new MyGraph();

        for (int vertex : order_to_build) {
            temp_graph.addNode(curr_graph.getNode(vertex));
        }

        for (int vertex1 : order_to_build) {
            for (int vertex2 : curr_graph.getNode(vertex1).getNeighbours()) {
                temp_graph.connect(vertex1, vertex2, 1);
            }
        }
        curr_component.init(temp_graph);
        return curr_component;
    }

    private void SCC(DirectedWeightedGraph OrigGraph){
        original_graph = OrigGraph;
        graph = OrigGraph;
        graph_algo = new MyGraphAlgo();
        graph_algo.init(OrigGraph);
        graphsArray = graph_algo.getGraphsFromBreaker(null);


    }

    public int getNumberOfColors() {
        int max_color = 1;
        Iterator node_iter = this.original_graph.nodeIter();
        while (node_iter.hasNext()) {
            Node node = (Node) node_iter.next();
            max_color = Math.max(node.getColor(), max_color);
        }
        return max_color;
    }

}
