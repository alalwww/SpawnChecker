package spawnchecker.enums;

import static spawnchecker.constants.Constants.DIMENSION_NETHER;
import static spawnchecker.constants.Constants.DIMENSION_SURFACE;
import static spawnchecker.constants.Constants.DIMENSION_THEEND;

/**
 * ディメンションの列挙.
 *
 * 数値フラグで switch はなんかいやなん…。
 *
 * @author takuru/ale
 */
public enum Dimension
{
    SURFACE(DIMENSION_SURFACE),
    NETHER(DIMENSION_NETHER),
    THE_END(DIMENSION_THEEND),
    UNKNOWN(Integer.MIN_VALUE);

    private int dimentionId;

    /**
     * Constructor.
     */
    private Dimension(int dimentionId)
    {
        this.dimentionId = dimentionId;
    }

    public int getDimentionId()
    {
        return dimentionId;
    }

    public static Dimension getDimensionById(int dimentionId)
    {
        for (Dimension d : values())
        {
            if (d.getDimentionId() == dimentionId)
            {
                return d;
            }
        }

        return UNKNOWN;
    }

    @Override
    public String toString()
    {
        return name() + " (id=" + dimentionId + ")";
    }
}
