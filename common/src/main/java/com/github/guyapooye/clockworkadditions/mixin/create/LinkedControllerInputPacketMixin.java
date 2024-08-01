package com.github.guyapooye.clockworkadditions.mixin.create;

import com.simibubi.create.content.redstone.link.controller.LecternControllerBlockEntity;
import com.simibubi.create.content.redstone.link.controller.LinkedControllerInputPacket;
import com.simibubi.create.content.redstone.link.controller.LinkedControllerItem;
import com.simibubi.create.content.redstone.link.controller.LinkedControllerServerHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

@Mixin(LinkedControllerInputPacket.class)
public abstract class LinkedControllerInputPacketMixin {
    @Shadow
    private Collection<Integer> activatedButtons;
    @Shadow
    private boolean press;
    /**
     * @author GuyApooye
     * @reason redirect lectern controller to transmit from block and not player
     */
    @Overwrite
    protected void handleLectern(ServerPlayer player, LecternControllerBlockEntity lectern) {
        if (lectern.isUsedBy(player)) clockworkAdditions$wrapHandleItem(player,lectern);

    }
    @Unique
    protected void clockworkAdditions$wrapHandleItem(ServerPlayer player, LecternControllerBlockEntity lectern) {
        Level world = lectern.getLevel();
        UUID uniqueID = player.getUUID();
        BlockPos pos = lectern.getBlockPos();

        if (player.isSpectator() && press)
            return;

        LinkedControllerServerHandler.receivePressed(world, pos, uniqueID, activatedButtons.stream()
                .map(i -> LinkedControllerItem.toFrequency(lectern.getController(), i))
                .collect(Collectors.toList()), press);
    }
}
