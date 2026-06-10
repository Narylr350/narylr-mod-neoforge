package com.narylr.narylrmod.item.material;

import com.narylr.narylrmod.item.HeavyItem;
import com.narylr.narylrmod.item.HeavyItemAttributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

// 钢锭物品，实现 HeavyItem 接口，主手持有时减速 15%
public class SteelIngotItem extends Item implements HeavyItem {
    public SteelIngotItem(Properties properties) {
        super(properties);
    }

    // 钢锭系沉重减速
    @Override
    public double getHeavyPenalty(ItemStack stack) {
        return HeavyItemAttributes.STEEL_INGOT_HEAVY_PENALTY;
    }

    // 创建钢锭属性（攻击 +2）
    public static ItemAttributeModifiers createSteelIngotAttributes() {
        return HeavyItemAttributes.createSteelIngotAttributes();
    }
}
