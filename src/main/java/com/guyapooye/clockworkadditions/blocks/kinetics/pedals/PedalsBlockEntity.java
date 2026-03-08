package com.guyapooye.clockworkadditions.blocks.kinetics.pedals;

import com.guyapooye.clockworkadditions.registries.BlockRegistry;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

public class PedalsBlockEntity extends KineticBlockEntity {

    @NotNull
    private Collection<Integer> pressedKeys = new HashSet<>();
    public float independentAngle;
    public float chasingVelocity;

    public PedalsBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public final void updateInput(@NotNull Collection<Integer> pressedKeys) {
        if (!Objects.equals(this.pressedKeys, pressedKeys)) {
            this.pressedKeys = pressedKeys;
        }
    }

    public float getIndependentAngle(float partialTicks) {
//        System.out.println("independentAngle: "+independentAngle);
//        System.out.println("chasingVelocity: "+chasingVelocity);
//        System.out.println("pressedKeys: "+pressedKeys);
        return (independentAngle + partialTicks * chasingVelocity) / 360;
    }
    @Override
    public float getGeneratedSpeed() {
        return 1;
    }
    @Override
    public void tick() {
        super.tick();
//        System.out.println(pressedKeys);
        float actualSpeed = getSpeed();
        chasingVelocity += ((actualSpeed * 10 / 3f) - chasingVelocity) * .25f;
        independentAngle += chasingVelocity;
    }
//    @Override
//    public float calculateAddedStressCapacity() {
//        float capacity = (pressedKeys.contains(InputKey.FORWARD) ^ pressedKeys.contains(InputKey.BACKWARD) ? (pressedKeys.contains(InputKey.SPRINT) ? (float) 0.5 : 1) : 0);
//        this.lastCapacityProvided = capacity;
//        return capacity;
//    }

    @Override
    public void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
    }

    @Override
    protected Block getStressConfigKey() {
        return BlockRegistry.PEDALS.get();
    }
}
