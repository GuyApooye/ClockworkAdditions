package com.guyapooye.clockworkadditions.blocks.kinetics.pedals;

import com.guyapooye.clockworkadditions.registries.PartialModelRegistry;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class PedalsVisual extends KineticBlockEntityVisual<PedalsBlockEntity> {
    protected RotatingInstance shaft;
    protected RotatingInstance pedal1;
    protected RotatingInstance pedal2;
    protected RotatingInstance pedal3;

    public PedalsVisual(VisualizationContext context, PedalsBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick);

        final Direction direction = blockState.getValue(PedalsBlock.HORIZONTAL_FACING);
        final Direction.Axis axis = direction.getClockWise().getAxis();

        this.shaft = instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(AllPartialModels.SHAFT_HALF, Direction.DOWN)).createInstance().rotateToFace(Direction.Axis.Y);
        this.pedal1 = instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(PartialModelRegistry.PEDALS_CRANK, Direction.EAST)).createInstance().rotateToFace(Direction.UP, axis);
        this.pedal2 = instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(PartialModelRegistry.RIGHT_PEDAL, Direction.EAST)).createInstance().rotateToFace(Direction.UP, axis);
        this.pedal3 = instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(PartialModelRegistry.LEFT_PEDAL, Direction.EAST)).createInstance().rotateToFace(Direction.UP, axis);

        shaft.setup(blockEntity, Direction.Axis.Y)
                .setPosition(getVisualPosition())
                .setChanged();

        pedal1.setup(blockEntity, axis)
                .setPosition(getVisualPosition())
                .nudge(direction.getStepX() * 0.5f, 0, direction.getStepZ() * 0.5f)
                .setRotationalSpeed(-blockEntity.getSpeed() * partialTick * 8f)
                .setChanged();

        pedal2.setup(blockEntity, axis)
                .setPosition(getVisualPosition())
                .nudge(direction.getStepX() == 0 ? 0.625f : direction.getStepX() * 0.5f, 0, direction.getStepZ() == 0 ? 0.625f : direction.getStepZ() * 0.5f)
                .setRotationalSpeed(0)
                .setChanged();

        pedal3.setup(blockEntity, axis)
                .setPosition(getVisualPosition())
                .nudge(direction.getStepX() == 0 ? -0.625f : direction.getStepX() * 0.5f, 0, direction.getStepZ() == 0 ? -0.625f : direction.getStepZ() * 0.5f)
                .setRotationalSpeed(0)
                .setChanged();
    }

    @Override
    public void update(float v) {
        final Direction direction = blockState.getValue(PedalsBlock.HORIZONTAL_FACING).getClockWise();
        final Direction.Axis axis = direction.getAxis();
        shaft.setup(blockEntity, Direction.Axis.Y, blockEntity.getSpeed()).setChanged();
        pedal1.setup(blockEntity, axis, -blockEntity.getSpeed()).setChanged();
        pedal2.setup(blockEntity, axis, -blockEntity.getSpeed()).light(pedal1.light).setChanged();
        pedal3.setup(blockEntity, axis, -blockEntity.getSpeed()).light(pedal1.light).setChanged();
    }

    public void updateLight(float v) {
        this.relight(this.pos, this.shaft, this.pedal1);
    }

    @Override
    public void collectCrumblingInstances(Consumer<@Nullable Instance> consumer) {
        consumer.accept(shaft);
        consumer.accept(pedal1);
        consumer.accept(pedal2);
        consumer.accept(pedal3);
    }

    @Override
    protected void _delete() {
        shaft.delete();
        pedal1.delete();
        pedal2.delete();
        pedal3.delete();
    }
}
