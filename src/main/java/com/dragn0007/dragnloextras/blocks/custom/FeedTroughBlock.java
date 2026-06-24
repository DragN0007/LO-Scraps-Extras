package com.dragn0007.dragnloextras.blocks.custom;

import com.dragn0007.dragnlivestock.entities.util.AbstractOMount;
import com.dragn0007.dragnloextras.blocks.DecorRotator;
import com.dragn0007.dragnloextras.util.SETags;
import com.dragn0007.dragnloextras.util.ScrapsExtrasCommonConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Stream;

public class FeedTroughBlock extends DecorRotator implements SimpleWaterloggedBlock {

    public static final BooleanProperty FEED = BooleanProperty.create("has_feed");
    public static final IntegerProperty FOOD_AMOUNT = IntegerProperty.create("feed_amount", 0, 500);

    protected IntegerProperty getFoodAmountProperty() {
        return FOOD_AMOUNT;
    }
    public int getMaxFoodAmount() {
        return ScrapsExtrasCommonConfig.MAX_FEED_AMOUNT.get();
    }
    public int getFoodAmount(BlockState state) {
        return state.getValue(this.getFoodAmountProperty());
    }
    public BlockState getStateForFoodAmount(int i) {
        return this.defaultBlockState().setValue(this.getFoodAmountProperty(), Integer.valueOf(i));
    }

    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    public FeedTroughBlock() {
        super(NORTH, EAST, SOUTH, WEST, Properties.copy(Blocks.OAK_PLANKS).pushReaction(PushReaction.DESTROY).noOcclusion().randomTicks());
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FEED, Boolean.valueOf(false))
                .setValue(getFoodAmountProperty(), 0)
                );
    }

    public static final VoxelShape NORTH = Stream.of(
            Block.box(-8, 2, 1, 24, 10, 15)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public static final VoxelShape EAST = Stream.of(
            Block.box(1, 2, -8, 15, 10, 24)
    ).reduce((v1, v2) -> Shapes.join(v1, v2,BooleanOp.OR)).get();

    public static final VoxelShape SOUTH = Stream.of(
            Block.box(-8, 2, 1, 24, 10, 15)
    ).reduce((v1, v2) -> Shapes.join(v1, v2,BooleanOp.OR)).get();

    public static final VoxelShape WEST = Stream.of(
            Block.box(1, 2, -8, 15, 10, 24)
    ).reduce((v1, v2) -> Shapes.join(v1, v2,BooleanOp.OR)).get();

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(FEED, Boolean.valueOf(false))
                .setValue(FOOD_AMOUNT, 0)
        ;
        return super.getStateForPlacement(context);
    }

    public BlockState updateShape(BlockState state, Direction direction, BlockState state1, LevelAccessor accessor, BlockPos pos, BlockPos pos1) {
        if (!state.canSurvive(accessor, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, direction, state1, accessor, pos, pos1);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FEED)
                .add(FOOD_AMOUNT)
                .add(FACING);
    }

    public boolean isPathfindable(BlockState p_56104_, BlockGetter p_56105_, BlockPos p_56106_, PathComputationType p_56107_) {
        return true;
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource randomSource) {
        super.tick(state, level, pos, randomSource);
        Direction facingDirection = state.getValue(BlockStateProperties.HORIZONTAL_FACING);

        int feed = this.getFoodAmount(state);
        double range = 8.0;
        AABB searchBox = new AABB(pos).inflate(range);
        List<AbstractOMount> nearbyAnimals = level.getEntitiesOfClass(AbstractOMount.class, searchBox);

        for (AbstractOMount animal : nearbyAnimals) {
            if (feed > 0) {
                level.setBlockAndUpdate(pos, this.getStateForFoodAmount(feed - 1).setValue(FEED, Boolean.TRUE)
                        .setValue(BlockStateProperties.HORIZONTAL_FACING, facingDirection));
            }
        }

        if (feed <= 0) {
            state.setValue(FEED, Boolean.FALSE).setValue(BlockStateProperties.HORIZONTAL_FACING, facingDirection);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
         ItemStack stack = player.getItemInHand(hand);
         Direction facingDirection = state.getValue(BlockStateProperties.HORIZONTAL_FACING);

         if (this.getFoodAmount(state) < this.getMaxFoodAmount()) {
             if (stack.is(SETags.Items.GRAIN_FEED)) {
                 level.setBlockAndUpdate(pos, state.setValue(FEED, Boolean.valueOf(true))
                         .setValue(FOOD_AMOUNT, Integer.valueOf(getFoodAmount(state) + 25))
                         .setValue(BlockStateProperties.HORIZONTAL_FACING, facingDirection)
                 );
                 if (this.getFoodAmount(state) > this.getMaxFoodAmount()) {
                     level.setBlockAndUpdate(pos, state.setValue(FOOD_AMOUNT, Integer.valueOf(this.getMaxFoodAmount())));
                 }
                 level.playSound(null, pos, SoundEvents.GRASS_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);

                 if (!player.getAbilities().instabuild) {
                     stack.shrink(1);
                 }
                 return InteractionResult.SUCCESS;
             } else if (stack.is(SETags.Items.HEARTY_GRAIN_FEED)) {

             }
         }

        return InteractionResult.SUCCESS;
    }
}
