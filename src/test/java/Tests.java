import net.noboard.fastconverter.Field;
import net.noboard.fastconverter.FieldConverter;
import net.noboard.fastconverter.filter.CommonConverterFilter;
import net.noboard.fastconverter.handler.BeanConverterContainer;
import net.noboard.fastconverter.handler.BeanToBeanConverterHandler;
import net.noboard.fastconverter.handler.DateToFormatStringConverterHandler;

import java.util.Date;

public class Tests {

    public class BeanA{
        private String value;

        private String valueb;

        public String getValueb() {
            return valueb;
        }

        public void setValueb(String valueb) {
            this.valueb = valueb;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public class BeanB{

        private Date valueB;

        @FieldConverter(converter = DateToFormatStringConverterHandler.class)
        private Date value;

        public Date getValueB() {
            return valueB;
        }

        public void setValueB(Date valueB) {
            this.valueB = valueB;
        }

        public Date getValue() {
            return value;
        }

        public void setValue(Date value) {
            this.value = value;
        }
    }

    public void test() {
        BeanB b = new BeanB();
        b.setValue(new Date());
        b.setValueB(new Date());
        BeanConverterContainer container = new BeanConverterContainer(b, new BeanA());
        new BeanToBeanConverterHandler(new CommonConverterFilter()).convert(container);
        System.out.println(container);
    }

    public static void main(String[] args) {
        new Tests().test();
    }
}
