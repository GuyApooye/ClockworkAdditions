package com.guyapooye.clockworkadditions.blocks.gas;

import com.guyapooye.clockworkadditions.registries.BlockEntityRegistry;
import com.simibubi.create.foundation.block.IBE;
import dev.architectury.registry.fuel.FuelRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.valkyrienskies.clockwork.util.gui.IHaveDuctStats;
import org.valkyrienskies.kelvin.api.DuctNode;
import org.valkyrienskies.kelvin.api.DuctNodePos;
import org.valkyrienskies.kelvin.util.INodeBlock;

import java.util.List;

public class SelfPrimingCoalBurnerBlock extends HorizontalDirectionalBlock implements INodeBlock, IBE<SelfPrimingCoalBurnerBlockEntity>, IHaveDuctStats {

    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public SelfPrimingCoalBurnerBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(LIT, false));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!(level.getBlockEntity(pos) instanceof SelfPrimingCoalBurnerBlockEntity be)) return InteractionResult.PASS;

        ItemStack item = player.getItemInHand(hand);

        if (player.isShiftKeyDown()) return InteractionResult.PASS;

        if (item.isEmpty()) {
            if (!be.storedFuelStack.isEmpty()) {
                player.setItemInHand(hand, be.remainingItemStack);
                be.remainingItemStack = ItemStack.EMPTY;
            } else if (!be.storedFuelStack.isEmpty()) {
                player.setItemInHand(hand, be.storedFuelStack);
                be.storedFuelStack = ItemStack.EMPTY;
            }
            return InteractionResult.SUCCESS;
        }

        if (FuelRegistry.get(item) > 0 && !player.isShiftKeyDown()) {
            if (be.storedFuelStack.isEmpty()) {
                be.storedFuelStack = item.copy();
                if (!player.isCreative()) player.setItemInHand(hand, ItemStack.EMPTY);
            } else if (be.storedFuelStack.getItem().equals(item.getItem())) {
                if (be.storedFuelStack.getCount() + item.getCount() <= item.getMaxStackSize()) {
                    ItemStack copy = item.copy();
                    copy.setCount(copy.getCount() + be.storedFuelStack.getCount());
                    be.storedFuelStack = copy;
                    if (!player.isCreative()) player.setItemInHand(hand, ItemStack.EMPTY);
                } else {
                    ItemStack copy = item.copy();
                    copy.setCount(item.getMaxStackSize());
                    be.storedFuelStack = copy;
                    if (!player.isCreative()) {
                        item.setCount(be.storedFuelStack.getCount() + item.getCount() - item.getMaxStackSize());
                    }
                }
            }
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        nodePlace(state, level, pos, oldState, isMoving);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos,
                         BlockState newState, boolean isMoving) {
        nodeRemove(state, level, pos, newState, isMoving);
        IBE.onRemove(state, level, pos, newState);
    }

    @Override
    public Class<SelfPrimingCoalBurnerBlockEntity> getBlockEntityClass() {
        return SelfPrimingCoalBurnerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends SelfPrimingCoalBurnerBlockEntity> getBlockEntityType() {
        return BlockEntityRegistry.SELF_PRIMING_COAL_BURNER.get();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState()
                .setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(AbstractFurnaceBlock.LIT)) {
            double d = pos.getX() + 0.5;
            double e = pos.getY() + 0.25;
            double f = pos.getZ() + 0.5;
            if (random.nextDouble() < 0.1) {
                level.playLocalSound(d, e, f, SoundEvents.FURNACE_FIRE_CRACKLE,
                        SoundSource.BLOCKS, 1.0f, 1.0f, false);
            }

            Direction direction = state.getValue(AbstractFurnaceBlock.FACING);
            Direction.Axis axis = direction.getAxis();
            double g = 0.52;
            double h = random.nextDouble() * 0.6 - 0.3;
            double i = (axis == Direction.Axis.X) ? direction.getStepX() * g : h;
            double j = random.nextDouble() * 6.0 / 16.0;
            double k = (axis == Direction.Axis.Z) ? direction.getStepZ() * g : h;

            level.addParticle(ParticleTypes.SMOKE, d + i, e + j, f + k, 0.0, 0.0, 0.0);
            level.addParticle(ParticleTypes.FLAME, d + i, e + j, f + k, 0.0, 0.0, 0.0);
        }
    }

    @Override
    public List<Component> getAdditionalInfoLines() {
        return List.of(
                Component.translatable("vs_clockwork.duct_stats.produces_heat").withStyle(ChatFormatting.GOLD),
                Component.translatable("vs_clockwork.coal_burner.function").withStyle(ChatFormatting.WHITE)
        );
    }

    @Override
    public void nodePlace(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull BlockState blockState1, boolean b) {

    }

    @Override
    public void nodeAddClient(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos) {

    }

    @Override
    public void nodeRemoveClient(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos) {
        int a = 0;
    }

    @Override
    public void nodeRemove(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull BlockState blockState1, boolean b) {
    }

    @Override
    public @NotNull DuctNode createNode(@NotNull DuctNodePos ductNodePos) {
        return null;
    }

    @Override
    public boolean canConnectTo(@NotNull BlockPos blockPos, @NotNull BlockPos blockPos1, @NotNull Direction direction, @NotNull BlockGetter blockGetter) {
        return false;
    }
}
