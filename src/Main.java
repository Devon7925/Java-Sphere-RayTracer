import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;

public class Main extends JPanel implements KeyListener {
    private static final long serialVersionUID = 1L;
    private JFrame frame;
    private Camera camera;
    private ArrayList<Sphere> renderables;

    private boolean move_front = false;
    private boolean move_left = false;
    private boolean move_back = false;
    private boolean move_right = false;
    private boolean move_up = false;
    private boolean move_down = false;

    private boolean look_right = false;
    private boolean look_left = false;
    private boolean look_up = false;
    private boolean look_down = false;
    private boolean look_count = false;
    private boolean look_clock = false;

    private float move_velocity = 0.2f;
    private float rot_velocity = 0.1f;

    @Override
    public void keyTyped(KeyEvent keyEvent) {}

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        int key = keyEvent.getKeyCode();
        switch (key) {
            case KeyEvent.VK_W:     move_front = true; break;
            case KeyEvent.VK_A:     move_left  = true; break;
            case KeyEvent.VK_S:     move_back  = true; break;
            case KeyEvent.VK_D:     move_right = true; break;
            case KeyEvent.VK_SPACE: move_up    = true; break;
            case KeyEvent.VK_SHIFT: move_down  = true; break;

            case KeyEvent.VK_RIGHT: look_right = true; break;
            case KeyEvent.VK_LEFT:  look_left  = true; break;
            case KeyEvent.VK_UP:    look_up    = true; break;
            case KeyEvent.VK_DOWN:  look_down  = true; break;

            case KeyEvent.VK_Z:     look_count = true; break;
            case KeyEvent.VK_C:     look_clock = true; break;
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        int key = keyEvent.getKeyCode();
        switch (key) {
            case KeyEvent.VK_W:     move_front = false; break;
            case KeyEvent.VK_A:     move_left  = false; break;
            case KeyEvent.VK_S:     move_back  = false; break;
            case KeyEvent.VK_D:     move_right = false; break;
            case KeyEvent.VK_SPACE: move_up    = false; break;
            case KeyEvent.VK_SHIFT: move_down  = false; break;

            case KeyEvent.VK_RIGHT: look_right = false; break;
            case KeyEvent.VK_LEFT:  look_left  = false; break;
            case KeyEvent.VK_UP:    look_up    = false; break;
            case KeyEvent.VK_DOWN:  look_down  = false; break;

            case KeyEvent.VK_Z:     look_count = false; break;
            case KeyEvent.VK_C:     look_clock = false; break;
            
            case KeyEvent.VK_P: camera.space_default(); break;
            case KeyEvent.VK_ESCAPE: System.exit(0);    break;
        }
    }


    @Override
    public void paint(Graphics graphics) {
        // camera movement
        if (move_front) camera.move_forward(move_velocity);
        if (move_back)  camera.move_forward(-move_velocity);
        if (move_right) camera.move_right(move_velocity);
        if (move_left)  camera.move_right(-move_velocity);
        if (move_up)    camera.move_up(move_velocity);
        if (move_down)  camera.move_up(-move_velocity);

        // camera rotation
        if (look_right) camera.yaw(rot_velocity);
        if (look_left)  camera.yaw(-rot_velocity);
        if (look_down)  camera.pitch(rot_velocity);
        if (look_up)    camera.pitch(-rot_velocity);
        if (look_count) camera.roll(rot_velocity);
        if (look_clock) camera.roll(-rot_velocity);

        camera.render_perspective(renderables);
        graphics.drawImage(camera.getImage(), 0, 0, null);
        repaint();

        Sphere s1 = (Sphere) renderables.get(0);
        Sphere s2 = (Sphere) renderables.get(1);
        s1.setPos(s1.getPos().rotateZ(rot_velocity));
        s2.setPos(s2.getPos().rotateZ(rot_velocity));
    }

    private Main(int width, int height) {
        assert(width > 0 && height > 0);

        this.setSize(width, height);

        frame = new JFrame("Render test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.addKeyListener(this);
        frame.setSize(width+2, height + 24);
        frame.setResizable(false);
        frame.setVisible(true);
        this.addKeyListener(this);

        Vec3 camera_pos = new Vec3(0, 0, 2);
        Vec3 camera_dir = new Vec3(1f,0f,0f).unit();
        Vec3 camera_up  = new Vec3(0f,0f,1f);

        Sphere s1 = new Sphere(1f, Color.GREEN, new Vec3(0f, -2.1f,2f));
        Sphere s2 = new Sphere(1f, Color.BLUE, new Vec3(0f, 2.1f,2f));
        Sphere s3 = new Sphere(19f, Color.GRAY, new Vec3(0f, 0f, -20f));
        MirrorSphere ms1 = new MirrorSphere(1f, new Vec3(0f, 0f, 2f));
        LensSphere ms3 = new LensSphere(1.2f, new Vec3(-7f, 0f, 2f));

        renderables = new ArrayList<>(Arrays.asList(s1, s2, s3, ms1, ms3));

        camera = new Camera(
                width, height,
                camera_pos,
                camera_dir,
                camera_up
        );
    }


    public static void image_demo() {
        int width = 1000;
        int height =  width * 2 / 3;
        new Main(width, height);
    }

    public static void main(String[] args) {
        image_demo();
    }


}
