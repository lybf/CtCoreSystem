package CtCoreSystem.CoreSystem.type.Ovulam5480;

import arc.Core;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.entities.Units;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;
import mindustry.type.ItemStack;
import mindustry.type.StatusEffect;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatValues;

import static CtCoreSystem.CoreSystem.type.CTColor.C;
import static mindustry.Vars.tilesize;
//这个消耗的物品不参与一次buff施加
public class BUFF投影2 extends Block {
    public Block 升级前置 = null;

    public ItemStack[] itemStacks;
    //施加BUFF的持续世界
    public float duration = 60f;
    //消耗间隔
    public float reload = 60f;
    public StatusEffect effect;
    public Effect applyEffect = Fx.none;
    public Effect activeEffect = Fx.overdriveWave;
    public float range = 60f;

    public BUFF投影2(String name){
        super(name);
        solid = true;
        update = true;
        sync = true;
        hasItems = true;
    }


    public class BUFF投影2Build extends Building {
        public float timer;

       /* public void draw(){
            super.draw();
            Drawf.circles(x, y, range, C("a2fefa"));
        }*/

        @Override
        public void updateTile(){
            CoreBlock.CoreBuild coreBuild = Vars.state.rules.defaultTeam.core();
          //  if(coreBuild==null)return;
            if(coreBuild!=null)
                if(coreBuild.items.has(itemStacks))timer += edelta();
            if(efficiency > 0){
                Units.nearby(Team.crux, x, y, range, other -> {
                    other.apply(effect, duration);
                    applyEffect.at(other);
                });
            }
            if(timer > reload){
                timer -= reload;
                activeEffect.at(x, y, range);
                if (coreBuild != null) {
                    coreBuild.items.remove(itemStacks);
                }
            }
        }


        @Override
        public void drawSelect(){
            Drawf.dashCircle(x, y, range, Pal.accent);
        }

    }
    public boolean canReplace(Block other) {
        if (other.alwaysReplace) return true;
        return 升级前置 == null ? super.canReplace(other) : 升级前置 == other;
    }

    @Override
    public boolean canPlaceOn(Tile tile, Team team, int rotation) {
        if (tile == null) return false;
        if (Vars.state.isEditor() || 升级前置 == null || Vars.state.rules.infiniteResources) return true;

        tile.getLinkedTilesAs(this, tempTiles);
        return tempTiles.contains(o -> o.block() == 升级前置);
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid) {
         /*   if ( (player.team().core() != null && player.team().core().items.has(requirements, Vars.state.rules.buildCostMultiplier)) || Vars.state.rules.infiniteResources ) {
                this.drawPlaceText(Core.bundle.get("bar.noresources"), x, y, false);
            }*/
        if (!valid && 升级前置 != null)
            drawPlaceText(Core.bundle.format("cttd.UpgradeFront") + 升级前置.localizedName, x, y, false);
        Drawf.dashCircle(x * tilesize + offset, y * tilesize + offset, range, Pal.accent);
        super.drawPlace(x, y, rotation, valid);
    }@Override
    public void setStats() {
        super.setStats();
        stats.add(Stat.input, StatValues.items(reload, itemStacks));

    }

}