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
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

// 钢制物品提示文本处理
public class SteelItemTooltip {

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack itemStack = event.getItemStack();

        // 沉重提示
        double basePenalty = HeavyItemHelper.getBaseHeavyPenalty(itemStack);
        if (basePenalty > 0.0D) {
            int level = getLightweightLevel(itemStack);
            double finalPenalty = applyLightweightReduction(basePenalty, level);
            int basePct = toPercent(basePenalty);
            int finalPct = toPercent(finalPenalty);

            if (level >= 3 || finalPenalty <= 0.0D) {
                event.getToolTip().add(Component.translatable("tooltip.narylr_mod.heavy_item.lightweight_full", basePct)
                        .withStyle(ChatFormatting.AQUA));
            } else if (level > 0) {
                event.getToolTip().add(Component.translatable("tooltip.narylr_mod.heavy_item.heavy_reduced", finalPct, basePct)
                        .withStyle(ChatFormatting.AQUA));
            } else {
                event.getToolTip().add(Component.translatable("tooltip.narylr_mod.heavy_item.heavy", basePct)
                        .withStyle(ChatFormatting.RED));
            }
        }

        // 钢工具提示
        if (itemStack.is(ModTags.STEEL_TOOLS)) {
            event.getToolTip().add(Component.translatable("tooltip.narylr_mod.steel_tool.durable")
                    .withStyle(ChatFormatting.GREEN));
        }

        // 钢材料提示
        if (itemStack.is(ModItems.STEEL_INGOT.get()) || itemStack.is(ModItems.STEEL_BLOCK_ITEM.get())) {
            event.getToolTip().add(Component.translatable("tooltip.narylr_mod.steel_material.hard")
                    .withStyle(ChatFormatting.GREEN));
        }

        // 普通钢甲沉重提示
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

        // 钢甲通用提示（仅普通钢甲在标签内）
        if (itemStack.is(ModTags.STEEL_ARMORS)) {
            event.getToolTip().add(Component.translatable("tooltip.narylr_mod.steel_armor.durable")
                    .withStyle(ChatFormatting.GREEN));
            event.getToolTip().add(Component.translatable("tooltip.narylr_mod.steel_armor.rule")
                    .withStyle(ChatFormatting.YELLOW));
        }
    }

    // 轻盈附魔抵消计算
    private static double applyLightweightReduction(double penalty, int level) {
        if (penalty <= 0.0D || level <= 0) {
            return penalty;
        }
        int clamped = Math.min(level, 3);
        return penalty * (1.0D - clamped / 3.0D);
    }

    // 获取物品上的轻盈附魔等级
    private static int getLightweightLevel(ItemStack stack) {
        if (stack.isEmpty() || Minecraft.getInstance().level == null) {
            return 0;
        }
        Registry<Enchantment> registry = Minecraft.getInstance().level.registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT);
        Holder<Enchantment> lightweight = registry.getHolderOrThrow(ModEnchantments.LIGHTWEIGHT);
        return stack.getEnchantmentLevel(lightweight);
    }

    private static int toPercent(double value) {
        return (int) Math.round(value * 100.0D);
    }
}
