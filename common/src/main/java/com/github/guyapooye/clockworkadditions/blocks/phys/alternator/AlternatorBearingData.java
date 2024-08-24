package com.github.guyapooye.clockworkadditions.blocks.phys.alternator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.guyapooye.clockworkadditions.blocks.phys.PhysData;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3dc;
import org.valkyrienskies.core.apigame.constraints.VSAttachmentConstraint;
import org.valkyrienskies.core.apigame.constraints.VSConstraintAndId;
import org.valkyrienskies.core.apigame.constraints.VSHingeOrientationConstraint;
import com.github.guyapooye.clockworkadditions.blocks.phys.alternator.AlternatorBearingData.AlternatorBearingUpdateData;

@Getter
@Setter
public class AlternatorBearingData implements PhysData<AlternatorBearingUpdateData> {
    @Nullable
    private final Vector3dc bearingPosition;
    @Nullable
    private final Vector3dc bearingAxis;
    private final long shiptraptionId;
    @Nullable
    private VSAttachmentConstraint attachConstraint;
    @JsonIgnore
    @Nullable
    private Integer attachId;
    @Nullable
    private VSHingeOrientationConstraint hingeConstraint;
    @JsonIgnore
    @Nullable
    private Integer hingeId;
    /*why would you ever use this bruh*/
    @Deprecated
    public AlternatorBearingData() {

        this.bearingPosition = null;
        this.bearingAxis = null;
        this.shiptraptionId = -1L;
    }
    public AlternatorBearingData(@Nullable Vector3dc bearingPosition,
                                 @Nullable Vector3dc bearingAxis,
                                 long shiptraptionID,
                                 VSConstraintAndId attachConstraint,
                                 VSConstraintAndId hingeConstraint) {
        this.bearingPosition = bearingPosition;
        this.bearingAxis = bearingAxis;
        this.shiptraptionId = shiptraptionID;
        this.attachConstraint = (VSAttachmentConstraint) attachConstraint.getVsConstraint();
        this.attachId = attachConstraint.getConstraintId();
        this.hingeConstraint = (VSHingeOrientationConstraint) hingeConstraint.getVsConstraint();
        this.hingeId = hingeConstraint.getConstraintId();
    }
    public final void updateWith(AlternatorBearingUpdateData updateData) {
        attachConstraint = updateData.attachConstraint;
        hingeConstraint = updateData.hingeConstraint;
        attachId = updateData.attachId;
        hingeId = updateData.hingeId;
    }
    public record AlternatorBearingUpdateData(VSAttachmentConstraint attachConstraint,
                                              Integer attachId,
                                              VSHingeOrientationConstraint hingeConstraint,
                                              Integer hingeId){}
}
