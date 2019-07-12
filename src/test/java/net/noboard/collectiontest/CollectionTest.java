package net.noboard.collectiontest;

import net.noboard.fastconverter.Converter;
import net.noboard.fastconverter.filter.AbstractConverterFilter;
import net.noboard.fastconverter.handler.BooleanToStringConverterHandler;
import net.noboard.fastconverter.handler.base.ArrayToArrayConverterHandler;
import net.noboard.fastconverter.handler.base.CollectionToCollectionConverterHandler;
import net.noboard.fastconverter.handler.base.MapToMapConverterHandler;
import net.noboard.fastconverter.handler.base.SkippingConverterHandler;

import java.util.*;

public class CollectionTest {


    public static void main(String[] args) {
        Integer[] a = new Integer[]{1,2,null};
        List<Boolean> array = new ArrayList();
        array.add(true);
        array.add(false);
        array.add(null);
        Map map = new HashMap<>();
        map.put("a",1);
        map.put("b",2);
        map.put("c",null);

        ArrayToArrayConverterHandler arrayToArrayConverterHandler = new ArrayToArrayConverterHandler(new AbstractConverterFilter() {
            @Override
            protected void initConverters(List<Converter<?, ?>> converters) {
//                converters.add(new SkippingConverterHandler(SkippingConverterHandler.BASIC_DATA_TYPE));
            }
        });

        CollectionToCollectionConverterHandler collectionToCollectionConverterHandler = new CollectionToCollectionConverterHandler(new AbstractConverterFilter() {
            @Override
            protected void initConverters(List<Converter<?, ?>> converters) {
//                converters.add(new BooleanToStringConverterHandler());
            }
        });

        MapToMapConverterHandler mapToMapConverterHandler = new MapToMapConverterHandler(new AbstractConverterFilter() {
            @Override
            protected void initConverters(List<Converter<?, ?>> converters) {
//                converters.add(new SkippingConverterHandler(SkippingConverterHandler.BASIC_DATA_TYPE));
            }
        });

        Integer[] o = (Integer[]) arrayToArrayConverterHandler.convert(a);
        Object o2 = collectionToCollectionConverterHandler.convert(array);
        Object o3 = mapToMapConverterHandler.convert(map);
        System.out.println("1");
    }
}
