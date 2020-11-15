package net.noboard.fastconverter.handler.core.bean;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BeanMapping {
    private Class<?> source;
    private Class<?> target;
}
