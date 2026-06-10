package com.narylr.narylrmod.item.weapon;

import com.narylr.narylrmod.item.HeavyItem;
import com.narylr.narylrmod.item.HeavyItemAttributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

// 钢平底锅：钢锭系重武器，伤害高于钢剑但攻速更慢，100% 暴击
public class SteelFryingPanItem extends SwordItem implements HeavyItem {
    // 暴击概率（预留）
    private static final float CRITICAL_CHANCE = 1.0F;

    public SteelFryingPanItem(Tier tier, Item.Properties properties) {
        super(tier, properties);
    }

    // 钢锭系沉重减速
    @Override
    public double getHeavyPenalty(ItemStack stack) {
        return HeavyItemAttributes.STEEL_INGOT_HEAVY_PENALTY;
    }

    // 判断是否触发暴击
    public boolean shouldTriggerCriticalHit() {
        return CRITICAL_CHANCE >= 1.0F;
    }

    // 暴击效果（预留）
    public void applyFryingPanCriticalEffect() {
        // TODO 后续实现 100% 暴击 buff
    }
}
