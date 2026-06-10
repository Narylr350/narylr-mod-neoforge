package com.narylr.narylrmod.screen;

import com.narylr.narylrmod.NarylrMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

// 模组菜单类型注册类
public class ModMenus {
    // 菜单类型延迟注册器
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, NarylrMod.MOD_ID);

    // 钢熔炉菜单类型
    public static final DeferredHolder<MenuType<?>, MenuType<SteelFurnaceMenu>> STEEL_FURNACE_MENU =
            MENUS.register("steel_furnace", () -> new MenuType<>(SteelFurnaceMenu::new, null));

    // 注册菜单类型到模组总线
    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
