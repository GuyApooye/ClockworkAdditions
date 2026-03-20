package com.guyapooye.clockworkadditions.registries;

import com.guyapooye.clockworkadditions.blocks.kinetics.pedals.PedalsBlockEntity;
import com.guyapooye.clockworkadditions.blocks.kinetics.pedals.PedalsVisual;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.OrientedRotatingVisual;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

import static com.guyapooye.clockworkadditions.ClockworkAdditions.REGISTRATE;

public class BlockEntityRegistry {
    public static final BlockEntityEntry<PedalsBlockEntity> PEDALS = REGISTRATE
            .blockEntity("mechanical_pedals", PedalsBlockEntity::new)
            .visual(() -> PedalsVisual::new)
            .validBlocks(BlockRegistry.PEDALS)
            .register();
//    public static final BlockEntityEntry<? extends HandlebarBlockEntity> HANDLEBAR = REGISTRATE
//            .blockEntity("handlebar", HandlebarBlockEntity::new)
//            .instance(() -> HandlebarInstance::new)
//            .validBlocks(BlockRegistry.HANDLEBAR)
//            .register();
//    public static final BlockEntityEntry<InvertedRedstoneResistorBlockEntity> INVERTED_RESISTOR = REGISTRATE
//            .blockEntity("inverted_redstone_resistor", InvertedRedstoneResistorBlockEntity::new)
//            .validBlocks(BlockRegistry.INVERTED_RESISTOR)
//            .renderer(() -> InvertedRedstoneResistorRenderer::new)
//            .register();

    public static void register() {
    }
}
