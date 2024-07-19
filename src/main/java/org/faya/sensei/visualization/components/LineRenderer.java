package org.faya.sensei.visualization.components;

import org.faya.sensei.mathematics.Vector3;
import org.faya.sensei.mathematics.Vector4;
import org.faya.sensei.visualization.Shader;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL30.*;

public class LineRenderer extends Renderer {

    private final List<Vector3> vertices = new ArrayList<>();
    private int vaoId;
    private int vboId;

    private float lineWidth = 0.05f;
    private Vector4 color = new Vector4(0.75f, 0.75f, 0.75f, 1.0f);

    public LineRenderer(Shader shader) {
        super(shader);
    }

    public void setLineWidth(float width) {
        this.lineWidth = width;
    }

    public void setColor(Vector4 color) {
        this.color = color;
    }

    /** @inheritDoc */
    @Override
    public void start() {
        super.start();

        shader.registerUniform("projectionMatrix");
        shader.registerUniform("viewMatrix");
        shader.registerUniform("lineWidth");
        shader.registerUniform("color");

        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);

        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public void addLine(Vector3 start, Vector3 end) {
        vertices.add(start);
        vertices.add(end);
        updateBuffer();
    }

    /** @inheritDoc */
    public void render() {
        shader.useProgram();

        shader.setUniform("lineWidth", lineWidth);
        shader.setUniform("color", color);

        glBindVertexArray(vaoId);
        glDrawArrays(GL_LINES, 0, vertices.size());
        glBindVertexArray(0);

        shader.stopProgram();
    }

    @Override
    public void dispose() {
        glDeleteBuffers(vboId);
        glDeleteVertexArrays(vaoId);
    }

    private void updateBuffer() {
        glBindBuffer(GL_ARRAY_BUFFER, vboId);

        float[] vertexArray = new float[vertices.size() * 3];
        int index = 0;
        for (Vector3 vertex : vertices) {
            vertexArray[index++] = vertex.x();
            vertexArray[index++] = vertex.y();
            vertexArray[index++] = vertex.z();
        }

        glBufferData(GL_ARRAY_BUFFER, vertexArray, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }
}
