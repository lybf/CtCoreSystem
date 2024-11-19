package CtCoreSystem.CoreSystem.type.TDTyep;

import arc.Core;
import arc.Events;
import arc.math.Mathf;
import arc.struct.Seq;
import arc.util.Time;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.logic.LAccess;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.type.StatusEffect;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.meta.BuildVisibility;

import static CtCoreSystem.content.ItemX.物品;
import static arc.Core.settings;
import static mindustry.Vars.state;
import static mindustry.type.ItemStack.with;

public class TDBuffChange {
    public static StatusEffect Buff = new StatusEffect("unitBuff") {
        {

            localizedName = "unitBuff";
            fullIcon = Core.atlas.find("ct-unitBuff");
            uiIcon = Core.atlas.find("ct-unitBuff");
            show = false;
        }
        @Override
        public void loadIcon() {
            fullIcon = Core.atlas.find("ct-unitBuff");
            uiIcon = Core.atlas.find("ct-unitBuff");
        }
    };

    public static class BuffSpee extends Block {//速度更改
        public BuffSpee(String name) {
            super(name);
            update = true;
            sync = true;
            canOverdrive = false;
            targetable = false;
            forceDark = true;
            privileged = true;
            size = 1;
            requirements(Category.effect, BuildVisibility.sandboxOnly, with(物品, 1));
        }
        @Override
        public boolean canBreak(Tile tile) {
            return Vars.state.rules.infiniteResources||!privileged || state.rules.editor || state.playtestingMap != null;
        }

        public class BuffSpeeBuild extends Building {
            float speedMulti = 1f;
            @Override
            public void control(LAccess type, double p1, double p2, double p3, double p4) {
                if (type == LAccess.config) speedMulti = (float) p1;
                super.control(type, p1, p2, p3, p4);
            }
            @Override
            public void updateTile() {
                Buff.speedMultiplier = speedMulti;
                Vars.state.teams.present.select(teamData -> teamData.team != Vars.state.rules.defaultTeam).each(teamData -> teamData.units.each(unit -> unit.apply(Buff, 60)));
            }
            /*
            @Override
            public void control(LAccess type, double p1, double p2, double p3, double p4) {
                if (type == LAccess.config) Buff.speedMultiplier = (float) p1;
                super.control(type, p1, p2, p3, p4);
            }

            @Override
            public void updateTile() {
                Vars.state.teams.present.select(teamData -> teamData.team != Vars.state.rules.defaultTeam).each(teamData -> teamData.units.each(unit -> unit.apply(Buff, 60)));
            }*/
        }
    }
    public static class BuffHealth extends Block {//生命更改
        public BuffHealth(String name) {
            super(name);
            update = true;
            sync = true;
            canOverdrive = false;
            targetable = false;
            forceDark = true;
            privileged = true;
            size = 1;
            requirements(Category.effect, BuildVisibility.sandboxOnly, with(物品, 1));
        }
        @Override
        public boolean canBreak(Tile tile) {
            return Vars.state.rules.infiniteResources||!privileged || state.rules.editor || state.playtestingMap != null;
        }
        public class BuffHealthBuild extends Building {
            @Override
            public void control(LAccess type, double p1, double p2, double p3, double p4) {
                if (type == LAccess.config) Buff.healthMultiplier = (float) p1;
                super.control(type, p1, p2, p3, p4);
            }
            @Override
            public void updateTile() {
                Vars.state.teams.present.select(teamData -> teamData.team != Vars.state.rules.defaultTeam).each(teamData -> teamData.units.each(unit -> unit.apply(Buff, 60)));
            }
        }
    }
    public static class BuffDmage extends Block {//伤害更改
        public BuffDmage(String name) {
            super(name);
            update = true;
            sync = true;
            canOverdrive = false;
            targetable = false;
            forceDark = true;
            privileged = true;
            size = 1;
            requirements(Category.effect, BuildVisibility.sandboxOnly, with(物品, 1));
        }
        @Override
        public boolean canBreak(Tile tile) {
            return Vars.state.rules.infiniteResources||!privileged || state.rules.editor || state.playtestingMap != null;
        }
        public class BuffDmageBuild extends Building {
            @Override
            public void control(LAccess type, double p1, double p2, double p3, double p4) {
                if (type == LAccess.config) Buff.damageMultiplier = (float) p1;
                super.control(type, p1, p2, p3, p4);
            }
            @Override
            public void updateTile() {
                Vars.state.teams.present.select(teamData -> teamData.team != Vars.state.rules.defaultTeam).each(teamData -> teamData.units.each(unit -> unit.apply(Buff, 60)));
            }
        }
    }
    public static class Buff加盾 extends Block {//直接修改盾，不使用BUFF
        public Buff加盾(String name) {
            super(name);
            update = true;
            sync = true;
            canOverdrive = false;
            targetable = false;
            forceDark = true;
            privileged = true;
            size = 1;
            requirements(Category.effect, BuildVisibility.sandboxOnly, with(物品, 1));
        }
        @Override
        public boolean canBreak(Tile tile) {
            return Vars.state.rules.infiniteResources||!privileged || state.rules.editor || state.playtestingMap != null;
        }
        public class 加盾Build extends Building {
            public Seq<Unit> units = new Seq<>();
            public Seq<Unit> removes = new Seq<>();
            public float 盾量 = 0;
            @Override
            public void control(LAccess type, double p1, double p2, double p3, double p4) {
                if (type == LAccess.config) 盾量 = (float) p1;
                super.control(type, p1, p2, p3, p4);
            }
            @Override
            public void updateTile() {
                Vars.state.teams.present.select(teamData -> teamData.team != Vars.state.rules.defaultTeam).each(teamData ->
                        teamData.units.select(unit -> !units.contains(unit)).each(unit -> {
                            units.add(unit);
                            unit.shield(盾量 - unit.shield);
                        }));
                removes.add(units.select(unit -> unit.dead));
                units.removeAll(removes);
                removes.clear();
            }
        }
            }
    //隐身方块 逻辑控制 0关闭隐身 1开启隐身
    public static class UnitCloaking extends Block {
        public UnitCloaking(String name) {
            super(name);
            update = true;
            sync = true;
            canOverdrive = false;
            targetable = false;
            forceDark = true;
            privileged = true;
            size = 1;
            requirements(Category.effect, BuildVisibility.sandboxOnly, with(物品, 1));
        }
        @Override
        public boolean canBreak(Tile tile) {
            return Vars.state.rules.infiniteResources||!privileged || state.rules.editor || state.playtestingMap != null;
        }
        public class UnitCloakingBuild extends Building {
            boolean 开启隐身;
            @Override
            public void control(LAccess type, double p1, double p2, double p3, double p4) {
                if (type == LAccess.config){
                    if(p1 == 1)开启隐身 = true;
                    else if(p1 == 0 && 开启隐身){
                        Vars.state.teams.get(Team.crux).units.each(unit -> Groups.draw.add(unit));
                        开启隐身 = false;
                    }
                }
                super.control(type, p1, p2, p3, p4);
            }
            @Override
            public void updateTile() {
                if (开启隐身){
                    Vars.state.teams.get(Team.crux).units.each(unit -> {Groups.draw.remove(unit);});
                }
            }
        }
    }
    //波次等待方块 逻辑控制 0关闭 1开启
    public static class WaitEnemies extends Block {
        public WaitEnemies(String name) {
            super(name);
            update = true;
            sync = true;
            canOverdrive = false;
            targetable = false;
            forceDark = true;
            privileged = true;
            size = 1;
            requirements(Category.effect, BuildVisibility.sandboxOnly, with(物品, 1));
        }
        @Override
        public boolean canBreak(Tile tile) {
            return Vars.state.rules.infiniteResources||!privileged || state.rules.editor || state.playtestingMap != null;
        }
        public class WaitEnemiesBuild extends Building {
                @Override
            public void control(LAccess type, double p1, double p2, double p3, double p4) {
                if (type == LAccess.config) {
                    if(p1 == 0){
                        Vars.state.rules.waitEnemies = false;
                    } else if (p1 == 1) {
                        Vars.state.rules.waitEnemies = true;
                    }
                }
                super.control(type, p1, p2, p3, p4);
            }
        }
    }
    //更改红方核心禁造圈 逻辑控制
    public static class 核心禁造圈 extends Block {
        public 核心禁造圈(String name) {
            super(name);
            update = true;
            sync = true;
            canOverdrive = false;
            targetable = false;
            forceDark = true;
            privileged = true;
            size = 1;
            requirements(Category.effect, BuildVisibility.sandboxOnly, with(物品, 1));
        }
        @Override
        public boolean canBreak(Tile tile) {
            return Vars.state.rules.infiniteResources||!privileged || state.rules.editor || state.playtestingMap != null;
        }
        public class 核心禁造圈Build extends Building {
            public float 范围 = -1;
            @Override
            public void control(LAccess type, double p1, double p2, double p3, double p4) {
                if (type == LAccess.config) 范围 = (float) p1;
                super.control(type, p1, p2, p3, p4);
            }
            @Override
            public void updateTile() {
                if (范围 < 0) return;
                state.rules.enemyCoreBuildRadius=范围*8;
            }
        }
    }
    //更改游戏速度方块 逻辑控制
    public static class 游戏速度 extends Block {
        public 游戏速度(String name) {
            super(name);
            update = true;
            sync = true;
            canOverdrive = false;
            targetable = false;
            forceDark = true;
            privileged = true;
            size = 1;
            requirements(Category.effect, BuildVisibility.sandboxOnly, with(物品, 1));
        }
        @Override
        public boolean canBreak(Tile tile) {
            return Vars.state.rules.infiniteResources||!privileged || state.rules.editor || state.playtestingMap != null;
        }
        public class 游戏速度Build extends Building {
        public float 速度 = 1;
        @Override
        public void control(LAccess type, double p1, double p2, double p3, double p4) {
            if (type == LAccess.config) 速度 = (float) p1;
            super.control(type, p1, p2, p3, p4);
        }
        @Override
        public void updateTile() {
            Time.setDeltaProvider(() -> Math.min(Core.graphics.getDeltaTime() * 60 * 速度, 3 * 速度));
        }
        }
    }
    //更改游戏速度方块 逻辑控制
    public static class 游戏缩放 extends Block {
        public 游戏缩放(String name) {
            super(name);
            update = true;
            sync = true;
            canOverdrive = false;
            targetable = false;
            forceDark = true;
            privileged = true;
            size = 1;
            requirements(Category.effect, BuildVisibility.sandboxOnly, with(物品, 1));
        }
        @Override
        public boolean canBreak(Tile tile) {
            return Vars.state.rules.infiniteResources||!privileged || state.rules.editor || state.playtestingMap != null;
        }
        public class 游戏缩放Build extends Building {
            public float 缩放 = -1;
            @Override
            public void control(LAccess type, double p1, double p2, double p3, double p4) {
                if (type == LAccess.config) 缩放 = (float) p1;
                super.control(type, p1, p2, p3, p4);
            }
            @Override
            public void updateTile() {
                if (缩放 <= 0) return;
                Vars.renderer.setScale(缩放);
            }
        }
    }
    //更改游戏环境光开关方块 逻辑控制 0关闭环境光 1开启环境光
    public static class 游戏环境光开关 extends Block {
        public 游戏环境光开关(String name) {
            super(name);
            update = true;
            sync = true;
            canOverdrive = false;
            targetable = false;
            forceDark = true;
            privileged = true;
            size = 1;
            requirements(Category.effect, BuildVisibility.sandboxOnly, with(物品, 1));
        }
        @Override
        public boolean canBreak(Tile tile) {
            return Vars.state.rules.infiniteResources||!privileged || state.rules.editor || state.playtestingMap != null;
        }
        public boolean 开关;
        public class 游戏环境光开关Build extends Building {
      @Override
            public void control(LAccess type, double p1, double p2, double p3, double p4) {
                if (type == LAccess.config){
                    if(p1 == 1)开关 = true;
                    else if(p1 == 0 && 开关){
                        开关 = false;
                    }
                }
            }
            @Override
            public void updateTile() {
                if (开关 == true){
                Vars.state.rules.lighting=true;
                Events.run(EventType.Trigger.update, () -> settings.put("drawlight", true));

                }else {
                    Vars.state.rules.lighting = false;
                    Events.run(EventType.Trigger.update, () -> settings.put("drawlight", settings.getBool("drawlight")));

                }
            }
        }
    }
}
