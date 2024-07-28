package com.github.guyapooye.clockworkaddons.blocks.kinetics.pedals;

import com.github.guyapooye.clockworkaddons.registries.BlockEntityRegistry;
import com.google.common.base.Optional;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllShapes;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.contraptions.actors.seat.SeatBlock;
import com.simibubi.create.content.contraptions.actors.seat.SeatEntity;
import com.simibubi.create.content.kinetics.base.DirectionalAxisKineticBlock;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.content.kinetics.base.KineticBlock;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;
import com.simibubi.create.foundation.utility.BlockHelper;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.infrastructure.config.AllConfigs;
import io.github.fabricators_of_create.porting_lib.block.CustomPathNodeTypeBlock;
import io.github.fabricators_of_create.porting_lib.util.TagUtil;
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
import net.minecraft.world.item.DyeColor;
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
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class PedalsBlock extends HorizontalKineticBlock implements ProperWaterloggedBlock, CustomPathNodeTypeBlock, IBE<PedalsBlockEntity> {

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
    public BlockPathTypes getAiPathNodeType(BlockState state, BlockGetter world, BlockPos pos, @Nullable Mob entity) {
        return BlockPathTypes.RAIL;
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos,
                               CollisionContext ctx) {
        return AllShapes.TURNTABLE_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos,
                                        CollisionContext ctx) {
//        if (ctx instanceof EntityCollisionContext ecc && ecc.getEntity() instanceof Player)
//            return AllShapes.TURNTABLE_SHAPE;
        return AllShapes.TURNTABLE_SHAPE;
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
        if (world.isClientSide)
            return;
        PedalsEntity seat = PedalsEntity.create(world,pos);
        seat.setPos(pos.getX() + .5f, pos.getY(), pos.getZ() + .5f);
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
