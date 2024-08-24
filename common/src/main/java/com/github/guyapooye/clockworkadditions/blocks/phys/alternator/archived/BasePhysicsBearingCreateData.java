package com.github.guyapooye.clockworkadditions.blocks.phys.alternator.archived;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3dc;
import org.valkyrienskies.core.apigame.constraints.VSConstraintAndId;

import java.util.Objects;

public class BasePhysicsBearingCreateData {
    @NotNull
    private final Vector3dc bearingPos;
    @NotNull
    private final Vector3dc bearingAxis;
    private final double bearingAngle;
    private final float bearingRPM;
    private final boolean locked;
    private final long shiptraptionID;
    @NotNull
    private final VSConstraintAndId constraint;
    @NotNull
    private final VSConstraintAndId hingeConstraint;
    @Nullable
    private final VSConstraintAndId posDampConstraint;
    @Nullable
    private final VSConstraintAndId rotDampConstraint;
    @Nullable
    private final VSConstraintAndId secondAttachment;

    public BasePhysicsBearingCreateData(@NotNull Vector3dc bearingPos, @NotNull Vector3dc bearingAxis, double bearingAngle, float bearingRPM, boolean locked, long shiptraptionID, @NotNull VSConstraintAndId constraint, @NotNull VSConstraintAndId hingeConstraint, @Nullable VSConstraintAndId posDampConstraint, @Nullable VSConstraintAndId rotDampConstraint, @Nullable VSConstraintAndId secondAttachment) {
        super();
        this.bearingPos = bearingPos;
        this.bearingAxis = bearingAxis;
        this.bearingAngle = bearingAngle;
        this.bearingRPM = bearingRPM;
        this.locked = locked;
        this.shiptraptionID = shiptraptionID;
        this.constraint = constraint;
        this.hingeConstraint = hingeConstraint;
        this.posDampConstraint = posDampConstraint;
        this.rotDampConstraint = rotDampConstraint;
        this.secondAttachment = secondAttachment;
    }

    // $FF: synthetic method
    public BasePhysicsBearingCreateData(Vector3dc var1, Vector3dc var2, double var3, float var5, boolean var6, long var7, VSConstraintAndId var9, VSConstraintAndId var10, VSConstraintAndId var11, VSConstraintAndId var12, VSConstraintAndId var13, int var14) {
        this(var1, var2, var3, var5, var6, var7, var9, var10, var11, var12, (var14 & 1024) != 0 ? null : var13);
    }

    @NotNull
    public final Vector3dc getBearingPos() {
        return this.bearingPos;
    }

    @NotNull
    public final Vector3dc getBearingAxis() {
        return this.bearingAxis;
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

    public final long getShiptraptionID() {
        return this.shiptraptionID;
    }

    @NotNull
    public final VSConstraintAndId getConstraint() {
        return this.constraint;
    }

    @NotNull
    public final VSConstraintAndId getHingeConstraint() {
        return this.hingeConstraint;
    }

    @Nullable
    public final VSConstraintAndId getPosDampConstraint() {
        return this.posDampConstraint;
    }

    @Nullable
    public final VSConstraintAndId getRotDampConstraint() {
        return this.rotDampConstraint;
    }

    @Nullable
    public final VSConstraintAndId getSecondAttachment() {
        return this.secondAttachment;
    }

    @NotNull
    public final Vector3dc component1() {
        return this.bearingPos;
    }

    @NotNull
    public final Vector3dc component2() {
        return this.bearingAxis;
    }

    public final double component3() {
        return this.bearingAngle;
    }

    public final float component4() {
        return this.bearingRPM;
    }

    public final boolean component5() {
        return this.locked;
    }

    public final long component6() {
        return this.shiptraptionID;
    }

    @NotNull
    public final VSConstraintAndId component7() {
        return this.constraint;
    }

    @NotNull
    public final VSConstraintAndId component8() {
        return this.hingeConstraint;
    }

    @Nullable
    public final VSConstraintAndId component9() {
        return this.posDampConstraint;
    }

    @Nullable
    public final VSConstraintAndId component10() {
        return this.rotDampConstraint;
    }

    @Nullable
    public final VSConstraintAndId component11() {
        return this.secondAttachment;
    }

    @NotNull
    public final BasePhysicsBearingCreateData copy(@NotNull Vector3dc bearingPos, @NotNull Vector3dc bearingAxis, double bearingAngle, float bearingRPM, boolean locked, long shiptraptionID, @NotNull VSConstraintAndId constraint, @NotNull VSConstraintAndId hingeConstraint, @Nullable VSConstraintAndId posDampConstraint, @Nullable VSConstraintAndId rotDampConstraint, @Nullable VSConstraintAndId secondAttachment) {
        return new BasePhysicsBearingCreateData(bearingPos, bearingAxis, bearingAngle, bearingRPM, locked, shiptraptionID, constraint, hingeConstraint, posDampConstraint, rotDampConstraint, secondAttachment);
    }

    // $FF: synthetic method
    public static BasePhysicsBearingCreateData copy$default(BasePhysicsBearingCreateData var0, Vector3dc var1, Vector3dc var2, double var3, float var5, boolean var6, long var7, VSConstraintAndId var9, VSConstraintAndId var10, VSConstraintAndId var11, VSConstraintAndId var12, VSConstraintAndId var13, int var14, Object var15) {
        if ((var14 & 1) != 0) {
            var1 = var0.getBearingPos();
        }

        if ((var14 & 2) != 0) {
            var2 = var0.getBearingAxis();
        }

        if ((var14 & 4) != 0) {
            var3 = var0.getBearingAngle();
        }

        if ((var14 & 8) != 0) {
            var5 = var0.getBearingRPM();
        }

        if ((var14 & 16) != 0) {
            var6 = var0.getLocked();
        }

        if ((var14 & 32) != 0) {
            var7 = var0.getShiptraptionID();
        }

        if ((var14 & 64) != 0) {
            var9 = var0.getConstraint();
        }

        if ((var14 & 128) != 0) {
            var10 = var0.getHingeConstraint();
        }

        if ((var14 & 256) != 0) {
            var11 = var0.getPosDampConstraint();
        }

        if ((var14 & 512) != 0) {
            var12 = var0.getRotDampConstraint();
        }

        if ((var14 & 1024) != 0) {
            var13 = var0.getSecondAttachment();
        }

        return var0.copy(var1, var2, var3, var5, var6, var7, var9, var10, var11, var12, var13);
    }

    @NotNull
    public String toString() {
        return "BasePhysicsBearingCreateData(bearingPos=" + this.bearingPos + ", bearingAxis=" + this.bearingAxis + ", bearingAngle=" + this.bearingAngle + ", bearingRPM=" + this.bearingRPM + ", locked=" + this.locked + ", shiptraptionID=" + this.shiptraptionID + ", constraint=" + this.constraint + ", hingeConstraint=" + this.hingeConstraint + ", posDampConstraint=" + this.posDampConstraint + ", rotDampConstraint=" + this.rotDampConstraint + ", secondAttachment=" + this.secondAttachment + ")";
    }

    public int hashCode() {
        int result = this.bearingPos.hashCode();
        result = result * 31 + this.bearingAxis.hashCode();
        result = result * 31 + Double.hashCode(this.bearingAngle);
        result = result * 31 + Float.hashCode(this.bearingRPM);
        int var10000 = result * 31;
        byte var10001 = (byte) (this.locked ? 1 : 0);
        if (var10001 != 0) {
            var10001 = 1;
        }

        result = var10000 + var10001;
        result = result * 31 + Long.hashCode(this.shiptraptionID);
        result = result * 31 + this.constraint.hashCode();
        result = result * 31 + this.hingeConstraint.hashCode();
        result = result * 31 + (this.posDampConstraint == null ? 0 : this.posDampConstraint.hashCode());
        result = result * 31 + (this.rotDampConstraint == null ? 0 : this.rotDampConstraint.hashCode());
        result = result * 31 + (this.secondAttachment == null ? 0 : this.secondAttachment.hashCode());
        return result;
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        } else if (!(other instanceof BasePhysicsBearingCreateData)) {
            return false;
        } else {
            BasePhysicsBearingCreateData var2 = (BasePhysicsBearingCreateData)other;
            if (!Objects.equals(this.bearingPos, var2.getBearingPos())) {
                return false;
            } else if (!Objects.equals(this.bearingAxis, var2.getBearingAxis())) {
                return false;
            } else if (Double.compare(this.bearingAngle, var2.getBearingAngle()) != 0) {
                return false;
            } else if (Float.compare(this.bearingRPM, var2.getBearingRPM()) != 0) {
                return false;
            } else if (this.locked != var2.getLocked()) {
                return false;
            } else if (this.shiptraptionID != var2.getShiptraptionID()) {
                return false;
            } else if (!Objects.equals(this.constraint, var2.getConstraint())) {
                return false;
            } else if (!Objects.equals(this.hingeConstraint, var2.getHingeConstraint())) {
                return false;
            } else if (!Objects.equals(this.posDampConstraint, var2.getPosDampConstraint())) {
                return false;
            } else if (!Objects.equals(this.rotDampConstraint, var2.getRotDampConstraint())) {
                return false;
            } else {
                return Objects.equals(this.secondAttachment, var2.getSecondAttachment());
            }
        }
    }
}
