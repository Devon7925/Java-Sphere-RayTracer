import java.awt.*;
import java.util.ArrayList;

public class MirrorSphere extends Sphere {

    public MirrorSphere(float radius, Vec3 position)  {
        super(radius, position);
    }

    public Color colorHit(Ray ray, ArrayList<Sphere> spheres, int n_reflections, float t) {
        if (n_reflections <= 0) return Color.BLACK;
        if (Float.isNaN(t) || t <= 0) return Color.BLACK;

        Vec3 rhat = super.rhat(ray.point(t));
        Ray reflect = ray.reflect(rhat, t);

        float min_dist = Float.MAX_VALUE;
        Color c = Color.black;

        for (Sphere sphere : spheres) {
            if (sphere == this) continue;

            float dist = sphere.touch(reflect);
            if (!Float.isNaN(dist) && dist > 0 &&  dist < min_dist) {
                min_dist = dist;
                c = sphere.colorHit(reflect, spheres, n_reflections-1, dist);
            }
        }

        int r = c.getRed();
        int g = c.getGreen();
        int b = c.getBlue();

        r = Math.min(r, (int)(8 * r / (t + min_dist)));
        g = Math.min(g, (int)(8 * g / (t + min_dist)));
        b = Math.min(b, (int)(8 * b / (t + min_dist)));

        return new Color(r, g, b);
    }
}
