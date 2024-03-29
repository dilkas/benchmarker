import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

/* This class sends messages to the given output channel according to the function given by the field 'function'.
   It is initialised by Jackson when given an appropriate global.yaml file with variables corresponding to the fields
   of this class. */
public class FunctionalWorkload extends Workload {
    public String function;
    public double binWidth;
    public double initialX;
    public double finalX;

    public void execute(PrintWriter out) throws Exception {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("js");
        long timeToSleep = (long) (binWidth * TimeUnit.NANOSECONDS.convert(1, TimeUnit.SECONDS));
        try {
            // Evaluate the function at initialX + binWidth / 2, initialX + 3 * binWidth / 2, ..., < finalX
            for (double x = initialX + binWidth / 2; x < finalX; x += binWidth) {
                // Replace all x's that are not part of a longer word with the numeric x.
                // This is needed so that the 'x' in 'exp' is not replaced.
                double y = (Double) engine.eval(function.replaceAll("(?<!\\w)x(?!\\w)", Double.toString(x)));
                int numRequests = (int) Math.round(y * binWidth);
                System.out.println("Sending " + numRequests + " messages");
                for (int i = 0; i < numRequests; i++)
                    out.println(".");
                if (x + binWidth < finalX)
                    TimeUnit.NANOSECONDS.sleep(timeToSleep);
            }
        } catch (ScriptException e) {
            System.err.println("ERROR: The provided function does not evaluate to a number. " +
                    "Is 'x' the only variable? Does the string contain valid JavaScript code?");
            e.printStackTrace();
        }
    }
}
