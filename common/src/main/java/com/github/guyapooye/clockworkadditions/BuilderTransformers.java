package com.github.guyapooye.clockworkadditions;

import com.simibubi.create.Create;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

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
}
