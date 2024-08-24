package com.github.guyapooye.clockworkadditions;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.Create;
import com.simibubi.create.content.decoration.encasing.EncasedCTBehaviour;
import com.simibubi.create.content.kinetics.base.AbstractEncasedShaftBlock;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Supplier;

import static com.simibubi.create.foundation.data.BlockStateGen.axisBlock;

public class BuilderTransformers {
    public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> flapBearing() {
//        for (int i = 0; i < 20; i++) {
//            System.out.println("HELP HELP HELP HELP HELP HELP HELP");
//        }
        ResourceLocation baseBlockModelLocation = ClockworkAdditions.asResource("block/kinetic_flap_bearing/block");
        ResourceLocation baseItemModelLocation = ClockworkAdditions.asResource("block/kinetic_flap_bearing/item");
//        ResourceLocation blockModelLocation = ClockworkAdditions.asResource("block/kinetic_flap_bearing");
//        ResourceLocation itemModelLocation = ClockworkAdditions.asResource("item/kinetic_flap_bearing");

        ResourceLocation sideTextureLocation = Create.asResource("block/clockwork_bearing_side");
        ResourceLocation topTextureLocation = ClockworkAdditions.asResource("block/kinetic_flap_bearing_top");
        ResourceLocation brassTextureLocation = Create.asResource("block/brass_gearbox");
        return b -> b.initialProperties(SharedProperties::stone)
                .properties(BlockBehaviour.Properties::noOcclusion)
                .blockstate((c, p) -> p.directionalBlock(c.get(),
                        p.models()
                        .withExistingParent(c.getName(), baseBlockModelLocation)
                        .texture("top", topTextureLocation)
                        .texture("side", sideTextureLocation)
                        .texture("particle", brassTextureLocation)))
                .item()
                .model((c, p) -> p
                        .withExistingParent(c.getName(), baseItemModelLocation)
                        .texture("top", topTextureLocation)
                        .texture("side", sideTextureLocation)
                        .texture("particle", brassTextureLocation))
                .build();
    }
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
    @ExpectPlatform
    private static <B extends RotatedPillarKineticBlock, P> BlockBuilder<B, P> encasedBase(BlockBuilder<B, P> b,
                                                                                           Supplier<ItemLike> drop) {
        throw new AssertionError();
    }
    @ExpectPlatform
    public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> copycat() {
        throw new AssertionError();
    }
}
