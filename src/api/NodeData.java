package api;

import java.util.HashSet;

/**
 * This interface represents the set of operations applicable on a 
 * node (vertex) in a (directional) weighted graph.
 * @author boaz.benmoshe
 */
public interface NodeData {
	/**
	 * Returns the key (id) associated with this node.
	 * @return
	 */
	public int getKey();
	/** Returns the location of this node, if none return null.
	 * @return
	 */
	public GeoLocation getLocation();
	/** Allows changing this node's location.
	 * @param p - new new location  (position) of this node.
	 */
	public void setLocation(GeoLocation p);
	/**
	 * Returns the weight associated with this node.
	 * @return
	 */
	public double getWeight();
	/**
	 * Allows changing this node's weight.
	 * @param w - the new weight
	 */
	public void setWeight(double w);
	/**
	 * Returns the remark (meta data) associated with this node.
	 * @return
	 */
	public String getInfo();
	/**
	 * Allows changing the remark (meta data) associated with this node.
	 * @param s
	 */
	public void setInfo(String s);
	/**
	 * Temporal data (aka color: e,g, white, gray, black) 
	 * which can be used be algorithms 
	 * @return
	 */
	public void setColor(int c);
	public int getColor();
	public boolean get_is_colored();
	public void set_is_colored(boolean flag);
	public int getDegree();
	public HashSet<Integer> getNeighbours();
	public boolean isVisited();
	public void setVisited(boolean visited);
	public void addNeighbour(NodeData node);
	public void removeNeighbour(NodeData node);
	public int getTag();
	public void setTag(int tag);

}
