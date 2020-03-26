uniform mat4 uMVPMatrix;
attribute vec4 vPosition;
attribute vec2 a_texcoord;
varying vec2 v_texcoord;
varying vec2 v_position;

void main() {
    v_position = 20.0 * vPosition.xy;
    gl_Position = uMVPMatrix * vPosition;
    v_texcoord = a_texcoord;
}
