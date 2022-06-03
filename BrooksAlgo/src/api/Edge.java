package api;

public class Edge implements EdgeData{
    private final int src;
    private final int dest;
    private final double weight;
    private String info;
    private boolean IsInMatch;

    // need to fill

    /**
     * Building edge
     * @param src is the source node.
     * @param dest is the destination node.
     * @param weight is the traveling cost of the node.
     */
    public Edge(int src, int dest, double weight){
        if(weight<=0){
            throw new RuntimeException("weight must be positive");
        }
        this.src = src;
        this.dest = dest;
        this.weight = weight;
    }

    /**
     * Copy constructor from EdgeData
     * @param e
     */
    public Edge(EdgeData e){
        this.src = e.getSrc();
        this.dest = e.getDest();
        this.weight = e.getWeight();
        this.tag=e.getTag();
        this.info=e.getInfo();
    }

    /**
     * Deep copy of an edge
     * @return
     */
    public Edge copy()
    {
        Edge e = new Edge(src,dest,weight);
        e.info=info;
        return e;
    }
    int tag;

    /**
     * Returns the source node ID.
     * @return source node id
     */
    @Override
    public int getSrc() {
        return this.src;
    }

    /**
     * Returns the destination node ID.
     * @return dest node id
     */
    @Override
    public int getDest() {
        return this.dest;
    }

    /**
     * The weight of the edge
     * @return weight of the edge
     */
    @Override
    public double getWeight() {
        return this.weight;
    }

    /**
     * Returns the metadata of the edge.
     * @return
     */
    @Override
    public String getInfo() {
        return this.info;
    }

    /**
     * Sets the metadata of the edge.
     * @param s
     */
    @Override
    public void setInfo(String s) {
        this.info = s;
    }

    /**
     * Returns the tag of the edge.
     * @return
     */
    @Override
    public int getTag() {
        return this.tag;
    }

    /**
     * Sets the tag of the edge
     * @param t - the new value of the tag
     */
    @Override
    public void setTag(int t) {
        this.tag = t;
    }

//    @Override
//    public String toString() {
//        return "\n{src = " + src +
//                ", dest = " + dest +
//                ", weight = " + weight +
//                "}";
//    }
    public void setIsInMtch( boolean flag){
        this.IsInMatch = flag;
    }
    public boolean isInMatch(){
        return this.IsInMatch;
    }
@Override
public String toString(){
        return "{\n"+'"'+"src"+'"'+": "+src+",\n"+'"'+"w"+'"'+": "+weight+",\n"+'"'+"dest"+'"'+": "+dest+"\n}";
}

}
