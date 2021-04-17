import java.io.PrintWriter;
import java.util.*;


/**
 * Adjacency list implementation for the AssociationGraph interface.
 *
 * Your task is to complete the implementation of this class.  You may add methods, but ensure your modified class compiles and runs.
 *
 * @author Jeffrey Chan, 2019.
 */
public class AdjacencyList extends AbstractGraph
{

    /**
	 * Contructs empty graph.
	 */

    // String[] adjlist;
    // int current_node_index;
    // Node next;
    // String data;

    public AdjacencyList() {
        // Implement me!
        // current_node_index = 0;
        // adjlist = new String[current_node_index];
        // next = null;
    }
     // end of AdjacencyList()


    public void addVertex(String vertLabel) {
        // Implement me!
    // Node head;
    // head.data = vertLabel;
    // for (int i = 0; i < current_node_index; ++i) {
    //     adjlist[i] = head.data;
    // }

    // ++current_node_index;


    } // end of addVertex()


    public void addEdge(String srcLabel, String tarLabel) {
        // Implement me!
        //int that is the source label position
        //use that to find which element of the array to add the head and next
        // head.data = srcLabel;
        // head.next.data = tarLabel;

    } // end of addEdge()


    public void toggleVertexState(String vertLabel) {
        // Implement me!
    } // end of toggleVertexState()


    public void deleteEdge(String srcLabel, String tarLabel) {
        // Implement me!
    } // end of deleteEdge()


    public void deleteVertex(String vertLabel) {
        // Implement me!
    } // end of deleteVertex()


    public String[] kHopNeighbours(int k, String vertLabel) {
        // Implement me!

        // please update!
        return null;
    } // end of kHopNeighbours()


    public void printVertices(PrintWriter os) {
        // Implement me!
    } // end of printVertices()


    public void printEdges(PrintWriter os) {
        // Implement me!
    } // end of printEdges()

} // end of class AdjacencyList
