package com.github.guyapooye.clockworkadditions;

import com.simibubi.create.content.kinetics.base.AbstractEncasedShaftBlock;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;


public class BuilderTransformers {
    @ExpectPlatform
    public static <B extends AbstractEncasedShaftBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> encasedShaft(String casing,
                                                                                                                 Supplier<CTSpriteShiftEntry> casingShift) {
        throw new AssertionError();
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
