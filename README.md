# AnywhereWeather

A small Paper/Spigot plugin (Java source) that does two things:

1. **Channeling works in any weather** — normally a Channeling trident only strikes
   lightning when the world is thundering. This plugin manually strikes lightning
   on trident impact whenever the item has Channeling, even in clear/rainy weather
   (it steps aside if it's already thundering, so vanilla can handle that case itself
   and you don't get double lightning).

2. **Riptide works anywhere** — normally Riptide only launches you when you're wet
   (in water or rain); otherwise right-clicking just throws the trident. This plugin
   intercepts that right-click and always performs the launch instead, regardless of
   location or weather. Launch speed scales with the Riptide enchant level.

This is source code, not a compiled `.jar` — I don't have internet/Maven access in
this environment to actually build and test it, so **you'll need to compile it
yourself** (or send it back to me if you'd rather I try compiling in a different
environment). Steps below.

## How to build

Requires Java 17+ and Maven installed on your machine.

```bash
cd AnywhereWeather
mvn package
```

The compiled plugin will be at `target/AnywhereWeather.jar`.

**Important:** the `pom.xml` currently targets Paper API version `1.21.4-R0.1-SNAPSHOT`.
If your server runs a different Minecraft version, open `pom.xml` and change that
version string to match (e.g. `1.20.4-R0.1-SNAPSHOT`), otherwise it may fail to build
or fail to load on your server.

## How to install

1. Drop `AnywhereWeather.jar` into your server's `plugins/` folder.
2. Restart (or `/reload`, though a full restart is safer).
3. That's it — no config needed. Both effects are on by default.

## Permissions (optional)

- `anywhereweather.channeling` — allows the any-weather Channeling effect (default: everyone)
- `anywhereweather.riptide` — allows the anywhere Riptide launch (default: everyone)

If you want to restrict either feature to certain players/ranks, set these to `false`
in your permissions plugin's default group and grant them individually instead.

## Known limitations / things worth testing

- The Riptide launch is a simplified re-implementation (a velocity boost + effects),
  not a byte-for-byte copy of vanilla's riptide physics, since that's baked into
  Minecraft's internal code and not something Bukkit exposes directly. It should feel
  close, but you may want to tune the speed formula in `RiptideListener.java`
  (`1.1 + riptideLevel * 0.6`) to taste.
- Fall damage handling after a riptide launch is a basic approximation
  (`setFallDistance(0)` at launch), not a full duration-based immunity like vanilla.
- Test both features on your actual server version before relying on them — enchant
  API names have been stable for a while but always worth double-checking.

## Files

```
AnywhereWeather/
  pom.xml
  src/main/java/com/kyle/anywhereweather/
    AnywhereWeather.java      (main plugin class)
    ChannelingListener.java   (any-weather Channeling)
    RiptideListener.java      (anywhere Riptide)
  src/main/resources/
    plugin.yml
```
