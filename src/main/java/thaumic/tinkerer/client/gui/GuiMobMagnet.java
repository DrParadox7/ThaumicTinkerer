/**
 * This class was created by <Vazkii>. It's distributed as part of the ThaumicTinkerer Mod.
 *
 * ThaumicTinkerer is Open Source and distributed under a Creative Commons Attribution-NonCommercial-ShareAlike 3.0
 * License (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 *
 * ThaumicTinkerer is a Derivative Work on Thaumcraft 4. Thaumcraft 4 (c) Azanor 2012
 * (http://www.minecraftforum.net/topic/1585216-)
 *
 * File Created @ [12 Sep 2013, 18:37:02 (GMT)]
 */
package thaumic.tinkerer.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import thaumic.tinkerer.client.gui.button.GuiButtonMM;
import thaumic.tinkerer.client.gui.button.GuiButtonMMRadio;
import thaumic.tinkerer.client.gui.button.IRadioButton;
import thaumic.tinkerer.client.lib.LibResources;
import thaumic.tinkerer.common.ThaumicTinkerer;
import thaumic.tinkerer.common.block.tile.TileMobMagnet;
import thaumic.tinkerer.common.block.tile.container.ContainerMobMagnet;
import thaumic.tinkerer.common.item.ItemSoulMould;
import thaumic.tinkerer.common.network.packet.PacketMobMagnetButton;

public class GuiMobMagnet extends GuiContainer {

    private static final ResourceLocation gui = new ResourceLocation(LibResources.GUI_MOB_MAGNET);

    int x, y;

    ItemStack stack = null;
    TileMobMagnet mobMagnet;

    List<GuiButtonMM> buttonListMM = new ArrayList();
    List<IRadioButton> radioButtons = new ArrayList();

    public GuiMobMagnet(TileMobMagnet tile, InventoryPlayer playerInv) {
        super(new ContainerMobMagnet(tile, playerInv));
        mobMagnet = tile;
    }

    @Override
    public void initGui() {
        super.initGui();
        x = (width - xSize) / 2;
        y = (height - ySize) / 2;
        buttonListMM.clear();
        addButton(new GuiButtonMMRadio(0, x + 100, y + 28, mobMagnet.adult, radioButtons));
        addButton(new GuiButtonMMRadio(1, x + 100, y + 48, !mobMagnet.adult, radioButtons));
        buttonList = buttonListMM;
    }

    private void addButton(GuiButtonMM button) {
        buttonListMM.add(button);
        if (button instanceof IRadioButton) radioButtons.add((IRadioButton) button);
    }

    @Override
    protected void actionPerformed(GuiButton par1GuiButton) {
        if (par1GuiButton instanceof IRadioButton) ((IRadioButton) par1GuiButton).enableFromClick();
        else buttonListMM.get(0).enabled = !buttonListMM.get(0).enabled;

        mobMagnet.adult = buttonListMM.get(0).enabled;

        ThaumicTinkerer.netHandler.sendToServer(new PacketMobMagnetButton(mobMagnet));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(gui);
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
        String adult = StatCollector.translateToLocal("ttmisc.adult");
        String child = StatCollector.translateToLocal("ttmisc.child");
        stack = mobMagnet.getStackInSlot(0);

        String filter;

        if (stack != null) {
            String name = ItemSoulMould.getPatternName(stack);
            if (name.isEmpty()) filter = StatCollector.translateToLocal("ttmisc.none");
            else filter = StatCollector.translateToLocal("entity." + name + ".name");
        } else filter = StatCollector.translateToLocal("ttmisc.all");

        fontRendererObj
                .drawString(filter, x + xSize / 2 - fontRendererObj.getStringWidth(filter) / 2 - 26, y + 16, 0x999999);
        fontRendererObj.drawString(adult, x + 120, y + 30, 0x999999);
        fontRendererObj.drawString(child, x + 120, y + 50, 0x999999);
        GL11.glColor3f(1F, 1F, 1F);
    }
}
