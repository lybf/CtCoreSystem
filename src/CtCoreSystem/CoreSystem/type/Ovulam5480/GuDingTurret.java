package CtCoreSystem.CoreSystem.type.Ovulam5480;

import arc.math.Angles;
import arc.scene.ui.layout.Table;
import mindustry.ui.Styles;
import mindustry.world.blocks.defense.turrets.ItemTurret;
//固定方向の炮台
public class GuDingTurret extends ItemTurret {
    public int 方向数量 = 4;
    public float 初始额外角度;
    public GuDingTurret(String name) {
        super(name);
       // alwaysShooting = true;
        configurable = true;
        rotateSpeed = 20f;
    }
    public class GuDingTurretBuilding extends ItemTurretBuild{
        public int currentRot;

        @Override
        public void buildConfiguration(Table table) {
            table.table(Styles.black6, table1 -> table1.table(sliders -> sliders.slider(0, 方向数量 - 1, 1, currentRot, f -> currentRot = (int) f)));
        }

        @Override
        protected void turnToTarget(float targetRot){
            float rot = (float) currentRot / 方向数量 * -360 + 初始额外角度;
            rotation = Angles.moveToward(rotation, rot, rotateSpeed * delta() * potentialEfficiency);
        }
    }
}
