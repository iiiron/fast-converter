import com.alibaba.fastjson.JSON;
import net.noboard.fastconverter.ConvertException;
import net.noboard.fastconverter.Converter;
import net.noboard.fastconverter.ConverterFilter;
import net.noboard.fastconverter.filter.CommonConverterFilter;
import net.noboard.fastconverter.handler.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    private static class Test {
        private boolean isSuccess;

        private Boolean isOk;

        public Boolean getOk() {
            return isOk;
        }

        public void setOk(Boolean ok) {
            isOk = ok;
        }

        public boolean isSuccess() {
            return isSuccess;
        }

        public void setSuccess(boolean success) {
            isSuccess = success;
        }
    }

    public static void main(String[] args) {
       Test test = new Test();
       test.setSuccess(true);

        try {
            new BeanToMapConverterHandler(new ConverterFilter() {
                @Override
                public <T> Converter getConverter(T value) {
                    return new BooleanToNumberStringConverterHandler();
                }
            }).convert(test);
        } catch (ConvertException e) {
            e.printStackTrace();
        }
    }
}
