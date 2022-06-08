package api;

import java.util.*;

public class SuperNode {
    private Stack<Integer> reverse_order;
    private HashMap<Integer, HashSet<EdgeData>> super_nodes; // for every represent key keep all the edges that connect
    HashMap<Integer, HashMap<Integer, NodeData>> preserved_nodes; // keep the nodes that compressed
    HashMap<Integer, HashSet<EdgeData>> tree_edges; // keep originaly tree edges
    DirectedWeightedGraph g; // the graph that operate the method on him
    DirectedWeightedGraph tree; // to de/compress with g

    public SuperNode(DirectedWeightedGraph h, DirectedWeightedGraph tree) { // constructor
        this.g = h;
        this.tree = tree;
        this.super_nodes = new HashMap<Integer, HashSet<EdgeData>>();
        this.preserved_nodes = new HashMap<Integer, HashMap<Integer, NodeData>>();
        this.tree_edges = new HashMap<>();
        this.reverse_order = new Stack<>();
    }

    /**
     * @param to_compress- list of integer - key nodes the goal of the function is
     *                     to compressed all the nodes to the single node who call
     *                     "contract node" the function take care to keep all the
     *                     details that contract
     **/
    void compress(List<Integer> to_compress) {
        // copy all the edges to the map
        int id_list = to_compress.get(0); // to represent node - stay after the contract
        HashSet<EdgeData> compressed_edges = new HashSet<EdgeData>(); // all the edges that remove
        this.tree_edges.put(id_list, new HashSet<>());
        this.reverse_order.push(id_list);
        this.preserved_nodes.put(id_list, new HashMap<>());
        // move on all the edges and insert them
        for (int node : to_compress) {
                for(int nei_key : this.tree.Neighbours(this.g.getNode(node))){
                    EdgeData e1 = this.tree.getEdge(node, nei_key);
                    EdgeData e2 = this.tree.getEdge(nei_key,node);
                    tree_edges.get(id_list).add(e1);
                    tree_edges.get(id_list).add(e2);
                    compressed_edges.add(e1); // add the edge to the list
                    compressed_edges.add(e2);

                    if (!to_compress.contains(nei_key)) { // check if it not "intrinsic" edge
                        this.g.connect(id_list, nei_key, 1); // connect the edge to represent node
                        this.g.connect(nei_key,id_list, 1);
                        EdgeData added_edge = this.g.getEdge(id_list, nei_key);
                        EdgeData added_edge2 = this.g.getEdge(nei_key ,id_list);
                        added_edge.setIsInMtch(e1.isInMatch());
                        added_edge2.setIsInMtch(e2.isInMatch());
                        this.tree.connect(e1.getSrc(), e1.getDest(), 1); // connect the edge to the tree
                        this.tree.connect(e2.getSrc(), e2.getDest(), 1);
                    }
            }

            if (node != id_list) { // check if it's not the first node
                this.preserved_nodes.get(id_list).put(node, this.g.removeNode(node));
                this.tree.removeNode(node);
            } else { // if it is to represent node you do not remove him from the map
                this.preserved_nodes.get(id_list).put(id_list, this.g.getNode(id_list));
            }
        }
        this.super_nodes.put(id_list, compressed_edges); // put the edges with the represent key

    }

    /**
     *
     * @param decompress - get an the represent integer the function restore all the
     *                   data that compressed in the super node
     */
    int decompress(int decompress) {
        if (!this.super_nodes.containsKey(decompress)) { // check if it's super node
            return -1;
        }
        this.g.removeNode(decompress); // remove the super node
        this.tree.removeNode(decompress);

        for (NodeData n : this.preserved_nodes.get(decompress).values()) {
            this.g.addNode(n);
            this.tree.addNode(n);
        }

        // add all the edges and nodes to the graph
        for (EdgeData edge : this.super_nodes.get(decompress)) {

            // connect the reserved edge
            this.g.connect(edge.getSrc(), edge.getDest(), 1);
            this.g.connect(edge.getDest(), edge.getSrc(), 1);

            this.tree.connect(edge.getSrc(), edge.getDest(), 1);
            this.tree.connect(edge.getDest(), edge.getSrc(), 1);


        }
        this.preserved_nodes.remove(decompress);
        this.super_nodes.remove(decompress);
        return decompress;
    }

    public void decompressAll() {
        for (var key : reverse_order) {
            if (this.super_nodes.containsKey(key)) {

                decompress(key);
            }
        }
    }


}