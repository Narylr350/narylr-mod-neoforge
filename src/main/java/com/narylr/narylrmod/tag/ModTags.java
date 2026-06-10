package com.narylr.narylrmod.tag;

import com.narylr.narylrmod.NarylrMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

// 模组物品标签定义
public class ModTags {
    // 炼钢配方可使用的碳源材料（如煤炭、木炭等）
    public static final TagKey<Item> STEEL_CARBON_SOURCES = TagKey.create(
            Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath(NarylrMod.MOD_ID, "steel_carbon_sources")
    );

    // 钢制工具物品标签
    public static final TagKey<Item> STEEL_TOOLS = TagKey.create(
            Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath(NarylrMod.MOD_ID, "steel_tools")
    );

    // 钢制盔甲物品标签
    public static final TagKey<Item> STEEL_ARMORS = TagKey.create(
            Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath(NarylrMod.MOD_ID, "steel_armors")
    );
}
