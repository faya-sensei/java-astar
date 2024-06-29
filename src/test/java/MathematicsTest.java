import org.faya.sensei.mathematics.*;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MathematicsTest {

    @Nested
    class Vector3Test {

        @Test
        public void testDivide() {
            assertAll(
                    () -> {
                        final Vector3 a = new Vector3(-353.131439f, -102.799866f, 51.3191528f);
                        final Vector3 b = new Vector3(-178.739563f, -302.096283f, -199.405823f);
                        final Vector3 r = new Vector3(1.97567582f, 0.34028843f, -0.257360339f);

                        assertEquals(r, Vector3.divide(a, b));
                    },
                    () -> {
                        final Vector3 a = new Vector3(-191.871674f, 8.041809f, -128.73764f);
                        final Vector3 b = new Vector3(278.850769f, 502.3376f, -361.484833f);
                        final Vector3 r = new Vector3(-0.688080132f, 0.0160087738f, 0.356135666f);

                        assertEquals(r, Vector3.divide(a, b));
                    },
                    () -> {
                        final Vector3 a = new Vector3(-136.0596f, -370.471f, -237.69455f);
                        final Vector3 b = new Vector3(353.121033f, -38.894928f, -75.76474f);
                        final Vector3 r = new Vector3(-0.385305852f, 9.524919f, 3.1372714f);

                        assertEquals(r, Vector3.divide(a, b));
                    },
                    () -> {
                        final Vector3 a = new Vector3(-432.546875f, 200.2655f, 361.4416f);
                        final Vector3 b = new Vector3(-195.217834f, -405.034f, -394.23f);
                        final Vector3 r = new Vector3(2.215714f, -0.4944412f, -0.9168292f);

                        assertEquals(r, Vector3.divide(a, b));
                    }
            );
        }

        @Test
        public void testDot() {
            assertEquals(67.1f, Vector3.dot(new Vector3(1.2f, 5.5f, 3.4f), new Vector3(6.1f, 9.2f, 2.7f)));
            assertEquals(-67.1f, Vector3.dot(new Vector3(-1.2f, 5.5f, -3.4f), new Vector3(6.1f, -9.2f, 2.7f)));
            assertEquals(6.71e37f, Vector3.dot(new Vector3(1.2e18f, 5.5e18f, 3.4e18f), new Vector3(6.1e18f, 9.2e18f, 2.7e18f)));
            assertEquals(-6.71e37f, Vector3.dot(new Vector3(-1.2e18f, 5.5e18f, 3.4e18f), new Vector3(6.1e18f, -9.2e18f, -2.7e18f)));
        }

        @Test
        public void testSqrt() {
            assertArrayEquals(
                    Vector3.toFloatArray(new Vector3(Float.intBitsToFloat(0xffc00000), 0f, 1E-05f)),
                    Vector3.toFloatArray(Vector3.sqrt(new Vector3(-1.0f, 0.0f, 1e-10f))),
                    0.001f
            );
            assertArrayEquals(
                    Vector3.toFloatArray(new Vector3(11.1108055f, Float.intBitsToFloat(0xffc00000), Float.intBitsToFloat(0xffc00000))),
                    Vector3.toFloatArray(Vector3.sqrt(new Vector3(123.45f, Float.NEGATIVE_INFINITY, Float.intBitsToFloat(0xffc00000)))),
                    0.001f
            );
        }

        @Test
        public void testNormalize() {
            assertArrayEquals(
                    Vector3.toFloatArray(new Vector3(0.464916f, -0.794861f, 0.389932f)),
                    Vector3.toFloatArray(Vector3.normalize(new Vector3(3.1f, -5.3f, 2.6f))),
                    0.0001f
            );
        }
    }

    @Nested
    class QuaternionTest {

        @Test
        public void testFromMatrix() {
            final Matrix3x3 matrix = new Matrix3x3(
                     0.686179155968f, -0.684009078513f, -0.247567660300f,
                     0.555656924414f,  0.273213475262f,  0.785238676636f,
                    -0.469471562786f, -0.676377097075f,  0.567547772692f
            );

            assertArrayEquals(
                    Matrix3x3.toFloatArray(matrix),
                    Matrix3x3.toFloatArray(Matrix3x3.fromQuaternion(Quaternion.fromMatrix(matrix))),
                    0.0001f
            );

            final Matrix3x3 m0 = Matrix3x3.fromAxisAngle(Vector3.normalize(new Vector3(1, 2, 3)), 1.0f);
            final Matrix3x3 m1 = Matrix3x3.fromAxisAngle(Vector3.normalize(new Vector3(3, 2, 1)), 3.0f);
            final Matrix3x3 m2 = Matrix3x3.fromAxisAngle(Vector3.normalize(new Vector3(1, 3, 2)), 3.0f);
            final Matrix3x3 m3 = Matrix3x3.fromAxisAngle(Vector3.normalize(new Vector3(1, 2, 3)), 3.0f);

            final Quaternion q0 = Quaternion.fromMatrix(m0);
            final Quaternion q1 = Quaternion.fromMatrix(m1);
            final Quaternion q2 = Quaternion.fromMatrix(m2);
            final Quaternion q3 = Quaternion.fromMatrix(m3);

            assertArrayEquals(
                    Quaternion.toFloatArray(new Quaternion(0.1281319f, 0.2562638f, 0.3843956f, 0.8775827f)),
                    Quaternion.toFloatArray(q0),
                    0.0001f
            );
            assertArrayEquals(
                    Quaternion.toFloatArray(new Quaternion(0.7997754f, 0.5331835f, 0.2665918f, 0.0707372f)),
                    Quaternion.toFloatArray(q1),
                    0.0001f
            );
            assertArrayEquals(
                    Quaternion.toFloatArray(new Quaternion(0.2665918f, 0.7997754f, 0.5331835f, 0.0707372f)),
                    Quaternion.toFloatArray(q2),
                    0.0001f
            );
            assertArrayEquals(
                    Quaternion.toFloatArray(new Quaternion(0.2665918f, 0.5331835f, 0.7997754f, 0.0707372f)),
                    Quaternion.toFloatArray(q3),
                    0.0001f
            );
        }

        @Test
        public void testLookRotation() {
            assertAll(
                    () -> {
                        final Vector3 forward = Vector3.normalize(new Vector3(1.0f, 2.0f, 3.0f));
                        final Vector3 up = new Vector3(0.0f, 1.0f, 0.0f);
                        final Quaternion q = Quaternion.lookRotation(forward, up);

                        assertArrayEquals(
                                Quaternion.toFloatArray(new Quaternion(-0.274657f, 0.153857f, 0.044571f, 0.948106f)),
                                Quaternion.toFloatArray(q),
                                0.001f
                        );
                        assertArrayEquals(
                                Matrix3x3.toFloatArray(Matrix3x3.lookRotation(forward, up)),
                                Matrix3x3.toFloatArray(Matrix3x3.fromQuaternion(q)),
                                0.001f
                        );
                    },
                    () -> {
                        final Vector3 forward = Vector3.normalize(new Vector3(-3.2f, 2.3f, -1.3f));
                        final Vector3 up = Vector3.normalize(new Vector3(1.0f, -3.2f, -1.5f));
                        final Quaternion q = Quaternion.lookRotation(forward, up);

                        assertArrayEquals(
                                Quaternion.toFloatArray(new Quaternion(0.805418f, 0.089103f, -0.435327f, -0.392240f)),
                                Quaternion.toFloatArray(q),
                                0.001f
                        );
                        assertArrayEquals(
                                Matrix3x3.toFloatArray(Matrix3x3.lookRotation(forward, up)),
                                Matrix3x3.toFloatArray(Matrix3x3.fromQuaternion(q)),
                                0.001f
                        );
                    },
                    () -> {
                        final Vector3 forward = Vector3.normalize(new Vector3(-2.6f, -5.2f, -1.1f));
                        final Vector3 up = Vector3.normalize(new Vector3(-4.2f, -1.2f, -4.5f));
                        final Quaternion q = Quaternion.lookRotation(forward, up);

                        assertArrayEquals(
                                Quaternion.toFloatArray(new Quaternion(-0.088343f, 0.764951f, -0.534144f, -0.348907f)),
                                Quaternion.toFloatArray(q),
                                0.001f
                        );
                        assertArrayEquals(
                                Matrix3x3.toFloatArray(Matrix3x3.lookRotation(forward, up)),
                                Matrix3x3.toFloatArray(Matrix3x3.fromQuaternion(q)),
                                0.001f
                        );
                    },
                    () -> {
                        final Vector3 forward = Vector3.normalize(new Vector3(1.3f, 2.1f, 3.4f));
                        final Vector3 up = Vector3.normalize(new Vector3(0.2f, -1.0f, 0.3f));
                        final Quaternion q = Quaternion.lookRotation(forward, up);

                        assertArrayEquals(
                                Quaternion.toFloatArray(new Quaternion(0.184984f, 0.247484f, 0.947425f, -0.083173f)),
                                Quaternion.toFloatArray(q),
                                0.001f
                        );
                        assertArrayEquals(
                                Matrix3x3.toFloatArray(Matrix3x3.lookRotation(forward, up)),
                                Matrix3x3.toFloatArray(Matrix3x3.fromQuaternion(q)),
                                0.001f
                        );
                    }
            );
        }
    }

    @Nested
    class Matrix3x3Test {

        @Test
        public void testLookRotation() {
            assertAll(
                    () -> {
                        final Vector3 forward = Vector3.normalize(new Vector3(1.0f, 2.0f, 3.0f));
                        final Vector3 up = new Vector3(0.0f, 1.0f, 0.0f);
                        final Matrix3x3 m = Matrix3x3.lookRotation(forward, up);

                        assertArrayEquals(
                                Matrix3x3.toFloatArray(new Matrix3x3(
                                         0.948683f, -0.169031f, 0.267261f,
                                         0.000000f,  0.845154f, 0.534523f,
                                        -0.316228f, -0.507093f, 0.801784f
                                )),
                                Matrix3x3.toFloatArray(m),
                                0.001f
                        );
                        assertArrayEquals(
                                Matrix3x3.toFloatArray(Matrix3x3.fromQuaternion(Quaternion.lookRotation(forward, up))),
                                Matrix3x3.toFloatArray(m),
                                0.001f
                        );
                    },
                    () -> {
                        final Vector3 forward = Vector3.normalize(new Vector3(-3.2f, 2.3f, -1.3f));
                        final Vector3 up = Vector3.normalize(new Vector3(1.0f, -3.2f, -1.5f));
                        final Matrix3x3 m = Matrix3x3.lookRotation(forward, up);

                        assertArrayEquals(
                                Matrix3x3.toFloatArray(new Matrix3x3(
                                         0.605102f, -0.197976f, -0.771140f,
                                         0.485036f, -0.676417f,  0.554257f,
                                        -0.631342f, -0.709413f, -0.313276f
                                )),
                                Matrix3x3.toFloatArray(m),
                                0.001f
                        );
                        assertArrayEquals(
                                Matrix3x3.toFloatArray(Matrix3x3.fromQuaternion(Quaternion.lookRotation(forward, up))),
                                Matrix3x3.toFloatArray(m),
                                0.001f
                        );
                    },
                    () -> {
                        final Vector3 forward = Vector3.normalize(new Vector3(-2.6f, -5.2f, -1.1f));
                        final Vector3 up = Vector3.normalize(new Vector3(-4.2f, -1.2f, -4.5f));
                        final Matrix3x3 m = Matrix3x3.lookRotation(forward, up);

                        assertArrayEquals(
                                Matrix3x3.toFloatArray(new Matrix3x3(
                                        -0.740918f, -0.507890f, -0.439418f,
                                         0.237577f,  0.413771f, -0.878835f,
                                         0.628170f, -0.755540f, -0.185907f
                                )),
                                Matrix3x3.toFloatArray(m),
                                0.001f
                        );
                        assertArrayEquals(
                                Matrix3x3.toFloatArray(Matrix3x3.fromQuaternion(Quaternion.lookRotation(forward, up))),
                                Matrix3x3.toFloatArray(m),
                                0.001f
                        );
                    },
                    () -> {
                        final Vector3 forward = Vector3.normalize(new Vector3(1.3f, 2.1f, 3.4f));
                        final Vector3 up = Vector3.normalize(new Vector3(0.2f, -1.0f, 0.3f));
                        final Matrix3x3 m = Matrix3x3.lookRotation(forward, up);

                        assertArrayEquals(
                                Matrix3x3.toFloatArray(new Matrix3x3(
                                        -0.917727f,  0.249162f, 0.309349f,
                                        -0.066040f, -0.863668f, 0.499717f,
                                         0.391685f,  0.438174f, 0.809065f
                                )),
                                Matrix3x3.toFloatArray(m),
                                0.001f
                        );
                        assertArrayEquals(
                                Matrix3x3.toFloatArray(Matrix3x3.fromQuaternion(Quaternion.lookRotation(forward, up))),
                                Matrix3x3.toFloatArray(m),
                                0.001f
                        );
                    }
            );
        }

        @Test
        public void testFromEuler() {
            final Vector3 angles = new Vector3((float) Math.toRadians(-50.0), (float) Math.toRadians(28.0f), (float) Math.toRadians(39.0f));

            final Matrix3x3 xyz = Matrix3x3.fromEuler(angles, Quaternion.RotationOrder.XYZ);
            final Matrix3x3 xzy = Matrix3x3.fromEuler(angles, Quaternion.RotationOrder.XZY);
            final Matrix3x3 yxz = Matrix3x3.fromEuler(angles, Quaternion.RotationOrder.YXZ);
            final Matrix3x3 yzx = Matrix3x3.fromEuler(angles, Quaternion.RotationOrder.YZX);
            final Matrix3x3 zxy = Matrix3x3.fromEuler(angles, Quaternion.RotationOrder.ZXY);
            final Matrix3x3 zyx = Matrix3x3.fromEuler(angles, Quaternion.RotationOrder.ZYX);

            assertArrayEquals(
                    Matrix3x3.toFloatArray(new Matrix3x3(
                             0.686179155968f, -0.684009078513f, -0.247567660300f,
                             0.555656924414f,  0.273213475262f,  0.785238676636f,
                            -0.469471562786f, -0.676377097075f,  0.567547772692f)
                    ),
                    Matrix3x3.toFloatArray(xyz),
                    0.0001f
            );
            assertArrayEquals(
                    Matrix3x3.toFloatArray(new Matrix3x3(
                             0.68617915596800f, -0.629320391050f, 0.364847929038f,
                            -0.00246669562435f,  0.499539794942f, 0.866287428445f,
                            -0.72742840288700f, -0.595328345266f, 0.341221453011f
                    )),
                    Matrix3x3.toFloatArray(yzx),
                    0.0001f
            );
            assertArrayEquals(
                    Matrix3x3.toFloatArray(new Matrix3x3(
                             0.459852836288f, -0.835146653037f, 0.301770503659f,
                             0.404519349890f,  0.499539794942f, 0.766044443119f,
                            -0.790505828266f, -0.230195701935f, 0.567547772692f
                    )),
                    Matrix3x3.toFloatArray(zxy),
                    0.0001f
            );
            assertArrayEquals(
                    Matrix3x3.toFloatArray(new Matrix3x3(
                             0.686179155968f, -0.716805468125f, -0.123887395569f,
                             0.629320391050f,  0.499539794942f,  0.595328345266f,
                            -0.364847929038f, -0.486466765705f,  0.793874092373f
                    )),
                    Matrix3x3.toFloatArray(xzy),
                    0.0001f
            );
            assertArrayEquals(
                    Matrix3x3.toFloatArray(new Matrix3x3(
                             0.912505475649f, -0.404519349890f, -0.0608099701904f,
                             0.276167195792f,  0.499539794942f,  0.8210917568930f,
                            -0.301770503659f, -0.766044443119f,  0.5675477726920f
                    )),
                    Matrix3x3.toFloatArray(yxz),
                    0.0001f
            );
            assertArrayEquals(
                    Matrix3x3.toFloatArray(new Matrix3x3(
                             0.686179155968f, -0.555656924414f, 0.469471562786f,
                             0.125029621267f,  0.725866114623f, 0.676377097075f,
                            -0.716607116711f, -0.405418013897f, 0.567547772692f
                    )),
                    Matrix3x3.toFloatArray(zyx),
                    0.0001f
            );
        }
    }

    @Nested
    class Matrix4x4Test {

        @Test
        public void testLookAt() {
            final Matrix4x4 m = Matrix4x4.lookAt(
                    new Vector3(0.3f, -0.5f, 3.0f),
                    new Vector3(3.2f, -3.1f, 0.2f),
                    Vector3.normalize(new Vector3(0.3f, 1.0f, -3.0f))
            );
            final Matrix4x4 r = new Matrix4x4(
                    -0.77374f, -0.18930f,  0.60456f,  0.30000f,
                    -0.57373f,  0.61404f, -0.54202f, -0.50000f,
                    -0.26862f, -0.76624f, -0.58371f,  3.00000f,
                     0.00000f,  0.00000f,  0.00000f,  1.00000f
            );

            assertArrayEquals(
                    Matrix4x4.toFloatArray(r),
                    Matrix4x4.toFloatArray(m),
                    0.001f
            );
        }

        @Test
        public void testPerspective() {
            final float fovy = 1.6f;
            final float aspect = 1.3333f;
            final float near = 0.1f;
            final float far = 100.0f;

            final float height = (float) Math.tan(fovy * 0.5f) * near;
            final float width = height * 1.3333f;

            final Matrix4x4 m = Matrix4x4.perspective(fovy, aspect, near, far);
            final Matrix4x4 r = new Matrix4x4(
                    0.72843f, 0.00000f,  0.00000f,  0.00000f,
                    0.00000f, 0.97121f,  0.00000f,  0.00000f,
                    0.00000f, 0.00000f, -1.00200f, -0.20020f,
                    0.00000f, 0.00000f, -1.00000f,  0.00000f
            );

            assertArrayEquals(Matrix4x4.toFloatArray(r), Matrix4x4.toFloatArray(m), 0.001f);

            assertAll(
                    () -> {
                        final Vector4 p = Matrix4x4.multiply(m, new Vector4(-width, -height, -near, 1.0f));
                        final Vector4 pp = Vector4.divide(p, new Vector4(p.w()));

                        assertArrayEquals(
                                new float[] { -1.0f, -1.0f, -1.0f },
                                new float[] { pp.x(), pp.y(), pp.z() },
                                0.001f
                        );
                    },
                    () -> {
                        final Vector4 p = Matrix4x4.multiply(m, new Vector4(width / near * far, height / near * far, -far, 1.0f));
                        final Vector4 pp = Vector4.divide(p, new Vector4(p.w()));

                        assertArrayEquals(
                                new float[] { 1.0f, 1.0f, 1.0f },
                                new float[] { pp.x(), pp.y(), pp.z() },
                                0.001f
                        );
                    }
            );
        }

        @Test
        public void testTRS() {
            final Vector3 scale = new Vector3(1.2f, -0.4f, 2.3f);
            final Quaternion rotation = new Quaternion(0.3270836f, 0.8449658f, -0.1090279f, 0.4088545f);
            final Vector3 translation = new Vector3(12.3f, -4.3f, 135.99f);
            final Matrix4x4 r = Matrix4x4.multiply(
                    new Matrix4x4(new Quaternion(0.0f, 0.0f, 0.0f, 1.0f), translation),
                    Matrix4x4.multiply(
                            new Matrix4x4(rotation, new Vector3(0.0f)),
                            new Matrix4x4(
                                    scale.x(), 0.0f,      0.0f,      0.0f,
                                    0.0f,      scale.y(), 0.0f,      0.0f,
                                    0.0f,      0.0f,      scale.z(), 0.0f,
                                    0.0f,      0.0f,      0.0f,      1.0f
                            )
                    )
            );
            final Matrix4x4 m = Matrix4x4.trs(translation, rotation, scale);

            assertArrayEquals(Matrix4x4.toFloatArray(r), Matrix4x4.toFloatArray(m), 0.001f);
        }
    }
}
