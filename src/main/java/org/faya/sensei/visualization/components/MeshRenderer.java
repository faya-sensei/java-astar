package org.faya.sensei.visualization.components;

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
    private int vaoId;
    private List<Integer> vboIdList;

    public MeshRenderer(Shader shader) {
        this.shader = shader;
    }

    @Override
    public void start() {
        this.shader.setup();

        final MeshFilter meshFilter = (MeshFilter) entity.getComponent(MeshFilter.class);

        final float[] vertices = meshFilter.getVertices();
        final float[] uvs = meshFilter.getUvs();
        final int[] indices = meshFilter.getIndices();

        try (final MemoryStack stack = MemoryStack.stackPush()) {
            this.vboIdList = new ArrayList<>();

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

    @Override
    public void dispose() {
        vboIdList.forEach(GL30::glDeleteBuffers);
        glDeleteVertexArrays(vaoId);
    }
}
