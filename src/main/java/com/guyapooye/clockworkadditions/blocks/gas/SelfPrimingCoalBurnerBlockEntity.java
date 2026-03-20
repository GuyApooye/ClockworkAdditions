package com.guyapooye.clockworkadditions.blocks.gas;

import com.google.common.base.Optional;
import com.guyapooye.clockworkadditions.blocks.kinetics.pedals.PedalsBlock;
import com.guyapooye.clockworkadditions.entities.pedals.PedalsEntity;
import com.guyapooye.clockworkadditions.registries.ShapesRegistry;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.contraptions.actors.seat.SeatEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.infrastructure.config.AllConfigs;
import dev.architectury.registry.fuel.FuelRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Clearable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.util.FakePlayer;
import org.valkyrienskies.clockwork.ClockworkMod;
import org.valkyrienskies.clockwork.ClockworkPackets;
import org.valkyrienskies.kelvin.util.mixtureCapacity
import org.valkyrienskies.clockwork.util.KNodeBlockEntity;
import org.valkyrienskies.clockwork.util.blocktype.ISyncableStorage;
import org.valkyrienskies.clockwork.util.blocktype.SyncableStoragePacket;
import org.valkyrienskies.kelvin.api.DuctNodePos;
import org.valkyrienskies.kelvin.util.KelvinExtensions;

import java.util.List;

public class SelfPrimingCoalBurnerBlockEntity extends KNodeBlockEntity implements Clearable, ISyncableStorage {

    private int fuelTicks = 0;
    private double maxBurnTime = 0.0;

    private ItemStack storedFuelStack = ItemStack.EMPTY;
    private ItemStack remainingItemStack = ItemStack.EMPTY;
    private int previousTotalItems = 0;

    public static final double MAX_JOULES_PER_TICK = 10000.0;

    public SelfPrimingCoalBurnerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }


    @Override
    public void tick() {
        super.tick();

        if (level.isClientSide) return;

        int totalItems = storedFuelStack.getCount() + remainingItemStack.getCount();
        if (totalItems != previousTotalItems) {
            ClockworkPackets.sendToNear(
                    (ServerLevel) level,
                    this.worldPosition,
                    64,
                    new SyncableStoragePacket(this)
            );
            this.previousTotalItems = totalItems;
        }

        var kelvin = ClockworkMod.getKelvin();
        DuctNodePos nodePos = KelvinExtensions.INSTANCE.toDuctNodePos(getBlockPos(), level.dimension().location());
        if (kelvin.getNodeAt(nodePos) == null) return;

        if (fuelTicks > 0) {
            fuelTicks -= 1;
            var currentInternalGasses = kelvin.getGasMassAt(nodePos);
            double currentInternalTemperature = kelvin.getTemperatureAt(nodePos);
            if (currentInternalGasses.values().stream().mapToDouble(Double::doubleValue).sum() > 1e-5) {
                double currentInternalHeatCapacity = GasPh mixtureCapacity(currentInternalGasses);
                double targetTemperature = 850.0;
                double energyToAdd = Math.min(currentInternalHeatCapacity * (targetTemperature - currentInternalTemperature), MAX_JOULES_PER_TICK);
                if (energyToAdd > 0) {
                    kelvin.modHeatEnergy(nodePos, energyToAdd);
                }
            }

            if (!blockState.getValue(SelfPrimingCoalBurnerBlock.LIT))
                level.setBlock(blockPos, blockState.setValue(CoalBurnerBlock.LIT, true), 15);

        } else {
            if (storedFuelStack.isEmpty() && blockState.getValue(SelfPrimingCoalBurnerBlock.LIT))
                level.setBlock(blockPos, blockState.setValue(CoalBurnerBlock.LIT, false), 15);

            if (!storedFuelStack.isEmpty()) {
                int burnTime = FuelRegistry.get(storedFuelStack);
                fuelTicks += burnTime;
                maxBurnTime = (double) burnTime;

                if (storedFuelStack.getItem().hasCraftingRemainingItem()) {
                    var remaining = storedFuelStack.getItem().getCraftingRemainingItem();
                    if (remainingItemStack.isEmpty()) {
                        remainingItemStack = new ItemStack(remaining);
                    } else if (remainingItemStack.getItem().equals(remaining) &&
                            remainingItemStack.getCount() + 1 <= remaining.getMaxStackSize()) {
                        remainingItemStack.grow(1);
                    } else {
                        ItemEntity dropped = new ItemEntity(level,
                                blockPos.getX() + 0.5,
                                blockPos.getY() + 1,
                                blockPos.getZ() + 0.5,
                                new ItemStack(remaining));
                        dropped.setDefaultPickUpDelay();
                        dropped.setDeltaMovement(0.0, 0.25, 0.0);
                        level.addFreshEntity(dropped);
                    }
                }
                storedFuelStack.shrink(1);
                sendData();
            }
        }
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {}

    @Override
    public DuctNodePos getDuctNodePosition() {
        if (level != null) {
            return KelvinExtensions.toDuctNodePos(blockPos, level.dimension().location());
        }
        return KelvinExtensions.toDuctNodePos(blockPos);
    }

    @Override
    public void read(CompoundTag tag, boolean clientPacket) {
        Tag subTag = tag.get("StoredFuelStack");
        storedFuelStack = (subTag == null) ? ItemStack.EMPTY : ItemStack.of((CompoundTag) subTag);

        Tag remainingTag = tag.get("RemainingItemStack");
        remainingItemStack = (remainingTag == null) ? ItemStack.EMPTY : ItemStack.of((CompoundTag) remainingTag);

        fuelTicks = tag.getInt("FuelTicks");
        maxBurnTime = tag.getDouble("MaxBurnTime");

        super.read(tag, clientPacket);
    }

    @Override
    public void write(CompoundTag tag, boolean clientPacket) {
        CompoundTag subTag = new CompoundTag();
        storedFuelStack.save(subTag);
        CompoundTag remainingTag = new CompoundTag();
        remainingItemStack.save(remainingTag);

        tag.put("StoredFuelStack", subTag);
        tag.put("RemainingItemStack", remainingTag);
        tag.putInt("FuelTicks", fuelTicks);
        tag.putDouble("MaxBurnTime", maxBurnTime);

        super.write(tag, clientPacket);
    }

    @Override
    public void destroy() {
        var vec3d = JOMLUtils.toJOMLD(blockPos);
        level.addFreshEntity(new ItemEntity(level, vec3d.x, vec3d.y, vec3d.z, storedFuelStack));
        level.addFreshEntity(new ItemEntity(level, vec3d.x, vec3d.y, vec3d.z, remainingItemStack));
        super.destroy();
    }

    @Override
    public void clearContent() {
        storedFuelStack = ItemStack.EMPTY;
        remainingItemStack = ItemStack.EMPTY;
    }



    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        if (!storedFuelStack.isEmpty() || !remainingItemStack.isEmpty()) {
            tooltip.add(Component.literal("    Coal burner Info").withStyle(ChatFormatting.GRAY));
            if (!storedFuelStack.isEmpty()) {
                tooltip.add(Component.literal("Fuel: ").withStyle(ChatFormatting.GOLD)
                        .append(storedFuelStack.getDisplayName())
                        .append(Component.literal("x " + storedFuelStack.getCount()).withStyle(ChatFormatting.GOLD)));
            }
            if (!remainingItemStack.isEmpty()) {
                tooltip.add(Component.literal("Remaining: ").withStyle(ChatFormatting.GOLD)
                        .append(remainingItemStack.getDisplayName())
                        .append(Component.literal("x " + remainingItemStack.getCount()).withStyle(ChatFormatting.GOLD)));
            }
        }
        return super.addToGoggleTooltip(tooltip, isPlayerSneaking);
    }


    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[]{side == Direction.DOWN ? 1 : 0};
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStack, Direction direction) {
        return direction != Direction.DOWN && FuelRegistry.get(itemStack) > 0 && index == 0;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return direction == Direction.DOWN && index == 1;
    }

    @Override
    public int getContainerSize() {
        return 2;
    }

    @Override
    public boolean isEmpty() {
        return storedFuelStack.isEmpty() && remainingItemStack.isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        return slot == 0 ? storedFuelStack : remainingItemStack;
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        if (remainingItemStack.isEmpty()) return ItemStack.EMPTY;
        ItemStack result = remainingItemStack.split(amount);
        if (remainingItemStack.isEmpty()) remainingItemStack = ItemStack.EMPTY;
        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack result = remainingItemStack.copy();
        remainingItemStack = ItemStack.EMPTY;
        return result;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if (slot != 0) return;
        storedFuelStack = stack;
    }

    @Override
    public boolean stillValid(Player player) {
        return level.getBlockEntity(this.worldPosition) == this;
    }

    @Override
    public void sync(NonNullList<ItemStack> storage) {
        storedFuelStack = storage.get(0);
        remainingItemStack = storage.get(1);
    }

    @Override
    public NonNullList<ItemStack> getStorageInventory() {
        NonNullList<ItemStack> list = NonNullList.withSize(2, ItemStack.EMPTY);
        list.set(0, storedFuelStack);
        list.set(1, remainingItemStack);
        return list;
    }

    @Override
    public int getStorageInventorySize() {
        return 1;
    }

    @Override
    public BlockPos getBlockPositionFromISS() {
        return this.worldPosition;
    }
}
