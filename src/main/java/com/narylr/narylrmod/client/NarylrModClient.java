package com.narylr.narylrmod.client;

import com.narylr.narylrmod.NarylrMod;
import com.narylr.narylrmod.client.screen.SteelFurnaceScreen;
import com.narylr.narylrmod.client.tooltip.SteelItemTooltip;
import com.narylr.narylrmod.screen.ModMenus;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.NeoForge;

// 客户端事件处理类
@EventBusSubscriber(modid = NarylrMod.MOD_ID, value = Dist.CLIENT)
public class NarylrModClient {

    // 客户端初始化事件
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        NarylrMod.LOGGER.info("Client setup");

        // 注册物品提示文本事件监听器到游戏总线
        NeoForge.EVENT_BUS.register(SteelItemTooltip.class);
    }

    // 注册菜单屏幕事件
    @SubscribeEvent
    public static void onRegisterMenuScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenus.STEEL_FURNACE_MENU.get(), SteelFurnaceScreen::new);
    }
}
