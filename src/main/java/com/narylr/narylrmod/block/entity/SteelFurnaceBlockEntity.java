package com.narylr.narylrmod.block.entity;

import com.narylr.narylrmod.block.SteelFurnaceBlock;
import com.narylr.narylrmod.recipe.ModRecipes;
import com.narylr.narylrmod.recipe.SteelFurnaceRecipe;
import com.narylr.narylrmod.recipe.SteelFurnaceRecipeInput;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Optional;

public class SteelFurnaceBlockEntity extends BlockEntity implements Container {
    private int progress = 0;
    private int maxProgress = 0;
    private int burnTime = 0;
    private int maxBurnTime = 0;
    private static final int MODE_IDLE = 0;
    private static final int MODE_STEEL = 1;
    private static final int MODE_SMELTING = 2;
    private int mode = MODE_IDLE;
    private float experienceStored = 0.0F;
    private SteelFurnaceRecipe currentSteelRecipe;
    private RecipeHolder<SmeltingRecipe> currentSmeltingRecipe;
    private final NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);
    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> progress;
                case 1 -> maxProgress;
                case 2 -> burnTime;
                case 3 -> maxBurnTime;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> progress = value;
                case 1 -> maxProgress = value;
                case 2 -> burnTime = value;
                case 3 -> maxBurnTime = value;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    };

    public SteelFurnaceBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.STEEL_FURNACE_BLOCK_ENTITY.get(), pos, blockState);
    }

    public ContainerData getData() {
        return data;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, SteelFurnaceBlockEntity blockEntity) {
        if (level.isClientSide) {
            return;
        }

        boolean working = blockEntity.tickWork();

        if (state.getValue(SteelFurnaceBlock.LIT) != working) {
            level.setBlock(pos, state.setValue(SteelFurnaceBlock.LIT, working), 3);
        }

        setChanged(level, pos, state);
    }

    private boolean tickWork() {
        if (mode == MODE_SMELTING && canStartSteelRecipe()) {
            stopWorking();
            return tryStartSteelRecipe();
        }

        if (mode == MODE_IDLE) {
            return tryStartSteelRecipe() || tryStartSmeltingRecipe();
        }

        if (mode == MODE_STEEL) {
            return processSteelRecipe();
        }

        if (mode == MODE_SMELTING) {
            return processSmeltingRecipe();
        }

        stopWorking();
        return false;
    }

    private boolean tryStartSteelRecipe() {
        SteelFurnaceRecipe recipe = findSteelRecipeWithCoal();

        if (recipe == null || !canOutput(recipe.result())) {
            return false;
        }

        ItemStack coal = items.get(1);
        coal.shrink(recipe.coalCount());

        currentSteelRecipe = recipe;
        currentSmeltingRecipe = null;

        mode = MODE_STEEL;
        progress = 0;
        maxProgress = recipe.cookingTime();
        burnTime = recipe.cookingTime();
        maxBurnTime = recipe.cookingTime();

        playSteelStartSound();

        setChanged();
        return true;
    }

    private void playSteelStartSound() {
        if (level == null || level.isClientSide) {
            return;
        }

        level.playSound(null, worldPosition, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 0.8F, 0.65F);
    }

    private SteelFurnaceRecipe findSteelRecipeWithCoal() {
        if (level == null) {
            return null;
        }

        SteelFurnaceRecipeInput recipeInput = new SteelFurnaceRecipeInput(items.get(0), items.get(1));

        Optional<RecipeHolder<SteelFurnaceRecipe>> recipeHolder =
                level.getRecipeManager().getRecipeFor(ModRecipes.STEEL_FURNACING_TYPE.get(), recipeInput, level);

        return recipeHolder.map(RecipeHolder::value).orElse(null);
    }

    private boolean canStartSteelRecipe() {
        SteelFurnaceRecipe recipe = findSteelRecipeWithCoal();

        return recipe != null && canOutput(recipe.result());
    }

    private SteelFurnaceRecipe findSteelRecipeByInputOnly() {
        if (level == null) {
            return null;
        }

        ItemStack input = items.get(0);

        return level.getRecipeManager()
                .getAllRecipesFor(ModRecipes.STEEL_FURNACING_TYPE.get())
                .stream()
                .map(RecipeHolder::value)
                .filter(recipe -> recipe.ingredient().test(input))
                .findFirst()
                .orElse(null);
    }

    private boolean processSteelRecipe() {
        if (burnTime <= 0) {
            stopWorking();
            return false;
        }

        SteelFurnaceRecipe recipe = currentSteelRecipe;

        if (recipe == null) {
            recipe = findSteelRecipeByInputOnly();
            currentSteelRecipe = recipe;
        }

        if (recipe == null || !canOutput(recipe.result())) {
            stopWorking();
            return false;
        }

        maxProgress = recipe.cookingTime();

        burnTime--;
        progress++;

        if (progress >= maxProgress) {
            finishSteelRecipe();
            stopWorking();
            return tryStartSteelRecipe();
        }

        setChanged();
        return true;
    }

    private void finishSteelRecipe() {
        if (currentSteelRecipe == null) {
            return;
        }

        ItemStack input = items.get(0);
        ItemStack output = items.get(2);
        ItemStack result = currentSteelRecipe.result().copy();

        input.shrink(1);
        experienceStored += currentSteelRecipe.experience();

        if (output.isEmpty()) {
            items.set(2, result);
        } else {
            output.grow(result.getCount());
        }

        setChanged();
    }

    private RecipeHolder<SmeltingRecipe> findSmeltingRecipe() {
        if (level == null) {
            return null;
        }

        SingleRecipeInput recipeInput = new SingleRecipeInput(items.get(0));

        Optional<RecipeHolder<SmeltingRecipe>> recipeHolder =
                level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, recipeInput, level);

        return recipeHolder.orElse(null);
    }

    private boolean tryStartSmeltingRecipe() {
        RecipeHolder<SmeltingRecipe> recipe = findSmeltingRecipe();

        if (recipe == null) {
            return false;
        }

        ItemStack result = recipe.value().getResultItem(level.registryAccess());

        if (!canOutput(result)) {
            return false;
        }

        if (!consumeFuel()) {
            return false;
        }

        currentSteelRecipe = null;
        currentSmeltingRecipe = recipe;

        mode = MODE_SMELTING;
        progress = 0;
        maxProgress = Math.max(recipe.value().getCookingTime() / 2, 1);

        setChanged();
        return true;
    }

    private boolean processSmeltingRecipe() {
        if (burnTime <= 0) {
            stopWorking();
            return false;
        }

        burnTime--;

        RecipeHolder<SmeltingRecipe> recipe = findSmeltingRecipe();

        if (recipe == null) {
            progress = 0;
            maxProgress = 0;

            if (burnTime <= 0) {
                stopWorking();
                return false;
            }

            setChanged();
            return true;
        }

        ItemStack result = recipe.value().getResultItem(level.registryAccess());

        if (!canOutput(result)) {
            progress = 0;
            maxProgress = Math.max(recipe.value().getCookingTime() / 2, 1);

            if (burnTime <= 0) {
                stopWorking();
                return false;
            }

            setChanged();
            return true;
        }

        currentSmeltingRecipe = recipe;
        maxProgress = Math.max(recipe.value().getCookingTime() / 2, 1);

        progress++;

        if (progress >= maxProgress) {
            finishSmeltingRecipe();
            progress = 0;
        }

        if (burnTime <= 0) {
            stopWorking();
            return tryStartSteelRecipe() || tryStartSmeltingRecipe();
        }

        setChanged();
        return true;
    }

    private boolean consumeFuel() {
        ItemStack fuel = items.get(1);

        if (fuel.isEmpty()) {
            return false;
        }

        int fuelTime = AbstractFurnaceBlockEntity.getFuel().getOrDefault(fuel.getItem(), 0);

        if (fuelTime <= 0) {
            return false;
        }

        burnTime = fuelTime;
        maxBurnTime = fuelTime;

        fuel.shrink(1);
        return true;
    }

    private void finishSmeltingRecipe() {
        if (level == null || currentSmeltingRecipe == null) {
            return;
        }

        ItemStack input = items.get(0);
        ItemStack output = items.get(2);
        ItemStack result = currentSmeltingRecipe.value().getResultItem(level.registryAccess()).copy();

        input.shrink(1);
        experienceStored += currentSmeltingRecipe.value().getExperience();

        if (output.isEmpty()) {
            items.set(2, result);
        } else {
            output.grow(result.getCount());
        }

        setChanged();
    }

    private void stopWorking() {
        mode = MODE_IDLE;

        progress = 0;
        maxProgress = 0;
        burnTime = 0;
        maxBurnTime = 0;

        currentSteelRecipe = null;
        currentSmeltingRecipe = null;

        setChanged();
    }

    private boolean canOutput(ItemStack result) {
        ItemStack output = items.get(2);

        if (result.isEmpty()) {
            return false;
        }

        if (output.isEmpty()) {
            return true;
        }

        return ItemStack.isSameItemSameComponents(output, result)
                && output.getCount() + result.getCount() <= output.getMaxStackSize();
    }

    private int getExperienceAmount() {
        int experience = Mth.floor(experienceStored);
        float fraction = experienceStored - experience;

        if (fraction > 0.0F && level != null && level.random.nextFloat() < fraction) {
            experience++;
        }

        return experience;
    }

    public void awardStoredExperience(Player player) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        int experience = getExperienceAmount();

        if (experience <= 0) {
            return;
        }

        ExperienceOrb.award(serverLevel, player.position(), experience);

        experienceStored = 0.0F;
        setChanged();
    }

    public void dropStoredExperience() {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        int experience = getExperienceAmount();

        if (experience <= 0) {
            return;
        }

        ExperienceOrb.award(serverLevel, worldPosition.getCenter(), experience);

        experienceStored = 0.0F;
        setChanged();
    }

    @Override
    protected void saveAdditional(@Nonnull CompoundTag tag, @Nonnull HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        ContainerHelper.saveAllItems(tag, items, registries);
        tag.putInt("Progress", progress);
        tag.putInt("MaxProgress", maxProgress);
        tag.putInt("BurnTime", burnTime);
        tag.putInt("MaxBurnTime", maxBurnTime);
        tag.putFloat("ExperienceStored", experienceStored);
    }

    @Override
    protected void loadAdditional(@Nonnull CompoundTag tag, @Nonnull HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        ContainerHelper.loadAllItems(tag, items, registries);
        progress = tag.getInt("Progress");
        maxProgress = tag.getInt("MaxProgress");
        burnTime = tag.getInt("BurnTime");
        maxBurnTime = tag.getInt("MaxBurnTime");
        mode = tag.getInt("Mode");
        experienceStored = tag.getFloat("ExperienceStored");
    }

    @Override
    public int getContainerSize() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack item : items) {
            if (!item.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    @Nonnull
    public ItemStack getItem(int slot) {
        return items.get(slot);
    }

    @Override
    @Nonnull
    public ItemStack removeItem(int slot, int amount) {
        ItemStack itemStack = ContainerHelper.removeItem(items, slot, amount);
        if (!itemStack.isEmpty()) {
            setChanged();
        }
        return itemStack;
    }

    @Override
    @Nonnull
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(items, slot);
    }

    @Override
    public void setItem(int slot, @Nonnull ItemStack itemStack) {
        items.set(slot, itemStack);

        if (itemStack.getCount() > getMaxStackSize()) {
            itemStack.setCount(getMaxStackSize());
        }

        setChanged();
    }

    @Override
    public boolean stillValid(@Nonnull Player player) {
        if (level == null) {
            return false;
        }

        if (level.getBlockEntity(worldPosition) != this) {
            return false;
        }
        return player.distanceToSqr(
                worldPosition.getX() + 0.5,
                worldPosition.getY() + 0.5,
                worldPosition.getZ() + 0.5
        ) <= 64;
    }

    @Override
    public void clearContent() {
        items.clear();
        setChanged();
    }

    public int getMode() {
        return mode;
    }

    public boolean isSteelMode() {
        return mode == MODE_STEEL;
    }

    public boolean isSmeltingMode() {
        return mode == MODE_SMELTING;
    }

    public int getProgress() {
        return progress;
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public int getBurnTime() {
        return burnTime;
    }

    public int getMaxBurnTime() {
        return maxBurnTime;
    }
}
