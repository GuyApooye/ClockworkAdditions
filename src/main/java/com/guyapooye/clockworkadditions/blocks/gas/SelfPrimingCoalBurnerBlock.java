package com.guyapooye.clockworkadditions.blocks.gas;

import com.google.common.base.Optional;
import com.guyapooye.clockworkadditions.blocks.kinetics.pedals.PedalsBlock;
import com.guyapooye.clockworkadditions.blocks.kinetics.pedals.PedalsBlockEntity;
import com.guyapooye.clockworkadditions.entities.pedals.PedalsEntity;
import com.guyapooye.clockworkadditions.registries.BlockEntityRegistry;
import com.guyapooye.clockworkadditions.registries.ShapesRegistry;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.contraptions.actors.seat.SeatEntity;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;
import com.simibubi.create.infrastructure.config.AllConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.player.Player;
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
import net.minecraftforge.common.util.FakePlayer;
import org.valkyrienskies.clockwork.content.logistics.gas.generation.coal_burner.CoalBurnerBlockEntity;

import java.util.List;

public class SelfPrimingCoalBurnerBlock extends PedalsBlock {

    public SelfPrimingCoalBurnerBlock(Properties properties) {
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
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
        updateWater(pLevel, pState, pCurrentPos);
        return pState;
    }

    @Override
    public FluidState getFluidState(BlockState pState) {
        return fluidState(pState);
    }

    @Override
    public void fallOn(Level level, BlockState blockState, BlockPos blockPos, Entity entity, float f) {
        super.fallOn(level, blockState, blockPos, entity, f * 0.5F);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext ctx) {
        return ShapesRegistry.PEDALS.get(blockState.getValue(SelfPrimingCoalBurnerBlock.HORIZONTAL_FACING));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext ctx) {
//        if (ctx instanceof EntityCollisionContext ecc && ecc.getEntity() instanceof Player)
//            return AllShapes.TURNTABLE_SHAPE;
        return ShapesRegistry.PEDALS_COLLISION.get(blockState.getValue(SelfPrimingCoalBurnerBlock.HORIZONTAL_FACING));
    }


    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult p_225533_6_) {
        if (player.isShiftKeyDown() || player instanceof FakePlayer) return InteractionResult.PASS;

        List<SeatEntity> seats = world.getEntitiesOfClass(SeatEntity.class, new AABB(pos));
        if (!seats.isEmpty()) {
            SeatEntity seatEntity = seats.get(0);
            List<Entity> passengers = seatEntity.getPassengers();
            if (!passengers.isEmpty() && passengers.get(0) instanceof Player)
                return InteractionResult.PASS;
            if (!world.isClientSide) {
                seatEntity.ejectPassengers();
                player.startRiding(seatEntity);
            }
            return InteractionResult.SUCCESS;
        }

        if (world.isClientSide) return InteractionResult.SUCCESS;
        sitDown(world, pos, getLeashed(world, player).or(player));
        return InteractionResult.SUCCESS;
    }

    public static void sitDown(Level world, BlockPos pos, Entity entity) {
        if (world.isClientSide) return;
        PedalsEntity seat = new PedalsEntity(world, pos);
        seat.setPos(pos.getX() + .5, pos.getY() + .6, pos.getZ() + .5);
        world.addFreshEntity(seat);
        entity.startRiding(seat, true);
        if (entity instanceof TamableAnimal ta) ta.setInSittingPose(true);
    }

    public static Optional<Entity> getLeashed(Level level, Player player) {
        List<Entity> entities = level.getEntities((Entity) null, player.getBoundingBox().inflate(10), e -> true);
        for (Entity e : entities) if (e instanceof Mob mob && mob.getLeashHolder() == player && SelfPrimingCoalBurnerBlock.canBePickedUp(e)) return Optional.of(mob);
        return Optional.absent();
    }

    public static boolean canBePickedUp(Entity passenger) {
        if (passenger instanceof Shulker) return false;
        if (passenger instanceof Player) return false;
        if (AllTags.AllEntityTags.IGNORE_SEAT.matches(passenger)) return false;
        if (!AllConfigs.server().logistics.seatHostileMobs.get() && !passenger.getType().getCategory().isFriendly()) return false;
        return passenger instanceof LivingEntity;
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
    public Class<CoalBurnerBlockEntity> getBlockEntityClass() {
        return CoalBurnerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends CoalBurnerBlockEntity> getBlockEntityType() {
        return CWBlo.PEDALS.get();
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return Direction.Axis.Y;
    }
}
