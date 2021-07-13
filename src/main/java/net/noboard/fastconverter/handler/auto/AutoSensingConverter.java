package net.noboard.fastconverter.handler.auto;

import net.noboard.fastconverter.*;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 自动感知转换器
 *
 * 通过识别数据和转换结果的类型，来自动执行一个转换过程
 *
 * @date 2020/11/11 9:22 下午
 * @author by wanxm
 */
public class AutoSensingConverter extends AbstractConverterHandler<Object, Object> {

    private static ConverterFilter converterFilter;

    static {
        converterFilter = new AbstractConverterFilter() {
            @Override
            protected void initConverters(List<Converter> converters) {
                converters.add(new DateToLongSensingConverter());
                converters.add(new LongToDateSensingConverter());
                converters.add(new StringToEnumSensingConverter());
                converters.add(new EnumToStringSensingConverter());
            }
        };
    }

    @Override
    protected Object converting(Object value, String tip) throws ConvertException {
       return converterFilter.filter(value, tip).convert(value, tip);
    }

    @Override
    public boolean supports(Object value, String tip) {
        return converterFilter.filter(value, tip) != null;
    }
}
