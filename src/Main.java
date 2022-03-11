import com.Constants;
import com.internal.InputFormat;
import com.io.InputReader;
import com.io.OutputWriter;
import com.logic.Simulation;

public class Main {

    private static final String TEST_FOLDER = "checker/resources/in/";

    public static void main(String[] args) throws Exception {
        InputReader inputReader = InputReader.getInstance();
        inputReader.setSource(args[0]);

        InputFormat copy = inputReader.read();

        Simulation simulationInstance = Simulation.getInstance();
        simulationInstance.setData(copy);
        simulationInstance.simulate();

        OutputWriter outputWriter = OutputWriter.getInstance();
        outputWriter.setDestination(args[1]);
        outputWriter.setDataToWrite(simulationInstance.getResult());
        outputWriter.write();
    }
}
