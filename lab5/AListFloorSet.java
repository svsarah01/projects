/**
 * TODO: Fill in the add and floor methods.
 */
public class AListFloorSet implements Lab5FloorSet {
    AList<Double> items;

    public AListFloorSet() {
        items = new AList<>();
    }

    public void add(double x) {
        items.addLast(x);
    }

    public double floor(double x) {
        double best = Double.NEGATIVE_INFINITY;

        for (int i = 0; i < items.size(); i += 1) {
            double thisItem = items.get(i);
            if (thisItem <= x) {
                if (thisItem > best) {
                    best = thisItem;
                }
            }
        }
        return best;
    }
}
