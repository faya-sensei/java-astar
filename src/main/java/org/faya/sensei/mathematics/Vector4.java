package org.faya.sensei.mathematics;

public record Vector4(double x, double y, double z, double w) {

    public Vector4(final double value) {
        this(value, value, value, value);
    }

    public static Vector4 add(final Vector4 a, final Vector4 b) {
        return new Vector4(a.x + b.x, a.y + b.y, a.z + b.z, a.w + b.w);
    }

    public static Vector4 subtract(final Vector4 a, final Vector4 b) {
        return new Vector4(a.x - b.x, a.y - b.y, a.z - b.z, a.w - b.w);
    }

    public static Vector4 multiply(final Vector4 a, final Vector4 b) {
        return new Vector4(a.x * b.x, a.y * b.y, a.z * b.z, a.w * b.w);
    }

    public static Vector4 divide(final Vector4 a, final Vector4 b) {
        return new Vector4(a.x / b.x, a.y / b.y, a.z / b.z, a.w / b.w);
    }

    public static float[] toArray(final Vector4 v) {
        return new float[] {
                (float) v.x, (float) v.y, (float) v.z, (float) v.w
        };
    }
}
