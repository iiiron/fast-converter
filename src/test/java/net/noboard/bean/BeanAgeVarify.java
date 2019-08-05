//package net.noboard.bean;
//
//import net.noboard.fastconverter.VerifyInfo;
//
//public class BeanAgeVarify implements Validator {
//    @Override
//    public VerifyInfo validate(Object value) {
//        ChildB childB = (ChildB) value;
//        if (childB.getAge() < 18) {
//            return new VerifyInfo(1,"年龄未满18周岁");
//        }
//        return new VerifyInfo();
//    }
//}
