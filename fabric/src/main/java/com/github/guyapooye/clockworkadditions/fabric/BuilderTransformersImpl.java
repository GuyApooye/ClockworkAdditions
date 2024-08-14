package com.github.guyapooye.clockworkadditions.fabric;

import com.github.guyapooye.clockworkadditions.BuilderTransformers;
import com.github.guyapooye.clockworkadditions.blocks.copycats.ICopycatBlock;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.decoration.encasing.EncasedCTBehaviour;
import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.content.kinetics.base.AbstractEncasedShaftBlock;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.foundation.data.TagGen;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

import java.util.function.Supplier;

import static com.simibubi.create.foundation.data.BlockStateGen.axisBlock;

public class BuilderTransformersImpl extends BuilderTransformers {
    public static <B extends AbstractEncasedShaftBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> encasedShaft(String casing,
                                                                                                                 Supplier<CTSpriteShiftEntry> casingShift) {
        return builder -> encasedBase(builder, () -> AllBlocks.SHAFT.get())
                .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCTBehaviour(casingShift.get())))
                .onRegister(CreateRegistrate.casingConnectivity((block, cc) -> cc.make(block, casingShift.get(),
                        (s, f) -> f.getAxis() != s.getValue(AbstractEncasedShaftBlock.AXIS))))
                .blockstate((c, p) -> axisBlock(c, p, blockState -> p.models()
                        .getExistingFile(p.modLoc("block/encased_shaft/block_" + casing)), true))
                .item()
                .model(AssetLookup.customBlockItemModel("encased_shaft", "item_" + casing))
                .build();
    }
    public static <B extends RotatedPillarKineticBlock, P> BlockBuilder<B, P> encasedBase(BlockBuilder<B, P> b,
                                                                                           Supplier<ItemLike> drop) {
        return b.initialProperties(SharedProperties::stone)
                .properties(BlockBehaviour.Properties::noOcclusion)
                .transform(BlockStressDefaults.setNoImpact())
                .loot((p, lb) -> p.dropOther(lb, drop.get()));
    }
    public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> copycat() {
        return b -> b.initialProperties(SharedProperties::softMetal)
                .blockstate((c, p) -> p.simpleBlock(c.get(), p.models()
                        .getExistingFile(p.mcLoc("air"))))
                .initialProperties(SharedProperties::softMetal)
                .properties(p -> p.noOcclusion()
                        .mapColor(MapColor.NONE))
                // fabric: only render base model on cutout. When rendering the wrapped model's material is copied.
                .addLayer(() -> RenderType::cutout)
                .color(() -> ICopycatBlock::wrappedColor)
                .transform(TagGen.axeOrPickaxe());
    }
}
