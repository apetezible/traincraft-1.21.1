# Traincraft 1.21.1

A Fabric port of Traincraft for Minecraft 1.21.1.

## Current Status

The project currently has a clean Fabric 1.21.1 foundation and builds successfully. The legacy Traincraft implementation in the companion 1.7.10 repository is being used as a reference for gameplay, rolling stock, models, textures, recipes, and sounds.

The new mod ID is `traincraft`. The legacy mod used `tc`; compatibility with legacy world data and identifiers will be considered as the port gains persistent content.

## Development

Requirements:

- Minecraft 1.21.1
- Java 21
- Fabric 0.102.0+1.21.1
- Fabric API 0.116.7+1.21.1

Build the mod with:

```text
./gradlew build
```

On Windows:

```text
gradlew.bat build
```

## Locomotive Milestone

The first gameplay milestone is one working 4-4-0 steam locomotive:

1. Register a locomotive entity and its spawn item.
2. Add server-authoritative rail movement and basic rider controls.
3. Persist locomotive state, including fuel, water, and owner/settings data.
4. Add the client renderer, converted model, and first livery.
5. Add a recipe, translation, sound, and smoke effects.
6. Test spawning, riding, movement, saving/loading, and client/server synchronization.

After this vertical slice works, the shared rolling-stock architecture can support tenders, passenger cars, freight cars, coupling, and additional locomotives without copying the legacy implementation directly.

## License

See [LICENSE](LICENSE) for the current project license. Legacy Traincraft code and assets retain the licensing and attribution requirements of the original project.
