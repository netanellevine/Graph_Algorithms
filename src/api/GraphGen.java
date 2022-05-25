package api;

import java.util.HashMap;
import java.util.Random;

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
    public MyGraph generate_connected_graph(int numOfNodes){
        HashMap<Integer, Node> nodes = new HashMap<>();
        Random rand = new Random();
        for(int i=0;i<numOfNodes;i++){
            nodes.put(i,new Node(rand.nextDouble()*100,rand.nextDouble()*100,i));
        }
        for(int i=0;i<numOfNodes;i++){
            Edge e = new Edge(i,(i+1)%numOfNodes,rand.nextDouble()*10);
            nodes.get(i).getEdges().put(i+1,e);
            nodes.get((i+1)%numOfNodes).inEdges().add(i);
        }
        return new MyGraph(nodes,numOfNodes);
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
        return new MyGraph(nodes,numberOfEdges);
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
            nodes.put(i,new Node(rand.nextDouble()*100,rand.nextDouble()*100,i));
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
                    return new MyGraph(nodes,count);
            }
        }
        return new MyGraph(nodes,numberOfEdges);
    }

}
