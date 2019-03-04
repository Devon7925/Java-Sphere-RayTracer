import java.awt.Color;
import java.util.List;

abstract class RenderComponent {
    float percent;

    public RenderComponent(float percent){
        this.percent = percent;
    }

    public abstract Color colorHit(Sphere root, Ray ray, List<Sphere> spheres, int n_reflections, float t);
}