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

/**
 * 主模组类
 * &#064;Mod  注解的值必须与 META-INF/neoforge.mods.toml 中的 modId 一致
 * FML 会自动识别构造函数中的 IEventBus 和 ModContainer 参数并注入
 */
@Mod(NarylrMod.MOD_ID)
public class NarylrMod {
    public static final String MOD_ID = "narylr_mod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    /**
     * 模组构造函数
     * @param modEventBus 模组总线，用于注册 DeferredRegister 和模组事件
     * @param modContainer 模组容器，用于注册配置等
     */
    public NarylrMod(IEventBus modEventBus, ModContainer modContainer) {
        LOGGER.info("Narylr Mod loading");

        // 注册各类 DeferredRegister 到模组总线
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModRecipes.register(modEventBus);
        ModMenus.register(modEventBus);
        ModItemGroup.register(modEventBus);

        // 注册游戏事件（服务端 tick 事件）
        HeavySystemEvents.register();
    }
}
