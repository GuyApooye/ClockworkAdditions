package com.guyapooye.clockworkadditions.registries;

import com.guyapooye.clockworkadditions.blocks.gas.SelfPrimingCoalBurnerBlockEntity;
import com.guyapooye.clockworkadditions.blocks.kinetics.pedals.PedalsBlockEntity;
import com.guyapooye.clockworkadditions.blocks.kinetics.pedals.PedalsVisual;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

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

    public static final BlockEntityEntry<SelfPrimingCoalBurnerBlockEntity> SELF_PRIMING_COAL_BURNER = REGISTRATE
            .blockEntity("self_priming_coal_burner", SelfPrimingCoalBurnerBlockEntity::new)
            .validBlocks(BlockRegistry.SELF_PRIMING_COAL_BURNER)
            .register();

    public static void register() {
    }
}
