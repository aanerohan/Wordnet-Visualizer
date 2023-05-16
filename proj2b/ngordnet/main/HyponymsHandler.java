package ngordnet.main;

import ngordnet.browser.NgordnetQuery;
import ngordnet.browser.NgordnetQueryHandler;
import ngordnet.ngrams.NGramMap;
import ngordnet.ngrams.TimeSeries;

import java.util.*;

public class HyponymsHandler extends NgordnetQueryHandler {

    private Graph graph;
    private HashMap<Integer, String[]> map;
    private HashMap<String, TreeSet<Integer>> rmap;
    private NGramMap ngm;
    public HyponymsHandler(HyponymsParser hp, NGramMap ngm) {
        this.graph = hp.getGraph();
        this.map = hp.getMap();
        this.rmap = hp.getrMap();
        this.ngm = ngm;
    }

    @Override
    public String handle(NgordnetQuery q) {
        String[] words = q.words().toArray(new String[q.words().size()]);
        int k = q.k();
        if (k == 0) {
            return formatHyponyms(handleKZero(words));
        } else {
            return formatHyponyms(handleKNotZero(words, q.startYear(), q.endYear(), q.k()));
        }
    }

    private TreeSet<String> handleKZero(String[] words) {
        TreeSet<String> hyponyms = handleHelper(words[0]);
        ArrayList<String> iter = new ArrayList<>(hyponyms);
        for (int i = 1; i < words.length; i++) {
            TreeSet<String> newHyponyms = handleHelper(words[i]);
            for (String hyponym : iter) {
                if (!newHyponyms.contains(hyponym)) {
                    hyponyms.remove(hyponym);
                }
            }
        }
        return hyponyms;
    }

    private TreeSet<String> handleKNotZero(String[] words, int startYear, int endYear, int k) {
        TreeSet<String> hyponyms = handleKZero(words);
        TreeSet<String> desiredHyponyms = new TreeSet<>();
        TreeMap<Long, String> countMap = new TreeMap<>();
        Set<Long> countSet = countMap.keySet();
        if (!hyponyms.isEmpty()) {
            for (String hyponym : hyponyms) {
                TimeSeries ts = ngm.countHistory(hyponym, startYear, endYear);
                long count = 0;
                List<Double> data = ts.data();
                for (Double dataPoint : data) {
                    count += dataPoint;
                }
                if (count > 0) {
                    countMap.put(count, hyponym);
                }
            }
            int startIndex = 0;
            for (Long count : countSet) {
                if (startIndex >= countSet.size() - k) {
                    desiredHyponyms.add(countMap.get(count));
                }
                startIndex += 1;
            }
        }
        return desiredHyponyms;
    }

    private TreeSet<String> handleHelper(String s) {
        TreeSet<String> result = new TreeSet<>();
        if (rmap.get(s) != null) {
            for (int id: rmap.get(s)) {
                TreeSet<Integer> hypernymIds = graph.traversal(id);
                for (int i : hypernymIds) {
                    for (String word : map.get(i)) {
                        result.add(word);
                    }
                }
            }
        }
        return result;
    }

    public String formatHyponyms(TreeSet<String> hyponyms) {
        ArrayList<String> container = new ArrayList<>(hyponyms);
        return container.toString();
    }
}
