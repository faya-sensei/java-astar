package org.faya.sensei.mathematics;

public record Matrix4x4(Vector4 c0, Vector4 c1, Vector4 c2, Vector4 c3) {

    public Matrix4x4(final float m00, final float m01, final float m02, final float m03,
                     final float m10, final float m11, final float m12, final float m13,
                     final float m20, final float m21, final float m22, final float m23,
                     final float m30, final float m31, final float m32, final float m33) {
        this(
                new Vector4(m00, m10, m20, m30),
                new Vector4(m01, m11, m21, m31),
                new Vector4(m02, m12, m22, m32),
                new Vector4(m03, m13, m23, m33)
        );
    }

    public Matrix4x4(final Quaternion rotation, final Vector3 translation) {
        this(Matrix3x3.fromQuaternion(rotation), translation);
    }

    public Matrix4x4(final Matrix3x3 rotation, final Vector3 translation) {
        this(
                new Vector4(rotation.c0(), 0.0f),
                new Vector4(rotation.c1(), 0.0f),
                new Vector4(rotation.c2(), 0.0f),
                new Vector4(translation, 1.0f)
        );
    }

    public static Vector4 multiply(final Matrix4x4 lhs, final Vector4 rhs) {
        return Vector4.add(
                Vector4.multiply(lhs.c0, rhs.x()),
                Vector4.multiply(lhs.c1, rhs.y()),
                Vector4.multiply(lhs.c2, rhs.z()),
                Vector4.multiply(lhs.c3, rhs.w())
        );
    }

    public static Matrix4x4 multiply(final Matrix4x4 lhs, final Matrix4x4 rhs) {
        return new Matrix4x4(
                Vector4.add(
                        Vector4.multiply(lhs.c0, rhs.c0.x()),
                        Vector4.multiply(lhs.c1, rhs.c0.y()),
                        Vector4.multiply(lhs.c2, rhs.c0.z()),
                        Vector4.multiply(lhs.c3, rhs.c0.w())
                ),
                Vector4.add(
                        Vector4.multiply(lhs.c0, rhs.c1.x()),
                        Vector4.multiply(lhs.c1, rhs.c1.y()),
                        Vector4.multiply(lhs.c2, rhs.c1.z()),
                        Vector4.multiply(lhs.c3, rhs.c1.w())
                ),
                Vector4.add(
                        Vector4.multiply(lhs.c0, rhs.c2.x()),
                        Vector4.multiply(lhs.c1, rhs.c2.y()),
                        Vector4.multiply(lhs.c2, rhs.c2.z()),
                        Vector4.multiply(lhs.c3, rhs.c2.w())
                ),
                Vector4.add(
                        Vector4.multiply(lhs.c0, rhs.c3.x()),
                        Vector4.multiply(lhs.c1, rhs.c3.y()),
                        Vector4.multiply(lhs.c2, rhs.c3.z()),
                        Vector4.multiply(lhs.c3, rhs.c3.w())
                )
        );
    }

    /**
     * Returns a Matrix4x4 perspective projection matrix based on field of view.
     *
     * @param verticalFov Vertical Field of view in radians.
     * @param aspect X:Y aspect ratio.
     * @param near Distance to near plane. Must be greater than zero.
     * @param far Distance to far plane. Must be greater than zero.
     * @return The Matrix4x4 perspective projection matrix.
     */
    public static Matrix4x4 perspective(final float verticalFov, final float aspect, final float near, final float far) {
        final float cotangent = 1.0f / (float) Math.tan(verticalFov * 0.5f);
        final float rcpdz = 1.0f / (near - far);

        return new Matrix4x4(
                cotangent / aspect,  0.0f,        0.0f,                  0.0f,
                0.0f,                cotangent,   0.0f,                  0.0f,
                0.0f,                0.0f,        (far + near) * rcpdz,  2.0f * near * far * rcpdz,
                0.0f,                0.0f,       -1.0f,                  0.0f
        );
    }

    /**
     * Returns a Matrix4x4 translation matrix given a Vector3 translation vector.
     *
     * @param v The translation vector.
     * @return The Matrix4x4 translation matrix.
     */
    public static Matrix4x4 translate(final Vector3 v) {
        return new Matrix4x4(
                new Vector4(1.0f, 0.0f, 0.0f, 0.0f),
                new Vector4(0.0f, 1.0f, 0.0f, 0.0f),
                new Vector4(0.0f, 0.0f, 1.0f, 0.0f),
                new Vector4(v.x(), v.y(), v.z(), 1.0f)
        );
    }

    /**
     * Returns a Matrix4x4 view matrix given an eye position, a target point and
     * a unit length up vector.
     *
     * @param eye    The eye position.
     * @param target The view target position.
     * @param up     The eye up direction.
     * @return The float4x4 view matrix.
     */
    public static Matrix4x4 lookAt(final Vector3 eye, final Vector3 target, final Vector3 up) {
        final Matrix3x3 rot = Matrix3x3.lookRotation(Vector3.normalize(Vector3.subtract(target, eye)), up);

        return new Matrix4x4(
                new Vector4(rot.c0(), 0.0f),
                new Vector4(rot.c1(), 0.0f),
                new Vector4(rot.c2(), 0.0f),
                new Vector4(eye, 1.0f)
        );
    }

    /**
     * Returns a float4x4 matrix representing a combined scale-, rotation- and
     * translation transform.
     *
     * @param translation The translation vector.
     * @param rotation    The quaternion rotation.
     * @param scale       The scaling factors of each axis.
     * @return The matrix representing the translation, rotation, and scale by
     * the inputs.
     */
    public static Matrix4x4 trs(final Vector3 translation, final Quaternion rotation, final Vector3 scale) {
        final Matrix3x3 r = Matrix3x3.fromQuaternion(rotation);

        return new Matrix4x4(
                new Vector4(Vector3.multiply(r.c0(), new Vector3(scale.x())), 0.0f),
                new Vector4(Vector3.multiply(r.c1(), new Vector3(scale.y())), 0.0f),
                new Vector4(Vector3.multiply(r.c2(), new Vector3(scale.z())), 0.0f),
                new Vector4(translation, 1.0f)
        );
    }

    public static float[] toFloatArray(final Matrix4x4 m) {
        return new float[] {
                m.c0.x(), m.c0.y(), m.c0.z(), m.c0.w(),
                m.c1.x(), m.c1.y(), m.c1.z(), m.c1.w(),
                m.c2.x(), m.c2.y(), m.c2.z(), m.c2.w(),
                m.c3.x(), m.c3.y(), m.c3.z(), m.c3.w()
        };
    }
}
