package com.github.guyapooye.clockworkadditions.blocks.fluids.extensiblehose;

import com.github.guyapooye.clockworkadditions.registries.BlockRegistry;
import com.github.guyapooye.clockworkadditions.registries.ConfigRegistry;
import com.github.guyapooye.clockworkadditions.util.PlatformUtil;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

import static com.github.guyapooye.clockworkadditions.util.WorldspaceUtil.getWorldSpace;

public abstract class ExtensibleHoseBlockEntity<Tank> extends SmartBlockEntity {

    public BlockPos target;
    @Environment(EnvType.CLIENT)
    public boolean isOrigin;
    public Tank tank;

    public ExtensibleHoseBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        isOrigin = compound.getBoolean("isOrigin");
        target = NbtUtils.readBlockPos(compound.getCompound("target"));
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        if (target == null) return;
        compound.putBoolean("isOrigin", isOrigin);
        compound.put("target", NbtUtils.writeBlockPos(target));
    }

    public void detach() {
        target = null;
        tank = null;
    }

    public abstract Tank createTank();

    public void attach(BlockPos pos) {
        if (pos.equals(target)) return;
        if (target != null) {
            ExtensibleHoseBlockEntity old = BlockRegistry.EXTENSIBLE_HOSE.get().getBlockEntity(level, target);
            if (old != null) {
                old.detach();
            }
            detach();
        }
        target = pos;
        ExtensibleHoseBlockEntity targ = BlockRegistry.EXTENSIBLE_HOSE.get().getBlockEntity(level, target);
        if (targ == null) return;
        detach();
        target = pos;
        tank = createTank();
        targ.tank = tank;
        targ.attach(getBlockPos());

    }

    @Override
    public void tick() {
        super.tick();
        if (target == null) {
            detach();
            return;
        }
        ExtensibleHoseBlockEntity other = BlockRegistry.EXTENSIBLE_HOSE.get().getBlockEntity(level, target);
        if (other == null) {
            if (level.isLoaded(target))
                detach();
            return;
        }
        if (isOrigin == other.isOrigin) isOrigin = !other.isOrigin;
        PlatformUtil.runWhenOn(EnvType.SERVER,() -> {
            if (getWorldSpace(this).sub(getWorldSpace(other)).lengthSquared() > Math.pow(ConfigRegistry.server().stretchables.hoseMaxLength.get(),2)) {
                target = null;
                other.target = null;
            }
        });
    }
}
