package com.boydti.fawe.object;

import com.boydti.fawe.FaweCache;
import com.boydti.fawe.object.extent.ExtentHeightCacher;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.function.pattern.AbstractPattern;

public class DataAnglePattern extends AbstractPattern {
    public final double FACTOR;
    public final Extent extent;
    public final int maxY;
    public final int distance;

    public DataAnglePattern(Extent extent, int distance) {
        this.extent = new ExtentHeightCacher(extent);
        this.maxY = extent.getMaximumPoint().getBlockY();
        this.distance = distance;
        this.FACTOR = (1D / distance) * (1D / 255);
    }

    public int getSlope(BaseBlock block, Vector vector) {
        int x = vector.getBlockX();
        int y = vector.getBlockY();
        int z = vector.getBlockZ();
        if (FaweCache.canPassThrough(block.getId(), block.getData())) {
            return -1;
        }
        int slope;
        boolean aboveMin;
        slope = Math.abs(extent.getNearestSurfaceTerrainBlock(x + distance, z, y, 0, maxY) - extent.getNearestSurfaceTerrainBlock(x - distance, z, y, 0, maxY)) * 7;
        slope += Math.abs(extent.getNearestSurfaceTerrainBlock(x, z + distance, y, 0, maxY) - extent.getNearestSurfaceTerrainBlock(x, z - distance, y, 0, maxY)) * 7;
        slope += Math.abs(extent.getNearestSurfaceTerrainBlock(x + distance, z + distance, y, 0, maxY) - extent.getNearestSurfaceTerrainBlock(x - distance, z - distance, y, 0, maxY)) * 5;
        slope += Math.abs(extent.getNearestSurfaceTerrainBlock(x - distance, z + distance, y, 0, maxY) - extent.getNearestSurfaceTerrainBlock(x + distance, z - distance, y, 0, maxY)) * 5;
        return slope;
    }

    @Override
    public BaseBlock apply(Vector position) {
        BaseBlock block = extent.getBlock(position);
        int slope = getSlope(block, position);
        if (slope == -1) return block;
        int data = (Math.min(slope, 255)) >> 4;
        return FaweCache.getBlock(block.getId(), data);
    }

    @Override
    public boolean apply(Extent extent, Vector setPosition, Vector getPosition) throws WorldEditException {
        BaseBlock block = extent.getBlock(getPosition);
        int slope = getSlope(block, getPosition);
        if (slope == -1) return false;
        int data = (Math.min(slope, 255)) >> 4;
        return extent.setBlock(setPosition, FaweCache.getBlock(block.getId(), data));
    }
}