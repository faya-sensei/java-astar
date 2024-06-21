package org.faya.sensei.mathematics;

public record Matrix4x4(Vector4 c0, Vector4 c1, Vector4 c2, Vector4 c3) {

    public Matrix4x4(final double m00, final double m01, final double m02, final double m03,
                     final double m10, final double m11, final double m12, final double m13,
                     final double m20, final double m21, final double m22, final double m23,
                     final double m30, final double m31, final double m32, final double m33) {
        this(
                new Vector4(m00, m01, m02, m03),
                new Vector4(m10, m11, m12, m13),
                new Vector4(m20, m21, m22, m23),
                new Vector4(m30, m31, m32, m33)
        );
    }

    public static float[] toFloatArray(final Matrix4x4 m) {
        return new float[] {
                (float) m.c0.x(), (float) m.c0.y(), (float) m.c0.z(), (float) m.c0.w(),
                (float) m.c1.x(), (float) m.c1.y(), (float) m.c1.z(), (float) m.c1.w(),
                (float) m.c2.x(), (float) m.c2.y(), (float) m.c2.z(), (float) m.c2.w(),
                (float) m.c3.x(), (float) m.c3.y(), (float) m.c3.z(), (float) m.c3.w()
        };
    }
}
