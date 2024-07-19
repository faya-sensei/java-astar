#version 330 core

layout(lines) in;
layout(triangle_strip, max_vertices = 4) out;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform float lineWidth;

void main() {
    vec4 p0 = viewMatrix * vec4(gl_in[0].gl_Position.xyz, 1.0);
    vec4 p1 = viewMatrix * vec4(gl_in[1].gl_Position.xyz, 1.0);

    vec3 lineDir = normalize(p1.xyz - p0.xyz);
    vec3 offset = normalize(cross(vec3(0.0, 0.0, 1.0), lineDir)) * lineWidth * 0.5;

    for (int i = 0; i < 2; ++i) {
        vec4 o = vec4(offset * (i == 0 ? 1.0 : -1.0), 0.0);
        gl_Position = projectionMatrix * (p0 + o); EmitVertex();
        gl_Position = projectionMatrix * (p1 + o); EmitVertex();
    }

    EndPrimitive();
}
