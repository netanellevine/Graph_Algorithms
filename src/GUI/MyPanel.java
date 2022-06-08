package GUI;

import api.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.List;

public class MyPanel extends JPanel {
    private final MyGraphAlgo graph;
    //    private Algorithms copyOfGraph;
    private NodeData center;
    private boolean isCenterActivated, isPathActivated, isTSPActivated, isBrooksActivated, isHungarianActivated, isEdmondActivated;
    private List<NodeData> pathByNodes;
    private List<NodeData> pathByNodesTSP;
    private int src, dest;
    public double minX;
    public double minY;
    public double maxX;
    public double maxY;
    private ArrayList<Color> random_colors;
    private HashSet<String> _edgeHash;


    public MyPanel(DirectedWeightedGraph g) {
        this.graph = new MyGraphAlgo();
//        this.copyOfGraph = new Algorithms();
        this.graph.init(g);
//        this.copyOfGraph.init(this.graph.copy());
        this.center = null;
        this.isCenterActivated = false;
        this.isPathActivated = false;
        this.isTSPActivated = false;
        this.isBrooksActivated = false;
        this.isHungarianActivated = false;
        this.isEdmondActivated = false;
        this.src = -1;
        this.dest = -1;
        this.pathByNodes = new LinkedList<>();
        this.pathByNodesTSP = new LinkedList<>();
        try {
            setMin();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Dimension scale = Toolkit.getDefaultToolkit().getScreenSize();
        int width = scale.width;
        int height = (int)(scale.height * 0.8);
        this.setLayout(new BorderLayout());
        this.setBackground(Color.white);
        this.setPreferredSize(new Dimension((int)(width / 1.3), (int)(height / 1.3)));
        repaint();
    }

    public void paint(Graphics graphics) {
        double ABSx = Math.abs(minX - maxX);
        double ABSy = Math.abs(minY - maxY);
        double scaleX = (getWidth() / ABSx) * 0.8;
        double scaleY = (getHeight() / ABSy) * 0.8;
        Graphics2D g2d = (Graphics2D) graphics;
        g2d.setPaint(Color.blue);
        g2d.setStroke(new BasicStroke(1));
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


        // Draw all Edges.
        Iterator<EdgeData> edgesIter = graph.getGraph().edgeIter();
        while (edgesIter.hasNext()) {
            EdgeData e = edgesIter.next();
            // default color
            g2d.setPaint(Color.black);

            if(this.isEdmondActivated || this.isHungarianActivated)
            {
                // check if the edge is in the set and set that the coloring is red
                if (isEdgeToColor(e))
                    g2d.setPaint(Color.RED);
            }

            // set the enviroment
            double srcX = (graph.getGraph().getNode(e.getSrc()).getLocation().x() - minX) * scaleX * 0.98 + 30;
            double srcY = (graph.getGraph().getNode(e.getSrc()).getLocation().y() - minY) * scaleY * 0.98 + 30;
            double destX = (graph.getGraph().getNode(e.getDest()).getLocation().x() - minX) * scaleX * 0.98 + 30;
            double destY = (graph.getGraph().getNode(e.getDest()).getLocation().y() - minY) * scaleY * 0.98 + 30;
            int x1 = (int) srcX;
            int y1 = (int) srcY;
            int x2 = (int) destX;
            int y2 = (int) destY;
            g2d.setStroke(new BasicStroke(3));
            drawArrowLine(g2d, x1, y1, x2, y2, 15, 7);

        }

        // Draw all points a.k.a. Nodes.
        Iterator<NodeData> nodesIter = graph.getGraph().nodeIter();
        while (nodesIter.hasNext()) {
            g2d.setPaint(Color.blue);
            g2d.setStroke(new BasicStroke(1));
            Node n = (Node) nodesIter.next();
            int width = 5;
            int height = 5;
            if (this.isBrooksActivated) {
                g2d.setPaint(this.random_colors.get(n.getColor()-1));
                g2d.setStroke(new BasicStroke(3));
                width = 18;
                height = 18;
            }
            double x = (n.getLocation().x() - minX) * scaleX * 0.98 + 30;
            double y = (n.getLocation().y() - minY) * scaleY * 0.98 + 30;
            g2d.fillOval((int) x - (height / 2), (int) y -( width / 2), width, height);
            g2d.drawString(""+n.getKey(), (int) x - (height / 2), (int) y -( width / 2)-5);
        }
    }

    /**
     * set the hash edges that needed to be colored
     * @param edges
     */
    public void setEdgesToColor(HashSet<EdgeData> edges)
    {
        _edgeHash = new HashSet<>();
        for (EdgeData edgeData : edges)     // enter by string - good indicator
        {
            _edgeHash.add(String.format("[%s,%s]", edgeData.getSrc(), edgeData.getDest()));
            _edgeHash.add(String.format("[%s,%s]", edgeData.getDest(), edgeData.getSrc()));
        }
    }

    /**
     * by given edge check if in hash set of needed to be colored
     * @param edge
     * @return
     */
    public boolean isEdgeToColor(EdgeData edge)
    {
        return _edgeHash.contains(String.format("[%s,%s]", edge.getSrc(), edge.getDest()));
    }

    public void setMin() {
        minX = Integer.MAX_VALUE;
        minY = Integer.MAX_VALUE;
        maxX = Integer.MIN_VALUE;
        maxY = Integer.MIN_VALUE;
        Iterator<NodeData> minXIter = graph.getGraph().nodeIter();
        while (minXIter.hasNext()) {
            NodeData n = minXIter.next();
            if (n.getLocation().x() < minX) {
                minX = n.getLocation().x();
            }
        }
        Iterator<NodeData> minYIter = graph.getGraph().nodeIter();
        while (minYIter.hasNext()) {
            NodeData m = minYIter.next();
            if (m.getLocation().y() < minY) {
                minY = m.getLocation().y();
            }
        }
        Iterator<NodeData> maxXIter = graph.getGraph().nodeIter();
        while (maxXIter.hasNext()) {
            NodeData n = maxXIter.next();
            if (n.getLocation().x() > maxX) {
                maxX = n.getLocation().x();
            }
        }
        Iterator<NodeData> maxYIter = graph.getGraph().nodeIter();
        while (maxYIter.hasNext()) {
            NodeData m = maxYIter.next();
            if (m.getLocation().y() > maxY) {
                maxY = m.getLocation().y();
            }
        }
    }


    /**
     * Draw an arrow line between two points.
     * @param g the graphics component.
     * @param x1 x-position of first point.
     * @param y1 y-position of first point.
     * @param x2 x-position of second point.
     * @param y2 y-position of second point.
     * @param d  the width of the arrow.
     * @param h  the height of the arrow.
     * this code has been taken from:
     *          https://stackoverflow.com/questions/2027613/how-to-draw-a-directed-arrow-line-in-java,
     *          with some modifications to fit to out needs.
     */
    private void drawArrowLine(Graphics g, int x1, int y1, int x2, int y2, int d, int h) {
        int dx = x2 - x1, dy = y2 - y1;
        double D = Math.sqrt(dx * dx + dy * dy);
        double xm = D - d, xn = xm, ym = h, yn = -h, x;
        double sin = dy / D, cos = dx / D;

        x = xm * cos - ym * sin + x1;
        ym = xm * sin + ym * cos + y1;
        xm = x;

        x = xn * cos - yn * sin + x1;
        yn = xn * sin + yn * cos + y1;
        xn = x;

        int[] Xpoints = {x2, (int) xm, (int) xn};
        int[] Ypoints = {y2, (int) ym, (int) yn};
        g.drawLine(x1, y1, x2, y2);
        g.setColor(Color.black);
//        g.fillPolygon(Xpoints, Ypoints, 3);
    }

    private double middle(double x1, double x2){
        return Math.abs((x2 + x1) / 2);
    }

    public MyGraphAlgo getGraph() {
        return graph;
    }

    public void setCenterActivated(boolean centerActivated) {
        isCenterActivated = centerActivated;
    }

    public void setBrooksActivated(boolean brooksActivated) {
        this.isBrooksActivated = brooksActivated;
    }

    public void setHungarianActivated(boolean HungarianActivated) {
        this.isHungarianActivated = HungarianActivated;
    }

    public void setEdmondActivated(boolean EdmondActivated) {
        this.isEdmondActivated = EdmondActivated;
    }

    public void setRandomColors(ArrayList<Color> random_colors) {this.random_colors = random_colors;}

    public void setPath(List<NodeData> path) {
        this.pathByNodes = path;
    }

    public int getSrc() {
        return src;
    }

    public void setSrc(int src) {
        this.src = src;
    }

    public int getDest() {
        return dest;
    }

    public void setPathByNodesTSP(List<NodeData> path) {
        this.pathByNodesTSP = path;
    }

    public void setDest(int dest) {
        this.dest = dest;
    }

    public void setPathActivated(boolean pathActivated) {
        isPathActivated = pathActivated;
    }

    public void setCenter(NodeData center) {
        this.center = center;
    }

    public void setPathByNodesTSPActivated(boolean pathActivated) {
        this.isTSPActivated = pathActivated;
    }

    public NodeData getCenter() {
        return center;
    }

    public void checkMin(NodeData newNode) {
        double x = newNode.getLocation().x();
        double y = newNode.getLocation().y();
        if(x > this.maxX){
            this.maxX = x;
        }
        else if(x < this.minX){
            this.minX = x;
        }
        if(y > this.maxY){
            this.maxY = y;
        }
        else if(y < this.minY){
            this.minY = y;
        }
    }
}
