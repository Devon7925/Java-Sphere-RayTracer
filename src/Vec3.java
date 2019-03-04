public class Vec3 {
    private float x, y, z;

    public Vec3(){
        x = y = z = 0f;
    }

    public Vec3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3(Vec3 vec) {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
    }

    public Vec3 scale(float scalar) {
        return new Vec3(x * scalar, y * scalar, z * scalar);
    }

    public Vec3 div(float scalar) {
        return new Vec3(x / scalar, y / scalar, z / scalar);
    }

    public Vec3 add(Vec3... vecs) {
        float X = x, Y = y, Z = z;
        for (Vec3 v : vecs) {
            X += v.x;
            Y += v.y;
            Z += v.z;
        }
        return new Vec3(X,Y,Z);
    }

    public Vec3 rotateX(float theta) {
        float sin = (float) Math.sin(theta);
        float cos = (float) Math.cos(theta);
        float Y = cos * y - sin*z;
        float Z = sin * y + cos*z;
        return new Vec3(x, Y, Z);
    }
    public Vec3 rotateY(float theta) {
        float sin = (float) Math.sin(theta);
        float cos = (float) Math.cos(theta);
        float X = cos * x + sin * y;
        float Z = cos * z - sin * x;
        return new Vec3(X, y, Z);
    }
    public Vec3 rotateZ(float theta){
        float sin = (float) Math.sin(theta);
        float cos = (float) Math.cos(theta);
        float X = cos * x - sin * y;
        float Y = sin * x + cos * y;
        return new Vec3(X, Y, z);
    }

    public Vec3 sub(Vec3... vecs) {
        float X = x, Y = y, Z = z;
        for (Vec3 v : vecs) {
            X -= v.x;
            Y -= v.y;
            Z -= v.z;
        }
        return new Vec3(X,Y,Z);
    }

    public Vec3 proj(Vec3 B) {
        return B.scale(dot(B) / B.dot(B));
    }

    public Vec3 negate() {
        return scale(-1);
    }

    public float dot(Vec3 vec) {
        return x*vec.x + y*vec.y + z*vec.z;
    }

    public Vec3 unit() {
        return div(mag());
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public float getZ() { return z; }

    public boolean isZero() { return x == 0 && y == 0 && z == 0; }

    public float mag() {
        return (float) Math.sqrt(x*x + y*y + z*z);
    }

    public Vec3 cross(Vec3 vec) {
        return new Vec3(
            y * vec.z - z * vec.y,
            z * vec.x - x * vec.z,
            x * vec.y - y * vec.x
        );
    }

    public String toString() {
        return String.format("Vec3(%.2f, %.2f, %.2f)", x, y, z);
    }
}
