package com.guyapooye.clockworkadditions.blocks.kinetics.pedals;

import com.guyapooye.clockworkadditions.registries.BlockRegistry;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3f;

public class PedalsBlockEntity extends GeneratingKineticBlockEntity {

    public final Vector3f move = new Vector3f();

    public PedalsBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public float getGeneratedSpeed() {
        float speed = 0;
        if (move.x() != 0) speed = 16;
        if (move.z() != 0) speed = (float) (move.z() / 0.98 * 16);
        return speed;
    }

    @Override
    public float calculateAddedStressCapacity() {
        return 256;
    }

    @Override
    public void tick() {
        super.tick();

        updateGeneratedRotation();
    }

    @Override
    public void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);

        move.set(compound.getFloat("move_x"), compound.getFloat("move_y"), compound.getFloat("move_z"));
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);

        compound.putFloat("move_x", move.x());
        compound.putFloat("move_y", move.y());
        compound.putFloat("move_z", move.z());
    }

    @Override
    protected Block getStressConfigKey() {
        return BlockRegistry.PEDALS.get();
    }
}
