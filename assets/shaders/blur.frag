#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_texture;

varying vec2 v_texCoord;

void main() {
    vec4 sum = vec4(0.0);
    float blurSize = 1.0 / 512.0;

    // Apply blur effect by sampling nearby pixels
    sum += texture2D(u_texture, v_texCoord + vec2(-4.0 * blurSize, 0.0)) * 0.05;
    sum += texture2D(u_texture, v_texCoord + vec2(-3.0 * blurSize, 0.0)) * 0.09;
    sum += texture2D(u_texture, v_texCoord + vec2(-2.0 * blurSize, 0.0)) * 0.12;
    sum += texture2D(u_texture, v_texCoord + vec2(-blurSize, 0.0)) * 0.15;
    sum += texture2D(u_texture, v_texCoord) * 0.16;
    sum += texture2D(u_texture, v_texCoord + vec2(blurSize, 0.0)) * 0.15;
    sum += texture2D(u_texture, v_texCoord + vec2(2.0 * blurSize, 0.0)) * 0.12;
    sum += texture2D(u_texture, v_texCoord + vec2(3.0 * blurSize, 0.0)) * 0.09;
    sum += texture2D(u_texture, v_texCoord + vec2(4.0 * blurSize, 0.0)) * 0.05;

    gl_FragColor = sum;
}
