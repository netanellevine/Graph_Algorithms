package GUI;

import javax.swing.*;
import java.awt.*;

public class Help {
    private final JFrame frame;
    private JButton back;
    private JPanel panel;
    private JLabel title;
    private JTextArea JTA;

    public Help() {
        this.frame = new JFrame("Shortcuts");
        this.panel = new JPanel();
        this.panel.setPreferredSize(new Dimension(600,35));
        this.JTA = new JTextArea();
        this.back = new JButton("BACK");
        this.back.addActionListener(e -> frame.dispose());
        this.panel.add(back);
        this.back.setVisible(true);
        this.panel.setVisible(true);
        try {
//            FileReader readTextFile = new FileReader("src/main/java/GUI/icons/ShortcutsFile.txt");
//
//            Scanner fileReaderScan = new Scanner(readTextFile);

            String storeAllString = "";

//            while (fileReaderScan.hasNextLine()) {
//                String temp = fileReaderScan.nextLine() + "\n";
//
//                storeAllString = storeAllString + temp;
//            }
            storeAllString = """
                     1 - File menu
                    	* Load a File                                            ALT+F+L
                    	* Save a File                                             ALT+F+S
                    	* Clear the Frame                                    ALT+F+C
                    	* Reset to the last Graph loaded            ALT+F+R
                    	* Exit the program                                  ALT+F+E
                    	
                     2 - Edit menu
                    	* Add new Node                                      ALT+E+A
                    	* Add new Edge                                      ALT+E+S
                    	* Remove  Node                                      ALT+E+D
                    	* Remove  Edge                                      ALT+E+F
                    	
                     3 - Algorithms menu
                    	* isConnected                                          ALT+A+I
                    	* ShortestPath                                         ALT+A+S
                    	* Center of the Graph                             ALT+A+C
                    	* TSP                                                       ALT+A+T
                    	
                     4 - View menu
                    	* Hide buttons bar                                  ALT+V+H
                    	* Show buttons bar                                  ALT+V+S
                    	* Set Full Screen                                      ALT+V+F
                    	* Set Default scale                                   ALT+V+D
                    	* Set Costume scale                                 ALT+V+C
                    """;
            this.title = new JLabel();
            this.title.setText("\s\s\s\s\s\s\s\s\s\s\s\s\s\s\s\s\sShortcuts Help");
            this.title.setLayout(new BorderLayout());
            this.title.setFont(new Font("Garamond", Font.BOLD, 40));
            this.title.setBackground(Color.darkGray);
            this.title.setVisible(true);

            this.JTA.setFont(new Font("Garamond", Font.BOLD, 19));
            this.JTA.setText(storeAllString);
            this.JTA.setLineWrap(true);
            this.JTA.setWrapStyleWord(true);

            ImageIcon img = new ImageIcon("src/main/java/GUI/icons/graph.jpg");
            this.frame.setIconImage(img.getImage());
            this.frame.add(this.title, BorderLayout.NORTH);
            this.frame.add(JTA, BorderLayout.CENTER);
            this.frame.add(panel, BorderLayout.PAGE_END);
            this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            this.frame.setSize(new Dimension(620, 685));
            this.frame.setLocationRelativeTo(null);
            this.frame.setResizable(false);
            this.frame.setVisible(true);
            this.frame.setAlwaysOnTop(true);

        } catch (Exception exception) {
            //Print Error in file processing if it can't process your text file
            System.out.println("Error in file processing");
        }
    }
}
