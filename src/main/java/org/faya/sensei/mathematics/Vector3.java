package org.faya.sensei.mathematics;

public record Vector3(double x, double y, double z) {

    public Vector3(final double value) {
        this(value, value, value);
    }

    public static Vector3 add(final Vector3 a, final Vector3 b) {
        return new Vector3(a.x + b.x, a.y + b.y, a.z + b.z);
    }

    public static Vector3 subtract(final Vector3 a, final Vector3 b) {
        return new Vector3(a.x - b.x, a.y - b.y, a.z - b.z);
    }

    public static Vector3 multiply(final Vector3 a, final Vector3 b) {
        return new Vector3(a.x * b.x, a.y * b.y, a.z * b.z);
    }

    public static Vector3 divide(final Vector3 a, final Vector3 b) {
        return new Vector3(a.x / b.x, a.y / b.y, a.z / b.z);
    }

    public static double dot(final Vector3 a, final Vector3 b) {
        return (a.x * b.x) + (a.y * b.y) + (a.z * b.z);
    }

    public static Vector3 cross(final Vector3 a, final Vector3 b) {
        return new Vector3((a.y * b.z) - (a.z * b.y), (a.z * b.x) - (a.x * b.z), (a.x * b.y) - (a.y * b.x));
    }

    public static Vector3 sqrt(final Vector3 v) {
        return new Vector3(Math.sqrt(v.x), Math.sqrt(v.y), Math.sqrt(v.z));
    }

    public static Vector3 normalize(final Vector3 v) {
        return divide(v, new Vector3(Math.sqrt(dot(v, v))));
    }

    public static float[] toFloatArray(final Vector3 v) {
        return new float[] {
                (float) v.x, (float) v.y, (float) v.z
        };
    }
}
