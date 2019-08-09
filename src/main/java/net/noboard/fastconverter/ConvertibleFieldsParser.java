package net.noboard.fastconverter;

import net.noboard.fastconverter.handler.base.PipelineConverterHandler;
import net.noboard.fastconverter.handler.support.ConvertibleUtils;

import java.util.LinkedHashSet;

public class ConvertibleFieldsParser extends AbstractConvertibleParser {
    @Override
    public ConvertibleMap parse() {
        LinkedHashSet<ConvertibleField> linkedHashSet = ConvertibleUtils.getMergedConvertField(super.annotatedElement, super.group);
        PipelineConverterHandler pipelineConverterHandler = new PipelineConverterHandler();
        CMap cMap = new CMap();
        try {
            for (ConvertibleField convertibleField : linkedHashSet) {
                Converter converter = convertibleField.converter().newInstance();
                converter.setDefaultTip(convertibleField.tip());
                pipelineConverterHandler.pushConverter(converter);
            }
            cMap.setConverter(pipelineConverterHandler);
            cMap.setGroup(group);
        }catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return cMap;
    }
}
