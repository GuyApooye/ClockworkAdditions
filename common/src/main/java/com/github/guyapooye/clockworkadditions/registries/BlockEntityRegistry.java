package com.github.guyapooye.clockworkadditions.registries;

import com.github.guyapooye.clockworkadditions.blocks.bearings.flap.KineticFlapBearingBlockEntity;
import com.github.guyapooye.clockworkadditions.blocks.bearings.flap.KineticFlapBearingInstance;
import com.github.guyapooye.clockworkadditions.blocks.bearings.flap.KineticFlapBearingRenderer;
import com.github.guyapooye.clockworkadditions.blocks.kinetics.cvjoint.CVJointBlockEntity;
import com.github.guyapooye.clockworkadditions.blocks.kinetics.cvjoint.CVJointInstance;
import com.github.guyapooye.clockworkadditions.blocks.kinetics.cvjoint.CVJointRenderer;
import com.github.guyapooye.clockworkadditions.blocks.kinetics.handlebar.HandlebarBlockEntity;
import com.github.guyapooye.clockworkadditions.blocks.kinetics.handlebar.HandlebarInstance;
import com.github.guyapooye.clockworkadditions.blocks.kinetics.pedals.PedalsBlockEntity;
import com.github.guyapooye.clockworkadditions.blocks.kinetics.pedals.PedalsInstance;
import com.github.guyapooye.clockworkadditions.blocks.kinetics.pedals.PedalsRenderer;
import com.github.guyapooye.clockworkadditions.blocks.phys.alternator.AlternatorBearingBlockEntity;
import com.github.guyapooye.clockworkadditions.blocks.phys.alternator.archived.BasePhysicsBearingBlockEntity;
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

    public static final BlockEntityEntry<AlternatorBearingBlockEntity> ALTERNATOR_BEARING = REGISTRATE
            .blockEntity("kinetic_alternator", AlternatorBearingBlockEntity::new)
            .validBlocks(BlockRegistry.ALTERNATOR_BEARING)
            .register();
    public static final BlockEntityEntry<KineticFlapBearingBlockEntity> KINETIC_FLAP_BEARING = REGISTRATE
            .blockEntity("kinetic_flap_bearing", KineticFlapBearingBlockEntity::new)
            .instance(() -> KineticFlapBearingInstance::new)
            .validBlocks(BlockRegistry.KINETIC_FLAP_BEARING)
            .renderer(() -> KineticFlapBearingRenderer::new)
            .register();
    public static final BlockEntityEntry<PedalsBlockEntity> PEDALS = REGISTRATE
            .blockEntity("mechanical_pedals", PedalsBlockEntity::new)
            .instance(() -> PedalsInstance::new)
            .validBlocks(BlockRegistry.PEDALS)
            .renderer(() -> PedalsRenderer::new)
            .register();
    public static final BlockEntityEntry<? extends HandlebarBlockEntity> HANDLEBAR = REGISTRATE
            .blockEntity("handlebar", HandlebarBlockEntity::new)
            .instance(() -> HandlebarInstance::new)
            .validBlocks(BlockRegistry.HANDLEBAR)
            .register();;
    public static final BlockEntityEntry<RedstoneGyroBlockEntity> REDSTONE_GYRO = REGISTRATE
            .blockEntity("redstone_gyro", RedstoneGyroBlockEntity::new)
            .validBlocks(BlockRegistry.REDSTONE_GYRO)
            .register();
    public static final BlockEntityEntry<CVJointBlockEntity> CV_JOINT = REGISTRATE
            .blockEntity("cv_joint", CVJointBlockEntity::new)
            .instance(() -> CVJointInstance::new)
            .validBlocks(BlockRegistry.CV_JOINT)
            .renderer(() -> CVJointRenderer::new)
            .register();

    public static void register() {}
}
