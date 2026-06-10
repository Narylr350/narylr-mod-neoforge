package com.narylr.narylrmod.item;

import net.minecraft.world.item.ItemStack;

// 沉重物品接口，实现此接口的物品会降低玩家移动速度
public interface HeavyItem {
    // 返回物品的基础移动速度惩罚，正数表示减速比例（如 0.15 表示减速 15%）
    double getHeavyPenalty(ItemStack stack);
}
