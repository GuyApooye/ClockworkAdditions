package com.github.guyapooye.clockworkadditions.blocks.fluid.extensible_hose;

import com.github.guyapooye.clockworkadditions.registries.BlockEntityRegistry;
import com.github.guyapooye.clockworkadditions.registries.ShapesRegistry;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlock;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ExtensibleHoseBlock extends DirectionalBlock implements IBE<ExtensibleHoseBlockEntity>, IWrenchable {
    public ExtensibleHoseBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return ShapesRegistry.EXTENSIBLE_HOSE.get(state.getValue(FACING));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(BlockStateProperties.WATERLOGGED);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction nearestLookingDirection = context.getNearestLookingDirection();
        BlockState toPlace = defaultBlockState().setValue(FACING, context.getPlayer() != null && context.getPlayer()
                .isShiftKeyDown() ? nearestLookingDirection : nearestLookingDirection.getOpposite());
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        toPlace = ProperWaterloggedBlock.withWater(level, toPlace, pos);

        Direction targetDirection = context.getPlayer() != null && context.getPlayer()
                .isShiftKeyDown() ? nearestLookingDirection : nearestLookingDirection.getOpposite();
        Direction bestConnectedDirection = null;
        double bestDistance = Double.MAX_VALUE;

        for (Direction d : Iterate.directions) {
            BlockPos adjPos = pos.relative(d);
            BlockState adjState = level.getBlockState(adjPos);
            if (!FluidPipeBlock.canConnectTo(level, adjPos, adjState, d))
                continue;
            double distance = Vec3.atLowerCornerOf(d.getNormal())
                    .distanceTo(Vec3.atLowerCornerOf(targetDirection.getNormal()));
            if (distance > bestDistance)
                continue;
            bestDistance = distance;
            bestConnectedDirection = d;
        }

        if (bestConnectedDirection == null)
            return toPlace;
        if (bestConnectedDirection.getAxis() == targetDirection.getAxis())
            return toPlace;
        if (player.isShiftKeyDown() && bestConnectedDirection.getAxis() != targetDirection.getAxis())
            return toPlace;

        return toPlace.setValue(FACING, bestConnectedDirection.getOpposite());
    }

    @Override
    public Class<ExtensibleHoseBlockEntity> getBlockEntityClass() {
        return ExtensibleHoseBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends ExtensibleHoseBlockEntity> getBlockEntityType() {
        return BlockEntityRegistry.EXTENSIBLE_HOSE.get();
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        link(stack, worldIn, pos);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level worldIn, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        ItemStack stack = player.getItemInHand(interactionHand);
        if (!stack.is(asItem()) || player.isCrouching()) {
            return InteractionResult.PASS;
        }
        return link(stack, worldIn, pos);
    }

    public InteractionResult link(ItemStack stack, Level worldIn, BlockPos pos) {
        CompoundTag tag = stack.getTag();
        BlockPos targ = null;
        if (tag != null)
            targ = NbtUtils.readBlockPos(tag.getCompound("BoundPosition"));;
        if (targ == null) {
            stack.addTagElement("BoundPosition", NbtUtils.writeBlockPos(pos));
            stack.addTagElement("Enchantments", new ListTag() {{
                add(new CompoundTag());
            }});
            return InteractionResult.CONSUME;
        }
        ExtensibleHoseBlockEntity targBE = getBlockEntity(worldIn, targ);
        ExtensibleHoseBlockEntity destBE = getBlockEntity(worldIn, pos);
        if (targBE != null && destBE != null && !targ.equals(pos)) {
            destBE.attach(targ);
            stack.removeTagKey("BoundPosition");
            stack.removeTagKey("Enchantments");
            return InteractionResult.CONSUME;
        } else {
            stack.addTagElement("BoundPosition", NbtUtils.writeBlockPos(pos));
            return InteractionResult.CONSUME;
        }
    }
}
