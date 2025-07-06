package CtCoreSystem.CoreSystem.type.TDTyep;

import mindustry.entities.bullet.BasicBulletType;
import mindustry.gen.Bullet;
import mindustry.gen.Healthc;
import mindustry.gen.Hitboxc;
//百分比伤害子弹
public class percentBulletType {

    //最大血量
    public static class maxHealthBulletType extends BasicBulletType {
        //百分比, 填写40表示造成40%的血量上限的伤害
        float percent;

        public maxHealthBulletType(float percent) {
            this.percent = percent;
        }

        @Override
        public void hitEntity(Bullet b, Hitboxc entity, float health) {
            if (entity instanceof Healthc h) {
                h.damagePierce(h.maxHealth() * percent / 100);
            }
            super.hitEntity(b, entity, health);
        }
    }

    //当前血量
    public static class healthBulletType extends BasicBulletType {
        //百分比, 填写40表示造成40%的血量上限的伤害
        float percent;

        public healthBulletType(float percent) {
            this.percent = percent;
        }

        @Override
        public void hitEntity(Bullet b, Hitboxc entity, float health) {
            if (entity instanceof Healthc h) {
                h.damagePierce(h.health() * percent / 100);
            }
            super.hitEntity(b, entity, health);
        }
    }


}
