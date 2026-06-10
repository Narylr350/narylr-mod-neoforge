package com.narylr.narylrmod.item;

import com.narylr.narylrmod.NarylrMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.component.ItemAttributeModifiers;

// 沉重物品属性常量和工具方法
public class HeavyItemAttributes {
    // 钢锭系沉重减速：主手持有时移动速度降低 15%
    public static final double STEEL_INGOT_HEAVY_PENALTY = 0.15D;

    // 钢块系沉重减速：主手持有时移动速度降低 30%
    public static final double STEEL_BLOCK_HEAVY_PENALTY = 0.30D;

    // 钢甲各部位沉重减速
    public static final double STEEL_HELMET_HEAVY_PENALTY = 0.02D;
    public static final double STEEL_CHESTPLATE_HEAVY_PENALTY = 0.05D;
    public static final double STEEL_LEGGINGS_HEAVY_PENALTY = 0.05D;
    public static final double STEEL_BOOTS_HEAVY_PENALTY = 0.03D;

    // 满套钢甲最大沉重减速：15%
    public static final double FULL_STEEL_ARMOR_HEAVY_PENALTY = 0.15D;

    // 钢锭/钢块本体攻击加成
    public static final double STEEL_INGOT_ATTACK_DAMAGE_BONUS = 2.0D;
    public static final double STEEL_BLOCK_ATTACK_DAMAGE_BONUS = 4.0D;

    // 创建钢锭本体属性：攻击 +2
    public static ItemAttributeModifiers createSteelIngotAttributes() {
        return ItemAttributeModifiers.builder()
                .add(
                        Attributes.ATTACK_DAMAGE,
                        createModifier("steel_ingot_attack_damage", STEEL_INGOT_ATTACK_DAMAGE_BONUS, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND
                )
                .build();
    }

    // 创建钢块本体属性：攻击 +4
    public static ItemAttributeModifiers createSteelBlockAttributes() {
        return ItemAttributeModifiers.builder()
                .add(
                        Attributes.ATTACK_DAMAGE,
                        createModifier("steel_block_attack_damage", STEEL_BLOCK_ATTACK_DAMAGE_BONUS, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND
                )
                .build();
    }

    // 钢锭系工具属性修饰器（目前直接返回原属性）
    public static ItemAttributeModifiers addSteelIngotToolModifier(ItemAttributeModifiers modifiers, String name) {
        return modifiers;
    }

    // 钢块系工具属性修饰器（目前直接返回原属性）
    public static ItemAttributeModifiers addSteelBlockToolModifier(ItemAttributeModifiers modifiers, String name) {
        return modifiers;
    }

    // 创建属性修饰器
    private static AttributeModifier createModifier(String name, double amount, AttributeModifier.Operation operation) {
        return new AttributeModifier(
                ResourceLocation.fromNamespaceAndPath(NarylrMod.MOD_ID, name),
                amount,
                operation
        );
    }

    private HeavyItemAttributes() {
    }
}
