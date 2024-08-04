package com.github.guyapooye.clockworkadditions.mixin.create;

import com.simibubi.create.foundation.utility.ControlsUtil;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Vector;

@Mixin(ControlsUtil.class)
public abstract class ControlsUtilMixin {
    @Shadow(remap = false)
    private static Vector<KeyMapping> standardControls;

    /**
     * @author GuyApooye
     * @reason add keySprint
     */
    @Overwrite(remap = false)
    public static Vector<KeyMapping> getControls() {
        if (standardControls == null) {
            Options gameSettings = Minecraft.getInstance().options;
            standardControls = new Vector<>(7);
            standardControls.add(gameSettings.keyUp);
            standardControls.add(gameSettings.keyDown);
            standardControls.add(gameSettings.keyLeft);
            standardControls.add(gameSettings.keyRight);
            standardControls.add(gameSettings.keyJump);
            standardControls.add(gameSettings.keyShift);
            standardControls.add(gameSettings.keySprint);
        }
        return standardControls;
    }
}
