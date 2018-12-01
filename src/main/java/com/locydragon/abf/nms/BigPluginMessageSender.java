package com.locydragon.abf.nms;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BigPluginMessageSender {
	private static final String nmsVersion
			= org.bukkit.Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
	public static void send(Player who, String channel, byte[] bytes) {
		try {
			Class<?> dataSerializer = Class.forName("net.minecraft.server."+nmsVersion+".PacketDataSerializer");
			ByteBuf byteBuf = Unpooled.wrappedBuffer(bytes);
			Object instance = dataSerializer.getConstructor(ByteBuf.class).newInstance(byteBuf);
			Class<?> payLoadPacket = Class.forName("net.minecraft.server."+nmsVersion+".PacketPlayOutCustomPayload");
			Object packetInstance = payLoadPacket.getConstructor(String.class, dataSerializer).newInstance(channel, instance);
			Class<?> craftPlayer = Class.forName("org.bukkit.craftbukkit."+nmsVersion+".entity.CraftPlayer");
			Object targetCraftPlayer = craftPlayer.cast(who);
			Method getHandle = craftPlayer.getMethod("getHandle");
			Object nmsPlayer = getHandle.invoke(targetCraftPlayer);
			Class<?> nmsEntityPlayer = Class.forName("net.minecraft.server."+nmsVersion+".EntityPlayer");
			try {
				Field playerConnectionField = nmsEntityPlayer.getField("playerConnection");
				Object playerConnection = playerConnectionField.get(nmsPlayer);
				Class<?> playerConnectionClass = Class.forName("net.minecraft.server."+nmsVersion+".PlayerConnection");
				Method sendPacketMethod
						= playerConnectionClass.getMethod("sendPacket", Class.forName("net.minecraft.server."+nmsVersion+".Packet"));
				sendPacketMethod.invoke(playerConnection, packetInstance);
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
