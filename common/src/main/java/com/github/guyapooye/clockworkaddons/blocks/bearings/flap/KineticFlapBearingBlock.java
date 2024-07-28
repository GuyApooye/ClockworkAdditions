package com.github.guyapooye.clockworkaddons.blocks.bearings.flap;

import com.github.guyapooye.clockworkaddons.registries.BlockEntityRegistry;
import com.simibubi.create.content.contraptions.bearing.BearingBlock;
import com.simibubi.create.foundation.block.IBE;
import kotlin.jvm.internal.Intrinsics;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class KineticFlapBearingBlock extends BearingBlock implements IBE<KineticFlapBearingBlockEntity> {
    public KineticFlapBearingBlock(@Nullable BlockBehaviour.Properties properties) {
        super(properties);
    }

    protected void createBlockStateDefinition(@NotNull StateDefinition.Builder builder) {
        super.createBlockStateDefinition(builder);
    }

    @NotNull
    public Class getBlockEntityClass() {
        return KineticFlapBearingBlockEntity.class;
    }

    @NotNull
    public InteractionResult use(@NotNull BlockState state, @NotNull Level worldIn, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand handIn, @NotNull BlockHitResult hit) {
        if (!player.mayBuild()) {
            return InteractionResult.FAIL;
        } else if (player.isShiftKeyDown()) {
            return InteractionResult.FAIL;
        } else if (player.getItemInHand(handIn).isEmpty()) {
            if (!worldIn.isClientSide) {
                this.withBlockEntityDo(worldIn, pos, KineticFlapBearingBlock::use);
            }

            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }

    private static void use(KineticFlapBearingBlockEntity te) {
        if (te.isRunning()) {
            te.disassemble();
        } else {
            te.setAssembleNextTick(true);
        }
    }

    @NotNull
    public BlockEntityType<KineticFlapBearingBlockEntity> getBlockEntityType() {
        return BlockEntityRegistry.KINETIC_FLAP_BEARING.get();
    }

    @NotNull
    public InteractionResult onWrenched(@NotNull BlockState state, @NotNull UseOnContext context) {
        Intrinsics.checkNotNullParameter(state, "state");
        Intrinsics.checkNotNullParameter(context, "context");
        InteractionResult resultType = super.onWrenched(state, context);
        if (!context.getLevel().isClientSide && resultType.consumesAction()) {
            this.withBlockEntityDo(context.getLevel(), context.getClickedPos(), KineticFlapBearingBlockEntity::disassemble);
        }

        Intrinsics.checkNotNull(resultType);
        return resultType;
    }


}
