package me.gameisntover.knockbackffa.nms;

import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NMSUtil {

    public static Object getConnection(Player player) throws SecurityException, NoSuchMethodException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Method getHandle = player.getClass().getMethod("getHandle");
        Object nmsPlayer = getHandle.invoke(player);
        Field conField = nmsPlayer.getClass().getField("playerConnection");
        return conField.get(nmsPlayer);
    }

    public static Class<?> getNMSClass(String nmsClassString) throws ClassNotFoundException {
        String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
        String name = "net.minecraft.server." + version + nmsClassString;
        return Class.forName(name);
    }

    @SneakyThrows
    public static Object getNMSMethod(Object obj, String method, Object... args){
        return obj.getClass().getMethod(method).invoke(obj,args);
    }

    public static Class<?> getKMSClass(String kmsClassString) throws ClassNotFoundException{
        String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
        String name = "me.gameisntover.knockbackffa_" + version + kmsClassString;
        return Class.forName(name);
    }

    public static void registerEntity(String name, int id, Class nmsClass, Class customClass){
        try {

            List<Map<?, ?>> dataMap = new ArrayList<>();
            for (Field f : getNMSClass("EntityTypes").getDeclaredFields()){
                if (f.getType().getSimpleName().equals(Map.class.getSimpleName())){
                    f.setAccessible(true);
                    dataMap.add((Map<?, ?>) f.get(0));
                }
            }

            if (dataMap.get(2).containsKey(id)){
                dataMap.get(0).remove(name);
                dataMap.get(2).remove(id);
            }

            Method method = getNMSClass("EntityTypes").getDeclaredMethod("a", Class.class, String.class, int.class);
            method.setAccessible(true);
            method.invoke(null, customClass, name, id);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
