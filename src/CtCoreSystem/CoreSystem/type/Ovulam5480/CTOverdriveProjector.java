package CtCoreSystem.CoreSystem.type.Ovulam5480;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.util.Time;
import mindustry.graphics.Layer;
import mindustry.world.blocks.defense.OverdriveProjector;

//超速仪的范围圈动画
public class CTOverdriveProjector extends OverdriveProjector {
    public float 间隔 = 180f;
    public CTOverdriveProjector(String name) {
        super(name);
        chuxi=4f;//粗细
        颜色=baseColor;
    }
    public float chuxi;
    public Color 颜色;
    public class CTOverdriveProjectorBuild extends OverdriveBuild{
        @Override
        public void draw() {
            super.draw();

            float realRange = range + phaseHeat * phaseRangeBoost;
            float progress = (Time.time % 间隔) / 间隔;

            Lines.stroke((1 - progress) * chuxi, 颜色.a(efficiency));
            Draw.z(Layer.effect);

            Lines.circle(x,y,realRange * progress);

            Draw.reset();
            Lines.stroke(1);
            Lines.circle(x,y,realRange);
        }
    }
}


