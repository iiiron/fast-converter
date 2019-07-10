package net.noboard.beantobeantest;

import net.noboard.fastconverter.FieldConverter;
import net.noboard.fastconverter.handler.DateToFormatStringConverterHandler;

import java.util.Date;

public class Tests {

    public class BeanA {
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

    public class BeanB {

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
//        BeanB b = new BeanB();
//        b.setValue(new Date());
//        b.setValueB(new Date());
//        BeanConverterContainer container = new BeanConverterContainer(new BeanA(),b );
//        new BeanToBeanConverterHandler(new CommonConverterFilter()).convert(container,BeanToBeanConverterHandler.NEGATIVE);
//        System.out.println(container);
    }

    public static void main(String[] args) {
        HumanA humanA = new HumanA();
//        net.noboard.beantobeantest.Man man = new net.noboard.beantobeantest.Man();
//        man.setName("wanxm");
//        man.setBirthday(new Date());
//        humanA.setHuman(man);
//        net.noboard.beantobeantest.HumanB humanB = (net.noboard.beantobeantest.HumanB) BeanToBeanConverterHandler.beanCopy().convert(humanA, net.noboard.beantobeantest.HumanB.class);
        humanA.setA(new int[]{1,2,3});
        new Tests().test();
    }
}
