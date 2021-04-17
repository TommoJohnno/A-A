import java.io.PrintWriter;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Incidence matrix implementation for the GraphInterface interface.
 *
 * Your task is to complete the implementation of this class. You may add
 * methods, but ensure your modified class compiles and runs.
 *
 * @author Jeffrey Chan, 2021.
 */
public class IncidenceMatrix extends AbstractGraph {

	private String[][] incidenceMatrix;
	private int numVertex;
	private int numEdges;
	private int initialSize = 1;

	private SIRState[] states = SIRState.values();
	private SIRState defaultState = SIRState.S;

	private HashMap<String, Integer> vertexIndex = new HashMap<>();
	private HashMap<String, Integer> edgeIndex = new HashMap<>();
	private HashMap<String, SIRState> sirState = new HashMap<>();

	private Queue<String> nodeQueue = new PriorityQueue<String>();
	private boolean visitedNodes[];
	private String result[];

	/**
	 * Constructs empty graph.
	 */
	public IncidenceMatrix() {

		numVertex = 0;
		numEdges = 0;

	} // end of IncidenceMatrix()

	public void addVertex(String vertLabel) {

		// If No Vertices Exist
		if (numVertex == 0) {

			// Increase Number of Vertices
			numVertex++; // 1

			// Initialize Matrix with New Edge
			incidenceMatrix = new String[numVertex + initialSize][initialSize];

			// Mark Corner of graph
			incidenceMatrix[0][0] = ".";

			// List Label
			incidenceMatrix[numVertex][0] = vertLabel.toUpperCase();

			// Store Location
			vertexIndex.put(vertLabel, numVertex);

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
				incidenceMatrix = reallocateArrayNewVertex(numVertex + initialSize, numEdges + initialSize);

				// List Label
				incidenceMatrix[numVertex][0] = vertLabel.toUpperCase();

				// Store Location
				vertexIndex.put(vertLabel, numVertex);

				// Initialize SIRState
				sirState.put(vertLabel, defaultState);

				printWholeGraph();

			} else {
				System.out.println("Not added. Vertex already exists.");
			}
		}

	} // end of addVertex()

	private String[][] reallocateArrayNewVertex(int rows, int columns) {

		// Matrix that can fit new vertex or edge
		String[][] matrixCopy = new String[rows][columns];

		// Loop through size of matrix prior to addition
		for (int i = 0; i < rows - 1; i++) {
			for (int j = 0; j < columns; j++) {
				matrixCopy[i][j] = incidenceMatrix[i][j];
			}
		}

		// Initialize New Vertex Column with Zeros
		for (int i = 1; i < columns; i++) {
			matrixCopy[rows - 1][i] = "0";
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

				// Build Edge Label
				String edgeLabel = srcLabel.concat(tarLabel);

				// Flipped Edge Label
				String flippedEdgeLabel = tarLabel.concat(srcLabel);

				// Check if Edge Already Exists
				if (edgeIndex.containsKey(edgeLabel) || edgeIndex.containsKey(flippedEdgeLabel)) {

					// Throw Error
					System.out.println("The edge already exists.");

				} else {

					// Increase Number of Vertices
					numEdges++;

					// Create New Sized Matrix with Past Values
					incidenceMatrix = reallocateArrayNewEdge(numVertex + initialSize, numEdges + initialSize);

					// List Label
					incidenceMatrix[0][numEdges] = edgeLabel.toUpperCase();

					// Store Location
					edgeIndex.put(edgeLabel, numEdges);

					// Get the Index of the Vertices Involved in the edge
					int sourceIndex = vertexIndex.get(srcLabel);
					int targetIndex = vertexIndex.get(tarLabel);

					// Set Edge Value at Appropriate Positions
					incidenceMatrix[sourceIndex][numEdges] = "1";
					incidenceMatrix[targetIndex][numEdges] = "1";

					printWholeGraph();
				}
			}
		}

	} // end of addEdge()

	private String[][] reallocateArrayNewEdge(int rows, int columns) {

		// Matrix that can fit new vertex or edge
		String[][] matrixCopy = new String[rows][columns];

		// Loop through size of matrix prior to addition
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns - 1; j++) {
				matrixCopy[i][j] = incidenceMatrix[i][j];
			}
		}

		// Initialize New Edge Column with Zeros
		for (int i = 1; i < rows; i++) {
			matrixCopy[i][columns - 1] = "0";
		}

		return matrixCopy;
	}

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

		} else {
			System.out.println("Vertex does not exist");
		}

	} // end of toggleVertexState()

	public void deleteEdge(String srcLabel, String tarLabel) {

		// Build Edge Label
		String edgeLabel = srcLabel.concat(tarLabel);

		// If Edge Exists
		if (edgeIndex.containsKey(edgeLabel) == true) {

			// Get Column From Map
			int desiredColumnIndex = edgeIndex.get(edgeLabel);

			// Reduce Needed Edges
			numEdges--;

			// Remove Contents of Edge Column
			incidenceMatrix = removeColumnContents(numVertex + initialSize, numEdges + initialSize, desiredColumnIndex);

			remapIndexes(edgeIndex, desiredColumnIndex);

			edgeIndex.remove(edgeLabel);

			printWholeGraph();

		} else {
			// Throw Error
			System.out.println("Edge Does not Exist");
		}

	} // end of deleteEdge()

	public void deleteVertex(String vertLabel) {

		// If Edge Exists
		if (vertexIndex.containsKey(vertLabel) == true) {

			// Get Row Index From Map
			int desiredRowIndex = vertexIndex.get(vertLabel);

			// int columns = numEdges + initialSize;

			for (int column = 1; column < numEdges + initialSize; column++) {

				System.out.println("scanned column: " + column);

				System.out.println(
						"incidenceMatrix[desiredRowIndex][column]: " + incidenceMatrix[desiredRowIndex][column]);

				// If Vertex Belongs in searched edge
				if (incidenceMatrix[desiredRowIndex][column] == "1") {

					System.out.println("deleted column: " + column);

					// Reduce Needed Edges
					numEdges--;

					// Find Edge Label of That to be Deleted
					String edgeLabel = incidenceMatrix[0][column];

					// Remove Contents of Edge Column
					incidenceMatrix = removeColumnContents(numVertex + initialSize, numEdges + initialSize, column);

					// Re-Map Indexes of Edges After the Deleted
					remapIndexes(edgeIndex, column);

					// Remove Edge from Map
					edgeIndex.remove(edgeLabel);

					// Decrement the index of the next column checked
					column--;
				}
			}

			numVertex--;

			// Remove Contents of Vertex Row
			incidenceMatrix = removeRowContents(numVertex + initialSize, numEdges + initialSize, desiredRowIndex);

			// Re-Map Indexes of Vertex After the Deleted
			remapIndexes(vertexIndex, desiredRowIndex);

			// Remove Edge from Map
			vertexIndex.remove(vertLabel);

			printWholeGraph();

		} else {
			System.out.println("Vertex does not exist");
		}

		// Implement me!
	} // end of deleteVertex()

	private void remapIndexes(HashMap<String, Integer> hashMap, int indexOfDeleted) {

		// For Each Vertex/Edge Mapped
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

	public String[] kHopNeighbours(int k, String vertLabel) {

		visitedNodes = new boolean[numVertex];
		result = new String[numVertex];


		return result;

	} // end of kHopNeighbours()

	public void printVertices(PrintWriter os) {

		for (String vertexLabel : vertexIndex.keySet()) {
			os.print('(');
			os.print(vertexLabel.toUpperCase());
			os.print(',');
			os.print(vertexIndex.get(vertexLabel));
			os.println(')');
		}

	} // end of printVertices()

	public void printEdges(PrintWriter os) {

		for (String edgeLabel : edgeIndex.keySet()) {
			os.print(edgeLabel.toUpperCase());
		}
	} // end of printEdges()

	private String[][] removeColumnContents(int rows, int columns, int desiredColumnIndex) {

		// New Array with Inputed Size
		String[][] matrixCopy = new String[rows][columns];

		// Copy Values Prior to Deleted Column
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < desiredColumnIndex; j++) {
				matrixCopy[i][j] = incidenceMatrix[i][j];
			}
		}

		// Copy Values after Deleted Column
		for (int i = 0; i < rows; i++) {
			for (int j = desiredColumnIndex; j < columns; j++) {
				matrixCopy[i][j] = incidenceMatrix[i][j + 1];
			}
		}

		return matrixCopy;
	}

	private String[][] removeRowContents(int rows, int columns, int desiredRowIndex) {

		// New Array with Inputed Size
		String[][] matrixCopy = new String[rows][columns];

		// Copy Values Prior to Deleted Row
		for (int i = 0; i < desiredRowIndex; i++) {
			for (int j = 0; j < columns; j++) {
				matrixCopy[i][j] = incidenceMatrix[i][j];
			}
		}

		// Copy Values after Deleted Row
		for (int i = desiredRowIndex; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				matrixCopy[i][j] = incidenceMatrix[i + 1][j];
			}
		}

		return matrixCopy;
	}

	private void printWholeGraph() {
		// Print Whole Graph
		for (int i = 0; i < numVertex + initialSize; i++) {

			System.out.print(incidenceMatrix[i][0] + " ");

			for (int j = 1; j < numEdges + initialSize; j++) {

				System.out.print(incidenceMatrix[i][j] + " ");

			}
			System.out.println(" ");
		}
	}

} // end of class IncidenceMatrix
