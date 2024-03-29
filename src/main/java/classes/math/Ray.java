package classes.math;

public class Ray {
    private final Point3D position; /*Ray origin */
    private final Vector3D direction; /*Ray direction */
    private final double t; /*distance */

    //*****************************
    // Constructors
    //*****************************

    public Ray(Vector3D direction, double distance) {
        this(new Point3D(), direction, distance);
    }

    public Ray(Point3D position, Vector3D direction, double distance) {
        this.position = new Point3D(position);
        this.direction = new Vector3D(direction).normalize();
        this.t = distance;
    }

    //*****************************
    // Methods
    //*****************************

    /**
     * gets origin of ray
     *
     * @return origin
     */
    public Point3D getPosition() {
        return position;
    }

    /**
     * gets direction of ray
     *
     * @return vector
     */
    public Vector3D getDirection() {
        return direction;
    }

    /**
     * gets length of ray
     *
     * @return length
     */
    public double getLength() {
        return t;
    }
}
