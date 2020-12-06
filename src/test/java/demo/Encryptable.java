package demo;

public interface Encryptable {
    void encrypt(int password, String[] input);

    void decrypt(int password);
}
