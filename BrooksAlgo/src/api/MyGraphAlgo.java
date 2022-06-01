package api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class MyGraphAlgo implements DirectedWeightedGraphAlgorithms {
    private DirectedWeightedGraph graph;
    private int connected = -1;//-1 unknown, 0 no, 1 yes
    private int mc;
    private int keyToStart;

    /**
     * Choose the graph you will perform your algorithms on.
     */
    @Override
    public void init(DirectedWeightedGraph g) {
        if (g != null) {
            this.graph = g;
            this.mc = this.graph.getMC();
        }

    }

    /**
     * Returns the graph you are currently performing algorithms on.
     *
     * @return
     */
    @Override
    public DirectedWeightedGraph getGraph() {
        return this.graph;
    }

    /**
     * Returns a deep copy of the current graph you are working on.
     *
     * @return
     */
    @Override
    public DirectedWeightedGraph copy() {
        return new MyGraph(this.graph);
    }

    @Override
    /** Checks if there is a two-sided route between every pair of vertexes.
     * source https://www.techiedelight.com/check-given-graph-strongly-connected-not/
     * @return true if so false otherwise.
     */
    public boolean isConnected() {
        if (this.mc == this.graph.getMC() && this.connected != -1) {
            if (this.connected == 1)
                return true;
            else
                return false;
        }
        this.mc = this.graph.getMC();
        boolean isConnected = isStronglyConnected();
        if (isConnected)
            this.connected = 1;
        else
            this.connected = 0;
        return isStronglyConnected();
    }

    /**
     * This is a simple DFS algorithm on the graph
     * The DFS will change every node it visits to true.
     *
     * @param visited hashmap of the visitor vertexes
     * @param curr_graph     on which graph to perform it
     */
    private void DFS(Queue<Integer> keys, HashSet<Integer> visited, DirectedWeightedGraph curr_graph) {
        // mark current node as visited
        while (!keys.isEmpty() && visited.size() != curr_graph.nodeSize()) {
            visit(keys, keys.poll(), visited, curr_graph);
        }
    }

    private void visit(Queue<Integer> keys, int key, HashSet<Integer> visited, DirectedWeightedGraph curr_graph) {
        visited.add(key);
        Iterator<EdgeData> iter = curr_graph.edgeIter(key);
        // do for every edge (v, u)
        while (iter.hasNext()) {
            int dest = iter.next().getDest();
            if (!visited.contains(dest)) {
                keys.add(dest);
            }
        }
    }

    /**
     * This function switch the source and destination of all the edges and returns a new graph.
     *
     * @return
     */
    private DirectedWeightedGraph reverse()
    {
        return graph.reversed();
    }

    /**
     * Checks if there is a two sided route between every pair of vertexes.
     *
     * @return
     */
    private boolean isStronglyConnected() {
        HashSet<Integer> visited = new HashSet<>();
        Queue<Integer> keys = new LinkedList<Integer>();
        int key = -1;
        Iterator<NodeData> iter = graph.nodeIter();
        if (iter.hasNext()) {
            key = iter.next().getKey();
        }
        if (key == -1)
            return false;
        keys.add(key);
        // run a DFS starting at `v`
        this.DFS(keys, visited, graph);

        // If DFS traversal doesn't visit all vertices,
        // then the graph is not strongly connected
        if (visited.size() != graph.nodeSize())
            return false;
        visited = new HashSet<>();

        // Reverse the direction of all edges in the directed graph
        DirectedWeightedGraph g = this.reverse();
        // create a graph from reversed edges

        // Again run a DFS starting at `v`
        keys = new LinkedList<Integer>();
        keys.add(key);
        DFS(keys, visited, g);

        // If DFS traversal doesn't visit all vertices,
        // then the graph is not strongly connected
        if (visited.size() != this.graph.nodeSize())
            return false;
        // if a graph "passes" both DFSs, it is strongly connected
        return true;
    }

    /**
     * This function will return the shortest path distance (sum of all the edges in the route) between 2 edges.
     * This algorithm will be utilizing djikstra algorithm for finding the shortest route between two nodes.
     * Here is a video to illustrate https://www.youtube.com/watch?v=CerlT7tTZfY
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        return djikstra(src, dest);
    }

    /**
     * This function will return the shortest path list (list of all the nodes in the route) between 2 edges.
     * This algorithm will be utilizing djikstra algorithm for finding the shortest route between two nodes.
     * Here is a video to illustrate https://www.youtube.com/watch?v=CerlT7tTZfY
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public List<NodeData> shortestPath(int src, int dest) {
        HashMap<Integer, father> s = djikstra_path(src, dest);
        if (s == null)
            return null;
        List<NodeData> path = new LinkedList<>();
        path.add(this.graph.getNode(dest));
        NodeData n = this.graph.getNode(src);
        int key = s.get(dest).prev;
        while (path.get(0) != n) {
            path.add(0, this.graph.getNode(key));
            key = s.get(key).prev;
        }
        return path;
    }

    /**
     * This algorithm will find the center of the graph,center of a graph is the node with the min(max(distance(u,v))).
     * Meaning the longest distance of a node is shorter than the longest distance of all the other nodes.
     * This algorithm will be utilized by performing djistra algorithm on all the nodes and will return the one with the shortest path.
     *
     * @return
     */
    @Override
    public NodeData center() {
        Iterator<NodeData> iter = this.graph.nodeIter();
        double max = Double.MAX_VALUE;
        int id = 0;
        while (iter.hasNext()) {
            int key = iter.next().getKey();
            double weight = djikstra(key);
            if (weight < max) {
                max = weight;
                id = key;
            }
        }
        return this.graph.getNode(id);

    }

    public class trio {
        int from;
        int to;
        double weight;

        public trio(int f, int t, double w) {
            from = f;
            to = t;
            weight = w;
        }

    }

    public class father {
        int prev;
        double weight;

        public father(int t, double w) {
            prev = t;
            weight = w;
        }

    }


    /**
     * Djikstra algorithm for finding the max cost path between 2 nodes using priority queue.
     * https://www.youtube.com/watch?v=CerlT7tTZfY
     *
     * @param src
     * @return
     */
    public double djikstra(int src) {
        HashSet<Integer> g = new HashSet<>();
        PriorityQueue<father> prio = new PriorityQueue<>((o1, o2) -> {
            if (o1.weight == o2.weight)
                return 0;
            else if (o1.weight > o2.weight)
                return 1;
            else
                return -1;
        });
        prio.add(new father(src, 0));
        double max = 0;
        while (g.size() < this.graph.nodeSize() && !prio.isEmpty()) {
            father f = prio.poll();
            int dest = f.prev;
            if (!g.contains(dest)) {
                Iterator<EdgeData> iter = this.graph.edgeIter(dest);
                g.add(dest);
                if (f.weight > max) {
                    max = f.weight;
                }
                while (iter.hasNext()) {
                    EdgeData e = iter.next();
                    double weight = f.weight + e.getWeight();
                    prio.add(new father(e.getDest(), weight));
                }
            }
        }
        return max;
    }

    /**
     * Djistra algorithm for finding the cost of the shortest path between two nodes.
     * Using priority queue
     * https://www.youtube.com/watch?v=CerlT7tTZfY
     *
     * @param src
     * @param des
     * @return
     */
    public double djikstra(int src, int des) {
        HashMap<Integer, father> s = new HashMap<>();
        PriorityQueue<trio> PQ = new PriorityQueue<>((o1, o2) -> {
            if (o1.weight == o2.weight)
                return 0;
            else if (o1.weight > o2.weight)
                return 1;
            else
                return -1;
        });
        PQ.add(new trio(src, src, 0));
        while (s.size() < graph.nodeSize() && !PQ.isEmpty()) {
            trio t = PQ.poll();
            int dest = t.to;
            if (!s.containsKey(dest)) {
                Iterator<EdgeData> iter = this.graph.edgeIter(dest);
                s.put(dest, new father(t.from, t.weight));
                if (s.containsKey(des)) {
                    return t.weight;
                }
                while (iter.hasNext()) {
                    EdgeData e = iter.next();
                    double weight = t.weight + e.getWeight();
                    PQ.add(new trio(e.getSrc(), e.getDest(), weight));
                }
            }
        }
        return -1;
    }

    /**
     * Djikstra's algorithm for finding the shortest path between two nodes.
     * https://www.youtube.com/watch?v=CerlT7tTZfY
     *
     * @param src
     * @param des
     * @return
     */
    public HashMap<Integer, father> djikstra_path(int src, int des) {
        HashMap<Integer, father> s = new HashMap<>();
        PriorityQueue<trio> PQ = new PriorityQueue<>((o1, o2) -> {
            if (o1.weight == o2.weight)
                return 0;
            else if (o1.weight > o2.weight)
                return 1;
            else
                return -1;
        });
        PQ.add(new trio(src, src, 0));
        while (s.size() < graph.nodeSize() && !PQ.isEmpty()) {
            trio t = PQ.poll();
            int dest = t.to;
            if (!s.containsKey(dest)) {
                Iterator<EdgeData> iter = this.graph.edgeIter(dest);
                s.put(dest, new father(t.from, t.weight));
                if (s.containsKey(des)) {
                    return s;
                }
                double weight = t.weight;
                while (iter.hasNext()) {
                    EdgeData e = iter.next();
                    weight += e.getWeight();
                    PQ.add(new trio(e.getSrc(), e.getDest(), weight));
                }
            }
        }
        return null;
    }

    /**
     * Greedy algorithm to find the shortest path between all the nodes in a given city.
     *
     * @param cities
     * @return
     */
    public List<NodeData> findRoute(List<NodeData> cities) {
        if (cities == null || cities.size() == 0)
            return null;
        if (cities.size() == 1) {
            return cities;
        }
        List<NodeData> TSPath = new ArrayList<>();
        HashSet<Integer> route = new HashSet<>();
        List<NodeData> copy = copy(cities);
        int last = cities.get(0).getKey();
        while (cities.size() > 1) {
            cities.remove(0);
            NodeData n1 = cities.get(0);
            if (!route.contains(n1.getKey())) {
                List<NodeData> part = shortestPath(last, n1.getKey());
                while (part!=null && part.size() > 0) {
                    NodeData g = part.remove(0);
                    if (TSPath.size() == 0 || TSPath.get(TSPath.size() - 1) != g)
                        TSPath.add(g);
                    route.add(g.getKey());
                }
                if(TSPath.size() -1 <0)
                    return null;
                last = TSPath.get(TSPath.size() - 1).getKey();
            }
        }
        for (NodeData nodeData : copy) {
            int key = nodeData.getKey();
            if (!route.contains(key))
                return new ArrayList<>();
        }
        return TSPath;
    }

    /**
     * In this TSP we will find the shortest path between all the nodes in a given city.
     * This will be done by using greedy algorithm(finding the best way from cities[0] to cities[1] and cities[1] to cities[2] and so on.
     *
     * @param cities
     * @return
     */
    @Override
    public List<NodeData> tsp(List<NodeData> cities) {
        if (cities == null) {
            return null;
        }
        boolean f=true;
        double min = Double.MAX_VALUE;
        List<NodeData> fin = new ArrayList<>();
        for (int i = 0; i < cities.size(); i++) {
            List<NodeData> copy = copy(cities);
            swap(0, i, copy);
            copy = findRoute(copy);
            if (copy !=null && copy.size() >= cities.size()) {
                double cost = 0;
                for (int j = 0; j < copy.size() - 1; j++) {
                    EdgeData e = this.graph.getEdge(copy.get(j).getKey(), copy.get(j + 1).getKey());
                    if(e==null){
                        f=false;
                        break;
                    }
                    else {
                        cost = cost + e.getWeight();
                    }
                }
                if (cost < min && f) {
                    min = cost;
                    fin = copy;
                }
                f=true;
            }
        }
        return fin;
    }

    public void swap(int i, int j, List<NodeData> l) {
        NodeData n = l.get(i);
        l.add(i, l.remove(j));
        l.add(j, n);
    }

    public List<NodeData> copy(List<NodeData> l) {
        List<NodeData> copy = new LinkedList<>();
        for (int i = 0; i < l.size(); i++) {
            copy.add(new Node(l.get(i)));
        }
        return copy;
    }

    /**
     * Save the current graph to a file
     * Used https://www.w3schools.com/java/java_files_create.asp
     *
     * @param file - the file name (may include a relative path).
     * @return
     */
    @Override
    public boolean save(String file) {
        try {
            File myObj = new File(file);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.err.println("File already exists.");
                return false;
            }
        } catch (IOException e) {
            System.err.println("An error occurred.");
            e.printStackTrace();
            return false;
        }
        try {
            FileWriter myWriter = new FileWriter(file);
            myWriter.write(this.graph.toString());
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
            return true;
        } catch (IOException e) {
            System.err.println("An error occurred.");
            e.printStackTrace();
            return false;
        }

    }


    /**
     * Load will load and initiate a graph from a given json.
     * Load will return true if the file has successfully loaded and false otherwise.
     *
     * @param file - file name of JSON file
     * @return
     */
    @Override
    public boolean load(String file) {
        boolean hasLoaded = false;
        try {
            ParseToGraph ptg = new ParseToGraph(file);
            graph = new MyGraph();
            this.init(new MyGraph(ptg));
            hasLoaded = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("\nJson file wasn't found!");
        }
        return hasLoaded;
    }

    public boolean is_clique(){
        if (graph.edgeSize() == (graph .nodeSize() * graph.nodeSize()-1) / 2){
            return true;
        }
        return false;

    }

    /**
     * find 3 keys x,y,z such that: xy and xz is in the graph and yz not
     * @return array of 3 keys of nodes
     */

    public int[] find_xyz(){
        int[] ans = new int[3];
        Iterator<NodeData> it_x = graph.nodeIter();
        Iterator<NodeData> it_y = graph.nodeIter();
        Iterator<NodeData> it_z = graph.nodeIter();
        while (it_x.hasNext()){
            while ((it_y.hasNext())){
                while (it_z.hasNext()){
                    int key_x = it_x.next().getKey();
                    int key_y = it_y.next().getKey();
                    int key_z = it_z.next().getKey();
                    if(key_x != key_y && key_x !=key_z && key_y != key_z){
                        if(graph.getEdge(key_x,key_y)!=null &&
                                graph.getEdge(key_x,key_z)!=null && graph.getEdge(key_y,key_z)==null){
                                ans[0] = key_x;
                                ans[1] = key_y;
                                ans[2] = key_z;
                        }

                    }

                }
            }
        }
        return ans;
    }

    @Override
    public boolean is_Kregular() { //if its return -1 so its a is_Kregular graph
        int max_deg = getMaxDegree();
        Iterator<NodeData> iter = graph.nodeIter();
        while (iter.hasNext()) {
            int tmpDegree = iter.next().getDegree();
            if ( tmpDegree < max_deg) {

                return false;
            }
        }
        return true;
    }
    public int KeyToStart(){
        return  this.keyToStart;
    }

    public int getMaxDegree(){
        int max_degree = 0;
        Iterator<NodeData> it = this.graph.nodeIter();
        while (it.hasNext()){
            int deg = it.next().getDegree();
            if (deg > max_degree) {
                max_degree = deg;
            }
        }
        return max_degree;
        }

    @Override
    public boolean is_Odd_cycle() {
        // if we have an odd number of vertices or we have more than 1 edge to each node
        // than it's node an odd cycle
        if (this.graph.nodeSize() % 2 == 0 || this.graph.nodeSize() != this.graph.edgeSize()) {
            return false;
        }
        else {
            Iterator node_iter = this.graph.nodeIter();
            while (node_iter.hasNext()) {
                Node node = new Node((NodeData) node_iter.next());
                // if the node degree is not 2, meaning the graph is not a cycle at all
                if (node.getDegree() != 2) {
                    return false;
                }
            }
            return true;
        }
    }

    @Override
    public boolean is_path() {
        int num1deg = 0;
        Iterator<NodeData> iter = this.graph.nodeIter();
        while(iter.hasNext()){
            if(iter.next().getDegree() == 1){
                num1deg++;
            }
        }
        return num1deg == 2;
    }
    public boolean isOneConnected()
    {
        Iterator<NodeData> nodeItr =  this.graph.nodeIter();
        DirectedWeightedGraph original = this.copy();
        boolean retAns = false;
        while (nodeItr.hasNext())
        {
            NodeData node = nodeItr.next();
            MyGraphAlgo algTemp = new MyGraphAlgo();
            DirectedWeightedGraph copyGraph = new MyGraph(original);
            copyGraph.removeNode(node.getKey());
            algTemp.init(copyGraph);

            if (!algTemp.isConnected())
            {
                retAns = true;
                break;
            }
        }
        return retAns;
    }
    public NodeData isOneConnectedNode()
    {
        Iterator<NodeData> nodeItr =  this.graph.nodeIter();
        DirectedWeightedGraph original = this.copy();
        NodeData retAns = null;
        while (nodeItr.hasNext())
        {
            NodeData node = nodeItr.next();
            MyGraphAlgo algTemp = new MyGraphAlgo();
            DirectedWeightedGraph copyGraph = new MyGraph(original);
            copyGraph.removeNode(node.getKey());
            algTemp.init(copyGraph);

            if (!algTemp.isConnected())
            {
                retAns = node;
                break;
            }
        }
        return retAns;
    }

    public ArrayList<DirectedWeightedGraph> getGraphsFromBreaker(NodeData nodeMain)
    {
        DirectedWeightedGraph original = this.copy();

        if (nodeMain != null)
            original.removeNode(nodeMain.getKey());

        ArrayList<DirectedWeightedGraph> retGrapsh = new ArrayList<>();
        MyGraphAlgo algoForGraph = new MyGraphAlgo();
        algoForGraph.init(original);

        while(original.nodeSize() > 0)
        {
            // copy the graph
            DirectedWeightedGraph tempGraph = new MyGraph(original);

            // get the nodes and the first node that appears
            ArrayList<Integer> arrNodes = nodesListInt(tempGraph);
            int nodeTemp = arrNodes.get(0);
            // create the bfs tree from random node and then rea
            // the bfs gives only that in the connected part by specific point
            Stack<Integer> bfsNodes = algoForGraph.BFS_visit(nodeTemp);
            HashSet<Integer> hashConection = new HashSet<>(bfsNodes);
            for (int node : arrNodes)
            {
                if (!hashConection.contains(node)) // if the node is not in the bfs then remove it
                {
                    tempGraph.removeNode(node);
                }
            }
            retGrapsh.add(tempGraph);

            // from the original take the used points
            while (!bfsNodes.empty() && original.nodeSize() > 0)
            {
                original.removeNode(bfsNodes.pop());
            }
        }

        if (nodeMain == null)
            return retGrapsh;

        // add to each graph the head node
        ArrayList<Integer> nodesNeigh = new ArrayList<>(nodeMain.getNeighbours());
        while (nodesNeigh.size() > 0)
        {
            int nodeId = nodesNeigh.get(0);

            for (int i = 0; i < retGrapsh.size(); i++)
            {
                if (retGrapsh.get(i).getNode(nodeId) != null)
                {
                    DirectedWeightedGraph temp_g = retGrapsh.get(i);
                    temp_g.addNode(nodeMain);
                    temp_g.connect(nodeMain.getKey(), nodeId, 1.0);
                    temp_g.connect(nodeId, nodeMain.getKey(),1.0);
                    nodesNeigh.remove(0);
                    break;
                }
            }
        }

        return retGrapsh;
    }

    public static ArrayList<Integer> nodesListInt(DirectedWeightedGraph graph)
    {
        ArrayList<Integer> arrInt = new ArrayList<>();
        Iterator<NodeData> nodeItr = graph.nodeIter();
        while (nodeItr.hasNext())
        {
            arrInt.add(nodeItr.next().getKey());
        }
        return arrInt;
    }

    public ArrayList<Integer> spanTree(int key)
    {
        Stack<Integer> treeRev = BFS_visit(key);
        ArrayList<Integer> spanTreeArray = new ArrayList<>();

        while (!treeRev.empty())
        {
            spanTreeArray.add(graph.getNode(treeRev.pop()).getKey());
        }

        return spanTreeArray;
    }

    public ArrayList<Integer> Adj(int key)
    {
        HashSet<Integer> adjHash = new HashSet<>();
        for (Iterator<EdgeData> it = graph.edgeIter(); it.hasNext(); )
        {
            EdgeData edg = it.next();
            if (edg.getSrc() == key)
                adjHash.add(edg.getDest());
            else if (edg.getDest() == key)
                adjHash.add(edg.getSrc());
        }
        return new ArrayList<>(adjHash);
    }

    public ArrayList<Integer> Adj(NodeData node)
    {
        if (node != null)
            return Adj(node.getKey());
        return new ArrayList<>();
    }

    public static final int WHITE = 0;
    public static final int GRAY = 1;
    public static final int BLACK = 2;

    public Stack<Integer> BFS_visit(int key)
    {
        HashMap<Integer, Integer> color = new HashMap<>();
        HashMap<Integer, Integer> p = new HashMap<>();
        // init the colors
        for (Iterator<NodeData> it = graph.nodeIter(); it.hasNext(); ) {
            NodeData u = it.next();
            color.put(u.getKey(), WHITE);
        }

        Queue<Integer> Q = new LinkedList<>();
        Q.add(key);
        Stack<Integer> treeRev = new Stack<>();
        while (!Q.isEmpty()) {
            int u = Q.poll();
            for (int v : Adj(u)) {
                if (color.containsKey(v) && color.get(v) == WHITE)
                {
                    color.put(v, GRAY);
                    p.put(v, u);
                    Q.add(v);
                }
            }
            color.put(u, BLACK);
            treeRev.push(u);
        }
        return treeRev;
    }


//    public static void main(String[] args) {
//        DirectedWeightedGraphAlgorithms algorithms = new MyGraphAlgo();
//        algorithms.init(new graphGen().generate_connected_graph(20));
//        algorithms.getGraph().removeNode(9);
//        List<NodeData> c = new LinkedList<>();
//        Iterator<NodeData> i = algorithms.getGraph().nodeIter();
//        while (i.hasNext())
//            c.add(i.next());
//        c = algorithms.tsp(c);
//        System.out.println("h");
//    }
}
