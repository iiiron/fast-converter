import net.noboard.fastconverter.ConvertException;
import net.noboard.fastconverter.filter.CommonConverterFilter;
import net.noboard.fastconverter.handler.CommonFilterBaseConverterHandler;
import net.noboard.fastconverter.handler.NullConverterHandler;

import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        int[] intary = {1,2,3,4};
        map.put("a", intary);

        CommonFilterBaseConverterHandler commonFilterBaseConverterHandler = new CommonFilterBaseConverterHandler(new CommonConverterFilter());
        try {
            Object result = commonFilterBaseConverterHandler.convert(map);
            System.out.println(result);
        } catch (ConvertException e) {
            e.printStackTrace();
        }
    }
}
