package com.narylr.narylrmod.item.material;

import com.narylr.narylrmod.item.HeavyItem;
import com.narylr.narylrmod.item.HeavyItemAttributes;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.block.Block;

// 钢块物品，实现 HeavyItem 接口，主手持有时减速 30%
public class SteelBlockItem extends BlockItem implements HeavyItem {
    public SteelBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    // 钢块系沉重减速
    @Override
    public double getHeavyPenalty(ItemStack stack) {
        return HeavyItemAttributes.STEEL_BLOCK_HEAVY_PENALTY;
    }

    // 创建钢块属性（攻击 +4）
    public static ItemAttributeModifiers createSteelBlockAttributes() {
        return HeavyItemAttributes.createSteelBlockAttributes();
    }
}
