/*
package CtCoreSystem.CoreSystem.type.Ovulam5480;

import arc.func.Prov;
import arc.struct.ObjectMap;
import mindustry.content.UnitTypes;
import mindustry.gen.EntityMapping;
import mindustry.gen.Entityc;
import mindustry.gen.UnitEntity;
import mindustry.type.UnitType;

import static mindustry.content.UnitTypes.flare;

//最大只掉1血单位
public class OnedamageUnit {
    public static ObjectMap<Class<? extends Entityc>, Integer> ids = new ObjectMap<>();

    public static int getId(Class<? extends Entityc> key) {
        return ids.get(key);
    }

    public static void put(Class<? extends Entityc> unitClass, UnitType type, Prov prov){
        ids.put(unitClass, EntityMapping.register(OnedamageUnit.class + type.name, prov));
    }
    public class OnedamageUnits extends UnitEntity {
        public int classId() {
            return EntityRegistry.getID(this.getClass());
        }

        @Override
        public void rawDamage(float amount) {
            super.rawDamage(1);
        }
    }
    public static void load() {
        ids.put(EntityMapping.register("ctcoresystem" + "最大只掉1血测试单位", OnedamageUnit.class));

    }
}
*/
