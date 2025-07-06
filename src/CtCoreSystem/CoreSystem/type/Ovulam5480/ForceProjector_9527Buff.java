package CtCoreSystem.CoreSystem.type.Ovulam5480;

import arc.Core;
import arc.func.Cons;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Mathf;
import arc.math.geom.Intersector;
import arc.util.Time;
import mindustry.content.Fx;
import mindustry.content.StatusEffects;
import mindustry.game.Team;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.graphics.Layer;
import mindustry.type.StatusEffect;
import mindustry.world.blocks.defense.ForceProjector;
import mindustry.world.consumers.Consume;
import mindustry.world.consumers.ConsumeItems;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import mindustry.world.meta.StatValues;

import static java.lang.Float.POSITIVE_INFINITY;
import static mindustry.Vars.*;

//原版护盾仪改的BUFF仪
public class ForceProjector_9527Buff extends ForceProjector {
    public StatusEffect status;
    public int  statusTime;
    public Color color1;
    public Color color2;
    public boolean isCurrentTeam ;
    public ForceProjector_9527Buff(String name){
        super(name);
        solid = true;
        update = true;
        sync = true;
        hasItems = true;
        status= StatusEffects.none;
        statusTime=30;
        color1= Team.sharded.color;
        color2=Color.white;
        cooldownNormal = phaseShieldBoost = 0;
        shieldHealth=POSITIVE_INFINITY;
        buildType = ForceProjector_9527BuffBuild::new;
        isCurrentTeam = true;
    }


    public class ForceProjector_9527BuffBuild extends ForceBuild {



        @Override
        public void updateTile() {
            float phaseValid = this.block.findConsumer(cons -> cons instanceof ConsumeItems).efficiency(this);
            this.phaseHeat = Mathf.lerpDelta(this.phaseHeat, phaseValid, 0.1f);

            boolean isPhaseValid = phaseValid > 0;
            if (isPhaseValid && !this.broken && this.timer.get(timerUse, phaseUseTime) && this.efficiency > 0) {
                this.consume();
            }

            this.radscl = Mathf.lerpDelta(this.radscl, this.broken ? 0 : this.warmup, 0.05f);

            if (Mathf.chanceDelta(this.buildup / shieldHealth * 0.1)) {
                Fx.reactorsmoke.at(this.x + Mathf.range(tilesize / 2), this.y + Mathf.range(tilesize / 2));
            }

            this.warmup = Mathf.lerpDelta(this.warmup, this.efficiency, 0.1f);

            if (this.broken && this.buildup <= 0) {
                this.broken = false;
            }

            if (this.buildup >= shieldHealth + phaseShieldBoost && !this.broken) {
                this.broken = true;
                this.buildup = shieldHealth;
                Fx.shieldBreak.at(this.x, this.y, this.realRadius(), this.team.color);
            }

            if (this.hit > 0) {
                this.hit -= 1 / 5f * Time.delta;
            }

            var realRadius = this.realRadius();
            // 在 Building 类中访问 Block 类的 isCurrentTeam 变量
            if (((ForceProjector_9527Buff) this.block).isCurrentTeam) {
                // 执行相应逻辑
               // isCurrentTeam=true;时对敌人生效
                if (realRadius > 0 && !this.broken) {
                    Groups.unit.intersect(this.x - realRadius, this.y - realRadius, realRadius * 2, realRadius * 2, shieldConsumer(this));
                }
            }else {
                // isCurrentTeam=false;时对自己生效
                if (realRadius > 0 &&!this.broken) {
                    Groups.unit.intersect(this.x - realRadius, this.y - realRadius, realRadius * 2, realRadius * 2, shieldConsumerP(this));
                }
            }

        }

        @Override
        public void drawShield(){
            Color COLOR =  Color.valueOf("c97061");
            Color COLOR2 =  Color.valueOf("c97061");
            float x = this.x;
            float y = this.y;
            float hit = this.hit;
            if (!this.broken) {
                float radius = this.realRadius();
                Draw.z(Layer.shields);
                if (Core.settings.getBool("animatedshields")) {
                    Draw.color(COLOR, COLOR2, Mathf.clamp(hit));
                    Fill.poly(x, y, 6, radius);
                    Draw.color(this.team.color, COLOR2, Mathf.clamp(hit));
                    Lines.poly(x, y, 6, radius);
                } else {
                    Lines.stroke(1.5f);
                    Draw.color(COLOR, COLOR2, Mathf.clamp(hit));
                    Draw.alpha(0.09f + Mathf.clamp(0.08f * hit));
                    Fill.poly(x, y, 6, radius);
                    Draw.color(this.team.color, COLOR2, Mathf.clamp(hit));
                    Draw.alpha(1);
                    Lines.poly(x, y, 6, radius);
                    Draw.reset();
                }
            }
            Draw.reset();
        }
    }
    //敌人方
    private Cons<Unit> shieldConsumer(ForceProjector_9527BuffBuild paramEntity) {
        return trait -> {
            if (!trait.team().equals(paramEntity.team)
                    && Intersector.isInsideHexagon(paramEntity.x, paramEntity.y, paramEntity.realRadius() * 2, trait.x(), trait.y())) {
                trait.apply(status, statusTime);
            }
        };
    }
    //玩家方
    private Cons<Unit> shieldConsumerP(ForceProjector_9527BuffBuild paramEntity) {
        return trait -> {
            if (trait.team().equals(paramEntity.team)
                    && Intersector.isInsideHexagon(paramEntity.x, paramEntity.y, paramEntity.realRadius() * 2, trait.x(), trait.y())) {
                trait.apply(status, statusTime);
            }
        };
    }

    public void setStats() {
        boolean consItems = this.itemConsumer != null;
        super.setStats();
        if (consItems) {
            Consume var3 = this.itemConsumer;
            if (var3 instanceof ConsumeItems) {
                ConsumeItems coni = (ConsumeItems)var3;
                this.stats.remove(Stat.booster);
                stats.remove(Stat.booster);
                stats.remove( Stat.shieldHealth);
                stats.remove( Stat.cooldownTime);
                stats.remove( Stat.liquidCapacity);
                stats.add(Stat.range,this.radius/8);
                this.stats.add(Stat.booster, StatValues.itemBoosters("+{0} " + StatUnit.shieldHealth.localized(), this.stats.timePeriod, this.phaseShieldBoost, this.phaseRadiusBoost, coni.items));
            }
        }

    }
}