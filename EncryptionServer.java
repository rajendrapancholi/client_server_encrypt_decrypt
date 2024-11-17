import java.io.*;
import java.net.*;
import javax.crypto.SecretKey;
import java.security.KeyPair;

public class EncryptionServer {
    private static SecretKey aesKey;
    private static KeyPair rsaKeyPair;

    public static void main(String[] args) throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            aesKey = EncryptionUtil.generateAESKey();
            rsaKeyPair = EncryptionUtil.generateRSAKeyPair();

            System.out.println("Server started...");

            while (true) {
                Socket socket = serverSocket.accept();
                new ClientHandler(socket).start();
            }
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {

                String algorithm = in.readUTF();
                String operation = in.readUTF();
                String text = in.readUTF();
                String result = "";

                long startTime = System.nanoTime();

                if (algorithm.equals("AES")) {
                    if (operation.equals("encrypt")) {
                        result = EncryptionUtil.encryptAES(text, aesKey);
                    } else {
                        result = EncryptionUtil.decryptAES(text, aesKey);
                    }
                } else if (algorithm.equals("RSA")) {
                    if (operation.equals("encrypt")) {
                        result = EncryptionUtil.encryptRSA(text, rsaKeyPair.getPublic());
                    } else {
                        result = EncryptionUtil.decryptRSA(text, rsaKeyPair.getPrivate());
                    }
                }

                long endTime = System.nanoTime();
                long duration = (endTime - startTime) / 1000000; // milliseconds

                out.writeUTF(result);
                out.writeLong(duration);
                out.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
