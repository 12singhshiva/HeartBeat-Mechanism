import java.net.*;
import java.util.*;


class PyHeartBeatServer {
    static int HB_PORT = 43278;
    static int CHECK_WAIT = 30;

    public static void main(String[] args) throws Exception {
        if (args.length > 0)
            HB_PORT = Integer.parseInt(args[0]);
        if (args.length > 1)
            CHECK_WAIT = Integer.parseInt(args[1]);

        BeatDict beatDict = new BeatDict();
        BeatRec beatRecThread = new BeatRec(beatDict, HB_PORT);
        beatRecThread.start();

        System.out.println("PyHeartBeat server listening on port " + HB_PORT);
        System.out.println("\n*** Press Ctrl-C to stop ***\n");

        while (true) {
            System.out.println("Beat Dictionary");
            System.out.println(beatDict.toString());
            List<String> silent = beatDict.extractSilent(CHECK_WAIT);
            if (!silent.isEmpty()) {
                System.out.println("Silent clients");
                System.out.println(silent);
            }
            Thread.sleep(CHECK_WAIT * 1000);
        }
    }
}

class BeatDict {
    private Map<String, Long> beatDict = new HashMap<>();
    private final Object lock = new Object();

    public void update(String entry) {
        synchronized (lock) {
            beatDict.put(entry, System.currentTimeMillis());
        }
    }

    public List<String> extractSilent(int howPast) {
        synchronized (lock) {
            List<String> silent = new ArrayList<>();
            long when = System.currentTimeMillis() - howPast * 1000;
            for (Map.Entry<String, Long> entry : beatDict.entrySet()) {
                if (entry.getValue() < when) {
                    silent.add(entry.getKey());
                }
            }
            return silent;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        synchronized (lock) {
            for (Map.Entry<String, Long> entry : beatDict.entrySet()) {
                sb.append("IP address: ").append(entry.getKey()).append(" - Last time: ").append(new Date(entry.getValue())).append("\n");
            }
        }
        return sb.toString();
    }
}

class BeatRec extends Thread {
    private BeatDict beatDict;
    private int port;

    public BeatRec(BeatDict beatDict, int port) {
        this.beatDict = beatDict;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            DatagramSocket recSocket = new DatagramSocket(port);
            byte[] receiveData = new byte[1024];
            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                recSocket.receive(receivePacket);
                String data = new String(receivePacket.getData(), 0, receivePacket.getLength());
                // Uncomment the line below for debugging
                // System.out.println("Received packet from " + receivePacket.getAddress());
                beatDict.update(receivePacket.getAddress().getHostAddress());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}