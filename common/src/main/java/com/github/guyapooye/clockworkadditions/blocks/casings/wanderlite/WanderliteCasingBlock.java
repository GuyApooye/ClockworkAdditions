package com.github.guyapooye.clockworkadditions.blocks.casings.wanderlite;

import com.simibubi.create.content.decoration.encasing.CasingBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.valkyrienskies.clockwork.content.curiosities.WanderShipControl;
import org.valkyrienskies.core.api.ships.ServerShip;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

public class WanderliteCasingBlock extends CasingBlock {
    public WanderliteCasingBlock(Properties p_i48440_1_) {
        super(p_i48440_1_);
    }


    public void onPlace(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (level instanceof ServerLevel) {
            ServerShip ship = VSGameUtilsKt.getShipManagingPos((ServerLevel)level, pos);
            if (ship != null) {
                WanderShipControl var7 = WanderShipControl.Companion.getOrCreate(ship);
                int var8 = var7.getAurics();
                var7.setAurics(var8 + 1);
            }
        }
    }

    public void destroy(@NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockState state) {
        if (level instanceof ServerLevel) {
            ServerShip ship = VSGameUtilsKt.getShipManagingPos((ServerLevel)level, pos);
            if (ship != null) {
                WanderShipControl var5 = WanderShipControl.Companion.getOrCreate(ship);
                int var6 = var5.getAurics();
                var5.setAurics(var6 - 1);
            }
        }

        super.destroy(level, pos, state);
    }
}
