package de.craftery.util;

import org.bukkit.inventory.ItemStack;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ItemStackConverter implements AttributeConverter<ItemStack, byte[]> {

    @Override
    public byte[] convertToDatabaseColumn(ItemStack itemStack) {
        return TestMocks.serializeItemStack(itemStack);
    }

    @Override
    public ItemStack convertToEntityAttribute(byte[] bytes) {
        return TestMocks.deserializeItemStack(bytes);
    }

}