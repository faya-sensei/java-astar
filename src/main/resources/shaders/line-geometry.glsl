#version 330

layout(lines) in;
layout(triangle_strip, max_vertices = 4) out;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform float lineWidth;

void main() {
    vec3 p0 = gl_in[0].gl_Position.xyz;
    vec3 p1 = gl_in[1].gl_Position.xyz;

    // Compute the direction of the line
    vec3 dir = normalize(p1 - p0);

    // Calculate the orthogonal vector to the line direction
    vec3 ortho = normalize(cross(dir, vec3(0.0, 0.0, 1.0))) * lineWidth;

    // Generate the four corners of the thick line (as a quad)
    gl_Position = projectionMatrix * viewMatrix * vec4(p0 + ortho, 1.0);
    EmitVertex();

    gl_Position = projectionMatrix * viewMatrix * vec4(p0 - ortho, 1.0);
    EmitVertex();

    gl_Position = projectionMatrix * viewMatrix * vec4(p1 + ortho, 1.0);
    EmitVertex();

    gl_Position = projectionMatrix * viewMatrix * vec4(p1 - ortho, 1.0);
    EmitVertex();

    EndPrimitive();
}
