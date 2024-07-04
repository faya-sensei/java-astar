package org.faya.sensei.visualization.components;

import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIVector3D;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class MeshFilter extends Component {

    private float[] vertices;
    private float[] uvs;
    private int[] indices;

    public MeshFilter(final AIMesh mesh) {
        vertices = processVertices(mesh);
        uvs = processTextCoords(mesh);
        indices = processIndices(mesh);

        if (uvs.length == 0) {
            final int numElements = (vertices.length / 3) * 2;
            uvs = new float[numElements];
        }
    }

    public MeshFilter(final float[] vertices, final float[] uvs, final int[] indices) {
        this.vertices = vertices;
        this.uvs = uvs;
        this.indices = indices;
    }

    // Getter and setter

    public float[] getVertices() {
        return vertices;
    }

    public void setVertices(final float[] vertices) {
        this.vertices = vertices;
    }

    public float[] getUvs() {
        return uvs;
    }

    public void setUvs(final float[] uvs) {
        this.uvs = uvs;
    }

    public int[] getIndices() {
        return indices;
    }

    public void setIndices(final int[] indices) {
        this.indices = indices;
    }

    // Function

    /**
     * Extract the uv buffer from the mesh instance.
     *
     * @param aiMesh The mesh instance.
     * @return The uv buffer.
     */
    private static float[] processTextCoords(AIMesh aiMesh) {
        final AIVector3D.Buffer buffer = aiMesh.mTextureCoords(0);
        if (buffer == null) return new float[] { };
        final float[] data = new float[buffer.remaining() * 2];
        int pos = 0;
        while (buffer.remaining() > 0) {
            final AIVector3D textCoord = buffer.get();
            data[pos++] = textCoord.x();
            data[pos++] = 1 - textCoord.y();
        }
        return data;
    }

    /**
     * Extract the vertex buffer from the mesh instance.
     *
     * @param aiMesh The mesh instance.
     * @return The vertex buffer.
     */
    private static float[] processVertices(AIMesh aiMesh) {
        final AIVector3D.Buffer buffer = aiMesh.mVertices();
        final float[] data = new float[buffer.remaining() * 3];
        int pos = 0;
        while (buffer.remaining() > 0) {
            final AIVector3D vertex = buffer.get();
            data[pos++] = vertex.x();
            data[pos++] = vertex.y();
            data[pos++] = vertex.z();
        }
        return data;
    }

    /**
     * Extract the index buffer from the mesh instance.
     *
     * @param aiMesh The mesh instance.
     * @return The index buffer.
     */
    private static int[] processIndices(AIMesh aiMesh) {
        final List<Integer> indices = new ArrayList<>();
        final int numFaces = aiMesh.mNumFaces();
        final AIFace.Buffer aiFaces = aiMesh.mFaces();
        for (int i = 0; i < numFaces; i++) {
            final AIFace aiFace = aiFaces.get(i);
            final IntBuffer buffer = aiFace.mIndices();
            while (buffer.remaining() > 0) {
                indices.add(buffer.get());
            }
        }
        return indices.stream().mapToInt(Integer::intValue).toArray();
    }
}
