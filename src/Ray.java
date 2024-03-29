/**
 * Effectively a line class with origin point and direction vector Every point
 * along this line is obtainable by Origin + t * Direction where t is time
 */
public class Ray {
    private Vec3 origin;
    private Vec3 direction;

    public Ray(Vec3 origin, Vec3 direction) {
        if (direction.isZero()) throw new IllegalArgumentException("Cannot create ray with zero direction");
        this.origin = new Vec3(origin);
        this.direction = (new Vec3(direction)).unit();
    }

    public Vec3 point(float t) { return origin.add(direction.scale(t)); }
    public Vec3 getOrigin() { return origin; }
    public Vec3 getDirection() { return direction; }

    public Ray reflect(Vec3 norm, float t) {
        Vec3 D = direction.sub(direction.proj(norm).scale(2));
        return new Ray(point(t), D);
    }

    public Ray refract(Vec3 norm, Vec3 point_t, float N1, float N2) {
        Vec3 r = direction.proj(norm).sub(direction);
        Vec3 D = direction.add(r.scale(1 - N1 / N2)).unit();
        return new Ray(point_t, D);
    }

    public String toString() {
        return "O:" + origin.toString() + ", D:" + direction.toString();
    }
}

