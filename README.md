<div align="center">

# Jukebox UI

**Right-click a jukebox to pick any music disc from a menu — no disc in your inventory required. Server-side only.**

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![Minecraft](https://img.shields.io/badge/Minecraft-26.1%20%7C%2026.2-brightgreen.svg)](#)
[![Fabric](https://img.shields.io/badge/Loader-Fabric-blue.svg)](https://fabricmc.net/)

</div>

Right-click a jukebox with an empty hand and a chest-style menu opens listing **every music
disc** in the game. Click one and it plays instantly — you don't need to own the disc. Great
for build servers, lobbies, and jukeboxes you want anyone to use. Entirely **server-side**;
players on an unmodified client need nothing installed.

---

## Features

- **Disc picker menu** — right-click a jukebox (empty-handed) to browse all discs and click to play.
- **No disc needed** — plays the selected track without consuming or requiring an item.
- **Stop playback** — right-click a playing jukebox with an empty hand to stop it.
- **Clean breaks** — a virtually-inserted disc won't pop out as a real item when the jukebox is broken.
- **Vanilla-friendly** — holding a real disc still inserts it the normal way.

---

## Installing (server owners)

1. Download the jar from [Releases](../../releases).
2. Put it in your server's `mods/` folder alongside **[Fabric API](https://modrinth.com/mod/fabric-api)**.
3. Start the server. Connecting players need nothing.

Works on **Minecraft 26.1–26.2** (Fabric).

---

## Usage

- **Open the menu:** right-click a jukebox with an empty hand → click a disc to play it.
- **Stop it:** right-click the playing jukebox with an empty hand.
- **Insert normally:** hold a disc and right-click as usual — vanilla behavior is untouched.

---

## Building from source

Requires **JDK 25** (Minecraft 26.1+).

```bash
git clone https://github.com/VIci0us1/Jukebox-UI.git
cd Jukebox-UI
./gradlew build
```

The jar lands in `build/libs/`. If the Gradle wrapper jar is missing, run `gradle wrapper` once first.

> Minecraft 26.1 was the first **unobfuscated** release, so this uses the `net.fabricmc.fabric-loom`
> toolchain with no mappings. The one jar runs on both 26.1 and 26.2.

---

## License

[MIT](LICENSE)
