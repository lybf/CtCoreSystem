#define HIGHP

// 水流基础色
#define WATER_COLOR vec3(0.8, 0.2, 0.4)
#define REFRACT_STRENGTH 0.1
#define WAVE_SPEED 0.0
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

    // ===== 1. 波纹生成 =====
    vec2 rippleUV = worldPos / DETAIL_SCALE;

    // 多层噪声叠加
    float wave1 = texture2D(u_noise, rippleUV + vec2(u_time * 0.002)).r;
    float wave2 = texture2D(u_noise, rippleUV * 0.7 + vec2(u_time * 0.002)).r;
    float wave3 = texture2D(u_noise, rippleUV * 0.7 + vec2(u_time * 0.002)).r;

    // 复合波形
    float compositeWave = (wave1 * 0.6 + wave2 * 0.3 + wave3 * 0.1);

    // ===== 2. 折射效果 =====
    vec2 refractOffset = vec2(
        compositeWave * 0.05 * sin(u_time * WAVE_SPEED),
        (compositeWave - 0.5) * 0.05 * cos(u_time * WAVE_SPEED * 0.7)
    ) * REFRACT_STRENGTH;

    // ===== 3. 颜色计算 =====
    vec4 baseColor = texture2D(u_texture, uv + refractOffset);

    // 水深效果（基于噪声）
    float depthFactor = smoothstep(0.3, 0.7, compositeWave);
    vec3 waterColor = mix(
        WATER_COLOR * 0.7,
        WATER_COLOR * 1.3,
        depthFactor
    );

    // ===== 4. 镜面高光 =====
    vec2 lightDir = normalize(vec2(1.0, 1.0));
    float specular = pow(max(0.0, dot(
        normalize(vec2(dFdx(compositeWave), dFdy(compositeWave))),
        lightDir
    )), 32.0) * 0.9;

    // ===== 5. 最终混合 =====
    float waterMask = texture2D(u_water, uv).a;
    vec3 finalColor = mix(
        baseColor.rgb,
        waterColor * (baseColor.rgb + vec3(specular)),
        waterMask
    );

    // ===== 6. 泡沫效果 =====
    float foam = smoothstep(0.6, 0.8, compositeWave) * waterMask;
    finalColor += foam * vec3(0.9, 0.95, 1.0) * 0.3;

    gl_FragColor = vec4(finalColor, baseColor.a);
}