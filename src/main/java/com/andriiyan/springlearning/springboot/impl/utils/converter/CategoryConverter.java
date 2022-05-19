package com.andriiyan.springlearning.springboot.impl.utils.converter;

import com.andriiyan.springlearning.springboot.api.model.Ticket;

import javax.persistence.AttributeConverter;

/**
 * Custom database converter for the Ticket.Category.
 */
public class CategoryConverter implements AttributeConverter<Ticket.Category, String> {
    @Override
    public String convertToDatabaseColumn(Ticket.Category category) {
        return category.name();
    }

    @Override
    public Ticket.Category convertToEntityAttribute(String s) {
        return Ticket.Category.valueOf(s);
    }
}
