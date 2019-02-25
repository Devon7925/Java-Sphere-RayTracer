import java.awt.*;
import java.util.ArrayList;

public class LensSphere extends Sphere {
    public LensSphere(float radius, Vec3 position)  {
        super(radius, position);
    }

    public Color colorHit(Ray ray, ArrayList<Sphere> spheres, int n_reflections, float t1) {
        if (n_reflections <= 0) return Color.BLACK;
        if (Float.isNaN(t1) || t1 <= 0) return Color.BLACK;

        Vec3 rhat = super.rhat(ray.point(t1));
        Ray refract = ray.refract(rhat, t1, 1f, 1.1f);

        refract = new Ray(refract.point(0.01f), refract.getDirection());
        float t2 = super.intersect(refract)[1];
        if (Float.isNaN(t2) || t2 <= 0) return Color.BLACK;
        rhat = super.rhat(refract.point(t2));
        refract = refract.refract(rhat, t2, 1.1f, 1f);

        float min_dist = Float.MAX_VALUE;
        int min_index = -1;
        int index = 0;
        Color c = Color.black;

        for (Sphere sphere : spheres) {
            if (sphere == this) continue;

            float dist = sphere.touch(refract);
            if (!Float.isNaN(dist) && dist < min_dist) {
                min_dist = dist;
                min_index = index;
            }
            index++;
        }
        if(min_index != -1) c = spheres.get(min_index).colorHit(refract, spheres, n_reflections-1, min_dist);

        if(t1+t2 > 8)
            return new Color(
                (int) (c.getRed()   * 8 / (t1+t2)),
                (int) (c.getGreen() * 8 / (t1+t2)),
                (int) (c.getBlue()  * 8 / (t1+t2))
            );

        return c;
    }
}
