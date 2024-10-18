package com.github.guyapooye.clockworkadditions.blocks.kinetics.cvjoint;

import com.github.guyapooye.clockworkadditions.registries.BlockEntityRegistry;
import com.github.guyapooye.clockworkadditions.registries.ShapesRegistry;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import static com.simibubi.create.foundation.block.ProperWaterloggedBlock.WATERLOGGED;

public class CVJointBlock extends DirectionalKineticBlock implements IBE<CVJointBlockEntity> {

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(FACING).getAxis();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return ShapesRegistry.CV_JOINT.get(state.getValue(FACING));

    }

    public CVJointBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(WATERLOGGED));
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == state.getValue(FACING).getOpposite();
    }

    @Override
    public Class<CVJointBlockEntity> getBlockEntityClass() {
        return CVJointBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends CVJointBlockEntity> getBlockEntityType() {
        return BlockEntityRegistry.CV_JOINT.get();
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(worldIn,pos,state,placer,stack);
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
            targ = NbtUtils.readBlockPos(tag.getCompound("BoundPosition"));
        if (targ == null) {
            stack.addTagElement("BoundPosition", NbtUtils.writeBlockPos(pos));
            stack.addTagElement("Enchantments", new ListTag() {{
                add(new CompoundTag());
            }});
            return InteractionResult.CONSUME;
        }
        CVJointBlockEntity targBE = getBlockEntity(worldIn, targ);
        CVJointBlockEntity destBE = getBlockEntity(worldIn, pos);
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
