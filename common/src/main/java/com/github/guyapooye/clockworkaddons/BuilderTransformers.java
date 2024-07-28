package com.github.guyapooye.clockworkaddons;

import com.google.common.collect.Maps;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import kotlin.jvm.internal.Intrinsics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import org.jetbrains.annotations.NotNull;
import org.valkyrienskies.clockwork.ClockworkMod;
import org.valkyrienskies.clockwork.util.builder.BuilderTransformersClockwork;

public class BuilderTransformers {
    public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> flapBearing() {
//        for (int i = 0; i < 20; i++) {
//            System.out.println("HELP HELP HELP HELP HELP HELP HELP");
//        }
        ResourceLocation baseBlockModelLocation = ClockworkAddons.asResource("block/kinetic_flap_bearing/block");
        ResourceLocation baseItemModelLocation = ClockworkAddons.asResource("block/kinetic_flap_bearing/item");
//        ResourceLocation blockModelLocation = ClockworkAddons.asResource("block/kinetic_flap_bearing");
//        ResourceLocation itemModelLocation = ClockworkAddons.asResource("item/kinetic_flap_bearing");

        ResourceLocation sideTextureLocation = Create.asResource("block/clockwork_bearing_side");
        ResourceLocation topTextureLocation = ClockworkAddons.asResource("block/kinetic_flap_bearing_top");
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
