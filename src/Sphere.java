import java.awt.Color;
import java.util.ArrayList;

public class Sphere {
    protected float radius;
    
    protected Vec3 pos;

    ArrayList<RenderComponent> renderComponents;

    public Sphere(float radius, Vec3 position) {
        this(radius, Color.RED, position);
    }

    public Sphere(float radius, Color color, Vec3 position) {
        this.radius = radius;
        this.pos = position;
        renderComponents = new ArrayList<>();
        renderComponents.add(new ColorReflect(color));
    }

    public Color colorHit(Ray ray, ArrayList<Sphere> spheres, int n_reflections, float t){
        double r = 0, g = 0, b = 0;
        for(RenderComponent comp : renderComponents){
            Color c = comp.colorHit(this, ray, spheres, n_reflections, t);
            r += comp.percent*c.getRed();
            g += comp.percent*c.getGreen();
            b += comp.percent*c.getBlue();
        }
        return new Color((int) r, (int) g, (int) b);
    }

    public void addComponent(RenderComponent comp){
        renderComponents.add(comp);
        renderComponents.get(0).percent -= comp.percent;
    }

    public boolean hit(Ray ray) {
        // uncomment if you allow hits from inside the sphere
        // if (ray.getOrigin().sub(pos).mag() <= radius) return true;

        Vec3 OP = pos.sub(ray.getOrigin()); // vector pointing from ray origin to sphere position
        float dotprod = OP.dot(ray.getDirection());
        return hit(ray, OP, dotprod);
    }

    public boolean hit(Ray ray, Vec3 OP, float dotprod) {
        // uncomment if you allow hits from inside the sphere
        // if (ray.getOrigin().sub(pos).mag() <= radius) return true;
        if (dotprod <= 0) return false;

        Vec3 proj = ray.getDirection().scale(dotprod);
        Vec3 OPproj = proj.sub(OP); // perp

        return OPproj.mag() <= radius;
    }

    public float[] intersect(Ray ray) {
        Vec3 OP = pos.sub(ray.getOrigin());
        return intersect(ray, OP, OP.dot(ray.getDirection()));
    }

    private float[] intersect(Ray ray, Vec3 OP, float base) {
        float[] ret = new float[2];
        float intersectRadii = (float) Math.sqrt(base*base - OP.dot(OP) + radius*radius);

        ret[0] = base - intersectRadii;
        ret[1] = base + intersectRadii;

        return ret;
    }

    public float touch(Ray ray) {
        // if (ray.getOrigin().sub(getPos()).mag() <= radius) return Float.NaN;
        float[] T = intersect(ray);
        if (Float.isNaN(T[0])) return Float.NaN;
        if (T[0] > 0) return T[0];
        if (T[1] > 0) return T[1];
        return Float.NaN;
    }

	public float touch(Ray ray, Vec3 OP, float dotprod) {
        float[] T = intersect(ray, OP, dotprod);
        if (Float.isNaN(T[0])) return Float.NaN;
        if (T[0] > 0) return T[0];
        if (T[1] > 0) return T[1];
        return Float.NaN;
	}

    public Vec3 rhat(Vec3 point) {
        return point.sub(pos).unit();
    }

    public float getRadius() { return radius; }

    public Vec3 getPos() { return pos; }
    public void setPos(Vec3 pos) { this.pos = pos; }

}
