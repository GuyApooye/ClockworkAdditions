package com.github.guyapooye.clockworkadditions.mixin.create;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.content.decoration.encasing.EncasedBlock;
import com.simibubi.create.content.decoration.encasing.EncasingRegistry;
import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(ShaftBlock.class)
public abstract class ShaftBlockMixin {
    @WrapOperation(method = "use", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/kinetics/simpleRelays/ShaftBlock;tryEncase(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/phys/BlockHitResult;)Lnet/minecraft/world/InteractionResult;"), remap = false)
    public InteractionResult wrapTryEncase(ShaftBlock instance, BlockState state, Level level, BlockPos pos, ItemStack heldItem, Player player, InteractionHand hand, BlockHitResult ray, Operation<InteractionResult> original) {
        List<Block> encasedVariants = EncasingRegistry.getVariants(state.getBlock());
        encasedVariants.addAll(com.github.guyapooye.clockworkadditions.registries.EncasingRegistry.getVariants(state.getBlock()));
        for (Block block : encasedVariants) {
            if (block instanceof EncasedBlock encased) {
                if (encased.getCasing().asItem() != heldItem.getItem())
                    continue;

                if (level.isClientSide)
                    return InteractionResult.SUCCESS;

                encased.handleEncasing(state, level, pos, heldItem, player, hand, ray);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }
}
