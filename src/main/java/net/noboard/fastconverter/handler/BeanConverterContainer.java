package net.noboard.fastconverter.handler;

public class BeanConverterContainer {
    private Object beanFrom;

    private Object beanTo;

    public BeanConverterContainer() {

    }

    public BeanConverterContainer(Object from, Object to) {
        this.beanFrom = from;
        this.beanTo = to;
    }

    public Object getBeanFrom() {
        return beanFrom;
    }

    public void setBeanFrom(Object beanFrom) {
        this.beanFrom = beanFrom;
    }

    public Object getBeanTo() {
        return beanTo;
    }

    public void setBeanTo(Object beanTo) {
        this.beanTo = beanTo;
    }
}
