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

package net.awairo.mcmod.spawnchecker.presetmode.spawnervisualizer;

import static net.awairo.mcmod.spawnchecker.presetmode.Options.*;

import java.awt.Color;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.init.Blocks;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;

import net.awairo.mcmod.spawnchecker.SpawnChecker;
import net.awairo.mcmod.spawnchecker.client.common.Refrection;
import net.awairo.mcmod.spawnchecker.client.marker.CachedSupplier;
import net.awairo.mcmod.spawnchecker.client.mode.ConditionalMode;
import net.awairo.mcmod.spawnchecker.presetmode.SkeletalPresetMode;
import net.awairo.mcmod.spawnchecker.presetmode.spawncheck.spawner.MobSpawnerSpawnableCheck;

/**
 * スポーナー可視化モード.
 * 
 * @author alalwww
 */
public class SpawnerVisualizerMode extends SkeletalPresetMode<SpawnerVisualizerMode> implements ConditionalMode
{
    /** mode id. */
    public static final String ID = "spawnervisualizer";

    private static final Random RANDOM = new Random();
    private static final Logger LOG = LogManager.getLogger(SpawnChecker.MOD_ID);

    private final SpawnerVisualizerModeConfig config;

    private MobSpawnerBlockMarker blockMarker;
    private MobSpawnerSpawnAreaMarker spawnAreaMarker;
    private MobSpawnerSpawnLimitAreaMarker spawnLimitAreaMarker;
    private MobSpawnerActivateAreaMarker activateAreaMarker;

    private int[] inherents;
    private List<MobSpawnerPointMerker> pointMarkers;
    private CachedSupplier<MobSpawnerPointMerker> pointMarkerSupplier;

    private MobSpawnerSpawnableCheck spawnableCheck;

    private boolean scheduleResetSpawnerPosition;

    private boolean started;
    private boolean clicking;

    private TileEntityMobSpawner foundSpawner;
    private MobSpawnerBaseLogic foundSpawnerLogic;
    private int foundSpawnerX;
    private int foundSpawnerY;
    private int foundSpawnerZ;

    private int computedBrightness;
    private boolean hidden = true;
    private boolean spawnArea;
    private boolean spawnLimitArea;
    private boolean spawnable;
    private boolean unspawnable;
    private boolean activateArea;

    /**
     * Constructor.
     * 
     * @param config 設定
     */
    public SpawnerVisualizerMode(SpawnerVisualizerModeConfig config)
    {
        super(ID);
        this.config = config;
    }

    @Override
    protected String modeNameKey()
    {
        return "spawnchecker.mode.spawner_visualizer";
    }

    @Override
    public String iconResourceName()
    {
        return "spawnchecker:icon/spawner_visualizer.png";
    }

    @Override
    public int priority()
    {
        return 10;
    }

    @Override
    public int compareTo(ConditionalMode o)
    {
        return ConditionalMode.Comparator.compare(this, o);
    }

    @Override
    protected SpawnerVisualizerModeConfig config()
    {
        return config;
    }

    @Override
    protected void onStart()
    {
        started = true;

        blockMarker = new MobSpawnerBlockMarker(config());
        spawnAreaMarker = new MobSpawnerSpawnAreaMarker(config());
        spawnLimitAreaMarker = new MobSpawnerSpawnLimitAreaMarker(config());
        activateAreaMarker = new MobSpawnerActivateAreaMarker(config());

        // スポーン判定を行う範囲 x:8block, y:3block, z:8block
        // TODO： スポーナーのTileEntityのもつロジックから参照したほうがよさそう
        final int size = 8 * 3 * 8;
        inherents = new int[size];
        for (int i = 0; i < inherents.length; i++)
            inherents[i] = RANDOM.nextInt(1024);
        pointMarkers = Lists.newArrayListWithCapacity(size);
        pointMarkerSupplier = CachedSupplier.of(size, MobSpawnerPointMerker.supplier(config()));
    }

    @Override
    protected void onStop()
    {
        started = false;

        foundSpawner = null;
        foundSpawnerLogic = null;

        blockMarker = null;
        spawnAreaMarker = null;
        spawnLimitAreaMarker = null;
        activateAreaMarker = null;

        inherents = null;
        pointMarkers = null;
        pointMarkerSupplier = null;

        spawnableCheck = null;

        hidden = true;

        spawnArea = false;
        spawnLimitArea = false;
        spawnable = false;
        unspawnable = false;
        activateArea = false;
    }

    @Override
    public boolean enabled()
    {
        // 起動中ならスポーナーがなくなってないかチェック
        if (started && !isSpawnerAt(foundSpawnerX, foundSpawnerY, foundSpawnerZ))
            return false;

        if (isMouseClicked())
        {
            // クリックをやめるまでに再度スポーナーをクリックしたか判定すると、ON/OFFが何度も切り替わってしまうので
            // クリックが始まった直後の初回のみクリックされた対象をチェックし、
            // 以降はクリックが中断されるまでクリック対象が何か判定しない
            if (!clicking && game.objectMouseOver != null)
            {
                clicking = true;

                // 素手じゃない場合はトグらない
                if (isPlayerHasItem())
                    return started;

                // 叩いたのがスポーナーか判断
                final MovingObjectPosition mop = game.objectMouseOver;
                final BlockPos pos = mop.func_178782_a();

                // 開始位置での素振りはスポーナーなくなってるのでトグる
                boolean same = foundSpawnerX == pos.getX();
                same = same && foundSpawnerY == pos.getY();
                same = same && foundSpawnerZ == pos.getZ();
                if (same && started)
                    return false;

                if (!isTargetedOnSpawner(mop))
                    // スポーナーじゃないのでトグらない
                    return started;

                foundSpawner = getSpawnerTileEntity(pos.getX(), pos.getY(), pos.getZ());
                if (foundSpawner == null)
                    // TileEntityが想定外のタイプだった。ブロック座標を直前で確認しているので、発生しないはずだが、念のためチェック
                    return false;

                final MobSpawnerBaseLogic logic = foundSpawner.getSpawnerBaseLogic();
                if (foundSpawnerLogic == logic)
                    // 既に同じ参照のロジックを参照しているなら変更されていないので再設定しなくて大丈夫
                    return true;

                // めっけたスポーナーの座標は覚えとく
                foundSpawnerLogic = logic;

                scheduleResetSpawnerPosition = true;
                foundSpawnerX = pos.getX();
                foundSpawnerY = pos.getY();
                foundSpawnerZ = pos.getZ();

                LOG.info("found mob spawner(x,y,z): {}, {}, {}\n",
                        foundSpawnerX, foundSpawnerY, foundSpawnerZ);

                return true;
            }
        }
        else
        {
            // クリックされていなければクリック中フラグ倒す
            clicking = false;
        }

        // クリック中の2回目以降の判定か、クリックされていない
        return started && isPlayerCloseToTheFoundSpawner();
    }

    private boolean isMouseClicked()
    {
        // 右クリ左クリどっちでもおｋ、途中で左右クリックをに入れ替えるような奴はおらんやろｗ
        return game.gameSettings.keyBindAttack.getIsKeyPressed()
                || game.gameSettings.keyBindUseItem.getIsKeyPressed();
    }

    private boolean isPlayerCloseToTheFoundSpawner()
    {
        final double center = 0.5d;
        double a = Math.pow(foundSpawnerX + center - game.thePlayer.posX, 2);
        a += Math.pow(foundSpawnerY + center - game.thePlayer.posY, 2);
        a += Math.pow(foundSpawnerZ + center - game.thePlayer.posZ, 2);

        // TODO: 上限距離の定数化
        return Math.sqrt(a) < 24.0d;
    }

    private boolean isPlayerHasItem()
    {
        return game.thePlayer.inventory.getCurrentItem() != null;
    }

    private boolean isTargetedOnSpawner(MovingObjectPosition mop)
    {
        if (mop == null || mop.typeOfHit != MovingObjectType.BLOCK)
            return false;

        final BlockPos pos = mop.func_178782_a();
        return isSpawnerAt(pos.getX(), pos.getY(), pos.getZ());
    }

    private boolean isSpawnerAt(int x, int y, int z)
    {
        return game.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.mob_spawner;
    }

    @Override
    public void onPlusKeyPress(boolean ctrl, boolean shift, boolean alt)
    {
        // 範囲はないので明るさ変更の操作以外を潰す
        if (ctrl)
            super.onPlusKeyPress(ctrl, shift, alt);
    }

    @Override
    public void onMinusKeyPress(boolean ctrl, boolean shift, boolean alt)
    {
        // 範囲はないので明るさ変更の操作以外を潰す
        if (ctrl)
            super.onMinusKeyPress(ctrl, shift, alt);
    }

    @Override
    protected void onUpdate()
    {
        pointMarkers.clear();

        // スポーナーの位置変わってたら再設定
        if (scheduleResetSpawnerPosition)
        {
            scheduleResetSpawnerPosition = false;

            blockMarker
                    .setPoint(foundSpawnerX, foundSpawnerY, foundSpawnerZ);
            spawnAreaMarker
                    .setPoint(foundSpawnerX, foundSpawnerY, foundSpawnerZ);
            spawnLimitAreaMarker
                    .setPoint(foundSpawnerX, foundSpawnerY, foundSpawnerZ);
            activateAreaMarker
                    .setPoint(foundSpawnerX, foundSpawnerY, foundSpawnerZ);

            spawnableCheck = MobSpawnerSpawnableCheck.EntityMap
                    .newInstanceOf(Refrection.getEntityNameToSpawnFrom(foundSpawnerLogic));
        }

        hidden = options().contains(SPAWNER_HIDDEN);
        if (hidden)
            return;

        spawnArea = options().contains(SPAWNER_SPAWN_AREA);
        spawnLimitArea = options().contains(SPAWNER_SPAWN_LIMIT_AREA);
        spawnable = options().contains(SPAWNER_SPAWNABLE_POINT);
        unspawnable = options().contains(SPAWNER_UNSPAWNABLE_POINT);
        activateArea = options().contains(SPAWNER_ACTIVATE_AREA);

        computedBrightness = commonState().computedBrightness();

        blockMarker.setColorAndBrightness(commonColor().spawnerOutline(), computedBrightness);
        spawnAreaMarker.setColorAndBrightness(commonColor().spawnerSpawnArea(), computedBrightness);
        spawnLimitAreaMarker.setColorAndBrightness(commonColor().spawnerSpawnLimitArea(), computedBrightness);

        activateAreaMarker
                .setFillColor(commonColor().spawnerActiveArea())
                .setLineColor(commonColor().spawnerActiveAreaLine())
                .setBrightness(computedBrightness);

        checkSpawnable(computedBrightness);
    }

    private void checkSpawnable(int brightness)
    {
        if (!spawnable && unspawnable)
            return;

        // TODO: スポーン範囲などはタイルエンティティのもつロジックの情報から得るべきっぽい
        final int maxX = foundSpawnerX + 4;
        final int maxY = foundSpawnerY + 2;
        final int maxZ = foundSpawnerZ + 4;

        int inherentIndex = 0;
        for (int posX = foundSpawnerX - 4; posX < maxX; posX++)
            for (int posY = foundSpawnerY - 1; posY < maxY; posY++)
                for (int posZ = foundSpawnerZ - 4; posZ < maxZ; posZ++)
                    checkSpawnable(posX, posY, posZ, inherentIndex++);
    }

    private void checkSpawnable(int posX, int posY, int posZ, int inherentIndex)
    {
        // スポーン可否の判定に対応していないスポーナーの場合
        if (!spawnableCheck.supported())
        {
            // TODO: 対応していない事を表示できるマーカーを追加する
            //            pointMarkers.add(pointMarkerSupplier.get()
            //                    .setPoint(posX, posY, posZ)
            //                    .setColorAndBrightness(color, computedBrightness)
            //                    .setInherent(inherents[inherentIndex]));
            return;
        }

        final Color color;
        if (spawnableCheck.isSpawnable(posX, posY, posZ))
        {
            if (!spawnable) return;
            color = commonColor().spawnerSpawnablePoint();
        }
        else
        {
            if (!unspawnable) return;
            color = commonColor().spawnerUnspawnablePoint();
        }

        pointMarkers.add(pointMarkerSupplier.get()
                .setPoint(posX, posY, posZ)
                .setColorAndBrightness(color, computedBrightness)
                .setInherent(inherents[inherentIndex]));
    }

    private TileEntityMobSpawner getSpawnerTileEntity(int x, int y, int z)
    {
        final TileEntity te = game.theWorld.getTileEntity(new BlockPos(x, y, z));

        if (te instanceof TileEntityMobSpawner)
            return (TileEntityMobSpawner) te;

        return null;
    }

    @Override
    public void renderIngame(long tickCounts, float partialTicks)
    {
        blockMarker.doRender(tickCounts, partialTicks);

        if (hidden)
            return;

        if (spawnArea)
            spawnAreaMarker.doRender(tickCounts, partialTicks);

        if (spawnLimitArea)
            spawnLimitAreaMarker.doRender(tickCounts, partialTicks);

        if (activateArea)
            activateAreaMarker.doRender(tickCounts, partialTicks);

        if (!pointMarkers.isEmpty())
            for (MobSpawnerPointMerker marker : pointMarkers)
                marker.doRender(tickCounts, partialTicks);
    }

}
