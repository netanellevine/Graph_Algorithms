package GUI;

import api.*;

import javax.naming.NoInitialContextException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.nio.file.FileAlreadyExistsException;
import java.rmi.NoSuchObjectException;
import java.util.*;
import java.io.File;
import java.util.List;


public class MyFrame extends JFrame implements ActionListener{
    private MyPanel mainPanel;
    private final JPanel buttonsPanel;
    private JPanel outputPanel;
    private JScrollPane terminal;
    private JTextArea JTA;
    private DirectedWeightedGraph graph;
    private DirectedWeightedGraph graphCopy;
    private MyGraphAlgo algo;
    private BrooksAlgo brooksAlgo;
    private NodeData center;
    private String outputText;
    private int width, height;
    private ArrayList<Color> random_colors;
    // private Boolean Hungarian_flag = false;

    // All menus
    JMenuBar menuBar;
    JMenu fileMenu;
    JMenu editMenu;
    JMenu algorithmsMenu;
    JMenu viewMenu;
    JMenu helpMenu;

    // File Menu
    JMenuItem loadItem;
    JMenuItem saveItem;
    JMenuItem clearItem;
    JMenuItem resetItem;
    JMenuItem exitItem;

    // Edit menu
    JMenuItem removeNodeItem;
    JMenuItem removeEdgeItem;
    JMenuItem addNodeItem;
    JMenuItem addEdgeItem;

    // Algorithms menu
    JMenuItem isConnectedItem;
    JMenuItem shortestPath;
    JMenuItem centerItem;
    JMenuItem TSPItem;
    JMenuItem BrooksItem;
    JMenuItem HungarianItem;
    JMenuItem EdmondsItem;

    // View menu
    JMenuItem hideButtonsItem;
    JMenuItem showButtonsItem;
    JMenuItem fullScreenItem;
    JMenuItem defaultItem;
    JMenuItem customScaleItem;

    // Help menu
    JMenuItem shortcutsItem;



    // Buttons
    JButton SP;
    JButton IC;
    JButton TSP;
    JButton CE;
    JButton RN;
    JButton RE;
    JButton AN;
    JButton AE;
    JButton CLR;
    JButton RESET;
    JButton LOAD;
    JButton SAVE;
    JButton Brooks;
    JButton Hungarian;
    JButton Edmonds;
    JButton stepByStep;


    public MyFrame(DirectedWeightedGraph g){
        this.graph = g;
        this.mainPanel = new MyPanel(this.graph);
        this.buttonsPanel = new JPanel();
        this.outputPanel = new JPanel(new BorderLayout());
        this.algo = this.mainPanel.getGraph();
        this.graphCopy = algo.copy();
        this.center = null;
        this.outputText = " Welcome to My Directed Weighted Graph action log...";
        initGUI();
        addButtonsAndText();
    }


    public void initGUI(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setTitle("My Directed Weighted Graph");
        ImageIcon title = new ImageIcon("src/main/java/GUI/icons/graph.jpg");
        this.setIconImage(title.getImage());

        Dimension scale = Toolkit.getDefaultToolkit().getScreenSize();
        this.width = (int) (scale.width * 0.75);
        this.height = (int) (scale.height * 0.9);
        this.setPreferredSize(new Dimension(this.width, this.height));
        this.setResizable(true);

        this.JTA = new JTextArea(outputText);
        this.JTA.setBackground(Color.black);
        this.JTA.setForeground(Color.white);
        this.JTA.setFont(new Font("ariel", Font.BOLD, 14));
        this.JTA.setEditable(false);

        this.terminal = new JScrollPane(JTA, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);


        this.buttonsPanel.setPreferredSize(new Dimension(width / 25, height / 22));
        buttonsPanel.setBackground(Color.GRAY);


        this.outputPanel.setPreferredSize(new Dimension(width / 10, height / 4));
        this.outputPanel.add(this.terminal);

        this.add(buttonsPanel, BorderLayout.NORTH);
        this.add(mainPanel, BorderLayout.CENTER);
        this.add(outputPanel, BorderLayout.SOUTH);

        createMenus();
        createKeyShortCuts();
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }


    private void createMenus(){
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        editMenu = new JMenu("Edit");
        algorithmsMenu = new JMenu("Algorithms");
        viewMenu = new JMenu("View");
        helpMenu = new JMenu("Help");


        // File Menu
        loadItem = new JMenuItem("Load                    (Alt+F+L)");
        saveItem = new JMenuItem("Save                    (Alt+F+S)");
        clearItem = new JMenuItem("Clear                   (Alt+F+C)");
        resetItem = new JMenuItem("Reset Graph      (Alt+F+R)");
        exitItem = new JMenuItem("Exit                      (Alt+F+E)");

        loadItem.addActionListener(this);
        saveItem.addActionListener(this);
        clearItem.addActionListener(this);
        resetItem.addActionListener(this);
        exitItem.addActionListener(this);

        fileMenu.add(loadItem);
        fileMenu.add(saveItem);
        fileMenu.add(clearItem);
        fileMenu.add(resetItem);
        fileMenu.add(exitItem);


        // Edit menu
        addNodeItem = new JMenuItem("Add Node             (Alt+E+A)");
        addEdgeItem = new JMenuItem("Add Edge             (Alt+E+S)");
        removeNodeItem = new JMenuItem("Remove Node     (Alt+E+D)");
        removeEdgeItem = new JMenuItem("Remove Edge     (Alt+E+F)");


        removeNodeItem.addActionListener(this);
        removeEdgeItem.addActionListener(this);
        addNodeItem.addActionListener(this);
        addEdgeItem.addActionListener(this);


        editMenu.add(addNodeItem);
        editMenu.add(addEdgeItem);
        editMenu.add(removeNodeItem);
        editMenu.add(removeEdgeItem);


        // Algorithms menu
        isConnectedItem = new JMenuItem("isConnected        (Alt+A+I)");
        shortestPath = new JMenuItem("Shortest Path      (Alt+A+S)");
        centerItem = new JMenuItem("Center                  (Alt+A+C)");
        TSPItem = new JMenuItem("TSP                       (Alt+A+T)");
        this.BrooksItem = new JMenuItem("Brooks coloring (Alt+A+B)");
        HungarianItem = new JMenuItem("Hungarian max-matching    (Alt+A+H)");
        EdmondsItem = new JMenuItem("Edmonds max-matching    (Alt+A+M)");



        isConnectedItem.addActionListener(this);
        shortestPath.addActionListener(this);
        centerItem.addActionListener(this);
        TSPItem.addActionListener(this);
        this.BrooksItem.addActionListener(this);
        this.HungarianItem.addActionListener(this);
        this.EdmondsItem.addActionListener(this);

        algorithmsMenu.add(isConnectedItem);
        algorithmsMenu.add(shortestPath);
        algorithmsMenu.add(centerItem);
        algorithmsMenu.add(TSPItem);
        this.algorithmsMenu.add(this.BrooksItem);
        this.algorithmsMenu.add(this.HungarianItem);
        this.algorithmsMenu.add(this.EdmondsItem);


        // View menu
        hideButtonsItem = new JMenuItem("Hide Buttons                  (Alt+V+H)");
        showButtonsItem = new JMenuItem("Show Buttons                 (Alt+V+S)");
        fullScreenItem = new JMenuItem("Full Screen                     (Alt+V+F)");
        defaultItem = new JMenuItem("Default Screen               (Alt+V+D)");
        customScaleItem = new JMenuItem("Custom Screen              (Alt+V+C)");

        hideButtonsItem.addActionListener(this);
        showButtonsItem.addActionListener(this);
        fullScreenItem.addActionListener(this);
        defaultItem.addActionListener(this);
        customScaleItem.addActionListener(this);

        viewMenu.add(hideButtonsItem);
        viewMenu.add(showButtonsItem);
        viewMenu.add(fullScreenItem);
        viewMenu.add(defaultItem);
        viewMenu.add(customScaleItem);

        // Help menu
        shortcutsItem = new JMenuItem("Shortcuts");

        shortcutsItem.addActionListener(this);

        helpMenu.add(shortcutsItem);



        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(algorithmsMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);
        this.setJMenuBar(menuBar);
    }


    private void createKeyShortCuts(){
        // Shortcuts for fileMenu.
        fileMenu.setMnemonic(KeyEvent.VK_F); // Alt + f
        loadItem.setMnemonic(KeyEvent.VK_L); // l
        saveItem.setMnemonic(KeyEvent.VK_S); // s
        clearItem.setMnemonic(KeyEvent.VK_C); // c
        resetItem.setMnemonic(KeyEvent.VK_R); // r
        exitItem.setMnemonic(KeyEvent.VK_E); // e

        // Shortcuts for editMenu.
        editMenu.setMnemonic(KeyEvent.VK_E); // Alt + e
        addNodeItem.setMnemonic(KeyEvent.VK_A); // a
        addEdgeItem.setMnemonic(KeyEvent.VK_S); // s
        removeNodeItem.setMnemonic(KeyEvent.VK_D); // d
        removeEdgeItem.setMnemonic(KeyEvent.VK_F); // f

        // Shortcuts for algorithmsMenu.
        algorithmsMenu.setMnemonic(KeyEvent.VK_A); // Alt + a
        isConnectedItem.setMnemonic(KeyEvent.VK_I); // i
        shortestPath.setMnemonic(KeyEvent.VK_S); // s
        centerItem.setMnemonic(KeyEvent.VK_C); // c
        TSPItem.setMnemonic(KeyEvent.VK_T); // t
        this.BrooksItem.setMnemonic(KeyEvent.VK_B); // b
        this.HungarianItem.setMnemonic(KeyEvent.VK_H); //H
        this.EdmondsItem.setMnemonic(KeyEvent.VK_M); //M


        // Shortcuts for viewMenu.
        viewMenu.setMnemonic(KeyEvent.VK_V); // Alt + v
        hideButtonsItem.setMnemonic(KeyEvent.VK_H); // f
        showButtonsItem.setMnemonic(KeyEvent.VK_S); // f
        fullScreenItem.setMnemonic(KeyEvent.VK_F); // f
        defaultItem.setMnemonic(KeyEvent.VK_D); // d
        customScaleItem.setMnemonic(KeyEvent.VK_C); // c

    }


    private void updateTerminal(){
        this.remove(this.outputPanel);
        this.outputPanel = new JPanel(new BorderLayout());
        this.JTA = new JTextArea(this.outputText);
        this.JTA.setBackground(Color.black);
        this.JTA.setForeground(Color.white);
        this.JTA.setFont(new Font("ariel", Font.BOLD, 14));
        this.JTA.setEditable(false);
        this.JTA.setCaretPosition(this.JTA.getText().length());

        this.terminal = new JScrollPane(JTA, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        this.outputPanel.setPreferredSize(new Dimension(this.width / 10, this.height / 4));
        this.outputPanel.add(this.terminal);

        this.add(this.buttonsPanel, BorderLayout.NORTH);
        this.add(this.mainPanel, BorderLayout.CENTER);
        this.add(this.outputPanel, BorderLayout.SOUTH);
        this.pack();
    }


    private void addButtonsAndText(){

        IC = new JButton("isConnected");
        IC.setFocusable(false);
        IC.addActionListener(this);
        IC.setBackground(Color.ORANGE);


        TSP = new JButton("TSP");
        TSP.setFocusable(false);
        TSP.addActionListener(this);
        TSP.setBackground(Color.ORANGE);


        CE = new JButton("Center");
        CE.setFocusable(false);
        CE.addActionListener(this);
        CE.setBackground(Color.ORANGE);


        SP = new JButton("Shortest Path");
        SP.setFocusable(false);
        SP.addActionListener(this);
        SP.setBackground(Color.ORANGE);



        RN = new JButton("Remove Node");
        RN.setFocusable(false);
        RN.addActionListener(this);
        RN.setBackground(new Color(243,76,76));


        RE = new JButton("Remove Edge");
        RE.setFocusable(false);
        RE.addActionListener(this);
        RE.setBackground(new Color(243,76,76));


        AN = new JButton("Add Node");
        AN.setFocusable(false);
        AN.addActionListener(this);
        AN.setBackground(new Color(243,76,76));


        AE = new JButton("Add Edge");
        AE.setFocusable(false);
        AE.addActionListener(this);
        AE.setBackground(new Color(243,76,76));


        CLR = new JButton("Clear");
        CLR.setFocusable(false);
        CLR.addActionListener(this);
        CLR.setBackground(new Color(51,153,255));

        RESET = new JButton("Reset Graph");
        RESET.setFocusable(false);
        RESET.addActionListener(this);
        RESET.setBackground(new Color(51,153,255));

        SAVE = new JButton("Save");
        SAVE.setFocusable(false);
        SAVE.addActionListener(this);
        SAVE.setBackground(new Color(51,153,255));

        LOAD = new JButton("Load");
        LOAD.setFocusable(false);
        LOAD.addActionListener(this);
        LOAD.setBackground(new Color(51,153,255));

        this.Brooks = new JButton("Brooks");
        this.Brooks.setFocusable(false);
        this.Brooks.addActionListener(this);
        this.Brooks.setBackground(Color.ORANGE);

        this.Hungarian = new JButton("Hungarian Max-Matching");
        this.Hungarian.setFocusable(false);
        this.Hungarian.addActionListener(this);
        this.Hungarian.setBackground(Color.PINK);

        this.Edmonds = new JButton("Edmonds Blossom");
        this.Edmonds.setFocusable(false);
        this.Edmonds.addActionListener(this);
        this.Edmonds.setBackground(Color.GREEN);

        this.stepByStep = new JButton("Step By Step Hungarian");
        this.stepByStep.addActionListener(this);
        this.stepByStep.setBackground(Color.MAGENTA);



        buttonsPanel.add(IC);
        buttonsPanel.add(TSP);
        buttonsPanel.add(CE);
        this.buttonsPanel.add(this.Brooks);
        this.buttonsPanel.add(this.Hungarian);
        this.buttonsPanel.add(this.stepByStep);
        this.buttonsPanel.add(this.Edmonds);
        //buttonsPanel.add(RN);
        //buttonsPanel.add(RE);
        //buttonsPanel.add(AN);
        //buttonsPanel.add(AE);
        buttonsPanel.add(LOAD);
        //buttonsPanel.add(SAVE);
        //buttonsPanel.add(CLR);
        buttonsPanel.add(RESET);


        repaint();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Object event = e.getSource();
        UIManager.put("OptionPane.messageFont", new Font("ariel", Font.BOLD, 14));

        // FILE

        if (loadItem.equals(event) || LOAD.equals(event)) {
            this.outputText += "\n Load Graph activated.";
            JFileChooser loadFile = new JFileChooser();
            try
            {
                loadFile.setCurrentDirectory(new File("./data"));
            }
            catch (Exception ex)
            {
                loadFile.setCurrentDirectory(new File("."));
            }

            int approved = loadFile.showOpenDialog(null);
            if (approved == JFileChooser.APPROVE_OPTION) {
                ParseToGraph pd = null;
                try {
                    pd = new ParseToGraph(loadFile.getSelectedFile().getAbsolutePath());
                    MyGraph g = new MyGraph(pd);
                    setVisible(false);
                    dispose();
                    new MyGraph_GUI(g, this.outputText);
                } catch (Exception ex) {
                    JOptionPane.showOptionDialog(null,
                            "Wrong file!\nThis Program supports only Json files.",
                            "WRONG FILE FORMAT",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.ERROR_MESSAGE,
                            null,
                            null,
                            null);
                    this.outputText += "\n The file: " + loadFile.getSelectedFile().getName() + " was not loaded.";
                    this.outputText += "\n " + ex.getMessage();
//                    System.out.println("This Program supports only Json files.");
                }
            } else {
                this.outputText += "\n Load Graph canceled.";
            }
        }

        else if (saveItem.equals(event) || SAVE.equals(event)) {
            JFileChooser saveFile = new JFileChooser();
            this.outputText += "\n Save Graph activated.";
            saveFile.setCurrentDirectory(new File("."));
            int approved = saveFile.showSaveDialog(null);
            if (approved == JFileChooser.APPROVE_OPTION) {
                String path = saveFile.getSelectedFile().getAbsolutePath();
                boolean success = this.algo.save(path);
                if(success) {
                    this.outputText += "\n The file: " + saveFile.getSelectedFile().getName() + ", was saved successfully";
                }
                else{
                    JOptionPane.showOptionDialog(null,
                            "The file was not saved, please check if the file already exists",
                            "ERROR",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.ERROR_MESSAGE,
                            null,
                            null,
                            null);
                    this.outputText += "\n The file: " + saveFile.getSelectedFile().getName() + " was not saved.";
                }
            } else {
                this.outputText += "\n Save Graph canceled.";
            }
        }

        else if (clearItem.equals(event) || CLR.equals(event)) {
            new MyGraph_GUI(new GraphGen().generate_connected_graph(20));
//            try {
//                MyGraph g = new MyGraph();
//                setVisible(false);
//                dispose();
//                new MyGraph_GUI(g);
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
        }

        else if (resetItem.equals(event) || RESET.equals(event)) {
            try {
                MyGraph g = new MyGraph(this.graphCopy);
                setVisible(false);
                dispose();
                new MyGraph_GUI(g);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        else if (exitItem.equals(event)) {
//            System.out.println("Exiting");
            System.exit(0);
        }

        // ADD/REMOVE

        else if (addEdgeItem.equals(event) || AE.equals(event)) {
            this.outputText += "\n Add Edge activated.";
            JTextField srcText = new JTextField("Source");
            JTextField destText = new JTextField("Destination");
            JTextField weightText = new JTextField("Weight");
            updateMouseListener(srcText);
            updateMouseListener(destText);
            updateMouseListener(weightText);
            String title = """
                    Insert the Source, Destination, Weight
                    values to add to the new Edge.
                    In order to finish click on the OK button.
                    """;
            srcText.setToolTipText("Enter the Source Node");
            destText.setToolTipText("Enter the Destination Node");
            weightText.setToolTipText("Enter the weight of this Edge");
            Object[] options = {title, "\n", srcText, destText, weightText};

            JOptionPane j = new JOptionPane();
            j.setMessage(options);
            j.setMessageType(JOptionPane.INFORMATION_MESSAGE);
            JDialog dialog = j.createDialog(null, "Add new Edge");
            dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            final boolean[] success = {false};
            dialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    dialog.dispose();
                    success[0] = true;
                }
            });
            dialog.setVisible(true);
            int src = -1;
            int dest = -1;
            double weight;
            try {
                if(success[0]){
                    throw new RuntimeException();
                }
                src = Integer.parseInt(srcText.getText());
                dest = Integer.parseInt(destText.getText());
                weight = Double.parseDouble(weightText.getText());
                if(this.graph.getNode(src) != null && this.graph.getNode(dest) != null) {
                    if (this.graph.getEdge(src, dest) == null) {
                        this.graph.connect(src, dest, weight);
                        this.outputText += "\n New Edge added: src = " + src + ", dest = " + dest + ", weight = " + weight + ".";
                    }
                    else{
                        throw new FileAlreadyExistsException("");
                    }
                }
                else{
                    throw new NoSuchObjectException("");
                }
            }
            // entered non numeric values.
            catch (NumberFormatException ex){
                JOptionPane.showOptionDialog(null,
                        "Wrong input!\nPlease enter only numeric values.",
                        "INPUT ERROR!",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.ERROR_MESSAGE,
                        null,
                        null,
                        null);
                this.outputText += "\n Add Edge failed.";
            }
            // pressed X to cancel the dialog.
            catch (RuntimeException ex){
                this.outputText += "\n Add Edge canceled.";
            }
            // src/dest does not exist in the graph.
            catch (NoSuchObjectException ex) {
                JOptionPane.showOptionDialog(null,
                        """
                                Wrong input!
                                The Graph does not contain the Source/Destination or both of them
                                Nodes in order to add an Edge between them""",
                        "INPUT ERROR!",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.ERROR_MESSAGE,
                        null,
                        null,
                        null);
//                this.outputText += "\n The Graph does not contain the Edge between: " + src + " to " + dest + ".";
                this.outputText += "\n Add Edge failed.";
            }
            // The Graph already contains this Edge.
            catch (FileAlreadyExistsException ex) {
                JOptionPane.showOptionDialog(null,
                        "Wrong input!\nThe Graph already contains this Edge.",
                        "INPUT ERROR!",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.ERROR_MESSAGE,
                        null,
                        null,
                        null);
                this.outputText += "\n The Graph already contains this Edge.";
                this.outputText += "\n Add Edge failed.";
            }
        }

        else if (addNodeItem.equals(event) || AN.equals(event)) {
            this.outputText += "\n Add Node was activated.";
            JTextField xCoordination = new JTextField("  X  ");
            JTextField yCoordination = new JTextField("  Y  ");
            JTextField ID = new JTextField(" ID ");
            updateMouseListener(xCoordination);
            updateMouseListener(yCoordination);
            updateMouseListener(ID);
            String title = "Insert the X, Y, ID values to add to the new Node\nIn order to finish click on the OK button.\n";
            xCoordination.setToolTipText("Enter the X coordination");
            yCoordination.setToolTipText("Enter the y coordination");
            ID.setToolTipText("Enter the ID number");
            Object[] options = {title, "\n", xCoordination, yCoordination, ID};

            JOptionPane j = new JOptionPane();
            j.setMessage(options);
            j.setMessageType(JOptionPane.INFORMATION_MESSAGE);
            JDialog dialog = j.createDialog(null, "Add new Node");
            final boolean[] success = {false};
            dialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    dialog.dispose();
                    success[0] = true;
                }
            });
            dialog.setVisible(true);

            double x;
            double y;
            int id;
            try {
                if(success[0]){
                    throw new RuntimeException();
                }
                x = Double.parseDouble(xCoordination.getText());
                y = Double.parseDouble(yCoordination.getText());
                id = Integer.parseInt(ID.getText());
                NodeData newNode = new Node(x, y, id);
                this.graph.addNode(newNode);
                this.mainPanel.checkMin(newNode);
            }
            catch (NumberFormatException ex){
                JOptionPane.showOptionDialog(null,
                        "Wrong input!\nPlease enter only numeric values.",
                        "INPUT ERROR!",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.ERROR_MESSAGE,
                        null,
                        null,
                        null);
                this.outputText += "\n Add Node failed.";
            }
            catch (RuntimeException ex){
                this.outputText += "\n Add Node canceled.";
            }
        }

        else if (removeEdgeItem.equals(event) || RE.equals(event)) {
            this.outputText += "\n Remove Edge was activated.";
            JTextField srcText = new JTextField("Source");
            JTextField destText = new JTextField("Destination");
            updateMouseListener(srcText);
            updateMouseListener(destText);
            String title = """
                    Insert the Source Node and the Destination Node
                    values of the Edge you would like to delete.
                    In order to finish click on the OK button.
                    """;
            srcText.setToolTipText("Enter the Source Node");
            destText.setToolTipText("Enter the Destination Node");
            Object[] options = {title, "\n", srcText, destText};

            JOptionPane j = new JOptionPane();
            j.setMessage(options);
            j.setMessageType(JOptionPane.INFORMATION_MESSAGE);
            JDialog dialog = j.createDialog(null, "Remove Edge");
            final boolean[] success = {false};
            dialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    dialog.dispose();
                    success[0] = true;
                }
            });
            dialog.setVisible(true);

            int src;
            int dest;
            try {
                if (success[0]) {
                    throw new NoInitialContextException();
                }
                src = Integer.parseInt(srcText.getText());
                dest = Integer.parseInt(destText.getText());
                try {
                    this.graph.removeEdge(src, dest);
                    this.outputText += "\n Edge " + src + " to " + dest + ", was removed.";
                } catch (NullPointerException nep) {
                    JOptionPane.showOptionDialog(null,
                            "Wrong input!\nThe Graph does not contain\nthe Edge between: " + src + " to " + dest + ".",
                            "INPUT ERROR!",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.ERROR_MESSAGE,
                            null,
                            null,
                            null);
                    this.outputText += "\n The Graph does not contain the Edge between: " + src + " to " + dest + ".";
                    this.outputText += "\n Remove Edge failed.";
                }
            }
            catch (NoInitialContextException ex){
                this.outputText += "\n Remove Edge canceled.";
            }
            catch (NumberFormatException ex){
                JOptionPane.showOptionDialog(null,
                        "Wrong input!\nPlease enter only numeric values.",
                        "INPUT ERROR!",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.ERROR_MESSAGE,
                        null,
                        null,
                        null);
                this.outputText += "\n Remove Edge failed, invalid input.";
            }
        }

        else if (removeNodeItem.equals(event) || RN.equals(event)) {
            this.outputText += "\n Remove Node was activated";
            JTextField ID = new JTextField(" ID ");
            updateMouseListener(ID);
            String title = """
                    Insert the ID number of the Node
                    you would like to delete.
                    In order to finish click on the OK button.
                    """;
            ID.setToolTipText("Enter the ID number");
            Object[] options = {title, "\n", ID};

            JOptionPane j = new JOptionPane();
            j.setMessage(options);
            j.setMessageType(JOptionPane.INFORMATION_MESSAGE);
            JDialog dialog = j.createDialog(null, "Remove Node");
            final boolean[] success = {false};
            dialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    dialog.dispose();
                    success[0] = true;
                }
            });
            dialog.setVisible(true);

            int id;
            try {
                if (success[0]) {
                    throw new NoInitialContextException();
                }
                id = Integer.parseInt(ID.getText());
                try {
                    this.algo.getGraph().removeNode(id);
//                    this.graph.removeNode(id);
                }
                catch (NullPointerException nep){
                    JOptionPane.showOptionDialog(null,
                            "Wrong input!\nThe Graph does not contain\n Node: " + id + ".",
                            "INPUT ERROR!",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.ERROR_MESSAGE,
                            null,
                            null,
                            null);
                }
            }
            catch (NoInitialContextException ex){
                this.outputText += "\n Remove Node canceled.";
            }
            catch (NumberFormatException ex){
                JOptionPane.showOptionDialog(null,
                        "Wrong input!\nPlease enter only numeric values.",
                        "INPUT ERROR!",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.ERROR_MESSAGE,
                        null,
                        null,
                        null);
                this.outputText += "\n Remove Node failed, invalid input.";
            }
        }

        // ALGORITHMS

        else if (isConnectedItem.equals(event) || IC.equals(event)) {
            this.outputText += "\n isConnected Activated.";
            boolean ans = algo.isConnected();
            String output = ans? "This graph is strongly connected!" : "This graph is not strongly connected!";
            JOptionPane.showOptionDialog(null,
                    output,
                    "IsConnected",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    null,
                    null);
            this.outputText += "\n " + output;
        }

        else if (shortestPath.equals(event) || SP.equals(event)) {
            this.outputText += "\n ShortestPath Activated.";
            JTextField srcText = new JTextField("Source");
            JTextField destText = new JTextField("Destination");
            updateMouseListener(srcText);
            updateMouseListener(destText);
            String title = """
                    Insert the Source Node and the Destination
                    Node values of the Path you seek.
                    In order to finish click on the OK button.
                    """;
            srcText.setToolTipText("Enter the Source Node");
            destText.setToolTipText("Enter the Destination Node");
            Object[] options = {title, "\n", srcText, destText};

            JOptionPane j = new JOptionPane();
            j.setMessage(options);
            j.setMessageType(JOptionPane.INFORMATION_MESSAGE);
            JDialog dialog = j.createDialog(null, "Shortest Path");
            final boolean[] success = {false};
            dialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    dialog.dispose();
                    success[0] = true;
                }
            });
            dialog.setVisible(true);


            int src;
            int dest;
            try {
                if(success[0]){
                    throw new NoInitialContextException();
                }
                src = Integer.parseInt(srcText.getText());
                dest = Integer.parseInt(destText.getText());
                if(graph.getNode(src) != null) {
                    if (graph.getNode(dest) != null) {

                        double weight = algo.shortestPathDist(src, dest);
                        List<NodeData> path = this.algo.shortestPath(src, dest);
                        if(path == null || weight == -1){
                            throw new NumberFormatException();
                        }
                        this.mainPanel.setPath(path);
                        this.mainPanel.setDest(dest);
                        this.mainPanel.setSrc(src);
                        this.mainPanel.setPathActivated(true);
                        String route = "";
                        for (NodeData n : path) {
                            route += n.getKey() + "->";
                        }
                        route = "[" + route.substring(0, route.length() - 2) + "]";
                        this.outputText += "\n The weight of the shortest path between " + src + " -> " + dest + " is: " + weight + ".";
                        this.outputText += "\n The path is: " + route + ".";
                    }
                    else{
                        JOptionPane.showOptionDialog(null,
                                "Wrong input!\nThe destination entered does not appear in the Graph.",
                                "DESTINATION INPUT ERROR!",
                                JOptionPane.DEFAULT_OPTION,
                                JOptionPane.WARNING_MESSAGE,
                                null,
                                null,
                                null);
                        this.outputText += "\n ShortestPath failed, the destination is not in the Graph.";
                    }
                }

                else{
                    JOptionPane.showOptionDialog(null,
                            "Wrong input!\nThe source entered does not appear in the Graph.",
                            "SOURCE INPUT ERROR!",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.WARNING_MESSAGE,
                            null,
                            null,
                            null);
                    this.outputText += "\n ShortestPath failed, the source is not in the Graph.";
                }
            }
            catch (NoInitialContextException ex){
                this.outputText += "\n ShortestPath canceled.";
            }
            catch (NumberFormatException ex){
                JOptionPane.showOptionDialog(null,
                        "Wrong input!\nPlease enter only numeric values.",
                        "INPUT ERROR!",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.ERROR_MESSAGE,
                        null,
                        null,
                        null);
                this.outputText += "\n ShortestPath failed, invalid input.";
            }
        }

        else if (centerItem.equals(event) || CE.equals(event)) {
            this.mainPanel.setCenterActivated(true);
            this.mainPanel.setCenter(this.algo.center());
            this.outputText +=  "\n Center Activated";
            this.outputText +=  "\n The center of the Graph is Node number: " + this.mainPanel.getCenter().getKey();
            JOptionPane.showOptionDialog(null,
                    "The center of this Graph is Node number: " + this.mainPanel.getCenter().getKey() + ".\n" +
                            "The Node Color is Red and The index is colored in Yellow.",
                    "Center of Graph",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    null,
                    null);
            System.out.println(this.mainPanel.getCenter());
        }

        else if (TSPItem.equals(event) || TSP.equals(event)) {
            this.outputText += "\n TSP activated";
            int choose = chooseInputTSPState();
            List<NodeData> nodesToVisit = new LinkedList<>();
            List<NodeData> fullPath = new LinkedList<>();
            boolean citiesInitiated = false;
            final boolean[] success = {false};
            // Case 1 -> one long string.
            if(choose == 0){
                while (!citiesInitiated) {
                    String explanation = """
                            Enter a String of all the Nodes
                            you would like to check TSP Algorithm on.
                            Separate each Node with a single Space Key.""";
                    String input = JOptionPane.showInputDialog(explanation, "Enter the Nodes");
                    if (input == null || !input.matches("[0-9 ]+")) {
                        break;
                    }
                    String[] nodes = input.split(" ");
                    this.outputText += "\n The Cities for the TSP are:\n " + Arrays.toString(nodes);
                    for (String n : nodes) {
                        int ID = -1;
                        try {
                            ID = Integer.parseInt(n);
                            if (graph.getNode(ID) != null) {
                                nodesToVisit.add(this.graph.getNode(ID));
                            } else {
                                JOptionPane.showOptionDialog(null,
                                        "Wrong input!\nThe ID: " + n + " does not appear in the Graph.",
                                        "ID INPUT ERROR!",
                                        JOptionPane.DEFAULT_OPTION,
                                        JOptionPane.WARNING_MESSAGE,
                                        null,
                                        null,
                                        null);
                                this.outputText += "\n TSP failed, the ID: " + n + " does not appear in the Graph.";
                                break;
                            }
                        } catch (NumberFormatException ex) {
                            JOptionPane.showOptionDialog(null,
                                    "Wrong input!\nPlease enter only numeric values.",
                                    "INPUT ERROR!",
                                    JOptionPane.DEFAULT_OPTION,
                                    JOptionPane.ERROR_MESSAGE,
                                    null,
                                    null,
                                    null);
                            break;
                        }
                    }
                    if (nodesToVisit.size() == nodes.length) {
                        citiesInitiated = true;
                    }
                    fullPath = this.algo.tsp(nodesToVisit);
                    this.mainPanel.setPathByNodesTSP(fullPath);
                    this.mainPanel.setPathByNodesTSPActivated(true);
                }
            }

            // Case 2 -> each time one Node.
            else if(choose == 1) {
                JTextField input = new JTextField("Enter Node");
                updateMouseListener(input);
                Object[] options = {"Insert the first number(ID) of the Node you want to add:", input, "When finished type DONE"};
                JOptionPane j = new JOptionPane();
                j.setMessage(options);
                j.setMessageType(JOptionPane.INFORMATION_MESSAGE);
                JDialog dialog = j.createDialog(null, "Add Nodes to List");
//                JDialog finalDialog = dialog;
                JDialog finalDialog = dialog;
                dialog.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        finalDialog.dispose();
                        success[0] = true;
                    }
                });
                dialog.setVisible(true);
                System.out.println(success[0]);
                options[0] = "Insert the number(ID) of the Node you want to add:";
                String text = input.getText().toUpperCase(Locale.ROOT);
                String temp = "[";
                while(!text.equals("DONE")){
                    int ID = -1;
                    try{
                        if(success[0]) {
                            throw new NoInitialContextException();
                        }
                        ID = Integer.parseInt(input.getText());
                        if(graph.getNode(ID) != null){
                            nodesToVisit.add(this.graph.getNode(ID));
                            temp += ID + ", ";
                        }
                        else{
                            JOptionPane.showOptionDialog(null,
                                    "Wrong input!\nthe ID: " + ID + " does not appear in the Graph.",
                                    "ID INPUT ERROR!",
                                    JOptionPane.DEFAULT_OPTION,
                                    JOptionPane.WARNING_MESSAGE,
                                    null,
                                    null,
                                    null);
                        }
                    }
                    catch (NoInitialContextException ex){
                        this.outputText += "\n TSP canceled.";
                        break;
                    }
                    catch (NumberFormatException ex){
                        JOptionPane.showOptionDialog(null,
                                "Wrong input!\nPlease enter only numeric values.",
                                "INPUT ERROR!",
                                JOptionPane.DEFAULT_OPTION,
                                JOptionPane.ERROR_MESSAGE,
                                null,
                                null,
                                null);
                    }
                    input.setText("DONE");
                    input.setCaretPosition(0);
                    j = new JOptionPane();
                    j.setMessage(options);
                    j.setMessageType(JOptionPane.QUESTION_MESSAGE);
                    dialog = j.createDialog(null, "Add Nodes to List");
                    dialog.setVisible(true);
                    text = input.getText().toUpperCase(Locale.ROOT);
                }
                if(!success[0] && temp.length() > 2) {
                    temp = temp.substring(0, temp.length() - 2) + "]";
                    this.outputText += "\n The Cities for the TSP are:" + temp + ".";
                    fullPath = this.algo.tsp(nodesToVisit);
                    this.mainPanel.setPathByNodesTSP(fullPath);
                    this.mainPanel.setPathByNodesTSPActivated(true);
                }
            }
            if(!success[0] && fullPath != null && !fullPath.isEmpty()) {
                String route = "";
                for (NodeData n : fullPath) {
                    route += n.getKey() + "->";
                }
                route = "[" + route.substring(0, route.length() - 2) + "]";
                this.outputText += "\n " + "The path is:" + route;
            }
            if(nodesToVisit.size() == 0 && (choose == 0 || choose == 1) && !success[0]){
                this.outputText += "\n The list is invalid.";
            }
            else{
                if(nodesToVisit.size() == 0) {
                    this.outputText += "\n TSP canceled.";
                }
            }
        }
        else if (this.Brooks.equals(event) || this.BrooksItem.equals(event))
        {
            this.outputText += "\n Brooks coloring Activated.";
            this.brooksAlgo = new BrooksAlgo(graph);
            this.brooksAlgo.BrooksColoring();

            //setting the colors
            this.random_colors = new ArrayList<>();
            for (int i = 0; i < this.brooksAlgo.getNumberOfColors(); i++) {
                Random rand = new Random();
                Color randomColor = new Color(rand.nextInt(0xFFFFFF));
                this.random_colors.add(randomColor);
            }

            //update the colors on log window
            this.outputText += "\n" + "There are " + random_colors.size() + " colors";
            this.mainPanel.setRandomColors(this.random_colors);
            this.mainPanel.setBrooksActivated(true);
        }

        // Hungarian listener
        else if (this.Hungarian.equals(event) || this.HungarianItem.equals(event)) {
            if (!this.algo.is_bipartite()){
                JOptionPane.showOptionDialog(null,
                        "Please enter a Bipartite graph!",
                        "INVALID INPUT!",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.ERROR_MESSAGE,
                        null,
                        null,
                        null);
            }else {
                this.outputText += "\n Hungarian Max-Matching coloring.";
                this.algo.match.clear();
                this.algo.Hungarian();
                this.outputText += "\n" + "There are " + this.algo.match.size() + " edges in the match.";
                printEdgesToLog();
                this.mainPanel.setEdgesToColor(this.algo.match);
                this.mainPanel.setHungarianActivated(true);
                repaint();
            }
        }

        else if (this.stepByStep.equals(event)){
            if (!this.algo.is_bipartite()){
                JOptionPane.showOptionDialog(null,
                        "Please enter a Bipartite graph!",
                        "INVALID INPUT!",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.ERROR_MESSAGE,
                        null,
                        null,
                        null);
            }else {
                this.outputText += "\nClick this button to see the augmenting path each time.";
                if (this.algo.maxMatch() != null) {
                    printEdgesToLog();
                    this.mainPanel.setEdgesToColor(this.algo.match);
                    this.mainPanel.setHungarianActivated(true);
                    repaint();
                } else {
                    JOptionPane.showOptionDialog(null,
                            "There are no more steps!",
                            "DONE!",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.ERROR_MESSAGE,
                            null,
                            null,
                            null);
                    this.outputText += "\n" + "There are " + this.algo.match.size() + " edges in the match.";
                }
            }
        }

        // Edmonds Blossom listener
        else if (this.Edmonds.equals(event) || this.EdmondsItem.equals(event)) {
            EdmondsAlgo edmondsAlgo = new EdmondsAlgo(graph);
            edmondsAlgo.update_match();
            this.algo.match = edmondsAlgo.get_match();
            this.outputText += "\nEdmonds Blossom Max-Matching coloring.";
            this.outputText += "\n" + "There are " + this.algo.match.size() + " edges in the match.";
            printEdgesToLog();
            this.mainPanel.setEdgesToColor(this.algo.match);
            this.mainPanel.setEdmondActivated(true);
            //repaint();
        }

        // VIEW

        else if (hideButtonsItem.equals(event)) {
            this.buttonsPanel.setVisible(false);
        }

        else if (showButtonsItem.equals(event)) {
            this.buttonsPanel.setVisible(true);
        }

        else if (fullScreenItem.equals(event)) {
            Dimension scale = Toolkit.getDefaultToolkit().getScreenSize();
            this.width = scale.width;
            this.height = scale.height;
            this.setPreferredSize(new Dimension(this.width, this.height));
            this.setResizable(true);
            this.outputText += "\n Scale changed to Full Screen.";

        }

        else if (defaultItem.equals(event)) {
            Dimension scale = Toolkit.getDefaultToolkit().getScreenSize();
            this.width = (int) (scale.width * 0.75);
            this.height = (int) (scale.height * 0.9);
            this.setPreferredSize(new Dimension(this.width, this.height));
            this.setResizable(true);
            this.outputText += "\n Scale changed back to default.";
        }

        else if (customScaleItem.equals(event)) {
            JTextField widthText = new JTextField("Width");
            JTextField heightText = new JTextField("Height");
            updateMouseListener(widthText);
            updateMouseListener(heightText);
            String title = """
                    Insert two numbers which will represents a percentage values
                    of the Width and Height of the new scale.
                    The numbers must be between 1-100.
                    In order to finish click on the OK button.
                    """;
            widthText.setToolTipText("Enter the Width (between 1-100)");
            heightText.setToolTipText("Enter the Height (between 1-100)");
            Object[] options = {title, "\n", widthText, heightText};

            JOptionPane j = new JOptionPane();
            j.setMessage(options);
            j.setMessageType(JOptionPane.INFORMATION_MESSAGE);
            JDialog dialog = j.createDialog(null, "Change scale");
            final boolean[] success = {false};
            dialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    dialog.dispose();
                    success[0] = true;
                }
            });
            dialog.setVisible(true);

            double width;
            double height;
            try {
                if (success[0]) {
                    throw new NoInitialContextException();
                }
                width = Double.parseDouble(widthText.getText());
                height = Double.parseDouble(heightText.getText());
                if (width < 1 || width > 100 || height < 1 || height > 100) {
                    throw new NumberFormatException();
                }
                Dimension scale = Toolkit.getDefaultToolkit().getScreenSize();
                this.width = (int) (scale.width * (width / 100));
                this.height = (int) (scale.height * (height / 100));
                this.setPreferredSize(new Dimension(this.width, this.height));
                this.setResizable(true);
                this.outputText += "\n Scale changed to custom scale.";
            } catch (NoInitialContextException ex) {
                this.outputText += "\n Scale change canceled.";
            } catch (NumberFormatException ex) {
                JOptionPane.showOptionDialog(null,
                        "Wrong input!\nPlease enter only numeric values.",
                        "INPUT ERROR!",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.ERROR_MESSAGE,
                        null,
                        null,
                        null);
                this.outputText += "\n Scale change failed, invalid input.";
            }
        }

        // HELP

        else if(shortcutsItem.equals(event)){
            new Help();
        }


        this.pack();
        this.setLocationRelativeTo(null);
        repaint();
        updateTerminal();
    }

    public ArrayList<Color> getRandomColors() {
        return this.random_colors;
    }

    private void updateMouseListener(JTextField field){
        field.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ((JTextField) e.getSource()).setText("");
                super.mouseClicked(e);
            }
        });
    }

    private void printEdgesToLog()
    {
        this.outputText += "\n";
        HashSet<String> edgeStrHash = new HashSet<>();
        for (EdgeData edge : this.algo.match)
        {
            int src = Math.min(edge.getSrc(), edge.getDest());
            int dest = Math.max(edge.getSrc(), edge.getDest());
            String strReg = String.format("%d  ======  %d", src, dest);
            edgeStrHash.add(strReg);
        }
        for (String s: edgeStrHash)
        {
            this.outputText += s + "\n";
        }
    }


    public void setOutputText(String outputText) {
        this.outputText = outputText;
    }


    private int chooseInputTSPState(){
        String[] options = {"One String input", "Each time one String"};
        String message = """
                Choose how you would like to type the List:
                one String input = you will write the list as a single String with spaces between each Node.
                each time one string = you will write each time one city until finished.
                """;
        int ans = JOptionPane.showOptionDialog(null,
                message,
                "CHOOSE HOW TO INPUT THE TSP LIST",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                null);
        return ans;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
