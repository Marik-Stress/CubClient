package es.blueberrypancak.module;

import java.util.List;

import es.blueberrypancak.Client;
import es.blueberrypancak.event.EventRender;
import es.blueberrypancak.event.Subscribe;
import es.blueberrypancak.hook.EntityPlayerSPHook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;

@RegisterModule(key=34,color=16775680,listed=true)
public class KillAura extends Module {

	private static double distanceThreshold = 36.0D;

	@Subscribe
	public void onRender(EventRender e) {
		Minecraft mc = Client.getMinecraft();
		List<Entity> l = mc.theWorld.getLoadedEntityList();
		EntityPlayer p = mc.thePlayer;
		if(isEnabled()) {
			if(p.getCooledAttackStrength(1.0F) == 1.0 && !p.isHandActive()) {
				Entity o = getClosestEntity();
				if(o != null && !p.isSwingInProgress) {
					hit(p, o);
				}
			}
		}
	}

	private int getWeaponSlot() {
		int slot = -1;
		double w = -1;
		EntityPlayer p = Client.getMinecraft().thePlayer;
		for(int i = 0; i < 9; i++) {
			ItemStack o = p.inventory.mainInventory[i];
			if(o != null) {
				List<String> data = o.getTooltip(p, false);
				double damage = -1;
				double speed = -1;
				for(String s : data) {
					if(s.contains("Damage")) {
						damage = Double.parseDouble(s.split(" ")[1]);
					} else if(s.contains("Speed")) {
						speed = Double.parseDouble(s.split(" ")[1]);
					}
				}
				if (damage != -1 && speed != -1) {
					double weight = damage*speed;
					if (weight > w) {
						w = weight;
						slot = i;
					}
				}
			}
		}
		return slot == -1 ? p.inventory.currentItem : slot;
	}

	private void faceEntity(Entity par1Entity) {
		EntityPlayerSPHook player = (EntityPlayerSPHook) Client.getMinecraft().thePlayer;
		double var4 = par1Entity.posX - player.posX;
		double var8 = par1Entity.posZ - player.posZ;
		double var6;

		if(par1Entity instanceof EntityLivingBase) {
			EntityLivingBase var10 = (EntityLivingBase) par1Entity;
			var6 = var10.posY + (double) var10.getEyeHeight() - (player.posY + (double) player.getEyeHeight());
		} else {
			var6 = (par1Entity.getEntityBoundingBox().minY + par1Entity.getEntityBoundingBox().maxY) / 2.0D - (player.posY + (double) player.getEyeHeight());
		}

		double var14 = (double) MathHelper.sqrt_double(var4 * var4 + var8 * var8);
		float var12 = (float) (Math.atan2(var8, var4) * 180.0D / Math.PI) - 90.0F;
		float var13 = (float) (-(Math.atan2(var6, var14) * 180.0D / Math.PI));
		player.getConnection().sendPacket(new CPacketPlayer.PositionRotation(player.posX, player.posY, player.posZ, var12, var13, player.onGround));
	}

	private Entity getClosestEntity() {
		Minecraft mc = Client.getMinecraft();
		List<Entity> l = mc.theWorld.getLoadedEntityList();
		EntityPlayer p = mc.thePlayer;
		Entity e = null;
		for(Entity o : l) {
			if(o != p && (o instanceof EntityOtherPlayerMP || o instanceof EntityLiving)) {
				if(o.isEntityAlive()) {
					if(e == null || o.getDistanceSqToEntity(p) <= e.getDistanceSqToEntity(p)) {
						if(o.getDistanceSqToEntity(p) <= distanceThreshold) {
							e = o;
						}
					}
				}
			}
		}
		return e;
	}

	private void hit(EntityPlayer p, Entity e) {
		p.inventory.currentItem = getWeaponSlot();
		PlayerControllerMP controller = Client.getMinecraft().playerController;
		faceEntity(e);
		controller.attackEntity(p, e);
		p.swingArm(EnumHand.MAIN_HAND);
	}

	@Override
	public void onEnabled() {
		Client.getMinecraft().thePlayer.inventory.currentItem = getWeaponSlot();
	}

	@Override
	public void onDisabled() {
		
	}

	@Override
	public String getName() {
		return String.format("%.1f", new Object[] { Double.valueOf(distanceThreshold) }) + "m";
	}
}