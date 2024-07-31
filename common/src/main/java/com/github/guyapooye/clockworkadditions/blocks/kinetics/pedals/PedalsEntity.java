package com.github.guyapooye.clockworkadditions.blocks.kinetics.pedals;

import com.github.guyapooye.clockworkadditions.registries.EntityRegistry;
import com.github.guyapooye.clockworkadditions.util.PedalsInputKey;
import com.simibubi.create.content.contraptions.actors.seat.SeatEntity;
import com.simibubi.create.infrastructure.config.AllConfigs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.valkyrienskies.clockwork.platform.SharedValues;
import org.valkyrienskies.core.api.ships.Ship;
import org.valkyrienskies.mod.common.VSGameUtilsKt;
import org.valkyrienskies.mod.common.entity.handling.WorldEntityHandler;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static java.lang.Math.abs;

public class PedalsEntity extends SeatEntity {

    protected Set<PedalsInputKey> prevKeys = new HashSet<>();

    public PedalsEntity(@Nullable EntityType type, @NotNull Level level) {
        super(type, level);
    }
    public void handlePosSet() {
        final Ship ship = VSGameUtilsKt.getShipManagingPos(level, this.getX(), this.getY(), this.getZ());
        if (ship != null) {
            WorldEntityHandler.INSTANCE.moveEntityFromShipyardToWorld(this, ship, this.getX(), this.getY(), this.getZ());
        }
    }
//    @Override
//    public @NotNull Vec3 position() {
//        final EntityDraggingInformation draggingInformation = ((IEntityDraggingInformationProvider) this).getDraggingInformation();
//        if (draggingInformation.isEntityBeingDraggedByAShip()) {
//            final Long shipStandingOnId = draggingInformation.getLastShipStoodOn();
//            if (shipStandingOnId != null) {
//                final Ship ship = VSGameUtilsKt.getShipObjectWorld(level).getLoadedShips().getById(shipStandingOnId);
//                if (ship != null) {
//                    final Vector3dc posInShip = ship.getTransform().getWorldToShip()
//                            .transformPosition(getX(), getY(), getZ(), new Vector3d());
//                    return new Vec3(posInShip.x(), posInShip.y(), posInShip.z());
//                }
//            }
//        }
//        return super.position();
//    }

    public void tick() {
        Entity passenger = getFirstPassenger();
        if (level.isClientSide) {
            if (passenger instanceof LocalPlayer) {

                checkKeybinds();
            }
        } else {
            boolean blockPresent = level.getBlockState(this.blockPosition()).getBlock() instanceof PedalsBlock;
            BlockEntity be = level.getBlockEntity(this.blockPosition());
            if (this.isVehicle() && blockPresent) {
                if (passenger instanceof Player) ((Player)passenger).causeFoodExhaustion(abs(((PedalsBlockEntity) be).getSpeed())/8 * AllConfigs.server().kinetics.crankHungerMultiplier.getF());
                return;
            }

            discard();
            PedalsBlockEntity var10000 = be instanceof PedalsBlockEntity ? (PedalsBlockEntity)be : null;
            if ((be instanceof PedalsBlockEntity ? (PedalsBlockEntity)be : null) != null) {
                var10000.updateInput(new HashSet<>());
            }
        }
    }
//    @Override
//    public @NotNull Vec3 getDismountLocationForPassenger(final @NotNull LivingEntity livingEntity) {
//        if (VSGameUtilsKt.isBlockInShipyard(level, position()) && VSGameUtilsKt.getShipManagingPos(level, position()) == null) {
//            // Don't teleport to the ship if we can't find the ship
//            return livingEntity.position();
//        }
//        return super.getDismountLocationForPassenger(livingEntity).add(0, 0.5f, 0);
//    }

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
