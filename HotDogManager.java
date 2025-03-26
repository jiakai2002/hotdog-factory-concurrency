import java.io.*;

public class HotDogManager {

    static int N; // total hot dogs required
    static int S; // buffer capacity
    static int M; // number of making machines
    static int P; // number of packing machines

    static volatile int made = 0;
    static volatile int packed = 0;

    static Buffer buffer;
    static PrintWriter writer;

    static final Object makerLock = new Object();
    static final Object packerLock = new Object();

    private static void work(int n) { 
        for (int i = 0; i < n; i++) {
            long m = 300000000;
            while (m > 0) {
                m--;
            }
        }
    }
/**
 * Thread-safe logging method to write records to a shared writer.
 * The method is declared static and synchronized to ensure only one thread
 * can write to the writer at a time, preventing race conditions.
 */ 
    static synchronized void log(String record) {
        writer.println(record);
        writer.flush();
    }

    public static class Hotdog {

        private final int id;
        private final int machineID;

        public Hotdog(int id, int machineID) {
            this.id = id;
            this.machineID = machineID;
        }
    }

    public static class Buffer {

        private final Hotdog[] buffer;
        private int head;
        private int tail; 
        private int count; 

        public Buffer(int S) {
            this.buffer = new Hotdog[S];
            this.head = 0;
            this.tail = 0;
            this.count = 0;
        }

        synchronized public void put(Hotdog hotdog) { // adds a hotdog to the hotdog queue
            while (count == buffer.length) { // block if hotdog Buffer is full
                try {
                    this.wait();
                } catch (InterruptedException e) {
                }
            }
            buffer[tail] = hotdog;
            tail = (tail + 1) % buffer.length;
            count++;
            this.notifyAll();
        }

        synchronized public Hotdog get() { // removes and returns the first hotdog
            while (count == 0) { // block if hotdog Buffer is empty
                // If production is complete and no hotdogs are waiting, then return null to avoid
                // infinite wait.
                if (HotDogManager.made >= HotDogManager.N) {
                    return null;
                }
                try {
                    this.wait();
                } catch (InterruptedException e) {
                }
            }
            final Hotdog temp = buffer[head];
            buffer[head] = null;
            head = (head + 1) % buffer.length;
            count--;
            this.notifyAll();
            return temp;
        }
    }

    public static class Maker extends Thread {
        private final int id;
        private int count;

        public Maker(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            while (true) {
                int tempHotdogID;
                // Synchronizes on makerLock to ensure thread-safe updates to the `made` counter.
                // This prevents race conditions when multiple threads attempt to increment `made` simultaneously.
                synchronized (makerLock) { 
                    if (made >= N) {
                        break;
                    }
                    made++; 
                    tempHotdogID = made; 
                }
                work(4); // making hotdog
                Hotdog newHotdog = new Hotdog(tempHotdogID, id);
                work(1); // send hotdog to buffer
                buffer.put(newHotdog);
                // thread-safe logging
                log(String.format("m%d puts %d", id, tempHotdogID));
                count++;
            }
        }
    }

    public static class Packer extends Thread {
        private final int id;
        private int count; 

        public Packer(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            while (true) {
                Hotdog taken = buffer.get(); 
                // No more hotdogs left to pack 
                if (taken == null) {
                    break;
                }
                work(1); // take hotdog
                work(2); // pack hotdog
                // Synchronizes on packerLock to ensure thread-safe updates to the `packed` counter.
                // This prevents race conditions when multiple threads attempt to increment `packed` simultaneously.
                synchronized (packerLock) {
                    if (packed >= N) {
                        break;
                    }
                    packed++;
                }
                //thread-safe logging
                log(String.format("p%d gets %d from m%d", id, taken.id, taken.machineID));
                count++;
            }
        }
    }

    public static void main(String[] args) {
        N = Integer.parseInt(args[0]); // number hot dogs to make
        S = Integer.parseInt(args[1]); // size of buffer
        M = Integer.parseInt(args[2]); // number of making machines
        P = Integer.parseInt(args[3]); // number of packing machines

        buffer = new Buffer(S);
        try {
            writer = new PrintWriter(new FileWriter("log.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // log header
        writer.println("order:" + N);
        writer.println("capacity:" + S);
        writer.println("making machines:" + M);
        writer.println("packing machines:" + P);
        writer.flush();

        // create and start maker threads
        Maker[] makers = new Maker[M]; 
        for (int i = 0; i < M; i++) {
            makers[i] = new Maker(i + 1);
            makers[i].start();
        }

        // create and start packer threads
        Packer[] packers = new Packer[P];
        for (int i = 0; i < P; i++) {
            packers[i] = new Packer(i + 1);
            packers[i].start();
        }

        // wait for all makers to finish
        for (Maker maker : makers) { 
            try {
                maker.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // wait for all packers to finish
        for (Packer packer : packers) {
            try {
                packer.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        // log summary
        writer.println("summary:");
        for (Maker maker : makers) {
            writer.println("m" + maker.id + " made " + maker.count);
        }
        for (Packer packer : packers) {
            writer.println("p" + packer.id + " packed " + packer.count);
        }
        writer.flush();
        writer.close();
    }
}