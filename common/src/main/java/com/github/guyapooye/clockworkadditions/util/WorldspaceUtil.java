package com.github.guyapooye.clockworkadditions.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.joml.Matrix4d;
import org.joml.Matrix4dc;
import org.joml.Vector3d;
import org.valkyrienskies.core.api.ships.ClientShip;
import org.valkyrienskies.core.api.ships.Ship;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

public class WorldspaceUtil {
    public static Matrix4dc getShipToWorld(BlockEntity be) {
        VSGameUtilsKt.getShipManagingPos(be.getLevel(), be.getBlockPos());
        Ship ship = VSGameUtilsKt.getShipManagingPos(be.getLevel(), be.getBlockPos());
        if (ship == null) return new Matrix4d(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);
        return ship.getShipToWorld();
    }

    public static Matrix4dc getShipToWorldClient(BlockEntity be, Level level) {
        VSGameUtilsKt.getShipManagingPos(level, be.getBlockPos());
        ClientShip ship = (ClientShip) VSGameUtilsKt.getShipManagingPos(level, be.getBlockPos());

        if (ship == null) return new Matrix4d(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);
        return ship.getRenderTransform().getShipToWorld();
    }

    public static Vector3d getWorldSpace(BlockEntity be) {
        BlockPos pos = be.getBlockPos();
        return getShipToWorld(be).transformPosition(new Vector3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5));
    }

    public static Vector3d getWorldSpaceClient(BlockEntity be, Level level) {
        BlockPos pos = be.getBlockPos();
        return getShipToWorldClient(be, level).transformPosition(new Vector3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5));
    }
}
