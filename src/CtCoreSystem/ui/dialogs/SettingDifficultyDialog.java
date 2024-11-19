package CtCoreSystem.ui.dialogs;
/*
 *@Author:LYBF
 *@Date  :2023/12/24
 */
//难度
import arc.Core;
import arc.func.Floatc;
import arc.graphics.Color;
import arc.scene.Element;
import arc.scene.event.Touchable;
import arc.scene.ui.Dialog;
import arc.scene.ui.Label;
import arc.scene.ui.Slider;
import arc.scene.ui.layout.Table;
import arc.util.Align;
import arc.util.Log;
import mindustry.Vars;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.SettingsMenuDialog;

import java.util.Objects;

import static arc.Core.bundle;

public class SettingDifficultyDialog extends Dialog {

    private Table container;

    public SettingDifficultyDialog(boolean 加载CT2) {
        //标题名
        super(bundle.get("settings", "Settings"));
        container = new Table();
        container.row();
        addChangeDiffcutySlider(加载CT2);
        container.setWidth(Math.min(Core.graphics.getWidth() / 1.2f, 480f));
        container.setHeight(Math.min(Core.graphics.getHeight() / 1.2f, 900f));
        add(container);
        container.row();
        container.button("关闭", this::hide).width(100).padTop(20);

    }


    private void addChangeDiffcutySlider(boolean 加载CT2) {
        Table table = new Table();
        Slider slider = new Slider(1, 加载CT2 ? 6 : 4, 1, false);
        slider.setValue(Core.settings.getInt("游戏难度"));
        Label value = new Label("", Styles.outlineLabel);
        Table content = new Table();
        content.add("难度设置", Styles.outlineLabel).left().growX().wrap();
        content.add(value).padLeft(10f).right();
        content.margin(3f, 33f, 3f, 33f);
        content.touchable = Touchable.disabled;
        slider.changed(() -> {
            //滑动时触发
            int value1 = (int) slider.getValue();
            value.setText(bundle.get(((加载CT2 ? "" : "CT3") + "Difficulty-" + value1)));
            //保存难度
            Core.settings.put("游戏难度", value1);
        });
        slider.change();
        table.stack(slider, content).width(Math.min(Core.graphics.getWidth() / 1.2f, 460f)).center().padTop(4f).get();


        table.row();
        table.image(Core.atlas.find(加载CT2 ? "ctcoresystem-nandu2" : "ctcoresystem-nandu3")).height(185).width(445).pad(3);//难度图片公示
        table.row();
        table.image().color(Color.valueOf("69dcee")).fillX().height(3).pad(9);
        table.row();
        table.add(加载CT2 ?"[yellow]当前为CT2难度":"[yellow]当前为CT3难度").left().growX().wrap().width(200).maxWidth(200).pad(4).row();
        table.add(Core.bundle.get("TD难度调整说明")).center().growX().wrap().width(500).maxWidth(500).pad(4).labelAlign(Align.center).row();
        container.add(table);
    }
}
