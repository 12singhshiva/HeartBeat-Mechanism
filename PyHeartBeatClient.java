import java.net.*;
import java.util.*;

class PyHeartBeatClient {
    static String SERVER_IP = "127.0.0.1"; // Localhost for testing
    static int HB_PORT = 43278; // Arbitrary UDP port
    static int BEAT_WAIT = 10; // Number of seconds between heartbeats

    public static void main(String[] args) throws Exception {
        if (args.length > 0)
            SERVER_IP = args[0];
        if (args.length > 1)
            HB_PORT = Integer.parseInt(args[1]);

        DatagramSocket hbSocket = new DatagramSocket();
        System.out.println("PyHeartBeat client sending to IP " + SERVER_IP + ", port " + HB_PORT);
        System.out.println("\n*** Press Ctrl-C to terminate ***\n");

        while (true) {
            byte[] sendData = "Thump!".getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(SERVER_IP), HB_PORT);
            hbSocket.send(sendPacket);

            // Uncomment the line below for debugging
            // System.out.println("Time: " + new Date());

            Thread.sleep(BEAT_WAIT * 1000);
        }
    }
}