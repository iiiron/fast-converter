package demo;

public class MessageDriver {
    private static final String[] gibberish = {"hello", "goodbye", "yes", "no", "why", "ok", "wonder"};

    public static void main(String[] args) {
        SecretMessage message1 = new SecretMessage(3, 1234, 5);
        message1.setOriginalMessage(new String[]{"my", "name", "is", "JiaCheng Lee", "!"});
        message1.encrypt(1234, gibberish);

        SecretMessage message2 = new SecretMessage(4, 4321, 4);
        message2.setEncryptedMessage(new String[]{"my", "ok", "ok", "ok", "name", "ok", "ok", "ok", "is", "ok", "ok", "ok", "Lee", "ok", "ok", "ok"});
        message2.decrypt(4321);

        String a = "zuid#1141";
        String b = a.substring(6, a.length());
        System.out.println(b);
    }
}
