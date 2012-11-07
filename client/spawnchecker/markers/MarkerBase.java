package spawnchecker.markers;

/**
 * marker base model;
 *
 * @author takuru/ale
 *
 */
public abstract class MarkerBase<T extends MarkerBase>
{
    public int x = 0;
    public int y = 0;
    public int z = 0;

    public MarkerBase(int x, int y, int z)
    {
        setPosition(x, y, z);
    }

    public T setPosition(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        return (T) this;
    }
}
