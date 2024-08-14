package com.github.guyapooye.clockworkadditions.blocks.copycats.wingalikes;

import com.github.guyapooye.clockworkadditions.blocks.copycats.ICopycatBlock;
import kotlin.NoWhenBranchMatchedException;
import kotlin.jvm.internal.Intrinsics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import org.valkyrienskies.clockwork.util.blocktype.ConnectedWingAlike;
import org.valkyrienskies.core.api.ships.Wing;
import org.valkyrienskies.mod.common.block.WingBlock;

public abstract class CopycatFlapBlock extends ConnectedWingAlike implements WingBlock, ICopycatBlock {
    public CopycatFlapBlock(@Nullable BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Nullable
    public BlockState getNewState(BlockState state, Level level, BlockPos pos) {
        Direction facing = state.getValue(ConnectedWingAlike.Companion.getFACING());
        BlockState north = level.getBlockState(pos.north());
        BlockState south = level.getBlockState(pos.south());
        BlockState east = level.getBlockState(pos.east());
        BlockState west = level.getBlockState(pos.west());
        BlockState up = level.getBlockState(pos.above());
        BlockState down = level.getBlockState(pos.below());

        return switch (CopycatWingBlock.WhenMappings.$EnumSwitchMapping$0[facing.ordinal()]) {
            case 1, 2 ->
                    state.setValue(ConnectedWingAlike.Companion.getNORTH(), false)
                            .setValue(ConnectedWingAlike.Companion.getSOUTH(), false)
                            .setValue(ConnectedWingAlike.Companion.getEAST(), east.getBlock() instanceof CopycatFlapBlock && east.getValue(ConnectedWingAlike.Companion.getFACING()) == facing)
                            .setValue(ConnectedWingAlike.Companion.getWEST(), west.getBlock() instanceof CopycatFlapBlock && west.getValue(ConnectedWingAlike.Companion.getFACING()) == facing)
                            .setValue(ConnectedWingAlike.Companion.getUP(), up.getBlock() instanceof CopycatFlapBlock && up.getValue(ConnectedWingAlike.Companion.getFACING()) == facing)
                            .setValue(ConnectedWingAlike.Companion.getDOWN(), down.getBlock() instanceof CopycatFlapBlock && down.getValue(ConnectedWingAlike.Companion.getFACING()) == facing)
                            .setValue(ConnectedWingAlike.Companion.getFACING(), Direction.NORTH);
            case 3, 4 ->
                    state.setValue(ConnectedWingAlike.Companion.getNORTH(), north.getBlock() instanceof CopycatFlapBlock && north.getValue(ConnectedWingAlike.Companion.getFACING()) == facing)
                            .setValue(ConnectedWingAlike.Companion.getSOUTH(), south.getBlock() instanceof CopycatFlapBlock && south.getValue(ConnectedWingAlike.Companion.getFACING()) == facing)
                            .setValue(ConnectedWingAlike.Companion.getEAST(), false)
                            .setValue(ConnectedWingAlike.Companion.getWEST(), false)
                            .setValue(ConnectedWingAlike.Companion.getUP(), up.getBlock() instanceof CopycatFlapBlock && up.getValue(ConnectedWingAlike.Companion.getFACING()) == facing)
                            .setValue(ConnectedWingAlike.Companion.getDOWN(), down.getBlock() instanceof CopycatFlapBlock && down.getValue(ConnectedWingAlike.Companion.getFACING()) == facing)
                            .setValue( ConnectedWingAlike.Companion.getFACING(), Direction.EAST);
            case 5, 6 ->
                    state.setValue(ConnectedWingAlike.Companion.getNORTH(), north.getBlock() instanceof CopycatFlapBlock && north.getValue(ConnectedWingAlike.Companion.getFACING()) == facing)
                            .setValue(ConnectedWingAlike.Companion.getSOUTH(), south.getBlock() instanceof CopycatFlapBlock && south.getValue(ConnectedWingAlike.Companion.getFACING()) == facing)
                            .setValue(ConnectedWingAlike.Companion.getEAST(), east.getBlock() instanceof CopycatFlapBlock && east.getValue(ConnectedWingAlike.Companion.getFACING()) == facing)
                            .setValue(ConnectedWingAlike.Companion.getWEST(), west.getBlock() instanceof CopycatFlapBlock && west.getValue(ConnectedWingAlike.Companion.getFACING()) == facing)
                            .setValue(ConnectedWingAlike.Companion.getUP(), false)
                            .setValue(ConnectedWingAlike.Companion.getDOWN(), false)
                            .setValue(ConnectedWingAlike.Companion.getFACING(), Direction.UP);
            default -> throw new NoWhenBranchMatchedException();
        };
    }
    @Override
    public InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, Player player, @NotNull InteractionHand hand,
                                 BlockHitResult ray) {
        return ICopycatBlock.super.use(state,level,pos,player,hand,ray);
    }

    @NotNull
    public Wing getWing(@Nullable Level level, @Nullable BlockPos pos, @NotNull BlockState blockState) {
        Intrinsics.checkNotNullParameter(blockState, "blockState");
        double wingPower = 150.0;
        double wingDrag = 150.0;
        double wingBreakingForce = 10.0;
        Direction var10000 = blockState.getValue(ConnectedWingAlike.Companion.getFACING());

        return switch (WhenMappings.$EnumSwitchMapping$0[var10000.ordinal()]) {
            case 1, 2 ->
                    new Wing(new Vector3d(0.0, 0.0, 1.0), wingPower, wingDrag, wingBreakingForce, 0.0);
            case 3, 4 ->
                    new Wing(new Vector3d(1.0, 0.0, 0.0), wingPower, wingDrag, wingBreakingForce, 0.0);
            case 5, 6 ->
                    new Wing(new Vector3d(0.0, 1.0, 0.0), wingPower, wingDrag, wingBreakingForce, 0.0);
            default -> throw new NoWhenBranchMatchedException();
        };
    }



    public static class WhenMappings {
        // $FF: synthetic field
        public static final int[] $EnumSwitchMapping$0;

        static {
            int[] var0 = new int[Direction.values().length];

            try {
                var0[Direction.NORTH.ordinal()] = 1;
                var0[Direction.SOUTH.ordinal()] = 2;
                var0[Direction.EAST.ordinal()] = 3;
                var0[Direction.WEST.ordinal()] = 4;
                var0[Direction.UP.ordinal()] = 5;
                var0[Direction.DOWN.ordinal()] = 6;
            } catch (NoSuchFieldError ignored) {
            }

            $EnumSwitchMapping$0 = var0;
        }
    }

}
