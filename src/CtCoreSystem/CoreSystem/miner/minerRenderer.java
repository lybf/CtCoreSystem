package CtCoreSystem.CoreSystem.miner;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.math.Mathf;
import arc.math.geom.Geometry;
import arc.math.geom.Rect;
import arc.struct.Seq;
import arc.util.Reflect;
import arc.util.Structs;
import arc.util.Time;
import arc.util.Tmp;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import mindustry.Vars;
import mindustry.game.Teams;
import mindustry.gen.BufferItem;
import mindustry.gen.Building;
import mindustry.gen.TimeItem;
import mindustry.type.Item;
import mindustry.world.Block;
import mindustry.world.DirectionalItemBuffer;
import mindustry.world.ItemBuffer;
import mindustry.world.Tile;
import mindustry.world.blocks.distribution.BufferedItemBridge;
import mindustry.world.blocks.distribution.DirectionBridge;
import mindustry.world.blocks.distribution.ItemBridge;
import mindustry.world.blocks.distribution.Junction;

public class minerRenderer {
    private static Field itemBridgeBufferField;
    private static Field bufferField;
    private static Field indexField;
    private static Item[] resultItems = new Item[8];
    private static float[] resultTimes = new float[8];
    private static Seq<Block> bridgeBlocks;
    private static Seq<Block> junctionBlocks;
    private static Seq<Block> directionBridgeBlocks;
    public minerRenderer() {
    }
    public static void init() {
        try {
            itemBridgeBufferField = BufferedItemBridge.BufferedItemBridgeBuild.class.getDeclaredField("buffer");
            bufferField = ItemBuffer.class.getDeclaredField("buffer");
            indexField = ItemBuffer.class.getDeclaredField("index");
            itemBridgeBufferField.setAccessible(true);
            bufferField.setAccessible(true);
            indexField.setAccessible(true);
        } catch (NoSuchFieldException var1) {
            NoSuchFieldException e = var1;
            throw new RuntimeException(e);
        }

        bridgeBlocks = Vars.content.blocks().select((b) -> {
            return b instanceof ItemBridge;
        });
        junctionBlocks = Vars.content.blocks().select((b) -> {
            return b instanceof Junction;
        });
        directionBridgeBlocks = Vars.content.blocks().select((b) -> {
            return b instanceof DirectionBridge;
        });
    }

    public static void draw() {
        Rect bounds = Core.camera.bounds(Tmp.r1).grow(8.0F);
        Draw.z(71.0F);
        Iterator var1 = Vars.state.teams.present.iterator();

        while(var1.hasNext()) {
            Teams.TeamData data = (Teams.TeamData)var1.next();
            Iterator var3 = bridgeBlocks.iterator();

            Block block;
            Iterator var5;
            Building building;
            while(var3.hasNext()) {
                block = (Block)var3.next();
                var5 = data.getBuildings(block).iterator();

                while(var5.hasNext()) {
                    building = (Building)var5.next();
                    if (bounds.contains(building.x, building.y)) {
                        drawItems(building);
                        drawBridgeItem((ItemBridge.ItemBridgeBuild)building);
                    }
                }
            }

            var3 = junctionBlocks.iterator();

            while(var3.hasNext()) {
                block = (Block)var3.next();
                var5 = data.getBuildings(block).iterator();

                while(var5.hasNext()) {
                    building = (Building)var5.next();
                    if (bounds.contains(building.x, building.y)) {
                        drawHiddenItem((Junction.JunctionBuild)building);
                    }
                }
            }

            var3 = directionBridgeBlocks.iterator();

            while(var3.hasNext()) {
                block = (Block)var3.next();
                var5 = data.getBuildings(block).iterator();

                while(var5.hasNext()) {
                    building = (Building)var5.next();
                    if (bounds.contains(building.x, building.y)) {
                        drawItems(building);
                    }
                }
            }
        }

        Draw.reset();
    }

    private static void ensureCapacity(int requireCapacity) {
        if (requireCapacity > resultItems.length) {
            Item[] newItems = new Item[requireCapacity];
            System.arraycopy(resultItems, 0, newItems, 0, resultTimes.length);
            resultItems = newItems;
        }

        if (requireCapacity > resultTimes.length) {
            float[] newTimes = new float[requireCapacity];
            System.arraycopy(resultTimes, 0, newTimes, 0, resultTimes.length);
            resultTimes = newTimes;
        }

    }

    private static void getBuffer(ItemBuffer itemBuffer) {
        long[] buffer = (long[])Reflect.get(itemBuffer, bufferField);
        int index = (Integer)Reflect.get(itemBuffer, indexField);
        int capacity = buffer.length;
        Arrays.fill(resultItems, (Object)null);
        Arrays.fill(resultTimes, 0.0F);
        ensureCapacity(capacity);

        for(int i = 0; i < index; ++i) {
            long l = buffer[i];
            resultItems[i] = Vars.content.item(TimeItem.item(l));
            resultTimes[i] = TimeItem.time(l);
        }

    }

    private static void getDirectionalBuffer(DirectionalItemBuffer buffer) {
        Arrays.fill(resultItems, (Object)null);
        Arrays.fill(resultTimes, 0.0F);
        int rotations = buffer.buffers.length;
        int capacity = buffer.buffers[0].length;
        int totalCapacity = rotations * capacity;
        ensureCapacity(totalCapacity);

        for(int i = 0; i < rotations; ++i) {
            for(int j = 0; j < buffer.indexes[i]; ++j) {
                long l = buffer.buffers[i][j];
                int index = i * capacity + j;
                resultItems[index] = Vars.content.item(BufferItem.item(l));
                resultTimes[index] = BufferItem.time(l);
            }
        }

    }

    public static void drawItems(Building building) {
        if (building.items != null) {
            int[] count = new int[]{0};
            building.items.each((item, amount) -> {
                for(int i = 0; i < amount; ++i) {
                    Draw.rect(item.uiIcon, building.x, building.y - 4.0F + 1.0F + 0.6F * (float)count[0], 4.0F, 4.0F);
                    int var10002 = count[0]++;
                }

            });
        }
    }

    public static void drawBridgeItem(ItemBridge.ItemBridgeBuild bridge) {
        Draw.color(Color.white, 0.8F);
        drawItems(bridge);
        if (bridge instanceof BufferedItemBridge.BufferedItemBridgeBuild) {
            BufferedItemBridge.BufferedItemBridgeBuild bufferedBridge = (BufferedItemBridge.BufferedItemBridgeBuild)bridge;
            drawHiddenItem(bufferedBridge);
        }

    }

    public static void drawHiddenItem(BufferedItemBridge.BufferedItemBridgeBuild bridge) {
        ItemBuffer buffer = (ItemBuffer)Reflect.get(bridge, itemBridgeBufferField);
        BufferedItemBridge block = (BufferedItemBridge)bridge.block;
        int capacity = block.bufferCapacity;
        getBuffer(buffer);
        if (Structs.contains(resultItems, Objects::nonNull)) {
            Tile other = Vars.world.tile(bridge.link);
            float x = bridge.x;
            float y = bridge.y;
            float begX;
            float begY;
            float endX;
            float endY;
            int i;
            float time;
            if (!block.linkValid(bridge.tile, other)) {
                begX = x - 4.0F;
                begY = y - 4.0F;
                endX = x + 4.0F;
                endY = y - 4.0F;
            } else {
                i = bridge.relativeTo(other);
                int otherBridgeRot = Math.floorMod(i + 2, 4);
                time = Vars.state.isEditor() ? 1.0F : bridge.warmup;
                begX = x + (float)(Geometry.d4x(i) * 8) / 2.0F;
                begY = y + (float)(Geometry.d4y(i) * 8) / 2.0F;
                endX = other.worldx() + (float)(Geometry.d4x(otherBridgeRot) * 8) / 2.0F;
                endY = other.worldy() + (float)(Geometry.d4y(otherBridgeRot) * 8) / 2.0F;
                endX = Mathf.lerp(begX, endX, time);
                endY = Mathf.lerp(begY, endY, time);
            }

            for(i = 0; i < capacity; ++i) {
                Item item = resultItems[i];
                time = resultTimes[i];
                if (item != null) {
                    float progress = Math.min((Time.time - time) * bridge.timeScale() / block.speed * (float)capacity, (float)(capacity - i - 1)) / (float)capacity;
                    float itemX = Mathf.lerp(begX, endX, progress);
                    float itemY = Mathf.lerp(begY, endY, progress);
                    Draw.rect(item.uiIcon, itemX, itemY, 4.0F, 4.0F);
                }
            }

        }
    }

    public static void drawHiddenItem(Junction.JunctionBuild junction) {
        DirectionalItemBuffer buffer = junction.buffer;
        Junction block = (Junction)junction.block;
        int rotations = buffer.indexes.length;
        int capacity = block.capacity;
        getDirectionalBuffer(buffer);
        if (Structs.contains(resultItems, Objects::nonNull)) {
            for(int from = 0; from < rotations; ++from) {
                if (buffer.indexes[from] > 0) {
                    int to = Math.floorMod(from + 1, 4);
                    float begX = junction.x - (float)(Geometry.d4x(from) * 8) / 4.0F + (float)(Geometry.d4x(to) * 8) / 4.0F;
                    float begY = junction.y - (float)(Geometry.d4y(from) * 8) / 4.0F + (float)(Geometry.d4y(to) * 8) / 4.0F;
                    float endX = junction.x + (float)(Geometry.d4x(from) * 8) / 2.0F + (float)(Geometry.d4x(to) * 8) / 4.0F;
                    float endY = junction.y + (float)(Geometry.d4y(from) * 8) / 2.0F + (float)(Geometry.d4y(to) * 8) / 4.0F;

                    for(int j = 0; j < capacity; ++j) {
                        int index = from * capacity + j;
                        Item item = resultItems[index];
                        float time = resultTimes[index];
                        if (item != null) {
                            float progress = Math.min((Time.time - time) * junction.timeScale() / block.speed * (float)capacity, (float)(capacity - j - 1)) / (float)capacity;
                            float itemX = Mathf.lerp(begX, endX, progress);
                            float itemY = Mathf.lerp(begY, endY, progress);
                            Draw.rect(item.uiIcon, itemX, itemY, 4.0F, 4.0F);
                        }
                    }
                }
            }

        }
    }
}
