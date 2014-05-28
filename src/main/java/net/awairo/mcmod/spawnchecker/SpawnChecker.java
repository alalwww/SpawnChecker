/*
 * SpawnChecker.
 * 
 * (c) 2014 alalwww
 * https://github.com/alalwww
 * 
 * This mod is distributed under the terms of the Minecraft Mod Public License 1.0, or MMPL.
 * Please check the contents of the license located in http://www.mod-buildcraft.com/MMPL-1.0.txt
 * 
 * この MOD は、Minecraft Mod Public License (MMPL) 1.0 の条件のもとに配布されています。
 * ライセンスの内容は次のサイトを確認してください。 http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package net.awairo.mcmod.spawnchecker;

import static com.google.common.base.Preconditions.*;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLFingerprintViolationEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms.IMCEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

import net.awairo.mcmod.common.v1.util.Fingerprint;
import net.awairo.mcmod.spawnchecker.client.mode.AlwaysRunMode;
import net.awairo.mcmod.spawnchecker.client.mode.ConditionalMode;
import net.awairo.mcmod.spawnchecker.client.mode.Mode;
import net.awairo.mcmod.spawnchecker.client.mode.SelectableMode;

/**
 * SpawnChecker
 * 
 * @author alalwww
 */
@Mod(
        modid = SpawnChecker.MOD_ID,
        certificateFingerprint = Fingerprint.VALUE,
        guiFactory = "net.awairo.mcmod.spawnchecker.client.gui.GuiScreenFactory")
public final class SpawnChecker
{
    /** mod id. */
    public static final String MOD_ID = "spawnchecker";

    /** Greeting message key (for presence check). */
    public static final String IMC_HELLO = "hello";

    /** mode registration message key. */
    public static final String IMC_REGISTERMODE = "registerMode";

    // ----------------------

    // プリセットモードからも参照したいのでパッケージプライベートで公開している
    @SidedProxy(
            modId = SpawnChecker.MOD_ID,
            clientSide = "net.awairo.mcmod.spawnchecker.client.ClientSideProxy",
            serverSide = "net.awairo.mcmod.spawnchecker.server.ServerSideProxy")
    static Proxy sideProxy;

    // ----------------------

    // コンストラクター非公開

    @Mod.InstanceFactory
    private static SpawnChecker newInstance()
    {
        return new SpawnChecker();
    }

    private SpawnChecker()
    {
    }

    //------------------------

    @Mod.EventHandler
    private void handleModEvent(FMLPreInitializationEvent event)
    {
        sideProxy.handleModEvent(event);
    }

    @Mod.EventHandler
    private void handleModEvent(FMLInitializationEvent event)
    {
        sideProxy.handleModEvent(event);
    }

    @Mod.EventHandler
    private void handleModEvent(FMLPostInitializationEvent event)
    {
        sideProxy.handleModEvent(event);
    }

    @Mod.EventHandler
    private void handleModEvent(IMCEvent event)
    {
        sideProxy.handleModEvent(event);
    }

    @Mod.EventHandler
    private void handleModEvent(FMLFingerprintViolationEvent event)
    {
        sideProxy.handleModEvent(event);
    }

    //------------------------

    /**
     * 新たなモードを登録します.
     * 
     * @param mode 選択起動モード
     */
    public static void registerMode(SelectableMode mode)
    {
        registerMode((Mode) mode);
    }

    /**
     * 新たなモードを登録します.
     * 
     * @param mode 条件起動モード
     */
    public static void registerMode(ConditionalMode mode)
    {
        registerMode((Mode) mode);
    }

    /**
     * 新たなモードを登録します.
     * 
     * @param mode 常時起動モード
     */
    public static void registerMode(AlwaysRunMode mode)
    {
        registerMode((Mode) mode);
    }

    private static void registerMode(Mode mode)
    {
        checkState(sideProxy != null, "mod is uninitialized.");
        sideProxy.registerMode(mode);
    }
}
