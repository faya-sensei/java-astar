package org.faya.sensei.mathematics;

public record Quaternion(float x, float y, float z, float w) {

    public static Quaternion add(final Quaternion lhs, final Quaternion rhs) {
        return new Quaternion(lhs.x + rhs.x, lhs.y + rhs.y, lhs.z + rhs.z, lhs.w + rhs.w);
    }

    public static Quaternion subtract(final Quaternion lhs, final Quaternion rhs) {
        return new Quaternion(lhs.x - rhs.x, lhs.y - rhs.y, lhs.z - rhs.z, lhs.w - rhs.w);
    }

    public static Quaternion multiply(final Quaternion lhs, final Quaternion rhs) {
        final float cx = lhs.y * rhs.z - lhs.z * rhs.y;
        final float cy = lhs.z * rhs.x - lhs.x * rhs.z;
        final float cz = lhs.x * rhs.y - lhs.y * rhs.x;

        final float dot = lhs.x * rhs.x + lhs.y * rhs.y + lhs.z * rhs.z;

        return new Quaternion(
                lhs.x * rhs.w + rhs.x * lhs.w + cx,
                lhs.y * rhs.w + rhs.y * lhs.w + cy,
                lhs.z * rhs.w + rhs.z * lhs.w + cz,
                lhs.w * rhs.w - dot
        );
    }

    public static Vector3 multiply(final Quaternion a, final Vector3 b) {
        final Vector3 value = new Vector3(a.x, a.y, a.z);

        final Vector3 t = Vector3.multiply(new Vector3(2.0f), Vector3.cross(value, b));

        return Vector3.add(b, Vector3.multiply(new Vector3(a.w), t), Vector3.cross(value, t));
    }

    public static Quaternion divide(final Quaternion lhs, final Quaternion rhs) {
        final float ls = rhs.x * rhs.x + rhs.y * rhs.y + rhs.z * rhs.z + rhs.w * rhs.w;
        final float invNorm = 1.0f / ls;

        final Quaternion invQ = new Quaternion(
                -rhs.x * invNorm,
                -rhs.y * invNorm,
                -rhs.z * invNorm,
                 rhs.w * invNorm
        );

        return multiply(lhs, invQ);
    }

    public static float dot(Quaternion a, Quaternion b) {
        return a.x * b.x + a.y * b.y + a.z * b.z + a.w * b.w;
    }

    public static Quaternion normalize(final Quaternion x) {
        final Vector4 v = new Vector4(x.x, x.y, x.z, x.w);
        final float length = (float) Math.sqrt(Vector4.dot(v, v));

        return new Quaternion(x.x / length, x.y / length, x.z / length, x.w / length);
    }

    public enum RotationOrder {XYZ, XZY, YXZ, YZX, ZXY, ZYX}

    /**
     * Returns a quaternion constructed by first performing 3 rotations around
     * the principal axes in a given order. All rotation angles are in radians
     * and clockwise when looking along the rotation axis towards the origin.
     *
     * @param xyz   A vector containing the rotation angles around the x-, y-
     *              and z-axis measures in radians.
     * @param order The order in which the rotations are applied.
     * @return The quaternion representing the Euler angle rotation in the
     * specified order.
     */
    public static Quaternion fromEuler(final Vector3 xyz, final RotationOrder order) {
        final Vector3 half = Vector3.multiply(xyz, new Vector3(0.5f));
        final Vector3 s = new Vector3((float) Math.sin(half.x()), (float) Math.sin(half.y()), (float) Math.sin(half.z()));
        final Vector3 c = new Vector3((float) Math.cos(half.x()), (float) Math.cos(half.y()), (float) Math.cos(half.z()));

        return switch (order) {
             case XYZ -> new Quaternion(
                    s.x() * c.y() * c.z() - s.y() * s.z() * c.x(),
                    s.y() * c.x() * c.z() + s.x() * s.z() * c.y(),
                    s.z() * c.x() * c.y() - s.x() * s.y() * c.z(),
                    c.x() * c.y() * c.z() + s.y() * s.z() * s.x()
            );
            case XZY -> new Quaternion(
                    s.x() * c.y() * c.z() + s.y() * s.z() * c.x(),
                    s.y() * c.x() * c.z() + s.x() * s.z() * c.y(),
                    s.z() * c.x() * c.y() - s.x() * s.y() * c.z(),
                    c.x() * c.y() * c.z() - s.y() * s.z() * s.x()
            );
            case YXZ -> new Quaternion(
                    s.x() * c.y() * c.z() - s.y() * s.z() * c.x(),
                    s.y() * c.x() * c.z() + s.x() * s.z() * c.y(),
                    s.z() * c.x() * c.y() + s.x() * s.y() * c.z(),
                    c.x() * c.y() * c.z() - s.y() * s.z() * s.x()
            );
            case YZX -> new Quaternion(
                    s.x() * c.y() * c.z() - s.y() * s.z() * c.x(),
                    s.y() * c.x() * c.z() - s.x() * s.z() * c.y(),
                    s.z() * c.x() * c.y() + s.x() * s.y() * c.z(),
                    c.x() * c.y() * c.z() + s.y() * s.z() * s.x()
            );
            case ZXY -> new Quaternion(
                    s.x() * c.y() * c.z() + s.y() * s.z() * c.x(),
                    s.y() * c.x() * c.z() - s.x() * s.z() * c.y(),
                    s.z() * c.x() * c.y() - s.x() * s.y() * c.z(),
                    c.x() * c.y() * c.z() + s.y() * s.z() * s.x()
            );
            case ZYX -> new Quaternion(
                    s.x() * c.y() * c.z() + s.y() * s.z() * c.x(),
                    s.y() * c.x() * c.z() - s.x() * s.z() * c.y(),
                    s.z() * c.x() * c.y() + s.x() * s.y() * c.z(),
                    c.x() * c.y() * c.z() - s.y() * s.x() * s.z()
            );
        };
    }

    /**
     * Returns the Euler angles (in radians) corresponding to a quaternion.
     *
     * @param value The quaternion to be converted.
     * @return A vector containing the Euler angles around the x-, y-, and
     * z-axes in radians.
     */
    public static Vector3 toEuler(final Quaternion value) {
        final float sqw = value.w * value.w;
        final float sqx = value.x * value.x;
        final float sqy = value.y * value.y;
        final float sqz = value.z * value.z;

        final float unit = sqx + sqy + sqz + sqw;
        final float test = value.x * value.y + value.z * value.w;

        if (test > 0.499f * unit) {
            return new Vector3((float) Math.atan2(value.x, value.w), (float) Math.PI / 2.0f, 0.0f);
        }
        if (test < -0.499f * unit) {
            return new Vector3(-2.0f * (float) Math.atan2(value.x, value.w), (float) -Math.PI / 2.0f, 0.0f);
        }

        return new Vector3(
                (float) Math.atan2(2.0f * value.y * value.w - 2.0f * value.x * value.z, sqx - sqy - sqz + sqw),
                (float) Math.asin(2.0f * test / unit),
                (float) Math.atan2(2.0f * value.x * value.w - 2.0f * value.y * value.z, -sqx + sqy - sqz + sqw)
        );
    }

    /**
     * Returns a quaternion that rotates around the x-axis by a given number of
     * radians.
     *
     * @param angle The clockwise rotation angle when looking along the x-axis
     *              towards the origin in radians.
     * @return The quaternion representing a rotation around the x-axis.
     */
    public static Quaternion rotateX(final float angle) {
        final float half = angle * 0.5f;
        final float s = (float) Math.sin(half);
        final float c = (float) Math.cos(half);

        return new Quaternion(s, 0.0f, 0.0f, c);
    }

    /**
     * Returns a quaternion that rotates around the y-axis by a given number of
     * radians.
     *
     * @param angle The clockwise rotation angle when looking along the y-axis
     *              towards the origin in radians.
     * @return The quaternion representing a rotation around the y-axis。
     */
    public static Quaternion rotateY(final float angle) {
        final float half = angle * 0.5f;
        final float s = (float) Math.sin(half);
        final float c = (float) Math.cos(half);

        return new Quaternion(0.0f, s, 0.0f, c);
    }

    /**
     * Returns a quaternion that rotates around the z-axis by a given number of
     * radians.
     *
     * @param angle The clockwise rotation angle when looking along the z-axis
     *              towards the origin in radians.
     * @return The quaternion representing a rotation around the z-axis.
     */
    public static Quaternion rotateZ(final float angle) {
        final float half = angle * 0.5f;
        final float s = (float) Math.sin(half);
        final float c = (float) Math.cos(half);

        return new Quaternion(0.0f, 0.0f, s, c);
    }

    /**
     * Returns a quaternion view rotation given a unit length forward vector and
     * a unit length up vector.
     *
     * @param forward The view forward direction.
     * @param up      The view up direction.
     * @return The quaternion view rotation.
     */
    public static Quaternion lookRotation(final Vector3 forward, final Vector3 up) {
        final Vector3 t = Vector3.normalize(Vector3.cross(up, forward));

        return fromMatrix(new Matrix3x3(t, Vector3.cross(forward, t), forward));
    }

    /**
     * Returns a unit quaternion from a Matrix3x3 rotation matrix. The matrix
     * must be orthonormal.
     *
     * @param m The Matrix3x3 orthonormal rotation matrix.
     * @return The quaternion representing a rotation.
     */
    public static Quaternion fromMatrix(final Matrix3x3 m) {
        final Vector3 u = m.c0(), v = m.c1(), w = m.c2();

        final int uSign = Float.floatToIntBits(u.x()) & 0x80000000;
        final float t = v.y() + Float.intBitsToFloat(Float.floatToIntBits(w.z()) ^ uSign);
        final int uMask = (uSign >> 31);
        final int tMask = (Float.floatToIntBits(t) >> 31);

        final float tr = 1.0f + Math.abs(u.x());

        int[] signFlips = {0x00000000, 0x80000000, 0x80000000, 0x80000000};

        signFlips[1] ^= (uMask & 0x80000000);
        signFlips[3] ^= (uMask & 0x80000000);

        signFlips[0] ^= (tMask & 0x80000000);
        signFlips[1] ^= (tMask & 0x80000000);
        signFlips[2] ^= (tMask & 0x80000000);

        // +---, +++-, ++-+, +-++
        float[] value = new float[] {
                tr + Float.intBitsToFloat(Float.floatToRawIntBits(t) ^ signFlips[0]),
                u.y() + Float.intBitsToFloat(Float.floatToRawIntBits(v.x()) ^ signFlips[1]),
                w.x() + Float.intBitsToFloat(Float.floatToRawIntBits(u.z()) ^ signFlips[2]),
                v.z() + Float.intBitsToFloat(Float.floatToRawIntBits(w.y()) ^ signFlips[3])
        };

        value = new float[] {
                Float.intBitsToFloat((Float.floatToRawIntBits(value[0]) & ~uMask) | (Float.floatToRawIntBits(value[2]) & uMask)),
                Float.intBitsToFloat((Float.floatToRawIntBits(value[1]) & ~uMask) | (Float.floatToRawIntBits(value[3]) & uMask)),
                Float.intBitsToFloat((Float.floatToRawIntBits(value[2]) & ~uMask) | (Float.floatToRawIntBits(value[0]) & uMask)),
                Float.intBitsToFloat((Float.floatToRawIntBits(value[3]) & ~uMask) | (Float.floatToRawIntBits(value[1]) & uMask))
        };

        value = new float[] {
                Float.intBitsToFloat((Float.floatToRawIntBits(value[3]) & ~tMask) | (Float.floatToRawIntBits(value[0]) & tMask)),
                Float.intBitsToFloat((Float.floatToRawIntBits(value[2]) & ~tMask) | (Float.floatToRawIntBits(value[1]) & tMask)),
                Float.intBitsToFloat((Float.floatToRawIntBits(value[1]) & ~tMask) | (Float.floatToRawIntBits(value[2]) & tMask)),
                Float.intBitsToFloat((Float.floatToRawIntBits(value[0]) & ~tMask) | (Float.floatToRawIntBits(value[3]) & tMask))
        };

        return Quaternion.normalize(new Quaternion(value[0], value[1], value[2], value[3]));
    }

    public static float[] toFloatArray(final Quaternion v) {
        return new float[] { v.x, v.y, v.z, v.w };
    }
}
