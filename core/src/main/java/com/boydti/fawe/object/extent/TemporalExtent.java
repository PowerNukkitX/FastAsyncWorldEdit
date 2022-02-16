package com.boydti.fawe.object.extent;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.BlockMaterial;
import com.sk89q.worldedit.extent.AbstractDelegateExtent;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.world.biome.BaseBiome;
import com.sk89q.worldedit.world.registry.BundledBlockData;

public class TemporalExtent extends AbstractDelegateExtent {
    private int x, y, z = Integer.MAX_VALUE;
    private BaseBlock block = EditSession.nullBlock;

    private int bx, bz = Integer.MAX_VALUE;
    private BaseBiome biome = EditSession.nullBiome;

    /**
     * Create a new instance.
     *
     * @param extent the extent
     */
    public TemporalExtent(Extent extent) {
        super(extent);
    }


    public void set(int x, int y, int z, BaseBlock block) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.block = block;
    }

    public void set(int x, int z, BaseBiome biome) {
        this.bx = x;
        this.bz = z;
        this.biome = biome;
    }

    @Override
    public int getBrightness(int x, int y, int z) {
        if (this.x == x && this.y == y && this.z == z) {
            BlockMaterial block = BundledBlockData.getInstance().getMaterialById(this.block.getId());
            if (block == null) {
                return 15;
            }
            return Math.min(15, block.getLightValue());
        }
        return super.getBrightness(x, y, z);
    }

    @Override
    public BaseBlock getBlock(Vector position) {
        if (position.getX() == x && position.getY() == y && position.getZ() == z) {
            return block;
        }
        return super.getBlock(position);
    }

    @Override
    public BaseBlock getLazyBlock(Vector position) {
        if (position.getX() == x && position.getY() == y && position.getZ() == z) {
            return block;
        }
        return super.getLazyBlock(position);
    }

    @Override
    public BaseBlock getLazyBlock(int x, int y, int z) {
        if (this.x == x && this.y == y && this.z == z) {
            return block;
        }
        return super.getLazyBlock(x, y, z);
    }

    @Override
    public BaseBiome getBiome(Vector2D position) {
        if (position.getX() == bx && position.getZ() == bz) {
            return biome;
        }
        return super.getBiome(position);
    }
}
