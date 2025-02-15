package CtCoreSystem;


import CtCoreSystem.CoreSystem.DsShaders;
import CtCoreSystem.CoreSystem.type.CTResearchDialog;
import CtCoreSystem.CoreSystem.type.VXV.SpawnDraw;
import CtCoreSystem.content.Effect.CT3FxEffect;
import CtCoreSystem.content.Effect.NewFx;
import CtCoreSystem.content.ItemX;
import CtCoreSystem.content.SourceCodeModification_Sandbox;
import CtCoreSystem.content.yuanban;
import CtCoreSystem.ui.CTGalaxyAcknowledgments;
import CtCoreSystem.ui.UnemFragment;
import CtCoreSystem.ui.dialogs.CT3InfoDialog;
import CtCoreSystem.ui.dialogs.CT3PlanetDialog;
import CtCoreSystem.ui.dialogs.CT3function;
import arc.Core;
import arc.Events;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.scene.ui.ImageButton;
import arc.struct.ObjectSet;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Tex;
import mindustry.graphics.Layer;
import mindustry.mod.Mod;
import mindustry.mod.Mods;
import mindustry.type.Planet;
import mindustry.type.UnitType;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.ui.dialogs.ResearchDialog;
import mindustry.world.Block;
import mindustry.world.blocks.distribution.Sorter;
import mindustry.world.blocks.sandbox.ItemSource;
import mindustry.world.blocks.sandbox.LiquidSource;

import java.io.IOException;
import java.util.Objects;

import static CtCoreSystem.CoreSystem.type.VXV.TDpowerShowBlock.TDloadPowerShow;
import static CtCoreSystem.CoreSystem.type.VXV.powerShowBlock.loadPowerShow;
import static arc.Core.camera;
import static arc.Core.files;
import static mindustry.Vars.*;

public class CtCoreSystem extends Mod {
    public final static Seq<Runnable> BlackListRun = new Seq<>();
    public Seq<String> BaiMingDan = new Seq<>();

    {
      //  Vars.state.rules.alloweditworldprocessors=false; 禁止世处编辑
        //缩放
        Vars.renderer.minZoom = 0.2F;
        Vars.renderer.maxZoom = 32;
//蓝图大小
        Vars.maxSchematicSize = 128;
    }

    public static void overrideVersion() {
        for (int i = 0; i < Vars.mods.list().size; i++) {
            Mods.LoadedMod mod = Vars.mods.list().get(i);
            if (mod != null) {
                mod.meta.description = Core.bundle.get("mod.CT.version") + mod.meta.version + "\n\n" + mod.meta.description;
            }
        }
    }

    public static void setPlanet(Planet planet, String[] names) {
        planet.ruleSetter = r -> {
            // planet.hiddenItems.addAll(Items.serpuloItems);
            var B = new ObjectSet<Block>();
            for (var b : content.blocks()) {
                if (b.minfo.mod == null) {
                    B.add(b);
                    continue;
                }
                boolean yes = true;
                for (var name : names) {
                    if (Objects.equals(b.minfo.mod.meta.name, name) || Objects.equals(b.minfo.mod.name, name)) {
                        yes = false;
                        break;
                    }
                }
                if (yes) {
                    B.add(b);
                }
            }
            r.bannedBlocks.addAll(B);
            var U = new ObjectSet<UnitType>();
            for (var u : content.units()) {
                if (u.minfo.mod == null) {
                    U.add(u);
                    continue;
                }
                boolean yes = true;
                for (var name : names) {
                    if (Objects.equals(u.minfo.mod.meta.name, name) || Objects.equals(u.minfo.mod.name, name)) {
                        yes = false;
                        break;
                    }
                }
                if (yes) {
                    U.add(u);
                }
            }
            r.bannedUnits.addAll(U);
            r.showSpawns = true;//显示单位刷出点
        };
    }

    //首页主功能按钮的系统
    public static ImageButton CreatorsIcon(String IconName, ImageButton.ImageButtonStyle imageButtonStyle, BaseDialog dialog) {
        TextureRegion A = Core.atlas.find("ctcoresystem-" + IconName);

        ImageButton buttonA = new ImageButton(A, imageButtonStyle);
        buttonA.clicked(dialog::show);
        return buttonA;
    }

    public void loadContent() {
        //解压自动存档模组
        if (Vars.mods.locateMod("auto_saver") == null) {
            try {
                Vars.mods.importMod(Vars.mods.locateMod("ctcoresystem").root.child("mod")
                        .child("AutoSaver.jar"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        if((Vars.mods.locateMod("auto-saver")!=null) ) {
            Vars.mods.getMod("auto-saver").state = Mods.ModState.disabled;
        }

  /*
   //分类栏ui
   new CT3ClassificationUi();
        Scripts scripts = mods.getScripts();
        Scriptable scope = scripts.scope;
        try {
            Object obj = Context.javaToJS(new CT3ClassificationUi(), scope);
            ScriptableObject.putProperty(scope, "CT3ClassificationUi", obj);
        } catch (Exception var5) {
            ui.showException(var5);
        }
        */
        //加载资源
        new CT3FxEffect();
        NewFx.load();
        NewFx.init();
        ItemX.load();
        yuanban.load();
        DsShaders.load();//电力节点力场的动画效果
        SourceCodeModification_Sandbox.load();
        CreatorsModJS.DawnMods();//JS加载器
    }

    public void init() {
        //显示怪物路径
        SpawnDraw.init();
        if (Vars.mods.locateMod("extra-utilities") == null) {
            overrideVersion();//显示版本号
        }
/*       //难度调整难度：
        if(Vars.mods.locateMod("creators")!=null) {//有CT2时加载它
            Events.on(EventType.ClientLoadEvent.class, e -> {
                ui.settings.game.sliderPref(
                        "游戏难度", 3, 0, 5, 1, i -> Core.bundle.format("Difficulty-" + i)
                );
                Core.settings.get("游戏难度", true);
                new WorldDifficultyCT2().set();
            });
        }else{  new WorldDifficulty().init();}*/
/*
       //动态logo
        try {
            Class arc = Class.forName("mindustry.arcModule.ARCVars");
        } catch (ClassNotFoundException e) {
            Vars.ui.menufrag = new UnemFragment();
            new UnemFragment().build(ui.menuGroup);
        }*/
        try {
            // 尝试加载 ARCVars 类
            Class arc = Class.forName("mindustryX.VarsX");
            // 如果类存在，执行你的逻辑
            Log.info("当前为mindustryX端，将禁止自动存档模组");
            // 在这里添加你的逻辑代码
            if((Vars.mods.locateMod("auto_saver")!=null) ) {
                Vars.mods.getMod("auto_saver").state = Mods.ModState.disabled;
            }
        } catch (ClassNotFoundException e) {
            // 如果类不存在，跳过逻辑
           // Log.info("ARCVars 类不存在，跳过逻辑");
        }



        if (加载CTTD()) {
            SpawnDraw.setEnable2(true, true, true);
        } else {
            SpawnDraw.setEnable2(true, false, true);
            Events.on(EventType.ClientLoadEvent.class, e -> {//加载塔防时，显示一个信息提示
                var dialog = new BaseDialog("");
                dialog.cont.table(Tex.button, t -> {
                    t.defaults().size(280, 160).left();
                    t.add(Core.bundle.format("planet.CT.xinxi")).row();
                    t.button("@close", (dialog::hide)).size(100, 64).center();//关闭按钮
                });
                dialog.addCloseListener();//按esc关闭
                dialog.show();
            });
        }
        //区块名显示
        Vars.ui.planet = new CT3PlanetDialog();
        if (!加载CTTD()) {
        } else {
            CTGalaxyAcknowledgments.标题页菜单();//添加标题页菜单
            if (加载CT2()) {
                Core.settings.put("游戏难度", 4);
            } else {
                Core.settings.put("游戏难度", 3);
            }//每次重启游戏恢复到默认难度
        }

        CT3InfoDialog.show();//开屏显示
        loadPowerShow();//电力显示方块
        TDloadPowerShow();//塔防电力显示方块
        CT3选择方块显示图标(); //选择方块显示图标

       ctUpdateDialog.load();//更新检测 新版 在用



        // Timer.schedule(CTUpdater::checkUpdate, 4);//檢測更新 旧版 未用
        //new Wave();   //跳波惩罚 这个被用在主篇去加载了

        //首页主功能按钮
        CT3function.show();
        ImageButton imagebutton = CreatorsIcon("functionIcon", Styles.defaulti, CT3function.功能图标UI);
        Vars.ui.menuGroup.fill(t -> {
            if (mobile) {
                t.add(imagebutton).update(b -> b.color.fromHsv(Time.time % 360, 1, 1)).size(80);//手机
                t.bottom();
            } else {
                t.add(imagebutton).update(b -> b.color.fromHsv(Time.time % 360, 1, 1)).size(120.0f);//电脑
                t.left().bottom();
            }
        });

       /*
       //动态logo
        try {
            Class arc = Class.forName("mindustry.arcModule.ARCVars");
        } catch (ClassNotFoundException e) {

            Vars.ui.menufrag = new UnemFragment();
            new UnemFragment().build(ui.menuGroup);
        }
        */
        //科技树全显
        CTResearchDialog dialog = new CTResearchDialog();
        ResearchDialog research = Vars.ui.research;
        research.shown(() -> {
            dialog.show();
            Objects.requireNonNull(research);
            Time.runTask(1.0F, research::hide);
        });

    }

    //选择方块显示图标
    public void CT3选择方块显示图标() {
        Events.run(EventType.Trigger.draw, () -> {
            if (Vars.ui != null) {
                indexer.eachBlock(null, camera.position.x, camera.position.y, (30 * tilesize), b -> true, b -> {
                    if (b instanceof LiquidSource.LiquidSourceBuild) {
                        var source = (LiquidSource.LiquidSourceBuild) b;
                        if (source.config() != null) {
                            Draw.z(Layer.block + 1);
                            Draw.rect(source.config().fullIcon, b.x, b.y, 3, 3);
                        }
                    }
                    if (b instanceof ItemSource.ItemSourceBuild) {
                        var source = (ItemSource.ItemSourceBuild) b;
                        if (source.config() != null) {
                            Draw.z(Layer.block + 1);
                            Draw.rect(source.config().fullIcon, b.x, b.y, 3, 3);
                        }
                    }
                    if (b instanceof Sorter.SorterBuild) {
                        var sorter = (Sorter.SorterBuild) b;
                        if (sorter.config() != null) {
                            Draw.z(Layer.block + 1);
                            Draw.rect(sorter.config().fullIcon, b.x, b.y, 3, 3);
                        }
                    }
                });
            }
        });
    }

    public boolean 加载CTTD() {
        return Vars.mods.locateMod("cttd") == null;
    }

    public boolean 加载CT2() {
        return Vars.mods.locateMod("creators") != null;
    }
}
