package com.github.guyapooye.clockworkadditions.util;

import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.content.contraptions.glue.SuperGlueEntity;
import com.simibubi.create.foundation.utility.UniqueLinkedList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.valkyrienskies.core.util.datastructures.DenseBlockPosSet;

import java.util.HashSet;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

public final class GlueAssembler {
    @NotNull
    public static final GlueAssembler INSTANCE = new GlueAssembler();

    private GlueAssembler() {
    }

    @Nullable
    public DenseBlockPosSet collectGlued(@NotNull Level level, @NotNull BlockPos pos) throws Throwable {
        Set toRemove = new HashSet();
        DenseBlockPosSet result = new DenseBlockPosSet();
        Queue frontier = new UniqueLinkedList();
        if (level.getBlockState(pos).isAir()) {
            return null;
        } else {
            frontier.add(pos);

            for(int limit = 100000; 0 < limit; --limit) {
                if (frontier.isEmpty()) {
                    if (result.isEmpty()) {
                        throw new AssemblyException(Component.literal("No blocks found!"));
                    }

                    return result;
                }

                this.visitBlock(level, frontier, result, toRemove);
            }

            toRemove.forEach(GlueAssembler::collectGlued$lambda$0);
            AssemblyException var10000 = AssemblyException.structureTooLarge();
            throw var10000;
        }
    }

    private void visitBlock(Level level, Queue<BlockPos> frontier, DenseBlockPosSet visited, Set<SuperGlueEntity> cache) {
        Object var10000 = frontier.poll();
        Objects.requireNonNull(var10000);
        BlockPos pos = (BlockPos)var10000;
        visited.add(pos.getX(), pos.getY(), pos.getZ());
        Direction[] var6 = Direction.values();
        int var7 = 0;

        for(int var8 = var6.length; var7 < var8; ++var7) {
            Direction direction = var6[var7];
            Objects.requireNonNull(cache, "null cannot be cast to non-null type java.util.Set<com.simibubi.create.content.contraptions.glue.SuperGlueEntity>");
            if (SuperGlueEntity.isGlued(level, pos, direction, cache)) {
                BlockPos newPos = pos.relative(direction);
                if (!visited.contains(newPos.getX(), newPos.getY(), newPos.getZ())) {
                    BlockState state = level.getBlockState(newPos);
                    Objects.requireNonNull(state);
                    if (this.isAllowed(state) && !state.isAir()) {
                        frontier.add(newPos);
                    }
                }
            }
        }

    }

    private boolean isAllowed(BlockState state) {
        return true;
    }

    private static void collectGlued$lambda$0(@NotNull Object obj) {
        Entity entity = (Entity) obj;
        entity.discard();
    }
}
