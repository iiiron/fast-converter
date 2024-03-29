package net.noboard.fastconverter.handler.container;

import lombok.Data;
import net.noboard.fastconverter.ConverterFilter;
import net.noboard.fastconverter.ConvertInfo;

@Data
public class ContainerConverterContext {

    private ConverterFilter converterFilter;

    private ConvertInfo convertInfo;
}
