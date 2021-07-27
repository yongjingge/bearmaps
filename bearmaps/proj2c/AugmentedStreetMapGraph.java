package bearmaps.proj2c;

import bearmaps.hw4.streetmap.Node;
import bearmaps.hw4.streetmap.StreetMapGraph;
import bearmaps.proj2ab.KdTree;
import bearmaps.proj2ab.Point;

import java.util.*;

/**
 * An augmented graph that is more powerful that a standard StreetMapGraph.
 * Specifically, it supports the following additional operations:
 *
 * @author Alan Yao, Josh Hug, ________
 */
public class AugmentedStreetMapGraph extends StreetMapGraph {

    Map<Point, Node> map;
    List<Point> points;
//    TST trie = new TST();
    TrieSet trie = new TrieSet();
    Map<String, Set<Node>> cleanToNodes;
    KdTree kdTree;

    public AugmentedStreetMapGraph(String dbPath) {
        super(dbPath);
        List<Node> graphNodes = this.getNodes();
        map = new HashMap<>(); // mapping Point to Node
        points = new ArrayList<>(); // record nodes with neighbors in the point format
        cleanToNodes = new HashMap<>(); // mapping 'clean' name to Nodes

        for (Node n : graphNodes) {
            if (this.neighbors(n.id()).size() != 0) {
                Point p = new Point(n.lon(), n.lat());
                map.put(p, n);
                points.add(p);
            }

            // multiple nodes can share the same clean name, we use a hashset to record these nodes.
            if (n.name() != null) {
                String cleanName = cleanString(n.name());
                trie.add(cleanName);
                if (! cleanToNodes.containsKey(cleanName)) {
                    Set<Node> nodesOfThisName = new HashSet<>();
                    nodesOfThisName.add(n);
                    cleanToNodes.put(cleanName, nodesOfThisName);
                } else {
                    cleanToNodes.get(cleanName).add(n);
                }
            }
        }
        kdTree = new KdTree(points);
    }

    /**
     * For Project Part II
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    public long closest(double lon, double lat) {
        Point nearest = kdTree.nearest(lon, lat);
        return map.get(nearest).id();
    }


    /**
     * For Project Part III (gold points)
     * In linear time, collect all the names of OSM locations that prefix-match the query string.
     * @param prefix Prefix string to be searched for. Could be any case, with our without
     *               punctuation.
     * @return A <code>List</code> of the full names of locations whose cleaned name matches the
     * cleaned <code>prefix</code>.
     */
    public List<String> getLocationsByPrefix(String prefix) {
        List<String> locationFullNames = new ArrayList<>();
        String query = cleanString(prefix);
        List<String> keys = trie.keysWithPrefix(query);
        for (String k : keys) {
            if (cleanToNodes.containsKey(k) && cleanToNodes.get(k) != null) {
                for (Node n : cleanToNodes.get(k)) {
                    locationFullNames.add(n.name());
                }
            }
        }
        return locationFullNames;
    }

    /**
     * For Project Part III (gold points)
     * Collect all locations that match a cleaned <code>locationName</code>, and return
     * information about each node that matches.
     * @param locationName A full name of a location searched for.
     * @return A list of locations whose cleaned name matches the
     * cleaned <code>locationName</code>, and each location is a map of parameters for the Json
     * response as specified: <br>
     * "lat" -> Number, The latitude of the node. <br>
     * "lon" -> Number, The longitude of the node. <br>
     * "name" -> String, The actual name of the node. <br>
     * "id" -> Number, The id of the node. <br>
     */
    public List<Map<String, Object>> getLocations(String locationName) {
        List<Map<String, Object>> locations = new ArrayList<>();
        String cleanLocation = cleanString(locationName);
        if (cleanToNodes.containsKey(cleanLocation) && cleanToNodes.get(cleanLocation) != null) {
            for (Node n : cleanToNodes.get(cleanLocation)) {
                Map<String, Object> singleMap = new HashMap<>();
                singleMap.put("lat", n.lat());
                singleMap.put("lon", n.lon());
                singleMap.put("name", n.name());
                singleMap.put("id", n.id());
                locations.add(singleMap);
            }
        }
        return locations;
    }


    /**
     * Useful for Part III. Do not modify.
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    private static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

}
