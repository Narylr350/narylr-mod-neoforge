package com.narylr.narylrmod.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.narylr.narylrmod.tag.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

// 钢熔炉配方 record，包含输入、碳源数量、产出、经验和烹饪时间
public record SteelFurnaceRecipe(
        Ingredient ingredient,  // 输入材料
        int coalCount,          // 需要的碳源数量
        ItemStack result,       // 产出物品
        float experience,       // 获得的经验
        int cookingTime         // 烹饪时间（tick）
) implements Recipe<SteelFurnaceRecipeInput> {

    // 检查配方是否匹配：输入材料和碳源标签都满足
    @Override
    public boolean matches(SteelFurnaceRecipeInput input, Level level) {
        ItemStack inputStack = input.getItem(0);
        ItemStack coalStack = input.getItem(1);

        return ingredient.test(inputStack)
                && coalStack.is(ModTags.STEEL_CARBON_SOURCES)
                && coalStack.getCount() >= coalCount;
    }

    @Override
    public ItemStack assemble(SteelFurnaceRecipeInput input, HolderLookup.Provider registries) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return result.copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.STEEL_FURNACING_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.STEEL_FURNACING_TYPE.get();
    }

    // 序列化器：处理 JSON 读写和网络同步
    public static class Serializer implements RecipeSerializer<SteelFurnaceRecipe> {
        // JSON 序列化编解码器
        public static final MapCodec<SteelFurnaceRecipe> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                                Ingredient.CODEC.fieldOf("ingredient").forGetter(SteelFurnaceRecipe::ingredient),
                                Codec.INT.fieldOf("coal_count").forGetter(SteelFurnaceRecipe::coalCount),
                                ItemStack.CODEC.fieldOf("result").forGetter(SteelFurnaceRecipe::result),
                                Codec.FLOAT.optionalFieldOf("experience", 0.0F).forGetter(SteelFurnaceRecipe::experience),
                                Codec.INT.fieldOf("cookingtime").forGetter(SteelFurnaceRecipe::cookingTime)
                ).apply(instance, SteelFurnaceRecipe::new)
        );

        // 网络同步编解码器
        public static final StreamCodec<RegistryFriendlyByteBuf, SteelFurnaceRecipe> STREAM_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC,
                SteelFurnaceRecipe::ingredient,
                ByteBufCodecs.VAR_INT,
                SteelFurnaceRecipe::coalCount,
                ItemStack.STREAM_CODEC,
                SteelFurnaceRecipe::result,
                ByteBufCodecs.FLOAT,
                SteelFurnaceRecipe::experience,
                ByteBufCodecs.VAR_INT,
                SteelFurnaceRecipe::cookingTime,
                SteelFurnaceRecipe::new
        );

        @Override
        public MapCodec<SteelFurnaceRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, SteelFurnaceRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
