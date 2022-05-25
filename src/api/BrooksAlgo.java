package api;

import java.util.*;

public class BrooksAlgo {
    private DirectedWeightedGraph graph;
    private DirectedWeightedGraphAlgorithms graph_algo;
    private ArrayList<DirectedWeightedGraphAlgorithms> Components = new ArrayList<>();

    public BrooksAlgo(DirectedWeightedGraph OrigGraph) {
        SCC(OrigGraph);
    }

    public void BrooksColoring() {
        for (DirectedWeightedGraphAlgorithms component: this.Components) {
            this.graph_algo = component;
            this.graph = component.getGraph();
            int delta = this.graph_algo.getMaxDegree();
            if (this.graph_algo.getMaxDegree() <= 2) {
                if (this.graph_algo.is_Odd_cycle())
                    greedyColor(arbitraryOrder());
                else if (this.graph_algo.is_path()) {
                    greedyColor(createPathOrder());
                } else {
                    greedyColor(buildEvenCircleOrder());
                }

            } else if (this.graph_algo.is_clique()) {
                greedyColor(arbitraryOrder());
            } else if (!this.graph_algo.is_Kregular()) { // this is not a K-regular graph
                greedyColor(this.graph_algo.spanTree(this.graph_algo.KeyToStart()));

            } else if (this.graph_algo.isOneConnected()) {
                NodeData breker = this.graph_algo.isOneConnectedNode();
                ArrayList<DirectedWeightedGraph> graph_list = this.graph_algo.getGraphsFromBreaker(breker);


            } else {
                int[] xyz = this.graph_algo.find_xyz();
                ArrayList<Integer> order = XYZ_Case(xyz);
                greedyColor(order);
            }
        }


    }
    public void oneConCase(ArrayList<DirectedWeightedGraph> graph_list, NodeData braker){
        this.graph_algo.init(graph_list.get(1));
        greedyColor(this.graph_algo.spanTree(braker.getKey()));
        int org_color = braker.getColor();
        this.graph_algo.init(graph_list.get(2));
        greedyColor(this.graph_algo.spanTree(braker.getKey()));
        int sec_color = braker.getColor();
        if(org_color!=sec_color){ //in this case we need to switch colors
            for (Iterator<NodeData> it = graph_algo.getGraph().nodeIter(); it.hasNext(); ) {
                NodeData node = it.next();
                if(node.getColor() == sec_color){
                    node.setColor(org_color);
                }else if(node.getColor() == org_color){
                    node.setColor(sec_color);
                }
            }
        }
        this.graph_algo.init(graph);

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
        for (int i = 0; i < nodes_key_order.size(); i++) {
            Node node = (Node) this.graph.getNode(nodes_key_order.get(i));
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
        int first = -1, last = -1;
        boolean firstFound = false;
        Iterator<NodeData> iter = this.graph_algo.getGraph().nodeIter();
        while(iter.hasNext()){
            if(iter.next().getDegree() == 1){
                if(!firstFound) {
                    first = iter.next().getKey();
                    path.add(first);
                    firstFound = true;
                }
                else{
                    last = iter.next().getKey();
                }
            }
        }
        path.add(this.graph.getNode(first).getNeighbours().iterator().next());
        while (path.size() < this.graph.nodeSize() - 1){
            for(int curr : this.graph.getNode(path.size() - 1).getNeighbours()){
                if (!path.contains(curr)) {
                    path.add(curr);
                }
            }
        }
        path.add(last);
        return path;
    }

    public ArrayList<Integer> buildEvenCircleOrder() {
        ArrayList<Integer> order = new ArrayList<Integer>();
        NodeData first = this.graph.getNode(0); //chosen in arbitrary
        order.add(first.getKey());
        HashSet<Integer> firstNei = first.getNeighbours();
        for (int sec : first.getNeighbours()) {
            order.add(sec);
            break;
        }

        while (order.size() < graph.nodeSize()) {
            for (int nei : graph.getNode(order.size() - 1).getNeighbours()) {
                if (!order.contains(nei)) {
                    order.add(nei);
                }
            }
        }
        return order;
    }


    private ArrayList<Integer> DFS(DirectedWeightedGraph curr_graph, NodeData node) {
        Stack<NodeData> keys = new Stack<>();
        ArrayList<Integer> order_of_tree = new ArrayList<>();
        keys.push(node);
        while (!keys.isEmpty()){
            NodeData curr_node = keys.pop();
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
                temp_graph.connect(vertex1, vertex2, curr_graph.getEdge(vertex1, vertex2).getWeight());
            }
        }
        curr_component.init(temp_graph);
        return curr_component;
    }

    private void SCC(DirectedWeightedGraph OrigGraph){
        ArrayList<Integer> curr_order;
        for (Iterator<NodeData> it = OrigGraph.nodeIter(); it.hasNext(); ) {
            it.next().setVisited(false); // reset all the visited params for the graph
        }

        for (Iterator<NodeData> it = OrigGraph.nodeIter(); it.hasNext(); ) {
            NodeData curr_node = it.next();
            if (!curr_node.isVisited()){ // we haven't visited in this node yet
                curr_order = DFS(OrigGraph, curr_node); // get the DFS tree for each component in the graph
                this.Components.add(createSubGraph(OrigGraph, curr_order)); // build a graph for each component.
            }
        }
    }

    public int getNumberOfColors() {
        int max_color = 1;
        Iterator node_iter = this.graph.nodeIter();
        while (node_iter.hasNext()) {
            Node node = (Node) node_iter.next();
            if (node.getColor() > max_color) {
                max_color = node.getColor();
            }
        }
        return max_color;
    }

}
