package com.github.guyapooye.clockworkadditions.mixin.clockwork;


import com.llamalad7.mixinextras.sugar.Local;
import org.joml.Vector3d;
import org.joml.Vector3dc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.valkyrienskies.clockwork.content.forces.EncasedFanController;
import org.valkyrienskies.core.api.ships.PhysShip;

@Mixin(EncasedFanController.class)
public abstract class EncasedFanControllerMixin {
//    @Inject(method = "applyForces", at = @At(value = "INVOKE", target = "Lorg/joml/Vector3dc;add(DDDLorg/joml/Vector3d;)Lorg/joml/Vector3d;"))
//    private void computeForce(PhysShip physShip, CallbackInfo ci, @Local Vector3dc force) {
//        force.mul(10.0, (Vector3d) force);
//    }
}
