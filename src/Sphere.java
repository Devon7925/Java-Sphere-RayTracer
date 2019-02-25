import java.awt.Color;
import java.util.ArrayList;

public class Sphere {
    protected float radius;
    
    protected Color color;
    protected Vec3 pos;

    public Sphere(float radius, Vec3 position) {
        this.radius = radius;
        this.pos = position;
        this.color = Color.RED;
    }

    public Sphere(float radius, Color color, Vec3 position) {
        this.radius = radius;
        this.color = color;
        this.pos = position;
    }

    public boolean hit(Ray ray) {
        // uncomment if you allow hits from inside the sphere
        if (ray.getOrigin().sub(pos).mag() <= radius) return true;

        Vec3 OP = pos.sub(ray.getOrigin()); // vector pointing from ray origin to sphere position
        float dotprod = OP.dot(ray.getDirection());
        if (dotprod <= 0) return false;

        Vec3 proj = ray.getDirection().scale(dotprod);
        Vec3 OPproj = proj.sub(OP); // perp

        return OPproj.mag() <= radius;
    }

    public float[] intersect(Ray ray) {
        float[] ret = new float[2];

        Vec3 CO = ray.getOrigin().sub(this.pos);
        float base = (ray.getDirection().dot(CO));
        float disc = (float) Math.sqrt(base*base - CO.dot(CO) + radius*radius);

        ret[0] = -base - disc;
        ret[1] = -base + disc;

        return ret;
    }

    public float touch(Ray ray) {
        if (ray.getOrigin().sub(getPos()).mag() <= radius) return Float.NaN;
        float[] T = intersect(ray);
        if (Float.isNaN(T[0])) return Float.NaN;
        float min = Math.min(T[0], T[1]);
        float max = Math.max(T[0], T[1]);
        if (min > 0) return min;
        if (max > 0) return max;
        return Float.NaN;
    }

    public Vec3 rhat(Vec3 point) {
        return point.sub(pos).unit();
    }

    public Color colorHit(Ray ray, ArrayList<Sphere> spheres, int n_reflections, float t) {
        if (!hit(ray) || Float.isNaN(t)) return Color.black; 

        if(t > 8)
            return new Color(
                (int) (color.getRed()   * 8 / t),
                (int) (color.getGreen() * 8 / t),
                (int) (color.getBlue()  * 8 / t)
            );

        return color;
    }

    public float getRadius() { return radius; }

    public Color getColor() { return color; }
    public void setColor(Color color) { this.color = color; }

    public Vec3 getPos() { return pos; }
    public void setPos(Vec3 pos) { this.pos = pos; }

}
