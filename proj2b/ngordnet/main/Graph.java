package ngordnet.main;

import org.antlr.v4.runtime.tree.Tree;

import java.util.*;

public class Graph {
    private HashMap<Integer, TreeSet<Integer>> graph;
    public Graph() {
        graph = new HashMap<>();
    }

    public void addNode(int id) {
        TreeSet<Integer> initial = new TreeSet<>();
        graph.put(id, initial);
    }

    public void addEdge(int id, int adj) {
        graph.get(id).add(adj);
    }

    public TreeSet<Integer> getNeighbors(int id) {
        return graph.get(id);
    }

    public boolean hasNode(int id) {
        return graph.containsKey(id);
    }

    public TreeSet<Integer> traversal(int id) {
        TreeSet<Integer> result = new TreeSet<>();
        result.add(id);
        return helperTraversal(id, result);
    }

    private TreeSet<Integer> helperTraversal(int id, TreeSet<Integer> result) {
        for (int adj : getNeighbors(id)) {
            result.add(adj);
            result = helperTraversal(adj, result);
        }
        return result;
    }
}
