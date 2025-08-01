package CtCoreSystem.CoreSystem.type;

import arc.Core;
import arc.graphics.Color;
import arc.util.Time;
import mindustry.Vars;
import mindustry.core.UI;
import mindustry.ctype.UnlockableContent;
import mindustry.game.Team;
import mindustry.graphics.Pal;
import mindustry.ui.Bar;
import mindustry.world.Tile;
import mindustry.world.blocks.storage.CoreBlock;

import static arc.util.Tmp.c1;
import static mindustry.Vars.state;

//改核心的覆盖方式
public class CTCoreBlock extends CoreBlock {
    public CTCoreBlock(String name) {
        super(name);
    }
    public void setBars() {
        super.setBars();
        this.addBar("capacity", (e) -> {
            return new Bar(() -> {
                return  Math.floor(e.health) + " / " + e.maxHealth;
            }, () -> {
                return (c1.set(Color.red).shiftHue((float) ((Time.time * 0.2) + (1 * (360 / 16)))));//变色;
            }, () -> {
                return (float)e.healthf();
            });
        });
        this.removeBar("health");
    }

    public boolean canPlaceOn(Tile tile, Team team, int rotation) {
        if (tile == null) return false;
        //in the editor, you can place them anywhere for convenience
        if (state.isEditor()) return true;

        CoreBuild core = team.core();

        //special floor upon which cores can be placed
        tile.getLinkedTilesAs(this, tempTiles);
        if (!tempTiles.contains(o -> !o.floor().allowCorePlacement || o.block() instanceof CoreBlock)) {
            return true;
        }

        //must have all requirements
        if (core == null || (!state.rules.infiniteResources && !core.items.has(requirements, state.rules.buildCostMultiplier)))
            return false;

        return tile.block() instanceof CoreBlock && size >= tile.block().size;//更改原版的限制 现在等于或大于都可以覆盖
    }
}
