package org.faya.sensei.mathematics;

import java.util.Arrays;

public record Vector4(float x, float y, float z, float w) {

    public Vector4(final float value) {
        this(value, value, value, value);
    }

    public Vector4(final Vector3 xyz, final float w) { this(xyz.x(), xyz.y(), xyz.z(), w); }

    /**
     * Returns the result of a component-wise addition operation on two Vector4
     * vectors.
     *
     * @param lhs Left hand side Vector4 to use to compute component-wise
     *            addition.
     * @param rhs Right hand side Vector4 to use to compute component-wise
     *            addition.
     * @return Vector4 result of the component-wise addition.
     */
    public static Vector4 add(final Vector4 lhs, final Vector4 rhs) {
        return new Vector4(lhs.x + rhs.x, lhs.y + rhs.y, lhs.z + rhs.z, lhs.w + rhs.w);
    }

    /**
     * Returns the result of a component-wise addition operation on multiple
     * Vector4 vectors.
     *
     * @param vectors The vectors use to compute component-wise addition.
     * @return Vector4 result of the component-wise addition.
     */
    public static Vector4 add(final Vector4... vectors) {
        return Arrays.stream(vectors).reduce(new Vector4(0.0f), Vector4::add);
    }

    /**
     * Returns the result of a component-wise subtraction operation on two
     * Vector4 vectors.
     *
     * @param lhs Left hand side Vector4 to use to compute component-wise
     *            subtraction.
     * @param rhs Right hand side Vector4 to use to compute component-wise
     *            subtraction.
     * @return Vector4 result of the component-wise subtraction.
     */
    public static Vector4 subtract(final Vector4 lhs, final Vector4 rhs) {
        return new Vector4(lhs.x - rhs.x, lhs.y - rhs.y, lhs.z - rhs.z, lhs.w - rhs.w);
    }

    /**
     * Returns the result of a component-wise subtraction operation on multiple
     * Vector4 vectors.
     *
     * @param vectors The vectors use to compute component-wise subtraction.
     * @return Vector4 result of the component-wise subtraction.
     */
    public static Vector4 subtract(final Vector4... vectors) {
        return Arrays.stream(vectors).skip(1).reduce(vectors[0], Vector4::subtract);
    }

    /**
     * Returns the result of a component-wise multiplication operation on two
     * Vector4 vectors.
     *
     * @param lhs Left hand side Vector4 to use to compute component-wise
     *            multiplication.
     * @param rhs Right hand side Vector4 to use to compute component-wise
     *            multiplication.
     * @return Vector4 result of the component-wise multiplication.
     */
    public static Vector4 multiply(final Vector4 lhs, final Vector4 rhs) {
        return new Vector4(lhs.x * rhs.x, lhs.y * rhs.y, lhs.z * rhs.z, lhs.w * rhs.w);
    }

    /**
     * Returns the result of a component-wise multiplication operation on
     * multiple Vector4 vectors.
     *
     * @param vectors The vectors use to compute component-wise multiplication.
     * @return Vector4 result of the component-wise multiplication.
     */
    public static Vector4 multiply(final Vector4... vectors) {
        return Arrays.stream(vectors).reduce(new Vector4(1.0f), Vector4::multiply);
    }

    /**
     * Returns the result of a component-wise division operation on two Vector4
     * vectors.
     *
     * @param lhs Left hand side Vector4 to use to compute component-wise
     *            division.
     * @param rhs Right hand side Vector4 to use to compute component-wise
     *            division。
     * @return Vector4 result of the component-wise division.
     */
    public static Vector4 divide(final Vector4 lhs, final Vector4 rhs) {
        return new Vector4(lhs.x / rhs.x, lhs.y / rhs.y, lhs.z / rhs.z, lhs.w / rhs.w);
    }

    /**
     * Returns the result of a component-wise division operation on multiple
     * Vector4 vectors.
     *
     * @param vectors The vectors use to compute component-wise division.
     * @return Vector4 result of the component-wise division.
     */
    public static Vector4 divide(final Vector4... vectors) {
        return Arrays.stream(vectors).skip(1).reduce(vectors[0], Vector4::divide);
    }

    /**
     * Returns the dot product of two Vector4 vectors.
     *
     * @param x The first vector.
     * @param y The second vector.
     * @return The dot product of two vectors.
     */
    public static float dot(final Vector4 x, final Vector4 y) {
        return (x.x * y.x) + (x.y * y.y) + (x.z * y.z) + (x.w * y.w);
    }

    /**
     * Returns the component-wise square root of a Vector4 vector.
     *
     * @param x Value to use when computing square root.
     * @return The component-wise square root.
     */
    public static Vector4 sqrt(final Vector4 x) {
        return new Vector4((float) Math.sqrt(x.x), (float) Math.sqrt(x.y), (float) Math.sqrt(x.z), (float) Math.sqrt(x.w));
    }

    /**
     * Returns the horizontal sum of components of a Vector4 vector.
     *
     * @param x The vector to use when computing the horizontal sum.
     * @return The horizontal sum of components of the vector.
     */
    public static float csum(final Vector4 x) {
        return x.x + x.y + x.z + x.w;
    }

    /**
     * Returns a normalized version of the Vector4 vector x by scaling it by
     * 1 / length(x).
     *
     * @param x Vector to normalize.
     * @return The normalized vector.
     */
    public static Vector4 normalize(final Vector4 x) {
        return divide(x, new Vector4((float) Math.sqrt(dot(x, x))));
    }

    /**
     * Returns the length of a Vector4 vector.
     *
     * @param x Vector to use when computing length.
     * @return Length of vector x.
     */
    public static float length(final Vector4 x) {
        return (float) Math.sqrt(dot(x, x));
    }

    /**
     * Return a buffer of a Vector4 vector.
     *
     * @param v The Vector4 vector.
     * @return The float buffer.
     */
    public static float[] toFloatArray(final Vector4 v) {
        return new float[] { v.x, v.y, v.z, v.w };
    }
}
