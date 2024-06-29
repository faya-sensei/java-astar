package org.faya.sensei.mathematics;

import java.util.Arrays;

public record Vector3(float x, float y, float z) {

    public Vector3(final float value) {
        this(value, value, value);
    }

    public static Vector3 add(final Vector3 lhs, final Vector3 rhs) {
        return new Vector3(lhs.x + rhs.x, lhs.y + rhs.y, lhs.z + rhs.z);
    }

    public static Vector3 add(final Vector3... vectors) {
        return Arrays.stream(vectors).reduce(new Vector3(0.0f), Vector3::add);
    }

    public static Vector3 subtract(final Vector3 lhs, final Vector3 rhs) {
        return new Vector3(lhs.x - rhs.x, lhs.y - rhs.y, lhs.z - rhs.z);
    }

    public static Vector3 subtract(final Vector3... vectors) {
        return Arrays.stream(vectors).skip(1).reduce(vectors[0], Vector3::subtract);
    }

    public static Vector3 multiply(final Vector3 lhs, final Vector3 rhs) {
        return new Vector3(lhs.x * rhs.x, lhs.y * rhs.y, lhs.z * rhs.z);
    }

    public static Vector3 multiply(final Vector3... vectors) {
        return Arrays.stream(vectors).reduce(new Vector3(1.0f), Vector3::multiply);
    }

    public static Vector3 divide(final Vector3 lhs, final Vector3 rhs) {
        return new Vector3(lhs.x / rhs.x, lhs.y / rhs.y, lhs.z / rhs.z);
    }

    public static Vector3 divide(final Vector3... vectors) {
        return Arrays.stream(vectors).skip(1).reduce(vectors[0], Vector3::divide);
    }

    public static float dot(final Vector3 x, final Vector3 y) {
        return (x.x * y.x) + (x.y * y.y) + (x.z * y.z);
    }

    public static Vector3 cross(final Vector3 x, final Vector3 y) {
        return new Vector3((x.y * y.z) - (x.z * y.y), (x.z * y.x) - (x.x * y.z), (x.x * y.y) - (x.y * y.x));
    }

    public static Vector3 sqrt(final Vector3 x) {
        return new Vector3((float) Math.sqrt(x.x), (float) Math.sqrt(x.y), (float) Math.sqrt(x.z));
    }

    public static Vector3 normalize(final Vector3 x) {
        return divide(x, new Vector3((float) Math.sqrt(dot(x, x))));
    }

    public static float[] toFloatArray(final Vector3 v) {
        return new float[] { v.x, v.y, v.z };
    }
}
