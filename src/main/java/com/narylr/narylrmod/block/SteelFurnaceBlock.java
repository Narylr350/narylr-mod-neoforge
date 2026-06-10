package com.narylr.narylrmod.block;

import com.mojang.serialization.MapCodec;
import com.narylr.narylrmod.block.entity.ModBlockEntities;
import com.narylr.narylrmod.block.entity.SteelFurnaceBlockEntity;
import com.narylr.narylrmod.screen.SteelFurnaceMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

// 钢熔炉方块，支持自定义配方和原版熔炼
public class SteelFurnaceBlock extends BaseEntityBlock {
    // 朝向属性
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final MapCodec<SteelFurnaceBlock> CODEC = simpleCodec(SteelFurnaceBlock::new);
    // 是否点燃
    public static final BooleanProperty LIT = BooleanProperty.create("lit");

    public SteelFurnaceBlock(Properties properties) {
        super(properties);
        // 默认状态：未点燃，朝北
        registerDefaultState(this.stateDefinition.any()
                .setValue(LIT, false)
                .setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    // 定义方块状态属性
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT);
    }

    // 放置时朝向玩家
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return defaultBlockState().setValue(FACING, blockPlaceContext.getHorizontalDirection().getOpposite());
    }

    // 右键打开 GUI
    @Override
    protected InteractionResult useWithoutItem(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            BlockHitResult hitResult
    ) {
        if (!level.isClientSide()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);

            if (blockEntity instanceof SteelFurnaceBlockEntity steelFurnaceBlockEntity) {
                player.openMenu(new SimpleMenuProvider(
                        (containerId, playerInventory, player1) -> new SteelFurnaceMenu(containerId, playerInventory, steelFurnaceBlockEntity),
                        Component.translatable("container.narylr_mod.steel_furnace")));
            }
        }
        return InteractionResult.SUCCESS;
    }

    // 创建方块实体
    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SteelFurnaceBlockEntity(pos, state);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    // 方块被破坏时掉落物品和经验
    @Override
    protected void onRemove(
            BlockState state,
            Level level,
            BlockPos pos,
            BlockState newState,
            boolean movedByPiston
    ) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);

            if (blockEntity instanceof SteelFurnaceBlockEntity steelFurnaceBlockEntity) {
                steelFurnaceBlockEntity.dropStoredExperience();
                Containers.dropContents(level, pos, steelFurnaceBlockEntity);
                level.updateNeighbourForOutputSignal(pos, this);
            }
        }

        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    // 获取服务端 ticker，用于驱动方块实体逻辑
    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level level,
            BlockState state,
            BlockEntityType<T> blockEntityType
    ) {
        if (level.isClientSide) {
            return null;
        }

        return createTickerHelper(
                blockEntityType,
                ModBlockEntities.STEEL_FURNACE_BLOCK_ENTITY.get(),
                SteelFurnaceBlockEntity::serverTick
        );
    }

    // 踩在点燃的钢熔炉上会受到火焰伤害
    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        super.stepOn(level, pos, state, entity);

        if (!level.isClientSide && state.getValue(LIT) && entity instanceof LivingEntity) {
            entity.hurt(level.damageSources().hotFloor(), 1.0F);
        }
    }

    // 客户端粒子动画：烟雾和火焰
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (!state.getValue(LIT)) {
            return;
        }

        double x = pos.getX() + 0.5;
        double y = pos.getY();
        double z = pos.getZ() + 0.5;

        // 随机播放火焰声音
        if (random.nextDouble() < 0.1) {
            level.playLocalSound(
                    x, y, z,
                    SoundEvents.FURNACE_FIRE_CRACKLE,
                    SoundSource.BLOCKS,
                    1.0F, 1.0F, false
            );
        }

        Direction direction = state.getValue(FACING);
        Direction.Axis axis = direction.getAxis();

        double frontOffset = 0.52;
        double sideRandom = random.nextDouble() * 0.6 - 0.3;

        // 前面火焰粒子
        double particleX = x + direction.getStepX() * frontOffset;
        double particleY = y + random.nextDouble() * 0.375 + 0.4;
        double particleZ = z + direction.getStepZ() * frontOffset;

        if (axis == Direction.Axis.X) {
            particleZ += sideRandom;
        } else {
            particleX += sideRandom;
        }

        level.addParticle(ParticleTypes.SMOKE, particleX, particleY, particleZ, 0.0, 0.0, 0.0);
        level.addParticle(ParticleTypes.FLAME, particleX, particleY, particleZ, 0.0, 0.0, 0.0);

        // 顶部烟雾粒子
        double topSmokeX = x + random.nextDouble() * 0.4 - 0.2;
        double topSmokeY = pos.getY() + 1.05;
        double topSmokeZ = z + random.nextDouble() * 0.4 - 0.2;

        level.addParticle(ParticleTypes.SMOKE, topSmokeX, topSmokeY, topSmokeZ, 0.0, 0.03, 0.0);
    }
}
