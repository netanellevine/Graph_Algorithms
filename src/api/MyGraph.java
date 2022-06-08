package api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class MyGraph implements DirectedWeightedGraph {
    private HashMap<Integer, NodeData> _nodeHash;                   // hash map for nodes - key int, node is the val
    private HashMap<Integer,HashMap<Integer, EdgeData>> _edgeHash;   // hash map for edges - key(1) src, key(2) dest, val Edges
    private HashSet<EdgeData> _edgeSet;                             // Hash set for edges, more comfortable
    private final HashMap<Integer, EdgeData> emptyEdgeMap = new HashMap<>();    // empty hash for iter null return
    private int _changes;                                           // num of changes in graph


    public MyGraph()
    {
        this._nodeHash = new HashMap<>();
        this._edgeHash = new HashMap<>();
        this._edgeSet = new HashSet<>();
        _changes = 0;
    }

    public MyGraph(ParseToGraph pd)
    {
        this();
        HashMap<Integer, Node> nodes = pd.getNodes();
        for (int n : nodes.keySet())
        {
            addNode(nodes.get(n));
        }
        HashMap<String, Edge> edges = pd.getEdges();
        for (String s : edges.keySet())
        {
            Edge ed = edges.get(s);
            connect(ed.getSrc(), ed.getDest(), ed.getWeight());
        }
    }


    /**
     * copy c'tor deep copy
     * @param other
     */
    public MyGraph(DirectedWeightedGraph other)
    {
        this();
        Iterator<NodeData> itNode = other.nodeIter();
        while (itNode.hasNext())
        {
            NodeData node = itNode.next();
            this.addNode(new Node(node));
        }

        Iterator<EdgeData> itEdge = other.edgeIter();
        while (itEdge.hasNext())
        {
            EdgeData edge = itEdge.next();
            this.connect(edge.getSrc(), edge.getDest(), edge.getWeight());
        }
        _changes = other.getMC();
    }

    public MyGraph reversed()
    {
        MyGraph graph = new MyGraph();
        Iterator<NodeData> itNode = this.nodeIter();
        while (itNode.hasNext())
        {
            NodeData node = itNode.next();
            graph.addNode(new Node(node));
        }

        Iterator<EdgeData> itEdge = this.edgeIter();
        while (itEdge.hasNext())
        {
            EdgeData edge = itEdge.next();
            graph.connect(edge.getDest(), edge.getSrc(), edge.getWeight());
        }
        graph._changes = this.getMC();
        return graph;
    }

    @Override
    public NodeData getNode(int key)
    {
        return _nodeHash.get(key);      // gets node in O(1)
    }

    @Override
    public NodeData getSomeNode()
    {
        if (nodeSize() == 0)
            return null;
        Iterator<NodeData> nodes = nodeIter();
        if (!nodes.hasNext())
            return null;
        return nodes.next();
    }

    @Override
    public EdgeData getEdge(int src, int dest)
    {
        if (!_edgeHash.containsKey(src) || !_edgeHash.get(src).containsKey(dest))
            return null;

        return _edgeHash.get(src).get(dest);      // gets edge in O(1)
    }

    @Override
    public void addNode(NodeData n)
    {
        _nodeHash.put(n.getKey(), n);           // add node by NodeData and put it in hash map
        _changes++;
    }

    public void addNode(int n)                  // add node by int
    {
        _nodeHash.put(n, new Node(0, 0, n));
        _changes++;
    }

    /**
     * connect the nodes - creating a new edge
     * @param src - the source of the edge.
     * @param dest - the destination of the edge.
     * @param w - positive weight representing the cost (aka time, price, etc) between src-->dest.
     */
    @Override
    public void connect(int src, int dest, double w)
    {
        if (getNode(src) == null || getNode(dest) == null)
            return;

        EdgeData edge1 = new Edge(src, dest, w);
//        EdgeData edge2 = new Edge(dest, src, w);
        if(!_edgeHash.containsKey(src))
        {
            _edgeHash.put(src, new HashMap<>());
            // if key doesn't exist then put the key
        }
//        if(!_edgeHash.containsKey(dest))
//        {
//
//            _edgeHash.put(dest, new HashMap<>());  // if key doesn't exist then put the key
//        }
        _edgeHash.get(src).put(dest, edge1);
        _edgeSet.add(edge1);
//        _edgeHash.get(dest).put(src, edge2);
//        _edgeSet.add(edge2);

        // add the src
        getNode(src).addNeighbour(getNode(dest));
        getNode(dest).addNeighbour(getNode(src));

        _changes++;     // changes has been made
    }

    @Override
    public void connect(EdgeData edge) {
        connect(edge.getSrc(), edge.getDest(), edge.getWeight());
    }

    @Override
    public Iterator<NodeData> nodeIter()
    {
        return _nodeHash.values().iterator();       // return iterator of the nodes
    }

    @Override
    public Iterator<EdgeData> edgeIter()
    {
        return _edgeSet.iterator();
    }

    @Override
    public Iterator<EdgeData> edgeIter(int node_id)
    {
        if (_edgeHash.containsKey(node_id))
        {
            return _edgeHash.get(node_id).values().iterator();
        }

        return (emptyEdgeMap.values().iterator());      // return an empty iterator
    }

    @Override
    public NodeData removeNode(int key)
    {
        if (!_nodeHash.containsKey(key))
            return null;

        // get the edges
        Iterator<EdgeData> edgeIter = edgeIter(key);
        ArrayList<EdgeData> edges = new ArrayList<>();
        while (edgeIter.hasNext())
            edges.add(edgeIter.next());

        // remove
        for (EdgeData edge : edges)
        {
            removeEdge(edge.getSrc(), edge.getDest());
        }

        _edgeHash.remove(key);
        // before removing the node we must first to remove all of the edges that connected to the node
        for (int node : _nodeHash.keySet())
        {
            removeEdge(node, key);
        }
        _changes++;
        return _nodeHash.remove(key);
    }

    @Override
    public EdgeData removeEdge(int src, int dest)
    {
        //if doesn't exist return null
        if (!(_edgeHash.containsKey(src)) || !(_edgeHash.get(src).containsKey(dest)))
            return null;

        EdgeData edge = _edgeHash.get(src).remove(dest);

        // if nothig lent remove the src from keys
        if (_edgeHash.get(src).isEmpty())
        {
            _edgeHash.remove(src);
        }
        _edgeSet.remove(edge);
        if (!_edgeHash.containsKey(dest) || !_edgeHash.get(dest).containsKey(src))
        {
            getNode(src).removeNeighbour(getNode(dest));
            getNode(dest).removeNeighbour(getNode(src));
        }
        _changes++;     //change has been made
        return edge;
    }
    public  HashSet<Integer> Neighbours(NodeData n){
        HashSet<Integer> nei = n.getNeighbours();
        HashSet<Integer> ans = new HashSet<Integer>();

        for (Integer it: nei) {
            EdgeData e = this.getEdge(n.getKey(), it);
            if (e != null) {
                ans.add(it);
            }
        }
        return ans;
    }

    public void resetColors()
    {
        for (int nodeId: _nodeHash.keySet())
        {
            NodeData node = getNode(nodeId);
            node.set_is_colored(false);
            node.setColor(0);
        }
    }

    @Override
    public int nodeSize()
    {
        return _nodeHash.size();
    }

    @Override
    public int edgeSize()
    {
        return _edgeSet.size();
    }

    @Override
    public int getMC()
    {
        return _changes;
    }

    public void mergeGraphs(DirectedWeightedGraph graph)
    {
        Iterator<NodeData> nodeItr = graph.nodeIter();
        while (nodeItr.hasNext())
        {
            NodeData node = nodeItr.next();
            if (!_nodeHash.containsKey(node.getKey()))
                addNode(new Node(node));
        }
        Iterator<EdgeData> edgeItr = graph.edgeIter();
        while (edgeItr.hasNext())
        {
            EdgeData edge = edgeItr.next();

            connect(edge.getSrc(), edge.getDest(), edge.getWeight());
            connect(edge.getDest(), edge.getSrc(), edge.getWeight());
        }
    }

}
