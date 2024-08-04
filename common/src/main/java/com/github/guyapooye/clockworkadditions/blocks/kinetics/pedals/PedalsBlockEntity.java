package com.github.guyapooye.clockworkadditions.blocks.kinetics.pedals;

import com.github.guyapooye.clockworkadditions.registries.BlockRegistry;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PedalsBlockEntity extends GeneratingKineticBlockEntity {

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
            updateGeneratedRotation();
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
        int forward = pressedKeys.contains(0) ? 32 : 0;
        int backward = pressedKeys.contains(1) ? 32 : 0;
        int sprint = pressedKeys.contains(6) ? 2 : 1;
//        System.out.println("FORWARD: "+pressedKeys.contains(InputKey.FORWARD));
//        System.out.println("BACKWARD: "+pressedKeys.contains(InputKey.BACKWARD));
//        System.out.println("SPRINT: "+pressedKeys.contains(InputKey.SPRINT));
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
//        float capacity = (pressedKeys.contains(InputKey.FORWARD) ^ pressedKeys.contains(InputKey.BACKWARD) ? (pressedKeys.contains(InputKey.SPRINT) ? (float) 0.5 : 1) : 0);
//        this.lastCapacityProvided = capacity;
//        return capacity;
//    }

    @Override
    protected Block getStressConfigKey() {
        return BlockRegistry.PEDALS.get();
    }
}
