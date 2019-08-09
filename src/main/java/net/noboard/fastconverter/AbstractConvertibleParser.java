package net.noboard.fastconverter;

import java.lang.reflect.AnnotatedElement;

public abstract class AbstractConvertibleParser implements ConvertibleParser {

    protected AnnotatedElement annotatedElement;

    protected String group;

    protected String tip;

    @Override
    public void setAnnotatedElement(AnnotatedElement annotatedElement) {
        this.annotatedElement = annotatedElement;
    }

    @Override
    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    public void setTip(String tip) {
        this.tip = tip;
    }

    public class CMap implements ConvertibleMap {

        private String tip;

        private String group;

        private Converter converter;

        @Override
        public String getTip() {
            return tip;
        }

        @Override
        public void setTip(String tip) {
            this.tip = tip;
        }

        @Override
        public String getGroup() {
            return group;
        }

        @Override
        public void setGroup(String group) {
            this.group = group;
        }

        @Override
        public Converter getConverter() {
            return converter;
        }

        @Override
        public void setConverter(Converter converter) {
            this.converter = converter;
        }
    }
}
