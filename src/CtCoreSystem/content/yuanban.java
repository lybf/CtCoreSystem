package CtCoreSystem.content;

import CtCoreSystem.CoreSystem.type.yuan;
import CtCoreSystem.content.Effect.NewFx;
import mindustry.content.Blocks;
import mindustry.content.UnitTypes;
import mindustry.entities.abilities.MoveEffectAbility;

import static CtCoreSystem.CoreSystem.type.CTColor.C;
import static CtCoreSystem.content.Effect.NewFx.*;


public class yuanban {
    public static void load() {

        UnitTypes.gamma.coreUnitDock = true;//附身脱离时不会再核心重生
        UnitTypes.alpha.coreUnitDock = true;
        UnitTypes.beta.coreUnitDock = true;


        UnitTypes.gamma.abilities.add(new yuan(30, 120, C("97faff")));
        UnitTypes.beta.abilities.add(new yuan(20, 120, C("97faff")));
        UnitTypes.alpha.abilities.add(new yuan(10, 120, C("97faff")));
        UnitTypes.gamma.abilities.add(new MoveEffectAbility(0f, -7f, C("89f08e"), 拖尾, 4f) {{
            minVelocity = 0.4f;
            rotateEffect = true;
            effectParam = 2;
            rotation = 180;
            teamColor = true;
        }});
        UnitTypes.beta.abilities.add(new MoveEffectAbility(0f, -7f, C("89f08e"), 拖尾圈, 4f) {{
            minVelocity = 1f;//效果的间隔时间
            rotateEffect = true;
            effectParam = 2;
            rotation = 180;
            teamColor = true;
        }});
    }
}
