package generic;

import processor.Clock;
import processor.Processor;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.DataInputStream;


public class Simulator {
		
	static Processor processor;
	static boolean simulationComplete;
	
	public static void setupSimulation(String assemblyProgramFile, Processor p)
	{
		Simulator.processor = p;
		loadProgram(assemblyProgramFile);
		
		simulationComplete = false;
	}
	
	static void loadProgram(String assemblyProgramFile)
	{
		/*
		 * TODO
		 * 1. load the program into memory according to the program layout described
		 *    in the ISA specification
		 * 2. set PC to the address of the first instruction in the main
		 * 3. set the following registers:
		 *     x0 = 0
		 *     x1 = 65535
		 *     x2 = 65535
		 */
		try (InputStream input_str = new FileInputStream(assemblyProgramFile)){
			int address = -1 ; 
			DataInputStream data_in = new DataInputStream(input_str);
			

			
			while(data_in.available() > 0){
					int nxt = data_in.readInt();
					System.out.println(nxt);
				if(address != -1){
					processor.getMainMemory().setWord(address, nxt);
				}
				else{
					processor.getRegisterFile().setProgramCounter(nxt);
				}	
				address += 1 ;
			}

			processor.getRegisterFile().setValue(0, 0);
			processor.getRegisterFile().setValue(1,65535);
			processor.getRegisterFile().setValue(2,65535);

		}
		catch(Exception error){
			error.printStackTrace();
		}
	}
	
	public static void simulate()
	{
		while(simulationComplete == false)
		{
			processor.getIFUnit().performIF();
			Clock.incrementClock();
			processor.getOFUnit().performOF();
			Clock.incrementClock();
			processor.getEXUnit().performEX();
			Clock.incrementClock();
			processor.getMAUnit().performMA();
			Clock.incrementClock();
			processor.getRWUnit().performRW();
			Clock.incrementClock();


			Statistics st = new Statistics();
			st.setNumberOfInstructions(st.getNumberOfInstructions() + 1);
			st.setNumberOfCycles(st.getNumberOfCycles()+1);
		}
		
		// TODO
		// set statistics
	}
	
	public static void setSimulationComplete(boolean value)
	{
		simulationComplete = value;
	}
}
