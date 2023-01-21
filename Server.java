import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ThreadLocalRandom;

public class Server {
    static final int PORT = 8080;
    static int number[] = { 7, 0, 1, 2, 0, 3, 0, 4, 2, 3, 0, 3, 0, 3, 2, 1, 2, 0, 1, 7, 0, 1 };

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        Socket socket = serverSocket.accept();

        DataOutputStream socketOut = null;
        try {
            socketOut = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Client connected");

        try {
            int memorySize = 3;
            socketOut.writeInt(memorySize);

            int memoryAccessCount = 22;
            for (int i = 0; i < memoryAccessCount; i++) {
                int memoryAddress = number[i];
                System.out.println(memoryAddress);
                socketOut.writeInt(memoryAddress);

                int delay = ThreadLocalRandom.current().nextInt(1, 2000 + 1);
                Thread.sleep(delay);
            }

            socketOut.writeInt(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}