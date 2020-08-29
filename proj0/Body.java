public class Body {
    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;

    public Body(double xP, double yP, double xV, double yV, double m, String img) {
            xxPos = xP;
            yyPos = yP;
            xxVel = xV;
            yyVel = yV;
            mass = m;
            imgFileName = img;
    }

    public Body(Body b) {
        this(b.xxPos, b.yyPos, b.xxVel, b.yyVel, b.mass, b.imgFileName);
    }

    public double calcDistance(Body b) {
        return Math.sqrt(Math.pow(this.xxPos - b.xxPos, 2)+Math.pow(this.yyPos - b.yyPos, 2));
    }

    public double calcForceExertedBy(Body b) {
        if (this.equals(b)) {
            return 0;
        }
        return (6.67e-11*b.mass*this.mass)/(Math.pow(this.calcDistance(b), 2));
    }

    public double calcForceExertedByX(Body b) {
        double larger = Math.max(this.xxPos, b.xxPos);
        double smaller = Math.min(this.xxPos, b.xxPos);
        return (this.calcForceExertedBy(b)*(larger-smaller))/this.calcDistance(b);
    }

    public double calcForceExertedByY(Body b) {
        double larger = Math.max(this.yyPos, b.yyPos);
        double smaller = Math.min(this.yyPos, b.yyPos);
        return (this.calcForceExertedBy(b)*(larger-smaller))/this.calcDistance(b);
    }

    public double calcNetForceExertedByX(Body[] a) {
        double sum = 0;
        for (Body b : a) {
            if (this.equals(b)) {
                continue;
            }
            sum += this.calcForceExertedByX(b);
        }
        return sum;
    }

    public double calcNetForceExertedByY(Body[] a) {
        double sum = 0;
        for (Body b : a) {
            if (this.equals(b)) {
                continue;
            }
            sum += this.calcForceExertedByY(b);
        }
        return sum;
    }

    public void update(double dt, double fX, double fY) {
        double aX = fX/this.mass;
        double aY = fY/this.mass;
        this.xxVel = this.xxVel + dt*aX;
        this.yyVel = this.yyVel + dt*aY;
        this.xxPos = this.xxPos + dt*this.xxVel;
        this.yyPos = this.yyPos + dt*this.yyVel;
    }
}
