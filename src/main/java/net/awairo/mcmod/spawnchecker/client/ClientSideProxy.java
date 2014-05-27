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

package net.awairo.mcmod.spawnchecker.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms.IMCMessage;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.client.settings.KeyBinding;

import net.awairo.mcmod.spawnchecker.Proxy;
import net.awairo.mcmod.spawnchecker.SpawnChecker;
import net.awairo.mcmod.spawnchecker.client.common.Settings;
import net.awairo.mcmod.spawnchecker.client.controls.KeyManager;
import net.awairo.mcmod.spawnchecker.client.gui.GuiManager;
import net.awairo.mcmod.spawnchecker.client.mode.Mode;
import net.awairo.mcmod.spawnchecker.client.mode.core.ModeManager;
import net.awairo.mcmod.spawnchecker.client.mode.information.InformationManager;

/**
 * ClientSideProxy
 * 
 * @author alalwww
 */
public final class ClientSideProxy extends Proxy
{
    /**
     * @return 設定
     */
    public Settings settings()
    {
        return ClientManager.settings;
    }

    @Override
    protected void handleModEvent(FMLPreInitializationEvent event)
    {
        super.handleModEvent(event);

        ClientManager.settings = new Settings(event.getSuggestedConfigurationFile());
    }

    @Override
    protected void handleModEvent(FMLInitializationEvent event)
    {
        super.handleModEvent(event);

        for (KeyBinding key : settings().keyConfig().keys())
            ClientRegistry.registerKeyBinding(key);

        registerFMLEventListener(settings().newTickEventListener());

        KeyManager.load();
        GuiManager.load();
        InformationManager.load();
        ModeManager.load();

        for (ClientManager manager : ClientManager.managers.values())
            registerEventListener(manager);
    }

    @Override
    protected void handleModEvent(FMLPostInitializationEvent event)
    {
        super.handleModEvent(event);

        ClientManager.get(ModeManager.class).initialize();
    }

    @Override
    protected void handleMessage(IMCMessage msg)
    {
        switch (msg.key)
        {
            case SpawnChecker.IMC_REGISTERMODE:
                registerMode(msg);
                break;

            default:
                super.handleMessage(msg);
                break;
        }
    }

    private void registerMode(IMCMessage msg)
    {
        try
        {
            @SuppressWarnings("unchecked")
            final Class<Mode> modeClass = (Class<Mode>) Class.forName(msg.getStringValue());
            registerMode(modeClass.newInstance());
        }
        catch (ReflectiveOperationException | RuntimeException e)
        {
            // メッセージの内容に問題があるのは自信の責任ではないので落とさずロギングだけして無視
            LOGGER.warn("unexpected imc event from {}", msg.getSender(), e);
        }
    }

    @Override
    protected void registerMode(Mode mode)
    {
        ClientManager.get(ModeManager.class).addMode(mode);
        LOGGER.info("SpawnChecker: mode {}(id:{}) has been registered.", mode.name(), mode.id());
    }
}
