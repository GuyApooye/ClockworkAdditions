package com.github.guyapooye.clockworkadditions.blocks.copycats.wingalikes;

import com.simibubi.create.content.decoration.copycat.CopycatPanelBlock;
import kotlin.jvm.internal.Intrinsics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.valkyrienskies.clockwork.ClockworkShapes;
import org.valkyrienskies.clockwork.util.blocktype.ConnectedWingAlike;
import org.valkyrienskies.core.api.ships.Wing;
import org.valkyrienskies.mod.common.block.WingBlock;
import org.valkyrienskies.mod.common.util.VectorConversionsMCKt;

public abstract class CopycatFlapBlock extends CopycatPanelBlock implements WingBlock {
    public CopycatFlapBlock(@Nullable BlockBehaviour.Properties properties) {
        super(properties);
    }
    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return ClockworkShapes.INSTANCE.getWING().get(pState.getValue(FACING).getAxis());
    }

    @NotNull
    public Wing getWing(@Nullable Level level, @Nullable BlockPos pos, @NotNull BlockState blockState) {
        Intrinsics.checkNotNullParameter(blockState, "blockState");
        double wingPower = 150.0;
        double wingDrag = 150.0;
        double wingBreakingForce = 10.0;
        Direction var10000 = blockState.getValue(ConnectedWingAlike.Companion.getFACING());

        return new Wing(VectorConversionsMCKt.toJOMLD(blockState.getValue(FACING).getNormal()).absolute(), wingPower, wingDrag, wingBreakingForce, 0.0);
    }
}
