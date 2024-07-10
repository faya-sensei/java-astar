package org.faya.sensei.mathematics;

import java.util.Arrays;

public record Vector3(float x, float y, float z) {

    public Vector3(final float value) {
        this(value, value, value);
    }

    /**
     * Returns the result of a component-wise addition operation on two Vector3
     * vectors.
     *
     * @param lhs Left hand side Vector3 to use to compute component-wise
     *            addition.
     * @param rhs Right hand side Vector3 to use to compute component-wise
     *            addition.
     * @return Vector3 result of the component-wise addition.
     */
    public static Vector3 add(final Vector3 lhs, final Vector3 rhs) {
        return new Vector3(lhs.x + rhs.x, lhs.y + rhs.y, lhs.z + rhs.z);
    }

    /**
     * Returns the result of a component-wise addition operation on multiple
     * Vector3 vectors.
     *
     * @param vectors The vectors use to compute component-wise addition.
     * @return Vector3 result of the component-wise addition.
     */
    public static Vector3 add(final Vector3... vectors) {
        return Arrays.stream(vectors).reduce(new Vector3(0.0f), Vector3::add);
    }

    /**
     * Returns the result of a component-wise subtraction operation on two
     * Vector3 vectors.
     *
     * @param lhs Left hand side Vector3 to use to compute component-wise
     *            subtraction.
     * @param rhs Right hand side Vector3 to use to compute component-wise
     *            subtraction.
     * @return Vector3 result of the component-wise subtraction.
     */
    public static Vector3 subtract(final Vector3 lhs, final Vector3 rhs) {
        return new Vector3(lhs.x - rhs.x, lhs.y - rhs.y, lhs.z - rhs.z);
    }

    /**
     * Returns the result of a component-wise subtraction operation on multiple
     * Vector3 vectors.
     *
     * @param vectors The vectors use to compute component-wise subtraction.
     * @return Vector3 result of the component-wise subtraction.
     */
    public static Vector3 subtract(final Vector3... vectors) {
        return Arrays.stream(vectors).skip(1).reduce(vectors[0], Vector3::subtract);
    }

    /**
     * Returns the result of a component-wise multiplication operation on two
     * Vector3 vectors.
     *
     * @param lhs Left hand side Vector3 to use to compute component-wise
     *            multiplication.
     * @param rhs Right hand side Vector3 to use to compute component-wise
     *            multiplication.
     * @return Vector3 result of the component-wise multiplication.
     */
    public static Vector3 multiply(final Vector3 lhs, final Vector3 rhs) {
        return new Vector3(lhs.x * rhs.x, lhs.y * rhs.y, lhs.z * rhs.z);
    }

    /**
     * Returns the result of a component-wise multiplication operation on
     * multiple Vector3 vectors.
     *
     * @param vectors The vectors use to compute component-wise multiplication.
     * @return Vector3 result of the component-wise multiplication.
     */
    public static Vector3 multiply(final Vector3... vectors) {
        return Arrays.stream(vectors).reduce(new Vector3(1.0f), Vector3::multiply);
    }

    /**
     * Returns the result of a component-wise division operation on two Vector3
     * vectors.
     *
     * @param lhs Left hand side Vector3 to use to compute component-wise
     *            division.
     * @param rhs Right hand side Vector3 to use to compute component-wise
     *            division。
     * @return Vector3 result of the component-wise division.
     */
    public static Vector3 divide(final Vector3 lhs, final Vector3 rhs) {
        return new Vector3(lhs.x / rhs.x, lhs.y / rhs.y, lhs.z / rhs.z);
    }

    /**
     * Returns the result of a component-wise division operation on multiple
     * Vector3 vectors.
     *
     * @param vectors The vectors use to compute component-wise division.
     * @return Vector3 result of the component-wise division.
     */
    public static Vector3 divide(final Vector3... vectors) {
        return Arrays.stream(vectors).skip(1).reduce(vectors[0], Vector3::divide);
    }

    /**
     * Returns the dot product of two Vector3 vectors.
     *
     * @param x The first vector.
     * @param y The second vector.
     * @return The dot product of two vectors.
     */
    public static float dot(final Vector3 x, final Vector3 y) {
        return (x.x * y.x) + (x.y * y.y) + (x.z * y.z);
    }

    /**
     * Returns the cross product of two Vector3 vectors.
     *
     * @param x The first vector to use in cross product.
     * @param y The second vector to use in cross product.
     * @return The cross product of two vectors.
     */
    public static Vector3 cross(final Vector3 x, final Vector3 y) {
        return new Vector3((x.y * y.z) - (x.z * y.y), (x.z * y.x) - (x.x * y.z), (x.x * y.y) - (x.y * y.x));
    }

    /**
     * Returns the component-wise square root of a Vector3 vector.
     *
     * @param x Value to use when computing square root.
     * @return The component-wise square root.
     */
    public static Vector3 sqrt(final Vector3 x) {
        return new Vector3((float) Math.sqrt(x.x), (float) Math.sqrt(x.y), (float) Math.sqrt(x.z));
    }

    /**
     * Returns a normalized version of the Vector3 vector x by scaling it by
     * 1 / length(x).
     *
     * @param x Vector to normalize.
     * @return The normalized vector.
     */
    public static Vector3 normalize(final Vector3 x) {
        return divide(x, new Vector3((float) Math.sqrt(dot(x, x))));
    }

    /**
     * Returns the length of a Vector3 vector.
     *
     * @param x Vector to use when computing length.
     * @return Length of vector x.
     */
    public static float length(final Vector3 x) {
        return (float) Math.sqrt(dot(x, x));
    }

    /**
     * Return a buffer of a Vector3 vector.
     *
     * @param v The Vector3 vector.
     * @return The float buffer.
     */
    public static float[] toFloatArray(final Vector3 v) {
        return new float[] { v.x, v.y, v.z };
    }
}
