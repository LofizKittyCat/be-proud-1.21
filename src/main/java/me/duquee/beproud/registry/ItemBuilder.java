package me.duquee.beproud.registry;

import me.duquee.beproud.BeProud;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings("unchecked")
public class ItemBuilder<T extends Item, P extends ItemBuilder<T, P>> {

    private T item;

    private final String name;
    private final Item.Settings settings = new Item.Settings();

    private final Function<Item.Settings, T> factory;

    protected ItemBuilder(String name, Function<Item.Settings, T> supplier) {
        this.name = name;
        this.factory = supplier;
    }

    public P settings(Consumer<Item.Settings> settings) {
        settings.accept(this.settings);
        return (P) this;
    }

    public P register() {

        item = factory.apply(settings);
        Registry.register(Registries.ITEM, BeProud.asIdentifier(name), item);

        if (Register.currentGroup != null)
            ItemGroupEvents.modifyEntriesEvent(Register.currentGroup).register(content -> content.add(item));

        return (P) this;
    }

    public T getItem() {
        return item;
    }

}