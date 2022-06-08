package api;

import java.util.*;

public class GraphGen {
    /**
     * This class is used for generating a graphs for testing purposes
     */
    public GraphGen(){}

    /**
     * Generating a strongly connected graph
     * @param numOfNodes number of nodes
     * @return
     */
    public static MyGraph generate_perfect_graph(int numOfNodes){
        Random rand = new Random();
        MyGraph graph = new MyGraph();
        for(int i=0;i<numOfNodes;i++){
            graph.addNode(new Node(rand.nextDouble()*100,rand.nextDouble()*100,i));
        }
        int counter = 0;
        for(int i=0; i < numOfNodes; i++)
        {
            for (int j = 0; j < numOfNodes; j++)
            {
                if (i == j)
                    continue;
                graph.connect(i, j, 1.0);
                counter++;
            }
        }
        return graph;
    }

    /**
     * generates a connected graph randomly
     * @param numOfNodes
     * @return
     */
    public static MyGraph generate_connected_graph(int numOfNodes){
        MyGraph graph = generate_perfect_graph(numOfNodes);
        Random rand = new Random();
        //
        HashSet<Integer[]> hashSet = new HashSet<>();
        Iterator<EdgeData> edgeIt = graph.edgeIter();
        while (edgeIt.hasNext())
        {
            EdgeData edge = edgeIt.next();
            int src = Math.min(edge.getSrc(), edge.getDest());
            int dest = Math.max(edge.getSrc(), edge.getDest());
            hashSet.add(new Integer[]{src, dest});
        }

        // remove part
        ArrayList<Integer[]> arrEdges = new ArrayList<>(hashSet);
        int numOfEdgesRemove = rand.nextInt(arrEdges.size() - arrEdges.size()/4) + arrEdges.size()/4;
        for(int i = 0; i < numOfEdgesRemove; i++)
        {
            int index = rand.nextInt(arrEdges.size());
            Integer[] arr = arrEdges.get(index);
            int src = arr[0];
            int dest = arr[1];
            if (graph.getNode(src).getDegree() == 1 || graph.getNode(dest).getDegree() == 1)
                continue;
            graph.removeEdge(src, dest);
            graph.removeEdge(dest, src);
            arrEdges.remove(index);
        }
        return graph;
    }

    public static MyGraph generate_biparted_graph(int numOfNodes){
        MyGraph graph = new MyGraph();
        Random rand = new Random();
        // adding the nodes
        for (int i = 0; i < numOfNodes; i++)
        {
            graph.addNode(new Node((double)(i % 2), (double)(i / 2), i));
        }

        for(int i = 0; i < numOfNodes; i++)
        {
            // how many nodes allowed
            int numEdges = numOfNodes / 2 + (i + 1) % 2;
            numEdges = rand.nextInt(numEdges)/3 + 1;
            for (int j = 0; j < numEdges; j++)
            {
                int node = rand.nextInt(numOfNodes*100) % numOfNodes;
                // get the even or odd node number according to its node
                node = 2*(node/2) + (i + 1) % 2;
                graph.connect(i, node, 1.0);
                graph.connect(node, i, 1.0);
            }
        }
        return graph;
    }

    /**
     * Random graph with a set number of nodes and edges
     * @param numOfNodes
     * @param numberOfEdges
     * @return
     */
    public MyGraph generate_graph(int numOfNodes,int numberOfEdges){
        HashMap<Integer, Node> nodes = new HashMap<>();
        Random rand = new Random();
        int count=0;
        for(int i=0;i<numOfNodes;i++){
            nodes.put(i,new Node(rand.nextDouble()+32,rand.nextDouble()+35,i));
        }
        while (count<numberOfEdges)
        {   int x = rand.nextInt(numOfNodes);
            int y = rand.nextInt(numOfNodes);
            while (x==y){
                y = rand.nextInt(numOfNodes);
            }
            Edge e = new Edge(x,y,rand.nextDouble()*10);
            if(!nodes.get(x).getEdges().containsKey(y)) {
                nodes.get(x).getEdges().put(y, e);
                nodes.get(y).inEdges().add(x);
                count++;
            }
        }
        return new MyGraph();
    }

    /**
     * Used for genrating large graphs.
     * @param numOfNodes
     * @param numberOfEdges
     * @return
     */
    public MyGraph generate_large_graph(int numOfNodes,int numberOfEdges){
        HashMap<Integer, Node> nodes = new HashMap<>();
        int count=0;
        Random rand = new Random();
        for(int i=0;i<numOfNodes;i++){
            nodes.put(i,new Node(rand.nextDouble()*1000,rand.nextDouble()*1000,i));
        }
        for(int i=0;i<numOfNodes;i++)
        {
            for(int j=i+1;j<numOfNodes;j++){
                Edge e = new Edge(i,j,rand.nextDouble()*10);
                nodes.get(i).addEdge(e);
                nodes.get(e.getDest()).inEdges().add(i);
                Edge e1 = new Edge(j,i,rand.nextDouble()*10);
                nodes.get(j).addEdge(e1);
                nodes.get(e1.getDest()).inEdges().add(j);
                count=count+2;
                if(count==numberOfEdges)
                    return new MyGraph();
            }
        }
        return new MyGraph();
    }

}
