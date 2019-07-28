import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.Random;

public class FullerComponent {
    public static double memoryUsage; // how much memory to fill (in MB)
    public static double outputSize; // the size of the output data (in KB)

    public static boolean databaseOnStartup = false; // true: database access is simulated at the start, false: with each map
    public static int numRequests = 1; // how many requests to make to the database
    public static double responseSize; // size of a single database response (in KB)
    public static double databaseLatency = 0; // the amount of time between a request and the first byte of the response (in ms)
    public static double bandwidth = 0; // database response bandwidth (in bytes/s)
    public static double intervalBetweenRequests = 0; // how long to wait between database requests (in ms)

    private static final int BYTES_IN_MB = 1 << 20;
    private static final int BYTES_IN_KB = 1 << 10;

    // These constants are measure experimentally. Relative errors are within 7%
    private static final double BASE_MEMORY_CONSUMPTION = 22.62 * BYTES_IN_MB;
    private static final double ARRAY_SIZE_LINEAR = 3.888;
    private static final double ARRAY_SIZE_QUADRATIC = -2.695e-6;
    private static final double STRING_LENGTH_LINEAR = 39.97;
    private static final double STRING_LENGTH_QUADRATIC = -3.128e-5;
    private static final double NUM_NODES_LINEAR = 51.97;
    private static final double NUM_NODES_QUADRATIC = 1.385e-5;

    private static void simulateDatabaseAccess() throws Exception {
        int latency = 0;
        int sleepTime = 0;
        int numNodes = (int) (responseSize * BYTES_IN_KB / NODE_SIZE);
        int bandwidthLatency = 0;
        Random rng = new Random();

        for (int i = 0; i < numRequests; i++) {
            // Gradually build up a linked list of random data, simulating a slow database response transfer
            List<Long> data = new LinkedList<>();
            for (int j = 0; j < numNodes; j++) {
                data.add(rng.nextLong());
            }
        }
    }

    public static void main(String[] args) throws Exception {
        memoryUsage = Double.parseDouble(args[0]);
        outputSize = Double.parseDouble(args[1]) * 1024;
        responseSize = Double.parseDouble(args[2]) * 1024;

        // Memory calculations
        int stringLength = (int) (outputSize * BYTES_IN_KB / BYTES_PER_CHAR);
        int arraySize = (int) (memoryUsage * BYTES_IN_MB - BASE_MEMORY_CONSUMPTION - outputSize * BYTES_IN_KB -
                               responseSize * BYTES_IN_KB);
        arraySize = Math.max(arraySize, stringLength); // arraySize >= stringLength

        // Fill the required amount of memory with random data
        byte[] memory = new byte[arraySize];
        new Random().nextBytes(memory);

        // Construct the output string
        String out = new String(memory, 0, stringLength);

        simulateDatabaseAccess();
    }
}