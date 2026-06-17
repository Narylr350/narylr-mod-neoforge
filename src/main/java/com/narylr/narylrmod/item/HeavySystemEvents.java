package com.narylr.narylrmod.item;

import com.narylr.narylrmod.NarylrMod;
import com.narylr.narylrmod.enchantment.ModEnchantments;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

// 沉重系统事件处理，统一管理玩家移动速度减速
public class HeavySystemEvents {

    // 统一沉重减速 modifier 的 id
    private static final ResourceLocation HEAVY_MOVEMENT_SPEED_ID =
            ResourceLocation.fromNamespaceAndPath(NarylrMod.MOD_ID, "heavy_movement_speed");

    // 注册到游戏总线
    public static void register() {
        NeoForge.EVENT_BUS.addListener(HeavySystemEvents::onServerTick);
    }

    // 服务端 tick：每 tick 更新所有玩家
    private static void onServerTick(ServerTickEvent.Post event) {
        for (ServerPlayer player : event.getServer().getPlayerList().getPlayers()) {
            double penalty = calculateFinalHeavyPenalty(player);
            applyMovementSpeedPenalty(player, penalty);
        }
    }

    // 计算最终减速值（主手和盔甲取最大值，不叠加）
    private static double calculateFinalHeavyPenalty(ServerPlayer player) {
        ItemStack mainHandStack = player.getMainHandItem();

        double mainHandPenalty = HeavyItemHelper.getBaseHeavyPenalty(mainHandStack);
        mainHandPenalty = applyLightweightReduction(player, mainHandStack, mainHandPenalty);

        double armorPenalty = calculateSteelArmorPenalty(player);

        return Math.max(mainHandPenalty, armorPenalty);
    }

    // 计算钢甲沉重：头盔2%/胸甲5%/护腿5%/靴子3%，满套上限15%
    private static double calculateSteelArmorPenalty(ServerPlayer player) {
        double penalty = 0.0D;

        if (isItemInSlot(player, EquipmentSlot.HEAD, ModItems.STEEL_HELMET.get())) {
            penalty += HeavyItemAttributes.STEEL_HELMET_HEAVY_PENALTY;
        }
        if (isItemInSlot(player, EquipmentSlot.CHEST, ModItems.STEEL_CHESTPLATE.get())) {
            penalty += HeavyItemAttributes.STEEL_CHESTPLATE_HEAVY_PENALTY;
        }
        if (isItemInSlot(player, EquipmentSlot.LEGS, ModItems.STEEL_LEGGINGS.get())) {
            penalty += HeavyItemAttributes.STEEL_LEGGINGS_HEAVY_PENALTY;
        }
        if (isItemInSlot(player, EquipmentSlot.FEET, ModItems.STEEL_BOOTS.get())) {
            penalty += HeavyItemAttributes.STEEL_BOOTS_HEAVY_PENALTY;
        }

        return Math.min(penalty, HeavyItemAttributes.FULL_STEEL_ARMOR_HEAVY_PENALTY);
    }

    // 判断玩家指定装备槽是否穿着指定物品
    private static boolean isItemInSlot(ServerPlayer player, EquipmentSlot slot, Item item) {
        ItemStack stack = player.getItemBySlot(slot);
        return !stack.isEmpty() && stack.is(item);
    }

    // 轻盈附魔抵消沉重减速：I级1/3，II级2/3，III级完全抵消
    private static double applyLightweightReduction(ServerPlayer player, ItemStack stack, double penalty) {
        if (penalty <= 0.0D || stack.isEmpty()) {
            return penalty;
        }

        int lightweightLevel = getLightweightLevel(player, stack);
        if (lightweightLevel <= 0) {
            return penalty;
        }

        int clampedLevel = Math.min(lightweightLevel, 3);
        double reductionRate = clampedLevel / 3.0D;
        return penalty * (1.0D - reductionRate);
    }

    // 获取物品上的轻盈附魔等级
    private static int getLightweightLevel(ServerPlayer player, ItemStack stack) {
        Registry<Enchantment> registry = player.registryAccess().registryOrThrow(Registries.ENCHANTMENT);
        Holder<Enchantment> lightweight = registry.getHolderOrThrow(ModEnchantments.LIGHTWEIGHT);
        return stack.getEnchantmentLevel(lightweight);
    }

    // 应用移动速度 modifier，只在值变化时才更新
    private static void applyMovementSpeedPenalty(ServerPlayer player, double penalty) {
        AttributeInstance movementSpeed = player.getAttribute(Attributes.MOVEMENT_SPEED);
        if (movementSpeed == null) {
            return;
        }

        AttributeModifier existing = movementSpeed.getModifier(HEAVY_MOVEMENT_SPEED_ID);

        // 没有沉重效果，只清理旧 modifier
        if (penalty <= 0.0D) {
            if (existing != null) {
                movementSpeed.removeModifier(HEAVY_MOVEMENT_SPEED_ID);
            }
            return;
        }

        // 检查是否需要更新
        double expected = -penalty;
        if (existing != null
                && existing.amount() == expected
                && existing.operation() == AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
            return;
        }

        movementSpeed.removeModifier(HEAVY_MOVEMENT_SPEED_ID);
        movementSpeed.addTransientModifier(new AttributeModifier(
                HEAVY_MOVEMENT_SPEED_ID,
                expected,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        ));
    }

    private HeavySystemEvents() {}
}
