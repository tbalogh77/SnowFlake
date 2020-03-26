precision mediump float;
uniform vec4 vColor;
uniform sampler2D tex_sampler;
varying vec2 v_texcoord;
varying vec2 v_position;

void main() {
    gl_FragColor = texture2D(tex_sampler,v_texcoord);
    gl_FragColor.x = gl_FragColor.x * vColor.x;
    gl_FragColor.y = gl_FragColor.y * vColor.y;
    gl_FragColor.z = gl_FragColor.z * vColor.z;
    gl_FragColor.w = gl_FragColor.w * vColor.w;
}
