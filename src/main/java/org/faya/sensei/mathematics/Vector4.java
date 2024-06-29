package org.faya.sensei.mathematics;

import java.util.Arrays;

public record Vector4(float x, float y, float z, float w) {

    public Vector4(final float value) {
        this(value, value, value, value);
    }

    public Vector4(final Vector3 xyz, final float w) { this(xyz.x(), xyz.y(), xyz.z(), w); }

    public static Vector4 add(final Vector4 lhs, final Vector4 rhs) {
        return new Vector4(lhs.x + rhs.x, lhs.y + rhs.y, lhs.z + rhs.z, lhs.w + rhs.w);
    }

    public static Vector4 add(final Vector4... vectors) {
        return Arrays.stream(vectors).reduce(new Vector4(0.0f), Vector4::add);
    }

    public static Vector4 subtract(final Vector4 lhs, final Vector4 rhs) {
        return new Vector4(lhs.x - rhs.x, lhs.y - rhs.y, lhs.z - rhs.z, lhs.w - rhs.w);
    }

    public static Vector4 subtract(final Vector4... vectors) {
        return Arrays.stream(vectors).skip(1).reduce(vectors[0], Vector4::subtract);
    }

    public static Vector4 multiply(final Vector4 lhs, final Vector4 rhs) {
        return new Vector4(lhs.x * rhs.x, lhs.y * rhs.y, lhs.z * rhs.z, lhs.w * rhs.w);
    }

    public static Vector4 multiply(final Vector4... vectors) {
        return Arrays.stream(vectors).reduce(new Vector4(1.0f), Vector4::multiply);
    }

    public static Vector4 multiply(final Vector4 lhs, final float rhs) {
        return new Vector4(lhs.x * rhs, lhs.y * rhs, lhs.z * rhs, lhs.w * rhs);
    }

    public static Vector4 divide(final Vector4 lhs, final Vector4 rhs) {
        return new Vector4(lhs.x / rhs.x, lhs.y / rhs.y, lhs.z / rhs.z, lhs.w / rhs.w);
    }

    public static Vector4 divide(final Vector4... vectors) {
        return Arrays.stream(vectors).skip(1).reduce(vectors[0], Vector4::divide);
    }

    public static float dot(final Vector4 x, final Vector4 y) {
        return (x.x * y.x) + (x.y * y.y) + (x.z * y.z) + (x.w * y.w);
    }

    public static Vector4 sqrt(final Vector4 x) {
        return new Vector4((float) Math.sqrt(x.x), (float) Math.sqrt(x.y), (float) Math.sqrt(x.z), (float) Math.sqrt(x.w));
    }

    public static Vector4 normalize(final Vector4 x) {
        return divide(x, new Vector4((float) Math.sqrt(dot(x, x))));
    }

    public static float[] toArray(final Vector4 v) {
        return new float[] { v.x, v.y, v.z, v.w };
    }
}
