package CtCoreSystem.CoreSystem.type;

import mindustry.world.blocks.power.Battery;

public class InfinitePowerSource extends Battery {
    private static final float INFINITE_POWER = Float.MAX_VALUE / 1000f;

    public InfinitePowerSource(String name) {
        super(name);
        consumesPower = false;
        outputsPower = true;
        consumesTap = true;
    }
    public class InfinitePowerSourceBuild extends BatteryBuild {
    @Override
    public float getPowerProduction() {
        // 返回一个极大的数值模拟无限供电
        return INFINITE_POWER;
    }
    }
}
