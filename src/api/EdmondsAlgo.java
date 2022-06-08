package api;

import java.util.*;

public class EdmondsAlgo {
    HashSet<Integer> free;
    HashSet<EdgeData> match;
    DirectedWeightedGraph g;
    DirectedWeightedGraph tree;

    public EdmondsAlgo(DirectedWeightedGraph g) {
        this.free = new HashSet<>();
        this.match = new HashSet<>();
        this.init(g);
    }

    public EdmondsAlgo() {
        this.free = new HashSet<>();
        this.match = new HashSet<>();
    }

    void init(DirectedWeightedGraph g) {
        this.g = new MyGraph(g);
        this.set_match();
        this.set_free();
    }

    /**
     * setting the free set to correct state after initialization for example, when
     * init called and edges added after last match update
     */
    private void set_free() {
        this.free.clear();
        Iterator<NodeData> it = g.nodeIter();
        while (it.hasNext()) {
            this.free.add(it.next().getKey());
        }

        this.match.forEach((e) -> {
            this.free.remove(e.getSrc());
            this.free.remove(e.getDest());
        });

    }

    /**
     * setting the match set to correct state after initialization for example, when
     * init called and edges added after last match update
     */
    private void set_match() {
        for (Iterator<NodeData> it = g.nodeIter(); it.hasNext(); ) {
            NodeData n = it.next();
            int n_key = n.getKey();
            for (int ni : n.getNeighbours()) {
                EdgeData e1 = g.getEdge(n_key, ni);
                EdgeData e2 = g.getEdge(ni, n_key);
                if (e1.isInMatch()) {
                    this.match.add(e1);
                }
                if (e2.isInMatch()) {
                    this.match.add(e2);
                }

            }
        }
    }

    /**
     * return last match calculated in g
     *
     * @return
     */
    public HashSet<EdgeData> get_match() {
        return this.match;
    }

    public void update_match() { // the algorithm!

        while (!this.free.isEmpty()) {
            int first = free.iterator().next();
            NodeData root = this.g.getNode(first);
            free.remove(first);
            mainBfs(root);
        }
    }

    /*
     * perform the main bfs of the Edmonds algorithm. build a bfs tree from a given
     * root (node), allowing a backtracking on the tree.
     *
     * @param root
     */
    public void mainBfs(NodeData root) {
        Queue<NodeData> q = new LinkedList<>();
        HashSet<Integer> visited = new HashSet<>();
        DirectedWeightedGraph tree = new MyGraph();
        SuperNode sn = new SuperNode(g, tree);
        tree.addNode(root);
        q.add(root);
        while (!q.isEmpty()) {
            // pop head of queue
            NodeData cur = q.poll();

            tree.addNode(cur);

            // mark visited
            visited.add(cur.getKey());

            // iterate on cur neighbors
            for (int kni : cur.getNeighbours()) {
                EdgeData e = g.getEdge(cur.getKey(), kni);
                EdgeData e2 = g.getEdge(kni, cur.getKey());

                // ni_mate will indicate the match of ni, -1 if ni not in match
                int ni_mate = getMate(kni);

                // ni not in tree and ni in match
                if (!visited.contains(kni) && ni_mate != -1) {
                    // add mate to queue
                    q.add(g.getNode(ni_mate));

                    // add ni and mate to tree
                    tree.addNode(g.getNode(kni));
                    tree.connect(e);
                    tree.connect(e2);
                    tree.addNode(g.getNode(ni_mate));
                    EdgeData con = g.getEdge(kni,ni_mate);
                    EdgeData con2 = g.getEdge(ni_mate,kni);
                    tree.connect(con);
                    tree.connect(con2);

                    // mark visited
                    visited.add(kni);
                    visited.add(ni_mate);

                }

                // ni in tree and free
                else if (visited.contains(kni) && ni_mate == -1) {
                    var cycle = bfs(cur.getKey(), kni, tree);
                    // detect odd cycle
                    if (cycle.size() % 2 != 0) {
                        // compress the odd cycle
                        sn.compress(cycle);
                        // add the super node to queue
                        q.add(cur);
                        break;
                    }
                }
                // ni not in tree and free, aug path found!
                else if (ni_mate == -1) {
                    // connect the destination point
                    tree.addNode(g.getNode(kni));
                    EdgeData con = g.getEdge(cur.getKey(), kni);
                    EdgeData con2 = g.getEdge(kni,cur.getKey());
                    tree.connect(con);
                    tree.connect(con2);

                    // decompress all
                    sn.decompressAll();

                    // back track on tree
                    var path = getPath(backTracking(kni, root.getKey(), tree));

                    // augment
                    augment(path);

                    free.remove(kni);
                    q.clear();
                    break;
                }
            }
        }
        sn.decompressAll();
    }
    /**
     * perform a backtrack on the tree, favorating mates in tree junctions
     *
     * @param src
     * @param dest
     * @param tree
     * @return
     */
    public List<Integer> backTracking(int src, int dest, DirectedWeightedGraph tree) {
        int curr;
        var path = new LinkedList<Integer>();
        int prev = -1;

        path.add(src);
        curr = src;
        // until curr is not the dest or -1 (which all had been set)
        while (curr != dest && curr != -1)
        {
            var nei = tree.Neighbours(g.getNode(curr));
            if (nei.size() == 1)
            {
                // only one way to go
                curr = nei.iterator().next();
            }
            else if (nei.size() == 2)
            {
                // go forward, not backward
                for (int n : nei) {
                    if (n != prev) {
                        curr = n;
                        break;
                    }
                }
            }
            // a junction in the tree
            else {
                // go to mate, only 1 option as your mate is your parent
                curr = getMate(curr);
            }
            prev = path.getLast();
            if (curr != -1)
                path.add(curr);
        }
        return path;
    }

    /*
     * @param key -key node
     * @return -the mate of a given key node.
     */
    public int getMate(int key) {
        for (EdgeData e : match) {
            if (e.getSrc() == key) {
                return e.getDest();
            }
            if (e.getDest() == key) {
                return e.getSrc();
            }

        }
        return -1;
    }

    public List<EdgeData> getPath(List<Integer> p) {
        var ans = new LinkedList<EdgeData>();
        Iterator<Integer> nodes = p.iterator();
        int n = nodes.next();
        while (nodes.hasNext()) {
            int nei = nodes.next();
            ans.add(g.getEdge(n, nei));
            ans.add(g.getEdge(nei,n));
            n = nei;
        }
        return ans;
    }

    /**
     * only method that changes the match
     *
     * @param path
     */
    void augment(List<EdgeData> path) {

        path.forEach((e) -> {
            e.setIsInMtch(!e.isInMatch());
            if (e.isInMatch()) {
                this.match.add(e);
            } else {
                this.match.remove(e);
            }
        });
    }
    private void reset(DirectedWeightedGraph graph){
        for (Iterator<NodeData> it = graph.nodeIter(); it.hasNext(); ) {
            NodeData u = it.next();
            u.setTag(-1);
        }
    }

    /**
     * @param *get src the run time of this function is o(v+e) sign in the tag of
     *            every node the distance from the src and if havn't path to which
     *            node her tag will be with -1 the path kept in the hash map and
     *            convert to linked list
     */
    private LinkedList<Integer> bfs(int src, int dest, DirectedWeightedGraph graph) {
        if (graph.nodeSize()==0) // check if it is empty graph
            return new LinkedList<Integer>();

        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>(); // keep the nodes in the path -if path exist
        map.put(src, 0); // insert the src node
      this.reset(graph);

        Queue<NodeData> queue = new LinkedList<>();
        queue.add(graph.getNode(src)); // keep the key in the queue
        graph.getNode(src).setTag(0); // the first node init with 0

        while (!queue.isEmpty()) {
            NodeData loc = queue.poll();
            for (int n : graph.Neighbours(loc)) {
                NodeData nei = graph.getNode(n);
                if (nei.getTag() == -1) {
                    queue.add(nei);
                    nei.setTag(graph.getNode(loc.getKey()).getTag() + 1); // if have neighbor update the the tag according to his
                    // parent
                    map.put(nei.getKey(), loc.getKey());
                }
            }
        }

        // extract the path from the map
        LinkedList<Integer> list = new LinkedList<Integer>();
        if (graph.getNode(dest).getTag() != -1) { // check if path between the nodes exist
            int curr = dest;
            list.add(curr); // insert the first
            while (map.get(curr) != src) {
                list.add(map.get(curr));
                curr = map.get(curr); // return the neighbord of the current node
            }
            list.add(src);
            Collections.reverse(list);
        }
        this.reset(graph);
        return list;
    }


}