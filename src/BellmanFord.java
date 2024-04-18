import java.io.*;
import java.util.*;
class Edge {
    int src, dest, weight;

    Edge(int src, int dest, int weight) {
        this.src = src;
        this.dest = dest;
        this.weight = weight;
    }
}
public class BellmanFord {
    private List<Edge> edges;
    private int vertices;

    public BellmanFord(String fileName) {
        edges = new ArrayList<>();
        readGraph(fileName);
    }

    private void readGraph(String fileName) {
        try (Scanner scanner = new Scanner(new File(fileName))) {
            if (scanner.hasNextInt()) {
                vertices = scanner.nextInt();
            }
            while (scanner.hasNextInt()) {
                int src = scanner.nextInt();
                int dest = scanner.nextInt();
                int capacity = scanner.nextInt();  // Read but ignore for now
                int weight = scanner.nextInt();
                edges.add(new Edge(src, dest, weight));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void bellmanFord(int src) {
        int[] dist = new int[vertices];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[src] = 0;

        for (int i = 1; i < vertices; i++) {
            for (Edge edge : edges) {
                if (dist[edge.src] != Integer.MAX_VALUE && dist[edge.src] + edge.weight < dist[edge.dest]) {
                    dist[edge.dest] = dist[edge.src] + edge.weight;
                }
            }
        }

        for (Edge edge : edges) {
            if (dist[edge.src] != Integer.MAX_VALUE && dist[edge.src] + edge.weight < dist[edge.dest]) {
                System.out.println("Graph contains a negative weight cycle");
                return;
            }
        }

        printArr(dist, vertices);
    }

    private void printArr(int[] dist, int vertices) {
        System.out.println("Vertex Distance from Source");
        for (int i = 0; i < vertices; i++) {
            System.out.println(i + "\t\t" + dist[i]);
        }
    }

    public static void main(String[] args) {
        BellmanFord graph = new BellmanFord("transport0.txt");
        graph.bellmanFord(0);
    }
}