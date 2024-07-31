package com.github.guyapooye.clockworkadditions.registries;

import com.github.guyapooye.clockworkadditions.BuilderTransformers;

import com.github.guyapooye.clockworkadditions.ClockworkAdditions;
import com.github.guyapooye.clockworkadditions.blocks.bearings.flap.KineticFlapBearingBlock;
import com.github.guyapooye.clockworkadditions.blocks.bearings.heli.PhysicsBearingBlock;
import com.github.guyapooye.clockworkadditions.blocks.bearings.heli.archived.BasePhysicsBearingBlock;
import com.github.guyapooye.clockworkadditions.blocks.kinetics.pedals.PedalsBlock;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MaterialColor;

import static com.github.guyapooye.clockworkadditions.ClockworkAdditions.REGISTRATE;
import static com.simibubi.create.foundation.data.TagGen.axeOnly;
import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;


public class BlockRegistry {
    static {
        REGISTRATE.creativeModeTab(() -> ClockworkAdditions.CWACreativeModeTab);
    }
    public static final BlockEntry<BasePhysicsBearingBlock> BASE_PHYSICS_BEARING =
            REGISTRATE.block("base_physics_bearing", BasePhysicsBearingBlock::new)
                    .initialProperties(SharedProperties::stone)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .transform(axeOrPickaxe())
                    .addLayer(() -> RenderType::cutoutMipped)
                    .blockstate(BlockStateGen.directionalBlockProvider(true))
                    .simpleItem()
                    .lang("Helicopter Physics Bearing")
                    .register();

    public static final BlockEntry<PhysicsBearingBlock> HELI_PHYSICS_BEARING =
            REGISTRATE.block("heli_physics_bearing", PhysicsBearingBlock::new)
                    .initialProperties(SharedProperties::stone)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .transform(axeOrPickaxe())
                    .addLayer(() -> RenderType::cutoutMipped)
                    .blockstate(BlockStateGen.directionalBlockProvider(true))
                    .simpleItem()
                    .lang("Helicopter Physics Bearing")
                    .register();
    public static final BlockEntry<KineticFlapBearingBlock> KINETIC_FLAP_BEARING =
            REGISTRATE.block("kinetic_flap_bearing", KineticFlapBearingBlock::new)
                    .properties(p -> p.color(MaterialColor.TERRACOTTA_BROWN))
                    .transform(axeOrPickaxe())
                    .transform(BuilderTransformers.flapBearing())
                    .transform(BlockStressDefaults.setImpact(4.0))
                    .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
//                    .properties(BlockBehaviour.Properties::noOcclusion)
//                    .blockstate(BlockStateGen.directionalBlockProvider(false))
//                    .simpleItem()
                    .register();
    public static final BlockEntry<PedalsBlock> PEDALS =
            REGISTRATE.block("mechanical_pedals", PedalsBlock::new)
                    .initialProperties(SharedProperties::stone)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .transform(axeOnly())
                    .transform(BlockStressDefaults.setCapacity(8.0))
                    .transform(BlockStressDefaults.setGeneratorSpeed(PedalsBlock::getSpeedRange))
                    .addLayer(() -> RenderType::cutoutMipped)
                    .blockstate(BlockStateGen.horizontalBlockProvider(true))
                    .simpleItem()
                    .lang("Kinetic Pedals")
                    .register();
    public static void register() {}
}
