package net.mod.blooddisplayer.config;

import java.util.ArrayList;
import java.util.List;

import net.mod.blooddisplayer.BloodDisplayer;

public class Configs {

	public static int maxDistance = 36;

	public static List<String> blackList = new ArrayList<String>();

	public static boolean customBackground = false;

	public static String backgroundPath = BloodDisplayer.MODID + ":" + "textures/background.png";
	
	public static boolean showBoss = true;
	
	public static boolean showOnlySelected = false;
	
	public static float size = 1.0F;

}
