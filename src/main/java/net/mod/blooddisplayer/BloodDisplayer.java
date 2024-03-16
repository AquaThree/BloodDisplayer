package net.mod.blooddisplayer;

import java.util.Arrays;

import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.mod.blooddisplayer.config.Configs;
import net.mod.blooddisplayer.proxy.CommonProxy;

@Mod(modid = BloodDisplayer.MODID, name = BloodDisplayer.NAME, version = BloodDisplayer.VERSION, guiFactory = "net.mod.blooddisplayer.gui.GuiFactory", acceptedMinecraftVersions = "[1.12.2]")
public class BloodDisplayer {

	public static final String MODID = "blooddisplayer";
	public static final String NAME = "BloodDisplayer";
	public static final String VERSION = "1.1.5";

	public static Logger logger;

	@SidedProxy(clientSide = "net.mod.blooddisplayer.proxy.ClientProxy", serverSide = "net.mod.blooddisplayer.proxy.CommonProxy")
	public static CommonProxy proxy;

	public static Configuration config;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		BloodDisplayer.load();
		proxy.preInit(event);

	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}

	@EventHandler
	public void onServerStarting(FMLServerStartingEvent event) {
		proxy.onServerStarting(event);
	}

	public static void load() {
		Configs.maxDistance = config.get(NAME, "maxDistance", Configs.maxDistance).getInt();
		Configs.blackList = Arrays.asList(config.get(NAME, "blackList", new String[] { "Shulker" }).getStringList());
		Configs.customBackground = config.get(NAME, "customBackground", Configs.customBackground).getBoolean();
		Configs.backgroundPath = config.get(NAME, "backgroundPath", Configs.backgroundPath).getString();
		Configs.showBoss = config.get(NAME, "showBoss", Configs.showBoss).getBoolean();
		Configs.showOnlySelected = config.get(NAME, "showOnlySelected", Configs.showOnlySelected).getBoolean();
		Configs.size = (float) config.get(NAME, "size", Configs.size).getDouble();
		Configs.opacity = (float) config.get(NAME, "opacity", Configs.opacity).getDouble();
		if (Loader.isModLoaded("customnpcs")) {
			Configs.showNPC = config.get(NAME, "showNPC", Configs.showNPC).getBoolean();
		}
		save();
	}

	public static void save() {
		if (config.hasChanged()) {
			config.save();
		}
	}
}

