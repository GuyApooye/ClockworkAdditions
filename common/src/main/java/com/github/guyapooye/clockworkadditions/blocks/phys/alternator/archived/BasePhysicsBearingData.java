package com.github.guyapooye.clockworkadditions.blocks.phys.alternator.archived;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3dc;
import org.valkyrienskies.core.apigame.constraints.*;

import java.util.Objects;

public class BasePhysicsBearingData {
    @Nullable
    private final Vector3dc bearingPosition;
    @Nullable
    private final Vector3dc bearingAxis;
    private double bearingAngle;
    private float bearingRPM;
    private boolean locked;
    private long shiptraptionID;
    private boolean aligning;
    @Nullable
    private VSAttachmentConstraint attachConstraint;
    @JsonIgnore
    @Nullable
    private Integer attachID;
    @Nullable
    private VSHingeOrientationConstraint hingeConstraint;
    @Nullable
    private VSFixedOrientationConstraint angleConstraint;
    @JsonIgnore
    @Nullable
    private Integer hingeID;
    @Nullable
    private VSAttachmentConstraint secondAttachConstraint;
    @JsonIgnore
    @Nullable
    private Integer secondAttachId;

    @Nullable
    public final Vector3dc getBearingPosition() {
        return this.bearingPosition;
    }

    @Nullable
    public final Vector3dc getBearingAxis() {
        return this.bearingAxis;
    }

    public final double getBearingAngle() {
        return this.bearingAngle;
    }

    public final void setBearingAngle(double val1) {
        this.bearingAngle = val1;
    }

    public final float getBearingRPM() {
        return this.bearingRPM;
    }

    public final void setBearingRPM(float val1) {
        this.bearingRPM = val1;
    }

    public final boolean getLocked() {
        return this.locked;
    }

    public final void setLocked(boolean val1) {
        this.locked = val1;
    }

    public final long getShiptraptionID() {
        return this.shiptraptionID;
    }

    public final void setShiptraptionID(long val1) {
        this.shiptraptionID = val1;
    }

    @Nullable
    public final VSAttachmentConstraint getAttachConstraint() {
        return this.attachConstraint;
    }

    public final void setAttachConstraint(@Nullable VSAttachmentConstraint val1) {
        this.attachConstraint = val1;
    }

    @Nullable
    public final Integer getAttachID() {
        return this.attachID;
    }

    public final void setAttachID(@Nullable Integer val1) {
        this.attachID = val1;
    }

    @Nullable
    public final VSHingeOrientationConstraint getHingeConstraint() {
        return this.hingeConstraint;
    }

    public final void setHingeConstraint(@Nullable VSHingeOrientationConstraint val1) {
        this.hingeConstraint = val1;
    }

    @Nullable
    public final VSFixedOrientationConstraint getAngleConstraint() {
        return this.angleConstraint;
    }

    public final void setAngleConstraint(@Nullable VSFixedOrientationConstraint val1) {
        this.angleConstraint = val1;
    }

    @Nullable
    public final Integer getHingeID() {
        return this.hingeID;
    }

    public final void setHingeID(@Nullable Integer val1) {
        this.hingeID = val1;
    }

    @Nullable
    public final VSAttachmentConstraint getSecondAttachConstraint() {
        return this.secondAttachConstraint;
    }

    public final void setSecondAttachConstraint(@Nullable VSAttachmentConstraint val1) {
        this.secondAttachConstraint = val1;
    }

    @Nullable
    public final Integer getSecondAttachId() {
        return this.secondAttachId;
    }

    public final void setSecondAttachId(@Nullable Integer val1) {
        this.secondAttachId = val1;
    }

    /** @deprecated */
    @Deprecated
    public BasePhysicsBearingData() {
        this.bearingPosition = null;
        this.bearingAxis = null;
        this.shiptraptionID = -1L;
    }

    public BasePhysicsBearingData(@Nullable Vector3dc bearingPosition, @Nullable Vector3dc bearingAxis, double bearingAngle, float bearingRPM, boolean locked, long shiptraptionID, @NotNull VSConstraintAndId constraintAndId, @NotNull VSConstraintAndId hingeConstraintAndId, @Nullable VSConstraintAndId posDampConstraintAndId, @Nullable VSConstraintAndId rotDampConstraintAndId, @Nullable VSConstraintAndId secondAttachment) {
        super();
        this.bearingPosition = bearingPosition;
        this.bearingAxis = bearingAxis;
        this.bearingAngle = bearingAngle;
        this.bearingRPM = bearingRPM;
        this.locked = locked;
        this.shiptraptionID = shiptraptionID;
        VSConstraint var10001 = constraintAndId.getVsConstraint();
        Objects.requireNonNull(var10001, "null cannot be cast to non-null type org.valkyrienskies.core.apigame.constraints.VSAttachmentConstraint");
        this.attachConstraint = (VSAttachmentConstraint)var10001;
        this.attachID = constraintAndId.getConstraintId();
        var10001 = hingeConstraintAndId.getVsConstraint();
        Objects.requireNonNull(var10001, "null cannot be cast to non-null type org.valkyrienskies.core.apigame.constraints.VSHingeOrientationConstraint");
        this.hingeConstraint = (VSHingeOrientationConstraint)var10001;
        this.hingeID = hingeConstraintAndId.getConstraintId();
        this.secondAttachConstraint = (VSAttachmentConstraint)(secondAttachment != null ? secondAttachment.getVsConstraint() : null);
        this.secondAttachId = secondAttachment != null ? secondAttachment.getConstraintId() : null;
    }

    public final void setAligning(boolean yn) {
        this.aligning = yn;
    }
}
