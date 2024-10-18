package com.github.guyapooye.clockworkadditions.mixin.create;

import com.github.guyapooye.clockworkadditions.registries.BlockRegistry;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.decoration.copycat.CopycatBlockEntity;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import static com.simibubi.create.Create.REGISTRATE;

@Mixin(AllBlockEntityTypes.class)
public class AllBlockEntityTypesMixin {
    @Final
    @Shadow(remap = false)
    public static BlockEntityEntry<CopycatBlockEntity> COPYCAT =
            REGISTRATE.blockEntity("copycat", CopycatBlockEntity::new)
                    .validBlocks(AllBlocks.COPYCAT_PANEL, AllBlocks.COPYCAT_STEP, BlockRegistry.COPYCAT_WING, BlockRegistry.COPYCAT_FLAP)
                    .register();
}
