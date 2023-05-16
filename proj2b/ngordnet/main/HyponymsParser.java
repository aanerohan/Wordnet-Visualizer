package ngordnet.main;

import edu.princeton.cs.algs4.In;
import org.antlr.v4.runtime.tree.Tree;

import java.util.HashMap;
import java.util.TreeSet;

public class HyponymsParser {
    private Graph graph;
    private HashMap<Integer, String[]> map;
    private HashMap<String, TreeSet<Integer>> rmap;
    public HyponymsParser(String synsetFile, String hyponymFile) {
        graph = new Graph();
        rmap = new HashMap<>();
        In inSysnet  = new In(synsetFile);
        In hyponyms = new In(hyponymFile);
        map = createMap(inSysnet);
        graph = createGraph(hyponyms);
    }

    private Graph createGraph(In hyponyms) {
        Graph graph = new Graph();
        while (!hyponyms.isEmpty() && hyponyms.hasNextLine()) {
            String[] adjacents = hyponyms.readLine().split(",");
            int node = Integer.parseInt(adjacents[0]);
            if (!graph.hasNode(node)) {
                graph.addNode(node);
            }
            for (int i = 1; i < adjacents.length; i++) {
                int adj = Integer.parseInt(adjacents[i]);
                if (graph.hasNode(adj)) {
                    graph.addEdge(node, adj);
                }
                else {
                    graph.addNode(adj);
                    graph.addEdge(node, adj);
                }
            }
        }
        return graph;
    }

    private HashMap<Integer, String[]> createMap(In inSysnet) {
        HashMap<Integer, String[]> map = new HashMap<>();
        while (!inSysnet.isEmpty() && inSysnet.hasNextLine()) {
            String[] line = inSysnet.readLine().split(",");
            int id = Integer.parseInt(line[0]);
            String[] words = line[1].split(" ");
            map.put(id, words);
            for (String s: words) {
                if (rmap.containsKey(s)) {
                    rmap.get(s).add(id);
                }
                else {
                    TreeSet<Integer> newTSet = new TreeSet<>();
                    newTSet.add(id);
                    rmap.put(s, newTSet);
                }

            }
            if (!graph.hasNode(id)) {
                graph.addNode(id);
            }
        }
        return map;
    }
    public Graph getGraph() {
        return graph;
    }
    public HashMap<Integer, String[]> getMap() {
        return map;
    }
    public HashMap<String, TreeSet<Integer>> getrMap() {
        return rmap;
    }
}
