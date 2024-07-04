package org.faya.sensei.visualization;

import org.faya.sensei.mathematics.Matrix4x4;
import org.faya.sensei.mathematics.Vector4;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import static org.lwjgl.opengl.GL20.*;

public class Shader {

    private final ShaderSystem shaderSystem = ShaderSystem.getInstance();
    private final List<ShaderModuleData> shaderModuleDataList = new ArrayList<>();
    private final Map<String, Integer> uniforms = new HashMap<>();

    private int programId;

    public record ShaderModuleData(String shaderFile, int shaderType) { }

    public Shader(final List<ShaderModuleData> shaderModuleDataList) {
        this.shaderModuleDataList.addAll(shaderModuleDataList);
    }

    public void setup() {
        programId = glCreateProgram();
        if (programId == 0) throw new RuntimeException("Could not create Shader.");

        final List<Integer> shaderModules = new ArrayList<>();
        shaderModuleDataList.forEach(s -> {
            try {
                final int shader = createShader(loadShader(s.shaderFile), s.shaderType);
                shaderModules.add(shader);
            } catch (Exception e) {
                throw new RuntimeException("Could not create Shader.", e);
            }
        });

        shaderModules.forEach(s -> glAttachShader(programId, s));

        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0)
            throw new RuntimeException("Error linking Shader code: " + glGetProgramInfoLog(programId));

        shaderModules.forEach(s -> glDetachShader(programId, s));
        shaderModules.forEach(GL20::glDeleteShader);
    }

    public void registerUniform(final String uniformName) {
        final int uniformLocation = glGetUniformLocation(programId, uniformName);
        if (uniformLocation < 0)
            throw new RuntimeException("Could not find uniform: " + uniformName + " in shader program:" + programId);
        if (!shaderSystem.registerUniformLocation(programId, uniformName, uniformLocation)) {
            uniforms.put(uniformName, uniformLocation);
        }
    }

    public void setUniform(final String uniformName, final int value) {
        glUniform1i(getUniformLocation(uniformName), value);
    }

    public void setUniform(final String uniformName, final float value) {
        glUniform1f(getUniformLocation(uniformName), value);
    }

    public void setUniform(final String uniformName, final Vector4 vector) {
        try (final MemoryStack stack = MemoryStack.stackPush()) {
            glUniform4fv(getUniformLocation(uniformName), stack.floats(Vector4.toFloatArray(vector)));
        }
    }

    public void setUniform(final String uniformName, final Matrix4x4 matrix) {
        try (final MemoryStack stack = MemoryStack.stackPush()) {
            glUniformMatrix4fv(getUniformLocation(uniformName), false, stack.floats(Matrix4x4.toFloatArray(matrix)));
        }
    }

    public void useProgram() {
        glUseProgram(programId);
    }

    public void stopProgram() {
        glUseProgram(0);
    }

    public void dispose() {
        stopProgram();
        glDeleteProgram(programId);
    }

    private String loadShader(final String shaderFile) throws IOException {
        final StringBuilder result = new StringBuilder();

        try (final InputStream inputStream = getClass().getClassLoader().getResourceAsStream(shaderFile);
             final BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)))) {
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
