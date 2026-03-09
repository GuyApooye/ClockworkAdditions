package com.guyapooye.clockworkadditions.registries;

import com.guyapooye.clockworkadditions.blocks.kinetics.pedals.PedalsBlock;
import com.simibubi.create.AllTags;
import com.simibubi.create.api.stress.BlockStressValues;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import static com.guyapooye.clockworkadditions.ClockworkAdditions.REGISTRATE;
import static com.simibubi.create.foundation.data.TagGen.axeOnly;


public class BlockRegistry {
//  public static final BlockEntry<KineticFlapBearingBlock> KINETIC_FLAP_BEARING =
//            REGISTRATE.block("kinetic_flap_bearing", KineticFlapBearingBlock::new)
//                    .properties(p -> p.mapColor(MapColor.TERRACOTTA_BROWN))
//                    .transform(axeOrPickaxe())
//                    .initialProperties(SharedProperties::stone)
//                    .properties(BlockBehaviour.Properties::noOcclusion)
////                    .transform(BuilderTransformers.flapBearing())
//                    .transform(BlockStressDefaults.setImpact(4.0))
//                    .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
////                    .properties(BlockBehaviour.Properties::noOcclusion)
//                    .blockstate(BlockStateGen.directionalBlockProvider(false))
//                    .simpleItem()
//                    .register();
    public static final BlockEntry<PedalsBlock> PEDALS =
            REGISTRATE.block("mechanical_pedals", PedalsBlock::new)
                    .initialProperties(SharedProperties::stone)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
                    .simpleItem()
                    .lang("Mechanical Pedals")
                    .blockstate(BlockStateGen.horizontalBlockProvider(true))
                    .register();
//    public static final BlockEntry<ExtensibleHoseBlock> EXTENSIBLE_HOSE =
//            REGISTRATE.block("extensible_hose", ExtensibleHoseBlock::new)
//                    .initialProperties(SharedProperties::stone)
//                    .properties(BlockBehaviour.Properties::noOcclusion)
//                    .addLayer(() -> RenderType::cutoutMipped)
//                    .blockstate(BlockStateGen.horizontalBlockProvider(true))
//                    .simpleItem()
//                    .lang("Extensible Hose")
//                    .register();

    public static void register() {
    }
}
