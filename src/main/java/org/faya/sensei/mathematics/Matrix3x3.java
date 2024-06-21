package org.faya.sensei.mathematics;

public record Matrix3x3(Vector3 c0, Vector3 c1, Vector3 c2) {

    public Matrix3x3(final double m00, final double m01, final double m02,
                     final double m10, final double m11, final double m12,
                     final double m20, final double m21, final double m22) {
        this(
                new Vector3(m00, m01, m02),
                new Vector3(m10, m11, m12),
                new Vector3(m20, m21, m22)
        );
    }

    public static Matrix3x3 transpose(final Matrix3x3 m) {
        return new Matrix3x3(
                m.c0.x(), m.c0.y(), m.c0.z(),
                m.c1.x(), m.c1.y(), m.c1.z(),
                m.c2.x(), m.c2.y(), m.c2.z()
        );
    }

    public static double determinant(final Matrix3x3 m) {
        final Vector3 c0 = m.c0;
        final Vector3 c1 = m.c1;
        final Vector3 c2 = m.c2;

        final double m00 = c1.y() * c2.z() - c1.z() * c2.y();
        final double m01 = c0.y() * c2.z() - c0.z() * c2.y();
        final double m02 = c0.y() * c1.z() - c0.z() * c1.y();

        return c0.x() * m00 - c1.x() * m01 + c2.x() * m02;
    }

    public static float[] toFloatArray(final Matrix3x3 m) {
        return new float[] {
                (float) m.c0.x(), (float) m.c0.y(), (float) m.c0.z(),
                (float) m.c1.x(), (float) m.c1.y(), (float) m.c1.z(),
                (float) m.c2.x(), (float) m.c2.y(), (float) m.c2.z()
        };
    }
}
