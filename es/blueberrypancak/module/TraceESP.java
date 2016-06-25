package es.blueberrypancak.module;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import es.blueberrypancak.Client;
import es.blueberrypancak.event.EventEntityRender;
import es.blueberrypancak.event.Subscribe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.Vec3d;

@RegisterModule
public class TraceESP extends Module {

	@Subscribe
	public void onEntityRender(EventEntityRender e) {
		Minecraft mc = Client.getMinecraft();
		for (Object o : mc.theWorld.loadedEntityList) {
			if (o instanceof EntityItem) {
				trace(mc, (EntityItem) o, e.getValue(), "#FFD105");
			} else if (o instanceof EntityEnderPearl) {
				trace(mc, (EntityEnderPearl) o, e.getValue(), "#00FF90");
			} else if (o instanceof EntityOtherPlayerMP) {
				box(mc, (EntityOtherPlayerMP) o, e.getValue(), 1.0);
				trace(mc, (EntityOtherPlayerMP)o, e.getValue(), 1.0);
			}
		}
	}

	private void box(Minecraft mc, EntityOtherPlayerMP e, float par1, double opacity) {
		/*
		 * if(CubAimbot.get().isFriend(par1Entity.getName())) { opacity = 0.4; }
		 */
		RenderManager r = mc.getRenderManager();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glLineWidth(1.0F);
		float hp = e.getHealth();
		Color c = Color.decode("#" + (hp / 2 > 7 ? "55FF55" : hp / 2 >= 4 ? "FFFF55" : "FF5555"));
		GL11.glPushMatrix();
		GL11.glDepthMask(false);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4d((double) c.getRed() / 255.0, (double) c.getGreen() / 255.0, (double) c.getBlue() / 255.0, opacity);
		double var3 = e.lastTickPosX + (e.posX - e.lastTickPosX) * (double) par1;
		double var5 = e.lastTickPosY + (e.posY - e.lastTickPosY) * (double) par1;
		double var7 = e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * (double) par1;
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBegin(GL11.GL_LINES);

		double w = e.width - 0.2;
		double h = e.height + 0.3;

		GL11.glVertex3d(var3 - r.renderPosX + w, var5 - r.renderPosY, var7 - r.renderPosZ + w);
		GL11.glVertex3d(var3 - r.renderPosX - w, var5 - r.renderPosY, var7 - r.renderPosZ + w);
		GL11.glVertex3d(var3 - r.renderPosX + w, var5 - r.renderPosY, var7 - r.renderPosZ - w);
		GL11.glVertex3d(var3 - r.renderPosX + w, var5 - r.renderPosY, var7 - r.renderPosZ + w);
		GL11.glVertex3d(var3 - r.renderPosX - w, var5 - r.renderPosY, var7 - r.renderPosZ - w);
		GL11.glVertex3d(var3 - r.renderPosX - w, var5 - r.renderPosY, var7 - r.renderPosZ + w);
		GL11.glVertex3d(var3 - r.renderPosX + w, var5 - r.renderPosY, var7 - r.renderPosZ - w);
		GL11.glVertex3d(var3 - r.renderPosX - w, var5 - r.renderPosY, var7 - r.renderPosZ - w);
		GL11.glVertex3d(var3 - r.renderPosX + w, var5 - r.renderPosY, var7 - r.renderPosZ + w);
		GL11.glVertex3d(var3 - r.renderPosX - w, var5 - r.renderPosY, var7 - r.renderPosZ + w);
		GL11.glVertex3d(var3 - r.renderPosX + w, var5 - r.renderPosY + h, var7 - r.renderPosZ - w);
		GL11.glVertex3d(var3 - r.renderPosX + w, var5 - r.renderPosY + h, var7 - r.renderPosZ + w);

		GL11.glVertex3d(var3 - r.renderPosX - w, var5 - r.renderPosY + h, var7 - r.renderPosZ - w);
		GL11.glVertex3d(var3 - r.renderPosX - w, var5 - r.renderPosY + h, var7 - r.renderPosZ + w);

		GL11.glVertex3d(var3 - r.renderPosX + w, var5 - r.renderPosY + h, var7 - r.renderPosZ - w);
		GL11.glVertex3d(var3 - r.renderPosX - w, var5 - r.renderPosY + h, var7 - r.renderPosZ - w);
		GL11.glVertex3d(var3 - r.renderPosX + w, var5 - r.renderPosY + h, var7 - r.renderPosZ + w);
		GL11.glVertex3d(var3 - r.renderPosX - w, var5 - r.renderPosY + h, var7 - r.renderPosZ + w);
		GL11.glVertex3d(var3 - r.renderPosX + w, var5 - r.renderPosY, var7 - r.renderPosZ + w);
		GL11.glVertex3d(var3 - r.renderPosX + w, var5 - r.renderPosY + h, var7 - r.renderPosZ + w);
		GL11.glVertex3d(var3 - r.renderPosX - w, var5 - r.renderPosY, var7 - r.renderPosZ + w);
		GL11.glVertex3d(var3 - r.renderPosX - w, var5 - r.renderPosY + h, var7 - r.renderPosZ + w);
		GL11.glVertex3d(var3 - r.renderPosX - w, var5 - r.renderPosY, var7 - r.renderPosZ - w);
		GL11.glVertex3d(var3 - r.renderPosX - w, var5 - r.renderPosY + h, var7 - r.renderPosZ - w);
		GL11.glVertex3d(var3 - r.renderPosX + w, var5 - r.renderPosY, var7 - r.renderPosZ - w);
		GL11.glVertex3d(var3 - r.renderPosX + w, var5 - r.renderPosY + h, var7 - r.renderPosZ - w);

		GL11.glEnd();
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glPopMatrix();
	}

	private void trace(Minecraft mc, Entity e, float par1, String color) {
		if (mc.getRenderManager().renderViewEntity != null) {
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glLineWidth(1F);
			Color c = Color.decode(color);
			GL11.glPushMatrix();
			GL11.glDepthMask(false);
			GL11.glColor3d((double) c.getRed() / 255.0, (double) c.getGreen() / 255.0, (double) c.getBlue() / 255.0);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glBegin(GL11.GL_LINES);
			RenderManager r = mc.getRenderManager();
			Vec3d v = new Vec3d(0.0D, 0.0D, 1.0D).rotatePitch(-((float) Math.toRadians((double) mc.thePlayer.rotationPitch))).rotateYaw(-((float) Math.toRadians((double) mc.thePlayer.rotationYaw)));
			GL11.glVertex3d(v.xCoord, mc.thePlayer.getEyeHeight() + v.yCoord, v.zCoord);
			double var3 = e.lastTickPosX + (e.posX - e.lastTickPosX) * (double) par1;
			double var5 = e.lastTickPosY + (e.posY - e.lastTickPosY) * (double) par1;
			double var7 = e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * (double) par1;
			GL11.glVertex3d(var3 - r.renderPosX, var5 - r.renderPosY + 0.25, var7 - r.renderPosZ);
			GL11.glEnd();
			GL11.glDepthMask(true);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glPopMatrix();
		}
	}

	private void trace(Minecraft mc, EntityOtherPlayerMP e, float par1, double opacity) {
		/*if (CubAimbot.get().isFriend(par1Entity.getName())) {
			opacity = 0.4;
		}*/
		RenderManager r = mc.getRenderManager();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glLineWidth(1F);
		float hp = e.getHealth();
		Color c = Color.decode("#" + (hp / 2 > 7 ? "55FF55" : hp / 2 >= 4 ? "FFFF55" : "FF5555"));
		GL11.glPushMatrix();
		GL11.glDepthMask(false);
		GL11.glColor4d((double) c.getRed() / 255.0, (double) c.getGreen() / 255.0, (double) c.getBlue() / 255.0, opacity);
		double var3 = e.lastTickPosX + (e.posX - e.lastTickPosX) * (double) par1;
		double var5 = e.lastTickPosY + (e.posY - e.lastTickPosY) * (double) par1;
		double var7 = e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * (double) par1;
		Vec3d v = new Vec3d(0.0D, 0.0D, 1.0D).rotatePitch(-((float) Math.toRadians((double) mc.thePlayer.rotationPitch))).rotateYaw(-((float) Math.toRadians((double) mc.thePlayer.rotationYaw)));
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex3d(v.xCoord, mc.thePlayer.getEyeHeight()+v.yCoord, v.zCoord);
		GL11.glVertex3d(var3 - r.renderPosX, var5 - r.renderPosY, var7 - r.renderPosZ);
		GL11.glEnd();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glPopMatrix();
	}

	@Override
	public void onEnabled() {

	}

	@Override
	public void onDisabled() {

	}

	@Override
	public String getName() {
		return null;
	}
}