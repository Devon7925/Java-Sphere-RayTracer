import java.awt.Color;
import java.util.List;

class Reflect extends RenderComponent {
    public Reflect(float percent){
        super(percent);
    }

    public Color colorHit(Sphere root, Ray ray, List<Sphere> spheres, int n_reflections, float t) {
        if (n_reflections <= 0) return Color.BLACK;

        Ray reflect = ray.reflect(root.rhat(ray.point(t)), t);

        float min_dist = Float.MAX_VALUE;
        int min_index = -1;

        for (int i = 0; i < spheres.size(); i++) {
            if (spheres.get(i) == root) continue;

            Vec3 OP = spheres.get(i).getPos().sub(reflect.getOrigin());
            if(OP.mag()-spheres.get(i).radius < min_dist){
                float dotprod = OP.dot(reflect.getDirection());

                if(spheres.get(i).hit(reflect, OP, dotprod)){
                    float dist = spheres.get(i).touch(reflect, OP, dotprod);
                    if (dist < min_dist) {
                        min_dist = dist;
                        min_index = i;
                    }
                }
            }
        }

        Color c = Color.BLACK;
        if(min_index != -1) c = spheres.get(min_index).colorHit(reflect, spheres, n_reflections-1, min_dist);

        return c;
    }
}