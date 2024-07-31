package com.github.guyapooye.clockworkadditions.blocks.kinetics.pedals;

import com.github.guyapooye.clockworkadditions.registries.BlockRegistry;
import com.github.guyapooye.clockworkadditions.util.PedalsInputKey;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PedalsBlockEntity extends GeneratingKineticBlockEntity {

    @NotNull
    private Set<PedalsInputKey> pressedKeys = new HashSet<>();
    public float independentAngle;
    public float chasingVelocity;

    public PedalsBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public final void updateInput(@NotNull Set<PedalsInputKey> pressedKeys) {
        if (!Objects.equals(this.pressedKeys, pressedKeys)) {
            this.pressedKeys = pressedKeys;
            updateGeneratedRotation();
        }
    }

    public float getIndependentAngle(float partialTicks) {
        return (independentAngle + partialTicks * chasingVelocity) / 360;
    }
    @Override
    public float getGeneratedSpeed() {
        int forward = pressedKeys.contains(PedalsInputKey.FORWARD) ? 32 : 0;
        int backward = pressedKeys.contains(PedalsInputKey.BACKWARD) ? 32 : 0;
        int sprint = pressedKeys.contains(PedalsInputKey.SPRINT) ? 2 : 1;
//        System.out.println("FORWARD: "+pressedKeys.contains(PedalsInputKey.FORWARD));
//        System.out.println("BACKWARD: "+pressedKeys.contains(PedalsInputKey.BACKWARD));
//        System.out.println("SPRINT: "+pressedKeys.contains(PedalsInputKey.SPRINT));
        int speed = (forward-backward)*sprint;
//        System.out.println(speed);
        return speed;
    }
    @Override
    public void tick() {
        super.tick();

        float actualSpeed = getSpeed();
        chasingVelocity += ((actualSpeed * 10 / 3f) - chasingVelocity) * .25f;
        independentAngle += chasingVelocity;
    }
//    @Override
//    public float calculateAddedStressCapacity() {
//        float capacity = (pressedKeys.contains(PedalsInputKey.FORWARD) ^ pressedKeys.contains(PedalsInputKey.BACKWARD) ? (pressedKeys.contains(PedalsInputKey.SPRINT) ? (float) 0.5 : 1) : 0);
//        this.lastCapacityProvided = capacity;
//        return capacity;
//    }

    @Override
    protected Block getStressConfigKey() {
        return BlockRegistry.PEDALS.get();
    }
}
