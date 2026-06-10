package com.narylr.narylrmod.block.entity;

import com.narylr.narylrmod.NarylrMod;
import com.narylr.narylrmod.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

// 模组方块实体注册类
public class ModBlockEntities {
    // 方块实体类型延迟注册器
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(net.minecraft.core.registries.Registries.BLOCK_ENTITY_TYPE, NarylrMod.MOD_ID);

    // 钢熔炉方块实体类型
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SteelFurnaceBlockEntity>> STEEL_FURNACE_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("steel_furnace", () ->
                    BlockEntityType.Builder.of(SteelFurnaceBlockEntity::new, ModBlocks.STEEL_FURNACE.get()).build(null));

    // 注册方块实体类型到模组总线
    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
