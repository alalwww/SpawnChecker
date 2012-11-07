package spawnchecker.enums;

import static spawnchecker.constants.Constants.MODE_ID_SC;
import static spawnchecker.constants.Constants.MODE_ID_SF;
import static spawnchecker.constants.Constants.MODE_ID_SV;

import java.util.LinkedList;
import java.util.List;

import spawnchecker.Settings;
import spawnchecker.SpawnChecker;
import spawnchecker.constants.Resources;

/**
 * SpawnChecker mode.
 *
 * @author takuru/ale
 */
public enum Mode
{
    /**
     * スポーンポイントチェックモード.
     */
    SPAWABLE_POINT_CHECKER()
    {
        @Override
        protected String getOptionSetSetting()
        {
            return settings.getOptionSetSC();
        }
        @Override
        protected int getOptionMask()
        {
            switch (settings.getDimension())
            {
                case NETHER:
                    return Option.SLIME;

                case SURFACE:
                    if (settings.getWorldSeed() == null)
                    {
                        return Option.SLIME | Option.GHAST;
                    }

                    return Option.GHAST;

                case THE_END:
                    return Option.SLIME | Option.GHAST;
            }

            return 0;
        }
        @Override
        protected int getOptionSetting()
        {
            return settings.getOptionSC();
        }
        @Override
        protected void updateOption(int newOption)
        {
            descList.add(Resources.MODE_SPAWNING_POINT_CHECKER);

            if (newOption > Option.DISABLE)
            {
                if ( and (newOption, Option.GHAST))
                {
                    descList.add(Resources.MODE_GHAST_MARKER);
                }

                if ( and (newOption, Option.MARKER))
                {
                    descList.add(Resources.MODE_MAKER);
                }

                if ( and (newOption, Option.SLIME))
                {
                    descList.add(Resources.MODE_SLIME_FINDER);
                }

                if ( and (newOption, Option.GUIDELINE))
                {
                    descList.add(Resources.MODE_GUIDELINE);
                }

                if ( and (newOption, Option.FORCE))
                {
                    descList.add(Resources.MODE_FORCE);
                }
            }
            else
            {
                descList.add(Resources.MODE_DISABLED);
            }

            settings.setOptionSC(newOption);
        }
        @Override
        public String toString()
        {
            return MODE_ID_SC;
        }
    },
    /**
     * スライムチャンク探しモード.
     */
    SLIME_CHUNK_FINDER()
    {
        @Override
        protected String getOptionSetSetting()
        {
            return settings.getOptionSetSF();
        }
        @Override
        protected int getOptionMask()
        {
            switch (settings.getDimension())
            {
                case NETHER:
                    return 0xf;

                case SURFACE:
                    if (settings.getWorldSeed() == null)
                    {
                        return 0xf;
                    }

                    return 0;

                case THE_END:
                    return 0xf;
            }

            return 0;
        }
        @Override
        protected int getOptionSetting()
        {
            return settings.getOptionSF();
        }
        @Override
        public int getOption()
        {
            int b = super.getOption();
            return b > Option.DISABLE ? (b | Option.SLIME) : b;
        }
        @Override
        protected void updateOption(int newOption)
        {
            newOption = newOption & ~Option.SLIME;
            descList.add(Resources.MODE_SLIME_CHUNK_FINDER);

            if (newOption > Option.DISABLE)
            {
                if ( and (newOption, Option.MARKER))
                {
                    descList.add(Resources.MODE_MAKER);
                }

                if ( and (newOption, Option.GUIDELINE))
                {
                    descList.add(Resources.MODE_GUIDELINE);
                }

                if ( and (newOption, Option.CHUNK_MARKER))
                {
                    descList.add(Resources.MODE_CHUNK_MARKER);
                }
            }
            else
            {
                descList.add(Resources.MODE_DISABLED);
            }

            settings.setOptionSF(newOption);
        }
        @Override
        public String toString()
        {
            return MODE_ID_SF;
        }
    },
    /**
     * スポナー可視化モード.
     */
    SPAWNER_VISUALIZER()
    {
        @Override
        protected String getOptionSetSetting()
        {
            return settings.getOptionSetSV();
        }
        @Override
        protected int getOptionMask()
        {
            return 0;
        }
        @Override
        protected int getOptionSetting()
        {
            return settings.getOptionSV();
        }
        @Override
        protected void updateOption(int newOption)
        {
            descList.add(Resources.MODE_SPAWNER_VISUALIZER);

            if (newOption > Option.DISABLE)
            {
                if ( and (newOption, Option.SPAWNER))
                {
                    descList.add(Resources.MODE_SPAWNER);
                }

                if ( and (newOption, Option.SPAWN_AREA))
                {
                    descList.add(Resources.MODE_SPAWN_AREA);
                }

                if ( and (newOption, Option.SPAWNABLE_POINT))
                {
                    descList.add(Resources.MODE_SPAWNABLE_POINT);
                }

                if ( and (newOption, Option.UNSPAWNABLE_POINT))
                {
                    descList.add(Resources.MODE_UNSPAWNABLE_POINT);
                }

                if ( and (newOption, Option.DUPLICATION_AREA))
                {
                    descList.add(Resources.MODE_DUPLICATION_AREA);
                }

                if ( and (newOption, Option.ACTIVATE_AREA))
                {
                    descList.add(Resources.MODE_ACTIVATE_AREA);
                }
            }
            else
            {
                descList.add(Resources.MODE_DISABLED);
            }

            settings.setOptionSV(newOption);
        }
        @Override
        public String toString()
        {
            return MODE_ID_SV;
        }
    },
    ;

    public static void initializeModes(Settings settings)
    {
        for (Mode m : values())
        {
            m.initialize(settings);
        }
    }

    public static boolean and (int b1, int b2)
    {
        return (b1 & b2) != 0;
    }

    private int[] options = null;
    protected final List<String> descList = new LinkedList<String>();
    protected Settings settings;
    protected int index = 0;

    protected abstract String getOptionSetSetting();

    protected abstract int getOptionMask();

    protected abstract int getOptionSetting();

    protected abstract void updateOption(int newOption);

    public int[] getAllOptions()
    {
        assert options != null;
        return options.clone();
    }

    public int getOption()
    {
        assert options != null;
        assert index >= 0;
        assert index < options.length;
        return options[index];
    }

    public boolean hasOption(int option)
    {
        return and (getOption(), option);
    }

    public boolean isInvalidOption()
    {
        return (getOption() & getOptionMask()) > 0;
    }

    public boolean toUpperOption()
    {
        assert settings != null;
        assert options != null;

        if (index >= options.length - 1)
        {
            return false;
        }

        index++;

        if (isInvalidOption())
        {
            if (toUpperOption())
            {
                return true;
            }

            index--;
            return false;
        }

        descList.clear();
        updateOption(getOption());
        SpawnChecker.mod.debug("option changed: ", options[index - 1], " -> ", options[index]);
        return true;
    }

    public boolean toLowerOption()
    {
        assert settings != null;

        if (index <= 0)
        {
            return false;
        }

        index--;

        if (isInvalidOption())
        {
            if (toLowerOption())
            {
                return true;
            }

            index++;
            return false;
        }

        descList.clear();
        updateOption(getOption());
        SpawnChecker.mod.debug("option changed: ", options[index + 1], " -> ", options[index]);
        return true;
    }

    public List<String> getDescriptionList()
    {
        return descList;
    }

    private void initialize(Settings settings)
    {
        assert settings != null;

        if (this.settings != null)
        {
            return;
        }

        this.settings = settings;
        initializeModeOptions();
        int op = getOptionSetting();

        for (int i = 0; i < options.length; i++)
        {
            if (options[i] == op)
            {
                index = i;
                break;
            }
        }

        updateOption(getOption());
    }

    private void initializeModeOptions()
    {
        String[] a = getOptionSetSetting().split(",");
        int size = a.length;
        int i = 0;

        try
        {
            int[] options = new int[size];

            for (; i < size; i++)
            {
                options[i] = Integer.parseInt(a[i].trim());
                SpawnChecker.mod.debug("add options: mode=", name(), " option=", options[i]);
            }

            this.options = options;
        }
        catch (NumberFormatException e)
        {
            SpawnChecker.mod.error(e, "Invalid option set value. parse failed value=", a[i], " all values=",
                    getOptionSetSetting());
            throw e;
        }
    }

    /**
     * オプション定数.
     */
    public static class Option
    {
        public static final int DISABLE = 0;
        public static final int MARKER = 1;
        public static final int GUIDELINE = 2;
        public static final int FORCE = 4;
        public static final int SLIME = 8;
        public static final int GHAST = 16;

        public static final int CHUNK_MARKER = 4;

        public static final int SPAWNER = 1;
        public static final int SPAWN_AREA = 2;
        public static final int SPAWNABLE_POINT = 4;
        public static final int UNSPAWNABLE_POINT = 8;
        public static final int DUPLICATION_AREA = 16;
        public static final int ACTIVATE_AREA = 32;

        private Option()
        {
        }
    }
}
