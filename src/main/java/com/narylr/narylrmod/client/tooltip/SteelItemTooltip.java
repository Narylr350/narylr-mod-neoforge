package com.narylr.narylrmod.client.tooltip;

import com.narylr.narylrmod.enchantment.ModEnchantments;
import com.narylr.narylrmod.item.HeavyItemHelper;
import com.narylr.narylrmod.item.ModItems;
import com.narylr.narylrmod.tag.ModTags;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

// 钢制物品提示文本处理类
public class SteelItemTooltip {

    // 物品提示文本事件处理
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack itemStack = event.getItemStack();
        double baseHeavyPenalty = HeavyItemHelper.getBaseHeavyPenalty(itemStack);

        if (baseHeavyPenalty > 0.0D) {
            int lightweightLevel = getLightweightLevel(itemStack);
            double finalHeavyPenalty = applyLightweightReduction(baseHeavyPenalty, lightweightLevel);

            int basePercent = toPercent(baseHeavyPenalty);
            int finalPercent = toPercent(finalHeavyPenalty);

            if (lightweightLevel >= 3 || finalHeavyPenalty <= 0.0D) {
                event.getToolTip().add(Component.translatable("tooltip.narylr_mod.heavy_item.lightweight_full", basePercent)
                        .withStyle(ChatFormatting.AQUA));
            } else if (lightweightLevel > 0) {
                event.getToolTip().add(Component.translatable("tooltip.narylr_mod.heavy_item.heavy_reduced", finalPercent, basePercent)
                        .withStyle(ChatFormatting.AQUA));
            } else {
                event.getToolTip().add(Component.translatable("tooltip.narylr_mod.heavy_item.heavy", basePercent)
                        .withStyle(ChatFormatting.RED));
            }
        }

        if (itemStack.is(ModTags.STEEL_TOOLS)) {
            event.getToolTip().add(Component.translatable("tooltip.narylr_mod.steel_tool.durable")
                    .withStyle(ChatFormatting.GREEN));
        }

        if (itemStack.is(ModItems.STEEL_INGOT.get())
                || itemStack.is(ModItems.STEEL_BLOCK_ITEM.get())) {
            event.getToolTip().add(Component.translatable("tooltip.narylr_mod.steel_material.hard")
                    .withStyle(ChatFormatting.GREEN));
        }

        if (itemStack.is(ModItems.STEEL_HELMET.get())) {
            event.getToolTip().add(Component.translatable("tooltip.narylr_mod.steel_armor.heavy", 2)
                    .withStyle(ChatFormatting.RED));
        }

        if (itemStack.is(ModItems.STEEL_CHESTPLATE.get())) {
            event.getToolTip().add(Component.translatable("tooltip.narylr_mod.steel_armor.heavy", 5)
                    .withStyle(ChatFormatting.RED));
        }

        if (itemStack.is(ModItems.STEEL_LEGGINGS.get())) {
            event.getToolTip().add(Component.translatable("tooltip.narylr_mod.steel_armor.heavy", 5)
                    .withStyle(ChatFormatting.RED));
        }

        if (itemStack.is(ModItems.STEEL_BOOTS.get())) {
            event.getToolTip().add(Component.translatable("tooltip.narylr_mod.steel_armor.heavy", 3)
                    .withStyle(ChatFormatting.RED));
        }

        if (itemStack.is(ModTags.STEEL_ARMORS)) {
            event.getToolTip().add(Component.translatable("tooltip.narylr_mod.steel_armor.durable")
                    .withStyle(ChatFormatting.GREEN));
            event.getToolTip().add(Component.translatable("tooltip.narylr_mod.steel_armor.rule")
                    .withStyle(ChatFormatting.YELLOW));
        }
    }

    private SteelItemTooltip() {
    }

    private static double applyLightweightReduction(double penalty, int lightweightLevel) {
        if (penalty <= 0.0D || lightweightLevel <= 0) {
            return penalty;
        }

        int clampedLevel = Math.min(lightweightLevel, 3);
        double reductionRate = clampedLevel / 3.0D;

        return penalty * (1.0D - reductionRate);
    }

    // 获取物品上的轻盈附魔等级
    private static int getLightweightLevel(ItemStack stack) {
        if (stack.isEmpty() || Minecraft.getInstance().level == null) {
            return 0;
        }

        Registry<Enchantment> enchantmentRegistry = Minecraft.getInstance()
                .level
                .registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT);

        Holder<Enchantment> lightweight = enchantmentRegistry.getHolderOrThrow(
                ModEnchantments.LIGHTWEIGHT
        );

        // 使用新的 API 获取附魔等级
        return stack.getEnchantmentLevel(lightweight);
    }

    private static int toPercent(double value) {
        return (int) Math.round(value * 100.0D);
    }
}
