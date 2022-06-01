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
    private boolean isCenterActivated, isPathActivated, isTSPActivated, isBrooksActivated;
    private List<NodeData> pathByNodes;
    private List<NodeData> pathByNodesTSP;
    private int src, dest;
    public double minX;
    public double minY;
    public double maxX;
    public double maxY;
    private ArrayList<Color> random_colors;

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
            g2d.setPaint(Color.black);
            double srcX = (graph.getGraph().getNode(e.getSrc()).getLocation().x() - minX) * scaleX * 0.98 + 30;
            double srcY = (graph.getGraph().getNode(e.getSrc()).getLocation().y() - minY) * scaleY * 0.98 + 30;
            double destX = (graph.getGraph().getNode(e.getDest()).getLocation().x() - minX) * scaleX * 0.98 + 30;
            double destY = (graph.getGraph().getNode(e.getDest()).getLocation().y() - minY) * scaleY * 0.98 + 30;
            int x1 = (int) srcX;
            int y1 = (int) srcY;
            int x2 = (int) destX;
            int y2 = (int) destY;
            g2d.setStroke(new BasicStroke(3));
            g2d.setPaint(Color.black);
            drawArrowLine(g2d, x1, y1, x2, y2, 15, 7);
            // to draw the weights for now will keep it as comment.
//            double medX = middle(x1, x2);
//            double medY = middle(y1, y2);
//            g2d.setStroke(new BasicStroke(1));
//            g2d.setPaint(Color.white);
//            g2d.fillRect((int) (medX - 2), (int) medY - 10, 35, 15);
//            g2d.setPaint(Color.black);
//            g2d.drawRect((int) (medX - 2), (int) medY - 10, 35, 15);
//            g2d.setFont(new Font("ariel", Font.BOLD, 9));
//            g2d.drawString((Math.round(e.getWeight() * 10000d) / 10000d) + "", (int) medX, (int) medY);


        }
        // Draw all points a.k.a. Nodes.
        Iterator<NodeData> nodesIter = graph.getGraph().nodeIter();
        while (nodesIter.hasNext()) {
            g2d.setPaint(Color.blue);
            g2d.setStroke(new BasicStroke(1));
            Node n = (Node) nodesIter.next();
            int width = 20;
            int height = 20;
            if (this.isBrooksActivated) {
                g2d.setPaint(this.random_colors.get(n.getColor()-1));
                g2d.setStroke(new BasicStroke(3));
                width = 18;
                height = 18;
            }
            double x = (n.getLocation().x() - minX) * scaleX * 0.98 + 30;
            double y = (n.getLocation().y() - minY) * scaleY * 0.98 + 30;
            g2d.fillOval((int) x - (height / 2), (int) y -( width / 2), width, height);
            g2d.drawString("node color: "+n.getColor(), (int) x - (height / 2), (int) y -( width / 2)-5);
        }

//        nodesIter = graph.getGraph().nodeIter();
//        while (nodesIter.hasNext()) {
//            NodeData n = nodesIter.next();
//            double x = (n.getLocation().x() - minX) * scaleX * 0.98 + 30;
//            double y = (n.getLocation().y() - minY) * scaleY * 0.98 + 30;
////            String xs = "" + (Math.round(n.getLocation().x() * 10000d) / 10000d);
////            String ys = "" + (Math.round(n.getLocation().y() * 10000d) / 10000d);
////            String coordinate = "(" + xs + "," + ys + ")";
//            g2d.setStroke(new BasicStroke(1));
//            g2d.setPaint(Color.white);
//            if(this.center != null && n.getKey() == this.center.getKey() && this.isCenterActivated){
//                g2d.setPaint(Color.YELLOW);
//                this.isCenterActivated = false;
//            }
//            Rectangle2D rec = new Rectangle2D.Double((int) (x - 3), (int) y - 25, 20, 20);
//            double centerX = rec.getCenterX();
//            double centerY = rec.getCenterY();
//            g2d.fillRect((int) (x - 3), (int) y - 25, 20, 20);
//            g2d.setPaint(Color.black);
//            g2d.drawRect((int) (x - 3), (int) y - 25, 20, 20);
//            g2d.setFont(new Font("ariel", Font.BOLD, 14));
//            // to draw the coordinate.
////            g2d.drawString("v" + n.getKey() + coordinate, (int) x, (int) y - 10);
//            g2d.drawString(n.getKey() + "", (int) centerX - 8, (int) centerY + 6);
//        }
//        int src = -1;
//        int dest;
//        if(!this.pathByNodes.isEmpty()){
//            src = this.pathByNodes.get(0).getKey();
//            this.pathByNodes.remove(0);
//        }
//        while(this.pathByNodes.size() > 0){
//            g2d.setPaint(new Color(0,204,0));
//            dest = this.pathByNodes.get(0).getKey();
//            this.pathByNodes.remove(0);
//            double srcX = (graph.getGraph().getNode(src).getLocation().x() - minX) * scaleX * 0.98 + 30;
//            double srcY = (graph.getGraph().getNode(src).getLocation().y() - minY) * scaleY * 0.98 + 30;
//            double destX = (graph.getGraph().getNode(dest).getLocation().x() - minX) * scaleX * 0.98 + 30;
//            double destY = (graph.getGraph().getNode(dest).getLocation().y() - minY) * scaleY * 0.98 + 30;
//            int x1 = (int) srcX;
//            int y1 = (int) srcY;
//            int x2 = (int) destX;
//            int y2 = (int) destY;
//            g2d.setStroke(new BasicStroke(3));
//            drawArrowLine(g2d, x1, y1, x2, y2, 15, 7);
//            src = dest;
//            if(this.pathByNodes.size() == 0){
//                this.isPathActivated = false;
//            }
//        }
//
//        src = -1;
//        if(this.pathByNodesTSP != null && !this.pathByNodesTSP.isEmpty()){
//            src = this.pathByNodesTSP.get(0).getKey();
//            this.pathByNodesTSP.remove(0);
//        }
//        while(this.pathByNodesTSP != null && this.pathByNodesTSP.size() > 0){
//            g2d.setPaint(new Color(255,51,255));
//            dest = this.pathByNodesTSP.get(0).getKey();
//            this.pathByNodesTSP.remove(0);
//            double srcX = (graph.getGraph().getNode(src).getLocation().x() - minX) * scaleX * 0.98 + 30;
//            double srcY = (graph.getGraph().getNode(src).getLocation().y() - minY) * scaleY * 0.98 + 30;
//            double destX = (graph.getGraph().getNode(dest).getLocation().x() - minX) * scaleX * 0.98 + 30;
//            double destY = (graph.getGraph().getNode(dest).getLocation().y() - minY) * scaleY * 0.98 + 30;
//            int x1 = (int) srcX;
//            int y1 = (int) srcY;
//            int x2 = (int) destX;
//            int y2 = (int) destY;
//            g2d.setStroke(new BasicStroke(3));
//            drawArrowLine(g2d, x1, y1, x2, y2, 15, 7);
//            src = dest;
//            if(this.pathByNodesTSP.size() == 0){
//                this.isTSPActivated = false;
//            }
//        }
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
