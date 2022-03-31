package net.noboard.demo.typesensin;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import net.noboard.Motion;
import net.noboard.Sex;
import net.noboard.fastconverter.FastConverter;
import org.checkerframework.checker.units.qual.C;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TypeSensingTest {

    @Test
    public void test() {
        ChildA childA = new ChildA();
        childA.setName("小小万");
        childA.setHobby(Lists.newArrayList(Motion.PING_PONG, Motion.BADMINTON));

        Map<Integer, ChildA> childAMap = new HashMap<>();
        childAMap.put(1, childA);

        ChildB childB = new ChildB();
        childB.setName("test");
        childB.setHobby(Lists.newArrayList(Motion.PING_PONG.name(), Motion.BADMINTON.name()));


        ParentA parentA = new ParentA();
        parentA.setSex(Sex.MAN);
        parentA.setBirthday(new Date());
        parentA.setBirthdayFormat(new Date());
        parentA.setAge(27L);
        parentA.setName("小万");
        parentA.setChildList(Lists.newArrayList(childB));
        parentA.setChildMap(Lists.newArrayList(childAMap));
        parentA.setChild(childA);

        Object o = FastConverter.autoConvert(parentA);

        Object result = FastConverter.autoConvert(parentA);

        System.out.println(JSON.toJSONString(result));

        ParentA result2 = FastConverter.autoConvert(result, ParentA.class);

        System.out.println(JSON.toJSONString(result2));

    }
}
