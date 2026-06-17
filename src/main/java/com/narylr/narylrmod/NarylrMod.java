package com.narylr.narylrmod;

import com.narylr.narylrmod.block.ModBlocks;
import com.narylr.narylrmod.block.entity.ModBlockEntities;
import com.narylr.narylrmod.item.HeavySystemEvents;
import com.narylr.narylrmod.item.ModItemGroup;
import com.narylr.narylrmod.item.ModItems;
import com.narylr.narylrmod.recipe.ModRecipes;
import com.narylr.narylrmod.screen.ModMenus;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// 主模组类，@Mod 的值必须与 neoforge.mods.toml 中的 modId 一致
@Mod(NarylrMod.MOD_ID)
public class NarylrMod {

    public static final String MOD_ID = "narylr_mod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    // FML 自动识别并注入 IEventBus 和 ModContainer
    public NarylrMod(IEventBus modEventBus, ModContainer modContainer) {
        LOGGER.info("Narylr Mod loading");

        // 注册
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModRecipes.register(modEventBus);
        ModMenus.register(modEventBus);
        ModItemGroup.register(modEventBus);

        // 游戏事件
        HeavySystemEvents.register();
    }
}
