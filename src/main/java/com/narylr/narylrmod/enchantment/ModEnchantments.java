package com.narylr.narylrmod.enchantment;

import com.narylr.narylrmod.NarylrMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

// 模组附魔定义类
// 1.21+ 的附魔主要由数据包 JSON 定义，这里只保存代码读取用的 ResourceKey
public class ModEnchantments {
    // 轻盈附魔：减少沉重物品的移动速度惩罚
    public static final ResourceKey<Enchantment> LIGHTWEIGHT = ResourceKey.create(
            Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(NarylrMod.MOD_ID, "lightweight")
    );

    private ModEnchantments() {
    }
}
