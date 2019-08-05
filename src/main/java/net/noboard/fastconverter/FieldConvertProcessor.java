//package net.noboard.fastconverter;
//
//import javax.validation.Validation;
//import javax.validation.Validator;
//import java.lang.annotation.Annotation;
//import java.lang.reflect.Field;
//
//public class FieldConvertProcessor {
//
//    private Validator validation;
//
//    public FieldConvertProcessor() {
//        this.validation = Validation.buildDefaultValidatorFactory().getValidator();
//    }
//
//    void process(Field field, Object value, String group, ConverterFilter converterFilter) {
//        if (group == null) {
//            group = "";
//        }
//
//        FieldConvertPipeline fieldConvertPipeline = new FieldConvertPipeline();
//        for (Annotation annotation : field.getDeclaredAnnotations()) {
//            if (annotation instanceof ConverterIndicator) {
//                ConverterIndicator anno = (ConverterIndicator) annotation;
//                try {
//                    fieldConvertPipeline.add(anno.converter().newInstance());
//                } catch (InstantiationException | IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        }
//
//    }
//
//
//    private class FieldConvertPipeline implements Pipeline{
//
//        private String a;
//
//        @Override
//        public void add(LifeCycle lifeCycle) {
//
//        }
//
//        @Override
//        public void start(Value value) {
//
//        }
//    }
//}
