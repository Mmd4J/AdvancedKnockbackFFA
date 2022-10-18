package me.gameisntover.knockbackffa.player;

import lombok.SneakyThrows;
import me.gameisntover.knockbackffa.nms.NMSUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class KnockerConnection {
    public Knocker knocker;
    public Object playerConnection;
    public final static Map<Knocker,KnockerConnection> connectionMap = new HashMap<>();
    protected KnockerConnection(Knocker knocker){
        this.knocker = knocker;
        try {
            playerConnection = NMSUtil.getConnection(knocker.getPlayer());
        } catch (NoSuchMethodException | NoSuchFieldException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    public void sendPacket(String packetName,int i, Object... args){
        Object packet = NMSUtil.getNMSClass(packetName).getConstructors()[i].newInstance(args);
        NMSUtil.getNMSMethod(packet,"sendPacket",packet);
    }

    protected static KnockerConnection fromKnocker(Knocker knocker){
        if (!connectionMap.containsKey(knocker)) connectionMap.put(knocker,new KnockerConnection(knocker));
        return connectionMap.get(knocker);
    }
}
