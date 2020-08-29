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
        double radius = readRadius(filename);
        Body[] bodies = readBodies(filename);
        // animating the universe
        StdDraw.enableDoubleBuffering();
        double time = 0;
        StdAudio.play("audio/2001.mid");
        while (time <= T) {
            double[] xForces = new double[bodies.length];
            double[] yForces = new double[bodies.length];
            for (int i = 0; i < bodies.length; i = i + 1) {
                xForces[i] = bodies[i].calcNetForceExertedByX(bodies);
                yForces[i] = bodies[i].calcNetForceExertedByY(bodies);
            }
            for (int i = 0; i < bodies.length; i = i + 1) {
                bodies[i].update(dt, xForces[i], yForces[i]);
            }
            // draw background
            StdDraw.setScale(-radius, radius);
            StdDraw.picture(0, 0, "images/starfield.jpg");
            // draw bodies
            for (Body b : bodies) {
                b.draw();
            }
            StdDraw.show();
        		StdDraw.pause(10);
            time += dt;
        }
        StdOut.printf("%d\n", bodies.length);
        StdOut.printf("%.2e\n", radius);
        for (int i = 0; i < bodies.length; i++) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                  bodies[i].xxPos, bodies[i].yyPos, bodies[i].xxVel,
                  bodies[i].yyVel, bodies[i].mass, bodies[i].imgFileName);
        }
    }
}
