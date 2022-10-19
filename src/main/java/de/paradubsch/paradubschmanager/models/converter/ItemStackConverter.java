package de.paradubsch.paradubschmanager.models.converter;

import org.bukkit.inventory.ItemStack;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ItemStackConverter implements AttributeConverter<ItemStack, byte[]> {

    @Override
    public byte[] convertToDatabaseColumn(ItemStack itemStack) {
        return itemStack.serializeAsBytes();
    }

    @Override
    public ItemStack convertToEntityAttribute(byte[] bytes) {
        return ItemStack.deserializeBytes(bytes);
    }

}