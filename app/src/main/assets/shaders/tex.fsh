precision mediump float;
uniform vec4 vColor;
uniform sampler2D tex_sampler;
varying vec2 v_texcoord;
varying vec2 v_position;

void main() {
    float fl1 = dot(v_position, v_position);
    float fl2 = sqrt(1.0 - fl1);
    float fl3 = step(fl1, 1.0);
    float fl4 = mix(0.2,fl2,fl3);
    gl_FragColor = vColor * fl4;
    gl_FragColor.w = fl3;
}
