package com.github.guyapooye.clockworkaddons.registries;

import com.github.guyapooye.clockworkaddons.blocks.bearings.flap.KineticFlapBearingBlockEntity;
import com.github.guyapooye.clockworkaddons.blocks.bearings.flap.KineticFlapBearingRenderer;
import com.github.guyapooye.clockworkaddons.blocks.bearings.heli.PhysicsBearingBlockEntity;
import com.github.guyapooye.clockworkaddons.blocks.bearings.heli.archived.BasePhysicsBearingBlockEntity;
import com.github.guyapooye.clockworkaddons.blocks.kinetics.pedals.PedalsBlockEntity;
import com.simibubi.create.content.contraptions.bearing.BearingInstance;
import com.simibubi.create.content.contraptions.bearing.BearingRenderer;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

import static com.github.guyapooye.clockworkaddons.ClockworkAddons.REGISTRATE;

public class BlockEntityRegistry {
    public static BlockEntityEntry<BasePhysicsBearingBlockEntity> BASE_PHYS_BEARING = REGISTRATE
            .blockEntity("base_physics_bearing", BasePhysicsBearingBlockEntity::new)
            .instance(() -> BearingInstance::new)
            .validBlocks(BlockRegistry.BASE_PHYSICS_BEARING)
            .renderer(() -> BearingRenderer::new)
            .register();

    public static BlockEntityEntry<PhysicsBearingBlockEntity> HELI_PHYS_BEARING = REGISTRATE
            .blockEntity("heli_physics_bearing", PhysicsBearingBlockEntity::new)
            .instance(() -> BearingInstance::new)
            .validBlocks(BlockRegistry.HELI_PHYSICS_BEARING)
            .renderer(() -> BearingRenderer::new)
            .register();
    public static BlockEntityEntry<KineticFlapBearingBlockEntity> KINETIC_FLAP_BEARING = REGISTRATE
            .blockEntity("kinetic_flap_bearing", KineticFlapBearingBlockEntity::new)
            .validBlocks(BlockRegistry.KINETIC_FLAP_BEARING)
            .renderer(() -> KineticFlapBearingRenderer::new)
            .register();
    public static BlockEntityEntry<PedalsBlockEntity> PEDALS = REGISTRATE
            .blockEntity("pedals", PedalsBlockEntity::new)
            .validBlocks(BlockRegistry.PEDALS)
            .renderer(() -> KineticBlockEntityRenderer::new)
            .register();

    public static void register() {}
}
