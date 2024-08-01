package com.github.guyapooye.clockworkadditions.mixin.create;

import com.simibubi.create.content.redstone.link.IRedstoneLinkable;
import com.simibubi.create.content.redstone.link.LinkBehaviour;
import com.simibubi.create.content.redstone.link.RedstoneLinkBlockEntity;
import com.simibubi.create.content.redstone.link.RedstoneLinkNetworkHandler;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.infrastructure.config.AllConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.valkyrienskies.clockwork.ClockworkItems;
import org.valkyrienskies.core.api.ships.Ship;
import org.valkyrienskies.mod.common.VSGameUtilsKt;
import org.valkyrienskies.mod.common.util.VectorConversionsMCKt;

@Mixin(value = RedstoneLinkNetworkHandler.class, priority = 1001)
public abstract class RedstoneLinkNetworkHandlerMixin {
    @Unique
    private static Level clockworkAdditions$harvestedWorld;
    @Inject(
            method = "withinRange",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/core/BlockPos;closerThan(Lnet/minecraft/core/Vec3i;D)Z"
            ),
            cancellable = true)
    private static void clockworkAdditions$wrapCloserThan(IRedstoneLinkable from, IRedstoneLinkable _to, CallbackInfoReturnable<Boolean> cir) {
        Couple<RedstoneLinkNetworkHandler.Frequency> frequencyCouple = from.getNetworkKey();
        boolean isCrystal = frequencyCouple.either(f -> (
                f.getStack().sameItem(ClockworkItems.WANDERLITE_CRYSTAL.asStack())));
        boolean isCube = frequencyCouple.either(f -> (
                f.getStack().sameItem(ClockworkItems.WANDERLITE_CUBE.asStack())));
        Ship ship1 = VSGameUtilsKt.getShipManagingPos(clockworkAdditions$harvestedWorld, from.getLocation());
        Ship ship2 = VSGameUtilsKt.getShipManagingPos(clockworkAdditions$harvestedWorld, new BlockPos(_to.getLocation()));
        Vec3 pos1 = Vec3.atLowerCornerOf(from.getLocation());
        Vec3 pos2 = Vec3.atLowerCornerOf(_to.getLocation());
        if (ship1 != null) {
            pos1 = VectorConversionsMCKt.toMinecraft(ship1.getShipToWorld().transformPosition(VectorConversionsMCKt.toJOML(pos1)));
        }
        if (ship2 != null) {
            pos2 = VectorConversionsMCKt.toMinecraft(ship2.getShipToWorld().transformPosition(VectorConversionsMCKt.toJOML(pos2)));
        }
        cir.setReturnValue(isCrystal ? from.getLocation().closerThan(_to.getLocation(), AllConfigs.server().logistics.linkRange.get()) : pos1.closerThan(pos2, AllConfigs.server().logistics.linkRange.get()));
    }
//    /**
//     * @author GuyApooye
//     * @reason redirect closerThan when link doesn't include wanderlite crystal
//     */
//    @Inject(method = "withinRange", at = @At("HEAD"), cancellable = true)
//    private static void withinRange(IRedstoneLinkable from, IRedstoneLinkable to, CallbackInfoReturnable<Boolean> cir) {
//        if (from == to) {
//            cir.setReturnValue(true);
//            return;
//        }
//        cir.setReturnValue(clockworkAdditions$wrapCloserThan(from.getLocation(),to.getLocation(), AllConfigs.server().logistics.linkRange.get()));
//    }


    @Inject(method = "updateNetworkOf", at = @At("HEAD"))
    private void harvestLevel(LevelAccessor world, IRedstoneLinkable actor, CallbackInfo ci) {
        clockworkAdditions$harvestedWorld = (Level) world;
    }
}
