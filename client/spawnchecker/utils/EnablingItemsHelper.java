package spawnchecker.utils;

import java.util.Arrays;

import net.minecraft.src.Block;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;
import spawnchecker.Settings;
import spawnchecker.SpawnChecker;

/**
 * 手に持ってるアイテムの判定などを行うヘルパークラス.
 *
 * @author takuru/ale
 */
public class EnablingItemsHelper
{
    /**
     * 設定を読み込んでパースし保持.
     */
    public static void loadPromotedItems(String enablingItemsCSV)
    {
        Settings settings = SpawnChecker.getSettings();
        String[] sa = enablingItemsCSV.split(",");
        SpawnChecker.mod.debug("parse 'enablingItemsCSV' value. ");
        int size = sa.length;
        int[] ids = new int[size];
        int length = 0;

        for (int i = 0; i < size; i++)
        {
            try
            {
                int id = Integer.parseInt(sa[i].trim());

                if (id < 0 || id >= 32000)
                {
                    SpawnChecker.mod.warn("Invalid value for config 'enablingItemsCSV'.: Value '", id,
                            "' is out of id range.");
                    continue;
                }

                ids[i] = id;
                length++;
            }
            catch (NumberFormatException e)
            {
                SpawnChecker.mod.debug(e, e.getMessage());
                SpawnChecker.mod.warn("config 'enablingItemsCSV' is invalid value.", ids[i]);
            }
        }

        int[] enablingItemIds;

        if (length > 0)
        {
            enablingItemIds = Arrays.copyOf(ids, length);
        }
        else
        {
            enablingItemIds = new int[] { Block.torchWood.blockID };
        }

        SpawnChecker.mod.debug("set promoted item ids. ", Arrays.toString(enablingItemIds));
        settings.setEnablingItemIds(enablingItemIds);
    }

    public static boolean hasEnablingItem()
    {
        ItemStack stack = ModLoader.getMinecraftInstance().thePlayer.inventory.getCurrentItem();

        if (stack == null)
        {
            return false;
        }

        for (int id : SpawnChecker.getSettings().getEnablingItemIds())
        {
            if (stack.itemID == id)
            {
                return true;
            }
        }

        return false;
    }

    private EnablingItemsHelper()
    {
    }
}
