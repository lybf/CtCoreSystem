package CtCoreSystem.CoreSystem.type.Ovulam5480.BulletType;

import arc.math.Mathf;
import arc.util.Tmp;
import mindustry.content.StatusEffects;
import mindustry.entities.Fires;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.gen.*;
import mindustry.type.StatusEffect;
//心灵控制子弹
//这个子弹的效果是, 当子弹击中敌人时,敌人会被冰冻, 并且有一定的概率将敌人的队伍改为自己的队伍
//给狗子用的
public class XinLingKongZhiZhiDan extends BasicBulletType {
    public float chanceDeflect = 0;
    public StatusEffect unitStatusEffect = StatusEffects.wet;//冰冻效果
    public float statusEffectTime = Float.POSITIVE_INFINITY;//持续时间

    public XinLingKongZhiZhiDan(float speed, float damage, String bulletSprite) {
        super(speed, damage, bulletSprite);
    }

    public XinLingKongZhiZhiDan(float speed, float damage) {
        this(speed, damage, "bullet");
    }

    @Override
    public void hitTile(Bullet b, Building build, float x, float y, float initialHealth, boolean direct) {
        if (Mathf.randomBoolean(chanceDeflect * (1 - build.health / build.maxHealth))) {
            build.changeTeam(b.team);
        } else {
            if (makeFire && build.team != b.team) {
                Fires.create(build.tile);
            }

            if (build.team != b.team) {
                healEffect.at(build.x, build.y, 0f, healColor, build.block);
                build.heal(b.damage);
            }

            handlePierce(b, initialHealth, x, y);
        }
    }

    @Override
    public void hitEntity(Bullet b, Hitboxc entity, float health) {
        if (entity instanceof Unit unit) {
            if (Mathf.randomBoolean(chanceDeflect / (1 - unit.health / unit.maxHealth))) {
                unit.team = b.team;
                unit.apply(unitStatusEffect, statusEffectTime);
            } else {

                Healthc h = (Healthc) entity;
                h.heal(b.damage);

                Tmp.v3.set(unit).sub(b).nor().scl(knockback * 80f);
                if (impact) Tmp.v3.setAngle(b.rotation() + (knockback < 0 ? 180f : 0f));
                unit.impulse(Tmp.v3);
                unit.apply(status, statusDuration);

                handlePierce(b, health, entity.x(), entity.y());

            }
        }
    }
}
