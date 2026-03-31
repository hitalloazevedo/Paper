package io.papermc.generator.utils;

import com.google.common.base.CaseFormat;
import com.google.common.collect.ImmutableMap;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jspecify.annotations.NullMarked;

// Value Object
final class Classname {
    public final String value;

    Classname(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Classname other)) return false;
        return value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}

// Value Object
final class ImplementationName {
    public final String value;

    ImplementationName(String value) {
        this.value = value;
    }
}

@NullMarked
public final class BlockEntityMapping {

    private static final String PREFIX = "Craft";

    private static final Map<Classname, ImplementationName> RENAMES =
        ImmutableMap.<Classname, ImplementationName>builder()
            .put(new Classname("CraftFurnace"), new ImplementationName("CraftFurnaceFurnace"))
            .put(new Classname("CraftMobSpawner"), new ImplementationName("CraftCreatureSpawner"))
            .put(new Classname("CraftPiston"), new ImplementationName("CraftMovingPiston"))
            .put(new Classname("CraftTrappedChest"), new ImplementationName("CraftChest"))
            .buildOrThrow();

    public static final Map<ResourceKey<BlockEntityType<?>>, String> MAPPING;

    static {
        Map<ResourceKey<BlockEntityType<?>>, String> mapping = new IdentityHashMap<>();

        BuiltInRegistries.BLOCK_ENTITY_TYPE.registryKeySet().forEach(key -> {

            String name = CaseFormat.LOWER_UNDERSCORE.to(
                CaseFormat.UPPER_CAMEL,
                key.identifier().getPath()
            );

            String implName = PREFIX + name;

            Classname keyName = new Classname(implName);

            ImplementationName resolved = RENAMES.getOrDefault(
                keyName,
                new ImplementationName(implName)
            );

            mapping.put(key, resolved.value);
        });

        MAPPING = Collections.unmodifiableMap(mapping);
    }
}