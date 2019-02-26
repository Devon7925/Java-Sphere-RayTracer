import java.awt.Color;
import java.util.ArrayList;

abstract class RenderComponent {
    float percent;

    public RenderComponent(float percent){
        this.percent = percent;
    }

    public abstract Color colorHit(Sphere root, Ray ray, ArrayList<Sphere> spheres, int n_reflections, float t);
}