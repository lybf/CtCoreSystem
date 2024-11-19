package CtCoreSystem.ui;

import CtCoreSystem.CoreSystem.WorldDifficulty;
import CtCoreSystem.CoreSystem.WorldDifficultyCT2;
import CtCoreSystem.ui.dialogs.SettingDifficultyDialog;
import arc.Core;
import arc.util.Log;
import mindustry.Vars;
import mindustry.ui.dialogs.SettingsMenuDialog;

import static arc.Core.bundle;
import static mindustry.Vars.ui;

//添加标题页菜单
public class CTGalaxyAcknowledgments {
    public static void 标题页菜单() {

        if (加载CT2()) {
            new WorldDifficultyCT2().set();
        } else {
            new WorldDifficulty().init();
        }

        ui.settings.game.sliderPref("游戏难度", 3, 1, 加载CT2() ? 6 : 4, 1, i -> Core.bundle.get((加载CT2() ? "" : "CT3") + "Difficulty-" + i));

        Vars.ui.menufrag.addButton(Core.bundle.get("difficulty.game"), () -> new SettingDifficultyDialog(加载CT2()).show());
    }


    public static void LoadAcknowledgments() {
     /*       Acknowledgments.cont.pane(cont -> {
        //255, 211, 127
            cont.add("[#FFD37F]DFAFS").pad(4).labelAlign(Align.center).row();
            cont.image(Tex.whiteui, Pal.accent).growX().height(3).pad(4).row();
            cont.image(Core.atlas.find("galaxymod-avatar-1")).height(280).width(280);
            cont.add(Core.bundle.get("galaxymod.acknowledgments.a1.content")).pad(4).labelAlign(Align.center).row();

            cont.add("[#FFD37F]d39052").pad(4).labelAlign(Align.center).row();
            cont.image(Tex.whiteui, Pal.accent).growX().height(3).pad(4).row();
            cont.image(Core.atlas.find("galaxymod-avatar-2")).height(280).width(280);
            cont.add(Core.bundle.get("galaxymod.acknowledgments.a2.content")).pad(4).labelAlign(Align.center).row();
        }).width(Core.scene.getWidth())*/
        ;
        // Acknowledgments.buttons.button(Core.bundle.get("galaxymod.report.button.close"),Acknowledgments::hide).size(128,64);
    }

    public static boolean 加载CT2(){
        return Vars.mods.locateMod("creators") != null;
    }
}
