package CtCoreSystem.ui;
import arc.ApplicationListener;
import arc.assets.Loadable;
//资源统计UI 2-2
public class CTui implements ApplicationListener, Loadable {
    public final static CTPlanetDialog CTplanet = new CTPlanetDialog();

    public void init(){
        Run(CTplanet);
    }
    private static void Run(CTPlanetDialog cTplanet) {
    }
}
