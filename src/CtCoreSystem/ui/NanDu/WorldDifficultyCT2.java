package CtCoreSystem.ui.NanDu;

import arc.Core;
import arc.Events;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.type.StatusEffect;

public class WorldDifficultyCT2 {
   // public DifficultyConfigure 配置项 = new DifficultyConfigure();
    public static StatusEffect 作弊系统;
    public static void set() {
        if(作弊系统 == null){
            作弊系统 = new StatusEffect("difficulty"){{
                show = true;
                localizedName = "难度模式";
                fullIcon = Core.atlas.find("ctcoresystem-difficulty");
                uiIcon = Core.atlas.find("ctcoresystem-difficulty");
            }
                @Override
                public void loadIcon(){
                    fullIcon = Core.atlas.find("ctcoresystem-difficulty");
                    uiIcon = Core.atlas.find("ctcoresystem-difficulty");
                }
            };
        }

        Events.run(EventType.Trigger.update, () -> {
            if(!Vars.ui.editor.isShown() && !Vars.state.rules.editor && !Vars.state.isMenu()) {
                var MapName = Vars.state.map.name();
                保存默认难度(MapName);
                设置难度(MapName);

                Groups.unit.each(WorldDifficultyCT2::UnitDraw);
            }
        });
    }

    public static void 保存默认难度(String MapName){
        var keyA = Vars.state.rules.waveTimer;
        if(keyA) {
            var key = Vars.state.rules.waveSpacing;
            setWorldMap(MapName, "波次", key);
        }

        var keyB = Vars.state.rules.waveTeam;

        var key1 = Vars.state.rules.teams.get(keyB).unitDamageMultiplier;
        //var key2 = Vars.state.rules.teams.get(keyB).blockHealthMultiplier;
        //var key3 = Vars.state.rules.teams.get(keyB).blockDamageMultiplier;

        setWorldMap(MapName, "地图敌人单位攻击", key1);
        //setWorldMap(MapName, "地图敌人建筑血量", key2);
        //setWorldMap(MapName, "地图敌人建筑攻击", key3);
    }

    public static void 设置难度(String MapName){
        var keyA = Vars.state.rules.waveTimer;
        if(keyA) {
            var key = getWorldMap(MapName, "波次");
            Vars.state.rules.waveSpacing = key * 时间倍率(Core.settings.getInt("游戏难度"));
        }

        var keyB = Vars.state.rules.waveTeam;

        var key1 = getWorldMap(MapName, "地图敌人单位攻击");
        //var key2 = getWorldMap(MapName, "地图敌人建筑血量");
        //var key3 = getWorldMap(MapName, "地图敌人建筑攻击");

        Vars.state.rules.teams.get(keyB).unitDamageMultiplier = key1 * 单位攻击倍率(Core.settings.getInt("游戏难度"));
        //Vars.state.rules.teams.get(keyB).blockHealthMultiplier = key2 * 配置项.建筑血量倍率(Core.settings.getInt("地图敌人建筑血量"));
        //Vars.state.rules.teams.get(keyB).blockDamageMultiplier = key3 * 配置项.建筑攻击倍率(Core.settings.getInt("地图敌人建筑攻击"));
    }

    public static void UnitDraw(Unit unit){
        if(unit.team == Vars.state.rules.waveTeam) {
            作弊系统.healthMultiplier = 单位血量倍率(Core.settings.getInt("游戏难度"));
            作弊系统.reloadMultiplier = 单位射速倍率(Core.settings.getInt("游戏难度"));
            作弊系统.speedMultiplier = 单位移动倍率(Core.settings.getInt("游戏难度"));
            unit.apply(作弊系统, 100f);
        }

    }

    public static float setWorldMap(String MapName, String State, float key){
        if(Vars.state.rules.tags.get(MapName + "-" + State + "-Difficulty") == null) {
            Vars.state.rules.tags.put(MapName + "-" + State + "-Difficulty", String.valueOf(key));
        }
        return key;
    }
    public static float getWorldMap(String MapName, String State){
        if(Vars.state.rules.tags.get(MapName + "-" + State + "-Difficulty") == null){
            setWorldMap(MapName, State, 0);
        }
        return Float.parseFloat(Vars.state.rules.tags.get(MapName + "-" + State + "-Difficulty"));
    }

    public static float 时间倍率(int 值){
        switch (值){
            case 1 : return 1f;
            case 2 : return 2;
            case 3 : return 1.5f;
            case 4 : return 1;
            case 5 : return 0.8f;
            case 6 : return 1.5f;
        }
        return 1f;
    }

    public static float 单位血量倍率(int 值){
        switch (值){
            case 1  : return 0.01f;
            case 2  : return 0.3f;
            case 3  : return 0.7f;
            case 4  : return 1;
            case 5  : return 1.8f;
            case 6  : return 2.5f;
        }
        return 1f;
    }

    public static float 单位攻击倍率(int 值){
        switch (值){
            case 1 : return 0.0001f;
            case 2 : return 0.5f;
            case 3 : return 0.75f;
            case 4 : return 1;
            case 5 : return 1.15f;
            case 6 : return 1.5f;
        }
        return 1f;
    }

    public static float 单位射速倍率(int 值){
        switch (值){
            case 1  : return 0f;
            case 2  : return 0.6f;
            case 3  : return 0.8f;
            case 4  : return 1;
            case 5  : return 1.5f;
            case 6  : return 1.8f;
        }
        return 1f;
    }

    public static float 单位移动倍率(int 值){
        switch (值){
            case 1  : return 1f;
            case 2  : return 0.8f;
            case 3  : return 0.9f;
            case 4  : return 1f;
            case 5  : return 1f;
            case 6  : return 0.8f;
        }
        return 1f;
    }
}