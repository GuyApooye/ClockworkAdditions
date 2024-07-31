package com.github.guyapooye.clockworkadditions.blocks.bearings.heli.archived;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.valkyrienskies.core.apigame.constraints.VSFixedOrientationConstraint;
import org.valkyrienskies.core.apigame.constraints.VSHingeOrientationConstraint;

import java.util.Objects;

public class BasePhysicsBearingUpdateData {
    private final double bearingAngle;
    private final float bearingRPM;
    private final boolean locked;
    @Nullable
    private final VSHingeOrientationConstraint hingeConstraint;
    @Nullable
    private final VSFixedOrientationConstraint angleConstraint;

    public BasePhysicsBearingUpdateData(double bearingAngle, float bearingRPM, boolean locked, @Nullable VSHingeOrientationConstraint hingeConstraint, @Nullable VSFixedOrientationConstraint angleConstraint) {
        this.bearingAngle = bearingAngle;
        this.bearingRPM = bearingRPM;
        this.locked = locked;
        this.hingeConstraint = hingeConstraint;
        this.angleConstraint = angleConstraint;
    }

    public final double getBearingAngle() {
        return this.bearingAngle;
    }

    public final float getBearingRPM() {
        return this.bearingRPM;
    }

    public final boolean getLocked() {
        return this.locked;
    }

    @Nullable
    public final VSHingeOrientationConstraint getHingeConstraint() {
        return this.hingeConstraint;
    }

    @Nullable
    public final VSFixedOrientationConstraint getAngleConstraint() {
        return this.angleConstraint;
    }

    public final double component1() {
        return this.bearingAngle;
    }

    public final float component2() {
        return this.bearingRPM;
    }

    public final boolean component3() {
        return this.locked;
    }

    @Nullable
    public final VSHingeOrientationConstraint component4() {
        return this.hingeConstraint;
    }

    @Nullable
    public final VSFixedOrientationConstraint component5() {
        return this.angleConstraint;
    }

    @NotNull
    public final BasePhysicsBearingUpdateData copy(double bearingAngle, float bearingRPM, boolean locked, @Nullable VSHingeOrientationConstraint hingeConstraint, @Nullable VSFixedOrientationConstraint angleConstraint) {
        return new BasePhysicsBearingUpdateData(bearingAngle, bearingRPM, locked, hingeConstraint, angleConstraint);
    }

    // $FF: synthetic method
    public static BasePhysicsBearingUpdateData copy$default(BasePhysicsBearingUpdateData var0, double var1, float var3, boolean var4, VSHingeOrientationConstraint var5, VSFixedOrientationConstraint var6, int var7, Object var8) {
        if ((var7 & 1) != 0) {
            var1 = var0.bearingAngle;
        }

        if ((var7 & 2) != 0) {
            var3 = var0.bearingRPM;
        }

        if ((var7 & 4) != 0) {
            var4 = var0.locked;
        }

        if ((var7 & 8) != 0) {
            var5 = var0.hingeConstraint;
        }

        if ((var7 & 16) != 0) {
            var6 = var0.angleConstraint;
        }

        return var0.copy(var1, var3, var4, var5, var6);
    }

    @NotNull
    public String toString() {
        return "BasePhysicsBearingUpdateData(bearingAngle=" + this.bearingAngle + ", bearingRPM=" + this.bearingRPM + ", locked=" + this.locked + ", hingeConstraint=" + this.hingeConstraint + ", angleConstraint=" + this.angleConstraint + ")";
    }

    public int hashCode() {
        int result = Double.hashCode(this.bearingAngle);
        result = result * 31 + Float.hashCode(this.bearingRPM);
        int var10000 = result * 31;
        byte var10001 = (byte) (this.locked ? 1 : 0);
        if (var10001 != 0) {
            var10001 = 1;
        }

        result = var10000 + var10001;
        result = result * 31 + (this.hingeConstraint == null ? 0 : this.hingeConstraint.hashCode());
        result = result * 31 + (this.angleConstraint == null ? 0 : this.angleConstraint.hashCode());
        return result;
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        } else if (!(other instanceof BasePhysicsBearingUpdateData)) {
            return false;
        } else {
            BasePhysicsBearingUpdateData var2 = (BasePhysicsBearingUpdateData)other;
            if (Double.compare(this.bearingAngle, var2.bearingAngle) != 0) {
                return false;
            } else if (Float.compare(this.bearingRPM, var2.bearingRPM) != 0) {
                return false;
            } else if (this.locked != var2.locked) {
                return false;
            } else if (!Objects.equals(this.hingeConstraint, var2.hingeConstraint)) {
                return false;
            } else {
                return Objects.equals(this.angleConstraint, var2.angleConstraint);
            }
        }
    }
}
