package org.faya.sensei.visualization.components;

import org.faya.sensei.mathematics.Matrix4x4;
import org.faya.sensei.visualization.Shader;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL30.*;

public class MeshRenderer extends Component {

    private final Shader shader;
    private MeshFilter mesh;
    private int vaoId;
    private List<Integer> vboIdList;

    public MeshRenderer(Shader shader) {
        this.shader = shader;
    }

    @Override
    public void start() {
        shader.init();

        shader.registerUniform("projectionMatrix");
        shader.registerUniform("modelMatrix");
        shader.registerUniform("viewMatrix");

        mesh = (MeshFilter) entity.getComponent(MeshFilter.class);

        final float[] vertices = mesh.getVertices();
        final float[] uvs = mesh.getUvs();
        final int[] indices = mesh.getIndices();

        try (final MemoryStack stack = MemoryStack.stackPush()) {
            vboIdList = new ArrayList<>();

            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            // Positions VBO
            {
                final int vboId = glGenBuffers();
                vboIdList.add(vboId);
                final FloatBuffer positionsBuffer = stack.callocFloat(vertices.length);
                positionsBuffer.put(0, vertices);
                glBindBuffer(GL_ARRAY_BUFFER, vboId);
                glBufferData(GL_ARRAY_BUFFER, positionsBuffer, GL_STATIC_DRAW);
                glEnableVertexAttribArray(0);
                glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
            }

            // uv VBO
            {
                final int vboId = glGenBuffers();
                vboIdList.add(vboId);
                final FloatBuffer textCoordsBuffer = stack.callocFloat(uvs.length);
                textCoordsBuffer.put(0, uvs);
                glBindBuffer(GL_ARRAY_BUFFER, vboId);
                glBufferData(GL_ARRAY_BUFFER, textCoordsBuffer, GL_STATIC_DRAW);
                glEnableVertexAttribArray(1);
                glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
            }

            // Index VBO
            {
                final int vboId = glGenBuffers();
                vboIdList.add(vboId);
                final IntBuffer indicesBuffer = stack.callocInt(indices.length);
                indicesBuffer.put(0, indices);
                glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
                glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
            }

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        }
    }

    public void render() {
        shader.useProgram();

        final Transform transform = entity.getTransform();

        shader.setUniform("modelMatrix", Matrix4x4.trs(
                transform.getWorldPosition(),
                transform.getWorldRotation(),
                transform.getWorldScale()
        ));

        glBindVertexArray(vaoId);
        glDrawElements(GL_TRIANGLES, mesh.getIndices().length, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);

        shader.stopProgram();
    }

    @Override
    public void dispose() {
        vboIdList.forEach(GL30::glDeleteBuffers);
        glDeleteVertexArrays(vaoId);
    }
}
