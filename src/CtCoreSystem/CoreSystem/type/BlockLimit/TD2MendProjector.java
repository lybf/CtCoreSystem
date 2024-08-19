package CtCoreSystem.CoreSystem.type.BlockLimit;

import arc.Core;
import mindustry.Vars;
import mindustry.game.Team;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.blocks.defense.MendProjector;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.meta.BuildVisibility;

public class TD2MendProjector extends MendProjector {
    public TD2MendProjector(String name) {
        super(name);
    }
    public void requirements(Category cat, ItemStack[] stacks){
        requirements(cat, BuildVisibility.shown, stacks);
    }
    public int 数量 = 1;

    //瞬间替换和扣除物品
    @Override
    public void placeBegan(Tile tile, Block previous) {
        if (!Vars.state.rules.infiniteResources) {
            CoreBlock.CoreBuild core = Vars.player.team().core();
            core.items.remove(requirements);
        }
        tile.setBlock(this, tile.team());
        tile.block().placeEffect.at(tile, tile.block().size);
    }

    //允许放置
    @Override
    public boolean canPlaceOn(Tile tile, Team team, int rotation) {
        CoreBlock.CoreBuild core = team.core();
        //size后面数字代表允许建筑的数量，最多是数字的值+1
        if (core == null || Vars.state.teams.get(team).getBuildings(this).size > 数量 - 1
                || (!Vars.state.rules.infiniteResources && !core.items.has(requirements, Vars.state.rules.buildCostMultiplier)
        )) return false;
        return super.canPlaceOn(tile, team, rotation);
    }

    //显示红字
    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid) {
        CoreBlock.CoreBuild core = Vars.player.team().core();
        if (!Vars.state.rules.infiniteResources && !core.items.has(requirements, Vars.state.rules.buildCostMultiplier)) {
            drawPlaceText(Core.bundle.format("bar.noresources"), x, y, valid);
        } else if (Vars.state.teams.get(Vars.player.team()).getBuildings(this).size > 数量 - 1)
            drawPlaceText(Core.bundle.get("QuantityLimit.限制最大数量") + 数量, x, y, valid);
        super.drawPlace(x, y, rotation, valid);
    }
}
