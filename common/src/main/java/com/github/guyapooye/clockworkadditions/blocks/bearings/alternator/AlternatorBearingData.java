package com.github.guyapooye.clockworkadditions.blocks.bearings.alternator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3dc;
import org.valkyrienskies.core.apigame.constraints.VSAttachmentConstraint;
import org.valkyrienskies.core.apigame.constraints.VSConstraintAndId;
import org.valkyrienskies.core.apigame.constraints.VSFixedOrientationConstraint;
import org.valkyrienskies.core.apigame.constraints.VSHingeOrientationConstraint;

public class AlternatorBearingData {
    @Nullable
    private final Vector3dc bearingPosition;
    @Nullable
    private final Vector3dc bearingAxis;
    private final long shiptraptionID;
    @Nullable
    private VSAttachmentConstraint attachConstraint;
    @JsonIgnore
    @Nullable
    private int attachID;
    @Nullable
    private VSHingeOrientationConstraint hingeConstraint;
    @JsonIgnore
    @Nullable
    private int hingeID;
    /*why would you ever use this bruh*/
    @Deprecated
    public AlternatorBearingData() {

        this.bearingPosition = null;
        this.bearingAxis = null;
        this.shiptraptionID = -1L;
    }
    public AlternatorBearingData(Vector3dc bearingPosition,
                                  Vector3dc bearingAxis,
                                  long shiptraptionID,
                                  VSConstraintAndId attachConstraint,
                                  VSConstraintAndId hingeConstraint) {
        this.bearingPosition = bearingPosition;
        this.bearingAxis = bearingAxis;
        this.shiptraptionID = shiptraptionID;
        this.attachConstraint = (VSAttachmentConstraint) attachConstraint.getVsConstraint();
        this.attachID = attachConstraint.getConstraintId();
        this.hingeConstraint = (VSHingeOrientationConstraint) hingeConstraint.getVsConstraint();
        this.hingeID = hingeConstraint.getConstraintId();
    }
    public final AlternatorBearingData updateWith(AlternatorBearingUpdateData updateData) {
//        this.bearingPosition = updateData.bearingPosition;
        return null;
    }
    public record AlternatorBearingUpdateData(VSAttachmentConstraint attachConstraint,
                                              VSHingeOrientationConstraint hingeConstraint){}
    public record AlternatorBearingCreateData(Vector3dc bearingPosition,
                                              Vector3dc bearingAxis,
                                              long shiptraptionID,
                                              VSConstraintAndId attachConstraint,
                                              VSConstraintAndId hingeConstraint){}
}
