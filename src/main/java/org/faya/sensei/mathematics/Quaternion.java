package org.faya.sensei.mathematics;

public record Quaternion(double x, double y, double z, double w) {

    public enum RotationOrder {XYZ, XZY, YXZ, YZX, ZXY, ZYX}

    /**
     * Returns a quaternion constructed by first performing 3 rotations around
     * the principal axes in a given order. All rotation angles are in radians
     * and clockwise when looking along the rotation axis towards the origin.
     *
     * @param xyz   A vector containing the rotation angles around the x-, y- and
     *              z-axis measures in radians.
     * @param order The order in which the rotations are applied.
     * @return The quaternion representing the Euler angle rotation in the
     * specified order.
     */
    public static Quaternion fromEuler(final Vector3 xyz, final RotationOrder order) {
        final Vector3 half = Vector3.multiply(xyz, new Vector3(0.5));
        final Vector3 s = new Vector3(Math.sin(half.x()), Math.sin(half.y()), Math.sin(half.z()));
        final Vector3 c = new Vector3(Math.cos(half.x()), Math.cos(half.y()), Math.cos(half.z()));

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
        final double sqw = value.w * value.w;
        final double sqx = value.x * value.x;
        final double sqy = value.y * value.y;
        final double sqz = value.z * value.z;

        final double unit = sqx + sqy + sqz + sqw;
        final double test = value.x * value.y + value.z * value.w;

        if (test > 0.499 * unit) {
            return new Vector3(Math.atan2(value.x, value.w), Math.PI / 2.0, 0.0);
        }
        if (test < -0.499 * unit) {
            return new Vector3(-2.0 * Math.atan2(value.x, value.w), -Math.PI / 2.0, 0.0);
        }

        return new Vector3(
                Math.atan2(2.0 * value.y * value.w - 2.0 * value.x * value.z, sqx - sqy - sqz + sqw),
                Math.asin(2.0 * test / unit),
                Math.atan2(2.0 * value.x * value.w - 2.0 * value.y * value.z, -sqx + sqy - sqz + sqw)
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
    public static Quaternion rotateX(final double angle) {
        final double half = angle * 0.5;
        final double s = Math.sin(half);
        final double c = Math.cos(half);

        return new Quaternion(s, 0.0, 0.0, c);
    }

    /**
     * Returns a quaternion that rotates around the y-axis by a given number of
     * radians.
     *
     * @param angle The clockwise rotation angle when looking along the y-axis
     *              towards the origin in radians.
     * @return The quaternion representing a rotation around the y-axis。
     */
    public static Quaternion rotateY(final double angle) {
        final double half = angle * 0.5;
        final double s = Math.sin(half);
        final double c = Math.cos(half);

        return new Quaternion(0.0, s, 0.0, c);
    }

    /**
     * Returns a quaternion that rotates around the z-axis by a given number of
     * radians.
     *
     * @param angle The clockwise rotation angle when looking along the z-axis
     *              towards the origin in radians.
     * @return The quaternion representing a rotation around the z-axis.
     */
    public static Quaternion rotateZ(final double angle) {
        final double half = angle * 0.5;
        final double s = Math.sin(half);
        final double c = Math.cos(half);

        return new Quaternion(0.0, 0.0, s, c);
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
        final Vector3 c0 = m.c0(), c1 = m.c1(), c2 = m.c2();
        double qw, qx, qy, qz;

        final double tr = c0.x() + c1.y() + c2.z();

        if (tr > 0.0) {
            final double s = Math.sqrt(tr + 1.0) * 2.0; // s = 4 * qw
            qw = 0.25 * s;
            qx = (c2.y() - c1.z()) / s;
            qy = (c0.z() - c2.x()) / s;
            qz = (c1.x() - c0.y()) / s;
        } else {
            if ((c0.x() > c1.y()) & (c0.x() > c2.z())) {
                final double s = Math.sqrt(1.0 + c0.x() - c1.y() - c2.z()) * 2.0; // s = 4 * qx
                qw = (c2.y() - c1.z()) / s;
                qx = 0.25 * s;
                qy = (c0.y() + c1.x()) / s;
                qz = (c0.z() + c2.x()) / s;
            } else if (c1.y() > c2.z()) {
                final double s = Math.sqrt(1.0 + c1.y() - c0.x() - c2.z()) * 2.0; // s = 4 * qy
                qw = (c0.z() - c2.x()) / s;
                qx = (c0.y() + c1.x()) / s;
                qy = 0.25 * s;
                qz = (c1.z() + c2.y()) / s;
            } else {
                final double s = Math.sqrt(1.0 + c2.z() - c0.x() - c1.y()) * 2.0; // s = 4 * qz
                qw = (c1.x() - c0.y()) / s;
                qx = (c0.z() + c2.x()) / s;
                qy = (c1.z() + c2.y()) / s;
                qz = 0.25 * s;
            }
        }

        return new Quaternion(qx, qy, qz, qw);
    }
}
