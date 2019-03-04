
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;

public class Camera {
    private BufferedImage image;
    private short[][] optimizer;
    private Vec3 position;
    private Vec3 direction;
    private Vec3 UP;
    private Vec3 RIGHT;

    private final int FOV;
    private final int optdiff;
    private int width, height;


    public Camera(int width, int height, Vec3 position) {
        FOV = 60;
        optdiff = 10;

        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        optimizer = new short[width/optdiff + 1][height/optdiff + 1];
        this.width = width;
        this.height = height;

        this.position = position;
        space_default();

        if (direction.dot(UP) != 0 || direction.isZero() || UP.isZero())
            throw new IllegalArgumentException("Direction and UP must be perpendicular and non-zero");
    }

    public void move_right(float t) {
        position = position.add(RIGHT.scale(t));
    }
    public void move_up(float t) {
        position = position.add(UP.scale(t));
    }
    public void move_forward(float t) {
        position = position.add(direction.scale(t));
    }

    public void space_default() {
        direction = new Vec3(1f, 0f, 0f);
        UP = new Vec3(0f, 0f, 1f);
        RIGHT = direction.cross(UP);
    }

    public void pitch(float t) {
        float sin = (float) Math.sin(t);
        float cos = (float) Math.cos(t);
        direction = direction.scale(cos).add(UP.scale(-sin)).unit();
        UP        = direction.scale(sin).add(UP.scale( cos)).unit();
    }

    public void yaw(float t) {
        float sin = (float) Math.sin(t);
        float cos = (float) Math.cos(t);
        direction = direction.scale( cos).add(RIGHT.scale(sin)).unit();
        RIGHT     = direction.scale(-sin).add(RIGHT.scale(cos)).unit();
    }

    public void roll(float t) {
        float sin = (float) Math.sin(t);
        float cos = (float) Math.cos(t);
        RIGHT = RIGHT.scale( cos).add(UP.scale(sin)).unit();
        UP    = RIGHT.scale(-sin).add(UP.scale(cos)).unit();
    }

    public void render_perspective(List<Sphere> spheres, Graphics graphics) {
        float canvas_distance = (float) (1.0 / Math.tan( Math.toRadians(FOV) / 2 ));
        Vec3 canvas_center = position.add(direction.scale(-canvas_distance));

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB((int) x,(int) y, getPixel(x, y, spheres, canvas_center).getRGB());
            }
        }

        graphics.drawImage(image, 0, 0, null);
    }

    public Color getPixel(int x, int y, List<Sphere> spheres, Vec3 canvas_center){
        Ray ray = new Ray(position, position.sub(canvas_center).add(
            UP.scale((height - (float) y)/width),
            RIGHT.scale((float) x/width - 1)
        ));

        float min_dist = Float.MAX_VALUE;
        short min_index = optimizer[(int) x / optdiff][(int) y / optdiff];

        Vec3 OP = spheres.get(min_index).getPos().sub(ray.getOrigin());
        float dotprod = OP.dot(ray.getDirection());

        if(spheres.get(min_index).hit(ray, OP, dotprod)){
            float dist = spheres.get(min_index).touch(ray, OP, dotprod);
            if (!Float.isNaN(dist)) {
                min_dist = dist;
            }
        }

        for (short i = 0; i < spheres.size(); i++) {
            if(i == min_index) continue;
            OP = spheres.get(i).getPos().sub(ray.getOrigin());
            if(OP.mag()-spheres.get(i).radius < min_dist){
                dotprod = OP.dot(ray.getDirection());

                if(spheres.get(i).hit(ray, OP, dotprod)){
                    float dist = spheres.get(i).touch(ray, OP, dotprod);
                    if (!Float.isNaN(dist) && dist < min_dist) {
                        min_dist = dist;
                        min_index = i;
                    }
                }
            }
        }
        optimizer[(int) x / optdiff][(int) y / optdiff] = min_index;
        return spheres.get(min_index).colorHit(ray, spheres, 7, min_dist);
    }
}
