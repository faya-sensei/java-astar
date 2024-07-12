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

    /**
     * Returns the Vector4 column vector result of a matrix multiplication
     * between a Matrix4x4 matrix and a Vector4 column vector.
     *
     * @param lhs Left hand side argument of the matrix multiply.
     * @param rhs Right hand side argument of the matrix multiply.
     * @return The computed matrix multiplication.
     */
    public static Vector4 multiply(final Matrix4x4 lhs, final Vector4 rhs) {
        return Vector4.add(
                Vector4.multiply(lhs.c0, new Vector4(rhs.x())),
                Vector4.multiply(lhs.c1, new Vector4(rhs.y())),
                Vector4.multiply(lhs.c2, new Vector4(rhs.z())),
                Vector4.multiply(lhs.c3, new Vector4(rhs.w()))
        );
    }

    /**
     * Returns the result of a component-wise multiplication operation on two
     * Matrix4x4 matrices.
     *
     * @param lhs Left hand side Matrix4x4 to use to compute component-wise
     *            multiplication.
     * @param rhs Right hand side Matrix4x4 to use to compute component-wise
     *            multiplication.
     * @return Matrix4x4 result of the component-wise multiplication.
     */
    public static Matrix4x4 multiply(final Matrix4x4 lhs, final Matrix4x4 rhs) {
        return new Matrix4x4(
                Vector4.add(
                        Vector4.multiply(lhs.c0, new Vector4(rhs.c0.x())),
                        Vector4.multiply(lhs.c1, new Vector4(rhs.c0.y())),
                        Vector4.multiply(lhs.c2, new Vector4(rhs.c0.z())),
                        Vector4.multiply(lhs.c3, new Vector4(rhs.c0.w()))
                ),
                Vector4.add(
                        Vector4.multiply(lhs.c0, new Vector4(rhs.c1.x())),
                        Vector4.multiply(lhs.c1, new Vector4(rhs.c1.y())),
                        Vector4.multiply(lhs.c2, new Vector4(rhs.c1.z())),
                        Vector4.multiply(lhs.c3, new Vector4(rhs.c1.w()))
                ),
                Vector4.add(
                        Vector4.multiply(lhs.c0, new Vector4(rhs.c2.x())),
                        Vector4.multiply(lhs.c1, new Vector4(rhs.c2.y())),
                        Vector4.multiply(lhs.c2, new Vector4(rhs.c2.z())),
                        Vector4.multiply(lhs.c3, new Vector4(rhs.c2.w()))
                ),
                Vector4.add(
                        Vector4.multiply(lhs.c0, new Vector4(rhs.c3.x())),
                        Vector4.multiply(lhs.c1, new Vector4(rhs.c3.y())),
                        Vector4.multiply(lhs.c2, new Vector4(rhs.c3.z())),
                        Vector4.multiply(lhs.c3, new Vector4(rhs.c3.w()))
                )
        );
    }

    /**
     * Returns a Matrix4x4 perspective projection matrix based on field of view.
     *
     * @param verticalFov Vertical Field of view in radians.
     * @param aspect      X:Y aspect ratio.
     * @param near        Distance to near plane. Must be greater than zero.
     * @param far         Distance to far plane. Must be greater than zero.
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
     * Returns the Matrix4x4 full inverse of a Matrix4x4 matrix.
     *
     * @param matrix Matrix to invert.
     * @return The inverted matrix.
     */
    public static Matrix4x4 inverse(Matrix4x4 matrix) {
        float a = matrix.c0.x(), b = matrix.c1.x(), c = matrix.c2.x(), d = matrix.c3.x();
        float e = matrix.c0.y(), f = matrix.c1.y(), g = matrix.c2.y(), h = matrix.c3.y();
        float i = matrix.c0.z(), j = matrix.c1.z(), k = matrix.c2.z(), l = matrix.c3.z();
        float m = matrix.c0.w(), n = matrix.c1.w(), o = matrix.c2.w(), p = matrix.c3.w();

        float kp_lo = k * p - l * o;
        float jp_ln = j * p - l * n;
        float jo_kn = j * o - k * n;
        float ip_lm = i * p - l * m;
        float io_km = i * o - k * m;
        float in_jm = i * n - j * m;

        float a11 = +(f * kp_lo - g * jp_ln + h * jo_kn);
        float a12 = -(e * kp_lo - g * ip_lm + h * io_km);
        float a13 = +(e * jp_ln - f * ip_lm + h * in_jm);
        float a14 = -(e * jo_kn - f * io_km + g * in_jm);

        float det = a * a11 + b * a12 + c * a13 + d * a14;

        if (Math.abs(det) < Float.MIN_VALUE) {
            return new Matrix4x4(
                    new Vector4(Float.NaN, Float.NaN, Float.NaN, Float.NaN),
                    new Vector4(Float.NaN, Float.NaN, Float.NaN, Float.NaN),
                    new Vector4(Float.NaN, Float.NaN, Float.NaN, Float.NaN),
                    new Vector4(Float.NaN, Float.NaN, Float.NaN, Float.NaN)
            );
        }

        float invDet = 1.0f / det;

        float gp_ho = g * p - h * o;
        float fp_hn = f * p - h * n;
        float fo_gn = f * o - g * n;
        float ep_hm = e * p - h * m;
        float eo_gm = e * o - g * m;
        float en_fm = e * n - f * m;

        float gl_hk = g * l - h * k;
        float fl_hj = f * l - h * j;
        float fk_gj = f * k - g * j;
        float el_hi = e * l - h * i;
        float ek_gi = e * k - g * i;
        float ej_fi = e * j - f * i;

        return new Matrix4x4(
                new Vector4(
                        a11 * invDet,
                        -(b * kp_lo - c * jp_ln + d * jo_kn) * invDet,
                        +(b * gp_ho - c * fp_hn + d * fo_gn) * invDet,
                        -(b * gl_hk - c * fl_hj + d * fk_gj) * invDet
                ),
                new Vector4(
                        a12 * invDet,
                        +(a * kp_lo - c * ip_lm + d * io_km) * invDet,
                        -(a * gp_ho - c * ep_hm + d * eo_gm) * invDet,
                        +(a * gl_hk - c * el_hi + d * ek_gi) * invDet
                ),
                new Vector4(
                        a13 * invDet,
                        -(a * jp_ln - b * ip_lm + d * in_jm) * invDet,
                        +(a * fp_hn - b * ep_hm + d * en_fm) * invDet,
                        -(a * fl_hj - b * el_hi + d * ej_fi) * invDet
                ),
                new Vector4(
                        a14 * invDet,
                        +(a * jo_kn - b * io_km + c * in_jm) * invDet,
                        -(a * fo_gn - b * eo_gm + c * en_fm) * invDet,
                        +(a * fk_gj - b * ek_gi + c * ej_fi) * invDet
                )
        );
    }

    /**
     * Return the Matrix4x4 transpose of a Matrix4x4 matrix.
     *
     * @param m Value to transpose.
     * @return Transposed value.
     */
    public static Matrix4x4 transpose(Matrix4x4 m) {
        return new Matrix4x4(
                m.c0().x(), m.c0().y(), m.c0().z(), m.c0().w(),
                m.c1().x(), m.c1().y(), m.c1().z(), m.c1().w(),
                m.c2().x(), m.c2().y(), m.c2().z(), m.c2().w(),
                m.c3().x(), m.c3().y(), m.c3().z(), m.c3().w()
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

    /**
     * Return a buffer of a Matrix4x4 matrix.
     *
     * @param m The Matrix4x4 matrix.
     * @return The float buffer.
     */
    public static float[] toFloatArray(final Matrix4x4 m) {
        return new float[] {
                m.c0.x(), m.c0.y(), m.c0.z(), m.c0.w(),
                m.c1.x(), m.c1.y(), m.c1.z(), m.c1.w(),
                m.c2.x(), m.c2.y(), m.c2.z(), m.c2.w(),
                m.c3.x(), m.c3.y(), m.c3.z(), m.c3.w()
        };
    }
}
