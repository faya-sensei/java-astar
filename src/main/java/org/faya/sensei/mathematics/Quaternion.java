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

    public static Vector4 toVector4(final Quaternion x) {
        return new Vector4(x.x, x.y, x.z, x.w);
    }

    /**
     * Returns the conjugate of a quaternion value.
     *
     * @param q The quaternion to conjugate.
     * @return The conjugate of the input quaternion.
     */
    public static Quaternion conjugate(final Quaternion q) {
        return new Quaternion(q.x * -1.0f, q.y * -1.0f, q.z * -1.0f, q.w);
    }

    /**
     * Returns the dot product of two quaternions.
     *
     * @param a The first quaternion.
     * @param b The second quaternion.
     * @return The dot product of two quaternions.
     */
    public static float dot(final Quaternion a, final Quaternion b) {
        return a.x * b.x + a.y * b.y + a.z * b.z + a.w * b.w;
    }

    /**
     * Returns the length of a quaternion.
     *
     * @param q The input quaternion.
     * @return The length of the input quaternion.
     */
    public static float length(final Quaternion q) {
        return (float) Math.sqrt(dot(q, q));
    }

    /**
     * Returns a normalized version of a quaternion q by scaling it by 1 / length(q).
     *
     * @param q The quaternion to normalize.
     * @return The normalized quaternion.
     */
    public static Quaternion normalize(final Quaternion q) {
        final Vector4 v = new Vector4(q.x, q.y, q.z, q.w);
        final float length = (float) Math.sqrt(Vector4.dot(v, v));

        return new Quaternion(q.x / length, q.y / length, q.z / length, q.w / length);
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
     * Returns the Euler angle representation of the quaternion. The returned
     * angles depend on the specified order to apply the three rotations around
     * the principal axes. All rotation angles are in radians and clockwise when
     * looking along the rotation axis towards the origin.
     *
     * @param value The quaternion to be converted.
     * @param order The order in which the rotations are applied.
     * @return The Euler angle representation of the quaternion in the specified
     * order.
     */
    public static Vector3 toEuler(final Quaternion value, final RotationOrder order) {
        final float epsilon = 1e-6f;
        final float cutoff = (1.0f - 2.0f * epsilon) * (1.0f - 2.0f * epsilon);

        final Vector4 d1 = Vector4.multiply(Quaternion.toVector4(value), new Vector4(value.w), new Vector4(2.0f)); // xw, yw, zw, ww
        final Vector4 d2 = Vector4.multiply(Quaternion.toVector4(value), new Vector4(value.y, value.z, value.x, value.w), new Vector4(2.0f)); // xy, yz, zx, ww
        final Vector4 d3 = Vector4.multiply(Quaternion.toVector4(value), Quaternion.toVector4(value));

        return switch (order) {
            case XYZ -> {
                float y1 = d2.z() - d1.y();
                if (y1 * y1 < cutoff) {
                    final float x1 = d2.y() + d1.x();
                    final float x2 = d3.z() + d3.w() - d3.y() - d3.x();
                    final float z1 = d2.x() + d1.z();
                    final float z2 = d3.x() + d3.w() - d3.y() - d3.z();
                    yield new Vector3((float) Math.atan2(x1, x2), -(float) Math.asin(y1), (float) Math.atan2(z1, z2));
                } else { // xzx
                    y1 = Math.min(-1.0f, Math.max(y1, 1.0f));
                    final Vector4 abcd = new Vector4(d2.z(), d1.y(), d2.x(), d1.z());
                    final float x1 = 2.0f * (abcd.x() * abcd.w() + abcd.y() * abcd.z()); // 2(ad+bc)
                    final float x2 = Vector4.csum(Vector4.multiply(abcd, abcd, new Vector4(-1.0f, 1.0f, -1.0f, 1.0f)));
                    yield new Vector3((float) Math.atan2(x1, x2), -(float) Math.asin(y1), 0.0f);
                }
            }
            case XZY -> {
                float y1 = d2.x() + d1.z();
                if (y1 * y1 < cutoff) {
                    final float x1 = -d2.y() + d1.x();
                    final float x2 = d3.y() + d3.w() - d3.z() - d3.x();
                    final float z1 = -d2.z() + d1.y();
                    final float z2 = d3.x() + d3.w() - d3.y() - d3.z();
                    yield new Vector3((float) Math.atan2(x1, x2), (float) Math.atan2(z1, z2), (float) Math.asin(y1));
                } else { // xyx
                    y1 = Math.min(-1.0f, Math.max(y1, 1.0f));
                    final Vector4 abcd = new Vector4(d2.x(), d1.z(), d2.z(), d1.y());
                    final float x1 = 2.0f * (abcd.x() * abcd.w() + abcd.y() * abcd.z()); // 2(ad+bc)
                    final float x2 = Vector4.csum(Vector4.multiply(abcd, abcd, new Vector4(-1.0f, 1.0f, -1.0f, 1.0f)));
                    yield new Vector3((float) Math.atan2(x1, x2), 0.0f, (float) Math.asin(y1));
                }
            }
            case YXZ -> {
                float y1 = d2.y() + d1.x();
                if (y1 * y1 < cutoff) {
                    final float x1 = -d2.z() + d1.y();
                    final float x2 = d3.z() + d3.w() - d3.x() - d3.y();
                    final float z1 = -d2.x() + d1.z();
                    final float z2 = d3.y() + d3.w() - d3.z() - d3.x();
                    yield new Vector3((float) Math.asin(y1), (float) Math.atan2(x1, x2), (float) Math.atan2(z1, z2));
                } else { // yzy
                    y1 = Math.min(-1.0f, Math.max(y1, 1.0f));
                    final Vector4 abcd = new Vector4(d2.x(), d1.z(), d2.y(), d1.x());
                    final float x1 = 2.0f * (abcd.x() * abcd.w() + abcd.y() * abcd.z()); // 2(ad+bc)
                    final float x2 = Vector4.csum(Vector4.multiply(abcd, abcd, new Vector4(-1.0f, 1.0f, -1.0f, 1.0f)));
                    yield new Vector3((float) Math.asin(y1), (float) Math.atan2(x1, x2), 0.0f);
                }
            }
            case YZX -> {
                float y1 = d2.x() - d1.z();
                if (y1 * y1 < cutoff) {
                    final float x1 = d2.z() + d1.y();
                    final float x2 = d3.x() + d3.w() - d3.z() - d3.y();
                    final float z1 = d2.y() + d1.x();
                    final float z2 = d3.y() + d3.w() - d3.x() - d3.z();
                    yield new Vector3((float) Math.atan2(z1, z2), (float) Math.atan2(x1, x2), -(float) Math.asin(y1));
                } else { // yxy
                    y1 = Math.min(-1.0f, Math.max(y1, 1.0f));
                    final Vector4 abcd = new Vector4(d2.x(), d1.z(), d2.y(), d1.x());
                    final float x1 = 2.0f * (abcd.x() * abcd.w() + abcd.y() * abcd.z()); // 2(ad+bc)
                    final float x2 = Vector4.csum(Vector4.multiply(abcd, abcd, new Vector4(-1.0f, 1.0f, -1.0f, 1.0f)));
                    yield new Vector3(0.0f, (float) Math.atan2(x1, x2), -(float) Math.asin(y1));
                }
            }
            case ZXY -> {
                float y1 = d2.y() - d1.x();
                if (y1 * y1 < cutoff) {
                    final float x1 = d2.x() + d1.z();
                    final float x2 = d3.y() + d3.w() - d3.x() - d3.z();
                    final float z1 = d2.z() + d1.y();
                    final float z2 = d3.z() + d3.w() - d3.x() - d3.y();
                    yield new Vector3(-(float) Math.asin(y1), (float) Math.atan2(z1, z2), (float) Math.atan2(x1, x2));
                } else { // zxz
                    y1 = Math.min(-1.0f, Math.max(y1, 1.0f));
                    final Vector4 abcd = new Vector4(d2.z(), d1.y(), d2.y(), d1.x());
                    final float x1 = 2.0f * (abcd.x() * abcd.w() + abcd.y() * abcd.z()); // 2(ad+bc)
                    final float x2 = Vector4.csum(Vector4.multiply(abcd, abcd, new Vector4(-1.0f, 1.0f, -1.0f, 1.0f)));
                    yield new Vector3(-(float) Math.asin(y1), 0.0f, (float) Math.atan2(x1, x2));
                }
            }
            case ZYX -> {
                float y1 = d2.z() + d1.y();
                if (y1 * y1 < cutoff) {
                    final float x1 = -d2.x() + d1.z();
                    final float x2 = d3.x() + d3.w() - d3.y() - d3.z();
                    final float z1 = -d2.y() + d1.x();
                    final float z2 = d3.z() + d3.w() - d3.y() - d3.x();
                    yield new Vector3((float) Math.atan2(z1, z2), (float) Math.asin(y1), (float) Math.atan2(x1, x2));
                } else { // zxz
                    y1 = Math.min(-1.0f, Math.max(y1, 1.0f));
                    final Vector4 abcd = new Vector4(d2.z(), d1.y(), d2.y(), d1.x());
                    final float x1 = 2.0f * (abcd.x() * abcd.w() + abcd.y() * abcd.z()); // 2(ad+bc)
                    final float x2 = Vector4.csum(Vector4.multiply(abcd, abcd, new Vector4(-1.0f, 1.0f, -1.0f, 1.0f)));
                    yield new Vector3(0.0f, (float) Math.asin(y1), (float) Math.atan2(x1, x2));
                }
            }
        };
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
     * Returns the angle in radians between two unit quaternions.
     *
     * @param q1 The first quaternion.
     * @param q2 The second quaternion.
     * @return The angle between two unit quaternions.
     */
    public static float angle(final Quaternion q1, final Quaternion q2) {
        final Quaternion normalize = normalize(multiply(conjugate(q1), q2));
        final float diff = (float) Math.asin(Vector3.length(new Vector3(normalize.x, normalize.y, normalize.z)));
        return diff + diff;
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
