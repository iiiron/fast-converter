package net.noboard.fastconverter.support;

public class GroupUtils {
    public static boolean checkGroup(String groupExpress, String group) {
        if (groupExpress.contains(",")) {
            String[] strings = groupExpress.split(",");
            for (String s : strings) {
                if ("".equals(s.trim())) {
                    throw new IllegalArgumentException(String.format("group express '%s' is invalid", groupExpress));
                }
                if (s.trim().equals(group)) {
                    return true;
                }
            }
            return false;
        } else {
            return groupExpress.equals(group);
        }
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
