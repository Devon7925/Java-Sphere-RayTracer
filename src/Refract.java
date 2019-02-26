import java.awt.Color;
import java.util.ArrayList;

class Refract extends RenderComponent {
    public Refract(float percent){
        super(percent);
    }

    public Color colorHit(Sphere root, Ray ray, ArrayList<Sphere> spheres, int n_reflections, float t1) {
        if (n_reflections <= 0) return Color.BLACK;
        if (Float.isNaN(t1) || t1 <= 0) return Color.BLACK;

        Vec3 t1_point = ray.point(t1);
        Vec3 rhat = root.rhat(t1_point);
        Ray refract = ray.refract(rhat, t1_point, 1f, 1.1f);

        refract = new Ray(refract.point(0.01f), refract.getDirection());
        float t2 = root.intersect(refract)[1];
        if (Float.isNaN(t2) || t2 <= 0) return Color.BLACK;
        Vec3 t2_point = refract.point(t2);
        rhat = root.rhat(t2_point);
        refract = refract.refract(rhat, t2_point, 1.1f, 1f);

        float min_dist = Float.MAX_VALUE;
        int min_index = -1;
        int index = -1;
        Color c = Color.black;

        for (Sphere sphere : spheres) {
            index++;
            if (sphere == root) continue;

            float dist = sphere.touch(refract);
            if (!Float.isNaN(dist) && dist < min_dist) {
                min_dist = dist;
                min_index = index;
            }
        }
        if(min_index != -1) c = spheres.get(min_index).colorHit(refract, spheres, n_reflections-1, min_dist);

        float t = t1 + t2;
        if(t > 8)
            return new Color(
                (int) (c.getRed()   * 8 / t),
                (int) (c.getGreen() * 8 / t),
                (int) (c.getBlue()  * 8 / t)
            );

        return c;
    }
}