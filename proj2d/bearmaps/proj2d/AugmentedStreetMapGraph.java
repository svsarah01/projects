package bearmaps.proj2d;

import bearmaps.proj2ab.KDTree;
import bearmaps.proj2ab.Point;
import bearmaps.proj2c.streetmap.StreetMapGraph;
import bearmaps.proj2c.streetmap.Node;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.LinkedList;

/**
 * An augmented graph that is more powerful that a standard StreetMapGraph.
 * Specifically, it supports the following additional operations:
 *
 *
 * @author Alan Yao, Josh Hug, ________
 */
public class AugmentedStreetMapGraph extends StreetMapGraph {
    HashMap<Point, Node> pointToNodeMap;
    KDTree kdt;
    List<String> names;
    HashMap<String, Node> cleanedNameMap;

    public AugmentedStreetMapGraph(String dbPath) {
        super(dbPath);
        // You might find it helpful to uncomment the line below:
        List<Node> nodes = this.getNodes();
        pointToNodeMap = new HashMap<>();
        List<Point> points = new LinkedList<>();
        cleanedNameMap = new HashMap<>();
        names = new LinkedList<>();
        for (Node n : nodes) {
            names.add(cleanString(n.name()));
            cleanedNameMap.put(cleanString(n.name()), n);
            long id = n.id();
            if (!this.neighbors(id).isEmpty()) {
                Point p = new Point(n.lon(), n.lat());
                pointToNodeMap.put(p, n);
                points.add(p);
            }
        }
        kdt = new KDTree(points);
    }


    /**
     * For Project Part II
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    public long closest(double lon, double lat) {
        Point closestPoint = kdt.nearest(lon, lat);
        Node closestNode = pointToNodeMap.get(closestPoint);
        return closestNode.id();
    }


    /**
     * For Project Part III (gold points)
     * In linear time, collect all the names of OSM locations that prefix-match the query string.
     * @paramprefix Prefix string to be searched for. Could be any case, with our without
     *               punctuation.
     * @return A <code>List</code> of the full names of locations whose cleaned name matches the
     * cleaned <code>prefix</code>.
     */

    class TrieNode {
        private boolean isKey;
        private HashMap<Character, TrieNode> next;

        private TrieNode(boolean b){
            isKey = b;
            next = new HashMap<>();
        }

        private void add(String s) {
            if (s.length() == 0) {
                return;
            }
            char c = s.charAt(0);
            boolean last = s.length() == 1;
            if (!next.containsKey(c)) {
                next.put(c, new TrieNode(last));
            }
            next.get(c).add(s.substring(1));
        }
    }

    class TrieMap {
        private TrieNode root;

        private TrieMap(List<String> names) {
            root = new TrieNode(false);
            for (String s : names) {
                root.add(s);
            }
        }

        private void collect(TrieNode n, String prefix, String result, List<String> keys) {
            if (n == null) {
                return;
            }
            if (n.isKey) {
                keys.add(result);
            }
            if (prefix.length() == 0) {
                String prevResult = result;
                for (char c : n.next.keySet()) {
                    result = prevResult + c;
                    TrieNode cNode = n.next.get(c);
                    collect(cNode, prefix, result, keys);
                }
            } else {
                char firstLetter = prefix.charAt(0);
                if (n.next.containsKey(firstLetter)) {
                    result += firstLetter;
                    TrieNode cNode = n.next.get(firstLetter);
                    collect(cNode, prefix.substring(1), result, keys);
                }
            }
        }

        private List<String> keysWithPrefix(String prefix) {
            List<String> keys = new LinkedList<>();
            collect(root, prefix, "", keys);
            return keys;
        }
    }

    private List<String> getLocationsByPrefixHelper(String prefix) {
        TrieMap t = new TrieMap(names);
        return t.keysWithPrefix(prefix);
    }


    public List<String> getLocationsByPrefix(String prefix) {
        return getLocationsByPrefixHelper(prefix);
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
        LinkedList<Map<String, Object>> result = new LinkedList<>();
        String cleanedLocationName = cleanString(locationName);
        for (String name : names) {
            if (name.equals(cleanedLocationName)) {
                Node n = cleanedNameMap.get(name);
                HashMap<String, Object> location = new HashMap<>();
                location.put("lat", n.lat());
                location.put("lon", n.lon());
                location.put("name", n.name());
                location.put("id", n.id());
                result.add(location);
            }
        }
        return result;
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
