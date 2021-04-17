import java.io.PrintWriter;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Adjacency matrix implementation for the GraphInterface interface.
 *
 * Your task is to complete the implementation of this class. You may add
 * methods, but ensure your modified class compiles and runs.
 *
 * @author Jeffrey Chan, 2021.
 */
public class AdjacencyMatrix extends AbstractGraph {

	private HashMap<String, Integer> vertexIndex = new HashMap<String, Integer>();
	private HashMap<Integer, String> indexVertex = new HashMap<Integer, String>();
	private HashMap<String, SIRState> sirState = new HashMap<String, SIRState>();

	private int numVertex;
	private int[][] adjacencyMatrix;

    private long sum;

	private SIRState defaultState = SIRState.S;
	private SIRState[] states = SIRState.values();

	// * Constructs empty graph.
	// */
	public AdjacencyMatrix() {

		numVertex = 0;
        sum = 0;

	} // end of AdjacencyMatrix()

	public void addVertex(String vertLabel) {

        long startTime = System.nanoTime();

		// If No Vertices Exist
		if (numVertex == 0) {

			// Increase Number of Vertices
			numVertex++; // 1

			// Initialize Matrix with New Edge
			adjacencyMatrix = new int[numVertex][numVertex];

			// Store Location
			vertexIndex.put(vertLabel, numVertex - 1); // -1 to Get Index Position

			// Vertex Accessible By Index Position
			indexVertex.put(numVertex - 1, vertLabel);

			// Initialize SIRState
			sirState.put(vertLabel, defaultState);

			printWholeGraph();

		} else {

			// If Vertex is Unique
			if (vertexIndex.containsKey(vertLabel.toUpperCase()) == false
					&& vertexIndex.containsKey(vertLabel.toLowerCase()) == false) {

				// Increase Number of Vertices
				numVertex++;

				// Create New Sized Matrix with Past Values
				adjacencyMatrix = reallocateArray();

				// Store Location
				vertexIndex.put(vertLabel, numVertex - 1); // -1 to Get Index Position

				// Vertex Accessible By Index Position
				indexVertex.put(numVertex - 1, vertLabel);

				// Initialize SIRState
				sirState.put(vertLabel, defaultState);

				printWholeGraph();

			} else {
				System.out.println("Not added. Vertex already exists.");
			}
		}

        long endTime = System.nanoTime();

        sum = sum + (endTime - startTime);


            System.out.println(
                "time taken = " +
                ((double) sum) /
                Math.pow(10, 9) +
                " sec");


	} // end of addVertex()

	private int[][] reallocateArray() {

		// Matrix that can fit new vertex
		int[][] matrixCopy = new int[numVertex][numVertex];

		// Loop through size of matrix prior to addition
		for (int i = 0; i < numVertex - 1; i++) {
			for (int j = 0; j < numVertex - 1; j++) {
				matrixCopy[i][j] = adjacencyMatrix[i][j];
			}
		}

		return matrixCopy;
	}

	public void addEdge(String srcLabel, String tarLabel) {

		// Does not Exist 2 Vertices that can be Connected by Edge
		if (numVertex < 2) {

			// Throw Error
			System.out.println("The graph does not contain 2 vertices.");

		} else {

			// If Source or Target Vertex Does Not Exist
			if (!sourceTargetExists(srcLabel, tarLabel)) {

				// Throw Error
				System.out.println("One or both of the selected vertices do not exist.");

			} else {

				// Get Index Position of Target and Source Label
				int sourceIndex = vertexIndex.get(srcLabel);
				int targetIndex = vertexIndex.get(tarLabel);

				// Check if Edge Already Exists
				if (adjacencyMatrix[sourceIndex][targetIndex] == 1) {

					// Throw Error
					System.out.println("The edge already exists.");

				} else {

					// Set Edge Value at Appropriate Positions
					adjacencyMatrix[sourceIndex][targetIndex] = 1;
					adjacencyMatrix[targetIndex][sourceIndex] = 1;

					printWholeGraph();
				}
			}
		}
	} // end of addEdge()

	private boolean sourceTargetExists(String srcLabel, String tarLabel) {

		// If Source and Target Vertex Exists
		if (vertexIndex.containsKey(srcLabel) && vertexIndex.containsKey(tarLabel))
			return true;
		else
			return false;

	}

	public void toggleVertexState(String vertLabel) {

		// If Vertex Exists
		if (vertexIndex.containsKey(vertLabel)) {

			SIRState currentState = sirState.get(vertLabel);
			SIRState nextState;

			// If Vertex Has Not Recovered
			if (currentState != SIRState.R) {

				// Toggle to Next State
				nextState = states[(currentState.ordinal() + 1) % states.length];

				// Map Vertex to State
				sirState.put(vertLabel, nextState);
			}

			// Print State
			System.out.println(sirState.get(vertLabel));

		} else {
			System.out.println("Vertex does not exist");
		}

	} // end of toggleVertexState()

	public void deleteEdge(String srcLabel, String tarLabel) {

        long startTime = System.nanoTime();

		// Get Index Position of Target and Source Label
		int sourceIndex = vertexIndex.get(srcLabel);
		int targetIndex = vertexIndex.get(tarLabel);

		// If Edge Exists
		if (adjacencyMatrix[sourceIndex][targetIndex] == 1) {

			// Remove Edge
			adjacencyMatrix[sourceIndex][targetIndex] = 0;
			adjacencyMatrix[targetIndex][sourceIndex] = 0;

			printWholeGraph();

		} else {

			System.out.println("Edge does not exist.");

		}
        long endTime = System.nanoTime();

        System.out.println(
            "time taken = " +
            ((double) (endTime - startTime)) /
            Math.pow(10, 9) +
            " sec");



	} // end of deleteEdge()

	public void deleteVertex(String vertLabel) {

        long startTime = System.nanoTime();

		// If Vertex Exists
		if (vertexIndex.containsKey(vertLabel)) {

			// Get Index of to be Deleted Vertex
			int desiredIndex = vertexIndex.get(vertLabel);

			// Reduce Number of Vertices
			numVertex--;

			// Find Edges Involving Deleted Vertex
			for (int i = 0; i < numVertex; i++) {

				// If edge found
				if (adjacencyMatrix[desiredIndex][i] == 1)

					// Clear Edge
					adjacencyMatrix[i][desiredIndex] = 0;
			}

			// Remove Desired Vertex from Matrix
			adjacencyMatrix = removeContents(desiredIndex);

			// Remove Vertex from Vertex Map
			vertexIndex.remove(vertLabel);
			indexVertex.remove(desiredIndex);

			// Remove SIR State from Map
			sirState.remove(vertLabel);

			// Re-Map Indexes of Vertices After the Deleted
			remapIndexes(vertexIndex, desiredIndex);
			remapIndexesIntKeyMap(indexVertex, desiredIndex);

			printWholeGraph();

		} else {
			System.out.println("Vertex does not exist");
		}

        long endTime = System.nanoTime();

        System.out.println(
            "time taken = " +
            ((double) (endTime - startTime)) /
            Math.pow(10, 9) +
            " sec");

		// Implement me!
	} // end of deleteVertex()

	private void remapIndexes(HashMap<String, Integer> hashMap, int indexOfDeleted) {

		// For Each Mapped
		for (String hashKey : hashMap.keySet()) {

			// Get Index
			int index = hashMap.get(hashKey);

			if (index > indexOfDeleted) {

				index -= 1;

				// Decrease Index by 1 if found after deleted row
				hashMap.replace(hashKey, index);
			}
		}
	}

	private void remapIndexesIntKeyMap(HashMap<Integer, String> hashMap, int indexOfDeleted) {

		// For Each Mapped
		for (int hashKey : hashMap.keySet()) {

			// Get Vertex Label
			String label = hashMap.get(hashKey);

			if (hashKey > indexOfDeleted) {

				hashKey -= 1;

				// Decrease Index by 1 if found after deleted row
				hashMap.replace(hashKey, label);
			}

		}
	}

	private int[][] removeContents(int desiredVertexIndex) {

		// New Array with Inputed Size
		int[][] matrixCopy = new int[numVertex][numVertex];

		// Copy Values Prior to Deleted Vertex
		for (int i = 0; i < desiredVertexIndex; i++) {
			for (int j = 0; j < desiredVertexIndex; j++) {
				matrixCopy[i][j] = adjacencyMatrix[i][j];
			}
		}

		// Copy Values after Deleted Vertex
		for (int i = desiredVertexIndex; i < numVertex; i++) {
			for (int j = desiredVertexIndex; j < numVertex; j++) {
				matrixCopy[i][j] = adjacencyMatrix[i + 1][j + 1];
			}
		}

		return matrixCopy;
	}

	public String[] kHopNeighbours(int k, String vertLabel) {
		// Implement me!

		// please update!
		return null;
	} // end of kHopNeighbours()

	public void printVertices(PrintWriter os) {

		for (String vertexLabel : sirState.keySet()) {
			os.print('(');
			os.print(vertexLabel.toUpperCase());
			os.print(',');
			os.print(sirState.get(vertexLabel));
			os.println(')');
		}

	} // end of printVertices()

	public void printEdges(PrintWriter os) {

		int rowsToCheck = 1;

		for (int i = 1; i < numVertex; i++) {

			for (int j = 0; j < rowsToCheck; j++) {

				if (adjacencyMatrix[i][j] == 1) {

					// Find Vertex on Position i
					os.print(indexVertex.get(i).toUpperCase());
					os.print(" ");
					// Find Vertex on Position j
					os.print(indexVertex.get(j).toUpperCase());
					os.println();
				}
			}
			rowsToCheck++;

		}

	} // end of printEdges()

	private void printWholeGraph() {
		// Print Whole Graph
		for (int i = 0; i < numVertex; i++) {

			for (int j = 0; j < numVertex; j++) {

				System.out.print(adjacencyMatrix[i][j] + " ");

			}
			System.out.println(" ");
		}
	}

} // end of class AdjacencyMatrix

//   A  B  C
// A    1
// B 1
// C

//SIR states = [SIR!, SIR2, SIR3...]


// letter_dict = dict()

// letter_dict['A'] = 0
// letter_dict['B'] = 1

// # delete 'C'
// C_val = letter_dict['C']

// #loop through hashmap
// for key in letter_dict:
//     # 'A', 'B'...
//     current_key_value = letter_dict[key]
//     if current_key_value > C_val:
//         letter_dict[key] = current_key_value - 1
        
// add('F')

// #3
// ++current_node_idx;
// #4
// letter_dict[vert_label] = current_node_idx
    
// add_edge('A','F')

// value_first = letter_dict['A']
// value_second = letter_dict['F']

// adjacency_matrix[value_first][value_second] = 1
// adjacency_matrix[value_second][value_first] = 1
