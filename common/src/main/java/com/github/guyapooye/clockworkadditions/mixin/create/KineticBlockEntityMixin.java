package com.github.guyapooye.clockworkadditions.mixin.create;

import com.github.guyapooye.clockworkadditions.blocks.redstone.gyro.RedstoneGyroAttachment;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.valkyrienskies.core.api.ships.ServerShip;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

@Mixin(KineticBlockEntity.class)
public abstract class KineticBlockEntityMixin extends SmartBlockEntity {



    public KineticBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Inject(method = "getFlickerScore",at = @At("HEAD"),remap = false, cancellable = true)
    private void resetFlickerScoreWithGyro(CallbackInfoReturnable<Integer> cir) {
        if (getLevel().isClientSide) return;
        ServerShip ship = VSGameUtilsKt.getShipManagingPos((ServerLevel) getLevel(),getBlockPos());
        if (ship == null) return;
        int gyroAmount = RedstoneGyroAttachment.getOrCreate(ship).getAmount();
        if (gyroAmount <= 0) return;
        cir.setReturnValue(0);
    }
}
