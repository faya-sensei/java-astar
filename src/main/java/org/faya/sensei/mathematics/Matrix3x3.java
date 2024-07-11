package org.faya.sensei.mathematics;

public record Matrix3x3(Vector3 c0, Vector3 c1, Vector3 c2) {

    public Matrix3x3(final float m00, final float m01, final float m02,
                     final float m10, final float m11, final float m12,
                     final float m20, final float m21, final float m22) {
        this(
                new Vector3(m00, m10, m20),
                new Vector3(m01, m11, m21),
                new Vector3(m02, m12, m22)
        );
    }

    /**
     * Return the Matrix3x3 transpose of a Matrix3x3 matrix.
     *
     * @param m Value to transpose.
     * @return Transposed value.
     */
    public static Matrix3x3 transpose(final Matrix3x3 m) {
        return new Matrix3x3(
                m.c0.x(), m.c0.y(), m.c0.z(),
                m.c1.x(), m.c1.y(), m.c1.z(),
                m.c2.x(), m.c2.y(), m.c2.z()
        );
    }

    /**
     * Returns the determinant of a Matrix3x3 matrix.
     *
     * @param m Matrix to use when computing determinant.
     * @return The determinant of the matrix.
     */
    public static float determinant(final Matrix3x3 m) {
        final Vector3 c0 = m.c0;
        final Vector3 c1 = m.c1;
        final Vector3 c2 = m.c2;

        final float m00 = c1.y() * c2.z() - c1.z() * c2.y();
        final float m01 = c0.y() * c2.z() - c0.z() * c2.y();
        final float m02 = c0.y() * c1.z() - c0.z() * c1.y();

        return c0.x() * m00 - c1.x() * m01 + c2.x() * m02;
    }

    /**
     * Returns a Matrix3x3 rotation matrix constructed by first performing 3
     * rotations around the principal axes in a given order. All rotation angles
     * are in radians and clockwise when looking along the rotation axis towards
     * the origin.
     *
     * @param xyz   A vector containing the rotation angles around the x-, y-
     *              and z-axis measures in radians.
     * @param order The order in which the rotations are applied.
     * @return The Matrix3x3 rotation matrix representing the rotation by Euler
     * angles in the given order.
     */
    public static Matrix3x3 fromEuler(final Vector3 xyz, final Quaternion.RotationOrder order) {
        final Vector3 s = new Vector3((float) Math.sin(xyz.x()), (float) Math.sin(xyz.y()), (float) Math.sin(xyz.z()));
        final Vector3 c = new Vector3((float) Math.cos(xyz.x()), (float) Math.cos(xyz.y()), (float) Math.cos(xyz.z()));

        return switch (order) {
            case XYZ -> new Matrix3x3(
                     c.y() * c.z(), c.z() * s.x() * s.y() - c.x() * s.z(),  c.x() * c.z() * s.y() + s.x() * s.z(),
                     c.y() * s.z(), c.x() * c.z() + s.x() * s.y() * s.z(),  c.x() * s.y() * s.z() - c.z() * s.x(),
                    -s.y(),         c.y() * s.x(),                          c.x() * c.y()
            );
            case XZY -> new Matrix3x3(
                     c.y() * c.z(), s.x() * s.y() - c.x() * c.y() * s.z(),  c.x() * s.y() + c.y() * s.x() * s.z(),
                     s.z(),         c.x() * c.z(),                         -c.z() * s.x(),
                    -c.z() * s.y(), c.y() * s.x() + c.x() * s.y() * s.z(),  c.x() * c.y() - s.x() * s.y() * s.z()
            );
            case YXZ -> new Matrix3x3(
                     c.y() * c.z() - s.x() * s.y() * s.z(), -c.x() * s.z(),  c.z() * s.y() + c.y() * s.x() * s.z(),
                     c.z() * s.x() * s.y() + c.y() * s.z(),  c.x() * c.z(),  s.y() * s.z() - c.y() * c.z() * s.x(),
                    -c.x() * s.y(),                          s.x(),          c.x() * c.y()
            );
            case YZX -> new Matrix3x3(
                     c.y() * c.z(),                         -s.z(),          c.z() * s.y(),
                     s.x() * s.y() + c.x() * c.y() * s.z(),  c.x() * c.z(),  c.x() * s.y() * s.z() - c.y() * s.x(),
                     c.y() * s.x() * s.z() - c.x() * s.y(),  c.z() * s.x(),  c.x() * c.y() + s.x() * s.y() * s.z()
            );
            case ZXY -> new Matrix3x3(
                     c.y() * c.z() + s.x() * s.y() * s.z(),  c.z() * s.x() * s.y() - c.y() * s.z(),  c.x() * s.y(),
                     c.x() * s.z(),                          c.x() * c.z(),                         -s.x(),
                     c.y() * s.x() * s.z() - c.z() * s.y(),  c.y() * c.z() * s.x() + s.y() * s.z(),  c.x() * c.y()
            );
            case ZYX -> new Matrix3x3(
                     c.y() * c.z(),                         -c.y() * s.z(),                          s.y(),
                     c.z() * s.x() * s.y() + c.x() * s.z(),  c.x() * c.z() - s.x() * s.y() * s.z(), -c.y() * s.x(),
                     s.x() * s.z() - c.x() * c.z() * s.y(),  c.z() * s.x() + c.x() * s.y() * s.z(),  c.x() * c.y()
            );
        };
    }

    /**
     * Returns a Matrix3x3 view rotation matrix given a unit length forward
     * vector and a unit length up vector.
     *
     * @param forward The forward vector to align the center of view with.
     * @param up      The up vector to point top of view toward.
     * @return The Matrix3x3 view rotation matrix.
     */
    public static Matrix3x3 lookRotation(final Vector3 forward, final Vector3 up) {
        final Vector3 t = Vector3.normalize(Vector3.cross(up, forward));

        return new Matrix3x3(t, Vector3.cross(forward, t), forward);
    }

    /**
     * Returns a Matrix3x3 matrix representing a rotation around a unit axis by
     * an angle in radians. The rotation direction is clockwise when looking
     * along the rotation axis towards the origin.
     *
     * @param axis The rotation axis.
     * @param angle The angle of rotation in radians.
     * @return The Matrix3x3 matrix representing the rotation around an axis.
     */
    public static Matrix3x3 fromAxisAngle(final Vector3 axis, final float angle) {
        final float x = axis.x(), y = axis.y(), z = axis.z();
        final float s = (float) Math.sin(angle);
        final float c = (float) Math.cos(angle);
        final float xx = x * x, yy = y * y, zz = z * z;
        final float xy = x * y, xz = x * z, yz = y * z;

        return new Matrix3x3(
                xx + c * (1.0f - xx), xy - c * xy - s * z,  xz - c * xz + s * y,
                xy - c * xy + s * z,  yy + c * (1.0f - yy), yz - c * yz - s * x,
                xz - c * xz - s * y,  yz - c * yz + s * x,  zz + c * (1.0f - zz)
        );
    }

    /**
     * Returns a Matrix3x3 matrix from a unit quaternion.
     *
     * @param q The quaternion rotation.
     * @return The Matrix3x3 rotation matrix.
     */
    public static Matrix3x3 fromQuaternion(final Quaternion q) {
        final float xx = q.x() * q.x();
        final float yy = q.y() * q.y();
        final float zz = q.z() * q.z();

        final float xy = q.x() * q.y();
        final float wz = q.z() * q.w();
        final float xz = q.z() * q.x();
        final float wy = q.y() * q.w();
        final float yz = q.y() * q.z();
        final float wx = q.x() * q.w();

        return new Matrix3x3(
                1.0f - 2.0f * (yy + zz), 2.0f * (xy - wz),        2.0f * (xz + wy),
                2.0f * (xy + wz),        1.0f - 2.0f * (zz + xx), 2.0f * (yz - wx),
                2.0f * (xz - wy),        2.0f * (yz + wx),        1.0f - 2.0f * (yy + xx)
        );
    }

    /**
     * Return a buffer of a Matrix3x3 matrix.
     *
     * @param m The Matrix3x3 matrix.
     * @return The float buffer.
     */
    public static float[] toFloatArray(final Matrix3x3 m) {
        return new float[] {
                m.c0.x(), m.c0.y(), m.c0.z(),
                m.c1.x(), m.c1.y(), m.c1.z(),
                m.c2.x(), m.c2.y(), m.c2.z()
        };
    }
}
