package spawnchecker;

import static spawnchecker.constants.Constants.ENTITY_HEIGHT;
import static spawnchecker.constants.Constants.ENTITY_WIDTH;
import net.minecraft.src.DamageSource;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLightningBolt;
import net.minecraft.src.ModLoader;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;
import net.minecraft.src.mod_SpawnChecker;

/**
 * Entity for rendering class.
 *
 * マーカー等の描画をレンダリングのフェーズで行うために、
 * プレイヤーの近くを追従するだけのエンティティ.
 *
 * 他の追加要素がこのエンティティを処理する際、どのメソッドを使いたいかは判らないため、
 * 外から影響を受けそうな気がするメソッドをいくつかオーバーライドし、デフォルト処理を無効化している。
 *
 * @author takuru/ale
 */
class EntitySpawnChecker extends Entity
{
    private double offsetX;
    private double offsetY;
    private double offsetZ;
    public int tickCount;

    /**
     * Constructor.
     */
    public EntitySpawnChecker(World world)
    {
        super(world);
    }

    @Override
    protected void entityInit()
    {
        // 殆どの場合カメラの表示範囲から見えない場所にエンティティがいる為 true
        ignoreFrustumCheck = true;
        // 小さすぎると描画されなくなるので程々に
        setSize(ENTITY_WIDTH, ENTITY_HEIGHT);
        SpawnChecker.mod.trace("entity create.");
        Settings settings = SpawnChecker.getSettings();
        this.offsetX = settings.getEntityOffsetX();
        this.offsetY = settings.getEntityOffsetY();
        this.offsetZ = settings.getEntityOffsetZ();
        // offsetを設定したので、もっかい位置調整
        resetPosition();
    }

    @Override
    public void onEntityUpdate()
    {
        if (mod_SpawnChecker.DEBUG_MODE)
        {
            if (tickCount % 20 == 0)
            {
                SpawnChecker.mod.trace("entity update.");
            }
        }

        resetPosition();
        tickCount++;

        if (tickCount >= 64800)
        {
            tickCount = 0;
        }
    }

    @Override
    public void setDead()
    {
        // undead
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt)
    {
        // nothing
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt)
    {
        // nothing
    }

    @Override
    public void setFire(int seconds)
    {
        // nothing
    }

    @Override
    public boolean isBurning()
    {
        return false;
    }

    @Override
    public void mountEntity(Entity entity)
    {
        // cannot mounting
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, int damage)
    {
        return false;
    }

    @Override
    public void moveEntity(double x, double y, double z)
    {
        // cannot moving
    }

    @Override
    public void applyEntityCollision(Entity entity)
    {
        // nothing
    }

    @Override
    public float getShadowSize()
    {
        return 0.0f;
    }

    @Override
    public boolean isOffsetPositionInLiquid(double x, double y, double z)
    {
        return false;
    }

    @Override
    public void onStruckByLightning(EntityLightningBolt lb)
    {
        // nothing
    }

    @Override
    public boolean canAttackWithItem()
    {
        return false;
    }

    @Override
    public void setPosition(double x, double y, double z)
    {
        resetPosition();
    }

    @Override
    public void setAngles(float yaw, float pitch)
    {
        // nothing
    }

    @Override
    public int getBrightnessForRender(float partialTickTime)
    {
        // 常に固定値、これで多分ライトマップの明るさMaxになる
        return 0xf00000;
    }

    @Override
    public float getBrightness(float partialTickTime)
    {
        // このエンティティの位置にチャンクが生成されてたら場合、
        // エンティティ位置の明るさをとる処理を行っているようだが
        // エンティティの位置自体に意味がないのでとりあえず0で返してる。
        // レンダーなどで意図的にこのメソッドを呼ばない限りは通常使われないハズ
        return 0.0f;
    }

    private void resetPosition()
    {
        Entity p = ModLoader.getMinecraftInstance().thePlayer;
        // このエンティティは当たり判定対象外であるため、通常は接触判定時に除外されるエンティティのはずだが
        // Modによっては、判定対象かのチェックを行っていないことがあり、ぶつかる判定をされることがあるため、
        // 設定からぶつかるのを回避できるよう、任意位置へと移動できるようにしてある
        super.setPosition(p.posX + offsetX, p.posY + offsetY, p.posZ + offsetZ);
    }

    /** setDead() のかわり. */
    public void destroy()
    {
        isDead = true;
    }
}