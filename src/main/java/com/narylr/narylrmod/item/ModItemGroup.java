package com.narylr.narylrmod.item;

import com.narylr.narylrmod.NarylrMod;
import com.narylr.narylrmod.enchantment.ModEnchantments;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

// 模组创造模式标签页注册类
public class ModItemGroup {
    // 创造模式标签页延迟注册器
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, NarylrMod.MOD_ID);

    // 钢工艺标签页，包含所有模组物品和附魔书
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> STEEL_EXPANSION_GROUP =
            CREATIVE_MODE_TABS.register("steel_craft", () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.STEEL_INGOT.get()))
                    .title(Component.translatable("itemGroup.narylr_mod.steel_craft"))
                    .displayItems((itemDisplayParameters, output) -> {
                        // 基础材料
                        output.accept(ModItems.STEEL_INGOT.get());
                        output.accept(ModItems.STEEL_NUGGET.get());
                        output.accept(ModItems.STEEL_BLOCK_ITEM.get());
                        output.accept(ModItems.RAW_STEEL.get());
                        output.accept(ModItems.STEEL_FURNACE_ITEM.get());
                        // 钢工具
                        output.accept(ModItems.STEEL_SWORD.get());
                        output.accept(ModItems.STEEL_PICKAXE.get());
                        output.accept(ModItems.STEEL_AXE.get());
                        output.accept(ModItems.STEEL_SHOVEL.get());
                        output.accept(ModItems.STEEL_HOE.get());
                        // 钢盔甲
                        output.accept(ModItems.STEEL_HELMET.get());
                        output.accept(ModItems.STEEL_CHESTPLATE.get());
                        output.accept(ModItems.STEEL_LEGGINGS.get());
                        output.accept(ModItems.STEEL_BOOTS.get());
                        // 下界合金钢工具
                        output.accept(ModItems.STEEL_NETHER_SWORD.get());
                        output.accept(ModItems.STEEL_NETHER_PICKAXE.get());
                        output.accept(ModItems.STEEL_NETHER_AXE.get());
                        output.accept(ModItems.STEEL_NETHER_HOE.get());
                        output.accept(ModItems.STEEL_NETHER_SHOVEL.get());
                        // 特殊武器
                        output.accept(ModItems.STEEL_FRYING_PAN.get());
                        output.accept(ModItems.STEEL_MORNING_STAR.get());
                        // 下界合金钢盔甲
                        output.accept(ModItems.STEEL_NETHER_HELMET.get());
                        output.accept(ModItems.STEEL_NETHER_CHESTPLATE.get());
                        output.accept(ModItems.STEEL_NETHER_LEGGINGS.get());
                        output.accept(ModItems.STEEL_NETHER_BOOTS.get());

                        // 轻盈附魔书
                        itemDisplayParameters.holders()
                                .lookup(Registries.ENCHANTMENT)
                                .flatMap(enchantmentRegistry -> enchantmentRegistry.get(ModEnchantments.LIGHTWEIGHT))
                                .ifPresent(lightweight -> {
                                    output.accept(createEnchantedBook(lightweight, 1));
                                    output.accept(createEnchantedBook(lightweight, 2));
                                    output.accept(createEnchantedBook(lightweight, 3));
                                });
                    })
                    .build());

    // 注册标签页到模组总线
    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }

    // 创建附魔书物品
    private static ItemStack createEnchantedBook(Holder<Enchantment> enchantment, int level) {
        return EnchantedBookItem.createForEnchantment(new EnchantmentInstance(enchantment, level));
    }
}
