package net.noboard;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.noboard.fastconverter.ConvertibleBean;
import net.noboard.fastconverter.FastConverter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TypeTest {


    public static void main(String[] args) {
//        Field[] declaredFields = ListB.class.getDeclaredFields();
//        for (Field declaredField : declaredFields) {
//            Type genericType = declaredField.getGenericType();
//            Class<? extends Type> aClass = genericType.getClass();
//            boolean assignableFrom = ParameterizedType.class.isAssignableFrom(aClass);
//            if (ParameterizedType.class.isAssignableFrom(aClass)) {
//                ParameterizedType genericType1 = (ParameterizedType) genericType;
//                genericType1.getActualTypeArguments();
//            }
//            System.out.println("type");
//        }

        ListA listA = new ListA();
        Map<Boolean, ItemA> a = new HashMap<>();
        a.put(true, new ItemA(true));
        listA.setList(Lists.newArrayList(a));

        ListB listB = FastConverter.autoConvert(listA, ListB.class);

        System.out.println("success");
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class ItemA {
        private Boolean pass;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class ListA {
        List<Map<Boolean, ItemA>> list;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @ConvertibleBean(relevantClass = CollectionTest.ItemA.class)
    public static class ItemB {

        private String pass;
    }

    @NoArgsConstructor
    @Data
    @ConvertibleBean(relevantClass = CollectionTest.ListA.class)
    public static class ListB {
        private List<Map<String, ItemB>> list;
    }
}
