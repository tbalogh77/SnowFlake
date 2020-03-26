uniform mat4 uMVPMatrix;
attribute vec4 vPosition;

attribute vec2 atexcoord;
varying vec2 vtexcoord;

void main() {
    gl_Position = uMVPMatrix * vPosition;
    vtexcoord = atexcoord;
}
