package com.github.guyapooye.clockworkaddons.registries;

import com.github.guyapooye.clockworkaddons.BuilderTransformers;
import com.github.guyapooye.clockworkaddons.CWACreativeModeTab;

import com.github.guyapooye.clockworkaddons.blocks.bearings.flap.KineticFlapBearingBlock;
import com.github.guyapooye.clockworkaddons.blocks.bearings.heli.PhysicsBearingBlock;
import com.github.guyapooye.clockworkaddons.blocks.bearings.heli.archived.BasePhysicsBearingBlock;
import com.github.guyapooye.clockworkaddons.blocks.kinetics.pedals.PedalsBlock;
import com.simibubi.create.AllTags;
import com.simibubi.create.Create;
import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.content.kinetics.crank.HandCrankBlock;
import com.simibubi.create.content.redstone.displayLink.source.EntityNameDisplaySource;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.utility.DyeHelper;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MaterialColor;

import static com.github.guyapooye.clockworkaddons.ClockworkAddons.REGISTRATE;
import static com.simibubi.create.AllInteractionBehaviours.interactionBehaviour;
import static com.simibubi.create.AllMovementBehaviours.movementBehaviour;
import static com.simibubi.create.content.redstone.displayLink.AllDisplayBehaviours.assignDataBehaviour;
import static com.simibubi.create.foundation.data.TagGen.axeOnly;
import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;


public class BlockRegistry {
    static {
        REGISTRATE.creativeModeTab(() -> CWACreativeModeTab.CWACreativeModeTab);
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
            REGISTRATE.block("pedals", PedalsBlock::new)
                    .initialProperties(SharedProperties::stone)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .transform(axeOnly())
                    .transform(BlockStressDefaults.setCapacity(4.0))
                    .transform(BlockStressDefaults.setGeneratorSpeed(PedalsBlock::getSpeedRange))
                    .addLayer(() -> RenderType::cutoutMipped)
                    .blockstate(BlockStateGen.horizontalBlockProvider(true))
                    .simpleItem()
                    .lang("Pedals")
                    .register();
    public static void register() {}
}
