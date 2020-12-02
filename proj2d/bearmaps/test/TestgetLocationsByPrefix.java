package bearmaps.test;

import org.junit.Before;
import org.junit.Test;
import bearmaps.proj2d.Router;
import bearmaps.proj2d.AugmentedStreetMapGraph;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestgetLocationsByPrefix {
    private static final String BERKELEY_OSM = "../library-fa20/data/proj2d_xml/berkeley-2019.osm.xml";
    private static AugmentedStreetMapGraph graph;
    private static boolean initialized = false;

    @Before
    public void setUp() throws Exception {
        if (initialized) {
            return;
        }
        graph = new AugmentedStreetMapGraph(BERKELEY_OSM);
        initialized = true;
    }

    @Test
    public void prefixMatching() {
        List<String> result = graph.getLocationsByPrefix("k");
        System.out.println(result);
        assertTrue(result.contains("Key Route Train Station"));
        assertTrue(result.contains("Kitchen On Fire"));
        assertTrue(result.contains("Kitchen On Fire West"));
    }
}
