package net.noboard.fastconverter.handler.support;

public class GroupUtils {
    public static boolean checkGroup(String groupExpress, String group) {
        return groupExpress.equals(group);
    }

    public static void requireGroup(String group, Class of) {
        if (group == null || "".equals(group)) {
            throw new IllegalArgumentException(
                    String.format(
                            "the attribute named group of annotation %s can not be empty string or null",
                            of.getName()));
        }
    }
}
