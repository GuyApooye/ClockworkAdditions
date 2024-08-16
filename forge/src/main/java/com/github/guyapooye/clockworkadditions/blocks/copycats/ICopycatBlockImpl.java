package com.github.guyapooye.clockworkadditions.blocks.copycats;

import com.github.guyapooye.clockworkadditions.blocks.copycats.CWACopycatBlockEntity;
import com.github.guyapooye.clockworkadditions.blocks.copycats.ICopycatBlock;
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
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.extensions.IForgeBlock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ICopycatBlockImpl extends ICopycatBlock{

    default boolean canSustainPlant(BlockState arg, BlockGetter arg2, BlockPos arg3, Direction arg4, IPlantable iPlantable) {
        return false;
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
    default @Nullable <S extends BlockEntity> BlockEntityTicker<S> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<S> p_153214_) {
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
    @Nullable
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

    @Override
    default float getFriction(BlockState state, LevelReader level, BlockPos pos, Entity entity) {
        return ICopycatBlock.getMaterial(level, pos).getFriction(level, pos, entity);
    }

    @Override
    default int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return ICopycatBlock.getMaterial(level, pos).getLightEmission(level, pos);
    }

    default boolean canHarvestBlock(BlockState state, BlockGetter level, BlockPos pos, Player player) {
        return ICopycatBlock.getMaterial(level, pos).canHarvestBlock(level, pos, player);
    }

    @Override
    default float getExplosionResistance(BlockState state, BlockGetter level, BlockPos pos, Explosion explosion) {
        return ICopycatBlock.getMaterial(level, pos).getExplosionResistance(level, pos, explosion);
    }

    @Override
    default boolean addLandingEffects(BlockState state1, ServerLevel level, BlockPos pos, BlockState state2,
                                      LivingEntity entity, int numberOfParticles) {
        return ICopycatBlock.getMaterial(level, pos).addLandingEffects(level, pos, state2, entity, numberOfParticles);
    }

    @Override
    default boolean addRunningEffects(BlockState state, Level level, BlockPos pos, Entity entity) {
        return ICopycatBlock.getMaterial(level, pos).addRunningEffects(level, pos, entity);
    }

    @Override
    default float getEnchantPowerBonus(BlockState state, LevelReader level, BlockPos pos) {
        return ICopycatBlock.getMaterial(level, pos).getEnchantPowerBonus(level, pos);
    }

    @Override
    default boolean canEntityDestroy(BlockState state, BlockGetter level, BlockPos pos, Entity entity) {
        return false;
    }

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
