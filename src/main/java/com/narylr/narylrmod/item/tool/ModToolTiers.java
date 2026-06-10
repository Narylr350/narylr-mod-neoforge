package com.narylr.narylrmod.item.tool;

import com.narylr.narylrmod.item.ModItems;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

// 模组工具材料定义
public class ModToolTiers {
    // 钢工具材料：定位为钻石的工业替代路线
    public static final Tier STEEL = new Tier() {
        // 使用钻石级掉落规则
        @Override
        public TagKey<Block> getIncorrectBlocksForDrops() {
            return BlockTags.INCORRECT_FOR_DIAMOND_TOOL;
        }

        // 耐久 1800（高于钻石的 1561）
        @Override
        public int getUses() {
            return 1800;
        }

        // 挖掘速度 8.5（略高于钻石）
        @Override
        public float getSpeed() {
            return 8.5F;
        }

        // 攻击加成（和钻石同级）
        @Override
        public float getAttackDamageBonus() {
            return 3.0F;
        }

        // 附魔能力（低于钻石）
        @Override
        public int getEnchantmentValue() {
            return 8;
        }

        // 修复材料：钢锭
        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.of(ModItems.STEEL_INGOT.get());
        }
    };
}
