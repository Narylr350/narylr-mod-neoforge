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

/**
 * 沉重系统事件处理类
 * 处理玩家手持沉重物品时的移动速度减速效果
 *
 * 注意：此类注册在游戏总线（NeoForge.EVENT_BUS）上，不是模组总线
 * 游戏总线用于游戏内事件（如 tick、玩家交互等）
 * 模组总线用于模组生命周期事件（如注册、初始化等）
 */
public class HeavySystemEvents {
    // 统一沉重减速 modifier 的 id
    private static final ResourceLocation HEAVY_MOVEMENT_SPEED_ID =
            ResourceLocation.fromNamespaceAndPath(NarylrMod.MOD_ID, "heavy_movement_speed");

    /**
     * 注册沉重系统事件到游戏总线
     * 使用 NeoForge.EVENT_BUS.addListener() 注册，这是官方推荐的方式
     */
    public static void register() {
        NeoForge.EVENT_BUS.addListener(HeavySystemEvents::onServerTick);
    }

    /**
     * 服务端 tick 事件处理
     * 每个游戏 tick 都会调用，用于更新所有玩家的沉重减速效果
     *
     * @param event 服务端 tick 事件，使用 Post 阶段确保在 tick 结束时执行
     */
    private static void onServerTick(ServerTickEvent.Post event) {
        for (ServerPlayer player : event.getServer().getPlayerList().getPlayers()) {
            updatePlayerHeavyPenalty(player);
        }
    }

    /**
     * 更新单个玩家的沉重减速效果
     */
    private static void updatePlayerHeavyPenalty(ServerPlayer player) {
        double penalty = calculateFinalHeavyPenalty(player);
        applyMovementSpeedPenalty(player, penalty);
    }

    /**
     * 计算玩家最终的沉重减速值
     * 主手物品和盔甲的减速取最大值（不叠加）
     */
    private static double calculateFinalHeavyPenalty(ServerPlayer player) {
        ItemStack mainHandStack = player.getMainHandItem();

        double mainHandPenalty = HeavyItemHelper.getBaseHeavyPenalty(mainHandStack);
        mainHandPenalty = applyLightweightReduction(player, mainHandStack, mainHandPenalty);

        double armorPenalty = calculateSteelArmorPenalty(player);

        return Math.max(mainHandPenalty, armorPenalty);
    }

    /**
     * 计算钢甲的沉重减速
     * 钢甲内部按部位累加：头盔 2%，胸甲 5%，护腿 5%，靴子 3%，满套 15%
     */
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

    /**
     * 判断玩家指定装备槽是否穿着指定物品
     */
    private static boolean isItemInSlot(ServerPlayer player, EquipmentSlot slot, Item item) {
        ItemStack stack = player.getItemBySlot(slot);
        return !stack.isEmpty() && stack.is(item);
    }

    /**
     * 轻盈附魔抵消沉重减速
     * 轻盈 I 抵消 1/3，轻盈 II 抵消 2/3，轻盈 III 完全抵消
     */
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
        Registry<Enchantment> enchantmentRegistry = player.registryAccess().registryOrThrow(Registries.ENCHANTMENT);

        Holder<Enchantment> lightweight = enchantmentRegistry.getHolderOrThrow(ModEnchantments.LIGHTWEIGHT);

        // 使用新的 API 获取附魔等级
        return stack.getEnchantmentLevel(lightweight);
    }

    /**
     * 给玩家添加或移除统一移动速度 modifier
     * 使用 ADD_MULTIPLIED_TOTAL 操作，-0.15 表示减速 15%
     */
    private static void applyMovementSpeedPenalty(ServerPlayer player, double penalty) {
        AttributeInstance movementSpeed = player.getAttribute(Attributes.MOVEMENT_SPEED);

        if (movementSpeed == null) {
            return;
        }

        // 获取当前 modifier，避免无变化时仍然删除再添加
        AttributeModifier existing = movementSpeed.getModifier(HEAVY_MOVEMENT_SPEED_ID);

        // 没有沉重效果
        if (penalty <= 0.0D) {
            if (existing != null) {
                movementSpeed.removeModifier(HEAVY_MOVEMENT_SPEED_ID);
            }
            return;
        }

        // 检查是否需要更新（数值有变化）
        double expected = -penalty;
        if (existing != null && existing.amount() == expected
                && existing.operation() == AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
            return; // 无需更新
        }

        movementSpeed.removeModifier(HEAVY_MOVEMENT_SPEED_ID);

        AttributeModifier modifier = new AttributeModifier(
                HEAVY_MOVEMENT_SPEED_ID,
                expected,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );

        movementSpeed.addTransientModifier(modifier);
    }

    private HeavySystemEvents() {
    }
}
