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
    //new
    Trie cleanedNameTrie;
    HashMap<String, List<Node>> cleanNametoNodeMap;

    public AugmentedStreetMapGraph(String dbPath) {
        super(dbPath);
        // You might find it helpful to uncomment the line below:
        List<Node> nodes = this.getNodes();
        pointToNodeMap = new HashMap<>();
        List<Point> points = new LinkedList<>();
        //new
        cleanedNameTrie = new Trie();
        cleanNametoNodeMap = new HashMap<>();
        for (Node n : nodes) {
            //new
            String cleanName = cleanString(n.name());
            cleanedNameTrie.add(cleanName);
            if (cleanNametoNodeMap.get(cleanName) == null) {
                List<Node> correspondingNodes = new LinkedList<>();
                correspondingNodes.add(n);
                cleanNametoNodeMap.put(cleanName, correspondingNodes);
            } else {
                cleanNametoNodeMap.get(cleanName).add(n);
            }
            //old
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
     * @param prefix Prefix string to be searched for. Could be any case, with our without
     *               punctuation.
     * @return A <code>List</code> of the full names of locations whose cleaned name matches the
     * cleaned <code>prefix</code>.
     */
    public List<String> getLocationsByPrefix(String prefix) {
        String cleanedPrefix = cleanString(prefix);
        List<String> cleanedResult = cleanedNameTrie.keysWithPrefix(cleanedPrefix);
        List<String> result = new LinkedList<>();
        for (String s : cleanedResult) {
            for (Node n : cleanNametoNodeMap.get(s)) {
                result.add(n.name());
            }
        }
        return result;
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
        String cleanedLocationName = cleanString(locationName);
        List<Map<String, Object>> result = new LinkedList<>();
        for (Node n : cleanNametoNodeMap.get(cleanedLocationName)) {
            Map<String, Object> locationInfo = new HashMap<>();
            locationInfo.put("lat", n.lat());
            locationInfo.put("lon", n.lon());
            locationInfo.put("name", n.name());
            locationInfo.put("id", n.id());
            result.add(locationInfo);
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
