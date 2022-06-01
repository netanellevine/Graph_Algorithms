# Weighted Directed Graphs

## Assignment 2 - Object Oriented Programming

**Netanel Levine**<br>
**Yanir Cohen**
--- 

## About

**Definition:**
A graph is a structure that is made up of **vertices** (also called nodes or points)
which are connected by **edges** (also called links or lines). A distinction is made between undirected graphs, where
edges link two vertices symmetrically, and directed graphs, where edges link two vertices
asymmetrically. <a href="https://en.wikipedia.org/wiki/Graph_theory">see more on Graph Theory - Wikipedia</a><br>
<br>In this project we will illustrate to the best of our capabilities a ***Directed Weighted Graph*** and some familiar
Graphs Algorithms. <br>
<br>The graph resembles a small area with significant places and ways to transport between each place.  
This can be used for a variety of purposes (best route between cities,routes etc.).
<br>In our project we will try to simulate it as closely as can be to a small area as we said before.

---

## Overview

In this project we were asked to do the following things:  

***Note: each one of the names here is a class/interface/test in our project and of the references is a link to the implementations.***

1. Implement the following Interfaces:
    1. GeoLocation [^1.1]
    2. EdgeData [^1.2]
    3. NodeData [^1.3]
    4. DirectedWeightedGraph [^1.4]
    5. DirectedWeightedGraphAlgorithms [^1.5]

2. Implement a class to each of the Interfaces:
    1. Location [^2.1]
    2. Edge [^2.2]
    3. Node [^2.3]
    4. MyGraph [^2.4]
    5. MyGraphAlgo [^2.5]

3. Implement a test to each of the classes:
    1. LocationTest [^3.1]
    2. EdgeTest [^3.2]
    3. NodeTest [^3.3]
    4. MyGraphTest[^3.4]
    5. MyGraphAlgoTest[^3.5]
    6. GraphGenTest[^3.6]

4. Build a Graph from a Json file:
    1. ParseToGraph[^4.1]

5. Implement a GUI (graphics) that will show the Graph:
    1. MyGraph_GUI [^5.1]
    2. MyFrame [^5.2]
    3. MyPanel [^5.3]
    4. Help [^5.4]

   [^1.1]: <a href="https://github.com/netanellevine/Weighted_Graph_Algorithms/blob/main/src/main/java/api/GeoLocation.java">GeoLocation Interface</a>
   [^1.2]: <a href="https://github.com/netanellevine/Weighted_Graph_Algorithms/blob/main/src/main/java/api/EdgeData.java">EdgeData Interface</a>
   [^1.3]: <a href="https://github.com/netanellevine/Weighted_Graph_Algorithms/blob/main/src/main/java/api/NodeData.java">NodeData Interface</a>
   [^1.4]: <a href="https://github.com/netanellevine/Weighted_Graph_Algorithms/blob/main/src/main/java/api/DirectedWeightedGraph.java">DirectedWeightedGraph Interface</a>
   [^1.5]: <a href="https://github.com/netanellevine/Weighted_Graph_Algorithms/blob/main/src/main/java/api/DirectedWeightedGraphAlgorithms.java">DirectedWeightedGraphAlgorithms Interface</a>
   [^2.1]: <a href="https://github.com/netanellevine/Weighted_Graph_Algorithms/blob/main/src/main/java/api/Location.java">Location Class</a>
   [^2.2]: <a href="https://github.com/netanellevine/Weighted_Graph_Algorithms/blob/main/src/main/java/api/Edge.java">Edge Class</a>
   [^2.3]: <a href="https://github.com/netanellevine/Weighted_Graph_Algorithms/blob/main/src/main/java/api/Node.java">Node Class</a>
   [^2.4]: <a href="https://github.com/netanellevine/Weighted_Graph_Algorithms/blob/main/src/main/java/api/MyGraph.java">MyGraph Class</a>
   [^2.5]: <a href="https://github.com/netanellevine/Weighted_Graph_Algorithms/blob/main/src/main/java/api/MyGraphAlgo.java">MyGraphAlgo Class</a>
   [^3.1]: <a href="https://github.com/netanellevine/Weighted_Graph_Algorithms/blob/main/src/test/java/api/LocationTest.java">LocationTest</a>
   [^3.2]: <a href="https://github.com/netanellevine/Weighted_Graph_Algorithms/blob/main/src/test/java/api/EdgeTest.java">EdgeTest</a>
   [^3.3]: <a href="https://github.com/netanellevine/Weighted_Graph_Algorithms/blob/main/src/test/java/api/NodeTest.java">NodeTest</a>
   [^3.4]: <a href="https://github.com/netanellevine/Weighted_Graph_Algorithms/blob/main/src/test/java/api/MyGraphTest.java">MyGraphTest</a>
   [^3.5]: <a href="https://github.com/netanellevine/Weighted_Graph_Algorithms/blob/main/src/test/java/api/MyGraphAlgoTest.java">MyGraphAlgoTest</a>
   [^3.6]: <a href="https://github.com/netanellevine/Weighted_Graph_Algorithms/blob/main/src/test/java/api/graphGenTest.java">GraphGenTest</a>
   [^4.1]: <a href="https://github.com/netanellevine/Weighted_Graph_Algorithms/blob/main/src/main/java/api/ParseToGraph.java">ParseToGraph Class</a>
   [^5.1]: <a href="https://github.com/netanellevine/Weighted_Graph_Algorithms/blob/main/src/main/java/GUI/MyGraph_GUI.java">MyGraph_GUI Class</a>
   [^5.2]: <a href="https://github.com/netanellevine/Weighted_Graph_Algorithms/blob/main/src/main/java/GUI/MyFrame.java">MyFrame Class</a>
   [^5.3]: <a href="https://github.com/netanellevine/Weighted_Graph_Algorithms/blob/main/src/main/java/GUI/MyPanel.java">MyPanel Class</a>
   [^5.4]: <a href="https://github.com/netanellevine/Weighted_Graph_Algorithms/blob/main/src/main/java/GUI/Help.java">Help Class</a>

---

[jump to GUI](#gui---graphics-explanation)<br>
[jump to UML](#Project-UML)<br>
[jump to How to use instructions](#How-to-use)<br>
[jump to Sources](#Sources)<br>

---

## Structure

**Our project structure:**

1. Node - Node represents a significant place in Graphs i.e. building,market etc. Each node holds information
   regarding itself such as: street name, tag etc. 
<br>In this project each Node holds his 2d coordinates and an ID.
2. Location - GeoLocation represents the real life (latitude and longitude).
3. Edge -  Edge resembles a way of transport (road,sidewalk etc.) between two nodes.  
   Edges contain information as well for example road 4 ,etc. Not all edges are two-way edges. Because there are one way
   roads. In addition, the cost of each edge is not the same for similar reasons.

### Graph:

Graph is built upon founded on those. Graph contains all the nodes and all the edges in a given place, nodes can be
added and removed, same goes for edges.  
Through it, we will represent a small area with places of significance and a way to modify them quickly providing
elasticity.

### Algorithms:

Algorithms are the main event over here.  
As can be seen, this graph can represent real life places.  
As so, we would want the best(shortest/chipset) way from a Node to another Node, this will be done by calculating the best way through each edges from each
node. This will be done with the Shortest path Algorithm.<br>  
In addition, we would like to know the time of each trip, as for we can retrieve the cost of the way from one node to
another. This will be done with The Shortest path dist Algorithm.<br>  
Moreover, we would like to know the best way between one node with a few stops on the way. This will be done through the
best effort TSP Algorithm.<br>  
At last, we would like to know the center of the place, which place is probably closest to all. This will be done through
Center Algorithm.<br>  
Those are the main Algorithms we will use to represent as closely as possible real life situations.

### Graphics visualisation:

Finally, we would like a visualisation way to show the users the graph, edges, the best roads etc.<br>
As a result we created user interface which will visualize the graph and algorithms mentioned above.  
It will display the nodes and edges between all the nodes. Algorithms will be easily activated to illustrate the
situations.  
This will be a **user-friendly** gui which aspires to be easy to use, adjustable and illustrate our project as good as we
can.<br>
In conclusion, you will be able to see the way and understand much better what is the best result for your request.

### Results
Here are the results of the algorithm on a connected graph.  
The left column has the function we activated. The first row has the number of nodes.  
| | **1000/20000** | **10000/200000** | **100000/2000000** | **1000000/200000000** |  
|-----------|--------|--------|--------|--------|  
|**is Connected**| 0.01s | 0.2s | 5-17s | NULL |  
|**TSP 10 Nodes** |0.02s | 0.5-1s | 1-10s | NULL |  
|**Center** |2s | 5.5m | NULL | NULL |  
|**Shortest Path** |0.02s | 0.04s | 5s | NULL |

We are able to generate a graph with 1mk nodes and 20mk edges but are unable to run any algorithm on it, due to heap
space. The results vary depends on how th graph is generated.

Since we did not create the graph randomly most of the time, it didn't take so much time.

---

## GUI - Graphics explanation

Implementing the GUI was a major part (and not easy at all) of this project because we wanted to give the user the Max
comfort and simplicity that we could think of.

### Structure

the GUI contains 4 classes:

1. **MyFrame** - MyFrame is the main part of this section, basically it holds all the components of the programs and
   creating them.

2. **MyPanel** - MyPanel is where the magic happen, MyPanel is the main panel that holds all the graph structure and
   responsible for drawing it correctly.

3. **Help** - Help is a small class that creates only a new JFrame to the main frame. The purpose of Help is to open a
   new window that contains all the shortcuts available.

4. **MyGraph_GUI** - MyGraph_GUI creates the main JFrame and then activating the GUI.

### Tutorial

Here we attached a simple image of the GUI.<br>
**The frame of the GUI has 4 main parts:**

1. **Menu Bar**
2. **Buttons Panel**
3. **The Graph**
4. **Action Log**

<img align="center"  src="https://github.com/netanellevine/Weighted_Graph_Algorithms/blob/main/ReadMePics/InitIMG.jpg">

### Explanation of the parts

1. **Menu Bar/Buttons Panel** - From them the user can execute all kind of features.  
   Not all the features in the Menu Bar appears in the Buttons Panel, but all the features in Buttons Panel appears in
   the Menu Bar.

   **Features list:**
   <br><br>***Note: the obvious features doesn't have an explanation/examples because their pretty simple.***<br>
    1. **Algorithms:**<br>
        - isConnected *// Algorithms Bar and Buttons Panel*,  [jump to isConnected](#isConnected)
        - TSP *// Algorithms Bar and Buttons Panel*,  [jump to TSP](#TSP)
        - Center *// Algorithms Bar and Buttons Panel*,  [jump to Center](#Center)
        - ShortestPath *// Algorithms Bar and Buttons Panel*,  [jump to ShortestPath](#ShortestPath)
    2. **Edit Graph:**<br>
        - Remove Node *// Edit Bar and Buttons Panel*, [jump to Remove Node](#Remove-Node)
        - Remove Edge *// Edit Bar and Buttons Panel*, [jump to Remove Edge](#Remove-Edge)
        - Add Node *// Edit Bar and Buttons Panel*, [jump to Add Node](#Add-Node)
        - Add Edge *// Edit Bar and Buttons Panel*, [jump to Add Edge](#Add-Edge)
    3. **View settings:**<br>
        - Hide/Show Buttons *// View bar only*
        - Full Screen *// View bar only*
        - Default Screen *// View bar only*
        - Costume Screen *// View bar only*
    4. **Help:**<br>
        - Shortcuts *// Help bar only*, [jump to Shortcuts](#Shortcuts)
    5. **File:**<br>
        - Load *// File Bar and Buttons Panel*
        - Save *// File Bar and Buttons Panel*
        - Clear *// File Bar and Buttons Panel*
        - Reset Graph *// File Bar and Buttons Panel*
        - Exit *// File bar only*<br>


2. **The Graph** - The Graph holds all the relevant data in order to keep updating the drawings.
3. **Action Log** - The Action Log purpose is helping the user to control and monitor all the changes he did with the
   graph. Every Button click or action made in the program the Action Log writes it down, for example: if the user
   clicked on the Center button, so after he got an update through a popup message it's also written in the Action Log.

<br><br>

### isConnected:

<img align="left" width="75%" src="https://github.com/netanellevine/Weighted_Graph_Algorithms/blob/main/ReadMePics/1.png"> 

<br>After isConnected is pressed, a new popup window appears with the answer to the question:<br>  
***is the Graph strongly connected?***<br>
<br>[jump to GUI-Tutorial](#explanation-of-the-parts)<br><br><br><br><br><br><br><br><br><br><br><br><br>

### TSP:

<img align="left" width="75%" src="https://github.com/netanellevine/Weighted_Graph_Algorithms/blob/main/ReadMePics/2.png"> 

<br>In the TSP we decided to give the user two ways to type the input:

1. One long String.
2. Each time one String.
   <br><br><br><br><br><br><br><br><br><br><br><br>

<br><br>**One long String:**
<img align="left" width="75%" src="https://github.com/netanellevine/Weighted_Graph_Algorithms/blob/main/ReadMePics/22.png">

- This option allows the user to type all the input in one time without the need to type each time one Node.

* The String format is:  
  ***"5 3 9 12 15 7 0"***, <br>each Node is seperated with a single *space* character.
  <br><br><br><br><br><br><br><br><br>

**Each time one String:**
<img align="left" width="75%" src="https://github.com/netanellevine/Weighted_Graph_Algorithms/blob/main/ReadMePics/222.png">
<br>

- This option will create a new popup window that asks the user to enter another Node, this process will finish once the
  user type *"done"/"DONE"*.
  <br><br><br><br><br><br><br><br><br><br><br><br><br>

**Output:**
<img align="left" width="75%" src="https://github.com/netanellevine/Weighted_Graph_Algorithms/blob/main/ReadMePics/2222.png">

- The path will be colored in light purple on the Graph.
- At the action log the user can see:<br>
    1. List of the Nodes he entered.<br>
    2. List of the path in the right order that go through every one of the Nodes he entered.<br>
    3. In case of any Invalid input the user will receive a popup window mentioning the problem, and it will be written
       in the action log too.<br>
       <br>[jump to GUI-Tutorial](#explanation-of-the-parts)<br><br><br><br>

### Center:

<img align="left" width="75%" src="https://github.com/netanellevine/Weighted_Graph_Algorithms/blob/main/ReadMePics/3.png"> 

<br>Once the user pressed the Center 3 things will happen:<br>
<br>1. Popup window will appear with the ID of the Node that our Algorithm chose to be the Center.
<br><br><br><br><br><br><br><br><br>

<br><br><br>
<img align="left" width="75%" src="https://github.com/netanellevine/Weighted_Graph_Algorithms/blob/main/ReadMePics/33.jpg">
<br><br>2. The Node with this ID will change his color from blue to red, and also the white box where his ID written
will change to yellow.

<br>3. The action log will write also this Node as the Center.<br><br>[jump to GUI-Tutorial](#explanation-of-the-parts)
<br><br><br><br><br><br><br><br>

### ShortestPath:

<img align="left" width="75%" src="https://github.com/netanellevine/Weighted_Graph_Algorithms/blob/main/ReadMePics/4.png"> 

<br>Once the user pressed the ShortestPath 3 things will happen:<br>

1. Popup window will open and ask the user to enter source Node and destination Node
2. The ShortestPath that our Algorithm chose will be marked in green.
3. The action log will write 2 things:
    1. Weight of the ShortestPath.
    2. String represents the ShortestPath in the right order.
       <br><br><br><br>

<img align="left" width="75%" src="https://github.com/netanellevine/Weighted_Graph_Algorithms/blob/main/ReadMePics/44.png">
<br>In case the user entered an invalid input, he will get a popup window mentioning what is the problem, and this will also be written in the log.<br>

**There are 2 types of wrong inputs:**

1. No input at all or String of chars, something that is not an Integer.<br>
2. source/destination/both are not in the Graph.<br>

[jump to GUI-Tutorial](#explanation-of-the-parts) <br><br><br><br><br><br><br>

### Remove Node:

<img align="left" width="75%" src="https://github.com/netanellevine/Weighted_Graph_Algorithms/blob/main/ReadMePics/5.jpg"> 

<br>Remove Node is simple, give me ID, and I'll delete the Node.  
After the user types the key the program checks if this Node is in the Graph, if true it deletes the Node, else meaning
wrong input.<br>

**Wrong values/inputs are:**

1. No input or a String.
2. ID that is not in the Graph.
   <br><br><br><br><br><br><br>

**Output:**  
<img align="left" width="75%" src="https://github.com/netanellevine/Weighted_Graph_Algorithms/blob/main/ReadMePics/55.png">
<br>If the input is valid the user will see that the Node he picked was removed from the Graph and all it's Edges
too.<br>
Otherwise, a popup window will appear with the cause written, and it will be added to the action log.<br>
After the Node was deleted, the Action log will write the Node number that was removed.<br>
<br>
[jump to GUI-Tutorial](#explanation-of-the-parts)<br><br><br><br><br><br><br>

### Remove Edge:

<img align="left" width="75%" src="https://github.com/netanellevine/Weighted_Graph_Algorithms/blob/main/ReadMePics/6.jpg">

<br>Remove Edge is similar to Remove Node only with 2 inputs.<br>
After the user types the source and the destination the program checks:

* if the source Node and the destination Node are Nodes in the Graph.<br>
* if there is an Edge between source to destination.<br>

if both of them are true the Edge will be deleted from the Graph.<br>
<br><br><br><br><br>


<img align="left" width="75%" src="https://github.com/netanellevine/Weighted_Graph_Algorithms/blob/main/ReadMePics/66.png">

<br>**Wrong values/inputs are:**

1. No input or a String.
2. source/destination that is not in the Graph or not connected.<br>
   <br>If the input is valid the user will see that the Edge he picked was removed from the Graph. Otherwise, a popup
   window will appear with the cause written, and it will be added to the action log. After the Edge was deleted, the
   Action log will write the details of the Edge that was deleted.<br>
   [jump to GUI-Tutorial](#explanation-of-the-parts)<br><br><br><br><br>

### Add Node:

<img align="left" width="75%" src="https://github.com/netanellevine/Weighted_Graph_Algorithms/blob/main/ReadMePics/7.png">

<br>In order to add a new Node to the Graph the user need to type x,y,id.<br>

* The (x,y) representing the location of the point on the Graph.
* The id represents an ID, each Node has an uniq ID.<br>

**valid inputs are:**

* x,y are numbers so any input that is not a pure number will cause an Invalid Input message.
* id is a number too, and it must be an uniq id.
  <br><br><br>

**Output:**  
<img align="left" width="75%" src="https://github.com/netanellevine/Weighted_Graph_Algorithms/blob/main/ReadMePics/77.jpg">
If the input was valid the user will see the new Node immediately on the Frame.<br>  
In addition, the Action Log that will write the details of the new Node.<br>
<br>[jump to GUI-Tutorial](#explanation-of-the-parts)<br><br><br><br><br><br><br><br><br><br><br><br><br>

### Add Edge:

<img align="left" width="75%" src="https://github.com/netanellevine/Weighted_Graph_Algorithms/blob/main/ReadMePics/8.png"> 

<br>In order to add a new Edge to the Graph the user need to type source and destination.<br>

* The source is the Node where the new Edge will begin.
* The destination is the Node where the new Edge will end.<br>
  **valid inputs are:**
* The source and the destination must be a Nodes that are existed in the Graph.
* The source and destination must be a numeric values.
  <br><br><br><br>

**Output:**  
<img align="left" width="75%" src="https://github.com/netanellevine/Weighted_Graph_Algorithms/blob/main/ReadMePics/88.jpg">

If the input was valid the user will see the new Edge immediately on the Frame.<br>  
In addition, the Action Log that will write the details of the new Edge.<br>
<br>[jump to GUI-Tutorial](#explanation-of-the-parts)<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>

### Shortcuts:

The shortcuts' purpose is one more way that we tried to make it easier on the user.
<br> By clicking the Shortcuts in the Help bar a new Frame will appear with a description of all the shortcuts available
in this program.<br>
By clicking BACK the user will return to the main Frame.<br>

[jump to GUI-Tutorial](#explanation-of-the-parts)<br>

<img align="center" src="https://github.com/netanellevine/Weighted_Graph_Algorithms/blob/main/ReadMePics/10.png">

<br>[back to top](#about)<br>

---

## Project UML

<img src="https://github.com/netanellevine/Weighted_Graph_Algorithms/blob/main/uml/Arcithecture.png">

<br>[back to top](#about)<br>

---

## How to use:

Let's begin with cloning the repository.

```
git clone https://github.com/netanellevine/Weighted_Graph_Algorithms.git && cd Weighted_Graph_Algorithms
```

Then we would like to run the jar to view the graph.  
For that we have few options:

**1.**  type/copy the blockquotes below as is.

- First, it loads an empty graph and will open an empty frame with all the GUI options.
- Second, now create a graph you desire or load one.

```
java -jar weighted-graphs.jar
```

**2.** type/copy the blockquotes below and add it to the end the line ***random***.

- Load a random generated graph (that we created already).
- The graph is loaded, play with all the features.

```
java -jar weighted-graphs.jar random
```

**3.** type/copy the blockquotes below and add an existing json_file in order to parse it into a graph.

- Load a graph with json file as a String.
- The graph is loaded, play with all the features.

```
java -jar weighted-graphs.jar <json-file>
```

In the data directory we have a few examples of some graph representation in json format. for example: **"
\Weighted_Graph_Algorithms\data\G1.json"**

<br>[back to top](#about)<br>

---

## Sources:

- <a href="https://www.youtube.com/watch?v=CerlT7tTZfY&t">Djikstra's algorithm using a priority queue - Youtube</a>
- <a href="https://www.geeksforgeeks.org/connectivity-in-a-directed-graph/">Check if a graph is strongly connected</a>
  <br>[back to top](#about)<br>
