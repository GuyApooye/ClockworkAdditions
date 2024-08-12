package com.github.guyapooye.clockworkadditions.blocks.kinetics.pedals;

import com.github.guyapooye.clockworkadditions.registries.BlockEntityRegistry;
import com.github.guyapooye.clockworkadditions.registries.ConfigRegistry;
import com.github.guyapooye.clockworkadditions.registries.ShapesRegistry;
import com.google.common.base.Optional;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.contraptions.actors.seat.SeatEntity;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.infrastructure.config.AllConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

public class PedalsBlock extends HorizontalKineticBlock implements ProperWaterloggedBlock, IBE<PedalsBlockEntity> {

    public PedalsBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(WATERLOGGED));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return withWater(super.getStateForPlacement(pContext), pContext);
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState,
                                  LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
        updateWater(pLevel, pState, pCurrentPos);
        return pState;
    }

    @Override
    public FluidState getFluidState(BlockState pState) {
        return fluidState(pState);
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> nonNullList) {
        super.fillItemCategory(group, nonNullList);
    }

    @Override
    public void fallOn(Level level, BlockState blockState, BlockPos blockPos, Entity entity, float f) {
        super.fallOn(level, blockState, blockPos, entity, f * 0.5F);
    }


    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos,
                               CollisionContext ctx) {
        return ShapesRegistry.PEDALS.get(blockState.getValue(PedalsBlock.HORIZONTAL_FACING));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos,
                                        CollisionContext ctx) {
//        if (ctx instanceof EntityCollisionContext ecc && ecc.getEntity() instanceof Player)
//            return AllShapes.TURNTABLE_SHAPE;
        return ShapesRegistry.PEDALS_COLLISION.get(blockState.getValue(PedalsBlock.HORIZONTAL_FACING));
    }





    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand,
                                          BlockHitResult blockHitResult) {
        if (player.isShiftKeyDown())
            return InteractionResult.PASS;

        List<PedalsEntity> seats = world.getEntitiesOfClass(PedalsEntity.class, new AABB(pos));
        if (!seats.isEmpty()) {
            PedalsEntity seatEntity = seats.get(0);
            List<Entity> passengers = seatEntity.getPassengers();
            if (!passengers.isEmpty() && passengers.get(0) instanceof Player)
                return InteractionResult.PASS;
            if (!world.isClientSide) {
                seatEntity.ejectPassengers();
                player.startRiding(seatEntity);
            }
            return InteractionResult.SUCCESS;
        }

        if (world.isClientSide)
            return InteractionResult.SUCCESS;
        sitDown(world, pos, getLeashed(world, player).or(player));
        return InteractionResult.SUCCESS;
    }
    public static boolean isSeatOccupied(Level world, BlockPos pos) {
        return !world.getEntitiesOfClass(PedalsEntity.class, new AABB(pos))
                .isEmpty();
    }


//    @Override
//    public void updateEntityAfterFallOn(BlockGetter reader, Entity entity) {
//        BlockPos pos = entity.blockPosition();
//        if (entity instanceof Player || !(entity instanceof LivingEntity) || !canBePickedUp(entity)
//                || isSeatOccupied(entity.level, pos)) {
//            if (entity.isSuppressingBounce()) {
//                super.updateEntityAfterFallOn(reader, entity);
//                return;
//            }
//
//            Vec3 vec3 = entity.getDeltaMovement();
//            if (vec3.y < 0.0D) {
//                double d0 = entity instanceof LivingEntity ? 1.0D : 0.8D;
//                entity.setDeltaMovement(vec3.x, -vec3.y * (double) 0.66F * d0, vec3.z);
//            }
//
//            return;
//        }
//        if (reader.getBlockState(pos)
//                .getBlock() != this)
//            return;
//        sitDown(entity.level, pos, entity);
//    }

    public static Optional<Entity> getLeashed(Level level, Player player) {
        List<Entity> entities = player.level.getEntities((Entity) null, player.getBoundingBox()
                .inflate(10), e -> true);
        for (Entity e : entities)
            if (e instanceof Mob mob && mob.getLeashHolder() == player && PedalsBlock.canBePickedUp(e))
                return Optional.of(mob);
        return Optional.absent();
    }

    public static boolean canBePickedUp(Entity passenger) {
        if (passenger instanceof Shulker)
            return false;
        if (passenger instanceof Player)
            return false;
        if (AllTags.AllEntityTags.IGNORE_SEAT.matches(passenger))
            return false;
        if (!AllConfigs.server().logistics.seatHostileMobs.get() && !passenger.getType()
                .getCategory()
                .isFriendly())
            return false;
        return passenger instanceof LivingEntity;
    }

    public static void sitDown(Level world, BlockPos pos, Entity entity) {
        if (world.isClientSide) return;
        SeatEntity seat = PedalsEntity.create(world,pos);
        seat.setPos(pos.getX() + .5d,  pos.getY()+ (double) ConfigRegistry.server().kinetics.pedalsSeatHightOffset.getF(), pos.getZ() + .5d);
//        seat.handlePosSet();
        world.addFreshEntity(seat);
        entity.startRiding(seat, true);
        if (entity instanceof TamableAnimal ta)
            ta.setInSittingPose(true);
    }
    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == Direction.DOWN;
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    public Class<PedalsBlockEntity> getBlockEntityClass() {
        return PedalsBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends PedalsBlockEntity> getBlockEntityType() {
        return BlockEntityRegistry.PEDALS.get();
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return Direction.Axis.Y;
    }
    public static Couple<Integer> getSpeedRange() {
        return Couple.create(64, 128);
    }
}
