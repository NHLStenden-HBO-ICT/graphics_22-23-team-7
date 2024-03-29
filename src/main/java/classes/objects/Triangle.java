package classes.objects;

import classes.math.Point3D;
import classes.math.Ray;
import classes.math.Vector3D;
import interfaces.objects.Shape;

import java.awt.*;


/**
 * intersection thx to kaya: https://github.com/NHLStenden-HBO-ICT-SE/graphics_22-23-graphics-groep-2
 */

public class Triangle implements Shape {


    private Point3D[] vertices = new Point3D[3];
    private Vector3D surfaceNormal = new Vector3D();
    private Point3D position;

    private Color color;

    /**
     * triangle constructor with custom normal direction
     *
     * @param vertice
     * @param vertice1
     * @param vertice2
     * @param surfaceNormal
     */
    public Triangle(Point3D vertice, Point3D vertice1, Point3D vertice2, Vector3D surfaceNormal) {
        this(vertice, vertice1, vertice2, Color.red);
        this.surfaceNormal = surfaceNormal;
    }

    /**
     * triangle constructor with custom normal direction
     *
     * @param vertice
     * @param vertice1
     * @param vertice2
     */
    public Triangle(Point3D vertice, Point3D vertice1, Point3D vertice2) {
        this(vertice, vertice1, vertice2, Color.white);
    }

    /**
     * triangle constructor
     *
     * @param vertice
     * @param vertice1
     * @param vertice2
     */
    public Triangle(Point3D vertice, Point3D vertice1, Point3D vertice2, Color color) {
        vertices[0] = vertice;
        vertices[1] = vertice1;
        vertices[2] = vertice2;
        this.calcSurfaceNormal();
        this.color = color;
        calcPosition();
    }

    /**
     * @return vertices of the triangle
     */
    public Point3D[] getVertices() {
        return this.vertices;
    }

    /**
     * sets specific index of triangle vertices
     *
     * @param index index (starting at 0)
     * @param p     new point
     */
    public void setVertex(int index, Point3D p) {
        this.vertices[index] = p;
        calcPosition();
    }

    /**
     * calculates service normal and saves it in the object for easy access. is called in the constructor and after changes.
     */
    private void calcSurfaceNormal() {
        //gets the two direction vectors
        Vector3D direction1 = vertices[0].getVector(vertices[1]);
        Vector3D direction2 = vertices[2].getVector(vertices[0]);

        //cross product
        surfaceNormal.x = ((direction1.y * direction2.z) - (direction1.z * direction2.y));
        surfaceNormal.y = ((direction1.z * direction2.x) - (direction1.x * direction2.z));
        surfaceNormal.z = ((direction1.x * direction2.y) - (direction1.y * direction2.x));
        surfaceNormal = surfaceNormal.normalize();
    }

    /**
     * gets surface normals
     *
     * @return
     */
    public Vector3D getSurfaceNormal() {
        return surfaceNormal;
    }

    /**
     * calculates center of triangle
     */
    private void calcPosition() {
        this.position = new Point3D((vertices[0].add(vertices[1].add(vertices[2]))).divide(3));
    }

    /**
     * returns position
     *
     * @return center of triangle
     */
    @Override
    public Point3D getPosition() {
        return position;
    }

    @Override
    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * intersection with ray
     *
     * @param ray the ray
     * @return IntersectionHandler of the intersect
     */
    @Override
    public IntersectionHandler intersection(Ray ray) {
        //easy access scoped pointer vars
        Point3D vertex0 = this.getVertices()[0];
        Point3D vertex1 = this.getVertices()[1];
        Point3D vertex2 = this.getVertices()[2];
        Vector3D crossproduct, s, q;

        double direction1dot, f, u, v;

        //gets direction vectors
        Vector3D direction1 = vertex0.getVector(vertex1);
        Vector3D direction2 = vertex0.getVector(vertex2);

        crossproduct = ray.getDirection().cross(direction2);
        direction1dot = direction1.dot(crossproduct);

        /**
         * direction1dot is the angle between the ray's direction and the triangle direction
         * if the angle is too small then the ray is probably in line with the triangle and
         * we don't intersect with it.
         **/
        if (direction1dot > -0.0000001 && direction1dot < 0.0000001) {
            return new IntersectionHandler(false);
        }

        f = 1.0 / direction1dot;
        s = vertex0.getVector(ray.getPosition());
        u = f * (s.dot(crossproduct));

        if (u < 0.0 || u > 1.0) {
            return new IntersectionHandler(false);
        }

        q = s.cross(direction1);
        v = f * ray.getDirection().dot(q);

        if (v < 0.0 || u + v > 1.0) {
            return new IntersectionHandler(false);
        }


        //calculate distance
        double distance = f * direction2.dot(q);

        //too close
        if (distance < 0.0000001) {
            return new IntersectionHandler(false);
        }

        return new IntersectionHandler(true, distance, this, ray);
    }

    /**
     * checks if ray is in range
     *
     * @param ray the ray
     * @return if is in range bool
     */
    @Override
    public Boolean isRayInRangeOfShape(Ray ray) {
        return ray.getPosition().distance(this.getPosition()) < ray.getLength();
    }

    /**
     * recalculate surface normals
     *
     * @param point
     * @return
     */
    @Override
    public Vector3D calcNormal(Point3D point) {
        //this.surfaceNormal = this.position.getVector(point).normalize();
        calcSurfaceNormal();
        return surfaceNormal;
    }

    /**
     * sets surface normal
     *
     * @param surfaceNormal the surface normal
     */
    public void setNormal(Vector3D surfaceNormal) {
        this.surfaceNormal = surfaceNormal;
    }
}
