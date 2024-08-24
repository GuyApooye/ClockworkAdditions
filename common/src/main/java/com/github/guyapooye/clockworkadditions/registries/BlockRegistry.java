package com.github.guyapooye.clockworkadditions.registries;

import com.github.guyapooye.clockworkadditions.ClockworkAdditions;
import com.github.guyapooye.clockworkadditions.blocks.bearings.flap.KineticFlapBearingBlock;
import com.github.guyapooye.clockworkadditions.blocks.copycats.wingalikes.CopycatFlapBlock;
import com.github.guyapooye.clockworkadditions.blocks.copycats.wingalikes.CopycatWingBlock;
import com.github.guyapooye.clockworkadditions.blocks.kinetics.cvjoint.CVJointBlock;
import com.github.guyapooye.clockworkadditions.blocks.kinetics.handlebar.HandlebarBlock;
import com.github.guyapooye.clockworkadditions.blocks.kinetics.pedals.PedalsBlock;
import com.github.guyapooye.clockworkadditions.blocks.phys.alternator.AlternatorBearingBlock;
import com.github.guyapooye.clockworkadditions.blocks.phys.alternator.archived.BasePhysicsBearingBlock;
import com.github.guyapooye.clockworkadditions.blocks.redstone.gyro.RedstoneGyroBlock;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MaterialColor;

import static com.github.guyapooye.clockworkadditions.ClockworkAdditions.REGISTRATE;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
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

    public static final BlockEntry<AlternatorBearingBlock> ALTERNATOR_BEARING =
            REGISTRATE.block("kinetic_alternator", AlternatorBearingBlock::new)
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
                    .properties(p -> p.color(MaterialColor.TERRACOTTA_LIGHT_GRAY))
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
