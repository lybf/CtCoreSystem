package CtCoreSystem;

import arc.Core;
import arc.graphics.Color;
import arc.util.Align;
import mindustry.Vars;
import mindustry.ui.dialogs.BaseDialog;
//开屏显示示例 并未在此游戏中使用本类
//本类是一个示例，你可以根据自己的需要修改使用
public class 开屏显示 {
    public static BaseDialog 显示加载;

    public static void show() {
        String QQ群 = "https://qm.qq.com/q/RIgCHB8EG4";
        String 网盘 = "这里是你的网盘分享链接，最好是分享文件夹，把MOD放进去";
        显示加载 = new BaseDialog("这你随便取个名字，自己进游戏看效果") {{
            cont.pane(t -> {
                t.add("这是你的mod介绍，你可以在这里写你的mod的介绍，也可以不写，自己看效果").left().wrap().width(620).maxWidth(620).pad(4).labelAlign(Align.left).center().row();
                t.image().color(Color.valueOf("69dcee")).fillX().height(3).pad(9).row();//分割线

                //下面是你的游戏宣传图或者模组的logo，你可以在这里放你的游戏宣传图
                t.image(Core.atlas.find("你的mod名字-图片文件名", Core.atlas.find("clear"))).height(290).width(587).pad(3).row();
            });
            addCloseListener();//按esc关闭
            buttons.button("@close", (this::hide)).size(100, 64);//关闭按钮

            /* buttons.button("网盘下载", (() -> {
                 if (!Core.app.openURI(网盘)) {
                     Vars.ui.showErrorMessage("@linkfail");
                     Core.app.setClipboardText(网盘);
                 }
             })).size(250, 50);*/
            buttons.button("模组反馈", (() -> {
                if (!Core.app.openURI(QQ群)) {
                    Vars.ui.showErrorMessage("@linkfail");
                    Core.app.setClipboardText(QQ群);
                }
            })).size(200, 50);
            buttons.button("致谢名单", (() -> {
                new BaseDialog("致谢名单") {{
                    cont.pane((table -> {
                        table.add("感谢致辞.....").left().growX().wrap().width(620).maxWidth(620).pad(4).labelAlign(Align.left).row();
                        table.image().color(Color.valueOf("69dcee")).fillX().height(3).pad(9).row();//分割线
                        table.add("感谢名单人员:\naaa\nbbb\nccc\nddd").left().growX().wrap().width(620).maxWidth(620).pad(4).labelAlign(Align.left);
                    })).row();
                    addCloseListener();//按esc关闭
                    defaults().size(210, 64);
                    buttons.button("@close", (this::hide)).size(100, 64);//关闭按钮
                }}.show();
            })).size(200, 64).row();
        }};
        显示加载.show();
    }
}
