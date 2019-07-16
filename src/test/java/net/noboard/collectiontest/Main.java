package net.noboard.collectiontest;

import net.noboard.fastconverter.*;
import net.noboard.fastconverter.filter.CommonConverterFilter;
import net.noboard.fastconverter.filter.CommonSkipConverterFilter;
import net.noboard.fastconverter.handler.*;
import net.noboard.fastconverter.handler.base.AbstractConverterHandler;
import net.noboard.fastconverter.handler.base.BeanToMapConverterHandler;

import java.util.*;

public class Main {

    public static class MyBean {
        @Field(name = "chineseName")
        private String name;

        @FieldConverter(converter = DateToFormatStringConverterHandler.class)
        private Date birthday;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Date getBirthday() {
            return birthday;
        }

        public void setBirthday(Date birthday) {
            this.birthday = birthday;
        }
    }

    public static void main(String[] args) {
        MyBean myBean = new MyBean();
        myBean.setName("万相明");
        myBean.setBirthday(new Date());

        Converter converter = new BeanToMapConverterHandler(new CommonSkipConverterFilter());
        Object o = converter.convert(myBean);

        System.out.println(new ObjectToJsonStringConverterHandler(new CommonSkipConverterFilter()).convert(myBean));
    }

    private class A extends AbstractConverterHandler<String, String> {

        @Override
        protected String converting(String value, String tip) throws ConvertException {
            return null;
        }

        @Override
        public boolean supports(Object value) {
            return false;
        }
    }
}
