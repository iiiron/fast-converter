package net.noboard.fastconverter.handler;

import net.noboard.fastconverter.ConvertException;
import net.noboard.fastconverter.handler.core.AbstractConverterHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class InputStreamToStringConverterHandler extends AbstractConverterHandler<InputStream, String> {

    public InputStreamToStringConverterHandler() {
        super("utf-8");
    }

    @Override
    protected String converting(InputStream value, String tip) throws ConvertException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(value, tip));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            throw new ConvertException(e);
        }
    }

    @Override
    public boolean supports(Object value, String tip) {
        return value != null && InputStream.class.isAssignableFrom(value.getClass());
    }
}
