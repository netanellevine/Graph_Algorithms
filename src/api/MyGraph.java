package api;

import java.util.HashMap;
import java.util.Iterator;
import java.util.function.Consumer;

public class MyGraph implements DirectedWeightedGraph {
    private HashMap<Integer, Node> nodes;
    private int size = 0;
    private int MC;

    /**
     * Empty builder for my graph
     */
    public MyGraph() {
        this.nodes = new HashMap<>();
        this.MC = 0;
    }


    /**
     * Builder for my graph with nodes and edge size
     *
     * @param Nodes
     * @param size
     */
    public MyGraph(HashMap<Integer, Node> Nodes, int size) {
        this.nodes = Nodes;
        this.size = size;
        this.MC = 0;
    }


    /**
     * Deep copy of directedweightedgraph
     *
     * @param g
     */
    public MyGraph(DirectedWeightedGraph g) {
        this.nodes = new HashMap<>();
        if (g != null) {
            Iterator<NodeData> nodeIter = g.nodeIter();

            while (nodeIter.hasNext()) {
                NodeData curr = nodeIter.next();
                Node n = new Node(curr);
                this.nodes.put(curr.getKey(), n);
            }
            Iterator<EdgeData> edgeIter = g.edgeIter();
            int count = 0;
            while (edgeIter.hasNext()) {
                EdgeData e = edgeIter.next();
                Node n = this.nodes.get(e.getDest());
                n.addInEdge(e.getSrc());
                Node n1 = this.nodes.get(e.getSrc());
                Edge e1 = new Edge(e);
                n1.addEdge(e1);
                count++;
            }
            this.size = count;
            MC = g.getMC();
        }
    }

    /**
     * if the node exists,it will return the node with a matching value of a key
     *
     * @param key - the node_id
     * @return
     */
    @Override
    public NodeData getNode(int key) {
        if (this.nodes.containsKey(key)) {
            return this.nodes.get(key);
        }
        return null;
    }


    /**
     * if the edge exists, it will return the edge with the matching attributes.
     *
     * @param src
     * @param dest
     * @return
     */
    @Override
    public EdgeData getEdge(int src, int dest) {
        if (this.nodes.get(src).getEdges().containsKey(dest)) {
            return this.nodes.get(src).getEdges().get(dest);
        }
        return null;
    }

    /**
     * Creates a new edge and add it to the graph, if the edge already exist it will do nothing
     *
     * @param src  - the source of the edge.
     * @param dest - the destination of the edge.
     * @param w    - positive weight representing the cost (aka time, price, etc) between src-->dest.
     */
    @Override
    public void connect(int src, int dest, double w) {
        // check if needed ()
        if (this.nodes.containsKey(src) && this.nodes.containsKey(dest)) {
            if (getEdge(src, dest) == null && getEdge(dest,src) == null ) {
                this.nodes.get(src).getEdges().put(dest, new Edge(src, dest, w));
                this.nodes.get(dest).getEdges().put(src, new Edge(dest, src, w));
                this.nodes.get(dest).setDegree(this.nodes.get(dest).getDegree()+1);
                this.nodes.get(src).setDegree(this.nodes.get(src).getDegree()+1);
                this.MC++;
                this.size++;
            }
        }
    }

    /**
     * Returns an iterator that can traverse all the nodes.
     *
     * @return
     */
    @Override
    public Iterator<NodeData> nodeIter() {

        return new Iterator<>() {
            private Iterator<Node> it = nodes.values().iterator();
            private int m = MC;
            private Node last;


            @Override
            public boolean hasNext() {
                if (m != MC)
                    throw new RuntimeException("MC counter was changed");
                return it.hasNext();
            }

            @Override
            public NodeData next() {
                if (m != MC)
                    throw new RuntimeException("MC counter was changed");
                last = it.next();
                return last;
            }

            @Override
            public void remove() {
                if (m != MC)
                    throw new RuntimeException("MC counter was changed");
                it.remove();
                removeRelatedEdges(last);
                MC++;
                m = MC;
            }
        };
    }

    /**
     * Returns an iterator that can traverse on all the edges
     *
     * @return
     */
    @Override
    public Iterator<EdgeData> edgeIter() {
        return new Iterator<>() {
            Iterator<Node> iter = nodes.values().iterator();

            Iterator<Edge> ed;
            private int m = MC;
            private Edge last;

            @Override
            public boolean hasNext() {
                if (iter.hasNext() && ed == null) {
                    ed = iter.next().getEdges().values().iterator();
                }
                if (ed == null)
                    return false;
                if (m != MC)
                    throw new RuntimeException("MC counter was changed");
                if (ed.hasNext()) {
                    return ed.hasNext();
                }

                while (iter.hasNext() && !ed.hasNext()) {
                    ed = iter.next().getEdges().values().iterator();
                }
                return ed.hasNext();
            }

            @Override
            public EdgeData next() {
                if (m != MC)
                    throw new RuntimeException("MC counter was changed");
                last = ed.next();
                return last;
            }

            @Override
            public void remove() {
                if (m != MC)
                    throw new RuntimeException("MC counter was changed");
                nodes.get(last.getDest()).inEdges().remove(last.getSrc());
                ed.remove();
                size--;
                MC++;
                m = MC;
            }

            @Override
            public void forEachRemaining(Consumer<? super EdgeData> action) {
                Iterator.super.forEachRemaining(action);
            }
        };

    }

    /**
     * Returns an iterator that can traverse on all the edges of a given node
     *
     * @return
     */
    @Override
    public Iterator<EdgeData> edgeIter(int node_id) {
        Node x = nodes.get(node_id);
        HashMap<Integer, Edge> E = x.getEdges();
        return new Iterator<EdgeData>() {

            private Iterator<Edge> it = E.values().iterator();
            private int m = MC;
            Edge last;


            @Override
            public boolean hasNext() {
                if (m != MC)
                    throw new RuntimeException("MC counter was changed");
                return it.hasNext();
            }

            @Override
            public EdgeData next() {
                if (m != MC)
                    throw new RuntimeException("MC counter was changed");
                last = it.next();
                return last;
            }

            @Override
            public void remove() {
                if (m != MC)
                    throw new RuntimeException("MC counter was changed");
                it.remove();
                nodes.get(last.getDest()).inEdges().remove(last.getSrc());
                size--;
                MC++;
                m = MC;
            }
        };
    }


    /**
     * Remove a node if it exists in the graph, it will remove all the edges that come from it and to it.
     *
     * @param key
     * @return
     */
    @Override
    public NodeData removeNode(int key) {
        if (!this.nodes.containsKey(key))
            throw new NullPointerException("Node does not exist");
        this.MC++;
        removeRelatedEdges(this.nodes.get(key));
        return this.nodes.remove(key);
    }

    /**
     * Remove all the related edges of a node from the graph.
     *
     * @param n
     */
    private void removeRelatedEdges(Node n) {
        for (int i : n.inEdges()) {
            Node n1 = this.nodes.get(i);
            if (n1 != null) {
                n1.getEdges().remove(n.getKey());
            }
        }
    }

    /**
     * Remove an existing edge from the graph.
     *
     * @param src
     * @param dest
     * @return
     */
    @Override
    public EdgeData removeEdge(int src, int dest) {
        if (!this.nodes.get(src).getEdges().containsKey(dest))
            throw new NullPointerException("The Edge does not exist in this Graph");
//        String key = src +"-"+ dest;
        this.nodes.get(dest).inEdges().remove(src);
        this.size--;
        this.MC++;
        return this.nodes.get(src).removeEdge(dest);
    }

    /**
     * Returns the amount of nodes in the graph
     *
     * @return
     */
    @Override
    public int nodeSize() {
        return this.nodes.size();
    }

    /**
     * Returns the amount of edges in the graph.
     *
     * @return
     */
    @Override
    public int edgeSize() {
        return this.size;
    }

    /**
     * Returns the modification counter of the graph(how many changes were done since it was created).
     *
     * @return
     */
    @Override
    public int getMC() {
        return this.MC;
    }

    /**
     * Adds a node to the graph.
     *
     * @param n
     */
    @Override
    public void addNode(NodeData n) {
        Node nCopy = new Node(n, new Iterator<EdgeData>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public EdgeData next() {
                return null;
            }
        });
        this.MC++;
        this.nodes.put(n.getKey(), nCopy);
    }

    /**
     * Returns all the nodes in the graph
     *
     * @return
     */
    public HashMap<Integer, Node> getNodes() {
        return this.nodes;
    }


    /**
     * Print the edges
     *
     * @return
     */
    public String toStringEdges() {
        String output = '"' + "Edges" + '"' + ": [\n";
        Iterator<EdgeData> iter = this.edgeIter();
        while (iter.hasNext()) {
            EdgeData e = iter.next();
            output += "" + "{\n" + '"' + "src" + '"' + ": " + e.getSrc() + ",\n" + '"' + "w" + '"' + ": " + e.getWeight() + ",\n" + '"' + "dest" + '"' + ": " + e.getDest() + "\n}" + ",\n";
        }
        return output.substring(0, output.length() - 2) + "\n]";
    }

    /**
     * Print the nodes.
     *
     * @return
     */
    public String toStringNodes() {
        String output = '"' + "Nodes" + '"' + ": [\n";
        Iterator<NodeData> iter = this.nodeIter();
        while (iter.hasNext()) {
            NodeData n = iter.next();
            output += "" + "{\n" + '"' + "pos" + '"' + ": " + '"' + n.getLocation().x() + "," + n.getLocation().y() + '"' + ",\n" + '"' + "id" + '"' + ": " + n.getKey() + "\n}" + ",\n";
        }
        return output.substring(0, output.length() - 2) + "\n]";
    }

    /**
     * Prints the graph in a json to string format.
     *
     * @return
     */
    @Override
    public String toString() {
        return "{\n" + toStringEdges() + ",\n\n" + toStringNodes() + "\n}";
    }


}
