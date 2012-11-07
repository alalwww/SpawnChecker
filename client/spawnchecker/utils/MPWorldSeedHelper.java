package spawnchecker.utils;

import static spawnchecker.constants.Constants.SERVERS_SEED_SETTING_FILENAME;
import static spawnchecker.constants.ReflectionConstants.NET_MANAGER;
import static spawnchecker.constants.ReflectionConstants.REMOTE_ADDR;
import static spawnchecker.constants.ReflectionConstants.SEND_QUEUE;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.minecraft.src.NetClientHandler;
import net.minecraft.src.INetworkManager;
import net.minecraft.src.TcpConnection;
import net.minecraft.src.World;
import net.minecraft.src.WorldClient;
import spawnchecker.Settings;
import spawnchecker.SpawnChecker;

/**
 * サーバーシード設定ファイルをロードし、現在のサーバーのシード設定があればそれを返します.
 *
 * @author takuru/ale
 */
public class MPWorldSeedHelper
{
    private static final Map<String, Long> mpSeedMap = new HashMap<String, Long>();

    /**
     * 設定を読み込んでパースしmapにつっこみます.
     */
    public static void loadServersSeedSetting()
    {
        File f = SpawnChecker.mod.getModConfigFile(SERVERS_SEED_SETTING_FILENAME);
        Properties prop = new Properties();

        try
        {
            if (f.exists())
            {
                SpawnChecker.mod.loadProperties(prop, f);
            }
            else
            {
                SpawnChecker.mod.info("create .minecraft/servers.txt");
                prop.setProperty("example.com/25565", "9223372036854775807");
                SpawnChecker.mod.saveProperties(prop, " << SpawnChecker server seed settings >>\n\n"
                        + " ex) [host_name]/[port]=[seed]\n   awairo.net/1234=9223372036854775807\n\n"
                        + " ex) [ip_address]/[port]=[seed]\n   127.0.0.1/5678=9223372036854775807\n", f);
            }
        }
        catch (IllegalStateException e)
        {
            // ファイルが読めなかったら起動失敗とせず諦める
            SpawnChecker.mod.debug(e, e.getMessage());
            SpawnChecker.mod.warn("Failed to load the file for servers seed mapping.");
            return;
        }

        for (Object key : prop.keySet())
        {
            Object value = prop.get(key);

            try
            {
                Long seed = value != null ? Long.valueOf(value.toString()) : null;

                if (seed != null)
                {
                    SpawnChecker.mod.debug("put to seed map: key=", key, " value=", value);
                    mpSeedMap.put(key.toString(), seed);
                }
                else
                {
                    SpawnChecker.mod.warn("Invalid property: key=", key, ", value=", value);
                }
            }
            catch (NumberFormatException e)
            {
                SpawnChecker.mod.debug(e, e.getMessage());
                SpawnChecker.mod.warn(e.getMessage());
            }
        }
    }

    /**
     * 現在のワールドからサーバー情報を取得しマップにあればそのSeed値を返します.
     * なければnull.
     */
    public static Long getServerSeed()
    {
        Settings settings = SpawnChecker.getSettings();
        World w = settings.getCurrentWorld();

        if (!(w instanceof WorldClient))
        {
            return null;
        }

        NetClientHandler nch = SpawnChecker.mod.getPrivateValue(WorldClient.class, (WorldClient) w, SEND_QUEUE);
        INetworkManager nm = SpawnChecker.mod.getPrivateValue(NetClientHandler.class, nch, NET_MANAGER);

        if (!(nm instanceof TcpConnection))
        {
            return null;
        }

        SpawnChecker.mod.debug("current world is remote. try servername.");
        InetSocketAddress addr = SpawnChecker.mod.getPrivateValue(TcpConnection.class, (TcpConnection) nm, REMOTE_ADDR);
        String host = addr.getHostName();

        if (host == null)
        {
            SpawnChecker.mod.warn("could not get server information.");
            return null;
        }

        String key = new StringBuilder().append(addr.getHostName()).append("/").append(addr.getPort()).toString();
        SpawnChecker.mod.debug("primary key=", key);
        Long seed = mpSeedMap.get(key); // Long value or null

        if (seed == null && !addr.isUnresolved())
        {
            String s = new StringBuilder().append(addr.getAddress().getHostAddress()).append("/")
            .append(addr.getPort()).toString();
            SpawnChecker.mod.debug("secoundary key=", s);

            if (!key.equals(s))
            {
                key = s;
                seed = mpSeedMap.get(key);
            }
        }

        SpawnChecker.mod.info("server: ", key);

        if (seed != null)
        {
            SpawnChecker.mod.info("server seed: ", seed);
        }
        else
        {
            SpawnChecker.mod.info("unknown server. seed settings is none.");
        }

        return seed;
    }
}
