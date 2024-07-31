package com.github.guyapooye.clockworkadditions.registries;

import com.github.guyapooye.clockworkadditions.blocks.bearings.flap.KineticFlapBearingBlockEntity;
import com.github.guyapooye.clockworkadditions.blocks.bearings.flap.KineticFlapBearingRenderer;
import com.github.guyapooye.clockworkadditions.blocks.bearings.heli.PhysicsBearingBlockEntity;
import com.github.guyapooye.clockworkadditions.blocks.bearings.heli.archived.BasePhysicsBearingBlockEntity;
import com.github.guyapooye.clockworkadditions.blocks.kinetics.pedals.PedalsBlockEntity;
import com.github.guyapooye.clockworkadditions.blocks.kinetics.pedals.PedalsInstance;
import com.github.guyapooye.clockworkadditions.blocks.kinetics.pedals.PedalsRenderer;
import com.simibubi.create.content.contraptions.bearing.BearingInstance;
import com.simibubi.create.content.contraptions.bearing.BearingRenderer;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

import static com.github.guyapooye.clockworkadditions.ClockworkAdditions.REGISTRATE;

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
            .blockEntity("mechanical_pedals", PedalsBlockEntity::new)
            .instance(() -> PedalsInstance::new)
            .validBlocks(BlockRegistry.PEDALS)
            .renderer(() -> PedalsRenderer::new)
            .register();

    public static void register() {}
}
