package com.narylr.narylrmod.recipe;

import com.narylr.narylrmod.NarylrMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

// 模组配方注册类
public class ModRecipes {
    // 配方类型延迟注册器
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, NarylrMod.MOD_ID);

    // 配方序列化器延迟注册器
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, NarylrMod.MOD_ID);

    // 钢熔炼配方类型
    public static final DeferredHolder<RecipeType<?>, RecipeType<SteelFurnaceRecipe>> STEEL_FURNACING_TYPE =
            RECIPE_TYPES.register("steel_furnacing", () -> new RecipeType<>() {
                @Override
                public String toString() {
                    return NarylrMod.MOD_ID + ":steel_furnacing";
                }
            });

    // 钢熔炼配方序列化器
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<SteelFurnaceRecipe>> STEEL_FURNACING_SERIALIZER =
            RECIPE_SERIALIZERS.register("steel_furnacing", SteelFurnaceRecipe.Serializer::new);

    // 注册配方类型和序列化器到模组总线
    public static void register(IEventBus eventBus) {
        RECIPE_TYPES.register(eventBus);
        RECIPE_SERIALIZERS.register(eventBus);
    }
}
