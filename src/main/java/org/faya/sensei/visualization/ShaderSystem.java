package org.faya.sensei.visualization;

import org.faya.sensei.mathematics.Matrix4x4;
import org.faya.sensei.mathematics.Vector4;
import org.lwjgl.system.MemoryStack;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

public class ShaderSystem {

    private static ShaderSystem instance;

    private final Map<String, Map<Integer, Integer>> globalUniforms = new HashMap<>();

    public static ShaderSystem getInstance() {
        if (instance == null) instance = new ShaderSystem();
        return instance;
    }

    public boolean registerUniformLocation(final int programId, final String uniformName, final int uniformLocation) {
        if (globalUniforms.containsKey(uniformName)) {
            globalUniforms.get(uniformName).put(programId, uniformLocation);
            return true;
        }
        return false;
    }

    public void registerGlobalUniform(final String uniformName) {
        globalUniforms.putIfAbsent(uniformName, new HashMap<>());
    }

    public void setUniform(final String uniformName, final int value) {
        final Map<Integer, Integer> programLocations = globalUniforms.get(uniformName);
        if (programLocations != null) {
            for (final Map.Entry<Integer, Integer> entry : programLocations.entrySet()) {
                glUseProgram(entry.getKey());
                glUniform1i(entry.getValue(), value);
            }
            glUseProgram(0);
        }
    }

    public void setUniform(final String uniformName, final float value) {
        final Map<Integer, Integer> programLocations = globalUniforms.get(uniformName);
        if (programLocations != null) {
            for (final Map.Entry<Integer, Integer> entry : programLocations.entrySet()) {
                glUseProgram(entry.getKey());
                glUniform1f(entry.getValue(), value);
            }
            glUseProgram(0);
        }
    }

    public void setUniform(final String uniformName, final Vector4 vector) {
        final Map<Integer, Integer> programLocations = globalUniforms.get(uniformName);
        if (programLocations != null) {
            for (final Map.Entry<Integer, Integer> entry : programLocations.entrySet()) {
                glUseProgram(entry.getKey());
                try (final MemoryStack stack = MemoryStack.stackPush()) {
                    glUniform4fv(entry.getValue(), stack.floats(Vector4.toFloatArray(vector)));
                }
            }
            glUseProgram(0);
        }
    }

    public void setUniform(final String uniformName, final Matrix4x4 matrix) {
        final Map<Integer, Integer> programLocations = globalUniforms.get(uniformName);
        if (programLocations != null) {
            for (final Map.Entry<Integer, Integer> entry : programLocations.entrySet()) {
                glUseProgram(entry.getKey());
                try (final MemoryStack stack = MemoryStack.stackPush()) {
                    glUniformMatrix4fv(entry.getValue(), false, stack.floats(Matrix4x4.toFloatArray(matrix)));
                }
            }
            glUseProgram(0);
        }
    }

    public void dispose() {
        globalUniforms.clear();
        instance = null;
    }
}
