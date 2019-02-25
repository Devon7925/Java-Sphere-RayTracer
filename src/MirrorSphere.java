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
        int min_index = -1;
        int index = 0;
        Color c = Color.black;

        for (Sphere sphere : spheres) {
            if (sphere == this) continue;

            float dist = sphere.touch(reflect);
            if (!Float.isNaN(dist) && dist > 0 &&  dist < min_dist) {
                min_dist = dist;
                min_index = index;
            }
            index++;
        }
        if(min_index != -1) c = spheres.get(min_index).colorHit(reflect, spheres, n_reflections-1, min_dist);

        if(t + min_dist > 8)
            return new Color(
                (int) (c.getRed()   * 8 / (t + min_dist)),
                (int) (c.getGreen() * 8 / (t + min_dist)),
                (int) (c.getBlue()  * 8 / (t + min_dist))
            );

        return c;
    }
}
