package com.github.guyapooye.clockworkadditions.mixin.vs;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.valkyrienskies.mod.common.entity.handling.DefaultShipyardEntityHandler;
import org.valkyrienskies.mod.common.entity.handling.VSEntityHandler;
import org.valkyrienskies.mod.common.entity.handling.VSEntityManager;
import org.valkyrienskies.mod.common.util.IEntityDraggingInformationProvider;

@Mixin(VSEntityManager.class)
public abstract class VSEntityManagerMixin {
    @Inject(method = "determineDefaultHandler", at = @At("HEAD"), cancellable = true)
    private void workWithPedals(Entity entity, CallbackInfoReturnable<VSEntityHandler> cir) {
        try {
            String className = entity.getClass().getSimpleName();
            if (className.contains("PedalsEntity")) cir.setReturnValue(DefaultShipyardEntityHandler.INSTANCE);
//            if (entity instanceof Player player && ((IEntityDraggingInformationProvider) player).vs$shouldDrag()) cir.setReturnValue(DefaultShipyardEntityHandler.INSTANCE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
