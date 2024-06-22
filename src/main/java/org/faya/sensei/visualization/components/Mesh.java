package org.faya.sensei.visualization.components;

import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL30.*;

public class Mesh extends Component {

    private final int numVertices;
    private final int vaoId;
    private final List<Integer> vboIdList;

    public Mesh(float[] positions, float[] uvs, int[] indices) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            numVertices = indices.length;
            vboIdList = new ArrayList<>();

            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            // Positions VBO
            {
                final int vboId = glGenBuffers();
                vboIdList.add(vboId);
                final FloatBuffer positionsBuffer = stack.callocFloat(positions.length);
                positionsBuffer.put(0, positions);
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

    public int getNumVertices() {
        return numVertices;
    }

    public final int getVaoId() {
        return vaoId;
    }

    @Override
    public void dispose() {
        vboIdList.forEach(GL30::glDeleteBuffers);
        glDeleteVertexArrays(vaoId);
    }
}
