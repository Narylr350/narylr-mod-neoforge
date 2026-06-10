package com.narylr.narylrmod.screen;

import com.narylr.narylrmod.block.entity.SteelFurnaceBlockEntity;
import com.narylr.narylrmod.recipe.ModRecipes;
import com.narylr.narylrmod.tag.ModTags;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;

import javax.annotation.Nonnull;

// 钢熔炉菜单类，处理 GUI 交互逻辑
public class SteelFurnaceMenu extends AbstractContainerMenu {
    private final Container container;      // 物品容器
    private final ContainerData data;       // 同步数据（进度、燃烧时间等）
    private final Level level;              // 游戏世界
    private final SteelFurnaceBlockEntity blockEntity; // 方块实体

    // 客户端构造函数（无方块实体）
    public SteelFurnaceMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(3), new SimpleContainerData(4), null);
    }

    // 服务端构造函数（有方块实体）
    public SteelFurnaceMenu(int containerId, Inventory playerInventory, SteelFurnaceBlockEntity blockEntity) {
        this(containerId, playerInventory, blockEntity, blockEntity.getData(), blockEntity);
    }

    // 完整构造函数
    public SteelFurnaceMenu(
            int containerId,
            Inventory playerInventory,
            Container container,
            ContainerData data,
            SteelFurnaceBlockEntity blockEntity
    ) {
        super(ModMenus.STEEL_FURNACE_MENU.get(), containerId);
        this.container = container;
        this.data = data;
        this.blockEntity = blockEntity;
        level = playerInventory.player.level();

        // 同步数据到客户端
        addDataSlots(data);

        // 输入槽（位置：56, 17）
        addSlot(new Slot(container, 0, 56, 17) {
            @Override
            public boolean mayPlace(@Nonnull ItemStack itemStack) {
                return canPlaceInput(itemStack);
            }
        });

        // 燃料/碳源槽（位置：56, 53）
        addSlot(new Slot(container, 1, 56, 53) {
            @Override
            public boolean mayPlace(@Nonnull ItemStack itemStack) {
                return canPlaceFuel(itemStack);
            }
        });

        // 输出槽（位置：116, 35）
        addSlot(new Slot(container, 2, 116, 35) {
            @Override
            public boolean mayPlace(@Nonnull ItemStack itemStack) {
                return false;
            }

            @Override
            public void onTake(@Nonnull Player player, @Nonnull ItemStack itemStack) {
                super.onTake(player, itemStack);

                // 取出物品时发放经验
                if (blockEntity != null) {
                    blockEntity.awardStoredExperience(player);
                }
            }
        });

        // 添加玩家背包和快捷栏
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    // 判断物品是否可以放入输入槽
    private boolean canPlaceInput(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return false;
        }

        return hasSteelFurnaceRecipe(itemStack) || hasSmeltingRecipe(itemStack);
    }

    // 检查是否有钢熔炉配方
    private boolean hasSteelFurnaceRecipe(ItemStack stack) {
        return level.getRecipeManager()
                .getAllRecipesFor(ModRecipes.STEEL_FURNACING_TYPE.get())
                .stream()
                .anyMatch(recipeHolder -> recipeHolder.value().ingredient().test(stack));
    }

    // 检查是否有原版熔炼配方
    private boolean hasSmeltingRecipe(ItemStack stack) {
        return level.getRecipeManager()
                .getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(stack), level)
                .isPresent();
    }

    // 添加玩家背包 27 格
    private void addPlayerInventory(Inventory playerInventory) {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(
                        playerInventory,
                        column + row * 9 + 9,
                        8 + column * 18,
                        84 + row * 18
                ));
            }
        }
    }

    // 添加快捷栏 9 格
    private void addPlayerHotbar(Inventory playerInventory) {
        for (int column = 0; column < 9; column++) {
            addSlot(new Slot(
                    playerInventory,
                    column,
                    8 + column * 18,
                    142
            ));
        }
    }

    // Shift 点击物品处理
    @Override
    @Nonnull
    public ItemStack quickMoveStack(@Nonnull Player player, int index) {
        ItemStack originalStack = ItemStack.EMPTY;
        Slot slot = slots.get(index);

        if (slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem();
            originalStack = stackInSlot.copy();

            // 机器槽到玩家背包
            if (index < 3) {
                if (!moveItemStackTo(stackInSlot, 3, 39, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // 输入物品到输入槽
                if (canPlaceInput(stackInSlot)) {
                    if (!moveItemStackTo(stackInSlot, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                // 燃料到燃料槽
                } else if (canPlaceFuel(stackInSlot)) {
                    if (!moveItemStackTo(stackInSlot, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                // 玩家背包到快捷栏
                } else if (index < 30) {
                    if (!moveItemStackTo(stackInSlot, 30, 39, false)) {
                        return ItemStack.EMPTY;
                    }
                // 快捷栏到玩家背包
                } else if (index < 39) {
                    if (!moveItemStackTo(stackInSlot, 3, 30, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (stackInSlot.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stackInSlot.getCount() == originalStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stackInSlot);
        }
        return originalStack;
    }

    @Override
    public boolean stillValid(@Nonnull Player player) {
        return container.stillValid(player);
    }

    // 获取当前进度
    public int getProgress() {
        return data.get(0);
    }

    // 获取最大进度
    public int getMaxProgress() {
        return data.get(1);
    }

    // 获取缩放后的进度条宽度
    public int getScaledProgress() {
        int progress = getProgress();
        int maxProgress = getMaxProgress();
        int arrowWidth = 24;

        if (maxProgress == 0 || progress == 0) {
            return 0;
        }

        return progress * arrowWidth / maxProgress;
    }

    // 获取剩余燃烧时间
    public int getBurnTime() {
        return data.get(2);
    }

    // 获取最大燃烧时间
    public int getMaxBurnTime() {
        return data.get(3);
    }

    // 获取缩放后的火焰高度
    public int getScaledBurnProgress() {
        int burnTime = getBurnTime();
        int maxBurnTime = getMaxBurnTime();
        int flameHeight = 13;

        if (maxBurnTime == 0 || burnTime == 0) {
            return 0;
        }

        return burnTime * flameHeight / maxBurnTime;
    }

    // 判断物品是否可以作为燃料/碳源
    private boolean canPlaceFuel(ItemStack itemStack) {
        return itemStack.is(ModTags.STEEL_CARBON_SOURCES)
                || AbstractFurnaceBlockEntity.isFuel(itemStack);
    }
}
