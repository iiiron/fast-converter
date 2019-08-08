package net.noboard.fastconverter;

public interface GroupableConverter<T, K> extends Converter<T, K> {
    String checkGroup();

    void configureGroup(String group);
}
