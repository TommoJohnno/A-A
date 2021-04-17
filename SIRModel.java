import java.io.PrintWriter;
/**
 * SIR model.
 *
 * @author Jeffrey Chan, 2021.
 */
public class SIRModel
{

    // ContactsGraph graph_2;
    // String[] seedvertices_2;
    // float infectionProb_2;
    // float recoverProb_2;
    int iterations;
    String vertices[];
    String infected[];


    /**
     * Default constructor, modify as needed.
     */
    public SIRModel() {
        // graph_2 = new ContactsGraph();
        // seedvertices_2 = String[0];
        // infectionProb_2 = 0;
        // recoverProb_2 = 0;
        infected = new String[0];
        vertices = new String[0];
        iterations = 0;
    } // end of SIRModel()


    /**
     * Run the SIR epidemic model to completion, i.e., until no more changes to the states of the vertices for a whole iteration.
     *
     * @param graph Input contracts graph.
     * @param seedVertices Set of seed, infected vertices.
     * @param infectionProb Probability of infection.
     * @param recoverProb Probability that a vertex can become recovered.
     * @param sirModelOutWriter PrintWriter to output the necessary information per iteration (see specs for details).
     */
    public void runSimulation(ContactsGraph graph, String[] seedVertices,
        float infectionProb, float recoverProb, PrintWriter sirModelOutWriter)
    {
        // IMPLEMENT ME!
    //What I need, vertices which is equal to all vertices made
    //infected which includes all infected vertices
    //newly infected which contains all the newly infected vertices
    //newly recovered which contains all the newly recovered vertices
    //while(vertices == infected) {
    // if(vertices == infected) {
        sirModelOutWriter.print('[');
        sirModelOutWriter.print('G'); //change to newly infected vertices
        sirModelOutWriter.print(']');
        sirModelOutWriter.print(' ');
        sirModelOutWriter.print(':');
        sirModelOutWriter.print(' ');
        sirModelOutWriter.print('[');
        sirModelOutWriter.print('G'); //change to newly recovered vertices
        sirModelOutWriter.println(']');
    //}
    //}
    } // end of runSimulation()
} // end of class SIRModel
