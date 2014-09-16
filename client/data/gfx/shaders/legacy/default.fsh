#version 120

uniform sampler2D texture;
uniform vec4 colour;

void main(void) {
  gl_FragColor = texture2D(texture, gl_TexCoord[0].st) * colour;
}