package com.narylr.narylrmod.item.weapon;

import com.narylr.narylrmod.item.HeavyItem;
import com.narylr.narylrmod.item.HeavyItemAttributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

// 钢狼牙棒：钢块系重武器，伤害更高但主手持有减速 30%，攻速也更慢
public class SteelMorningStarItem extends SwordItem implements HeavyItem {
    // 流血持续时间（tick）
    private static final int BLEED_DURATION_TICKS = 100;

    // 流血伤害
    private static final float BLEED_DAMAGE = 1.0F;

    public SteelMorningStarItem(Tier tier, Item.Properties properties) {
        super(tier, properties);
    }

    // 钢块系沉重减速
    @Override
    public double getHeavyPenalty(ItemStack stack) {
        return HeavyItemAttributes.STEEL_BLOCK_HEAVY_PENALTY;
    }

    // 判断是否可以施加流血
    public boolean canApplyBleeding() {
        return true;
    }

    // 流血效果（预留）
    public void applyBleedingEffect() {
        // TODO 后续实现流血 buff
    }
}
