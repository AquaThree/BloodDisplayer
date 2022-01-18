package net.mod.blooddisplayer.event;

import java.awt.Color;
import java.util.Iterator;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.mod.blooddisplayer.BloodDisplayer;
import net.mod.blooddisplayer.config.Configs;

public class Event {

	@SubscribeEvent
	public void onRenderWorldLast(RenderWorldLastEvent event) {
		GlStateManager.disableLighting();
		GlStateManager.disableDepth();

		Iterator<Entity> entityIterator = Minecraft.getMinecraft().world.loadedEntityList.iterator();
		while (entityIterator.hasNext()) {
			Entity entity = entityIterator.next();

			if (!(entity instanceof EntityLivingBase)) {
				continue;
			}

			if (entity == Minecraft.getMinecraft().player) {
				continue;
			}
			
			if (entity.isInvisible()) {
				continue;
			}

			if (Configs.blackList.contains(EntityList.getEntityString(entity)))
				continue;

			if (Minecraft.getMinecraft().player.getDistance(entity) > Configs.maxDistance) {
				continue;
			}
			
			if(!Configs.showBoss) {
				if(!entity.isNonBoss()) {
					continue;
				}
			}
			
			if(Configs.showOnlySelected) {
				if(entity != Minecraft.getMinecraft().objectMouseOver.entityHit) {
					continue;
				}
			}

			if (!Minecraft.getMinecraft().player.canEntityBeSeen(entity)) {
				continue;
			}

			drawBar((EntityLivingBase) entity, event.getPartialTicks());
		}

		GlStateManager.enableLighting();
		GlStateManager.enableDepth();
	}

	private void drawBar(EntityLivingBase entity, float partialTicks) {
		GlStateManager.pushMatrix();

		RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
		FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;

		double viewX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks;
		double viewY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks;
		double viewZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks;

		float viewerPitch = renderManager.playerViewX;
		float viewerYaw = renderManager.playerViewY;

		String name = entity.getName();

		int width = 100;
		int height = 75;

		GlStateManager.translate((float) (viewX - renderManager.viewerPosX),
				(float) (viewY - renderManager.viewerPosY
						+ (entity.height + 0.5F - (entity.isSneaking() ? 0.25F : 0.0F))),
				(float) (viewZ - renderManager.viewerPosZ));
		GlStateManager.rotate(-viewerYaw, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(viewerPitch, 1.0F, 0.0F, 0.0F);

		float scale = 0.65F * Configs.size;

		GlStateManager.scale(scale, scale, scale);
		GlStateManager.scale(-0.025F, -0.025F, 0.025F);

		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
				GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
				GlStateManager.DestFactor.ZERO);

		GlStateManager.disableLighting();
		GlStateManager.depthMask(false);
		GlStateManager.disableDepth();
		GlStateManager.blendFunc(770, 771);
		GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);

		GlStateManager.enableBlend();

		if (Configs.customBackground && new ResourceLocation(Configs.backgroundPath) != null) {
			Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(Configs.backgroundPath));
			GlStateManager.color(1.0F, 1.0F, 1.0F, 0.5F);
			Gui.drawModalRectWithCustomSizedTexture(-width - 1, 15 - height, 0, 0, (width + 1) * 2, height + 1, (width + 1) * 2,
					height + 1);
		} else {
			GlStateManager.disableTexture2D();
			Tessellator tessellator = Tessellator.getInstance();

			BufferBuilder bufferbuilder = tessellator.getBuffer();

			bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
			bufferbuilder.pos(-width - 1, 15 - height, 0.0D).color(0.0F, 0.0F, 0.0F, 0.5F).endVertex();
			bufferbuilder.pos(-width - 1, 15, 0.0D).color(0.0F, 0.0F, 0.0F, 0.5F).endVertex();
			bufferbuilder.pos(width + 1, 15, 0.0D).color(0.0F, 0.0F, 0.0F, 0.5F).endVertex();
			bufferbuilder.pos(width + 1, 15 - height, 0.0D).color(0.0F, 0.0F, 0.0F, 0.5F).endVertex();
			tessellator.draw();

			GlStateManager.enableTexture2D();
		}

		fontRenderer.drawString(name, -fontRenderer.getStringWidth(name) / 2, 15 - height + 7,
				new Color(1.0F, 1.0F, 1.0F, 1.0F).getRGB());

		float health = entity.getHealth();
		float maxHealth = entity.getMaxHealth();

		Gui.drawRect(-width + 5, 15 - height + 24, width - 5, 15 - height + 36,
				new Color(0.0F, 0.0F, 0.0F, 1.0F).getRGB());
		Gui.drawRect(-width + 7, 15 - height + 26, (int) (-width + 7 + (width - 7 - (-width + 7)) * health / maxHealth),
				15 - height + 34, new Color(1.0F, 0.0F, 0.0F, 1.0F).getRGB());
		fontRenderer.drawString(health + "/" + maxHealth, -fontRenderer.getStringWidth(health + "/" + maxHealth) / 2,
				15 - height + 26, new Color(1.0F, 1.0F, 1.0F, 1.0F).getRGB());

		float armor = entity.getTotalArmorValue();

		Gui.drawRect(-width + 5, 15 - height + 49, width - 5, 15 - height + 61,
				new Color(0.0F, 0.0F, 0.0F, 1.0F).getRGB());
		Gui.drawRect(-width + 7, 15 - height + 51, (int) (-width + 7 + (width - 7 - (-width + 7)) * armor / 20),
				15 - height + 59, new Color(0.0F, 0.0F, 1.0F, 1.0F).getRGB());
		fontRenderer.drawString(armor + "/" + 20.0, -fontRenderer.getStringWidth(armor + "/" + 20.0) / 2, 15 - height + 51,
				new Color(1.0F, 1.0F, 1.0F, 1.0F).getRGB());

		GlStateManager.glNormal3f(0.0F, 0.0F, 0.0F);

		GlStateManager.disableBlend();
		GlStateManager.enableDepth();
		GlStateManager.depthMask(true);
		GlStateManager.popMatrix();
	}

}
