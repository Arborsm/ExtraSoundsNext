package dev.arbor.extrasoundsnext.annotation;

import dev.arbor.extrasoundsnext.ExtraSoundsNext;
import dev.arbor.extrasoundsnext.mapping.DefaultAutoGenerator;
import dev.arbor.extrasoundsnext.mapping.SoundDefinition;
import dev.arbor.extrasoundsnext.mapping.SoundGenerator;
//? if fabric {
/*import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
*///?} elif neoforge {
import com.mojang.text2speech.Narrator;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforgespi.language.IModInfo;
import net.neoforged.neoforgespi.language.ModFileScanData;
import org.objectweb.asm.Type;
//?} elif forge {
/*import com.mojang.text2speech.Narrator;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.forgespi.language.ModFileScanData;
import org.objectweb.asm.Type;
*///?}
import net.minecraft.world.item.Item;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;

public class AddonFinder {
    protected static List<Field> cache = null;
    private static final Map<String, Class<?>> CLASS_CACHE = new HashMap<>();

    public static List<Field> getSoundsGenerators() {
        if (cache == null) {
            cache = getAnnotatedFields();
        }
        return cache;
    }

    public static Class<?> loadClass(String className) throws ClassNotFoundException {
        Class<?> clazz = CLASS_CACHE.get(className);
        if (clazz == null) {
            clazz = Class.forName(className);
            CLASS_CACHE.put(className, clazz);
        }
        return clazz;
    }

    //? if fabric {
    /*private static List<Field> getAnnotatedFields() {
		List<EntrypointContainer<ISoundsGenerator>> entrypointContainerList = FabricLoader.getInstance().getEntrypointContainers("sounds_generator", ISoundsGenerator.class);
		Set<Field> annotatedFields = new LinkedHashSet<>();

		for (EntrypointContainer<ISoundsGenerator> entrypointContainer : entrypointContainerList) {
			var entrypoint = entrypointContainer.getEntrypoint();
			for (Field field : entrypoint.getClass().getDeclaredFields()) {
				if (field.isAnnotationPresent(SoundsGenerator.class)) {
					if (!field.canAccess(null)) {
						field.setAccessible(true);
					}
					annotatedFields.add(field);
				}
			}
		}
		return new ArrayList<>(annotatedFields);
    }

    public static List<SoundGenerator> getSoundGenerators() {
		List<SoundGenerator> list = new ArrayList<>();
		List<String> namespaces = new ArrayList<>();
		Map<String, Function<Item, SoundDefinition>> soundGenerators = new HashMap<>();
		var fields = AddonFinder.getSoundsGenerators();
		if (fields.isEmpty()) throw new RuntimeException("No sound generators found!");
		fields.forEach(field -> {
			try {
				if (field.getType().equals(SoundGenerator.class)) {
					SoundGenerator soundGenerator = (SoundGenerator) field.get(null);
					soundGenerators.put(soundGenerator.namespace, soundGenerator.itemSoundGenerator);
					ExtraSoundsNext.LOGGER.info("Loaded sound generator: {}", soundGenerator.namespace);
				}
			} catch (IllegalAccessException e) {
				ExtraSoundsNext.LOGGER.error("Failed to load field: {}", field.getName(), e);
			}
		});
		FabricLoader.getInstance().getAllMods().forEach(modContainer -> namespaces.add(modContainer.getMetadata().getId()));
		for (String namespace : namespaces) {
			list.add(SoundGenerator.of(namespace, soundGenerators.getOrDefault(namespace, DefaultAutoGenerator::autoGenerator)));
		}
		return list;
    }

    public static List<String> getModList(){
		return FabricLoader.getInstance().getAllMods().stream()
				.map(modContainer -> modContainer.getMetadata().getId())
				.filter(s -> !s.contains("generated"))
				.sorted()
				.toList();
    }
    *///?} neoforge {

    public static List<Field> getAnnotatedFields() {
        Type annotationType = Type.getType(SoundsGenerator.class);
        List<ModFileScanData> allScanData = ModList.get().getAllScanData();
        Set<Field> annotatedFields = new LinkedHashSet<>();

        for (ModFileScanData scanData : allScanData) {
            Iterable<ModFileScanData.AnnotationData> annotations = scanData.getAnnotations();
            for (ModFileScanData.AnnotationData a : annotations) {
                if (annotationType != null && Objects.equals(a.annotationType(), annotationType)) {
                    try {
                        Class<?> asmClass = AddonFinder.loadClass(a.clazz().getClassName());
                        for (Field field : asmClass.getDeclaredFields()) {
                            if (field.isAnnotationPresent(SoundsGenerator.class)) {
                                if (!field.canAccess(null)) {
                                    field.setAccessible(true);
                                }
                                annotatedFields.add(field);
                            }
                        }
                    } catch (ClassNotFoundException e) {
                        ExtraSoundsNext.LOGGER.error("Failed to load class: {}", a.clazz().getClassName(), e);
                        if (!FMLLoader.isProduction()) throw new RuntimeException(e);
                    }
                }
            }
        }
        return new ArrayList<>(annotatedFields);
    }

    public static List<SoundGenerator> getSoundGenerators() {
        List<SoundGenerator> list = new ArrayList<>();
        List<String> namespaces = new ArrayList<>();
        Map<String, Function<Item, SoundDefinition>> soundGenerators = new HashMap<>();
        var fields = AddonFinder.getSoundsGenerators();
        if (fields.isEmpty()) throw new RuntimeException("No sound generators found!");
        fields.forEach(field -> {
            try {
                if (field.getType().equals(SoundGenerator.class)) {
                    SoundGenerator soundGenerator = (SoundGenerator) field.get(null);
                    soundGenerators.put(soundGenerator.namespace, soundGenerator.itemSoundGenerator);
                    ExtraSoundsNext.LOGGER.info("Loaded sound generator: {}", soundGenerator.namespace);
                }
            } catch (IllegalAccessException e) {
                ExtraSoundsNext.LOGGER.error("Failed to load field: {}", field.getName(), e);
                if (!FMLLoader.isProduction()) throw new RuntimeException(e);
            }
        });
        ModList.get().getMods().forEach(iModInfo -> namespaces.add(iModInfo.getModId()));
        for (String namespace : namespaces) {
            list.add(SoundGenerator.of(namespace, soundGenerators.getOrDefault(namespace, DefaultAutoGenerator::autoGenerator)));
        }
        return list;
    }

    public static List<String> getModList() {
        return ModList.get().getMods().stream()
                .map(IModInfo::getModId)
                .filter(s -> !s.contains("generated"))
                .sorted()
                .toList();
    }
    //?} forge {

	/*public static List<Field> getAnnotatedFields() {
		Type annotationType = Type.getType(SoundsGenerator.class);
		List<ModFileScanData> allScanData = ModList.get().getAllScanData();
		Set<Field> annotatedFields = new LinkedHashSet<>();

		for (ModFileScanData scanData : allScanData) {
			Iterable<ModFileScanData.AnnotationData> annotations = scanData.getAnnotations();
			for (ModFileScanData.AnnotationData a : annotations) {
				if (annotationType != null && Objects.equals(a.annotationType(), annotationType)) {
					try {
						Class<?> asmClass = AddonFinder.loadClass(a.clazz().getClassName());
						for (Field field : asmClass.getDeclaredFields()) {
							if (field.isAnnotationPresent(SoundsGenerator.class)) {
								if (!field.canAccess(null)) {
									field.setAccessible(true);
								}
								annotatedFields.add(field);
							}
						}
					} catch (ClassNotFoundException e) {
						ExtraSoundsNext.LOGGER.error("Failed to load class: {}", a.clazz().getClassName(), e);
						if (!FMLLoader.isProduction()) throw new RuntimeException(e);
					}
				}
			}
		}
		return new ArrayList<>(annotatedFields);
	}

	public static List<SoundGenerator> getSoundGenerators() {
		List<SoundGenerator> list = new ArrayList<>();
		List<String> namespaces = new ArrayList<>();
		Map<String, Function<Item, SoundDefinition>> soundGenerators = new HashMap<>();
		var fields = AddonFinder.getSoundsGenerators();
		if (fields.isEmpty()) throw new RuntimeException("No sound generators found!");
		fields.forEach(field -> {
			try {
				if (field.getType().equals(SoundGenerator.class)) {
					SoundGenerator soundGenerator = (SoundGenerator) field.get(null);
					soundGenerators.put(soundGenerator.namespace, soundGenerator.itemSoundGenerator);
					ExtraSoundsNext.LOGGER.info("Loaded sound generator: {}", soundGenerator.namespace);
				}
			} catch (IllegalAccessException e) {
				ExtraSoundsNext.LOGGER.error("Failed to load field: {}", field.getName(), e);
				if (!FMLLoader.isProduction()) throw new RuntimeException(e);
			}
		});
		ModList.get().getMods().forEach(iModInfo -> namespaces.add(iModInfo.getModId()));
		for (String namespace : namespaces) {
			list.add(SoundGenerator.of(namespace, soundGenerators.getOrDefault(namespace, DefaultAutoGenerator::autoGenerator)));
		}
		return list;
	}

    public static List<String> getModList(){
        return ModList.get().getMods().stream()
                .map(IModInfo::getModId)
                .filter(s -> !s.contains("generated"))
                .sorted()
                .toList();
    }
    *///?}
}
