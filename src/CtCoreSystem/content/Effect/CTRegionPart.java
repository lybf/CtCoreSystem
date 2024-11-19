package CtCoreSystem.content.Effect;

import arc.graphics.Blending;
import arc.graphics.Color;
import arc.graphics.g2d.TextureRegion;
import mindustry.entities.part.RegionPart;

public class CTRegionPart {

    public static  class 射击时显示 extends RegionPart {
        public float wScl;
        public float hScl;
        public float spin;
        public 射击时显示(String region,float 宽,float 高,float 转速 ,Color 开始颜色,Color 结束颜色) {
            suffix = region;
            spin = 转速;
            wScl = 宽;
            hScl = 高;
            progress = PartProgress.warmup;
            mirror = false;//镜像
            blending = Blending.additive;
            // under = true;//在主体下面
            moveRot = 0;//倾斜角度
            moveY = -0;//上下
            moveX = 0f;//左右
            y = 16;
            outline = false;
            layer = 115;
            color = 开始颜色;//Color.valueOf("f8ca4d00");//开始颜色，尾部不加00 贴图会一直存在
            colorTo = 结束颜色;//Color.valueOf("f8ca4d");//结束颜色

        }

        @Override
        public void draw(PartParams params) {
            rotation += progress.getClamp(params) * spin;
            super.draw(params);
        }

        @Override
        public void load(String name) {
            super.load(name);

            for (TextureRegion region : regions) {
                region.setWidth(wScl);
                region.setHeight(hScl);
            }
            for (TextureRegion region : outlines) {
                region.setWidth(wScl);
                region.setHeight(hScl);
            }
        }

    }

    ;

}