#define HIGHP

// ====== 颜色配置 ======
#define WATER_COLOR vec3(0.1, 0.7, 0.4)  // 翡翠绿主色
#define SPECULAR_COLOR vec3(1.0, 0.9, 0.5) // 金色高光
#define FOAM_COLOR vec3(0.95, 0.9, 0.6)   // 淡黄泡沫
// =====================

#define REFRACT_STRENGTH 0.05
#define WAVE_SPEED 0.6
#define DETAIL_SCALE 30.0

uniform sampler2D u_texture;
uniform sampler2D u_noise;
uniform sampler2D u_water;

uniform vec2 u_campos;
uniform vec2 u_resolution;
uniform float u_time;

varying vec2 v_texCoords;

void main(){
    vec2 uv = v_texCoords;
    vec2 worldPos = (uv * u_resolution) + u_campos;

    // 波纹生成（保持原样）
    vec2 rippleUV = worldPos / DETAIL_SCALE;
    float slowTime = u_time * 0.5;
    float wave1 = texture2D(u_noise, rippleUV + vec2(slowTime * 0.2, 0)).r;
    float wave2 = texture2D(u_noise, rippleUV * 1.5 - vec2(0, slowTime * 0.1)).r;
    float compositeWave = (wave1 * 0.6 + wave2 * 0.4) * 0.8;

    // 折射效果（保持原样）
    vec2 refractOffset = vec2(
        compositeWave * 0.03 * sin(slowTime),
        (compositeWave - 0.5) * 0.03 * cos(slowTime * 0.7)
    ) * REFRACT_STRENGTH;

    // ====== 修改的颜色计算 ======
    vec4 baseColor = texture2D(u_texture, uv + refractOffset);
    float depthFactor = smoothstep(0.3, 0.7, compositeWave);

    // 水深颜色梯度（使用翡翠绿）
    vec3 waterColor = mix(
        WATER_COLOR * 0.6,  // 深水区（更暗）
        WATER_COLOR * 1.4,   // 浅水区（更亮）
        depthFactor
    );

    // 金色高光计算
    vec2 lightDir = normalize(vec2(1.0, 1.0));
    float specular = pow(max(0.0, dot(
        normalize(vec2(dFdx(compositeWave), dFdy(compositeWave))),
        lightDir
    )), 32.0) * 0.3;

    // 水域混合（包含高光）
    float waterMask = texture2D(u_water, uv).a;
    vec3 finalColor = mix(
        baseColor.rgb,
        waterColor * (baseColor.rgb + SPECULAR_COLOR * specular), // 金色高光
        waterMask
    );

    // ====== 淡黄泡沫效果 ======
    float foam = smoothstep(0.7, 0.9, compositeWave) * waterMask * 0.5;
    finalColor += foam * FOAM_COLOR * 0.3; // 淡黄色泡沫

    gl_FragColor = vec4(finalColor, baseColor.a);
}