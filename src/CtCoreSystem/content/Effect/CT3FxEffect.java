package CtCoreSystem.content.Effect;
//完全复制学术原版改为逻辑器使用

import CtCoreSystem.CoreSystem.type.CTColor;
import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Font;
import arc.graphics.g2d.Lines;
import arc.math.Angles;
import arc.math.Interp;
import arc.math.Mathf;
import arc.math.Rand;
import arc.math.geom.Vec2;
import arc.struct.Seq;
import arc.util.Align;
import arc.util.Structs;
import arc.util.Time;
import arc.util.Tmp;
import mindustry.content.Liquids;
import mindustry.entities.Effect;
import mindustry.entities.abilities.ForceFieldAbility;
import mindustry.gen.Unit;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.ui.Fonts;

import static CtCoreSystem.CoreSystem.type.CTColor.C;
import static arc.Core.batch;
import static arc.Core.settings;
import static arc.graphics.g2d.Draw.rect;
import static arc.graphics.g2d.Draw.*;
import static arc.graphics.g2d.Lines.*;
import static arc.math.Angles.*;
import static mindustry.Vars.renderer;
import static mindustry.Vars.tilesize;
import static mindustry.content.Blocks.container;

public class CT3FxEffect {
    public static final Rand rand = new Rand();
    public static final Vec2 v = new Vec2();
    public static final Effect
    none = new Effect(0, 0f, e -> {
    }),

    pointHit = new Effect(8f, e -> {
        color(Color.white, e.color, e.fin());
        stroke(e.fout() + 0.2f);
        Lines.circle(e.x, e.y, e.fin() * 6f);
    }),

    pointShockwave = new Effect(20, e -> {
        color(e.color);
        stroke(e.fout() * 2f);
        Lines.circle(e.x, e.y, e.finpow() * e.rotation);
        randLenVectors(e.id + 1, 8, 1f + 23f * e.finpow(), (x, y) ->
                lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 1f + e.fout() * 3f));
    }),

    moveCommand = new Effect(20, e -> {
        color(Pal.command);
        stroke(e.fout() * 5f);
        Lines.circle(e.x, e.y, 6f + e.fin() * 2f);
    }).layer(Layer.overlayUI),

    attackCommand = new Effect(20, e -> {
        color(Pal.remove);
        stroke(e.fout() * 5f);
        poly(e.x, e.y, 4, 7f + e.fin() * 2f);
    }).layer(Layer.overlayUI),

    commandSend = new Effect(28, e -> {
        color(Pal.command);
        stroke(e.fout() * 2f);
        Lines.circle(e.x, e.y, 4f + e.finpow() * e.rotation);
    }),

    upgradeCoreBloom = new Effect(80f, e -> {
        color(Pal.accent);
        stroke(4f * e.fout());
        Lines.square(e.x, e.y, tilesize / 2f * e.rotation + 2f);
    }),

    placeBlock = new Effect(16, e -> {
        color(e.color);
        stroke(3f - e.fin() * 2f);
        Lines.square(e.x, e.y, tilesize / 2f * e.rotation + e.fin() * 3f);
    }),

    coreLaunchConstruct = new Effect(35, e -> {
        color(Pal.accent);
        stroke(4f - e.fin() * 3f);
        Lines.square(e.x, e.y, tilesize / 2f * e.rotation * 1.2f + e.fin() * 5f);

        randLenVectors(e.id, 5 + (int) (e.rotation * 5), e.rotation * 3f + (tilesize * e.rotation) * e.finpow() * 1.5f, (x, y) -> {
            Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 1f + e.fout() * (4f + e.rotation));
        });
    }),

    tapBlock = new Effect(12, e -> {
        color(Pal.accent);
        stroke(3f - e.fin() * 2f);
        Lines.circle(e.x, e.y, 4f + (tilesize / 1.5f * e.rotation) * e.fin());
    }),

    breakBlock = new Effect(12, e -> {
        color(Pal.remove);
        stroke(3f - e.fin() * 2f);
        Lines.square(e.x, e.y, tilesize / 2f * e.rotation + e.fin() * 3f);

        randLenVectors(e.id, 3 + (int) (e.rotation * 3), e.rotation * 2f + (tilesize * e.rotation) * e.finpow(), (x, y) -> {
            Fill.square(e.x + x, e.y + y, 1f + e.fout() * (3f + e.rotation));
        });
    }),

    select = new Effect(23, e -> {
        color(Pal.accent);
        stroke(e.fout() * 3f);
        Lines.circle(e.x, e.y, 3f + e.fin() * 14f);
    }),

    smoke = new Effect(100, e -> {
        color(Color.gray, Pal.darkishGray, e.fin());
        Fill.circle(e.x, e.y, (7f - e.fin() * 7f) / 2f);
    }),


    rocketSmoke = new Effect(120, e -> {
        color(Color.gray);
        alpha(Mathf.clamp(e.fout() * 1.6f - Interp.pow3In.apply(e.rotation) * 1.2f));
        Fill.circle(e.x, e.y, (1f + 6f * e.rotation) - e.fin() * 2f);
    }),

    rocketSmokeLarge = new Effect(220, e -> {
        color(Color.gray);
        alpha(Mathf.clamp(e.fout() * 1.6f - Interp.pow3In.apply(e.rotation) * 1.2f));
        Fill.circle(e.x, e.y, (1f + 6f * e.rotation * 1.3f) - e.fin() * 2f);
    }),

    magmasmoke = new Effect(110, e -> {
        color(Color.gray);
        Fill.circle(e.x, e.y, e.fslope() * 6f);
    }),

    spawn = new Effect(30, e -> {
        stroke(2f * e.fout());
        color(e.color);
        Lines.poly(e.x, e.y, 4, 5f + e.fin() * 12f);
        Drawf.light(e.x , e.y , e.rotation+5f + e.fin() * 12f,C("FFFFFF"),  0.5f*e.fout());
    }),

    unitAssemble = new Effect(70, e -> {
        if (!(e.data instanceof UnitType type)) return;

        alpha(e.fout());
        mixcol(Pal.accent, e.fout());
        rect(type.fullIcon, e.x, e.y, e.rotation);
    }).layer(Layer.flyingUnit + 5f),

    padlaunch = new Effect(10, e -> {
        stroke(4f * e.fout());
        color(Pal.accent);
        Lines.poly(e.x, e.y, 4, 5f + e.fin() * 60f);
    }),

    breakProp = new Effect(23, e -> {
        float scl = Math.max(e.rotation, 1);
        color(Tmp.c1.set(e.color).mul(1.1f));
        randLenVectors(e.id, 6, 19f * e.finpow() * scl, (x, y) -> {
            Fill.circle(e.x + x, e.y + y, e.fout() * 3.5f * scl + 0.3f);
        });
    }).layer(Layer.debris),

    unitDrop = new Effect(30, e -> {
        color(Pal.lightishGray);
        randLenVectors(e.id, 9, 3 + 20f * e.finpow(), (x, y) -> {
            Fill.circle(e.x + x, e.y + y, e.fout() * 4f + 0.4f);
        });
    }).layer(Layer.debris),

    unitLand = new Effect(30, e -> {
        color(Tmp.c1.set(e.color).mul(1.1f));
        //TODO doesn't respect rotation / size
        randLenVectors(e.id, 6, 17f * e.finpow(), (x, y) -> {
            Fill.circle(e.x + x, e.y + y, e.fout() * 4f + 0.3f);
        });
    }).layer(Layer.debris),

    unitDust = new Effect(30, e -> {
        color(Tmp.c1.set(e.color).mul(1.3f));
        randLenVectors(e.id, 3, 8f * e.finpow(), e.rotation, 30f, (x, y) -> {
            Fill.circle(e.x + x, e.y + y, e.fout() * 3f + 0.3f);
        });
    }).layer(Layer.debris),

    unitLandSmall = new Effect(30, e -> {
        color(Tmp.c1.set(e.color).mul(1.1f));
        randLenVectors(e.id, (int) (6 * e.rotation), 12f * e.finpow() * e.rotation, (x, y) -> {
            Fill.circle(e.x + x, e.y + y, e.fout() * 3f + 0.1f);
        });
    }).layer(Layer.debris),

    unitPickup = new Effect(18, e -> {
        color(Pal.lightishGray);
        stroke(e.fin() * 2f);
        Lines.poly(e.x, e.y, 4, 13f * e.fout());
    }).layer(Layer.debris),


    landShock = new Effect(12, e -> {
        color(Pal.lancerLaser);
        stroke(e.fout() * 3f);
        Lines.poly(e.x, e.y, 12, 20f * e.fout());
    }).layer(Layer.debris),

    pickup = new Effect(18, e -> {
        color(Pal.lightishGray);
        stroke(e.fout() * 2f);
        Lines.spikes(e.x, e.y, 1f + e.fin() * 6f, e.fout() * 4f, 6);
    }),

    sparkExplosion = new Effect(30f, 160f, e -> {
        color(e.color);
        stroke(e.fout() * 3f);
        float circleRad = 6f + e.finpow() * e.rotation;
        Lines.circle(e.x, e.y, circleRad);

        rand.setSeed(e.id);
        for (int i = 0; i < 16; i++) {
            float angle = rand.random(360f);
            float lenRand = rand.random(0.5f, 1f);
            Lines.lineAngle(e.x, e.y, angle, e.foutpow() * e.rotation * 0.8f * rand.random(1f, 0.6f) + 2f, e.finpow() * e.rotation * 1.2f * lenRand + 6f);
        }
    }),

    titanExplosion = new Effect(30f, 160f, e -> {
        color(e.color);
        stroke(e.fout() * 3f);
        float circleRad = 6f + e.finpow() * 60f;
        Lines.circle(e.x, e.y, circleRad);

        rand.setSeed(e.id);
        for (int i = 0; i < 16; i++) {
            float angle = rand.random(360f);
            float lenRand = rand.random(0.5f, 1f);
            Lines.lineAngle(e.x, e.y, angle, e.foutpow() * 50f * rand.random(1f, 0.6f) + 2f, e.finpow() * 70f * lenRand + 6f);
        }
    }),

    titanSmoke = new Effect(300f, 300f, b -> {
        float intensity = 3f;

        color(b.color, 0.7f);
        for (int i = 0; i < 4; i++) {
            rand.setSeed(b.id * 2 + i);
            float lenScl = rand.random(0.5f, 1f);
            int fi = i;
            b.scaled(b.lifetime * lenScl, e -> {
                randLenVectors(e.id + fi - 1, e.fin(Interp.pow10Out), (int) (2.9f * intensity), 22f * intensity, (x, y, in, out) -> {
                    float fout = e.fout(Interp.pow5Out) * rand.random(0.5f, 1f);
                    float rad = fout * ((2f + intensity) * 2.35f);

                    Fill.circle(e.x + x, e.y + y, rad);
                    Drawf.light(e.x + x, e.y + y, rad * 2.5f, b.color, 0.5f);
                });
            });
        }
    }),

    missileTrailSmoke = new Effect(180f, 300f, b -> {
        float intensity = 2f;

        color(b.color, 0.7f);
        for (int i = 0; i < 4; i++) {
            rand.setSeed(b.id * 2 + i);
            float lenScl = rand.random(0.5f, 1f);
            int fi = i;
            b.scaled(b.lifetime * lenScl, e -> {
                randLenVectors(e.id + fi - 1, e.fin(Interp.pow10Out), (int) (2.9f * intensity), 13f * intensity, (x, y, in, out) -> {
                    float fout = e.fout(Interp.pow5Out) * rand.random(0.5f, 1f);
                    float rad = fout * ((2f + intensity) * 2.35f);

                    Fill.circle(e.x + x, e.y + y, rad);
                    Drawf.light(e.x + x, e.y + y, rad * 2.5f, b.color, 0.5f);
                });
            });
        }
    }).layer(Layer.bullet - 1f),

    neoplasmSplat = new Effect(400f, 300f, b -> {
        float intensity = 3f;

        color(Pal.neoplasm1);
        for (int i = 0; i < 4; i++) {
            rand.setSeed(b.id * 2 + i);
            float lenScl = rand.random(0.5f, 1f);
            int fi = i;
            b.scaled(b.lifetime * lenScl, e -> {
                randLenVectors(e.id + fi - 1, e.fin(Interp.pow10Out), (int) (5f * intensity), 22f * intensity, (x, y, in, out) -> {
                    float fout = e.fout(Interp.pow5Out) * rand.random(0.5f, 1f);
                    float rad = fout * ((2f + intensity) * 1.35f);

                    Fill.circle(e.x + x, e.y + y, rad);
                    Drawf.light(e.x + x, e.y + y, rad * 2.5f, b.color, 0.5f);
                });
            });
        }
    }).layer(Layer.bullet - 2f),

    scatheExplosion = new Effect(60f, 160f, e -> {
        color(e.color);
        stroke(e.fout() * 5f);
        float circleRad = 6f + e.finpow() * 60f;
        Lines.circle(e.x, e.y, circleRad);

        rand.setSeed(e.id);
        for (int i = 0; i < 16; i++) {
            float angle = rand.random(360f);
            float lenRand = rand.random(0.5f, 1f);
            Tmp.v1.trns(angle, circleRad);

            for (int s : Mathf.signs) {
                Drawf.tri(e.x + Tmp.v1.x, e.y + Tmp.v1.y, e.foutpow() * 40f, e.fout() * 30f * lenRand + 6f, angle + 90f + s * 90f);
            }
        }
    }),

    scatheLight = new Effect(60f, 160f, e -> {
        float circleRad = 6f + e.finpow() * 60f;

        color(e.color, e.foutpow());
        Fill.circle(e.x, e.y, circleRad);
    }).layer(Layer.bullet + 2f),

    scatheSlash = new Effect(40f, 160f, e -> {
        Draw.color(e.color);
        for (int s : Mathf.signs) {
            Drawf.tri(e.x, e.y, e.fout() * 25f, e.foutpow() * 66f + 6f, e.rotation + s * 90f);
        }
    }),

    dynamicSpikes = new Effect(40f, 100f, e -> {
        color(e.color);
        stroke(e.fout() * 2f);
        float circleRad = 4f + e.finpow() * e.rotation;
        Lines.circle(e.x, e.y, circleRad);

        for (int i = 0; i < 4; i++) {
            Drawf.tri(e.x, e.y, 6f, e.rotation * 1.5f * e.fout(), i * 90);
        }

        color();
        for (int i = 0; i < 4; i++) {
            Drawf.tri(e.x, e.y, 3f, e.rotation * 1.45f / 3f * e.fout(), i * 90);
        }

        Drawf.light(e.x, e.y, circleRad * 1.6f, Pal.heal, e.fout());
    }),

    greenBomb = new Effect(40f, 100f, e -> {
        color(Pal.heal);
        stroke(e.fout() * 2f);
        float circleRad = 4f + e.finpow() * 65f;
        Lines.circle(e.x, e.y, circleRad);

        color(Pal.heal);
        for (int i = 0; i < 4; i++) {
            Drawf.tri(e.x, e.y, 6f, 100f * e.fout(), i * 90);
        }

        color();
        for (int i = 0; i < 4; i++) {
            Drawf.tri(e.x, e.y, 3f, 35f * e.fout(), i * 90);
        }

        Drawf.light(e.x, e.y, circleRad * 1.6f, Pal.heal, e.fout());
    }),
            greenBomb2 = new Effect(40f, 100f, e -> {
                color( C("f7beff"));
                stroke(e.fout() * 2f);
                float circleRad = 4f + e.finpow() * 65f;
                Lines.circle(e.x, e.y, circleRad);

                color( C("f7beff"));
                for (int i = 0; i < 4; i++) {
                    Drawf.tri(e.x, e.y, 6f, 100f * e.fout(), i * 90);
                }

                color();
                for (int i = 0; i < 4; i++) {
                    Drawf.tri(e.x, e.y, 3f, 35f * e.fout(), i * 90);
                }

                Drawf.light(e.x, e.y, circleRad * 1.6f, Pal.heal, e.fout());
            }),
    greenLaserCharge = new Effect(80f, 100f, e -> {
        color(Pal.heal);
        stroke(e.fin() * 2f);
        Lines.circle(e.x, e.y, 4f + e.fout() * 100f);

        Fill.circle(e.x, e.y, e.fin() * 20);

        randLenVectors(e.id, 20, 40f * e.fout(), (x, y) -> {
            Fill.circle(e.x + x, e.y + y, e.fin() * 5f);
            Drawf.light(e.x + x, e.y + y, e.fin() * 15f, Pal.heal, 0.7f);
        });

        color();

        Fill.circle(e.x, e.y, e.fin() * 10);
        Drawf.light(e.x, e.y, e.fin() * 20f, Pal.heal, 0.7f);
    }).followParent(true).rotWithParent(true),

    greenLaserChargeSmall = new Effect(40f, 100f, e -> {
        color(Pal.heal);
        stroke(e.fin() * 2f);
        Lines.circle(e.x, e.y, e.fout() * 50f);
    }).followParent(true).rotWithParent(true),

    greenCloud = new Effect(80f, e -> {
        color(Pal.heal);
        randLenVectors(e.id, e.fin(), 7, 9f, (x, y, fin, fout) -> {
            Fill.circle(e.x + x, e.y + y, 5f * fout);
        });
    }),

    healWaveDynamic = new Effect(22, e -> {
        color(Pal.heal);
        stroke(e.fout() * 2f);
        Lines.circle(e.x, e.y, 4f + e.finpow() * e.rotation);
    }),

    healWave = new Effect(22, e -> {
        color(Pal.heal);
        stroke(e.fout() * 2f);
        Lines.circle(e.x, e.y, 4f + e.finpow() * 60f);
    }),

    heal = new Effect(11, e -> {
        color(Pal.heal);
        stroke(e.fout() * 2f);
        Lines.circle(e.x, e.y, 2f + e.finpow() * 7f);
    }),

    dynamicWave = new Effect(22, e -> {
        color(e.color, 0.7f);
        stroke(e.fout() * 2f);
        Lines.circle(e.x, e.y, 4f + e.finpow() * e.rotation);
    }),

    shieldWave = new Effect(22, e -> {
        color(e.color, 0.7f);
        stroke(e.fout() * 2f);
        Lines.circle(e.x, e.y, 4f + e.finpow() * 60f);
    }),

    shieldApply = new Effect(11, e -> {
        color(e.color, 0.7f);
        stroke(e.fout() * 2f);
        Lines.circle(e.x, e.y, 2f + e.finpow() * 7f);
    }),

    disperseTrail = new Effect(13, e -> {
        color(Color.white, e.color, e.fin());
        stroke(0.6f + e.fout() * 1.7f);
        rand.setSeed(e.id);

        for (int i = 0; i < 2; i++) {
            float rot = e.rotation + rand.range(15f) + 180f;
            v.trns(rot, rand.random(e.fin() * 27f));
            lineAngle(e.x + v.x, e.y + v.y, rot, e.fout() * rand.random(2f, 7f) + 1.5f);
        }
    }),


    hitBulletSmall = new Effect(14, e -> {
        color(Color.white, Pal.lightOrange, e.fin());

        e.scaled(7f, s -> {
            stroke(0.5f + s.fout());
            Lines.circle(e.x, e.y, s.fin() * 5f);
        });

        stroke(0.5f + e.fout());

        randLenVectors(e.id, 5, e.fin() * 15f, (x, y) -> {
            float ang = Mathf.angle(x, y);
            lineAngle(e.x + x, e.y + y, ang, e.fout() * 3 + 1f);
        });

        Drawf.light(e.x, e.y, 20f, Pal.lightOrange, 0.6f * e.fout());
    }),

    hitBulletColor = new Effect(14, e -> {
        color(Color.white, e.color, e.fin());

        e.scaled(7f, s -> {
            stroke(0.5f + s.fout());
            Lines.circle(e.x, e.y, s.fin() * 5f);
        });

        stroke(0.5f + e.fout());

        randLenVectors(e.id, 5, e.fin() * 15f, (x, y) -> {
            float ang = Mathf.angle(x, y);
            lineAngle(e.x + x, e.y + y, ang, e.fout() * 3 + 1f);
        });

        Drawf.light(e.x, e.y, 20f, e.color, 0.6f * e.fout());
    }),

    hitSquaresColor = new Effect(14, e -> {
        color(Color.white, e.color, e.fin());

        e.scaled(7f, s -> {
            stroke(0.5f + s.fout());
            Lines.circle(e.x, e.y, s.fin() * 5f);
        });

        stroke(0.5f + e.fout());

        randLenVectors(e.id, 5, e.fin() * 17f, (x, y) -> {
            float ang = Mathf.angle(x, y);
            Fill.square(e.x + x, e.y + y, e.fout() * 3.2f, ang);
        });

        Drawf.light(e.x, e.y, 20f, e.color, 0.6f * e.fout());
    }),

    hitFuse = new Effect(14, e -> {
        color(Color.white, Pal.surge, e.fin());

        e.scaled(7f, s -> {
            stroke(0.5f + s.fout());
            Lines.circle(e.x, e.y, s.fin() * 7f);
        });

        stroke(0.5f + e.fout());

        randLenVectors(e.id, 6, e.fin() * 15f, (x, y) -> {
            float ang = Mathf.angle(x, y);
            lineAngle(e.x + x, e.y + y, ang, e.fout() * 3 + 1f);
        });
    }),

    hitBulletBig = new Effect(13, e -> {
        color(Color.white, Pal.lightOrange, e.fin());
        stroke(0.5f + e.fout() * 1.5f);

        randLenVectors(e.id, 8, e.finpow() * 30f, e.rotation, 50f, (x, y) -> {
            float ang = Mathf.angle(x, y);
            lineAngle(e.x + x, e.y + y, ang, e.fout() * 4 + 1.5f);
        });
    }),

    hitFlameSmall = new Effect(14, e -> {
        color(Pal.lightFlame, Pal.darkFlame, e.fin());
        stroke(0.5f + e.fout());

        randLenVectors(e.id, 2, 1f + e.fin() * 15f, e.rotation, 50f, (x, y) -> {
            float ang = Mathf.angle(x, y);
            lineAngle(e.x + x, e.y + y, ang, e.fout() * 3 + 1f);
        });
    }),

    hitFlamePlasma = new Effect(14, e -> {
        color(e.color, e.fin());
        stroke(0.5f + e.fout());

        randLenVectors(e.id, 2, 1f + e.fin() * 15f, e.rotation, 50f, (x, y) -> {
            float ang = Mathf.angle(x, y);
            lineAngle(e.x + x, e.y + y, ang, e.fout() * 3 + 1f);
        });
    }),

    hitFlameBeam = new Effect(19, e -> {
        color(e.color);

        randLenVectors(e.id, 7, e.finpow() * 11f, (x, y) -> {
            Fill.circle(e.x + x, e.y + y, e.fout() * 2 + 0.5f);
        });
    }),
            hitBeam = new Effect(12, e -> {
                color(e.color);
                stroke(e.fout() * 2f);

                randLenVectors(e.id, 6, e.finpow() * 18f, (x, y) -> {
                    float ang = Mathf.angle(x, y);
                    lineAngle(e.x + x, e.y + y, ang, e.fout() * 4 + 1f);
                });
            }),

    instBomb = new Effect(15f, 100f, e -> {
        color(Pal.bulletYellowBack);
        stroke(e.fout() * 4f);
        Lines.circle(e.x, e.y, 4f + e.finpow() * 20f);

        for (int i = 0; i < 4; i++) {
            Drawf.tri(e.x, e.y, 6f, 80f * e.fout(), i * 90 + 45);
        }

        color();
        for (int i = 0; i < 4; i++) {
            Drawf.tri(e.x, e.y, 3f, 30f * e.fout(), i * 90 + 45);
        }

        Drawf.light(e.x, e.y, 150f, Pal.bulletYellowBack, 0.9f * e.fout());
    }),


    instShoot = new Effect(24f, e -> {
        e.scaled(10f, b -> {
            color(Color.white, Pal.bulletYellowBack, b.fin());
            stroke(b.fout() * 3f + 0.2f);
            Lines.circle(b.x, b.y, b.fin() * 50f);
        });

        color(Pal.bulletYellowBack);

        for (int i : Mathf.signs) {
            Drawf.tri(e.x, e.y, 13f * e.fout(), 85f, e.rotation + 90f * i);
            Drawf.tri(e.x, e.y, 13f * e.fout(), 50f, e.rotation + 20f * i);
        }

        Drawf.light(e.x, e.y, 180f, Pal.bulletYellowBack, 0.9f * e.fout());
    }),

    instHit = new Effect(20f, 200f, e -> {
        color(Pal.bulletYellowBack);

        for (int i = 0; i < 2; i++) {
            color(i == 0 ? Pal.bulletYellowBack : Pal.bulletYellow);

            float m = i == 0 ? 1f : 0.5f;

            for (int j = 0; j < 5; j++) {
                float rot = e.rotation + Mathf.randomSeedRange(e.id + j, 50f);
                float w = 23f * e.fout() * m;
                Drawf.tri(e.x, e.y, w, (80f + Mathf.randomSeedRange(e.id + j, 40f)) * m, rot);
                Drawf.tri(e.x, e.y, w, 20f * m, rot + 180f);
            }
        }

        e.scaled(10f, c -> {
            color(Pal.bulletYellow);
            stroke(c.fout() * 2f + 0.2f);
            Lines.circle(e.x, e.y, c.fin() * 30f);
        });

        e.scaled(12f, c -> {
            color(Pal.bulletYellowBack);
            randLenVectors(e.id, 25, 5f + e.fin() * 80f, e.rotation, 60f, (x, y) -> {
                Fill.square(e.x + x, e.y + y, c.fout() * 3f, 45f);
            });
        });
    }),

    hitLaser = new Effect(8, e -> {
        color(Color.white, Pal.heal, e.fin());
        stroke(0.5f + e.fout());
        Lines.circle(e.x, e.y, e.fin() * 5f);

        Drawf.light(e.x, e.y, 23f, Pal.heal, e.fout() * 0.7f);
    }),

    hitLaserColor = new Effect(8, e -> {
        color(Color.white, e.color, e.fin());
        stroke(0.5f + e.fout());
        Lines.circle(e.x, e.y, e.fin() * 5f);

        Drawf.light(e.x, e.y, 23f, e.color, e.fout() * 0.7f);
    }),

    despawn = new Effect(12, e -> {
        color(Pal.lighterOrange, Color.gray, e.fin());
        stroke(e.fout());

        randLenVectors(e.id, 7, e.fin() * 7f, e.rotation, 40f, (x, y) -> {
            float ang = Mathf.angle(x, y);
            lineAngle(e.x + x, e.y + y, ang, e.fout() * 2 + 1f);
        });

    }),

    airBubble = new Effect(100f, e -> {
        randLenVectors(e.id, 1, e.fin() * 12f, (x, y) -> {
            rect(renderer.bubbles[Math.min((int) (renderer.bubbles.length * Mathf.curveMargin(e.fin(), 0.11f, 0.06f)), renderer.bubbles.length - 1)], e.x + x, e.y + y);
        });
    }).layer(Layer.flyingUnitLow + 1),

    massiveExplosion = new Effect(30, e -> {
        color(e.color);

        e.scaled(7, i -> {
            stroke(3f * i.fout());
            Lines.circle(e.x, e.y, 4f + i.fin() * 30f);
        });

        color(Color.gray);

        randLenVectors(e.id, 8, 2f + 30f * e.finpow(), (x, y) -> {
            Fill.circle(e.x + x, e.y + y, e.fout() * 4f + 0.5f);
        });

        color(Pal.missileYellowBack);
        stroke(e.fout());

        randLenVectors(e.id + 1, 6, 1f + 29f * e.finpow(), (x, y) -> {
            lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 1f + e.fout() * 4f);
        });

        Drawf.light(e.x, e.y, 50f, Pal.missileYellowBack, 0.8f * e.fout());
    }),

    colorTrail = new Effect(50, e -> {
        color(e.color);
        Fill.circle(e.x, e.y, e.rotation * e.fout());
    }),

    absorb = new Effect(12, e -> {
        color(Pal.accent);
        stroke(2f * e.fout());
        Lines.circle(e.x, e.y, 5f * e.fout());
    }),

    forceShrink = new Effect(20, e -> {
        color(e.color, e.fout());
        if (renderer.animateShields) {
            Fill.poly(e.x, e.y, 6, e.rotation * e.fout());
        } else {
            stroke(1.5f);
            Draw.alpha(0.09f);
            Fill.poly(e.x, e.y, 6, e.rotation * e.fout());
            Draw.alpha(1f);
            Lines.poly(e.x, e.y, 6, e.rotation * e.fout());
        }
    }).layer(Layer.shields),

    flakExplosionBig = new Effect(30, e -> {
        color(Pal.bulletYellowBack);

        e.scaled(6, i -> {
            stroke(3f * i.fout());
            Lines.circle(e.x, e.y, 3f + i.fin() * 25f);
        });

        color(Color.gray);

        randLenVectors(e.id, 6, 2f + 23f * e.finpow(), (x, y) -> {
            Fill.circle(e.x + x, e.y + y, e.fout() * 4f + 0.5f);
        });

        color(Pal.bulletYellow);
        stroke(e.fout());

        randLenVectors(e.id + 1, 4, 1f + 23f * e.finpow(), (x, y) -> {
            lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 1f + e.fout() * 3f);
        });

        Drawf.light(e.x, e.y, 60f, Pal.bulletYellowBack, 0.7f * e.fout());
    }),

    burning = new Effect(35f, e -> {
        color(Pal.lightFlame, Pal.darkFlame, e.fin());

        randLenVectors(e.id, 3, 2f + e.fin() * 7f, (x, y) -> {
            Fill.circle(e.x + x, e.y + y, 0.1f + e.fout() * 1.4f);
        });
    }),

    fire = new Effect(50f, e -> {
        color(Pal.lightFlame, Pal.darkFlame, e.fin());

        randLenVectors(e.id, 2, 2f + e.fin() * 9f, (x, y) -> {
            Fill.circle(e.x + x, e.y + y, 0.2f + e.fslope() * 1.5f);
        });

        color();

        Drawf.light(e.x, e.y, 20f * e.fslope(), Pal.lightFlame, 0.5f);
    }),

    fireHit = new Effect(35f, e -> {
        color(Pal.lightFlame, Pal.darkFlame, e.fin());

        randLenVectors(e.id, 3, 2f + e.fin() * 10f, (x, y) -> {
            Fill.circle(e.x + x, e.y + y, 0.2f + e.fout() * 1.6f);
        });

        color();
    }),

    fireSmoke = new Effect(35f, e -> {
        color(Color.gray);

        randLenVectors(e.id, 1, 2f + e.fin() * 7f, (x, y) -> {
            Fill.circle(e.x + x, e.y + y, 0.2f + e.fslope() * 1.5f);
        });
    }),

    //TODO needs a lot of work
    neoplasmHeal = new Effect(120f, e -> {
        color(Pal.neoplasm1, Pal.neoplasm2, e.fin());

        randLenVectors(e.id, 1, e.fin() * 3f, (x, y) -> {
            Fill.circle(e.x + x, e.y + y, 0.2f + e.fslope() * 2f);
        });
    }).followParent(true).rotWithParent(true).layer(Layer.bullet - 2),

    steam = new Effect(35f, e -> {
        color(Color.lightGray);

        randLenVectors(e.id, 2, 2f + e.fin() * 7f, (x, y) -> {
            Fill.circle(e.x + x, e.y + y, 0.2f + e.fslope() * 1.5f);
        });
    }),

    ventSteam = new Effect(140f, e -> {
        color(e.color, Pal.vent2, e.fin());

        alpha(e.fslope() * 0.78f);

        float length = 3f + e.finpow() * 10f;
        rand.setSeed(e.id);
        for (int i = 0; i < rand.random(3, 5); i++) {
            v.trns(rand.random(360f), rand.random(length));
            Fill.circle(e.x + v.x, e.y + v.y, rand.random(1.2f, 3.5f) + e.fslope() * 1.1f);
        }
    }).layer(Layer.darkness - 1),

    fluxVapor = new Effect(140f, e -> {
        color(e.color);
        alpha(e.fout() * 0.7f);

        randLenVectors(e.id, 2, 3f + e.finpow() * 10f, (x, y) -> {
            Fill.circle(e.x + x, e.y + y, 0.6f + e.fin() * 5f);
        });
    }).layer(Layer.bullet - 1f),

    vapor = new Effect(110f, e -> {
        color(e.color);
        alpha(e.fout());

        randLenVectors(e.id, 3, 2f + e.finpow() * 11f, (x, y) -> {
            Fill.circle(e.x + x, e.y + y, 0.6f + e.fin() * 5f);
        });
    }),

    vaporSmall = new Effect(50f, e -> {
        color(e.color);
        alpha(e.fout());

        randLenVectors(e.id, 4, 2f + e.finpow() * 5f, (x, y) -> {
            Fill.circle(e.x + x, e.y + y, 1f + e.fin() * 4f);
        });
    }),

    fireballsmoke = new Effect(25f, e -> {
        color(Color.gray);

        randLenVectors(e.id, 1, 2f + e.fin() * 7f, (x, y) -> {
            Fill.circle(e.x + x, e.y + y, 0.2f + e.fout() * 1.5f);
        });
    }),


    freezing = new Effect(40f, e -> {
        color(e.color);

        randLenVectors(e.id, 2, 1f + e.fin() * 2f, (x, y) -> {
            Fill.circle(e.x + x, e.y + y, e.fout() * 1.2f);
        });
    }),

    wet = new Effect(80f, e -> {
        color(Liquids.water.color);
        alpha(Mathf.clamp(e.fin() * 2f));

        Fill.circle(e.x, e.y, e.fout());
    }),

    muddy = new Effect(80f, e -> {
        color(Pal.muddy);
        alpha(Mathf.clamp(e.fin() * 2f));

        Fill.circle(e.x, e.y, e.fout());
    }),

    sapped = new Effect(40f, e -> {
        color(Pal.sap);

        randLenVectors(e.id, 2, 1f + e.fin() * 2f, (x, y) -> {
            Fill.square(e.x + x, e.y + y, e.fslope() * 1.1f, 45f);
        });
    }),

    electrified = new Effect(40f, e -> {
        color(Pal.heal);

        randLenVectors(e.id, 2, 1f + e.fin() * 2f, (x, y) -> {
            Fill.square(e.x + x, e.y + y, e.fslope() * 1.1f, 45f);
        });
    }),

    sporeSlowed = new Effect(40f, e -> {
        color(Pal.spore);

        Fill.circle(e.x, e.y, e.fslope() * 1.1f);
    }),

    oily = new Effect(42f, e -> {
        color(Liquids.oil.color);

        randLenVectors(e.id, 2, 1f + e.fin() * 2f, (x, y) -> {
            Fill.circle(e.x + x, e.y + y, e.fout());
        });
    }),

    overdriven = new Effect(20f, e -> {
        color(e.color);

        randLenVectors(e.id, 2, 1f + e.fin() * 2f, (x, y) -> {
            Fill.square(e.x + x, e.y + y, e.fout() * 2.3f + 0.5f);
        });
    }),

    overclocked = new Effect(50f, e -> {
        color(e.color);

        Fill.square(e.x, e.y, e.fslope() * 2f, 45f);
    }),

    shockwave = new Effect(10f, 80f, e -> {
        color(Color.white, Color.lightGray, e.fin());
        stroke(e.fout() * 2f + 0.2f);
        Lines.circle(e.x, e.y, e.fin() * 28f);
    }),

    bigShockwave = new Effect(10f, 80f, e -> {
        color(Color.white, Color.lightGray, e.fin());
        stroke(e.fout() * 3f);
        Lines.circle(e.x, e.y, e.fin() * 50f);
    }),

    spawnShockwave = new Effect(20f, 400f, e -> {
        color(Color.white, Color.lightGray, e.fin());
        stroke(e.fout() * 3f + 0.5f);
        Lines.circle(e.x, e.y, e.fin() * (e.rotation + 50f));
    }),

    explosion = new Effect(30, e -> {
        e.scaled(7, i -> {
            stroke(3f * i.fout());
            Lines.circle(e.x, e.y, 3f + i.fin() * 10f);
        });

        color(Color.gray);

        randLenVectors(e.id, 6, 2f + 19f * e.finpow(), (x, y) -> {
            Fill.circle(e.x + x, e.y + y, e.fout() * 3f + 0.5f);
            Fill.circle(e.x + x / 2f, e.y + y / 2f, e.fout());
        });

        color(Pal.lighterOrange, Pal.lightOrange, Color.gray, e.fin());
        stroke(1.5f * e.fout());

        randLenVectors(e.id + 1, 8, 1f + 23f * e.finpow(), (x, y) -> {
            lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 1f + e.fout() * 3f);
        });
    }),

    dynamicExplosion = new Effect(30, 500f, b -> {
        float intensity = b.rotation;
        float baseLifetime = 26f + intensity * 15f;
        b.lifetime = 43f + intensity * 35f;

        color(Color.gray);
        //TODO awful borders with linear filtering here
        alpha(0.9f);
        for (int i = 0; i < 4; i++) {
            rand.setSeed(b.id * 2 + i);
            float lenScl = rand.random(0.4f, 1f);
            int fi = i;
            b.scaled(b.lifetime * lenScl, e -> {
                randLenVectors(e.id + fi - 1, e.fin(Interp.pow10Out), (int) (3f * intensity), 14f * intensity, (x, y, in, out) -> {
                    float fout = e.fout(Interp.pow5Out) * rand.random(0.5f, 1f);
                    Fill.circle(e.x + x, e.y + y, fout * ((2f + intensity) * 1.8f));
                });
            });
        }

        b.scaled(baseLifetime, e -> {
            e.scaled(5 + intensity * 2.5f, i -> {
                stroke((3.1f + intensity / 5f) * i.fout());
                Lines.circle(e.x, e.y, (3f + i.fin() * 14f) * intensity);
                Drawf.light(e.x, e.y, i.fin() * 14f * 2f * intensity, Color.white, 0.9f * e.fout());
            });

            color(Pal.lighterOrange, Pal.lightOrange, Color.gray, e.fin());
            stroke((1.7f * e.fout()) * (1f + (intensity - 1f) / 2f));

            Draw.z(Layer.effect + 0.001f);
            randLenVectors(e.id + 1, e.finpow() + 0.001f, (int) (9 * intensity), 40f * intensity, (x, y, in, out) -> {
                lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 1f + out * 4 * (3f + intensity));
                Drawf.light(e.x + x, e.y + y, (out * 4 * (3f + intensity)) * 3.5f, Draw.getColor(), 0.8f);
            });
        });
    }),

    reactorExplosion = new Effect(30, 500f, b -> {
        float intensity = 6.8f;
        float baseLifetime = 25f + intensity * 11f;
        b.lifetime = 50f + intensity * 65f;

        color(b.color);
        alpha(0.7f);
        for (int i = 0; i < 4; i++) {
            rand.setSeed(b.id * 2 + i);
            float lenScl = rand.random(0.4f, 1f);
            int fi = i;
            b.scaled(b.lifetime * lenScl, e -> {
                randLenVectors(e.id + fi - 1, e.fin(Interp.pow10Out), (int) (2.9f * intensity), 22f * intensity, (x, y, in, out) -> {
                    float fout = e.fout(Interp.pow5Out) * rand.random(0.5f, 1f);
                    float rad = fout * ((2f + intensity) * 2.35f);

                    Fill.circle(e.x + x, e.y + y, rad);
                    Drawf.light(e.x + x, e.y + y, rad * 2.5f, Pal.reactorPurple, 0.5f);
                });
            });
        }

        b.scaled(baseLifetime, e -> {
            Draw.color(e.color);
            e.scaled(5 + intensity * 2f, i -> {
                stroke((3.1f + intensity / 5f) * i.fout());
                Lines.circle(e.x, e.y, (3f + i.fin() * 14f) * intensity);
                Drawf.light(e.x, e.y, i.fin() * 14f * 2f * intensity, Color.white, 0.9f * e.fout());
            });

            color(Pal.lighterOrange, Pal.reactorPurple, e.fin());
            stroke((2f * e.fout()));

            Draw.z(Layer.effect + 0.001f);
            randLenVectors(e.id + 1, e.finpow() + 0.001f, (int) (8 * intensity), 28f * intensity, (x, y, in, out) -> {
                lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 1f + out * 4 * (4f + intensity));
                Drawf.light(e.x + x, e.y + y, (out * 4 * (3f + intensity)) * 3.5f, Draw.getColor(), 0.8f);
            });
        });
    }),

    blockExplosionSmoke = new Effect(30, e -> {
        color(e.color);

        randLenVectors(e.id, 6, 4f + 30f * e.finpow(), (x, y) -> {
            Fill.circle(e.x + x, e.y + y, e.fout() * 3f);
            Fill.circle(e.x + x / 2f, e.y + y / 2f, e.fout());
        });
    }),

    smokePuff = new Effect(30, e -> {
        color(e.color);

        randLenVectors(e.id, 6, 4f + 30f * e.finpow(), (x, y) -> {
            Fill.circle(e.x + x, e.y + y, e.fout() * 3f);
            Fill.circle(e.x + x / 2f, e.y + y / 2f, e.fout());
        });
    }),

    shootSmall = new Effect(8, e -> {
        color(Pal.lighterOrange, Pal.lightOrange, e.fin());
        float w = 1f + 5 * e.fout();
        Drawf.tri(e.x, e.y, w, 15f * e.fout(), e.rotation);
        Drawf.tri(e.x, e.y, w, 3f * e.fout(), e.rotation + 180f);
    }),

    shootSmallColor = new Effect(8, e -> {
        color(e.color, Color.gray, e.fin());
        float w = 1f + 5 * e.fout();
        Drawf.tri(e.x, e.y, w, 15f * e.fout(), e.rotation);
        Drawf.tri(e.x, e.y, w, 3f * e.fout(), e.rotation + 180f);
    }),

    shootHeal = new Effect(8, e -> {
        color(Pal.heal);
        float w = 1f + 5 * e.fout();
        Drawf.tri(e.x, e.y, w, 17f * e.fout(), e.rotation);
        Drawf.tri(e.x, e.y, w, 4f * e.fout(), e.rotation + 180f);
    }),

    shootHealYellow = new Effect(8, e -> {
        color(Pal.lightTrail);
        float w = 1f + 5 * e.fout();
        Drawf.tri(e.x, e.y, w, 17f * e.fout(), e.rotation);
        Drawf.tri(e.x, e.y, w, 4f * e.fout(), e.rotation + 180f);
    }),

    shootSmallSmoke = new Effect(20f, e -> {
        color(Pal.lighterOrange, Color.lightGray, Color.gray, e.fin());

        randLenVectors(e.id, 5, e.finpow() * 6f, e.rotation, 20f, (x, y) -> {
            Fill.circle(e.x + x, e.y + y, e.fout() * 1.5f);
        });
    }),

    shootBig = new Effect(9, e -> {
        color(Pal.lighterOrange, Pal.lightOrange, e.fin());
        float w = 1.2f + 7 * e.fout();
        Drawf.tri(e.x, e.y, w, 25f * e.fout(), e.rotation);
        Drawf.tri(e.x, e.y, w, 4f * e.fout(), e.rotation + 180f);
    }),

    shootBig2 = new Effect(10, e -> {
        color(Pal.lightOrange, Color.gray, e.fin());
        float w = 1.2f + 8 * e.fout();
        Drawf.tri(e.x, e.y, w, 29f * e.fout(), e.rotation);
        Drawf.tri(e.x, e.y, w, 5f * e.fout(), e.rotation + 180f);
    }),

    shootBigColor = new Effect(11, e -> {
        color(e.color, Color.gray, e.fin());
        float w = 1.2f + 9 * e.fout();
        Drawf.tri(e.x, e.y, w, 32f * e.fout(), e.rotation);
        Drawf.tri(e.x, e.y, w, 3f * e.fout(), e.rotation + 180f);
    }),

    shootTitan = new Effect(10, e -> {
        color(Pal.lightOrange, e.color, e.fin());
        float w = 1.3f + 10 * e.fout();
        Drawf.tri(e.x, e.y, w, 35f * e.fout(), e.rotation);
        Drawf.tri(e.x, e.y, w, 6f * e.fout(), e.rotation + 180f);
    }),

    shootBigSmoke = new Effect(17f, e -> {
        color(Pal.lighterOrange, Color.lightGray, Color.gray, e.fin());

        randLenVectors(e.id, 8, e.finpow() * 19f, e.rotation, 10f, (x, y) -> {
            Fill.circle(e.x + x, e.y + y, e.fout() * 2f + 0.2f);
        });
    }),

    shootBigSmoke2 = new Effect(18f, e -> {
        color(Pal.lightOrange, Color.lightGray, Color.gray, e.fin());

        randLenVectors(e.id, 9, e.finpow() * 23f, e.rotation, 20f, (x, y) -> {
            Fill.circle(e.x + x, e.y + y, e.fout() * 2.4f + 0.2f);
        });
    }),

    shootSmokeDisperse = new Effect(25f, e -> {
        color(Pal.lightOrange, Color.white, Color.gray, e.fin());

        randLenVectors(e.id, 9, e.finpow() * 29f, e.rotation, 18f, (x, y) -> {
            Fill.circle(e.x + x, e.y + y, e.fout() * 2.2f + 0.1f);
        });
    }),

    shootSmokeSquare = new Effect(20f, e -> {
        color(Color.white, e.color, e.fin());

        rand.setSeed(e.id);
        for (int i = 0; i < 6; i++) {
            float rot = e.rotation + rand.range(22f);
            v.trns(rot, rand.random(e.finpow() * 21f));
            Fill.poly(e.x + v.x, e.y + v.y, 4, e.fout() * 2f + 0.2f, rand.random(360f));
        }
    }),

    shootSmokeSquareSparse = new Effect(30f, e -> {
        color(Color.white, e.color, e.fin());

        rand.setSeed(e.id);
        for (int i = 0; i < 2; i++) {
            float rot = e.rotation + rand.range(30f);
            v.trns(rot, rand.random(e.finpow() * 27f));
            Fill.poly(e.x + v.x, e.y + v.y, 4, e.fout() * 3.8f + 0.2f, rand.random(360f));
        }
    }),

    shootSmokeSquareBig = new Effect(32f, e -> {
        color(Color.white, e.color, e.fin());
        rand.setSeed(e.id);
        for (int i = 0; i < 13; i++) {
            float rot = e.rotation + rand.range(26f);
            v.trns(rot, rand.random(e.finpow() * 30f));
            Fill.poly(e.x + v.x, e.y + v.y, 4, e.fout() * 4f + 0.2f, rand.random(360f));
        }
    }),

    shootSmokeMissile = new Effect(130f, 300f, e -> {
        color(Pal.redLight);
        alpha(0.5f);
        rand.setSeed(e.id);
        for (int i = 0; i < 35; i++) {
            v.trns(e.rotation + 180f + rand.range(21f), rand.random(e.finpow() * 90f)).add(rand.range(3f), rand.range(3f));
            e.scaled(e.lifetime * rand.random(0.2f, 1f), b -> {
                Fill.circle(e.x + v.x, e.y + v.y, b.fout() * 9f + 0.3f);
            });
        }
    }),

    regenParticle = new Effect(100f, e -> {
        color(Pal.regen);

        Fill.square(e.x, e.y, e.fslope() * 1.5f + 0.14f, 45f);
    }),

    regenSuppressParticle = new Effect(35f, e -> {
        color(Pal.sapBullet, e.color, e.fin());
        stroke(e.fout() * 1.4f + 0.5f);

        randLenVectors(e.id, 4, 17f * e.fin(), (x, y) -> {
            lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fslope() * 3f + 0.5f);
        });
    }),

    surgeCruciSmoke = new Effect(160f, e -> {
        color(Pal.slagOrange);
        alpha(0.6f);

        rand.setSeed(e.id);
        for (int i = 0; i < 3; i++) {
            float len = rand.random(6f), rot = rand.range(40f) + e.rotation;

            e.scaled(e.lifetime * rand.random(0.3f, 1f), b -> {
                v.trns(rot, len * b.finpow());
                Fill.circle(e.x + v.x, e.y + v.y, 2f * b.fslope() + 0.2f);
            });
        }
    }),

    neoplasiaSmoke = new Effect(280f, e -> {
        color(Pal.neoplasmMid);
        alpha(0.6f);

        rand.setSeed(e.id);
        for (int i = 0; i < 6; i++) {
            float len = rand.random(10f), rot = rand.range(120f) + e.rotation;

            e.scaled(e.lifetime * rand.random(0.3f, 1f), b -> {
                v.trns(rot, len * b.finpow());
                Fill.circle(e.x + v.x, e.y + v.y, 3.3f * b.fslope() + 0.2f);
            });
        }
    }),

    heatReactorSmoke = new Effect(180f, e -> {
        color(Color.gray);

        rand.setSeed(e.id);
        for (int i = 0; i < 5; i++) {
            float len = rand.random(6f), rot = rand.range(50f) + e.rotation;

            e.scaled(e.lifetime * rand.random(0.3f, 1f), b -> {
                alpha(0.9f * b.fout());
                v.trns(rot, len * b.finpow());
                Fill.circle(e.x + v.x, e.y + v.y, 2.4f * b.fin() + 0.6f);
            });
        }
    }),

    circleColorSpark = new Effect(21f, e -> {
        color(Color.white, e.color, e.fin());
        stroke(e.fout() * 1.1f + 0.5f);

        randLenVectors(e.id, 9, 27f * e.fin(), 9f, (x, y) -> {
            lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fslope() * 5f + 0.5f);
        });
    }),

    colorSpark = new Effect(21f, e -> {
        color(Color.white, e.color, e.fin());
        stroke(e.fout() * 1.1f + 0.5f);

        randLenVectors(e.id, 5, 27f * e.fin(), e.rotation, 9f, (x, y) -> {
            lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fslope() * 5f + 0.5f);
        });
    }),

    colorSparkBig = new Effect(25f, e -> {
        color(Color.white, e.color, e.fin());
        stroke(e.fout() * 1.3f + 0.7f);

        randLenVectors(e.id, 8, 41f * e.fin(), e.rotation, 10f, (x, y) -> {
            lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fslope() * 6f + 0.5f);
        });
    }),

    randLifeSpark = new Effect(24f, e -> {
        color(Color.white, e.color, e.fin());
        stroke(e.fout() * 1.5f + 0.5f);

        rand.setSeed(e.id);
        for (int i = 0; i < 15; i++) {
            float ang = e.rotation + rand.range(9f), len = rand.random(90f * e.finpow());
            e.scaled(e.lifetime * rand.random(0.5f, 1f), p -> {
                v.trns(ang, len);
                lineAngle(e.x + v.x, e.y + v.y, ang, p.fout() * 7f + 0.5f);
            });
        }
    }),

    shootPayloadDriver = new Effect(30f, e -> {
        color(Pal.accent);
        Lines.stroke(0.5f + 0.5f * e.fout());
        float spread = 9f;

        rand.setSeed(e.id);
        for (int i = 0; i < 20; i++) {
            float ang = e.rotation + rand.range(17f);
            v.trns(ang, rand.random(e.fin() * 55f));
            Lines.lineAngle(e.x + v.x + rand.range(spread), e.y + v.y + rand.range(spread), ang, e.fout() * 5f * rand.random(1f) + 1f);
        }
    }),

    shootSmallFlame = new Effect(32f, 80f, e -> {
        color(Pal.lightFlame, Pal.darkFlame, Color.gray, e.fin());

        randLenVectors(e.id, 8, e.finpow() * 60f, e.rotation, 10f, (x, y) -> {
            Fill.circle(e.x + x, e.y + y, 0.65f + e.fout() * 1.5f);
        });
    }),

    shootPyraFlame = new Effect(33f, 80f, e -> {
        color(Pal.lightPyraFlame, Pal.darkPyraFlame, Color.gray, e.fin());

        randLenVectors(e.id, 10, e.finpow() * 70f, e.rotation, 10f, (x, y) -> {
            Fill.circle(e.x + x, e.y + y, 0.65f + e.fout() * 1.6f);
        });
    }),

    shootLiquid = new Effect(15f, 80f, e -> {
        color(e.color);

        randLenVectors(e.id, 2, e.finpow() * 15f, e.rotation, 11f, (x, y) -> {
            Fill.circle(e.x + x, e.y + y, 0.5f + e.fout() * 2.5f);
        });
    }),

    casing1 = new Effect(30f, e -> {
        color(Pal.lightOrange, Color.lightGray, Pal.lightishGray, e.fin());
        alpha(e.fout(0.3f));
        float rot = Math.abs(e.rotation) + 90f;
        int i = -Mathf.sign(e.rotation);

        float len = (2f + e.finpow() * 6f) * i;
        float lr = rot + e.fin() * 30f * i;
        Fill.rect(
                e.x + trnsx(lr, len) + Mathf.randomSeedRange(e.id + i + 7, 3f * e.fin()),
                e.y + trnsy(lr, len) + Mathf.randomSeedRange(e.id + i + 8, 3f * e.fin()),
                1f, 2f, rot + e.fin() * 50f * i
        );

    }).layer(Layer.bullet),

    casing2 = new Effect(34f, e -> {
        color(Pal.lightOrange, Color.lightGray, Pal.lightishGray, e.fin());
        alpha(e.fout(0.5f));
        float rot = Math.abs(e.rotation) + 90f;
        int i = -Mathf.sign(e.rotation);
        float len = (2f + e.finpow() * 10f) * i;
        float lr = rot + e.fin() * 20f * i;
        rect(Core.atlas.find("casing"),
                e.x + trnsx(lr, len) + Mathf.randomSeedRange(e.id + i + 7, 3f * e.fin()),
                e.y + trnsy(lr, len) + Mathf.randomSeedRange(e.id + i + 8, 3f * e.fin()),
                2f, 3f, rot + e.fin() * 50f * i
        );
    }).layer(Layer.bullet),

    casing3 = new Effect(40f, e -> {
        color(Pal.lightOrange, Pal.lightishGray, Pal.lightishGray, e.fin());
        alpha(e.fout(0.5f));
        float rot = Math.abs(e.rotation) + 90f;
        int i = -Mathf.sign(e.rotation);
        float len = (4f + e.finpow() * 9f) * i;
        float lr = rot + Mathf.randomSeedRange(e.id + i + 6, 20f * e.fin()) * i;

        rect(Core.atlas.find("casing"),
                e.x + trnsx(lr, len) + Mathf.randomSeedRange(e.id + i + 7, 3f * e.fin()),
                e.y + trnsy(lr, len) + Mathf.randomSeedRange(e.id + i + 8, 3f * e.fin()),
                2.5f, 4f,
                rot + e.fin() * 50f * i
        );
    }).layer(Layer.bullet),

    casing4 = new Effect(45f, e -> {
        color(Pal.lightOrange, Pal.lightishGray, Pal.lightishGray, e.fin());
        alpha(e.fout(0.5f));
        float rot = Math.abs(e.rotation) + 90f;
        int i = -Mathf.sign(e.rotation);
        float len = (4f + e.finpow() * 9f) * i;
        float lr = rot + Mathf.randomSeedRange(e.id + i + 6, 20f * e.fin()) * i;

        rect(Core.atlas.find("casing"),
                e.x + trnsx(lr, len) + Mathf.randomSeedRange(e.id + i + 7, 3f * e.fin()),
                e.y + trnsy(lr, len) + Mathf.randomSeedRange(e.id + i + 8, 3f * e.fin()),
                3f, 6f,
                rot + e.fin() * 50f * i
        );
    }).layer(Layer.bullet),

    casing2Double = new Effect(34f, e -> {
        color(Pal.lightOrange, Color.lightGray, Pal.lightishGray, e.fin());
        alpha(e.fout(0.5f));
        float rot = Math.abs(e.rotation) + 90f;
        for (int i : Mathf.signs) {
            float len = (2f + e.finpow() * 10f) * i;
            float lr = rot + e.fin() * 20f * i;
            rect(Core.atlas.find("casing"),
                    e.x + trnsx(lr, len) + Mathf.randomSeedRange(e.id + i + 7, 3f * e.fin()),
                    e.y + trnsy(lr, len) + Mathf.randomSeedRange(e.id + i + 8, 3f * e.fin()),
                    2f, 3f, rot + e.fin() * 50f * i
            );
        }

    }).layer(Layer.bullet),

    casing3Double = new Effect(40f, e -> {
        color(Pal.lightOrange, Pal.lightishGray, Pal.lightishGray, e.fin());
        alpha(e.fout(0.5f));
        float rot = Math.abs(e.rotation) + 90f;

        for (int i : Mathf.signs) {
            float len = (4f + e.finpow() * 9f) * i;
            float lr = rot + Mathf.randomSeedRange(e.id + i + 6, 20f * e.fin()) * i;

            rect(Core.atlas.find("casing"),
                    e.x + trnsx(lr, len) + Mathf.randomSeedRange(e.id + i + 7, 3f * e.fin()),
                    e.y + trnsy(lr, len) + Mathf.randomSeedRange(e.id + i + 8, 3f * e.fin()),
                    2.5f, 4f,
                    rot + e.fin() * 50f * i
            );
        }

    }).layer(Layer.bullet),


    railTrail = new Effect(16f, e -> {
        color(Pal.orangeSpark);

        for (int i : Mathf.signs) {
            Drawf.tri(e.x, e.y, 10f * e.fout(), 24f, e.rotation + 90 + 90f * i);
        }

        Drawf.light(e.x, e.y, 60f * e.fout(), Pal.orangeSpark, 0.5f);
    }),

    railHit = new Effect(18f, 200f, e -> {
        color(Pal.orangeSpark);

        for (int i : Mathf.signs) {
            Drawf.tri(e.x, e.y, 10f * e.fout(), 60f, e.rotation + 140f * i);
        }
    }),

    lancerLaserShoot = new Effect(21f, e -> {
        color(Pal.lancerLaser);

        for (int i : Mathf.signs) {
            Drawf.tri(e.x, e.y, 4f * e.fout(), 29f, e.rotation + 90f * i);
        }
    }),

    lancerLaserShootSmoke = new Effect(26f, e -> {
        color(Color.white);
        float length = !(e.data instanceof Float) ? 70f : (Float) e.data;

        randLenVectors(e.id, 7, length, e.rotation, 0f, (x, y) -> {
            lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fout() * 9f);
        });
    }),

    lancerLaserCharge = new Effect(38f, e -> {
        color(Pal.lancerLaser);

        randLenVectors(e.id, 14, 1f + 20f * e.fout(), e.rotation, 120f, (x, y) -> {
            lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fslope() * 3f + 1f);
        });
    }),

    lancerLaserChargeBegin = new Effect(60f, e -> {
        float margin = 1f - Mathf.curve(e.fin(), 0.9f);
        float fin = Math.min(margin, e.fin());

        color(Pal.lancerLaser);
        Fill.circle(e.x, e.y, fin * 3f);

        color();
        Fill.circle(e.x, e.y, fin * 2f);
    }),

    lightningCharge = new Effect(38f, e -> {
        color(Pal.lancerLaser);

        randLenVectors(e.id, 2, 1f + 20f * e.fout(), e.rotation, 120f, (x, y) -> {
            Drawf.tri(e.x + x, e.y + y, e.fslope() * 3f + 1, e.fslope() * 3f + 1, Mathf.angle(x, y));
        });
    }),

    sparkShoot = new Effect(12f, e -> {
        color(Color.white, e.color, e.fin());
        stroke(e.fout() * 1.2f + 0.6f);

        randLenVectors(e.id, 7, 25f * e.finpow(), e.rotation, 3f, (x, y) -> {
            lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fslope() * 5f + 0.5f);
        });
    }),

    lightningShoot = new Effect(12f, e -> {
        color(Color.white, Pal.lancerLaser, e.fin());
        stroke(e.fout() * 1.2f + 0.5f);

        randLenVectors(e.id, 7, 25f * e.finpow(), e.rotation, 50f, (x, y) -> {
            lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fin() * 5f + 2f);
        });
    }),

    thoriumShoot = new Effect(12f, e -> {
        color(Color.white, Pal.thoriumPink, e.fin());
        stroke(e.fout() * 1.2f + 0.5f);

        randLenVectors(e.id, 7, 25f * e.finpow(), e.rotation, 50f, (x, y) -> {
            lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fin() * 5f + 2f);
        });
    }),

    redgeneratespark = new Effect(90, e -> {
        color(Pal.redSpark);
        alpha(e.fslope());

        rand.setSeed(e.id);
        for (int i = 0; i < 2; i++) {
            v.trns(rand.random(360f), rand.random(e.finpow() * 9f)).add(e.x, e.y);
            Fill.circle(v.x, v.y, rand.random(1.4f, 2.4f));
        }
    }).layer(Layer.bullet - 1f),

    turbinegenerate = new Effect(100, e -> {
        color(Pal.vent);
        alpha(e.fslope() * 0.8f);

        rand.setSeed(e.id);
        for (int i = 0; i < 3; i++) {
            v.trns(rand.random(360f), rand.random(e.finpow() * 14f)).add(e.x, e.y);
            Fill.circle(v.x, v.y, rand.random(1.4f, 3.4f));
        }
    }).layer(Layer.bullet - 1f),

    conveyorPoof = new Effect(35, e -> {
        color(Pal.plasticBurn, Color.gray, e.fin());
        randLenVectors(e.id, 4, 3f + e.fin() * 4f, (x, y) -> {
            Fill.circle(e.x + x, e.y + y, e.fout() * 1.11f);
        });
    }),

    artilleryTrailSmoke = new Effect(50, e -> {
        color(e.color);
        rand.setSeed(e.id);
        for (int i = 0; i < 13; i++) {
            float fin = e.fin() / rand.random(0.5f, 1f), fout = 1f - fin, angle = rand.random(360f), len = rand.random(0.5f, 1f);

            if (fin <= 1f) {
                Tmp.v1.trns(angle, fin * 24f * len);

                alpha((0.5f - Math.abs(fin - 0.5f)) * 2f);
                Fill.circle(e.x + Tmp.v1.x, e.y + Tmp.v1.y, 0.5f + fout * 4f);
            }
        }
    }),
// e.rotation,
    generate = new Effect(11, e -> {
        color(e.color);
        stroke(1f);
        Lines.spikes(e.x, e.y,e.rotation+e.fin() * 5, 2, 8);
   //Lines.spikes(e.x, e.y,e.rotation,2, 8, 2);
    }),

    mineWallSmall = new Effect(50, e -> {
        color(e.color, Color.darkGray, e.fin());
        randLenVectors(e.id, 2, e.fin() * 6f, (x, y) -> {
            Fill.circle(e.x + x, e.y + y, e.fout() + 0.5f);
        });
    }),

    mineSmall = new Effect(30, e -> {
        color(e.color, Color.lightGray, e.fin());
        randLenVectors(e.id, 3, e.fin() * 5f, (x, y) -> {
            Fill.square(e.x + x, e.y + y, e.fout() + 0.5f, 45);
        });
    }),

    mine = new Effect(20, e -> {
        color(e.color, Color.lightGray, e.fin());
        randLenVectors(e.id, 6, 3f + e.fin() * 6f, (x, y) -> {
            Fill.square(e.x + x, e.y + y, e.fout() * 2f, 45);
        });
    }),

    mineBig = new Effect(30, e -> {
        color(e.color, Color.lightGray, e.fin());
        randLenVectors(e.id, 6, 4f + e.fin() * 8f, (x, y) -> {
            Fill.square(e.x + x, e.y + y, e.fout() * 2f + 0.2f, 45);
        });
    }),

    mineHuge = new Effect(40, e -> {
        color(e.color, Color.lightGray, e.fin());
        randLenVectors(e.id, 8, 5f + e.fin() * 10f, (x, y) -> {
            Fill.square(e.x + x, e.y + y, e.fout() * 2f + 0.5f, 45);
        });
    }),

    mineImpact = new Effect(90, e -> {
        color(e.color, Color.lightGray, e.fin());
        randLenVectors(e.id, 12, 5f + e.finpow() * 22f, (x, y) -> {
            Fill.square(e.x + x, e.y + y, e.fout() * 2.5f + 0.5f, 45);
        });
    }),

    mineImpactWave = new Effect(50f, e -> {
        color(e.color);

        stroke(e.fout() * 1.5f);

        randLenVectors(e.id, 12, 4f + e.finpow() * e.rotation, (x, y) -> {
            lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fout() * 5 + 1f);
        });

        e.scaled(30f, b -> {
            Lines.stroke(5f * b.fout());
            Lines.circle(e.x, e.y, b.finpow() * 28f);
        });
    }),

    payloadReceive = new Effect(30, e -> {
        color(Color.white, Pal.accent, e.fin());
        randLenVectors(e.id, 12, 7f + e.fin() * 13f, (x, y) -> {
            Fill.square(e.x + x, e.y + y, e.fout() * 2.1f + 0.5f, 45);
        });
    }),

    teleportActivate = new Effect(50, e -> {
        color(e.color);

        e.scaled(8f, e2 -> {
            stroke(e2.fout() * 4f);
            Lines.circle(e2.x, e2.y, 4f + e2.fin() * 27f);
        });

        stroke(e.fout() * 2f);

        randLenVectors(e.id, 30, 4f + 40f * e.fin(), (x, y) -> {
            lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fin() * 4f + 1f);
        });
    }),

    teleport = new Effect(60, e -> {
        color(e.color);
        stroke(e.fin() * 2f);
        Lines.circle(e.x, e.y, 7f + e.fout() * 8f);

        randLenVectors(e.id, 20, 6f + 20f * e.fout(), (x, y) -> {
            lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fin() * 4f + 1f);
        });
    }),

    teleportOut = new Effect(20, e -> {
        color(e.color);
        stroke(e.fout() * 2f);
        Lines.circle(e.x, e.y, 7f + e.fin() * 8f);

        randLenVectors(e.id, 20, 4f + 20f * e.fin(), (x, y) -> {
            lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fslope() * 4f + 1f);
        });
    }),

    ripple = new Effect(30, e -> {
        e.lifetime = 30f * e.rotation;

        color(Tmp.c1.set(e.color).mul(1.5f));
        stroke(e.fout() * 1.4f);
        Lines.circle(e.x, e.y, (2f + e.fin() * 4f) * e.rotation);
    }).layer(Layer.debris),

    bubble = new Effect(20, e -> {
        color(Tmp.c1.set(e.color).shiftValue(0.1f));
        stroke(e.fout() + 0.2f);
        randLenVectors(e.id, 2, e.rotation * 0.9f, (x, y) -> {
            Lines.circle(e.x + x, e.y + y, 1f + e.fin() * 3f);
        });
    }),

    launch = new Effect(28, e -> {
        color(Pal.command);
        stroke(e.fout() * 2f);
        Lines.circle(e.x, e.y, 4f + e.finpow() * 120f);
    }),

    launchPod = new Effect(50, e -> {
        color(Pal.engine);

        e.scaled(25f, f -> {
            stroke(f.fout() * 2f);
            Lines.circle(e.x, e.y, 4f + f.finpow() * 30f);
        });

        stroke(e.fout() * 2f);

        randLenVectors(e.id, 24, e.finpow() * 50f, (x, y) -> {
            float ang = Mathf.angle(x, y);
            lineAngle(e.x + x, e.y + y, ang, e.fout() * 4 + 1f);
        });
    }),

    healWaveMend = new Effect(40, e -> {
        color(e.color);
        stroke(e.fout() * 2f);
        Lines.circle(e.x, e.y, e.finpow() * e.rotation);
    }),

    overdriveWave = new Effect(50, e -> {
        color(e.color);
        stroke(e.fout());
        Lines.circle(e.x, e.y, e.finpow() * e.rotation);
    }),

    healBlock = new Effect(20, e -> {
        color(Pal.heal);
        stroke(2f * e.fout() + 0.5f);
        Lines.square(e.x, e.y, 1f + (e.fin() * e.rotation * tilesize / 2f - 1f));
    }),

    rotateBlock = new Effect(30, e -> {
        color(Pal.accent);
        alpha(e.fout() * 1);
        Fill.square(e.x, e.y, e.rotation * tilesize / 2f);
    }),

    lightBlock = new Effect(60, e -> {
        color(e.color);
        alpha(e.fout() * 1);
        Fill.square(e.x, e.y, e.rotation * tilesize / 2f);
    }),

    overdriveBlockFull = new Effect(60, e -> {
        color(e.color);
        alpha(e.fslope() * 0.4f);
        Fill.square(e.x, e.y, e.rotation * tilesize);
    }),

    shieldBreak = new Effect(40, e -> {
        color(e.color);
        stroke(3f * e.fout());
        if (e.data instanceof Unit u) {
            var ab = (ForceFieldAbility) Structs.find(u.abilities, a -> a instanceof ForceFieldAbility);
            if (ab != null) {
                Lines.poly(e.x, e.y, ab.sides, e.rotation + e.fin(), ab.rotation);
                return;
            }
        }

        Lines.poly(e.x, e.y, 6, e.rotation + e.fin());
    }).followParent(true),

    coreLandDust = new Effect(100f, e -> {
        color(e.color, e.fout(0.1f));
        rand.setSeed(e.id);
        Tmp.v1.trns(e.rotation, e.finpow() * 90f * rand.random(0.2f, 1f));
        Fill.circle(e.x + Tmp.v1.x, e.y + Tmp.v1.y, 8f * rand.random(0.6f, 1f) * e.fout(0.2f));
    }).layer(Layer.groundUnit + 1f),


    arcMarker = new Effect(1800, e -> {
        color(Pal.command);
        stroke(2f);
        Lines.circle(e.x, e.y, 1f * tilesize);
        stroke(e.fout() * 1.5f + 0.5f);
        Lines.circle(e.x, e.y, 8f + e.finpow() * 92f);
        //Drawf.arrow(player.x, player.y, e.x, e.y, 5f * tilesize, 4f, Pal.command);
    }),

    arcGatherMarker = new Effect(1800, e -> {
        color(Color.cyan, 0.8f);
        stroke(2f);
        Lines.circle(e.x, e.y, 4f * tilesize);
        stroke(1f);
        Lines.circle(e.x, e.y, 4f * tilesize * (e.finpow() * 8f - (int) (e.finpow() * 8f)));
        for (int j = 0; j < 4; j++) {
            if (e.fout() * 3 < j) {
                for (int i = 0; i < 8; i++) {
                    float rot = i * 45f;
                    float radius = 4f * tilesize + j * 6f + 4f;
                    simpleArrow(e.x + Angles.trnsx(rot, radius), e.y + Angles.trnsy(rot, radius), e.x, e.y, 4f, 2f, Color.cyan, Math.min(1f, j - e.fout() * 3));
                }
            }
        }
    }),

    arcAttackMarker = new Effect(1800, e -> {
        Color attackMark = Color.valueOf("#DC143C");
        color(attackMark);
        stroke(2f);
        Lines.circle(e.x, e.y, 1f * tilesize);
        float radius = 20f + e.finpow() * 80f;
        Lines.circle(e.x, e.y, radius);
        for (int i = 0; i < 4; i++) {
            float rot = i * 90f + 45f + (-Time.time) % 360f;
            simpleArrow(e.x + Angles.trnsx(rot, radius), e.y + +Angles.trnsy(rot, radius), e.x, e.y, 6f + 4 * e.finpow(), 2f + 4 * e.finpow(), attackMark);
        }
    }),

    arcDefenseMarker = new Effect(1800, e -> {
        color(Pal.heal);
        if (e.fin() < 0.2f) {
            Lines.circle(e.x, e.y, 20f + e.fin() * 400f);
            return;
        }
        Lines.circle(e.x, e.y, 101f);
        Lines.circle(e.x, e.y, 93f);
        for (int i = 0; i < 16; i++) {
            float rot = i * 22.5f;
            if ((e.fin() - 0.2f) * 50 > i)
                simpleArrow(e.x, e.y, e.x + Angles.trnsx(rot, 120f), e.y + +Angles.trnsy(rot, 120f), 96f, 4f, Pal.heal);
        }
    }),

    arcQuesMarker = new Effect(1200, e -> {
        color(Color.violet);
        stroke(2f);
        Draw.alpha(Math.min(e.fin() * 5, 1));
        Lines.arc(e.x, e.y + 25f, 10, 0.75f, 270);
        Lines.line(e.x, e.y + 15f, e.x, e.y + 7f);
        Lines.circle(e.x, e.y, 3f);
        Lines.circle(e.x, e.y + 18.5f, 27f);
    }),

    simpleCircle = new Effect(60f, e -> {
        color(e.color);
        Fill.circle(e.x, e.y, 2f * e.fout());
    }),

    simpleRect = new Effect(60f, e -> {
        color(e.color);
        Fill.square(e.x, e.y, 2f * e.fout(), 45);
    }),

    arcIndexer = new Effect(120f, e -> {
        color(getThemeColor());
        Lines.circle(e.x, e.y, 8f);
        for (int i = 0; i < 3; i++) {
            float rot = i * 120f + 90f;
            simpleArrow(e.x, e.y, e.x + Angles.trnsx(rot, 120f), e.y + Angles.trnsy(rot, 120f), 100f - 80f * e.fin(), -4f, getThemeColor());
        }
    }),
            D1 = new Effect(60, e -> {
                Draw.color(Color.valueOf("f9f9ca"));
                Lines.stroke(e.fout() + 0.2f);
                Angles.randLenVectors(e.id, 2, 1 + 20 * e.fout(), e.rotation, 120, (x, y) -> {
                    Lines.circle(e.x + x, e.y + y, 1 + e.fin() * 3);
                    Drawf.tri(e.x + x, e.y + y, e.fslope() * 3 + 1, e.fslope() * 3 + 1, Mathf.angle(x, y));
                });
            }),

    D2 = new Effect(120, e -> {//八卦阴阳鱼太极
        float rad = 16f;
        float rotation = 2 * e.fin() * 6.283f;

        Drawf.circles(e.x, e.y, 32, Color.white);
        Draw.z(90f);
        //大圆
        Draw.color(Color.white);
        Fill.arc(e.x, e.y, rad * 2, 0.5f, 180 + rotation * 360 / 6.283f);
        Draw.color(Color.black);
        Fill.arc(e.x, e.y, rad * 2, 0.5f, rotation * 360 / 6.283f);

        //中圆
        Draw.color(Color.black);
        Fill.circle((float) (e.x - rad * Math.cos(rotation)), (float) (e.y - rad * Math.sin(rotation)), rad);
        Draw.color(Color.white);
        Fill.circle((float) (e.x + rad * Math.cos(rotation)), (float) (e.y + rad * Math.sin(rotation)), rad);

        //小圆
        Draw.color(Color.black);
        Fill.circle((float) (e.x + rad * Math.cos(rotation)), (float) (e.y + rad * Math.sin(rotation)), rad / 3f);
        Draw.color(Color.white);
        Fill.circle((float) (e.x - rad * Math.cos(rotation)), (float) (e.y - rad * Math.sin(rotation)), rad / 3f);

    }),
            D3 = new Effect(90, 200f, e -> {
                randLenVectors(e.id, 10, e.finpow() * 90f, (x, y) -> {
                    float size = e.fout() * 14f;
                    color(Color.lime, Color.gray, e.fin());
                    Fill.circle(e.x + x, e.y + y, size / 2f);
                });
            }),

    D4 = new Effect(13f, 300f, e -> {
        color(Pal.lighterOrange, Color.lightGray, e.fin());
        stroke(e.fout() * 4f + 0.2f);
        Lines.circle(e.x, e.y, e.fin() * 200f);
    }),
D5=new Effect(300f, e -> {
    // 设置颜色为渐变色
    color(e.color, Color.lightGray, e.fin());

    // 设置线条宽度，随着效果结束逐渐变细
    stroke(e.fout() * 3f);

    // 绘制一个圆形，半径随时间变化
    float circleRad = 4f + e.finpow() * 80f; // 修改 50f 为 80f，增加圆形最大半径
    Lines.circle(e.x, e.y, circleRad);

    // 计算旋转角度和大小
    float rotationSpeed = 2f; // 每秒旋转的角度（360度为一圈）
    float rotation = e.time * rotationSpeed; // 基于时间计算旋转角度
    float sizeScale = e.finpow(); // 大小变化因子（从小变大）

    // 绘制旋转的四角星（外层大三角形）
    color(e.color);
    for (int i = 0; i < 4; i++) {
        Drawf.tri(e.x, e.y, 8f * sizeScale, 120f * sizeScale * e.fout(), i * 90 + rotation); // 修改 80f 为 120f，增加外层四角星最大大小
    }

    // 绘制旋转的四角星（内层小三角形）
    color();
    for (int i = 0; i < 4; i++) {
        Drawf.tri(e.x, e.y, 4f * sizeScale, 60f * sizeScale * e.fout(), i * 90 + rotation); // 修改 40f 为 60f，增加内层四角星最大大小
    }

    // 生成随机粒子效果
    randLenVectors(e.id + 1, 12, 10f + 60f * e.finpow(), (x, y) -> { // 修改 40f 为 60f，增加粒子最大范围
        // 绘制从星形中心向外辐射的粒子
        float angle = Mathf.angle(x, y);
        float len = 1f + e.fout() * 5f; // 粒子长度逐渐变短
        lineAngle(e.x + x, e.y + y, angle, len);
    });

    // 添加光效
    Drawf.light(e.x, e.y, circleRad * 2f, Pal.lighterOrange, 0.8f * e.fout()); // 修改 1.6f 为 2f，增加光效最大范围
}),
    // 绘制六芒星
    D6=new Effect(300f, e -> {
        // 设置颜色为渐变色
        color(e.color, Color.lightGray, e.fin());

        // 设置线条宽度，随着效果结束逐渐变细
        stroke(e.fout() * 3f);

        // 绘制一个圆形，半径随时间变化
        float circleRad = 4f + e.finpow() * 50f;
        Lines.circle(e.x, e.y, circleRad);

        // 计算旋转角度和大小
        float rotationSpeed = 2f; // 每秒旋转的角度（360度为一圈）
        float rotation = e.time * rotationSpeed; // 基于时间计算旋转角度
        float sizeScale = e.finpow(); // 大小变化因子（从小变大）

        // 绘制六芒星（由两个三角形组成）
        color(e.color);

        // 绘制正三角形
        for (int i = 0; i < 3; i++) {
            Drawf.tri(e.x, e.y, 8f * sizeScale, 80f * sizeScale * e.fout(), i * 120 + rotation);
        }

        // 绘制倒三角形
        for (int i = 0; i < 3; i++) {
            Drawf.tri(e.x, e.y, 8f * sizeScale, 80f * sizeScale * e.fout(), i * 120 + 60 + rotation); // 60度偏移，形成倒三角形
        }

              // 生成空心圆效果
        randLenVectors(e.id + 1, 18, 10f + 40f * e.finpow(), (x, y) -> {
            // 绘制空心圆
            float radius = 3f + e.fout() * 1f; // 空心圆的半径，随时间变化
            Lines.circle(e.x + x, e.y + y, radius); // 在随机位置绘制空心圆
        });


        // 添加光效
        Drawf.light(e.x, e.y, circleRad * 1.6f, Pal.lighterOrange, 0.8f * e.fout());
    }),
    // 五角星
    D7=new Effect(300, e -> {
        // 五角星的旋转速度
        float rotationSpeed = 2f;
        // 五角星的颜色
        Color starColor = Color.valueOf("ffd700"); // 金色
        // 粒子颜色
        Color particleColor = Color.valueOf("ff6b6b"); // 红色
        // 五角星的大小
        float radius = 50f;

        // 计算旋转角度
        float rotation = e.fin() * 360f * rotationSpeed;

        // 设置绘制颜色
        Draw.color(starColor);
        Lines.stroke(3f);
        // 绘制五角星
        drawStar(e.x, e.y, 5, radius, rotation);
        // 添加颜色渐变效果
        Draw.color(starColor, particleColor, Mathf.absin(e.fin() * 10f, 1f, 1f));
        drawStar(e.x, e.y, 5, radius * 0.8f, -rotation);
        // 添加粒子特效
        for (int i = 0; i < 3; i++) {
            float angle = Mathf.random(360f);
            float len = Mathf.random(radius * 0.5f, radius * 1.5f);
            float px = e.x + Angles.trnsx(angle, len);
            float py = e.y + Angles.trnsy(angle, len);
            Fill.circle(px, py, 2f);
        }
        // 添加光效
        Drawf.light(
                e.x, e.y, // 光源位置
                radius * 2f, // 光源半径
                starColor.cpy().lerp(particleColor, Mathf.absin(e.fin() * 10f, 1f, 1f)), // 光源颜色（渐变）
                0.8f // 光源强度
        );
        // 重置颜色
        Draw.reset();
    }),
      D8=new Effect(300, e -> {
        // 线条宽度
        float lineWidth = 5f;
        // 线条长度
        float lineLength = 5f*e.rotation;
        // 旋转速度（度/秒）
        float rotationSpeed = 1f;
        // 线条颜色
        Color lineColor = Color.valueOf("ff6b6b"); // 红色

        // 计算旋转角度
        float rotation = e.fin() * 360f * rotationSpeed;

        // 设置线条颜色
        Draw.color(e.color);

        // 绘制第一条斜线（40度）
        Draw.rect(
                Core.atlas.white(), // 使用白色纹理
                e.x, e.y, // 中心点
                lineLength, lineWidth, // 长度和宽度
                rotation + 45f // 旋转角度
        );

        // 绘制第二条斜线（-40度）
        Draw.rect(
                Core.atlas.white(), // 使用白色纹理
                e.x, e.y, // 中心点
                lineLength, lineWidth, // 长度和宽度
                rotation - 45f // 旋转角度
        );
          // 圆圈颜色
          Color circleColor = Color.valueOf("00aaff"); // 蓝色
          // 圆圈半径
          float circleRadius = lineLength * 0.6f; // 圆圈半径略小于线条长度
          // 绘制外围圆圈
          Draw.color(e.color);
          Lines.stroke(1f); // 圆圈线条宽度
          Lines.circle(e.x, e.y, circleRadius); // 绘制圆圈
          // 添加光效
          Drawf.light(e.x, e.y, 1.6f, e.color, 0.8f * e.fout());
        // 重置颜色
        Draw.reset();
    }),

        D9=   new Effect(180, e -> {
        // 线条宽度
        float lineWidth = 2f;
        // 线条长度
        float lineLength = 10f*e.rotation;
        // 线条颜色
        Color lineColor =e.color;
        // 三角形颜色
        Color triangleColor = e.color;
        // 两组平行线的间距
        float groupSpacing = 29f;
        // 每组两条平行线的间距
        float lineSpacing = 5f;
        // 三角形高度
        float triangleHeight = 18f;
        // 三角形宽度,调整宽高度就可以设置三角形的大小以及角的角度
        float triangleWidth = 20f;

        // 根据线条长度自动计算三角形数量
        int triangleCount = (int) (lineLength / triangleHeight); // 三角形数量 = 线条长度 / 三角形高度

        // 设置线条颜色
        Draw.color(lineColor);
        Lines.stroke(lineWidth);

        // 绘制第一组平行线（左侧）
        float leftGroupX = e.x - groupSpacing / 2;
        Lines.line(leftGroupX - lineSpacing / 2, e.y - lineLength / 2, leftGroupX - lineSpacing / 2, e.y + lineLength / 2);
        Lines.line(leftGroupX + lineSpacing / 2, e.y - lineLength / 2, leftGroupX + lineSpacing / 2, e.y + lineLength / 2);

        // 绘制第二组平行线（右侧）
        float rightGroupX = e.x + groupSpacing / 2;
        Lines.line(rightGroupX - lineSpacing / 2, e.y - lineLength / 2, rightGroupX - lineSpacing / 2, e.y + lineLength / 2);
        Lines.line(rightGroupX + lineSpacing / 2, e.y - lineLength / 2, rightGroupX + lineSpacing / 2, e.y + lineLength / 2);

        // 设置三角形颜色
        Draw.color(triangleColor);

        // 计算三角形的显示进度
        float progress = e.fin(); // 0 到 1 的进度
        int visibleTriangles = 1+(int) (triangleCount * progress); // 根据进度计算显示的三角形数量

        // 绘制三角形
        for (int i = 0; i < visibleTriangles; i++) {
            float triangleY = e.y - lineLength / 2 + (i + 0.5f) * (lineLength / triangleCount); // 三角形中心 Y 坐标
            float triangleX = e.x; // 三角形中心 X 坐标

            // 绘制三角形
            Fill.tri(
                    triangleX - triangleWidth / 2, triangleY - triangleHeight / 2, // 左下角
                    triangleX + triangleWidth / 2, triangleY - triangleHeight / 2, // 右下角
                    triangleX, triangleY + triangleHeight / 2 // 顶点
            );
        }
        // 添加光效
        Drawf.light(e.x, e.y, 1.6f, e.color, 0.8f * e.fout());
        // 重置颜色
        Draw.reset();
    }),
    D10= new Effect(60, e -> {
        Draw.color(e.color);
        Lines.stroke(1.2f); // 圆圈线条宽度
        Lines.circle(e.x, e.y, 3+e.rotation); // 绘制圆圈
        // 添加光效
        Drawf.light(e.x, e.y, 1.6f, e.color, 0.8f * e.fout());
        // 重置颜色
        Draw.reset();
    }),
    D11= new Effect(30, e -> {

        // 气泡数量
        int bubbleCount = 10;
        // 气泡最小半径
        float minRadius = 2f*e.rotation;
        // 气泡最大半径
        float maxRadius = 6f*e.rotation;
        // 气泡漂浮速度范围
        float minSpeed = 0.5f;
        float maxSpeed = 2f;
        // 气泡漂浮范围
        float bubbleRange = 50f*e.rotation;

        // 设置气泡颜色
        Draw.color(e.color);

        // 绘制气泡
        for (int i = 0; i < bubbleCount; i++) {
            // 气泡的初始位置（随机偏移）
            float offsetX = Mathf.random(-bubbleRange, bubbleRange);
            float offsetY = Mathf.random(-bubbleRange, bubbleRange);

            // 气泡的半径（随机大小）
            float radius = Mathf.random(minRadius, maxRadius);

            // 气泡的随机移动方向
            float angle = Mathf.random(360f); // 随机角度
            float speed = Mathf.random(minSpeed, maxSpeed); // 随机速度

            // 气泡的漂浮效果
            float bubbleX = e.x + offsetX + Mathf.cosDeg(angle) * speed * e.fin() * 100f; // 水平移动
            float bubbleY = e.y + offsetY + Mathf.sinDeg(angle) * speed * e.fin() * 100f; // 垂直移动

            // 气泡的透明度（逐渐消失）
            float alpha = 1f - e.fin();
            Draw.alpha(alpha);

            // 绘制气泡
            Fill.circle(bubbleX, bubbleY, radius);

            // 绘制气泡高光（增加立体感）
            Draw.alpha(alpha * 0.5f);
            Fill.circle(bubbleX + radius / 3, bubbleY + radius / 3, radius / 3);
        }

        // 重置颜色和透明度
        Draw.reset();
    }),
                D12= new Effect(300, e -> {
        // 法阵颜色
        Color circleColor = e.color;
        // 法阵半径
        float circleRadius = 100f+e.rotation;
        // 符文数量
        int runeCount = 8;
        // 符文大小
        float runeSize = 20f;
        // 旋转速度（度/秒）
        float rotationSpeed = 1f;

        // 设置法阵颜色
        Draw.color(circleColor);

        // 绘制法阵光环
        Lines.stroke(3f);
        Lines.circle(e.x, e.y, circleRadius);

        // 设置符文颜色
        Draw.color(e.color);

        // 计算旋转角度
        float rotation = e.fin() * 360f * rotationSpeed;

        // 绘制符文
        for (int i = 0; i < runeCount; i++) {
            // 计算符文位置
            float angle = 360f / runeCount * i + rotation;
            float runeX = e.x + Mathf.cosDeg(angle) * circleRadius;
            float runeY = e.y + Mathf.sinDeg(angle) * circleRadius;

            // 绘制符文（使用三角形模拟符文）
            Fill.tri(
                    runeX - runeSize / 2, runeY - runeSize / 2, // 左下角
                    runeX + runeSize / 2, runeY - runeSize / 2, // 右下角
                    runeX, runeY + runeSize / 2 // 顶点
            );
        }
                    // 添加光效
                    Drawf.light(e.x, e.y, 1.6f, e.color, 0.8f * e.fout());
        // 重置颜色
        Draw.reset();
    }),

                TriangleEffect =  new Effect(300, e -> {
                    // 线条颜色
                    Color lineColor = Color.white; // 白色
                    // 三角形大小
                    float triangleSize = 100f;
                    // 圆圈半径
                    float outerCircleRadius = 15f;
                    float innerCircleRadius = 10f;
                    // 线条宽度
                    float outerLineWidth = 3f;
                    float innerLineWidth = 5f;

                    // 设置外发光颜色
                    Draw.color(e.color);
                    Lines.stroke(outerLineWidth + 2f); // 外发光线条宽度

                    // 绘制正三角形（向下移动半个高度）
                    drawTriangle(e.x, e.y + triangleSize * Mathf.sqrt(3) / 4, triangleSize, false);
                    // 绘制倒三角形（向上移动半个高度）
                    drawTriangle(e.x, e.y - triangleSize * Mathf.sqrt(3) / 4, triangleSize, true);

                    // 设置线条颜色
                    Draw.color(lineColor);
                    Lines.stroke(outerLineWidth);

                    // 绘制正三角形的圆圈
                    drawCircles(e.x, e.y + triangleSize * Mathf.sqrt(3) / 4, triangleSize, false, outerCircleRadius, innerCircleRadius, outerLineWidth, innerLineWidth);
                    // 绘制倒三角形的圆圈
                    drawCircles(e.x, e.y - triangleSize * Mathf.sqrt(3) / 4, triangleSize, true, outerCircleRadius, innerCircleRadius, outerLineWidth, innerLineWidth);

                    // 重置颜色
                    Draw.reset();
                }); // 在指定坐标 (x, y) 处播放效果


// 绘制三角形
private static void drawTriangle(float x, float y, float size, boolean inverted) {
    float height = size * Mathf.sqrt(3) / 2; // 三角形高度

    if (inverted) {
        // 倒三角形
        Lines.line(x - size / 2, y + height / 2, x + size / 2, y + height / 2); // 底边
        Lines.line(x - size / 2, y + height / 2, x, y - height / 2); // 左边
        Lines.line(x + size / 2, y + height / 2, x, y - height / 2); // 右边
    } else {
        // 正三角形
        Lines.line(x - size / 2, y - height / 2, x + size / 2, y - height / 2); // 底边
        Lines.line(x - size / 2, y - height / 2, x, y + height / 2); // 左边
        Lines.line(x + size / 2, y - height / 2, x, y + height / 2); // 右边
    }
}

// 绘制圆圈
private static void drawCircles(float x, float y, float size, boolean inverted, float outerRadius, float innerRadius, float outerWidth, float innerWidth) {
    float height = size * Mathf.sqrt(3) / 2; // 三角形高度

    // 计算顶点位置
    float[] vertices = new float[6];
    if (inverted) {
        // 倒三角形的顶点
        vertices[0] = x - size / 2; vertices[1] = y + height / 2; // 左下角
        vertices[2] = x + size / 2; vertices[3] = y + height / 2; // 右下角
        vertices[4] = x; vertices[5] = y - height / 2; // 顶点
    } else {
        // 正三角形的顶点
        vertices[0] = x - size / 2; vertices[1] = y - height / 2; // 左下角
        vertices[2] = x + size / 2; vertices[3] = y - height / 2; // 右下角
        vertices[4] = x; vertices[5] = y + height / 2; // 顶点
    }

    // 绘制每个顶点的圆圈
    for (int i = 0; i < 3; i++) {
        float vertexX = vertices[i * 2];
        float vertexY = vertices[i * 2 + 1];

        // 绘制外圆
        Lines.stroke(outerWidth);
        Lines.circle(vertexX, vertexY, outerRadius);

        // 绘制内圆
        Lines.stroke(innerWidth);
        Lines.circle(vertexX, vertexY, innerRadius);
    }
}



    // 绘制五角星的辅助方法
    private static void drawStar(float x, float y, int sides, float radius, float rotation) {
        float angleStep = 360f / sides;
        for (int i = 0; i < sides; i++) {
            float angle1 = angleStep * i + rotation;
            float angle2 = angleStep * (i + 2) + rotation; // 跳过中间的点以形成五角星
            float x1 = x + Angles.trnsx(angle1, radius);
            float y1 = y + Angles.trnsy(angle1, radius);
            float x2 = x + Angles.trnsx(angle2, radius);
            float y2 = y + Angles.trnsy(angle2, radius);
            Lines.line(x1, y1, x2, y2);
        }
    }
    public static Color getThemeColor() {
        try {
            return Color.valueOf(settings.getString("themeColor"));
        } catch (Exception e) {
            return Color.valueOf("ffd37f");
        }
    }

    public static void simpleArrow(float x, float y, float x2, float y2, float length, float radius, Color color) {
        simpleArrow(x, y, x2, y2, length, radius, color, 1f);
    }

    public static void simpleArrow(float x, float y, float x2, float y2, float length, float radius, Color color, float alpha) {
        float angle = Angles.angle(x, y, x2, y2);
        Tmp.v1.set(x2, y2).sub(x, y).limit(length);
        float vx = Tmp.v1.x + x, vy = Tmp.v1.y + y;

        Draw.color(color, alpha);
        Fill.poly(vx, vy, 3, radius, angle);
        Draw.color();
    }
}
