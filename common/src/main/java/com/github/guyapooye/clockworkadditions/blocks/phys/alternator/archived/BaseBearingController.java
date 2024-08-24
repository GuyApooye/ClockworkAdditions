package com.github.guyapooye.clockworkadditions.blocks.phys.alternator.archived;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mojang.datafixers.util.Pair;
import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3dc;
import org.joml.Quaterniondc;
import org.joml.Vector3d;
import org.joml.Vector3dc;
import org.valkyrienskies.core.api.ships.PhysShip;
import org.valkyrienskies.core.api.ships.ServerShip;
import org.valkyrienskies.core.api.ships.ShipForcesInducer;
import org.valkyrienskies.core.api.ships.properties.ShipTransform;
import org.valkyrienskies.core.apigame.constraints.VSAttachmentConstraint;
import org.valkyrienskies.core.apigame.constraints.VSHingeOrientationConstraint;
import org.valkyrienskies.core.impl.game.ships.PhysShipImpl;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BaseBearingController implements ShipForcesInducer {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private final HashMap<Integer,BasePhysicsBearingData> bearingData = new HashMap<>();
    @JsonIgnore
    @NotNull
    private final ConcurrentHashMap<Integer ,BasePhysicsBearingUpdateData> bearingUpdateData = new ConcurrentHashMap();
    @JsonIgnore
    @NotNull
    private final ConcurrentLinkedQueue<Pair> createdBearings = new ConcurrentLinkedQueue();
    @NotNull
    private final ConcurrentLinkedQueue<Integer> removedBearings = new ConcurrentLinkedQueue();
    private int nextBearingID = 0;

    @NotNull
    public final HashMap<Integer,BasePhysicsBearingData> getBearingData() {
        return this.bearingData;
    }
    public void applyForces(@NotNull PhysShip physShip) {
    }
    public void applyForcesAndLookupPhysShips(@NotNull PhysShip physShip, @NotNull Function1 lookupPhysShip) {
        while(!this.createdBearings.isEmpty()) {
            Pair createData = this.createdBearings.remove();
            ((Map)this.bearingData).put(createData.getFirst(),
                    new BasePhysicsBearingData(((BasePhysicsBearingCreateData)createData.getSecond()).getBearingPos(),
                            ((BasePhysicsBearingCreateData)createData.getSecond()).getBearingAxis(), ((BasePhysicsBearingCreateData)createData.getSecond()).getBearingAngle(),
                            ((BasePhysicsBearingCreateData)createData.getSecond()).getBearingRPM(), ((BasePhysicsBearingCreateData)createData.getSecond()).getLocked(),
                            ((BasePhysicsBearingCreateData)createData.getSecond()).getShiptraptionID(), ((BasePhysicsBearingCreateData)createData.getSecond()).getConstraint(),
                            ((BasePhysicsBearingCreateData)createData.getSecond()).getHingeConstraint(), ((BasePhysicsBearingCreateData)createData.getSecond()).getPosDampConstraint(),
                            ((BasePhysicsBearingCreateData)createData.getSecond()).getRotDampConstraint(), ((BasePhysicsBearingCreateData)createData.getSecond()).getSecondAttachment()));
        }

        while(!this.removedBearings.isEmpty()) {
            HashMap var10000 = this.bearingData;
            Object var10001 = this.removedBearings.remove();
            Objects.requireNonNull(var10001, "null cannot be cast to non-null type int");
            var10000.remove(var10001);
        }

        Map $this$forEach$iv = this.bearingUpdateData;
        Iterator var5 = $this$forEach$iv.entrySet().iterator();

        while(var5.hasNext()) {
            Map.Entry element$iv = (Map.Entry)var5.next();
            int id = ((Number)element$iv.getKey()).intValue();
            BasePhysicsBearingUpdateData data = (BasePhysicsBearingUpdateData)element$iv.getValue();
            BasePhysicsBearingData var19 = this.bearingData.get(id);
            if (var19 != null) {
                BasePhysicsBearingData var11 = var19;
                Objects.requireNonNull(var11);
                var11.setBearingAngle(data.getBearingAngle());
                var11.setBearingRPM(data.getBearingRPM());
                var11.setLocked(data.getLocked());
            }
        }

        this.bearingUpdateData.clear();
        Iterator var14 = this.bearingData.values().iterator();

        while(var14.hasNext()) {
            BasePhysicsBearingData data = (BasePhysicsBearingData) var14.next();
            data.setAligning(true);
            if (data.getAngleConstraint() == null) {
                VSHingeOrientationConstraint var20 = data.getHingeConstraint();
                Objects.requireNonNull(var20);
                long physShipBearingIsOnId = var20.getShipId1();
                if (physShipBearingIsOnId == -1L) {
                    Objects.requireNonNull(data);
                    Vector3dc torque = this.computeRotationalForce(data, (PhysShipImpl)physShip, null);
                    physShip.applyRotDependentTorque(torque);
                } else {
                    PhysShip physShipBearingIsOn = (PhysShip)lookupPhysShip.invoke(physShipBearingIsOnId);
                    Vector3dc torque;
                    if (physShipBearingIsOn == null) {
                        Objects.requireNonNull(data);
                        torque = this.computeRotationalForce(data, (PhysShipImpl)physShip, null);
                        physShip.applyRotDependentTorque(torque);
                    } else {
                        Objects.requireNonNull(data);
                        torque = this.computeRotationalForce(data, (PhysShipImpl)physShip, (PhysShipImpl)physShipBearingIsOn);
                        physShip.applyRotDependentTorque(torque);
                        PhysShipImpl var21 = (PhysShipImpl)physShipBearingIsOn;
                        Vector3d var22 = torque.mul(-1.0, new Vector3d());
                        Objects.requireNonNull(var22, "mul(...)");
                        var21.applyRotDependentTorque(var22);
                    }
                }
            }
        }

    }

    private Vector3dc computeRotationalForce(BasePhysicsBearingData data, PhysShipImpl physShip, PhysShipImpl otherPhysShip) {
        Vector3dc torque = null;
        torque = data.getLocked() ?
                this.computeLockedRotationalForce(data, physShip, otherPhysShip) :
                this.computeUnlockedRotationalForce(data, physShip, otherPhysShip);
        return torque;
    }

    private double getAngularInertia(PhysShipImpl physShip, Vector3dc localPos, Vector3dc axisGlobal) {
        Vector3d var10000 = physShip.getTransform().getShipToWorld().transformPosition(localPos, new Vector3d());
        Objects.requireNonNull(var10000, "transformPosition(...)");
        Vector3dc globalPos = var10000;
        var10000 = globalPos.sub(physShip.getPoseVel().getPos(), new Vector3d());
        Objects.requireNonNull(var10000, "sub(...)");
        Vector3dc offset = var10000;
        return this.getAngularInertia(physShip.getInertia().getMomentOfInertiaTensor(), physShip.getTransform().getShipToWorldRotation(), physShip.getInertia().getShipMass(), offset, axisGlobal);
    }

    private double getAngularInertia(Matrix3dc inertiaTensorLocal, Quaterniondc rotation, double mass, Vector3dc offsetGlobal, Vector3dc axisGlobal) {
        Vector3d var10000 = offsetGlobal.sub(axisGlobal.mul(axisGlobal.dot(offsetGlobal), new Vector3d()), new Vector3d());
        Objects.requireNonNull(var10000, "sub(...)");
        Vector3dc offsetPerpToAxis = var10000;
        var10000 = rotation.transformInverse(axisGlobal, new Vector3d());
        Objects.requireNonNull(var10000, "transformInverse(...)");
        Vector3dc axisLocal = var10000;
        return inertiaTensorLocal.transform(axisLocal, new Vector3d()).dot(axisLocal) + offsetPerpToAxis.lengthSquared() * mass;
    }

    private double parallelOperator(double left, double right) {
        return 1.0 / (1.0 / left + 1.0 / right);
    }

    private Vector3dc computeUnlockedRotationalForce(BasePhysicsBearingData data, PhysShipImpl physShip, PhysShipImpl otherPhysShip) {
        if (data.getBearingAxis() == null) {
            return new Vector3d();
        } else {
            Vector3d bearingAxisInGlobal = new Vector3d(data.getBearingAxis());
            if (otherPhysShip != null) {
                ShipTransform var10000 = otherPhysShip.getTransform();
                if (var10000 != null) {
                    Quaterniondc var15 = var10000.getShipToWorldRotation();
                    if (var15 != null) {
                        var15.transform(bearingAxisInGlobal);
                    }
                }
            }

            Vector3d idealRelativeOmega = bearingAxisInGlobal.mul(data.getBearingRPM(), new Vector3d()).mul(0.10471975511965977);
            Vector3d actualRelativeOmega = null;
            actualRelativeOmega = !physShip.isStatic() ? new Vector3d(physShip.getPoseVel().getOmega()) : new Vector3d();
            double torqueMassMultiplier = 0.0;
            VSAttachmentConstraint var10002;
            if (!physShip.isStatic()) {
                if (otherPhysShip != null && !otherPhysShip.isStatic()) {
                    var10002 = data.getAttachConstraint();
                    Objects.requireNonNull(var10002);
                    double physShipInertia = this.getAngularInertia(physShip, var10002.getLocalPos0(), bearingAxisInGlobal);
                    var10002 = data.getAttachConstraint();
                    Objects.requireNonNull(var10002);
                    double otherShipInertia = this.getAngularInertia(otherPhysShip, var10002.getLocalPos1(), bearingAxisInGlobal);
                    torqueMassMultiplier = this.parallelOperator(physShipInertia, otherShipInertia);
                    actualRelativeOmega.sub(otherPhysShip.getPoseVel().getOmega());
                } else {
                    var10002 = data.getAttachConstraint();
                    Objects.requireNonNull(var10002);
                    torqueMassMultiplier = this.getAngularInertia(physShip, var10002.getLocalPos0(), bearingAxisInGlobal);
                }
            } else {
                if (otherPhysShip == null || otherPhysShip.isStatic()) {
                    return new Vector3d();
                }

                var10002 = data.getAttachConstraint();
                Objects.requireNonNull(var10002);
                torqueMassMultiplier = this.getAngularInertia(otherPhysShip, var10002.getLocalPos1(), bearingAxisInGlobal);
                actualRelativeOmega.sub(otherPhysShip.getPoseVel().getOmega());
            }

            Vector3d var16 = data.getBearingAxis().rotate(physShip.getPoseVel().getRot(), new Vector3d());
            Objects.requireNonNull(var16, "rotate(...)");
            Vector3dc bearingAxisAfterRot = var16;
            if (otherPhysShip != null) {
                var16 = otherPhysShip.getPoseVel().getRot().transformInverse(bearingAxisAfterRot, new Vector3d());
                Objects.requireNonNull(var16, "transformInverse(...)");
                bearingAxisAfterRot = var16;
            }

            if (bearingAxisAfterRot.angleCos(data.getBearingAxis()) < 0.9961947 && bearingAxisAfterRot.angleCos(data.getBearingAxis()) > -0.9961947) {
                return new Vector3d();
            } else {
                var16 = idealRelativeOmega.sub(actualRelativeOmega, new Vector3d());
                Objects.requireNonNull(var16, "sub(...)");
                Vector3dc angularVelError = var16;
                var16 = bearingAxisInGlobal.mul(bearingAxisInGlobal.dot(angularVelError), new Vector3d());
                Objects.requireNonNull(var16, "mul(...)");
                Vector3dc angularVelErrorAlongBearingAxis = var16;
                var16 = angularVelErrorAlongBearingAxis.mul(torqueMassMultiplier * 10.0, new Vector3d());
                Objects.requireNonNull(var16, "mul(...)");
                return var16;
            }
        }
    }

    private Vector3dc computeLockedRotationalForce(BasePhysicsBearingData data, PhysShipImpl physShip, PhysShipImpl otherPhysShip) {
        if (data.getBearingAxis() == null) {
            return new Vector3d();
        } else {
            Vector3d bearingAxisInGlobal = new Vector3d(data.getBearingAxis());
            if (otherPhysShip != null) {
                ShipTransform var10000 = otherPhysShip.getTransform();
                if (var10000 != null) {
                    Quaterniondc var28 = var10000.getShipToWorldRotation();
                    if (var28 != null) {
                        var28.transform(bearingAxisInGlobal);
                    }
                }
            }

            Vector3d actualRelativeOmega = null;
            actualRelativeOmega = !physShip.isStatic() ? new Vector3d(physShip.getPoseVel().getOmega()) : new Vector3d();
            double torqueMassMultiplier = 0.0;
            VSAttachmentConstraint var10002;
            if (!physShip.isStatic()) {
                if (otherPhysShip != null && !otherPhysShip.isStatic()) {
                    var10002 = data.getAttachConstraint();
                    Objects.requireNonNull(var10002);
                    double physShipInertia = this.getAngularInertia(physShip, var10002.getLocalPos0(), bearingAxisInGlobal);
                    var10002 = data.getAttachConstraint();
                    Objects.requireNonNull(var10002);
                    double otherShipInertia = this.getAngularInertia(otherPhysShip, var10002.getLocalPos1(), bearingAxisInGlobal);
                    torqueMassMultiplier = this.parallelOperator(physShipInertia, otherShipInertia);
                    actualRelativeOmega.sub(otherPhysShip.getPoseVel().getOmega());
                } else {
                    var10002 = data.getAttachConstraint();
                    Objects.requireNonNull(var10002);
                    torqueMassMultiplier = this.getAngularInertia(physShip, var10002.getLocalPos0(), bearingAxisInGlobal);
                }
            } else {
                if (otherPhysShip == null || otherPhysShip.isStatic()) {
                    return new Vector3d();
                }

                var10002 = data.getAttachConstraint();
                Objects.requireNonNull(var10002);
                torqueMassMultiplier = this.getAngularInertia(otherPhysShip, var10002.getLocalPos1(), bearingAxisInGlobal);
                actualRelativeOmega.sub(otherPhysShip.getPoseVel().getOmega());
            }

            Vector3dc var29;
            if (Math.abs(data.getBearingAxis().x()) == 1.0) {
                var29 = new Vector3d(0.0, 1.0, 0.0);
            } else if (Math.abs(data.getBearingAxis().y()) == 1.0) {
                var29 = new Vector3d(1.0, 0.0, 0.0);
            } else {
                if (Math.abs(data.getBearingAxis().z()) != 1.0) {
                    throw new RuntimeException("how the fuck did you mess this up g");
                }

                var29 = new Vector3d(0.0, 1.0, 0.0);
            }

            Vector3dc perpendicularAxis = var29;
            Vector3d var30 = data.getBearingAxis().rotate(physShip.getPoseVel().getRot(), new Vector3d());
            Objects.requireNonNull(var30, "rotate(...)");
            Vector3dc bearingAxisAfterRot = var30;
            var30 = perpendicularAxis.rotate(physShip.getPoseVel().getRot(), new Vector3d());
            Objects.requireNonNull(var30, "rotate(...)");
            Vector3dc perpAfterRot = var30;
            if (otherPhysShip != null) {
                var30 = otherPhysShip.getPoseVel().getRot().transformInverse(perpAfterRot, new Vector3d());
                Objects.requireNonNull(var30, "transformInverse(...)");
                perpAfterRot = var30;
                var30 = otherPhysShip.getPoseVel().getRot().transformInverse(bearingAxisAfterRot, new Vector3d());
                Objects.requireNonNull(var30, "transformInverse(...)");
                bearingAxisAfterRot = var30;
            }

            if (bearingAxisAfterRot.angleCos(data.getBearingAxis()) < 0.9961947 && bearingAxisAfterRot.angleCos(data.getBearingAxis()) > -0.9961947) {
                return new Vector3d();
            } else {
                var30 = perpAfterRot.sub(data.getBearingAxis().mul(data.getBearingAxis().dot(perpAfterRot), new Vector3d()), new Vector3d());
                Objects.requireNonNull(var30, "sub(...)");
                Vector3dc perpAfterRotInPlane = var30;
                double angleBTShipInRadians = perpAfterRotInPlane.angle(perpendicularAxis);
                var30 = perpAfterRotInPlane.cross(perpendicularAxis, new Vector3d());
                Objects.requireNonNull(var30, "cross(...)");
                Vector3dc crossOfYourMother = var30;
                double angleWRespectToBearingAxis = crossOfYourMother.lengthSquared() > 1.0E-12 ? angleBTShipInRadians * Math.signum(crossOfYourMother.dot(data.getBearingAxis())) * (double)-1 : 0.0;

                double angleErr;
                for(angleErr = Math.toRadians(data.getBearingAngle()) - angleWRespectToBearingAxis; angleErr > Math.PI; angleErr -= 6.283185307179586) {
                }

                while(angleErr < -3.141592653589793) {
                    angleErr += 6.283185307179586;
                }

                var30 = physShip.getTransform().getWorldToShip().transformDirection(actualRelativeOmega, new Vector3d());
                Objects.requireNonNull(var30, "transformDirection(...)");
                Vector3dc relativeOmegaInPhysShip = var30;
                double relativeOmegaInPhysShipParallelBearingAxis = data.getBearingAxis().dot(relativeOmegaInPhysShip);
                double omegaErr = (double)data.getBearingRPM() * 0.10471975511965977 - relativeOmegaInPhysShipParallelBearingAxis;
                //0.10471975511965977
                double torque = angleErr * torqueMassMultiplier * 50.0 + omegaErr * torqueMassMultiplier * 50.0;
                var30 = bearingAxisInGlobal.mul(torque, new Vector3d());
                Objects.requireNonNull(var30, "mul(...)");
                return var30;
            }
        }
    }

    public final int addPhysBearing(@NotNull BasePhysicsBearingCreateData data) {
        int var3 = this.nextBearingID++;
        this.createdBearings.add(new Pair<>(var3, data));
//        this.bearingData.put(var3,
//                new BasePhysicsBearingData(data.getBearingPos(),
//                        data.getBearingAxis(), data.getBearingAngle(),
//                        data.getBearingRPM(), data.getLocked(),
//                        data.getShiptraptionID(), data.getConstraint(),
//                        data.getHingeConstraint(), data.getPosDampConstraint(),
//                        data.getRotDampConstraint(), data.getSecondAttachment()));
        System.out.println("next bearing id:"+var3);
        return var3;
    }

    public final void removePhysBearing(int id) {
        this.removedBearings.add(id);
    }

    public final void updatePhysBearing(int id, @NotNull BasePhysicsBearingUpdateData data) {
        Integer var3 = id;
        ((Map)this.bearingUpdateData).put(var3, data);

        BasePhysicsBearingData var19 = this.bearingData.get(id);
        if (var19 != null) {
            Objects.requireNonNull(var19);
            var19.setBearingAngle(data.getBearingAngle());
            var19.setBearingRPM(data.getBearingRPM());
            var19.setLocked(data.getLocked());
        }
    }

    public boolean equals(@Nullable Object other) {
        boolean var10000;
        if (this == other) {
            var10000 = true;
        } else if (!(other instanceof BaseBearingController)) {
            var10000 = false;
        } else {
            if (Objects.equals(this.bearingData, ((BaseBearingController)other).bearingData) && Objects.equals(this.bearingUpdateData, ((BaseBearingController)other).bearingUpdateData)) {
                Companion var2 = Companion;
                Queue left$iv = this.createdBearings;
                Queue right$iv = ((BaseBearingController)other).createdBearings;
                Collection $this$toTypedArray$iv$iv = left$iv;
                Object[] var10 = $this$toTypedArray$iv$iv.toArray(new Pair[0]);
                $this$toTypedArray$iv$iv = right$iv;
                Object[] var12 = $this$toTypedArray$iv$iv.toArray(new Pair[0]);
                if (Arrays.equals(var10, var12)) {
                    var2 = Companion;
                    left$iv = this.removedBearings;
                    right$iv = ((BaseBearingController)other).removedBearings;
                    $this$toTypedArray$iv$iv = left$iv;
                    var10 = $this$toTypedArray$iv$iv.toArray(new Integer[0]);
                    $this$toTypedArray$iv$iv = right$iv;
                    var12 = $this$toTypedArray$iv$iv.toArray(new Integer[0]);
                    if (Arrays.equals(var10, var12) && this.nextBearingID == ((BaseBearingController)other).nextBearingID) {
                        var10000 = true;
                        return var10000;
                    }
                }
            }

            var10000 = false;
        }

        return var10000;
    }

    public final boolean canDisassemble() {
        return false;
    }

    public final void setAligning(boolean yn, int id) {
        Object var10000 = this.bearingData.get(id);
        Objects.requireNonNull(var10000);
        ((BasePhysicsBearingData)var10000).setAligning(yn);
    }

    public static final class Companion {
        private Companion() {
        }

        @Nullable
        public BaseBearingController getOrCreate(@NotNull ServerShip ship) {
            if (ship.getAttachment(BaseBearingController.class) == null) {
                System.out.println("created bearing controller");
                ship.saveAttachment(BaseBearingController.class, new BaseBearingController());
            }
            return ship.getAttachment(BaseBearingController.class);
        }

        // $FF: synthetic method
        public boolean areQueuesEqual(@NotNull Queue left,@NotNull Queue right) {
            Collection $this$toTypedArray$iv = left;
            Collection thisCollection$iv = $this$toTypedArray$iv;
            Object[] var8 = thisCollection$iv.toArray(new Object[0]);
            $this$toTypedArray$iv = right;
            thisCollection$iv = $this$toTypedArray$iv;
            Object[] var10 = thisCollection$iv.toArray(new Object[0]);
            return Arrays.equals(var8, var10);
        }

        // $FF: synthetic method
        public Companion(Object obj) {this();}
    }
}
