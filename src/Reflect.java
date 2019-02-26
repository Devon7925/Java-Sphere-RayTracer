import java.awt.Color;
import java.util.ArrayList;

class Reflect extends RenderComponent {
    public Reflect(float percent){
        super(percent);
    }

    public Color colorHit(Sphere root, Ray ray, ArrayList<Sphere> spheres, int n_reflections, float t) {
        if (n_reflections <= 0) return Color.BLACK;
        if (Float.isNaN(t) || t <= 0) return Color.BLACK;

        Vec3 rhat = root.rhat(ray.point(t));
        Ray reflect = ray.reflect(rhat, t);








        
        float min_dist = Float.MAX_VALUE;
        int min_index = -1;
        int index = -1;
        Color c = Color.BLACK;

        for (Sphere sphere : spheres) {
            index++;
            if (sphere == root) continue;

            float dist = sphere.touch(reflect);
            if (!Float.isNaN(dist) && dist < min_dist) {
                min_dist = dist;
                min_index = index;
            }
        }
        if(min_index != -1) {
            c = spheres.get(min_index).colorHit(reflect, spheres, n_reflections-1, min_dist);
        }

        t = t + min_dist;
        if(t > 8)
            return new Color(
                (int) (c.getRed()   * 8 / t),
                (int) (c.getGreen() * 8 / t),
                (int) (c.getBlue()  * 8 / t)
            );

        return c;
    }
}