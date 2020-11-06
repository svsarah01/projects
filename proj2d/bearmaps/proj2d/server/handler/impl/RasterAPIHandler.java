package bearmaps.proj2d.server.handler.impl;

import bearmaps.proj2d.AugmentedStreetMapGraph;
import bearmaps.proj2d.server.handler.APIRouteHandler;
import spark.Request;
import spark.Response;
import bearmaps.proj2d.utils.Constants;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static bearmaps.proj2d.utils.Constants.*;

/**
 * Handles requests from the web browser for map images. These images
 * will be rastered into one large image to be displayed to the user.
 * @author rahul, Josh Hug, _________
 */
public class RasterAPIHandler extends APIRouteHandler<Map<String, Double>, Map<String, Object>> {

    /**
     * Each raster request to the server will have the following parameters
     * as keys in the params map accessible by,
     * i.e., params.get("ullat") inside RasterAPIHandler.processRequest(). <br>
     * ullat : upper left corner latitude, <br> ullon : upper left corner longitude, <br>
     * lrlat : lower right corner latitude,<br> lrlon : lower right corner longitude <br>
     * w : user viewport window width in pixels,<br> h : user viewport height in pixels.
     **/
    private static final String[] REQUIRED_RASTER_REQUEST_PARAMS = {"ullat", "ullon", "lrlat",
            "lrlon", "w", "h"};

    /**
     * The result of rastering must be a map containing all of the
     * fields listed in the comments for RasterAPIHandler.processRequest.
     **/
    private static final String[] REQUIRED_RASTER_RESULT_PARAMS = {"render_grid", "raster_ul_lon",
            "raster_ul_lat", "raster_lr_lon", "raster_lr_lat", "depth", "query_success"};


    @Override
    protected Map<String, Double> parseRequestParams(Request request) {
        return getRequestParams(request, REQUIRED_RASTER_REQUEST_PARAMS);
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * requestParams Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * response : Not used by this function. You may ignore.
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image;
     *                    can also be interpreted as the length of the numbers in the image
     *                    string. <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */

    private Integer[] cornersHelper(double lrlon, double ullon, double ullat, double lrlat, int i) {
        double x_increment = Math.abs(ROOT_LRLON - ROOT_ULLON) / i;
        double y_increment = Math.abs(ROOT_LRLAT - ROOT_ULLAT)/ i;

        /* finding upper lon & lat of raster */
        int x0 = -1;
        int y0 = -1;
        double lon = ROOT_ULLON;
        double lat = ROOT_ULLAT;
        while (ullon > lon) {
            lon += x_increment;
            x0 += 1;
        }
        while (ullat < lat) {
            lat -= y_increment;
            y0 += 1;
        }

        /* finding lower lon & lat of raster */
        int x1 = i;
        int y1 = i;
        double bottomLon = ROOT_LRLON;
        double bottomLat = ROOT_LRLAT;
        while (lrlon < bottomLon) {
            bottomLon -= x_increment;
            x1 -= 1;
        }
        while (lrlat > bottomLat) {
            bottomLat += y_increment;
            y1 -= 1;
        }

        Integer[] corners = new Integer[]{x0, x1, y0, y1};
        return corners;
    }

    private String[][] imagesConstructor(int depth, int x0, int x1, int y0, int y1) {
        int x = x0;
        int y = y0;
        String[][] result = new String[y1 - y0 + 1][x1 - x0 + 1];
        for (int i = 0; i < y1 - y0 + 1; i++) {
            for (int j = 0; j < x1 - x0 + 1; j++) {
                result[i][j] = "d" + depth + "_x" + x +"_y" + y +".png";
                x += 1;
            }
            y += 1;
            x = x0;
        }
        return result;
    }

    private Double[] latAndLon(int i, int x0, int x1, int y0, int y1) {
        double x_increment = Math.abs(ROOT_LRLON - ROOT_ULLON) / i;
        double y_increment = Math.abs(ROOT_LRLAT - ROOT_ULLAT)/ i;
        Double[] result = new Double[4];
        result[0] = ROOT_ULLON + (x_increment * x0);
        result[1] = ROOT_ULLAT - (y_increment * y0);
        result[2] = ROOT_ULLON + (x_increment * (x1 + 1));
        result[3] = ROOT_ULLAT - (y_increment * (y1 + 1));
        return result;
    }

    private boolean isValid(double lrlon, double ullon, double ullat, double lrlat) {
        return !(ullon > ROOT_LRLON && ullat > ROOT_LRLAT || ullon > lrlon || ullat < lrlat);
    }

    @Override
    public Map<String, Object> processRequest(Map<String, Double> requestParams, Response response) {
        double lrlon = requestParams.get("lrlon");
        double ullon = requestParams.get("ullon");
        double width = requestParams.get("w");
        double height = requestParams.get("h");
        double ullat = requestParams.get("ullat");
        double lrlat = requestParams.get("lrlat");

        /* checking for no coverage */
        boolean success = isValid(lrlon, ullon, ullat, lrlat);

        /* accounting for partial coverage */
        ullon = Math.max(ROOT_ULLON, ullon);
        ullat = Math.min(ROOT_ULLAT, ullat);
        lrlon = Math.min(ROOT_LRLON, lrlon);
        lrlat = Math.max(ROOT_LRLAT, lrlat);

        /* calculating depth */
        double queryLonDPP = (lrlon - ullon) / width;
        int depth = 0;
        double depthLonDPP = (ROOT_LRLON - ROOT_ULLON) / TILE_SIZE;
        while (depthLonDPP > queryLonDPP) {
            depthLonDPP /= 2.0;
            depth += 1;
        }
        depth = Math.min(depth, 7);

        /* n represents the num of rows/cols */
        int n = (int) Math.sqrt(Math.pow(4, depth));

        /* getting min & max x and y coordinates at depth */
        Integer[] startAndEnd = cornersHelper(lrlon, ullon, ullat, lrlat, n);
        int x0 = startAndEnd[0];
        int x1 = startAndEnd[1];
        int y0 = startAndEnd[2];
        int y1 = startAndEnd[3];

        /* getting images array */
        String[][] raster = imagesConstructor(depth, x0, x1, y0, y1);

        /* getting lon/lat coordinates of raster */
        Double[] corners = latAndLon(n, x0, x1, y0, y1);

        /* constructing Map to return */
        HashMap<String, Object> result = new HashMap<>();
        result.put("render_grid", raster);
        result.put("raster_ul_lon", corners[0]);
        result.put("raster_ul_lat", corners[1]);
        result.put("raster_lr_lon", corners[2]);
        result.put("raster_lr_lat", corners[3]);
        result.put("depth", depth);
        result.put("query_success", success);
        return result;
    }

    @Override
    protected Object buildJsonResponse(Map<String, Object> result) {
        boolean rasterSuccess = validateRasteredImgParams(result);

        if (rasterSuccess) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            writeImagesToOutputStream(result, os);
            String encodedImage = Base64.getEncoder().encodeToString(os.toByteArray());
            result.put("b64_encoded_image_data", encodedImage);
        }
        return super.buildJsonResponse(result);
    }

    private Map<String, Object> queryFail() {
        Map<String, Object> results = new HashMap<>();
        results.put("render_grid", null);
        results.put("raster_ul_lon", 0);
        results.put("raster_ul_lat", 0);
        results.put("raster_lr_lon", 0);
        results.put("raster_lr_lat", 0);
        results.put("depth", 0);
        results.put("query_success", false);
        return results;
    }

    /**
     * Validates that Rasterer has returned a result that can be rendered.
     * @param rip : Parameters provided by the rasterer
     */
    private boolean validateRasteredImgParams(Map<String, Object> rip) {
        for (String p : REQUIRED_RASTER_RESULT_PARAMS) {
            if (!rip.containsKey(p)) {
                System.out.println("Your rastering result is missing the " + p + " field.");
                return false;
            }
        }
        if (rip.containsKey("query_success")) {
            boolean success = (boolean) rip.get("query_success");
            if (!success) {
                System.out.println("query_success was reported as a failure");
                return false;
            }
        }
        return true;
    }

    /**
     * Writes the images corresponding to rasteredImgParams to the output stream.
     * In Spring 2016, students had to do this on their own, but in 2017,
     * we made this into provided code since it was just a bit too low level.
     */
    private  void writeImagesToOutputStream(Map<String, Object> rasteredImageParams,
                                                  ByteArrayOutputStream os) {
        String[][] renderGrid = (String[][]) rasteredImageParams.get("render_grid");
        int numVertTiles = renderGrid.length;
        int numHorizTiles = renderGrid[0].length;

        BufferedImage img = new BufferedImage(numHorizTiles * Constants.TILE_SIZE,
                numVertTiles * Constants.TILE_SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics graphic = img.getGraphics();
        int x = 0, y = 0;

        for (int r = 0; r < numVertTiles; r += 1) {
            for (int c = 0; c < numHorizTiles; c += 1) {
                graphic.drawImage(getImage(Constants.IMG_ROOT + renderGrid[r][c]), x, y, null);
                x += Constants.TILE_SIZE;
                if (x >= img.getWidth()) {
                    x = 0;
                    y += Constants.TILE_SIZE;
                }
            }
        }

        /* If there is a route, draw it. */
        double ullon = (double) rasteredImageParams.get("raster_ul_lon"); //tiles.get(0).ulp;
        double ullat = (double) rasteredImageParams.get("raster_ul_lat"); //tiles.get(0).ulp;
        double lrlon = (double) rasteredImageParams.get("raster_lr_lon"); //tiles.get(0).ulp;
        double lrlat = (double) rasteredImageParams.get("raster_lr_lat"); //tiles.get(0).ulp;

        final double wdpp = (lrlon - ullon) / img.getWidth();
        final double hdpp = (ullat - lrlat) / img.getHeight();
        AugmentedStreetMapGraph graph = SEMANTIC_STREET_GRAPH;
        List<Long> route = ROUTE_LIST;

        if (route != null && !route.isEmpty()) {
            Graphics2D g2d = (Graphics2D) graphic;
            g2d.setColor(Constants.ROUTE_STROKE_COLOR);
            g2d.setStroke(new BasicStroke(Constants.ROUTE_STROKE_WIDTH_PX,
                    BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            route.stream().reduce((v, w) -> {
                g2d.drawLine((int) ((graph.lon(v) - ullon) * (1 / wdpp)),
                        (int) ((ullat - graph.lat(v)) * (1 / hdpp)),
                        (int) ((graph.lon(w) - ullon) * (1 / wdpp)),
                        (int) ((ullat - graph.lat(w)) * (1 / hdpp)));
                return w;
            });
        }

        rasteredImageParams.put("raster_width", img.getWidth());
        rasteredImageParams.put("raster_height", img.getHeight());

        try {
            ImageIO.write(img, "png", os);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private BufferedImage getImage(String imgPath) {
        BufferedImage tileImg = null;
        if (tileImg == null) {
            try {
                File in = new File(imgPath);
                tileImg = ImageIO.read(in);
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        }
        return tileImg;
    }
}
