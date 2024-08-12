package com.github.guyapooye.clockworkadditions.blocks.redstone.gyro;

import org.valkyrienskies.core.api.ships.ServerShip;

public class RedstoneGyroAttachment {
    private int amount = 0;
    public void decrement() {
        amount--;
    }
    public void increment() {
        amount++;
    }
    public int getAmount() {
        return amount;
    }
    public static RedstoneGyroAttachment getOrCreate(ServerShip ship) {
        RedstoneGyroAttachment attachment = ship.getAttachment(RedstoneGyroAttachment.class);
        if (attachment == null) {
            RedstoneGyroAttachment newAttachment = new RedstoneGyroAttachment();
            ship.saveAttachment(RedstoneGyroAttachment.class,newAttachment);
            return newAttachment;
        };
        return attachment;
    }
}
