//package com.github.guyapooye.clockworkadditions.blocks.kinetics.handlebar;
//
//import com.github.guyapooye.clockworkadditions.registries.PartialModelRegistry;
//import net.createmod.catnip.animation.AnimationTickHolder;
//import net.createmod.catnip.math.AngleHelper;
//import net.minecraft.core.Direction;
//import net.minecraft.world.level.block.state.properties.BlockStateProperties;
//import net.minecraftforge.client.model.data.ModelData;
//
//
//public class HandlebarInstance extends BackHalfShaftInstance <HandlebarBlockEntity> implements DynamicInstance {
//    private final ModelData handle;
//    private final Direction facing;
//
//    public HandlebarInstance(MaterialManager materialManager, HandlebarBlockEntity blockEntity) {
//        super(materialManager, blockEntity);
//        facing = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);
//        handle = getTransformMaterial().getModel(PartialModelRegistry.HANDLEBAR, blockState).createInstance();
//        rotateHandle();
//    }
//    @Override
//    protected Direction getShaftDirection() {
//        return Direction.DOWN;
//    }
//    private void rotateHandle() {
//        float angle = blockEntity.getIndependentAngle(AnimationTickHolder.getPartialTicks());
//        handle.loadIdentity()
//                .translate(getInstancePosition())
//                .centre()
//                .rotateY(180 + AngleHelper.horizontalAngle(facing)+angle*70)
//                .unCentre();
//    }
//
//    @Override
//    public void remove() {
//        super.remove();
//        handle.delete();
//    }
//    @Override
//    public void update() {
//        super.update();
//        rotateHandle();
//    }
//
//    @Override
//    public void updateLight() {
//        super.updateLight();
//        relight(pos, handle);
//    }
//    @Override
//    public void beginFrame() {
//        rotateHandle();
//    }
//}
