package bearmaps;
import java.util.List;

public class NaivePointSet implements PointSet{
    List points;

    public NaivePointSet(List<Point> points) {
        this.points = points;
    }

    @Override
    public Point nearest(double x, double y) {
        double min = 100000000;
        Point closest = null;
        Point pt = new Point(x, y);
        for (int i = 0; i < points.size(); i++) {
            if (Point.distance(pt, (Point) points.get(i)) < min) {
                min = Point.distance(pt, (Point) points.get(i));
                closest = (Point) points.get(i);
            }
        }
        return closest;
    }

    public static void main(String[] args) {
        Point p1 = new Point(1.1, 2.2); // constructs a Point with x = 1.1, y = 2.2
        Point p2 = new Point(3.3, 4.4);
        Point p3 = new Point(-2.9, 4.2);

        NaivePointSet nn = new NaivePointSet(List.of(p1, p2, p3));
        Point ret = nn.nearest(3.0, 4.0); // returns p2
        ret.getX(); // evaluates to 3.3
        ret.getY(); // evaluates to 4.4
    }
}
