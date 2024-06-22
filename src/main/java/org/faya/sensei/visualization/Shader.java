package org.faya.sensei.visualization;

import org.faya.sensei.mathematics.Matrix4x4;
import org.lwjgl.opengl.GL20;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.*;

import static org.lwjgl.opengl.GL20.*;

public class Shader {

    private final Map<String, Integer> uniforms = new HashMap<>();
    private final int programId;

    public record ShaderModuleData(String shaderFile, int shaderType) { }

    public Shader(final List<ShaderModuleData> shaderModuleList) {
        programId = glCreateProgram();
        if (programId == 0) throw new RuntimeException("Could not create Shader.");

        final List<Integer> shaderModules = new ArrayList<>();
        shaderModuleList.forEach(s -> {
            try {
                final int shader = createShader(loadShader(s.shaderFile), s.shaderType);
                shaderModules.add(shader);
            } catch (Exception e) {
                throw new RuntimeException("Could not create Shader.", e);
            }
        });

        if (glGetProgrami(programId, GL_LINK_STATUS) == 0)
            throw new RuntimeException("Error linking Shader code: " + glGetProgramInfoLog(programId));

        shaderModules.forEach(s -> glDetachShader(programId, s));
        shaderModules.forEach(GL20::glDeleteShader);
    }

    public void createUniform(final String uniformName) {
        final int uniformLocation = glGetUniformLocation(programId, uniformName);
        if (uniformLocation < 0)
            throw new RuntimeException("Could not find uniform: " + uniformName + " in shader program:" + programId);
        uniforms.put(uniformName, uniformLocation);
    }

    public void setUniform(final String uniformName, final int value) {
        glUniform1i(getUniformLocation(uniformName), value);
    }

    public void setUniform(final String uniformName, final float value) {
        glUniform1f(getUniformLocation(uniformName), value);
    }

    public void setUniform(final String name, final Matrix4x4 m) {
        glUniformMatrix4fv(uniforms.get(name), false, FloatBuffer.wrap(Matrix4x4.toFloatArray(m)));
    }

    public void bind() {
        glUseProgram(programId);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void dispose() {
        unbind();

        if (programId != 0) {
            glDeleteProgram(programId);
        }
    }

    private String loadShader(final String fileName) throws IOException {
        final StringBuilder result = new StringBuilder();

        try (final InputStream inputStream = getClass().getResourceAsStream(fileName);
             final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }
        }

        return result.toString();
    }

    private int createShader(final String shaderCode, final int shaderType) {
        final int shaderId = glCreateShader(shaderType);
        if (shaderId == 0) throw new RuntimeException("Error creating shader type: " + shaderType);

        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0)
            throw new RuntimeException("Error compiling Shader code: " + glGetShaderInfoLog(shaderId));

        return shaderId;
    }

    private int getUniformLocation(final String uniformName) {
        final Integer location = uniforms.get(uniformName);
        if (location == null) throw new RuntimeException("Could not find uniform: " + uniformName);

        return location;
    }
}
