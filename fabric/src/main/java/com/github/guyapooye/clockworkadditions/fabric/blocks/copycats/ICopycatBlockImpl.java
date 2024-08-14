package com.github.guyapooye.clockworkadditions.fabric.blocks.copycats;

import com.github.guyapooye.clockworkadditions.blocks.copycats.CWACopycatBlockEntity;
import com.github.guyapooye.clockworkadditions.blocks.copycats.ICopycatBlock;
import com.simibubi.create.AllBlocks;
import io.github.fabricators_of_create.porting_lib.block.*;
import io.github.fabricators_of_create.porting_lib.enchant.EnchantmentBonusBlock;
import io.github.fabricators_of_create.porting_lib.util.ExplosionResistanceBlock;
import io.github.fabricators_of_create.porting_lib.util.ValidSpawnBlock;
import net.fabricmc.fabric.api.block.BlockPickInteractionAware;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public interface ICopycatBlockImpl extends ICopycatBlock, BlockPickInteractionAware, CustomFrictionBlock, CustomSoundTypeBlock, LightEmissiveBlock, ExplosionResistanceBlock,
        CustomLandingEffectsBlock, CustomRunningEffectsBlock, EnchantmentBonusBlock,
        ValidSpawnBlock {
    @Override
    default ItemStack getPickedStack(BlockState state, BlockGetter level, BlockPos pos, @Nullable Player player, @Nullable HitResult result) {
        BlockState material = ICopycatBlock.getMaterial(level, pos);
        if (AllBlocks.COPYCAT_BASE.has(material) || player != null && player.isShiftKeyDown())
            return new ItemStack((ItemLike) this);
        return ICopycatBlock.maybeMaterialAs(
                level, pos, BlockPickInteractionAware.class,
                (mat, block) -> block.getPickedStack(mat, level, pos, player, result),
                mat -> mat.getBlock().getCloneItemStack(level, pos, mat)
        );
    }

    @Override
    default Class<CWACopycatBlockEntity> getBlockEntityClass() {
        return ICopycatBlock.super.getBlockEntityClass();
    }

    @Override
    default BlockEntityType<? extends CWACopycatBlockEntity> getBlockEntityType() {
        return ICopycatBlock.super.getBlockEntityType();
    }

    @Override
    default @org.jetbrains.annotations.Nullable <S extends BlockEntity> BlockEntityTicker<S> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<S> p_153214_) {
        return ICopycatBlock.super.getTicker(p_153212_, p_153213_, p_153214_);
    }

    @Override
    default InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        return ICopycatBlock.super.onSneakWrenched(state, context);
    }

    @Override
    default InteractionResult onWrenched(BlockState state, UseOnContext context) {
        return ICopycatBlock.super.onWrenched(state, context);
    }

    @Override
    default InteractionResult use(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, Player pPlayer, @NotNull InteractionHand pHand, BlockHitResult pHit) {
        return ICopycatBlock.super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    @Override
    default void setPlacedBy(@NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pState, LivingEntity pPlacer, @NotNull ItemStack pStack) {
        ICopycatBlock.super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
    }

    @Override
    @org.jetbrains.annotations.Nullable
    default BlockState getAcceptedBlockState(Level pLevel, BlockPos pPos, ItemStack item, Direction face) {
        return ICopycatBlock.super.getAcceptedBlockState(pLevel, pPos, item, face);
    }

    @Override
    default boolean isAcceptedRegardless(BlockState material) {
        return ICopycatBlock.super.isAcceptedRegardless(material);
    }

    @Override
    default BlockState prepareMaterial(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer, InteractionHand pHand, BlockHitResult pHit, BlockState material) {
        return ICopycatBlock.super.prepareMaterial(pLevel, pPos, pState, pPlayer, pHand, pHit, material);
    }

    @Override
    default void onRemove(BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pNewState, boolean pIsMoving) {
        ICopycatBlock.super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    default SoundType getSoundType(BlockState state, LevelReader level, BlockPos pos, Entity entity) {
        return ICopycatBlock.super.getSoundType(state, level, pos, entity);
    }

    default float getFriction(BlockState state, LevelReader level, BlockPos pos, Entity entity) {
        return ICopycatBlock.maybeMaterialAs(
                level, pos, CustomFrictionBlock.class,
                (material, block) -> block.getFriction(material, level, pos, entity),
                material -> material.getBlock().getFriction()
        );
    }

    default int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return ICopycatBlock.maybeMaterialAs(
                level, pos, LightEmissiveBlock.class,
                (material, block) -> block.getLightEmission(material, level, pos),
                BlockBehaviour.BlockStateBase::getLightEmission
        );
    }

    default float getExplosionResistance(BlockState state, BlockGetter level, BlockPos pos, Explosion explosion) {
        return ICopycatBlock.maybeMaterialAs(
                level, pos, ExplosionResistanceBlock.class,
                (material, block) -> block.getExplosionResistance(material, level, pos, explosion),
                material -> material.getBlock().getExplosionResistance()
        );
    }
    default boolean addLandingEffects(BlockState state1, ServerLevel level, BlockPos pos, BlockState state2,
                                      LivingEntity entity, int numberOfParticles) {
        return ICopycatBlock.maybeMaterialAs(
                level, pos, CustomLandingEffectsBlock.class, // duplicate material is not a bug
                (material, block) -> block.addLandingEffects(material, level, pos, material, entity, numberOfParticles),
                material -> false // default to vanilla, true cancels
        );
    }

    default boolean addRunningEffects(BlockState state, Level level, BlockPos pos, Entity entity) {
        return ICopycatBlock.maybeMaterialAs(
                level, pos, CustomRunningEffectsBlock.class,
                (material, block) -> block.addRunningEffects(material, level, pos, entity),
                material -> false // default to vanilla, true cancels
        );
    }

    @Override
    default float getEnchantPowerBonus(BlockState state, LevelReader level, BlockPos pos) {
        return ICopycatBlock.maybeMaterialAs(
                level, pos, EnchantmentBonusBlock.class,
                (material, block) -> block.getEnchantPowerBonus(material, level, pos),
                material -> EnchantmentBonusBlock.super.getEnchantPowerBonus(material, level, pos)
        );
    }

    @Override
    boolean canEntityDestroy(BlockState state, BlockGetter level, BlockPos pos, Entity entity);

    @Override
    default boolean isValidSpawn(BlockState state, BlockGetter level, BlockPos pos, SpawnPlacements.Type type, EntityType<?> entityType) {
        return ICopycatBlock.super.isValidSpawn(state, level, pos, type, entityType);
    }

    @Override
    default void fallOn(@NotNull Level pLevel, @NotNull BlockState pState, @NotNull BlockPos pPos, @NotNull Entity pEntity, float p_152430_) {
        ICopycatBlock.super.fallOn(pLevel, pState, pPos, pEntity, p_152430_);
    }

    @Override
    default float getDestroyProgress(@NotNull BlockState pState, @NotNull Player pPlayer, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos) {
        return ICopycatBlock.super.getDestroyProgress(pState, pPlayer, pLevel, pPos);
    }
}
