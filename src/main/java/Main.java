import net.noboard.fastconverter.*;
import net.noboard.fastconverter.filter.CommonConverterFilter;
import net.noboard.fastconverter.handler.*;
import net.noboard.fastconverter.handler.base.AbstractConverterHandler;

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

        List<String> list = new ArrayList<>();
        Converter c = new CollectionToListConverterHandler<>(new CommonConverterFilter());
        boolean d = List.class.isAssignableFrom(list.getClass());
        List b = (List) c.convert(list);

        System.out.println(new ObjectToJsonStringConverterHandler(new CommonConverterFilter()).convert(myBean));
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
