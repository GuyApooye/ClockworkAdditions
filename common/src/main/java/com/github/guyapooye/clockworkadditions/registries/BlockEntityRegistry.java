package com.github.guyapooye.clockworkadditions.registries;

import com.github.guyapooye.clockworkadditions.blocks.bearings.flap.KineticFlapBearingBlockEntity;
import com.github.guyapooye.clockworkadditions.blocks.bearings.flap.KineticFlapBearingRenderer;
import com.github.guyapooye.clockworkadditions.blocks.bearings.heli.PhysicsBearingBlockEntity;
import com.github.guyapooye.clockworkadditions.blocks.bearings.heli.archived.BasePhysicsBearingBlockEntity;
import com.github.guyapooye.clockworkadditions.blocks.copycats.CWACopycatBlockEntity;
import com.github.guyapooye.clockworkadditions.blocks.fluid.extensible_hose.ExtensibleHoseBlockEntity;
import com.github.guyapooye.clockworkadditions.blocks.fluid.extensible_hose.ExtensibleHoseInstance;
import com.github.guyapooye.clockworkadditions.blocks.kinetics.cv_joint.CVJointBlockEntity;
import com.github.guyapooye.clockworkadditions.blocks.kinetics.cv_joint.CVJointInstance;
import com.github.guyapooye.clockworkadditions.blocks.kinetics.handlebar.HandlebarBlockEntity;
import com.github.guyapooye.clockworkadditions.blocks.kinetics.pedals.PedalsBlockEntity;
import com.github.guyapooye.clockworkadditions.blocks.kinetics.pedals.PedalsInstance;
import com.github.guyapooye.clockworkadditions.blocks.kinetics.pedals.PedalsRenderer;
import com.github.guyapooye.clockworkadditions.blocks.redstone.gyro.RedstoneGyroBlockEntity;
import com.simibubi.create.content.contraptions.bearing.BearingInstance;
import com.simibubi.create.content.contraptions.bearing.BearingRenderer;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

import static com.github.guyapooye.clockworkadditions.ClockworkAdditions.REGISTRATE;

public class BlockEntityRegistry {
    public static final BlockEntityEntry<BasePhysicsBearingBlockEntity> BASE_PHYS_BEARING = REGISTRATE
            .blockEntity("base_physics_bearing", BasePhysicsBearingBlockEntity::new)
            .instance(() -> BearingInstance::new)
            .validBlocks(BlockRegistry.BASE_PHYSICS_BEARING)
            .renderer(() -> BearingRenderer::new)
            .register();

    public static final BlockEntityEntry<PhysicsBearingBlockEntity> HELI_PHYS_BEARING = REGISTRATE
            .blockEntity("heli_physics_bearing", PhysicsBearingBlockEntity::new)
            .instance(() -> BearingInstance::new)
            .validBlocks(BlockRegistry.HELI_PHYSICS_BEARING)
            .renderer(() -> BearingRenderer::new)
            .register();
    public static final BlockEntityEntry<KineticFlapBearingBlockEntity> KINETIC_FLAP_BEARING = REGISTRATE
            .blockEntity("kinetic_flap_bearing", KineticFlapBearingBlockEntity::new)
            .validBlocks(BlockRegistry.KINETIC_FLAP_BEARING)
            .renderer(() -> KineticFlapBearingRenderer::new)
            .register();
    public static final BlockEntityEntry<PedalsBlockEntity> PEDALS = REGISTRATE
            .blockEntity("mechanical_pedals", PedalsBlockEntity::new)
            .instance(() -> PedalsInstance::new)
            .validBlocks(BlockRegistry.PEDALS)
            .renderer(() -> PedalsRenderer::new)
            .register();
    public static BlockEntityEntry<? extends HandlebarBlockEntity> HANDLEBAR;
    public static final BlockEntityEntry<RedstoneGyroBlockEntity> REDSTONE_GYRO = REGISTRATE
            .blockEntity("redstone_gyro", RedstoneGyroBlockEntity::new)
            .validBlocks(BlockRegistry.REDSTONE_GYRO)
            .register();
    public static final BlockEntityEntry<CVJointBlockEntity> CV_JOINT = REGISTRATE
            .blockEntity("cv_joint", CVJointBlockEntity::new)
            .instance(() -> CVJointInstance::new)
            .validBlocks(BlockRegistry.CV_JOINT)
            //.renderer(() -> CVJointRenderer::new)
            .register();
    public static BlockEntityEntry<? extends ExtensibleHoseBlockEntity> EXTENSIBLE_HOSE;
    public static BlockEntityEntry<? extends CWACopycatBlockEntity> COPYCAT;

    public static void register() {}
}
