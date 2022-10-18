package me.gameisntover.knockbackffa.nms;

import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NMSUtil {

    public static Class<?> getNMSClass(String nmsClassString) throws ClassNotFoundException {
        String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
        String name = "net.minecraft.server." + version + nmsClassString;
        Class<?> nmsClass = Class.forName(name);
        return nmsClass;
    }

    public static Object getConnection(Player player) throws SecurityException, NoSuchMethodException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Method getHandle = player.getClass().getMethod("getHandle");
        Object nmsPlayer = getHandle.invoke(player);
        Field conField = nmsPlayer.getClass().getField("playerConnection");
        return conField.get(nmsPlayer);
    }

    @SneakyThrows
    public static Object getNMSMethod(Object obj, String method, Object... args){
        return obj.getClass().getMethod(method).invoke(args);
    }

    public static Class<?> getKMSClass(String kmsClassString) throws ClassNotFoundException{
        String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
        String name = "net.minecraft.server.knockbackffa_" + version + kmsClassString;
        Class<?> kmsClass = Class.forName(name);
        return kmsClass;
    }

    @SneakyThrows
    public static Object getKMSMethod(Object obj, String methodName, Object... args){
        return obj.getClass().getMethod(methodName).invoke(args);
    }
}
