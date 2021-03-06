package client.main.module;

import client.main.Client;
import client.main.event.EventRecPacket;
import client.main.event.EventRender;
import client.main.event.EventSendPacket;
import client.main.event.Subscribe;
import client.main.helper.InventoryHelper;
import client.main.hook.EntityPlayerSPHook;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.server.SPacketParticles;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

@RegisterModule(key = 37, color = 0x3F7F47, secondary_color = 0xFF1C07, listed = true)
public class AutoFish extends Module {

	private long nextTick;

	private int lastSlot = -1;

	private final int FISHING_ROD = 346;

	@Subscribe
	public void onSendPacket(EventSendPacket e) {
		Packet packet = e.getValue();
		if (isEnabled() && packet instanceof CPacketHeldItemChange && getFishingRod() != -1) {
			CPacketHeldItemChange change = (CPacketHeldItemChange) packet;
			if (lastSlot != -1 && change.getSlotId() != lastSlot) {
				e.setValue(new CPacketHeldItemChange(lastSlot));
			}
		}
	}

	@Subscribe
	public void onReceivePacket(EventRecPacket e) {
		Packet packet = e.getValue();
		if (isEnabled() && packet instanceof SPacketParticles) {
			SPacketParticles particle = (SPacketParticles) packet;
			if (particle.getParticleType() == EnumParticleTypes.WATER_WAKE) {
				if (particle.getParticleCount() == 6 && particle.getParticleSpeed() == 0.2F) {
					EntityFishHook o = getFishHook();
					if (o != null && o.getDistance(particle.getXCoordinate(), particle.getYCoordinate(),
							particle.getZCoordinate()) < 1.5) {
						if (equipRod()) {
							toss();
							toss();
						}
					}
				}
			}
		}
	}

	private boolean equipRod() {
		int slot = getFishingRod();
		if (slot < 0)
			return false;
		EntityPlayerSPHook p = (EntityPlayerSPHook) Client.getMinecraft().player;
		lastSlot = slot;
		if (slot >= 9) {
			int chosenSlot = InventoryHelper.getEmptySlot();
			InventoryHelper.move(slot, chosenSlot);
			lastSlot = chosenSlot;
		}
		p.getConnection().sendPacket(new CPacketHeldItemChange(lastSlot));
		return true;
	}

	private int getFishingRod() {
		return InventoryHelper.getItem(FISHING_ROD);
	}

	private void toss() {
		EntityPlayerSPHook p = (EntityPlayerSPHook) Client.getMinecraft().player;
		p.getConnection().sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
	}

	private EntityFishHook getFishHook() {
		Minecraft mc = Client.getMinecraft();
		for (Entity e : mc.world.loadedEntityList) {
			if (e instanceof EntityFishHook) {
				EntityFishHook o = (EntityFishHook) e;
				if (o.angler == mc.player) {
					return o;
				}
			}
		}
		return mc.player.fishEntity;
	}

	private boolean isFishing() {
		return getFishHook() != null;
	}

	private boolean facingWater(int dist) {
		Minecraft mc = Client.getMinecraft();
		EntityPlayer p = mc.player;
		Vec3d vec3d = ((Entity) p).getPositionEyes(1F);
		Vec3d vec3d1 = ((Entity) p).getLook(1F);
		Vec3d vec3d2 = vec3d.addVector(vec3d1.xCoord * dist, vec3d1.yCoord * dist, vec3d1.zCoord * dist);
		RayTraceResult result = mc.world.rayTraceBlocks(vec3d, vec3d2, true, false, true);
		if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
			return mc.world.getBlockState(result.getBlockPos()).toString().contains("water");
		}
		return false;
	}

	@Subscribe
	public void onRender(EventRender e) {
		if (isEnabled()) {
			if (lastSlot == -1 || Client.getMinecraft().player.inventory.mainInventory.isEmpty() || !isFishing()) {
				if (System.currentTimeMillis() >= nextTick) {
					onEnabled();
				}
			}
			active_color = getFishingRod() != -1 ? getColor() : getSecondaryColor();
		}
	}

	@Override
	public void onEnabled() {
		if (equipRod() && facingWater(15)) {
			toss();
		}
		nextTick = System.currentTimeMillis() + 1500;
	}

	@Override
	public void onDisabled() {
		if (isFishing()) {
			onEnabled();
		}
		EntityPlayerSPHook p = (EntityPlayerSPHook) Client.getMinecraft().player;
		p.getConnection().sendPacket(new CPacketHeldItemChange(p.inventory.currentItem));
	}

	@Override
	public String getName() {
		return getFishingRod() != -1 ? "AutoFish" : "No rod!";
	}
}
