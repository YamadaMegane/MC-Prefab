package com.wuest.prefab.structures.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.Tuple;
import com.wuest.prefab.config.ServerModConfiguration;
import com.wuest.prefab.events.ClientEventHandler;
import com.wuest.prefab.gui.GuiLangKeys;
import com.wuest.prefab.gui.GuiUtils;
import com.wuest.prefab.gui.controls.ExtendedButton;
import com.wuest.prefab.gui.controls.GuiCheckBox;
import com.wuest.prefab.structures.config.ModerateHouseConfiguration;
import com.wuest.prefab.structures.messages.StructureTagMessage.EnumStructureConfiguration;
import com.wuest.prefab.structures.predefined.StructureModerateHouse;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.item.DyeColor;

/**
 * @author WuestMan
 */
public class GuiModerateHouse extends GuiStructure {
    protected ModerateHouseConfiguration configuration;
    protected ServerModConfiguration serverConfiguration;
    private ExtendedButton btnHouseStyle;
    private GuiCheckBox btnAddChest;
    private GuiCheckBox btnAddChestContents;
    private GuiCheckBox btnAddMineShaft;
    private ExtendedButton btnBedColor;
    private boolean allowItemsInChestAndFurnace = true;

    public GuiModerateHouse() {
        super("Moderate House");

        this.structureConfiguration = EnumStructureConfiguration.ModerateHouse;
    }

    @Override
    protected void Initialize() {
        this.modifiedInitialXAxis = 215;
        this.modifiedInitialYAxis = 117;
        this.shownImageHeight = 150;
        this.shownImageWidth = 268;

        if (!Minecraft.getInstance().player.isCreative()) {
            this.allowItemsInChestAndFurnace = !ClientEventHandler.playerConfig.builtStarterHouse;
        }

        this.serverConfiguration = Prefab.proxy.getServerConfiguration();
        this.configuration = ClientEventHandler.playerConfig.getClientConfig("Moderate Houses", ModerateHouseConfiguration.class);
        this.configuration.pos = this.pos;

        // Get the upper left hand corner of the GUI box.
        Tuple<Integer, Integer> adjustedXYValue = this.getAdjustedXYValue();
        int grayBoxX = adjustedXYValue.getFirst();
        int grayBoxY = adjustedXYValue.getSecond();

        // Create the buttons.
        this.btnHouseStyle = this.createAndAddButton(grayBoxX + 8, grayBoxY + 45, 90, 20, this.configuration.houseStyle.getDisplayName(), false);
        this.btnBedColor = this.createAndAddDyeButton(grayBoxX + 8, grayBoxY + 90, 90, 20, this.configuration.bedColor);
        this.btnAddChest = this.createAndAddCheckBox(grayBoxX + 8, grayBoxY + 125, GuiLangKeys.STARTER_HOUSE_ADD_CHEST, this.configuration.addChests, this::buttonClicked);
        this.btnAddChestContents = this.createAndAddCheckBox(grayBoxX + 8, grayBoxY + 155, GuiLangKeys.STARTER_HOUSE_ADD_CHEST_CONTENTS, this.configuration.addChestContents, this::buttonClicked);
        this.btnAddMineShaft = this.createAndAddCheckBox(grayBoxX + 8, grayBoxY + 140, GuiLangKeys.STARTER_HOUSE_BUILD_MINESHAFT, this.configuration.addMineshaft, this::buttonClicked);

        // Create the standard buttons.
        this.btnVisualize = this.createAndAddCustomButton(grayBoxX + 24, grayBoxY + 175, 90, 20, GuiLangKeys.GUI_BUTTON_PREVIEW);
        this.btnBuild = this.createAndAddCustomButton(grayBoxX + 310, grayBoxY + 175, 90, 20, GuiLangKeys.GUI_BUTTON_BUILD);
        this.btnCancel = this.createAndAddButton(grayBoxX + 154, grayBoxY + 175, 90, 20, GuiLangKeys.GUI_BUTTON_CANCEL);
    }

    @Override
    protected void preButtonRender(MatrixStack matrixStack, int x, int y, int mouseX, int mouseY, float partialTicks) {
        int imagePanelUpperLeft = x + 136;
        int imagePanelWidth = 285;
        int imagePanelMiddle = imagePanelWidth / 2;

        this.renderBackground(matrixStack);

        this.drawControlLeftPanel(matrixStack, x + 2, y + 10, 135, 190);
        this.drawControlRightPanel(matrixStack, imagePanelUpperLeft, y + 10, imagePanelWidth, 190);

        int middleOfImage = this.shownImageWidth / 2;
        int imageLocation = imagePanelUpperLeft + (imagePanelMiddle - middleOfImage);

        GuiUtils.bindAndDrawScaledTexture(
                this.configuration.houseStyle.getHousePicture(),
                matrixStack,
                imageLocation,
                y + 15,
                this.shownImageWidth,
                this.shownImageHeight,
                this.shownImageWidth,
                this.shownImageHeight,
                this.shownImageWidth,
                this.shownImageHeight);

        this.btnAddChest.visible = this.serverConfiguration.addChests;
        this.btnAddChestContents.visible = this.allowItemsInChestAndFurnace && this.serverConfiguration.addChestContents;
        this.btnAddMineShaft.visible = this.serverConfiguration.addMineshaft;
    }

    @Override
    protected void postButtonRender(MatrixStack matrixStack, int x, int y, int mouseX, int mouseY, float partialTicks) {
        // Draw the text here.
        this.drawString(matrixStack, GuiLangKeys.translateString("item.prefab.item_moderate_house"), x + 8, y + 17, this.textColor);

        this.drawString(matrixStack, GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_STYLE), x + 8, y + 35, this.textColor);

        this.drawString(matrixStack, GuiLangKeys.translateString(GuiLangKeys.GUI_STRUCTURE_BED_COLOR), x + 8, y + 80, this.textColor);
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    @Override
    public void buttonClicked(AbstractButton button) {
        this.configuration.addChests = this.btnAddChest.visible && this.btnAddChest.isChecked();
        this.configuration.addChestContents = this.allowItemsInChestAndFurnace && (this.btnAddChestContents.visible && this.btnAddChestContents.isChecked());
        this.configuration.addMineshaft = this.btnAddMineShaft.visible && this.btnAddMineShaft.isChecked();

        this.performCancelOrBuildOrHouseFacing(this.configuration, button);

        if (button == this.btnHouseStyle) {
            int id = this.configuration.houseStyle.getValue() + 1;
            this.configuration.houseStyle = ModerateHouseConfiguration.HouseStyle.ValueOf(id);

            GuiUtils.setButtonText(btnHouseStyle, this.configuration.houseStyle.getDisplayName());
        } else if (button == this.btnVisualize) {
            StructureModerateHouse structure = StructureModerateHouse.CreateInstance(this.configuration.houseStyle.getStructureLocation(), StructureModerateHouse.class);
            this.performPreview(structure, this.configuration);
        } else if (button == this.btnBedColor) {
            this.configuration.bedColor = DyeColor.byId(this.configuration.bedColor.getId() + 1);
            GuiUtils.setButtonText(btnBedColor, GuiLangKeys.translateDye(this.configuration.bedColor));
        }
    }
}
