package com.github.guyapooye.clockworkaddons.blocks.kinetics.pedals;

import com.github.guyapooye.clockworkaddons.registries.EntityRegistry;
import com.github.guyapooye.clockworkaddons.util.PedalsInputKey;
import com.simibubi.create.content.contraptions.actors.seat.SeatEntity;
import dev.architectury.injectables.annotations.ExpectPlatform;
import kotlin.Metadata;
import kotlin.collections.SetsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.valkyrienskies.clockwork.ClockworkEntities;
import org.valkyrienskies.clockwork.ClockworkPackets;
import org.valkyrienskies.clockwork.content.kinetics.sequenced_seat.*;
import org.valkyrienskies.clockwork.platform.SharedValues;
import org.valkyrienskies.clockwork.platform.api.network.C2SCWPacket;
import org.valkyrienskies.clockwork.platform.api.network.PacketChannel;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class PedalsEntity extends SeatEntity {

    protected Set<PedalsInputKey> prevKeys = new HashSet<>();

    public PedalsEntity(@Nullable EntityType type, @Nullable Level level) {
        super(type, level);
    }
    public PedalsEntity(Level world, BlockPos pos) {
        super(world,pos);
    }

    public void tick() {
        if (this.level.isClientSide) {
            if (this.getFirstPassenger() instanceof LocalPlayer) {
                this.checkKeybinds();
            }
        } else {
            boolean blockPresent = this.level.getBlockState(this.blockPosition()).getBlock() instanceof PedalsBlock;
            if (this.isVehicle() && blockPresent) {
                return;
            }

            this.discard();
            BlockEntity be = this.level.getBlockEntity(this.blockPosition());
            PedalsBlockEntity var10000 = be instanceof PedalsBlockEntity ? (PedalsBlockEntity)be : null;
            if ((be instanceof PedalsBlockEntity ? (PedalsBlockEntity)be : null) != null) {
                var10000.updateInput(new HashSet<>());
            }
        }

    }

    private void checkKeybinds() {
        Options options = Minecraft.getInstance().options;

        Set<PedalsInputKey> keys = new HashSet<>();
        if (options.keyUp.isDown()) {
            keys.add(PedalsInputKey.FORWARD);
        }

        if (options.keyDown.isDown()) {
            keys.add(PedalsInputKey.BACKWARD);
        }

        if (options.keySprint.isDown()) {
            keys.add(PedalsInputKey.SPRINT);
        }

        if (!Objects.equals(keys, this.prevKeys)) {
            this.sendUpdate(keys);
        }

        this.prevKeys = keys;
    }

    private void sendUpdate(Set<PedalsInputKey> keys) {
        SharedValues.getPacketChannel().sendToServer(new PedalsDrivingPacket(this.getId(), keys));
//        ClockworkPackets.Companion.sendToServer();
    }

    @NotNull
    public static PedalsEntity create(@NotNull Level level, @NotNull BlockPos pos) {
        return Objects.requireNonNull(EntityRegistry.PEDALS.create(level));
    }
    public static class Render extends EntityRenderer<PedalsEntity> {

        public Render(EntityRendererProvider.Context context) {
            super(context);
        }

        @Override
        public boolean shouldRender(PedalsEntity p_225626_1_, Frustum p_225626_2_, double p_225626_3_, double p_225626_5_,
                                    double p_225626_7_) {
            return false;
        }

        @Override
        public ResourceLocation getTextureLocation(PedalsEntity p_110775_1_) {
            return null;
        }
    }
}
