package net.noboard.fastconverter;

import net.noboard.fastconverter.handler.bean.ConvertInfo;

import javax.validation.constraints.NotNull;

/**
 * 转换器过滤器
 *
 * @author wanxm
 */
public interface ConverterFilter {
    @NotNull
    ConverterFacade filter(Object value, ConvertInfo convertInfo);
}
