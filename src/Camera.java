
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Camera {
    private BufferedImage image;
    private BufferedImage optImage;
    private Vec3 position;
    private Vec3 direction;
    private Vec3 UP;
    private Vec3 RIGHT;

    private final int FOV;
    private int width, height;

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
        position = new Vec3(0f, 0f, 2f);
    }

    public void pitch(float t) {
        float sin = (float) Math.sin(t);
        float cos = (float) Math.cos(t);
        direction = direction.scale(cos).add(UP.scale(-sin)).unit();
        UP = direction.scale(sin).add(UP.scale(cos)).unit();
    }

    public void yaw(float t) {
        float sin = (float) Math.sin(t);
        float cos = (float) Math.cos(t);
        direction = direction.scale(cos).add(RIGHT.scale(sin)).unit();
        RIGHT = direction.scale(-sin).add(RIGHT.scale(cos)).unit();
    }

    public void roll(float t) {
        float sin = (float) Math.sin(t);
        float cos = (float) Math.cos(t);
        RIGHT = RIGHT.scale(cos).add(UP.scale(sin)).unit();
        UP = RIGHT.scale(-sin).add(UP.scale(cos)).unit();
    }

    public Camera(int width, int height, Vec3 position, Vec3 direction, Vec3 UP) {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        optImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        this.width = width;
        this.height = height;
        FOV = 60;

        this.position = position;
        this.direction = direction.unit();
        this.UP = UP.unit();

        if (direction.dot(UP) != 0 || direction.isZero() || UP.isZero())
            throw new IllegalArgumentException("Direction and UP must be perpendicular and non-zero");

        this.RIGHT = direction.cross(UP).unit();
    }

    public void render_perspective(ArrayList<Sphere> spheres, Graphics graphics) {

        float canvas_distance = (float) (1.0 / Math.tan( Math.toRadians(FOV) / 2 ));
        Vec3 canvas_center = position.add(direction.scale(-canvas_distance));

        for (float x = 0; x < width; x++) {
            for (float y = 0; y < height; y++) {
                float UPScale = (height - y)/width;
                float RIGHTScale = x/width - 1;

                Ray ray = new Ray(position, position.sub(canvas_center).add(
                    UP.scale(UPScale),
                    RIGHT.scale(RIGHTScale)
                ));

                float min_dist = Float.MAX_VALUE;
                int min_index = optImage.getRGB((int) x, (int) y);

                Vec3 OP = spheres.get(min_index).getPos().sub(ray.getOrigin());
                float dotprod = OP.dot(ray.getDirection());

                if(spheres.get(min_index).hit(ray, OP, dotprod)){
                    float dist = spheres.get(min_index).touch(ray, OP, dotprod);
                    if (!Float.isNaN(dist)) {
                        min_dist = dist;
                    }
                }

                for (int i = 0; i < spheres.size(); i++) {
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
                optImage.setRGB((int) x, (int) y, min_index);
                image.setRGB((int) x,(int) y, 
                    (min_index == -1?
                        Color.BLACK:
                        spheres.get(min_index).colorHit(ray, spheres, 7, min_dist)
                    ).getRGB()
                );
            }
        }

        graphics.drawImage(image, 0, 0, null);
    }

    public BufferedImage getImage() {
        return image;
    }
}
