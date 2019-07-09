package net.noboard.fastconverter.handler;

import com.alibaba.fastjson.JSON;
import net.noboard.fastconverter.ConvertException;

/**
 * 这个转换器能将json字符串格式化为缩进和折行的形式
 */
public class JsonStringToFormatJsonStringConverterHandler extends AbstractConverterHandler<String, String> {
    @Override
    protected String converting(String value, String tip) throws ConvertException {
        int level = 0;

        //存放格式化的json字符串
        StringBuffer jsonForMatStr = new StringBuffer();
        try {
            for (int index = 0; index < value.length(); index++)//将字符串中的字符逐个按行输出
            {
                //获取s中的每个字符
                char c = value.charAt(index);

                //level大于0并且jsonForMatStr中的最后一个字符为\n,jsonForMatStr加入\t
                if (level > 0 && '\n' == jsonForMatStr.charAt(jsonForMatStr.length() - 1)) {
                    jsonForMatStr.append(getLevelStr(level));
                }

                switch (c) {
                    case '{':
                    case '[':
                        jsonForMatStr.append(c + "\n");
                        level++;
                        break;
                    case ',':
                        jsonForMatStr.append(c + "\n");
                        break;
                    case '}':
                    case ']':
                        jsonForMatStr.append("\n");
                        level--;
                        jsonForMatStr.append(getLevelStr(level));
                        jsonForMatStr.append(c);
                        break;
                    default:
                        jsonForMatStr.append(c);
                        break;
                }
            }
        } catch (Exception e) {
            throw new ConvertException("格式化JSON字符串时出错：" + e.getMessage());
        }

        return jsonForMatStr.toString();
    }

    private static String getLevelStr(int level) {
        StringBuffer levelStr = new StringBuffer();
        for (int levelI = 0; levelI < level; levelI++) {
            levelStr.append("\t");
        }
        return levelStr.toString();
    }

    @Override
    public boolean supports(Object value) {
        return value != null && String.class.isAssignableFrom(value.getClass());
    }
}
