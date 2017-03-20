SpawnChecker2
================================================================================================

What is this?
---------------------------------------------------------------------------
[Minecraft](http://minecraft.net/) mod. Drawing monster spawn points, slime chunks, and the MobSpawner informations.

モンスターがスポーンする場所、スライムチャンク、モンスタースポーナーの情報を画面内に表示する、[マインクラフト](http://minecraft.net/)のMODです。

Installation
---------------------------------------------------------------------------

### Requirements
* [Minecraft](https://minecraft.net)
* [Minecraft Forge](http://minecraftforge.net/) [(downloads)](http://files.minecraftforge.net/)

### Installation
* put in the mods folder!

Controll
---------------------------------------------------------------------------
key: `UP`, `DOWN`, `RIGHT`, `LEFT`, `Num Add(+)`, `Num Sub(-)`, and modifier keys(CTRL/SHIFT/ALT)

- `ctrl` + `up`/`down`: change mode
- `up`/`down`: change mode option
- `num +` or `right`/`num -` or `left`: change horizontal scan range
- `shift`/`alt` + `num +` or `right`/`num -` or `left`: change vertical scan range
- `ctrl` + `num +` or `right`/`num -` or `left`: change marker brightness

- click mob spawner block with bare hands: start spawner visualizer mode 

Downloads
---------------------------------------------------------------------------
* [Downloads](http://goo.gl/Hkp2d)

For Modder
---------------------------------------------------------------------------

cloneしてsetupしてbuildタスクを叩けば、多分jarは生成されます。

Modの開発環境から参照したい場合、mavenリポジトリを追加することで参照できます。

	repositories {
	    mavenCentral()
	    maven {
	        name = "awairo"
	        url = "http://maven.awairo.net/"
	    }
	}
	
	dependencies {
	    // 2.0.x.xxx:dev 形式で明確なバージョンを指定もできます
	    compile 'net.awairo.mcmod:SpawnChecker:2.0-SNAPSHOT:dev'
	}


SpawnChecker2は、他のModから任意のモードを追加することができます。
リファレンスなどは作成していないので、presetmodeの実装を参照して下さい。
バージョンアップでもなるべく互換性は保ちますが、保証はできません。

PRは受け付けますが、マージするかは内容を見て判断します。

