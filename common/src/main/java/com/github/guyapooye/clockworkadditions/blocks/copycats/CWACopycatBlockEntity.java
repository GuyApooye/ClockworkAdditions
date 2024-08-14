package com.github.guyapooye.clockworkadditions.blocks.copycats;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.ITransformableBlockEntity;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.redstone.RoseQuartzLampBlock;
import com.simibubi.create.content.schematics.requirement.ISpecialBlockEntityItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.IPartialSafeNBT;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.List;

public abstract class CWACopycatBlockEntity extends SmartBlockEntity
        implements ISpecialBlockEntityItemRequirement, ITransformableBlockEntity, IPartialSafeNBT {

    protected BlockState material;
    protected ItemStack consumedItem;

    public CWACopycatBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        material = AllBlocks.COPYCAT_BASE.getDefaultState();
        consumedItem = ItemStack.EMPTY;
    }

    public BlockState getMaterial() {
        return material;
    }

    public boolean hasCustomMaterial() {
        return !AllBlocks.COPYCAT_BASE.has(getMaterial());
    }

    public void setMaterial(BlockState blockState) {
        BlockState wrapperState = getBlockState();

        if (!material.is(blockState.getBlock()))
            for (Direction side : Iterate.directions) {
                BlockPos neighbour = worldPosition.relative(side);
                BlockState neighbourState = level.getBlockState(neighbour);
                if (neighbourState != wrapperState)
                    continue;
                if (!(level.getBlockEntity(neighbour)instanceof CWACopycatBlockEntity cbe))
                    continue;
                BlockState otherMaterial = cbe.getMaterial();
                if (!otherMaterial.is(blockState.getBlock()))
                    continue;
                blockState = otherMaterial;
                break;
            }

        material = blockState;
        if (!level.isClientSide()) {
            notifyUpdate();
            return;
        }
        redraw();
    }


    public boolean cycleMaterial() {
        if (material.hasProperty(TrapDoorBlock.HALF) && material.getOptionalValue(TrapDoorBlock.OPEN)
                .orElse(false))
            setMaterial(material.cycle(TrapDoorBlock.HALF));
        else if (material.hasProperty(BlockStateProperties.FACING))
            setMaterial(material.cycle(BlockStateProperties.FACING));
        else if (material.hasProperty(BlockStateProperties.HORIZONTAL_FACING))
            setMaterial(material.setValue(BlockStateProperties.HORIZONTAL_FACING,
                    material.getValue(BlockStateProperties.HORIZONTAL_FACING)
                            .getClockWise()));
        else if (material.hasProperty(BlockStateProperties.AXIS))
            setMaterial(material.cycle(BlockStateProperties.AXIS));
        else if (material.hasProperty(BlockStateProperties.HORIZONTAL_AXIS))
            setMaterial(material.cycle(BlockStateProperties.HORIZONTAL_AXIS));
        else if (material.hasProperty(BlockStateProperties.LIT))
            setMaterial(material.cycle(BlockStateProperties.LIT));
        else if (material.hasProperty(RoseQuartzLampBlock.POWERING))
            setMaterial(material.cycle(RoseQuartzLampBlock.POWERING));
        else
            return false;

        return true;
    }

    public ItemStack getConsumedItem() {
        return consumedItem;
    }

    public void setConsumedItem(ItemStack stack) {
        if (stack.getCount() == 0) consumedItem = ItemStack.EMPTY;
        else {
            ItemStack copy = stack.copy();
            copy.setCount(stack.getCount());
            consumedItem = copy;
        }

        setChanged();
    }

    protected void redraw() {
        // fabric: no need for requestModelDataUpdate
        if (hasLevel()) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 16);
            level.getChunkSource()
                    .getLightEngine()
                    .checkBlock(worldPosition);
        }
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {}

    @Override
    public ItemRequirement getRequiredItems(BlockState state) {
        if (consumedItem.isEmpty())
            return ItemRequirement.NONE;
        return new ItemRequirement(ItemRequirement.ItemUseType.CONSUME, consumedItem);
    }

    @Override
    public void transform(StructureTransform transform) {
        material = transform.apply(material);
        notifyUpdate();
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        super.read(tag, clientPacket);

        consumedItem = ItemStack.of(tag.getCompound("Item"));

        BlockState prevMaterial = material;
        if (!tag.contains("Material")) {
            consumedItem = ItemStack.EMPTY;
            return;
        }

        material = NbtUtils.readBlockState(blockHolderGetter(),tag.getCompound("Material"));

        // Validate Material
        if (!clientPacket) {
            BlockState blockState = getBlockState();
            if (!(blockState.getBlock() instanceof ICopycatBlock cb))
                return;
            BlockState acceptedBlockState = cb.getAcceptedBlockState(level, worldPosition, consumedItem, null);
            if (acceptedBlockState != null && material.is(acceptedBlockState.getBlock()))
                return;
            consumedItem = ItemStack.EMPTY;
            material = AllBlocks.COPYCAT_BASE.getDefaultState();
        }

        if (clientPacket && prevMaterial != material)
            redraw();
    }

    @Override
    public void writeSafe(CompoundTag tag) {
        super.writeSafe(tag);

        ItemStack stackWithoutNBT = consumedItem.copy();
        stackWithoutNBT.setTag(null);

        write(tag, stackWithoutNBT, material);
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        super.write(tag, clientPacket);
        write(tag, consumedItem, material);
    }

    protected void write(CompoundTag tag, ItemStack stack, BlockState material) {
        CompoundTag ret = new CompoundTag();
        tag.put("Item", stack.save(ret));
        tag.put("Material", NbtUtils.writeBlockState(material));
    }

    public BlockState getRenderAttachmentData() {
        return material;
    }
}
