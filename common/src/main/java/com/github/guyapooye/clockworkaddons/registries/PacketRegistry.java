package com.github.guyapooye.clockworkaddons.registries;

import com.github.guyapooye.clockworkaddons.blocks.kinetics.pedals.PedalsDrivingPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.packs.repository.Pack;
import org.valkyrienskies.clockwork.platform.SharedValues;
import org.valkyrienskies.clockwork.platform.api.network.CWPacket;
import org.valkyrienskies.clockwork.platform.api.network.PacketChannel;
import org.valkyrienskies.clockwork.platform.fabric.PacketChannelImpl;

import java.util.function.Function;

public class PacketRegistry {

    public static PacketEntry<PedalsDrivingPacket> PEDALS_DRIVING = PacketEntry.register(PedalsDrivingPacket.class,PedalsDrivingPacket::new);

    private record PacketEntry<T extends CWPacket>(Class<T> type, Function<FriendlyByteBuf, T> factory) {

        private static <R extends CWPacket> PacketEntry<R> register(Class<R> type, Function<FriendlyByteBuf, R> factory) {
                PacketEntry<R> entry = new PacketEntry<>(type, factory);
                PacketChannel var10000 = SharedValues.getPacketChannel();
                Class<R> var10001 = entry.type;
                Function<FriendlyByteBuf, R> var10002 = entry.factory;
                var10000.registerPacket(var10001, var10002);
                return entry;

            }
        }
//
//    public final void sendToNear(@Nonnull Level world, @Nonnull BlockPos pos, int range, @Nonnull S2CCWPacket message) {
//        PacketChannel var10000 = SharedValues.getPacketChannel();
//        var10000.sendToNear(world, pos, range, message);
//    }
//
//    public final void sendToServer(@Nonnull C2SCWPacket packet) {
//        PacketChannel var10000 = SharedValues.getPacketChannel();
//        var10000.sendToServer(packet);
//    }
//
//    public final void sendToClientsTracking(@Nonnull S2CCWPacket packet, @Nonnull Entity entity) {
//        PacketChannel var10000 = SharedValues.getPacketChannel();
//        var10000.sendToClientsTracking(packet, entity);
//    }
//
//    public final void sendToClientsTrackingAndSelf(@Nonnull S2CCWPacket packet, @Nonnull ServerPlayer player) {
//        PacketChannel var10000 = SharedValues.getPacketChannel();
//        var10000.sendToClientsTrackingAndSelf(packet, player);
//    }
//
//    public final void sendTo(@Nonnull S2CCWPacket packet, @Nonnull ServerPlayer player) {
//        PacketChannel var10000 = SharedValues.getPacketChannel();
//        var10000.sendTo(packet, player);
//    }

    public static void register() {
    }
}
