package net.noboard.collectiontest;

import net.noboard.fastconverter.Converter;
import net.noboard.fastconverter.filter.AbstractConverterFilter;
import net.noboard.fastconverter.handler.BooleanToStringConverterHandler;
import net.noboard.fastconverter.handler.NumberToBigDecimalConverterHandler;
import net.noboard.fastconverter.handler.core.ArrayToArrayConverterHandler;
import net.noboard.fastconverter.handler.core.CollectionToCollectionConverterHandler;
import net.noboard.fastconverter.handler.core.MapToMapConverterHandler;

import java.util.*;

public class CollectionTest {


    public static void main(String[] args) {
        Integer[] a = new Integer[]{1, 2, null};
        List<Boolean> array = new ArrayList();
        array.add(true);
        array.add(false);
        array.add(null);
        Map map = new HashMap<>();
        map.put("a", null);
        map.put("b", 2);
        map.put("c", 3);

        ArrayToArrayConverterHandler arrayToArrayConverterHandler = new ArrayToArrayConverterHandler(new AbstractConverterFilter() {
            @Override
            protected void initConverters(List<Converter> converters) {
                converters.add(new NumberToBigDecimalConverterHandler());
            }
        });

        CollectionToCollectionConverterHandler collectionToCollectionConverterHandler = new CollectionToCollectionConverterHandler(new AbstractConverterFilter() {
            @Override
            protected void initConverters(List<Converter> converters) {
                converters.add(new BooleanToStringConverterHandler());
            }
        });

        MapToMapConverterHandler mapToMapConverterHandler = new MapToMapConverterHandler(new AbstractConverterFilter() {
            @Override
            protected void initConverters(List<Converter> converters) {

            }
        });

        Object o = arrayToArrayConverterHandler.convert(a);
        Object o2 = collectionToCollectionConverterHandler.convert(array);
        Object o3 = mapToMapConverterHandler.convert(map);
        System.out.println("1");
    }
}
