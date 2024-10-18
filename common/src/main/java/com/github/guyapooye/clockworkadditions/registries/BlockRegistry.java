package com.github.guyapooye.clockworkadditions.registries;

import com.github.guyapooye.clockworkadditions.blocks.bearings.flap.KineticFlapBearingBlock;
import com.github.guyapooye.clockworkadditions.blocks.bearings.heli.PhysicsBearingBlock;
import com.github.guyapooye.clockworkadditions.blocks.bearings.heli.archived.BasePhysicsBearingBlock;
import com.github.guyapooye.clockworkadditions.blocks.copycats.wingalikes.CopycatFlapBlock;
import com.github.guyapooye.clockworkadditions.blocks.copycats.wingalikes.CopycatWingBlock;
import com.github.guyapooye.clockworkadditions.blocks.fluids.extensiblehose.ExtensibleHoseBlock;
import com.github.guyapooye.clockworkadditions.blocks.fluids.extensiblehose.ExtensibleHoseBlockEntity;
import com.github.guyapooye.clockworkadditions.blocks.kinetics.cvjoint.CVJointBlock;
import com.github.guyapooye.clockworkadditions.blocks.kinetics.handlebar.HandlebarBlock;
import com.github.guyapooye.clockworkadditions.blocks.kinetics.invertedresistor.InvertedRedstoneResistorBlock;
import com.github.guyapooye.clockworkadditions.blocks.kinetics.pedals.PedalsBlock;
import com.github.guyapooye.clockworkadditions.blocks.redstone.gyro.RedstoneGyroBlock;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.foundation.data.TagGen;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

import static com.github.guyapooye.clockworkadditions.ClockworkAdditions.REGISTRATE;
import static com.simibubi.create.foundation.data.TagGen.axeOnly;
import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;


public class BlockRegistry {
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
                    .properties(p -> p.mapColor(MapColor.TERRACOTTA_BROWN))
                    .transform(axeOrPickaxe())
                    .initialProperties(SharedProperties::stone)
                    .properties(BlockBehaviour.Properties::noOcclusion)
//                    .transform(BuilderTransformers.flapBearing())
                    .transform(BlockStressDefaults.setImpact(4.0))
                    .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
//                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .blockstate(BlockStateGen.directionalBlockProvider(false))
                    .simpleItem()
                    .register();
    public static final BlockEntry<PedalsBlock> PEDALS =
            REGISTRATE.block("mechanical_pedals", PedalsBlock::new)
                    .initialProperties(SharedProperties::stone)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .transform(axeOnly())
                    .transform(BlockStressDefaults.setCapacity(10.0))
                    .transform(BlockStressDefaults.setGeneratorSpeed(PedalsBlock::getSpeedRange))
                    .addLayer(() -> RenderType::cutoutMipped)
                    .blockstate(BlockStateGen.horizontalBlockProvider(true))
                    .simpleItem()
                    .lang("Mechanical Pedals")
                    .register();
    public static final BlockEntry<HandlebarBlock> HANDLEBAR =
            REGISTRATE.block("handlebar", HandlebarBlock::new)
                    .initialProperties(SharedProperties::stone)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .transform(axeOnly())
                    .transform(BlockStressDefaults.setCapacity(8.0))
                    .transform(BlockStressDefaults.setGeneratorSpeed(HandlebarBlock::getSpeedRange))
                    .addLayer(() -> RenderType::cutoutMipped)
                    .blockstate(BlockStateGen.horizontalBlockProvider(true))
                    .simpleItem()
                    .lang("Handlebar")
                    .register();
    public static final BlockEntry<RedstoneGyroBlock> REDSTONE_GYRO =
            REGISTRATE.block("redstone_gyro", RedstoneGyroBlock::new)
                    .properties(p -> p.mapColor(MapColor.TERRACOTTA_LIGHT_GRAY))
                    .simpleItem()
                    .register();
    public static final BlockEntry<CVJointBlock> CV_JOINT =
            REGISTRATE.block("cv_joint", CVJointBlock::new)
                    .initialProperties(SharedProperties::stone)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .addLayer(() -> RenderType::cutoutMipped)
                    .blockstate(BlockStateGen.horizontalBlockProvider(true))
                    .simpleItem()
                    .lang("Constant Velocity Joint")
                    .simpleItem()
                    .register();
    public static final BlockEntry<InvertedRedstoneResistorBlock> INVERTED_RESISTOR =
            REGISTRATE.block("inverted_redstone_resistor", InvertedRedstoneResistorBlock::new)
                    .initialProperties(SharedProperties::stone)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .transform(BlockStressDefaults.setNoImpact())
                    .transform(TagGen.axeOrPickaxe())
                    .addLayer(() -> RenderType::cutoutMipped)
                    .simpleItem()
                    .lang("Inverted Redstone Resistor")
                    .register();
    public static final BlockEntry<ExtensibleHoseBlock> EXTENSIBLE_HOSE =
            REGISTRATE.block("extensible_hose", ExtensibleHoseBlock::new)
                    .initialProperties(SharedProperties::stone)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .addLayer(() -> RenderType::cutoutMipped)
                    .blockstate(BlockStateGen.horizontalBlockProvider(true))
                    .simpleItem()
                    .lang("Extensible Hose")
                    .register();

    public static final BlockEntry<? extends CopycatWingBlock> COPYCAT_WING = registerCopycatWing();
    public static final BlockEntry<? extends CopycatFlapBlock> COPYCAT_FLAP = registerCopycatFlap();
    @ExpectPlatform
    public static BlockEntry<? extends CopycatWingBlock> registerCopycatWing() {
        throw new AssertionError();
    }
    @ExpectPlatform
    public static BlockEntry<? extends CopycatFlapBlock> registerCopycatFlap() {
        throw new AssertionError();
    }
    public static void register() {
    }
}
