package CtCoreSystem.content;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.Angles;
import mindustry.entities.Effect;
import mindustry.game.Team;
import mindustry.type.Item;
import mindustry.type.StatusEffect;

public class ItemX {
    public static Item 物品;
    public static StatusEffect 超级Boss;

    public static void load() {
        物品 = new Item("物品", Color.valueOf("f1b7e3")) {{
            localizedName = Core.bundle.get("items.mull");
            description = Core.bundle.getOrNull("items.description.null");
            // radioactivity = 2f;
            // frames = 5;// >0时会有有动画
            // transitionFrames = 1;//每帧之间生成的过渡帧的数量
            //frameTime = 10f;//贴图变换之间的时间，默认5
/*            for(var i=6;i!=0;i--){
                addFrame(1,i*5f);
                addFrame(2,i*5f);
            }
            frameReloadTime=300;*/
        }};
     超级Boss=new StatusEffect("superBoss"){{
         color = Team.crux.color;
         permanent = true;//状态永久持续
         healthMultiplier = 2.8F;//血量倍率
         speedMultiplier = 0.9F;//移速
         damageMultiplier = 1.4F;//伤害倍率
         damage = -60; //负数为治疗
         reloadMultiplier = 1.25F;//射击速度
         effect=new Effect(20, e -> {
                 Angles.randLenVectors(e.id, 6, 4 + e.fin() * 50, (x, y) -> {
                 Draw.color(Color.valueOf("e8d123"), e.color, e.fin());
         Fill.square(e.x + x, e.y + y, 0.5F + e.fout() * 2, 450);
        });
         });
     }};
    }
}
