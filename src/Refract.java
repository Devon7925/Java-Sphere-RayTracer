import java.awt.Color;
import java.util.List;

class Refract extends RenderComponent {
    public Refract(float percent){
        super(percent);
    }

    public Color colorHit(Sphere root, Ray ray, List<Sphere> spheres, int n_reflections, float t1) {
        if (n_reflections <= 0) return Color.BLACK;

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

        for (int i = 0; i < spheres.size(); i++) {
            if (spheres.get(i) == root) continue;
            
            Vec3 OP = spheres.get(i).getPos().sub(refract.getOrigin());
            if(OP.mag()-spheres.get(i).radius < min_dist){
                float dotprod = OP.dot(refract.getDirection());

                if(spheres.get(i).hit(refract, OP, dotprod)){
                    float dist = spheres.get(i).touch(refract, OP, dotprod);
                    if (dist < min_dist) {
                        min_dist = dist;
                        min_index = i;
                    }
                }
            }
        }

        Color c = Color.black;
        if(min_index != -1) c = spheres.get(min_index).colorHit(refract, spheres, n_reflections-1, min_dist);

        return c;
    }
}