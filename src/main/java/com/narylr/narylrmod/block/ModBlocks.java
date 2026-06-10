package com.narylr.narylrmod.block;

import com.narylr.narylrmod.NarylrMod;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

// 模组方块注册类
public class ModBlocks {
    // 方块延迟注册器
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(NarylrMod.MOD_ID);

    // 钢块：硬度6，爆炸抗性8，需要正确工具掉落
    public static final DeferredBlock<Block> STEEL_BLOCK = BLOCKS.registerSimpleBlock("steel_block",
            BlockBehaviour.Properties.of()
                    .strength(6.0F, 8.0F)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.METAL));

    // 钢熔炉：硬度10，可自定义配方的熔炉
    public static final DeferredBlock<Block> STEEL_FURNACE = BLOCKS.register("steel_furnace",
            () -> new SteelFurnaceBlock(BlockBehaviour.Properties.of()
                    .strength(6.0F, 10.0F)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.METAL)));

    // 注册所有方块到模组总线
    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
