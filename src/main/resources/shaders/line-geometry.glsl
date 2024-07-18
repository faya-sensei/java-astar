#version 330 core

layout(lines) in;
layout(triangle_strip, max_vertices = 4) out;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform float lineWidth;

void main() {
    vec3 p0 = gl_in[0].gl_Position.xyz;
    vec3 p1 = gl_in[1].gl_Position.xyz;

    vec3 cameraRight = vec3(viewMatrix[0][0], viewMatrix[1][0], viewMatrix[2][0]);
    vec3 lineDir = normalize(p1 - p0);
    vec3 offset = normalize(cross(lineDir, cameraRight)) * lineWidth * 0.5;

    // Generate the four corners of the thick line (as a quad)
    gl_Position = projectionMatrix * viewMatrix * vec4(p0 + offset, 1.0);
    EmitVertex();

    gl_Position = projectionMatrix * viewMatrix * vec4(p0 - offset, 1.0);
    EmitVertex();

    gl_Position = projectionMatrix * viewMatrix * vec4(p1 + offset, 1.0);
    EmitVertex();

    gl_Position = projectionMatrix * viewMatrix * vec4(p1 - offset, 1.0);
    EmitVertex();

    EndPrimitive();
}
