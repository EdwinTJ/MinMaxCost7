import java.io.File;
import java.util.*;

public class Graph {
    private int vertexCt;  // Number of vertices in the graph.
    private int[][] capacity;  // Adjacency matrix
    private int[][] residual; // residual matrix
    private int[][] edgeCost; // cost of edges in the matrix
    private String graphName;  //The file from which the graph was created.
    private int[][] totalFlow; // total achieved flow
    private int source = 0; // start of all paths
    private int sink; // end of all paths

    private int[] cost;       // Cost to reach each vertex from the source
    private int[] pred;       // Predecessors of each vertex in the path
    private List<String> paths = new ArrayList<>(); // List of paths taken

    public Graph(String fileName) {
        this.vertexCt = 0;
        source = 0;
        this.graphName = fileName;
        makeGraph(fileName);
    }

    private boolean addEdge(int source, int destination, int cap, int weight) {
        if (source < 0 || source >= vertexCt || destination < 0 || destination >= vertexCt) {
            return false;
        }
        capacity[source][destination] = cap;
        residual[source][destination] = cap;
        edgeCost[source][destination] = weight;
        edgeCost[destination][source] = -weight;
        return true;
    }

    private void makeGraph(String filename) {
        try {
            System.out.println("\n****Find Flow " + filename);
            Scanner reader = new Scanner(new File(filename));
            vertexCt = reader.nextInt();
            capacity = new int[vertexCt][vertexCt];
            residual = new int[vertexCt][vertexCt];
            edgeCost = new int[vertexCt][vertexCt];
            totalFlow = new int[vertexCt][vertexCt];

            while (reader.hasNextInt()) {
                int v1 = reader.nextInt();
                int v2 = reader.nextInt();
                int cap = reader.nextInt();
                int weight = reader.nextInt();
                addEdge(v1, v2, cap, weight);
            }
            reader.close();
            sink = vertexCt - 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void minCostMaxFlow() {
        while (hasAugmentingCheapestPath()) {
            augmentFlow();

        }
        System.out.println("Paths found in order (" + graphName + ")");
        for (String path : paths) {
            System.out.println(path);
        }
    }

    private boolean hasAugmentingCheapestPath() {
        pred = new int[vertexCt];
        cost = new int[vertexCt];
        Arrays.fill(cost, Integer.MAX_VALUE);
        cost[source] = 0;
        Arrays.fill(pred, -1);

        for (int i = 0; i < vertexCt - 1; i++) {
            for (int u = 0; u < vertexCt; u++) {
                for (int v = 0; v < vertexCt; v++) {
                    if (residual[u][v] > 0 && cost[u] != Integer.MAX_VALUE && cost[u] + edgeCost[u][v] < cost[v]) {
                        cost[v] = cost[u] + edgeCost[u][v];
                        pred[v] = u;
                    }
                }
            }
        }
        return pred[sink] != -1;
    }

    private void augmentFlow() {
        int path_flow = Integer.MAX_VALUE;
        int total_cost = 0;

        for (int v = sink; v != source; v = pred[v]) {
            int u = pred[v];
            path_flow = Math.min(path_flow, residual[u][v]);
            total_cost += edgeCost[u][v];
        }

        for (int v = sink; v != source; v = pred[v]) {
            int u = pred[v];
            residual[u][v] -= path_flow;
            residual[v][u] += path_flow;
            totalFlow [u][v] += path_flow;

        }

        // Record the path
        List<Integer> path = new ArrayList<>();
        for (int v = sink; v != source; v = pred[v]) {
            path.add(v);
        }
        path.add(source);
        Collections.reverse(path);
        paths.add(path.toString() + "(" + path_flow + ") $" + total_cost);
    }

    public static void main(String[] args) {
        String[] files = {"transport0.txt"};
        for (String fileName : files) {
            Graph graph = new Graph(fileName);
            graph.minCostMaxFlow();
        }
    }
}
