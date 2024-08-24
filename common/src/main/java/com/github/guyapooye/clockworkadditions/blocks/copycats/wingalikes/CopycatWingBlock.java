package com.github.guyapooye.clockworkadditions.blocks.copycats.wingalikes;

import com.simibubi.create.content.decoration.copycat.CopycatBlock;
import com.simibubi.create.content.decoration.copycat.CopycatPanelBlock;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.valkyrienskies.clockwork.ClockworkShapes;
import org.valkyrienskies.core.api.ships.Wing;
import org.valkyrienskies.mod.common.block.WingBlock;
import org.valkyrienskies.mod.common.util.VectorConversionsMCKt;

public class CopycatWingBlock extends CopycatPanelBlock implements WingBlock {
    public CopycatWingBlock(@Nullable BlockBehaviour.Properties properties) {
        super(properties);
    }


    @Environment(EnvType.CLIENT)
    public static BlockColor wrappedColor() {
        return new CopycatBlock.WrappedBlockColor();
    }

    @Environment(EnvType.CLIENT)
    public static class WrappedBlockColor implements BlockColor {

        @Override
        public int getColor(BlockState pState, @javax.annotation.Nullable BlockAndTintGetter pLevel, @javax.annotation.Nullable BlockPos pPos,
                            int pTintIndex) {
            if (pLevel == null || pPos == null)
                return GrassColor.get(0.5D, 1.0D);
            return Minecraft.getInstance()
                    .getBlockColors()
                    .getColor(getMaterial(pLevel, pPos), pLevel, pPos, pTintIndex);
        }

    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return ClockworkShapes.INSTANCE.getWING().get(pState.getValue(FACING).getAxis());
    }

    @NotNull
    public Wing getWing(@Nullable Level level, @Nullable BlockPos pos, @NotNull BlockState blockState) {
        double wingPower = 150.0;
        double wingDrag = 150.0;
        double wingBreakingForce = 10.0;
        double wingCamberAttackingBias = Math.toRadians(10.0);

        return new Wing(VectorConversionsMCKt.toJOMLD(blockState.getValue(FACING).getNormal()).absolute(), wingPower, wingDrag, wingBreakingForce, wingCamberAttackingBias);
    }
}
