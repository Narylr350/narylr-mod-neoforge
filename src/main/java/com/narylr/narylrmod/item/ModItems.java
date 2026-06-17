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
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

// 模组物品注册
public class ModItems {

    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(NarylrMod.MOD_ID);

    private static final Tier STEEL = ModToolTiers.STEEL;
    private static final Tier STEEL_NETHER = ModToolTiers.STEEL_NETHER;

    // ===== 材料 =====
    public static final DeferredItem<Item> STEEL_INGOT = ITEMS.register(
            "steel_ingot",
            () -> new SteelIngotItem(prop().attributes(SteelIngotItem.createSteelIngotAttributes()))
    );
    public static final DeferredItem<Item> STEEL_NUGGET = ITEMS.register(
            "steel_nugget", () -> new Item(prop())
    );
    public static final DeferredItem<Item> STEEL_BLOCK_ITEM = ITEMS.register(
            "steel_block",
            () -> new SteelBlockItem(ModBlocks.STEEL_BLOCK.get(),
                    prop().attributes(SteelBlockItem.createSteelBlockAttributes()))
    );
    public static final DeferredItem<Item> RAW_STEEL = ITEMS.register(
            "raw_steel", () -> new Item(prop())
    );

    // ===== 方块物品 =====
    public static final DeferredItem<Item> STEEL_FURNACE_ITEM = ITEMS.register(
            "steel_furnace",
            () -> new BlockItem(ModBlocks.STEEL_FURNACE.get(), prop())
    );

    // ===== 钢工具 =====
    public static final DeferredItem<Item> STEEL_SWORD = ITEMS.register(
            "steel_sword",
            () -> new SwordItem(STEEL, prop(SwordItem.createAttributes(STEEL, 4, -2.6F)))
    );
    public static final DeferredItem<Item> STEEL_PICKAXE = ITEMS.register(
            "steel_pickaxe",
            () -> new PickaxeItem(STEEL, prop(PickaxeItem.createAttributes(STEEL, 1.0F, -3.0F)))
    );
    public static final DeferredItem<Item> STEEL_AXE = ITEMS.register(
            "steel_axe",
            () -> new AxeItem(STEEL, prop(AxeItem.createAttributes(STEEL, 6.0F, -3.2F)))
    );
    public static final DeferredItem<Item> STEEL_SHOVEL = ITEMS.register(
            "steel_shovel",
            () -> new ShovelItem(STEEL, prop(ShovelItem.createAttributes(STEEL, 1.5F, -3.0F)))
    );
    public static final DeferredItem<Item> STEEL_HOE = ITEMS.register(
            "steel_hoe",
            () -> new HoeItem(STEEL, prop(HoeItem.createAttributes(STEEL, -1.0F, 0.0F)))
    );

    // ===== 钢盔甲 =====
    public static final DeferredItem<Item> STEEL_HELMET = ITEMS.register(
            "steel_helmet",
            () -> new ArmorItem(ModArmorMaterials.STEEL, ArmorItem.Type.HELMET,
                    prop().durability(ArmorItem.Type.HELMET.getDurability(37)))
    );
    public static final DeferredItem<Item> STEEL_CHESTPLATE = ITEMS.register(
            "steel_chestplate",
            () -> new ArmorItem(ModArmorMaterials.STEEL, ArmorItem.Type.CHESTPLATE,
                    prop().durability(ArmorItem.Type.CHESTPLATE.getDurability(37)))
    );
    public static final DeferredItem<Item> STEEL_LEGGINGS = ITEMS.register(
            "steel_leggings",
            () -> new ArmorItem(ModArmorMaterials.STEEL, ArmorItem.Type.LEGGINGS,
                    prop().durability(ArmorItem.Type.LEGGINGS.getDurability(37)))
    );
    public static final DeferredItem<Item> STEEL_BOOTS = ITEMS.register(
            "steel_boots",
            () -> new ArmorItem(ModArmorMaterials.STEEL, ArmorItem.Type.BOOTS,
                    prop().durability(ArmorItem.Type.BOOTS.getDurability(37)))
    );

    // ===== 下界合金钢盔甲 =====
    public static final DeferredItem<Item> STEEL_NETHER_HELMET = ITEMS.register(
            "steel_nether_helmet",
            () -> new ArmorItem(ModArmorMaterials.STEEL_NETHER, ArmorItem.Type.HELMET,
                    prop().durability(ArmorItem.Type.HELMET.getDurability(37)))
    );
    public static final DeferredItem<Item> STEEL_NETHER_CHESTPLATE = ITEMS.register(
            "steel_nether_chestplate",
            () -> new ArmorItem(ModArmorMaterials.STEEL_NETHER, ArmorItem.Type.CHESTPLATE,
                    prop().durability(ArmorItem.Type.CHESTPLATE.getDurability(37)))
    );
    public static final DeferredItem<Item> STEEL_NETHER_LEGGINGS = ITEMS.register(
            "steel_nether_leggings",
            () -> new ArmorItem(ModArmorMaterials.STEEL_NETHER, ArmorItem.Type.LEGGINGS,
                    prop().durability(ArmorItem.Type.LEGGINGS.getDurability(37)))
    );
    public static final DeferredItem<Item> STEEL_NETHER_BOOTS = ITEMS.register(
            "steel_nether_boots",
            () -> new ArmorItem(ModArmorMaterials.STEEL_NETHER, ArmorItem.Type.BOOTS,
                    prop().durability(ArmorItem.Type.BOOTS.getDurability(37)))
    );

    // ===== 下界合金钢工具 =====
    public static final DeferredItem<Item> STEEL_NETHER_SWORD = ITEMS.register(
            "steel_nether_sword",
            () -> new SwordItem(STEEL_NETHER, prop(SwordItem.createAttributes(STEEL_NETHER, 4, -2.6F)))
    );
    public static final DeferredItem<Item> STEEL_NETHER_PICKAXE = ITEMS.register(
            "steel_nether_pickaxe",
            () -> new PickaxeItem(STEEL_NETHER, prop(PickaxeItem.createAttributes(STEEL_NETHER, 1.0F, -3.0F)))
    );
    public static final DeferredItem<Item> STEEL_NETHER_SHOVEL = ITEMS.register(
            "steel_nether_shovel",
            () -> new ShovelItem(STEEL_NETHER, prop(ShovelItem.createAttributes(STEEL_NETHER, 1.5F, -3.0F)))
    );
    public static final DeferredItem<Item> STEEL_NETHER_AXE = ITEMS.register(
            "steel_nether_axe",
            () -> new AxeItem(STEEL_NETHER, prop(AxeItem.createAttributes(STEEL_NETHER, 7.0F, -3.0F)))
    );
    public static final DeferredItem<Item> STEEL_NETHER_HOE = ITEMS.register(
            "steel_nether_hoe",
            () -> new HoeItem(STEEL_NETHER, prop(HoeItem.createAttributes(STEEL_NETHER, -4.0F, 0.0F)))
    );

    // ===== 特殊武器 =====
    public static final DeferredItem<Item> STEEL_FRYING_PAN = ITEMS.register(
            "steel_frying_pan",
            () -> new SteelFryingPanItem(STEEL, prop(SwordItem.createAttributes(STEEL, 5, -2.8F)))
    );
    public static final DeferredItem<Item> STEEL_MORNING_STAR = ITEMS.register(
            "steel_morning_star",
            () -> new SteelMorningStarItem(STEEL, prop(HeavyItemAttributes.addSteelBlockToolModifier(
                    SwordItem.createAttributes(STEEL, 8, -3.4F), "")))
    );

    // ===== 其他 =====
    public static final DeferredItem<Item> STEEL_NETHER_INGOT = ITEMS.register(
            "steel_nether_ingot", () -> new Item(prop())
    );

    // 注册
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    // -- 辅助方法 --
    private static Item.Properties prop() {
        return new Item.Properties();
    }

    private static Item.Properties prop(ItemAttributeModifiers attrs) {
        return new Item.Properties().attributes(attrs);
    }
}
