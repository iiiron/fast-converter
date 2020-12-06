package demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

public class SecretMessage implements Encryptable {

    private int password;

    private int key;

    private String[] originalMessage;

    private String[] encryptedMessage;

    public SecretMessage(int key, int password, int length) {
        this.key = key;
        this.password = password;
        this.originalMessage = new String[length];
        this.encryptedMessage = new String[key * length];
    }

    @Override
    public void encrypt(int password, String[] input) {
        String salt = input[new Random().nextInt(input.length)];
        int index = 0;
        for (String rm : originalMessage) {
            encryptedMessage[index++] = rm;
            for (int i = 0; i < key - 1; i++) {
                encryptedMessage[index++] = salt;
            }
        }
        print(encryptedMessage);
    }

    @Override
    public void decrypt(int password) {
        int index = 0;
        int count = 0;
        for (String em : encryptedMessage) {
            if (count++ % key == 0) {
                originalMessage[index++] = em;
            }
        }
        print(originalMessage);
    }

    public void setOriginalMessage(String[] originalMessage) {
        this.originalMessage = originalMessage;
    }

    public void setEncryptedMessage(String[] encryptedMessage) {
        this.encryptedMessage = encryptedMessage;
    }

    private void print(String[] args) {
        System.out.println(Arrays.stream(args).reduce("", (a, b) -> {
            return a + " " + b;
        }));
    }
}
