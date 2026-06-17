package com.narylr.narylrmod.item;

import com.narylr.narylrmod.NarylrMod;
import com.narylr.narylrmod.block.ModBlocks;
import com.narylr.narylrmod.item.armor.ModArmorMaterials;
import com.narylr.narylrmod.item.material.SteelBlockItem;
import com.narylr.narylrmod.item.material.SteelIngotItem;
import com.narylr.narylrmod.item.tool.ModToolTiers;
import com.narylr.narylrmod.item.weapon.SteelFryingPanItem;
import com.narylr.narylrmod.item.weapon.SteelMorningStarItem;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

// 模组物品注册类，所有物品在这里统一注册
public class ModItems {
    // 物品延迟注册器
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(NarylrMod.MOD_ID);

    // 基础材料
    public static final DeferredItem<Item> STEEL_INGOT = ITEMS.register("steel_ingot",
            () -> new SteelIngotItem(new Item.Properties().attributes(SteelIngotItem.createSteelIngotAttributes())));

    public static final DeferredItem<Item> STEEL_NUGGET = ITEMS.register("steel_nugget",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> STEEL_BLOCK_ITEM = ITEMS.register("steel_block",
            () -> new SteelBlockItem(ModBlocks.STEEL_BLOCK.get(), new Item.Properties().attributes(SteelBlockItem.createSteelBlockAttributes())));

    public static final DeferredItem<Item> RAW_STEEL = ITEMS.register("raw_steel",
            () -> new Item(new Item.Properties()));

    // 方块物品
    public static final DeferredItem<Item> STEEL_FURNACE_ITEM = ITEMS.register("steel_furnace",
            () -> new BlockItem(ModBlocks.STEEL_FURNACE.get(), new Item.Properties()));

    // 钢工具
    public static final DeferredItem<Item> STEEL_SWORD = ITEMS.register("steel_sword",
            () -> new SwordItem(ModToolTiers.STEEL, new Item.Properties().attributes(
                    HeavyItemAttributes.addSteelIngotToolModifier(
                            SwordItem.createAttributes(ModToolTiers.STEEL, 4, -2.6F), "steel_sword"))));

    public static final DeferredItem<Item> STEEL_PICKAXE = ITEMS.register("steel_pickaxe",
            () -> new PickaxeItem(ModToolTiers.STEEL, new Item.Properties().attributes(
                    HeavyItemAttributes.addSteelIngotToolModifier(
                            PickaxeItem.createAttributes(ModToolTiers.STEEL, 1.0F, -3.0F), "steel_pickaxe"))));

    public static final DeferredItem<Item> STEEL_AXE = ITEMS.register("steel_axe",
            () -> new AxeItem(ModToolTiers.STEEL, new Item.Properties().attributes(
                    HeavyItemAttributes.addSteelIngotToolModifier(
                            AxeItem.createAttributes(ModToolTiers.STEEL, 6.0F, -3.2F), "steel_axe"))));

    public static final DeferredItem<Item> STEEL_SHOVEL = ITEMS.register("steel_shovel",
            () -> new ShovelItem(ModToolTiers.STEEL, new Item.Properties().attributes(
                    HeavyItemAttributes.addSteelIngotToolModifier(
                            ShovelItem.createAttributes(ModToolTiers.STEEL, 1.5F, -3.0F), "steel_shovel"))));

    public static final DeferredItem<Item> STEEL_HOE = ITEMS.register("steel_hoe",
            () -> new HoeItem(ModToolTiers.STEEL, new Item.Properties().attributes(
                    HeavyItemAttributes.addSteelIngotToolModifier(
                            HoeItem.createAttributes(ModToolTiers.STEEL, -1.0F, 0.0F), "steel_hoe"))));

    // 钢盔甲
    public static final DeferredItem<Item> STEEL_HELMET = ITEMS.register("steel_helmet",
            () -> new ArmorItem(ModArmorMaterials.STEEL, ArmorItem.Type.HELMET,
                    new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(37))));

    public static final DeferredItem<Item> STEEL_CHESTPLATE = ITEMS.register("steel_chestplate",
            () -> new ArmorItem(ModArmorMaterials.STEEL, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(37))));

    public static final DeferredItem<Item> STEEL_LEGGINGS = ITEMS.register("steel_leggings",
            () -> new ArmorItem(ModArmorMaterials.STEEL, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(37))));

    public static final DeferredItem<Item> STEEL_BOOTS = ITEMS.register("steel_boots",
            () -> new ArmorItem(ModArmorMaterials.STEEL, ArmorItem.Type.BOOTS,
                    new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(37))));

    // 下界合金钢盔甲
    public static final DeferredItem<Item> STEEL_NETHER_HELMET = ITEMS.register("steel_nether_helmet",
            () -> new ArmorItem(ModArmorMaterials.STEEL_NETHER, ArmorItem.Type.HELMET,
                    new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(37))));

    public static final DeferredItem<Item> STEEL_NETHER_CHESTPLATE = ITEMS.register("steel_nether_chestplate",
            () -> new ArmorItem(ModArmorMaterials.STEEL_NETHER, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(37))));

    public static final DeferredItem<Item> STEEL_NETHER_LEGGINGS = ITEMS.register("steel_nether_leggings",
            () -> new ArmorItem(ModArmorMaterials.STEEL_NETHER, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(37))));

    public static final DeferredItem<Item> STEEL_NETHER_BOOTS = ITEMS.register("steel_nether_boots",
            () -> new ArmorItem(ModArmorMaterials.STEEL_NETHER, ArmorItem.Type.BOOTS,
                    new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(37))));

    // 下界合金钢工具
    public static final DeferredItem<Item> STEEL_NETHER_SWORD = ITEMS.register("steel_nether_sword",
            () -> new SwordItem(ModToolTiers.STEEL_NETHER, new Item.Properties().attributes(
                    HeavyItemAttributes.addSteelIngotToolModifier(
                            SwordItem.createAttributes(ModToolTiers.STEEL_NETHER, 4, -2.6F), "steel_nether_sword"))));

    public static final DeferredItem<Item> STEEL_NETHER_PICKAXE = ITEMS.register("steel_nether_pickaxe",
            () -> new PickaxeItem(ModToolTiers.STEEL_NETHER, new Item.Properties().attributes(
                    HeavyItemAttributes.addSteelIngotToolModifier(
                            PickaxeItem.createAttributes(ModToolTiers.STEEL_NETHER, 1.0F, -3.0F), "steel_nether_pickaxe"))));

    public static final DeferredItem<Item> STEEL_NETHER_SHOVEL = ITEMS.register("steel_nether_shovel",
            () -> new ShovelItem(ModToolTiers.STEEL_NETHER, new Item.Properties().attributes(
                    HeavyItemAttributes.addSteelIngotToolModifier(
                            ShovelItem.createAttributes(ModToolTiers.STEEL_NETHER, 1.5F, -3.0F), "steel_nether_shovel"))));

    public static final DeferredItem<Item> STEEL_NETHER_AXE = ITEMS.register("steel_nether_axe",
            () -> new AxeItem(ModToolTiers.STEEL_NETHER, new Item.Properties().attributes(
                    HeavyItemAttributes.addSteelIngotToolModifier(
                            AxeItem.createAttributes(ModToolTiers.STEEL_NETHER, 7.0F, -3.0F), "steel_nether_axe"))));

    public static final DeferredItem<Item> STEEL_NETHER_HOE = ITEMS.register("steel_nether_hoe",
            () -> new HoeItem(ModToolTiers.STEEL_NETHER, new Item.Properties().attributes(
                    HeavyItemAttributes.addSteelIngotToolModifier(
                            HoeItem.createAttributes(ModToolTiers.STEEL_NETHER, -4.0F, 0.0F), "steel_nether_hoe"))));

    // 特殊武器
    public static final DeferredItem<Item> STEEL_FRYING_PAN = ITEMS.register("steel_frying_pan",
            () -> new SteelFryingPanItem(ModToolTiers.STEEL, new Item.Properties().attributes(
                    HeavyItemAttributes.addSteelIngotToolModifier(
                            SwordItem.createAttributes(ModToolTiers.STEEL, 5, -2.8F), "steel_frying_pan"))));

    public static final DeferredItem<Item> STEEL_MORNING_STAR = ITEMS.register("steel_morning_star",
            () -> new SteelMorningStarItem(ModToolTiers.STEEL, new Item.Properties().attributes(
                    HeavyItemAttributes.addSteelBlockToolModifier(
                            SwordItem.createAttributes(ModToolTiers.STEEL, 8, -3.4F), "steel_morning_star"))));

    // 下界合金钢锭
    public static final DeferredItem<Item> STEEL_NETHER_INGOT = ITEMS.register("steel_nether_ignot",
            () -> new Item(new Item.Properties()));

    // 注册所有物品到模组总线
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
