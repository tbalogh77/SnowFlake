precision mediump float;
uniform vec4 vColor;
uniform vec4 vMinMax;
uniform sampler2D tex_sampler;
varying vec2 v_texcoord;
varying vec2 v_position;

void main() {

    float pihalf = 1.57079632679;
    vec2 tc;


/*
    float fl1 = dot(v_position, v_position);
    float fl2 = sqrt(1.0 - fl1);
    float fl3 = step(fl1, 1.0);
*/
/*
    tc.x = v_texcoord.x * v_texcoord.x * vMinMax.x + vMinMax.z;
    tc.y = v_texcoord.y * v_texcoord.y * vMinMax.y + vMinMax.w;
*/
/*
    tc.x = v_texcoord.x * v_texcoord.x * v_texcoord.x * vMinMax.x + vMinMax.z;
    tc.y = v_texcoord.y * v_texcoord.y * v_texcoord.y * vMinMax.y + vMinMax.w;
*/
/*
    tc.x = sin(v_texcoord.x*pihalf) * vMinMax.x + vMinMax.z;
    tc.y = sin(v_texcoord.y*pihalf) * vMinMax.y + vMinMax.w;
*/
/*
    tc.x = v_texcoord.x;
    tc.y = v_texcoord.y;
*/
/*
    vec2 sp = vec2(2.0*(v_texcoord.x-0.5),2.0*(v_texcoord.y-0.5));
    vec2 mp = vec2(sin(sp.x*pihalf)/2.0+0.5, sin(sp.y*pihalf)/2.0+0.5);
    tc.x = mp.x * vMinMax.x + vMinMax.z;
    tc.y = v_texcoord.y * vMinMax.y + vMinMax.w;
*/

/*    float intens = sin(v_texcoord.x*pihalf*2.0)*sin(v_texcoord.y*pihalf*2.0); */


    vec2 lin = vec2( 1.0-abs(2.0*(v_texcoord.x-0.5)), 1.0-abs(2.0*(v_texcoord.y-0.5)));

    vec2 bas = vec2( 2.0*(v_texcoord.x-0.5), 2.0*(v_texcoord.y-0.5));
    vec2 sqv = vec2( bas.x*bas.x, bas.y*bas.y);
    vec2 bal = vec2( 1.0-sqv.x, 1.0-sqv.y);

/**/
    float aura = 0.016;
    tc.x = v_texcoord.x * vMinMax.x + vMinMax.z - bas.x * aura;
    tc.y = v_texcoord.y * vMinMax.y + vMinMax.w - bas.y * aura;
/**/
/*
    tc.x = v_texcoord.x * vMinMax.x + vMinMax.z;
    tc.y = v_texcoord.y * vMinMax.y + vMinMax.w;
*/
/*    float intens = sin(bal.x*pihalf) ;*/
/*    float intens = sin(v_texcoord.x*v_texcoord.y*pihalf*2.0) ;*/
    float intens = sin( sqrt(1.0-sqv.x-sqv.y) * pihalf );


/**/
    gl_FragColor = texture2D(tex_sampler,tc);
    gl_FragColor.x = gl_FragColor.x * vColor.x*intens;
    gl_FragColor.y = gl_FragColor.y * vColor.y*intens;
    gl_FragColor.z = gl_FragColor.z * vColor.z*intens;
    gl_FragColor.w = step(0.0,1.0-sqv.x-sqv.y);
/**/

/*
    gl_FragColor.x = intens;
    gl_FragColor.y = intens;
    gl_FragColor.z = intens;
    gl_FragColor.w = 1.0;
*/
/*
    gl_FragColor = texture2D(tex_sampler,tc);
    gl_FragColor.x = gl_FragColor.x * vColor.x;
    gl_FragColor.y = gl_FragColor.y * vColor.y;
    gl_FragColor.z = gl_FragColor.z * vColor.z;
    gl_FragColor.w = gl_FragColor.w * vColor.w;
*/
}
