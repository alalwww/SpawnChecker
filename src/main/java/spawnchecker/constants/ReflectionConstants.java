package spawnchecker.constants;

import static net.minecraft.src.mod_SpawnChecker.RELEASE;

/**
 * reflections constants.
 *
 * @author takuru/ale
 */
public final class ReflectionConstants
{
    // for minecraft1.6.1
    /**
     * sendQueue.
     *
     * @see net.minecraft.client.multiplayer.WorldClient
     * */
    public static final String SEND_QUEUE = RELEASE ? "field_73035_a" : "sendQueue";

    /**
     * netManager.
     *
     * @see net.minecraft.client.multiplayer.NetClientHandler
     */
    public static final String NET_MANAGER = RELEASE ? "field_72555_g" : "netManager";

    /**
     * remoteSocketAddress.
     *
     * @see net.minecraft.network.TcpConnection
     */
    public static final String REMOTE_ADDR = RELEASE ? "field_74476_j" : "remoteSocketAddress";

    private ReflectionConstants()
    {
    }
}
