public class NBody {

    public static double readRadius(String txt) {
        In in = new In(txt);
        int num_planets = in.readInt();
        double radius = in.readDouble();
        return radius;
    }

    public static Body[] readBodies(String txt) {
        In in = new In(txt);
        int num_planets = in.readInt();
        double radius = in.readDouble();
        Body[] bodies = new Body[num_planets];
        for (int i = 0; i < num_planets; i = i+1) {
            double xP = in.readDouble();
            double yP = in.readDouble();
            double xV = in.readDouble();
            double yV = in.readDouble();
            double m = in.readDouble();
            String img = in.readString();
            bodies[i] = new Body(xP, yP, xV, yV, m, img);
        }
        return bodies;
    }

    public static void main(String[] args) {
        double T = Double.parseDouble(args[0]);
        double dt = Double.parseDouble(args[1]);
        String filename = args[2];
        this.readRadius(filename);
        this.readBodies(filename);
    }
}
