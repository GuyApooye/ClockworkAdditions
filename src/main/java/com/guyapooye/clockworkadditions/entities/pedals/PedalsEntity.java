package com.guyapooye.clockworkadditions.entities.pedals;

import com.guyapooye.clockworkadditions.blocks.kinetics.pedals.PedalsBlock;
import com.guyapooye.clockworkadditions.blocks.kinetics.pedals.PedalsBlockEntity;
import com.simibubi.create.content.contraptions.actors.seat.SeatBlock;
import com.simibubi.create.content.contraptions.actors.seat.SeatEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;
import org.joml.Vector3f;

public class PedalsEntity extends SeatEntity {
    public PedalsEntity(EntityType<?> p_i48580_1_, Level p_i48580_2_) {
        super(p_i48580_1_, p_i48580_2_);
    }
    public PedalsEntity(Level world, BlockPos pos) {
        super(world, pos);
    }

    @Override
    public void tick() {
        if (level().isClientSide) return;
        BlockState blockState = level().getBlockState(blockPosition());
        if (!(blockState.getBlock() instanceof PedalsBlock)) return;

        if (!(this.getFirstPassenger() instanceof LivingEntity livingEntity)) return;
        Vector3f move = new Vector3f(livingEntity.xxa, livingEntity.yya, livingEntity.zza);
        move.rotate(blockState.getValue(PedalsBlock.HORIZONTAL_FACING).getRotation().invert());

        if (level().getBlockEntity(blockPosition()) instanceof PedalsBlockEntity pedalsBlockEntity) {
            pedalsBlockEntity.move.set(livingEntity.xxa, livingEntity.yya, livingEntity.zza);
        }

        if (!isVehicle()) this.discard();
    }
}
