import java.awt.Color;
import java.util.ArrayList;

class ColorReflect extends RenderComponent {

    protected Color color;

    public ColorReflect(Color color){
        super(1);
        this.color = color;
    }

    @Override
    public Color colorHit(Sphere root, Ray ray, ArrayList<Sphere> spheres, int n_reflections, float t) {
        if (Float.isNaN(t)) return Color.black; 

        if(t > 8)
            return new Color(
                (int) (color.getRed()   * 8 / t),
                (int) (color.getGreen() * 8 / t),
                (int) (color.getBlue()  * 8 / t)
            );

        return color;
    }

    public Color getColor() { return color; }
    public void setColor(Color color) { this.color = color; }
}