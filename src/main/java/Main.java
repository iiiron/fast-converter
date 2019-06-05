import com.alibaba.fastjson.JSON;
import net.noboard.fastconverter.*;
import net.noboard.fastconverter.filter.CommonConverterFilter;
import net.noboard.fastconverter.handler.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        try {
            System.out.println(new ObjectToJsonStringConverterHandler(new CommonConverterFilter()).convert(myBean));
        } catch (ConvertException e) {
            e.printStackTrace();
        }
    }
}
