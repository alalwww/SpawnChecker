/*
 * (c) 2014 alalwww
 * https://github.com/alalwww
 * 
 * This mod is distributed under the terms of the Minecraft Mod Public License 1.0, or MMPL.
 * Please check the contents of the license located in http://www.mod-buildcraft.com/MMPL-1.0.txt
 * 
 * この MOD は、Minecraft Mod Public License (MMPL) 1.0 の条件のもとに配布されています。
 * ライセンスの内容は次のサイトを確認してください。 http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package net.awairo.mcmod.common.v1.util;

import static com.google.common.base.Preconditions.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.resources.I18n;

import cpw.mods.fml.common.event.FMLFingerprintViolationEvent;

/**
 * 署名.
 * 
 * @author alalwww
 */
public enum Fingerprint
{
    HANDLER;

    public static final String VALUE = "26e70d58815b73cd7c6c865fe091672f79070a35";

    /**
     * FMLFingerprintViolationEvent イベントを処理します.
     * 
     * @param modid Mod ID
     * @param event イベント
     */
    public void handle(String modid, FMLFingerprintViolationEvent event)
    {
        checkNotNull(modid, "modid");
        checkNotNull(event, "event");

        if (Env.develop() && event.isDirectory)
            return;

        final String msg = "\n"
                + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-\n"
                + "\n"
                + " This mod may have been hacked. (modid: {})\n"
                + "\n"
                + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-";

        final Logger logger = LogManager.getLogger(modid);

        if (Env.getBoolean(modid + ".ignoreFingerprintViolation").or(false))
        {
            logger.warn(msg, modid);
            return;
        }

        logger.error(msg + "\nPlease specify JVM option ’-D{}.ignoreFingerprintViolation=true’ "
                + "if you badly want to ignore this error.\n", modid, modid);

        throw new RuntimeException(I18n.format("spawnchecker.fingerprint_violation", modid));
    }
}
