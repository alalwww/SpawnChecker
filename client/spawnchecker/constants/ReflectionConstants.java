package spawnchecker.constants;

import static net.minecraft.src.mod_SpawnChecker.RELEASE;

/**
 * reflections constants.
 *
 * @author takuru/ale
 */
public final class ReflectionConstants
{
    // for minecraft1.5.0 pre (MCP7.42)
    /**
     * sendQueue.
     *
     * @see net.minecraft.src.WorldClient
     * */
    public static final String SEND_QUEUE = RELEASE ? "a" : "sendQueue";

    /**
     * netManager.
     *
     * @see net.minecraft.src.NetClientHandler
     */
    public static final String NET_MANAGER = RELEASE ? "g" : "netManager";

    /**
     * remoteSocketAddress.
     *
     * @see net.minecraft.src.TcpConnection
     */
    public static final String REMOTE_ADDR = RELEASE ? "k" : "remoteSocketAddress";

    private ReflectionConstants()
    {
    }
}
