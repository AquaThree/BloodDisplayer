package net.mod.blooddisplayer.gui;

import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.mod.blooddisplayer.BloodDisplayer;

public class GuiFactory implements IModGuiFactory {

	@Override
	public Set<IModGuiFactory.RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return null;
	}

	@Override
	public boolean hasConfigGui() {
		return true;
	}

	@Override
	public GuiScreen createConfigGui(GuiScreen guiScreen) {
		return new GuiConfig(guiScreen,
				new ConfigElement(BloodDisplayer.config.getCategory(BloodDisplayer.NAME)).getChildElements(),
				BloodDisplayer.MODID, false, false, BloodDisplayer.NAME + "Config");
	}

	@Override
	public void initialize(Minecraft minecraft) {
	}
}
