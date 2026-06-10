package com.narylr.narylrmod.client.screen;

import com.narylr.narylrmod.NarylrMod;
import com.narylr.narylrmod.screen.SteelFurnaceMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

// 钢熔炉 GUI 屏幕，负责渲染界面背景、进度条和火焰动画
public class SteelFurnaceScreen extends AbstractContainerScreen<SteelFurnaceMenu> {
    // GUI 背景纹理
    public static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(NarylrMod.MOD_ID, "textures/gui/container/furnace.png");

    // 燃烧进度条精灵图
    public static final ResourceLocation BURN_PROGRESS_SPRITE =
            ResourceLocation.fromNamespaceAndPath(NarylrMod.MOD_ID, "container/steel_furnace/burn_progress");

    // 点燃状态精灵图
    private static final ResourceLocation LIT_PROGRESS_SPRITE =
            ResourceLocation.fromNamespaceAndPath(NarylrMod.MOD_ID, "container/steel_furnace/lit_progress");

    public SteelFurnaceScreen(SteelFurnaceMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        imageWidth = 176;
        imageHeight = 166;
    }

    // 初始化屏幕，设置标题和背包标签位置
    @Override
    protected void init() {
        super.init();

        titleLabelX = (imageWidth - font.width(title)) / 2;
        titleLabelY = 8;

        inventoryLabelX = 8;
        inventoryLabelY = 72;
    }

    // 渲染背景：绘制 GUI 背景、火焰动画和进度条
    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = leftPos;
        int y = topPos;

        // 绘制背景纹理
        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        // 绘制火焰动画（燃烧进度）
        int burnProgress = menu.getScaledBurnProgress();
        if (burnProgress > 0) {
            guiGraphics.blitSprite(
                    LIT_PROGRESS_SPRITE,
                    14, 14,
                    0, 14 - burnProgress,
                    x + 56, y + 36 + 14 - burnProgress,
                    14, burnProgress
            );
        }

        // 绘制进度条箭头
        int progress = menu.getScaledProgress();
        if (progress > 0) {
            guiGraphics.blitSprite(
                    BURN_PROGRESS_SPRITE,
                    24, 16,
                    0, 0,
                    x + 79, y + 34,
                    progress, 16
            );
        }
    }

    // 渲染屏幕：背景、内容和工具提示
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
