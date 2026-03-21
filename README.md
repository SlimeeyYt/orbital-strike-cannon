# OrbitalRod

`OrbitalRod` is a Fabric mod for **Minecraft 1.21.11** on **Java 21** by **Slimeey Studios**.

## Dependencies
- `fabric-loader` (required)
- `fabric-api` (required)

This project currently targets:
- `net.fabricmc:fabric-loader:0.18.4`
- `net.fabricmc.fabric-api:fabric-api:0.139.4+1.21.11`

## Features
- `Orbital TNT Rod` custom item
- Always-enchanted glint on the rod
- Orbital strike with **3 large circumferences** plus a **straight center stack**
- 9-second cooldown per successful use
- 1 durability consumed per successful use
- Anvil repair supported with `minecraft:nether_star`
- Recipe unlock via the **Orbital Strike** advancement / recipe book reward

## Crafting Recipe
Arrange items in a crafting table as:

```text
[ Nether Star ] [   TNT   ] [ Nether Star ]
[    TNT     ] [Fishing Rod] [    TNT     ]
[    (air)   ] [ Nether Star] [   (air)   ]
```

- `N` = `minecraft:nether_star`
- `T` = `minecraft:tnt`
- `R` = `minecraft:fishing_rod`

## Advancement
- **Orbital Strike**: obtain a Nether Star to unlock the Orbital TNT Rod recipe.

## Development Setup
```powershell
cd "D:\Slimeey Studios\orbital-strike"
$env:JAVA_HOME = "C:\Path\To\JDK-21"
.\gradlew.bat build
.\gradlew.bat configureLaunch configureClientLaunch
```

## Run Minecraft Client (dev)
```powershell
cd "D:\Slimeey Studios\orbital-strike"
$env:JAVA_HOME = "C:\Path\To\JDK-21"
.\gradlew.bat runClient
```

## Run Minecraft Server (dev)
```powershell
cd "D:\Slimeey Studios\orbital-strike"
$env:JAVA_HOME = "C:\Path\To\JDK-21"
Set-Content -Path ".\run\server\eula.txt" -Value "eula=true"
.\gradlew.bat runServer
```
