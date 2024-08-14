package com.github.guyapooye.clockworkadditions.blocks.copycats;

import com.github.guyapooye.clockworkadditions.registries.BlockEntityRegistry;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface ICopycatBlock extends IBE<CWACopycatBlockEntity>, IWrenchable{
    @Override
    default Class<CWACopycatBlockEntity> getBlockEntityClass() {
        return CWACopycatBlockEntity.class;
    }

    @Override
    default BlockEntityType<? extends CWACopycatBlockEntity> getBlockEntityType() {
        return BlockEntityRegistry.COPYCAT.get();
    }
    @Nullable
    @Override
    default <S extends BlockEntity> BlockEntityTicker<S> getTicker(Level p_153212_, BlockState p_153213_,
                                                                  BlockEntityType<S> p_153214_) {
        return null;
    }

    @Override
    default InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        onWrenched(state, context);
        return IWrenchable.super.onSneakWrenched(state, context);
    }

    @Override
    default InteractionResult onWrenched(BlockState state, UseOnContext context) {
        return onBlockEntityUse(context.getLevel(), context.getClickedPos(), ufte -> {
            ItemStack consumedItem = ufte.getConsumedItem();
            if (!ufte.hasCustomMaterial())
                return InteractionResult.PASS;
            Player player = context.getPlayer();
            if (!player.isCreative())
                player.getInventory()
                        .placeItemBackInInventory(consumedItem);
            context.getLevel()
                    .levelEvent(2001, context.getClickedPos(), Block.getId(ufte.getBlockState()));
            ufte.setMaterial(AllBlocks.COPYCAT_BASE.getDefaultState());
            ufte.setConsumedItem(ItemStack.EMPTY);
            return InteractionResult.SUCCESS;
        });
    }

    default InteractionResult use(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, Player pPlayer, @NotNull InteractionHand pHand,
                                 BlockHitResult pHit) {

        Direction face = pHit.getDirection();
        ItemStack itemInHand = pPlayer.getItemInHand(pHand);
        BlockState materialIn = getAcceptedBlockState(pLevel, pPos, itemInHand, face);

        if (materialIn != null)
            materialIn = prepareMaterial(pLevel, pPos, pState, pPlayer, pHand, pHit, materialIn);
        if (materialIn == null)
            return InteractionResult.PASS;

        BlockState material = materialIn;
        return onBlockEntityUse(pLevel, pPos, ufte -> {
            if (ufte.getMaterial()
                    .is(material.getBlock())) {
                if (!ufte.cycleMaterial())
                    return InteractionResult.PASS;
                ufte.getLevel()
                        .playSound(null, ufte.getBlockPos(), SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, .75f,
                                .95f);
                return InteractionResult.SUCCESS;
            }
            if (ufte.hasCustomMaterial())
                return InteractionResult.PASS;
            if (pLevel.isClientSide())
                return InteractionResult.SUCCESS;

            ufte.setMaterial(material);
            ufte.setConsumedItem(itemInHand);
            ufte.getLevel()
                    .playSound(null, ufte.getBlockPos(), material.getSoundType()
                            .getPlaceSound(), SoundSource.BLOCKS, 1, .75f);

            if (pPlayer.isCreative())
                return InteractionResult.SUCCESS;

            itemInHand.shrink(1);
            if (itemInHand.isEmpty())
                pPlayer.setItemInHand(pHand, ItemStack.EMPTY);
            return InteractionResult.SUCCESS;
        });
    }

    default void setPlacedBy(@NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pState, LivingEntity pPlacer, @NotNull ItemStack pStack) {
        if (pPlacer == null)
            return;
        ItemStack offhandItem = pPlacer.getItemInHand(InteractionHand.OFF_HAND);
        BlockState appliedState =
                getAcceptedBlockState(pLevel, pPos, offhandItem, Direction.orderedByNearest(pPlacer)[0]);

        if (appliedState == null)
            return;
        withBlockEntityDo(pLevel, pPos, ufte -> {
            if (ufte.hasCustomMaterial())
                return;

            ufte.setMaterial(appliedState);
            ufte.setConsumedItem(offhandItem);

            if (pPlacer instanceof Player player && player.isCreative())
                return;
            offhandItem.shrink(1);
            if (offhandItem.isEmpty())
                pPlacer.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
        });
    }

    @Nullable
    default BlockState getAcceptedBlockState(Level pLevel, BlockPos pPos, ItemStack item, Direction face) {
        if (!(item.getItem()instanceof BlockItem bi))
            return null;

        Block block = bi.getBlock();
        if (block instanceof ICopycatBlock)
            return null;

        BlockState appliedState = block.defaultBlockState();
        boolean hardCodedAllow = isAcceptedRegardless(appliedState);

        if (!AllTags.AllBlockTags.COPYCAT_ALLOW.matches(block) && !hardCodedAllow) {

            if (AllTags.AllBlockTags.COPYCAT_DENY.matches(block))
                return null;
            if (block instanceof EntityBlock)
                return null;
            if (block instanceof StairBlock)
                return null;

            if (pLevel != null) {
                VoxelShape shape = appliedState.getShape(pLevel, pPos);
                if (shape.isEmpty() || !shape.bounds()
                        .equals(Shapes.block()
                                .bounds()))
                    return null;

                VoxelShape collisionShape = appliedState.getCollisionShape(pLevel, pPos);
                if (collisionShape.isEmpty())
                    return null;
            }
        }

        if (face != null) {
            Direction.Axis axis = face.getAxis();

            if (appliedState.hasProperty(BlockStateProperties.FACING))
                appliedState = appliedState.setValue(BlockStateProperties.FACING, face);
            if (appliedState.hasProperty(BlockStateProperties.HORIZONTAL_FACING) && axis != Direction.Axis.Y)
                appliedState = appliedState.setValue(BlockStateProperties.HORIZONTAL_FACING, face);
            if (appliedState.hasProperty(BlockStateProperties.AXIS))
                appliedState = appliedState.setValue(BlockStateProperties.AXIS, axis);
            if (appliedState.hasProperty(BlockStateProperties.HORIZONTAL_AXIS) && axis != Direction.Axis.Y)
                appliedState = appliedState.setValue(BlockStateProperties.HORIZONTAL_AXIS, axis);
        }

        return appliedState;
    }

    default boolean isAcceptedRegardless(BlockState material) {
        return false;
    }

    default BlockState prepareMaterial(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer,
                                      InteractionHand pHand, BlockHitResult pHit, BlockState material) {
        return material;
    }

    default void onRemove(BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pNewState, boolean pIsMoving) {
        if (!pState.hasBlockEntity() || pState.getBlock() == pNewState.getBlock())
            return;
        if (!pIsMoving)
            withBlockEntityDo(pLevel, pPos, ufte -> Block.popResource(pLevel, pPos, ufte.getConsumedItem()));
        pLevel.removeBlockEntity(pPos);
    }

//    public void playerWillDestroy(@NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pState, @NotNull Player pPlayer) {
//        super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
//        if (pPlayer.isCreative())
//            withBlockEntityDo(pLevel, pPos, ufte -> ufte.setConsumedItem(ItemStack.EMPTY));
//    }

    static BlockState getMaterial(BlockGetter reader, BlockPos targetPos) {
        if (reader.getBlockEntity(targetPos) instanceof CWACopycatBlockEntity cbe)
            return cbe.getMaterial();
        return Blocks.AIR.defaultBlockState();
    }

    // Wrapped properties

    default SoundType getSoundType(BlockState state, LevelReader level, BlockPos pos, Entity entity) {
        return getMaterial(level, pos).getSoundType();
    }

    float getFriction(BlockState state, LevelReader level, BlockPos pos, Entity entity);

    int getLightEmission(BlockState state, BlockGetter level, BlockPos pos);

    float getExplosionResistance(BlockState state, BlockGetter level, BlockPos pos, Explosion explosion);

    boolean addLandingEffects(BlockState state1, ServerLevel level, BlockPos pos, BlockState state2,
                                     LivingEntity entity, int numberOfParticles);

    boolean addRunningEffects(BlockState state, Level level, BlockPos pos, Entity entity);

    float getEnchantPowerBonus(BlockState state, LevelReader level, BlockPos pos);

    // fabric: unsupported
    boolean canEntityDestroy(BlockState state, BlockGetter level, BlockPos pos, Entity entity);

    default boolean isValidSpawn(BlockState state, BlockGetter level, BlockPos pos, SpawnPlacements.Type type,
                                EntityType<?> entityType) {
        return false;
    }

    default void fallOn(@NotNull Level pLevel, @NotNull BlockState pState, @NotNull BlockPos pPos, @NotNull Entity pEntity, float p_152430_) {
        BlockState material = getMaterial(pLevel, pPos);
        material.getBlock()
                .fallOn(pLevel, material, pPos, pEntity, p_152430_);
    }

    default float getDestroyProgress(@NotNull BlockState pState, @NotNull Player pPlayer, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos) {
        return getMaterial(pLevel, pPos).getDestroyProgress(pPlayer, pLevel, pPos);
    }

    //

    @Environment(EnvType.CLIENT)
    static BlockColor wrappedColor() {
        return new WrappedBlockColor();
    }

    @Environment(EnvType.CLIENT)
    class WrappedBlockColor implements BlockColor {

        @Override
        public int getColor(@NotNull BlockState pState, @javax.annotation.Nullable BlockAndTintGetter pLevel, @javax.annotation.Nullable BlockPos pPos,
                            int pTintIndex) {
            if (pLevel == null || pPos == null)
                return GrassColor.get(0.5D, 1.0D);
            return Minecraft.getInstance()
                    .getBlockColors()
                    .getColor(getMaterial(pLevel, pPos), pLevel, pPos, pTintIndex);
        }

    }


    // fabric: util
    static <T, R> R maybeMaterialAs(BlockGetter level, BlockPos pos, Class<T> clazz,
                                    BiFunction<BlockState, T, R> ifType, Function<BlockState, R> ifNot) {
        BlockState material = getMaterial(level, pos);
        Block block = material.getBlock();
        if (clazz.isInstance(block))
            return ifType.apply(material, clazz.cast(block));
        return ifNot.apply(material);
    }
}
