# OrbitalRod

`OrbitalRod` is a Fabric mod for **Minecraft 1.20.1** on **Java 17**.

## Included content
- `Orbital TNT Rod` custom item
- Block raycast up to 100 blocks on right-click
- ~1056 primed TNT entities spawned in stacked circular rings
- Server-side strike logic
- Example crafting recipe

## Notes
- The item falls back to normal `FishingRodItem` behavior when you are not targeting a block.
- The orbital strike is intentionally extreme and may lag or crash weaker servers/worlds.

## Testing environment setup
```powershell
cd "D:\Slimeey Studios\orbital-strike"
$env:JAVA_HOME = "C:\Users\naman\.jdks\temurin-17.0.18"
.\gradlew.bat build
.\gradlew.bat configureLaunch configureClientLaunch
```

## Run Minecraft client (dev)
```powershell
cd "D:\Slimeey Studios\orbital-strike"
$env:JAVA_HOME = "C:\Users\naman\.jdks\temurin-17.0.18"
.\gradlew.bat runClient
```

## Run Minecraft server (dev)
```powershell
cd "D:\Slimeey Studios\orbital-strike"
$env:JAVA_HOME = "C:\Users\naman\.jdks\temurin-17.0.18"
Set-Content -Path ".\run\server\eula.txt" -Value "eula=true"
.\gradlew.bat runServer
```
