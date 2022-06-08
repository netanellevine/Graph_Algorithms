package api;

import java.util.*;

public class Node implements NodeData {
    // edgeData or edges we will decide
    private final HashMap<Integer, Edge> Edges = new HashMap<>();
    private final HashSet<Integer> inEdges = new HashSet<>();
    private int id;
    private GeoLocation location;
    private double weight;
    private int degree = 0;
    //    private NodeData father = null;
    private String info;
    private boolean is_colored;
    private  int color;
    private HashSet<Integer> neighbours = new HashSet<>();
    private boolean visited = false;
    private int tag;

//    private boolean black = false;

    /**
     * Building a new node by location and ID
     * @param x is the x point of the location
     * @param y is the y axis point of the location
     * @param id the id of the node
     */
    public Node(double x, double y, int id) {
        this.id = id;
        this.location = new Location(x, y, 0);
        this.neighbours = new HashSet<>();
        is_colored = false;
        neighbours = new HashSet<>();
    }

    /**
     * Copy constructor of a node from NodeData
     * @param n
     */
    public Node(NodeData n) {
        this.id = n.getKey();
        neighbours = new HashSet<>();
        this.location = new Location(n.getLocation().x(), n.getLocation().y(), n.getLocation().z());
    }

    /**
     * Empty builder
     */
    public Node(){
        this.id = -1;
        neighbours = new HashSet<>();
    }

    /**
     * Deep copy of all the Nodedata attributes.
     * @param n
     * @param iter
     */
    public Node(NodeData n, Iterator<EdgeData> iter) {
        this.id = n.getKey();
        this.weight = n.getWeight();
        this.location = new Location(n.getLocation().x(), n.getLocation().y(), n.getLocation().z());
        this.info = n.getInfo();
        while (iter.hasNext()) {
            EdgeData e = iter.next();
//            String key = e.getSrc() +"-"+ e.getDest();
            this.Edges.put(e.getDest(), new Edge(e));
        }

    }

    /**
     * Add edge that comes from another node to this node
     * @param key
     */
    public void addInEdge(int key){
        inEdges.add(key);
        this.neighbours.add(key);
//        System.out.println(this.neighbours.size());
        this.degree +=1;
    }
    public HashSet<Integer> inEdges(){

        return inEdges;
    }

    /**
     * Deep copy of a node
     * @return
     */
    public Node copy() {
        Node c = new Node(location.x(), location.y(), id);
        c.weight = weight;
        c.info = info;
        HashMap<Integer, Edge> y = c.getEdges();
        Set<Integer> x = Edges.keySet();
        for (Integer i : x) {
            y.put(i, Edges.get(i));
        }
        return c;
    }

    /**
     * Returns the ID of the node, unique id
     * @return
     */
    @Override
    public int getKey() {
        if (this.id != -1) {
            return this.id;
        } else {
            throw new NullPointerException();
        }
    }

    /**
     * Sets the id of a node.
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }
//    public NodeData getFather() {
//        return father;
//    }

//    public int getId() {
//        return id;
//    }

//    public boolean isBlack() {
//        return black;
//    }

//    public void setBlack() {
//        this.black = "BLACK";
//    }

//    public void setFather(NodeData father) {
//        this.father = father;
//    }

    /**
     * Returns the location of a node.
     * @return
     */
    @Override
    public GeoLocation getLocation() {
        return location;
    }

    /**
     * Returns the weight of a node, unused for now.
     * @return
     */
    @Override
    public double getWeight() {
        return weight;
    }

    /**
     * Sets the weight of a node, unused for now.
     * @param w - the new weight
     */
    @Override
    public void setWeight(double w) {
        weight = w;
    }

    /**
     * Returns the metadata of a node.
     * @return
     */
    @Override
    public String getInfo() {
        return info;
    }

    /**
     * Sets the metadata of a node.
     * @param s
     */
    @Override
    public void setInfo(String s) {
        info = s;
    }

    /**
     * returns the tag of the node
     * @return
     */

    public boolean get_is_colored() {
        return this.is_colored;
    }
    public void set_is_colored(boolean flag) {
        this.is_colored = flag;
    }

    public int getColor(){
        return this.color;
    }
    public void setColor(int c){
        this.color = c;
    }
    /**
     * Change the tag value of the node.
     * @param t - the new value of the tag
     */


    /**
     * This method adds a new Edge @e to the HashMap of the current Edges of this Node.
     *
     * @param e - new Edge to add to this Node.
     */
    public void addEdge(Edge e) {
        if(e.getSrc()==this.getKey()) {
            if (!this.Edges.containsKey(e.getDest())) {
                this.Edges.put(e.getDest(), e);
                this.neighbours.add(e.getDest());
//                System.out.println(this.neighbours.size());
            }
        }
    }


    /**
     * remove edge that goes out of a node.
     * @param key
     * @return
     */
    public EdgeData removeEdge(int key) {
        return this.Edges.remove(key);

    }

    /**
     * Change the location of the node.
     * @param p - new new location  (position) of this node.
     */
    @Override
    public void setLocation(GeoLocation p) {
        location = p;
    }

    /**
     * Returns the edges of the node as a hashmap.
     * @return
     */
    public HashMap<Integer, Edge> getEdges() {
//        return this.Edges.keySet().toArray(new Double[0]);
        return this.Edges;
    }
    @Override
    public boolean isVisited() {
        return visited;
    }
    @Override
    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public int getDegree()
    {
        return neighbours.size();
    }
    public void setDegree(int deg){
        this.degree=deg;
    }

    public HashSet<Integer> getNeighbours() {
        return this.neighbours;
    }

    //    @Override
//    public String toString() {
//        return "\n<Index = " + id + ",\nlocation =" + location + ",\nEdges =" + Edges.values() + ">\n";
//    }
    @Override
    public String toString() {
        return "{\n"+'"'+"pos"+'"'+": " +'"'+location.x()+","+location.y()+'"'+",\n"+'"'+"id"+'"'+": "+id+"\n}";
    }

    public void addNeighbour(NodeData node)
    {
        if (node != null)
        {
            this.neighbours.add(node.getKey());
        }
    }

    public void removeNeighbour(NodeData node)
    {
        if (node != null)
        {
            this.neighbours.remove(node.getKey());
        }
    }
    public int getTag(){
        return  this.tag;
    }
    public void setTag(int tag){
        this.tag = tag;
    }

}


