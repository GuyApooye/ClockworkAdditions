package com.guyapooye.clockworkadditions.blocks.gas;

import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import dev.architectury.registry.fuel.FuelRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Clearable;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3d;
import org.valkyrienskies.clockwork.ClockworkMod;
import org.valkyrienskies.clockwork.ClockworkPackets;
import org.valkyrienskies.clockwork.util.kelvin.KNodeBlockEntity;
import org.valkyrienskies.kelvin.util.GasPhysics;
import org.valkyrienskies.clockwork.util.blocktype.ISyncableStorage;
import org.valkyrienskies.clockwork.util.blocktype.SyncableStoragePacket;
import org.valkyrienskies.kelvin.api.DuctNodePos;
import org.valkyrienskies.kelvin.util.KelvinExtensions;

import java.util.List;

public class SelfPrimingCoalBurnerBlockEntity extends KNodeBlockEntity implements Clearable, ISyncableStorage {

    private int fuelTicks = 0;
    private double maxBurnTime = 0.0;

    protected ItemStack storedFuelStack = ItemStack.EMPTY;
    protected ItemStack remainingItemStack = ItemStack.EMPTY;
    private int previousTotalItems = 0;

    public static final double MAX_JOULES_PER_TICK = 10000.0;

    public SelfPrimingCoalBurnerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }


    @Override
    public void tick() {
        super.tick();

        if (level != null && level.isClientSide) return;

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
        DuctNodePos nodePos = KelvinExtensions.INSTANCE.toDuctNodePos(getBlockPositionFromISS(), level.dimension().location());
        if (kelvin.getNodeAt(nodePos) == null) return;

        BlockState blockState = getBlockState();

        if (fuelTicks > 0) {
            fuelTicks -= 1;
            var currentInternalGasses = kelvin.getGasMassAt(nodePos);
            double currentInternalTemperature = kelvin.getTemperatureAt(nodePos);
            if (currentInternalGasses.values().stream().mapToDouble(Double::doubleValue).sum() > 1e-5) {
                double currentInternalHeatCapacity = GasPhysics.INSTANCE.mixtureCapacity(currentInternalGasses);
                double targetTemperature = 850.0;
                double energyToAdd = Math.min(currentInternalHeatCapacity * (targetTemperature - currentInternalTemperature), MAX_JOULES_PER_TICK);
                if (energyToAdd > 0) {
                    kelvin.modHeatEnergy(nodePos, energyToAdd);
                }
            }

            if (!blockState.getValue(SelfPrimingCoalBurnerBlock.LIT))
                level.setBlock(getBlockPos(), blockState.setValue(SelfPrimingCoalBurnerBlock.LIT, true), 15);

        } else {
            if (storedFuelStack.isEmpty() && blockState.getValue(SelfPrimingCoalBurnerBlock.LIT))
                level.setBlock(getBlockPos(), blockState.setValue(SelfPrimingCoalBurnerBlock.LIT, false), 15);

            if (!storedFuelStack.isEmpty()) {
                int burnTime = FuelRegistry.get(storedFuelStack);
                fuelTicks += burnTime;
                maxBurnTime = burnTime;

                if (storedFuelStack.getItem().hasCraftingRemainingItem()) {
                    var remaining = storedFuelStack.getItem().getCraftingRemainingItem();
                    if (remainingItemStack.isEmpty()) {
                        remainingItemStack = new ItemStack(remaining);
                    } else if (remainingItemStack.getItem().equals(remaining) &&
                            remainingItemStack.getCount() + 1 <= remaining.getMaxStackSize()) {
                        remainingItemStack.grow(1);
                    } else {
                        ItemEntity dropped = new ItemEntity(level,
                                getBlockPos().getX() + 0.5,
                                getBlockPos().getY() + 1,
                                getBlockPos().getZ() + 0.5,
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
            return KelvinExtensions.INSTANCE.toDuctNodePos(getBlockPos(), level.dimension().location());
        }
        return KelvinExtensions.INSTANCE.toDuctNodePos(getBlockPos(), level.dimension().location());
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
        BlockPos blockPos = getBlockPos();
        Vector3d vec3d = new Vector3d(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        if (level != null) {
            level.addFreshEntity(new ItemEntity(level, vec3d.x, vec3d.y, vec3d.z, storedFuelStack));
        level.addFreshEntity(new ItemEntity(level, vec3d.x, vec3d.y, vec3d.z, remainingItemStack));
        }
        super.destroy();
    }

    @Override
    public void clearContent() {
        storedFuelStack = ItemStack.EMPTY;
        remainingItemStack = ItemStack.EMPTY;
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
