package generic;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;

import generic.Operand.OperandType;
import generic.ParsedProgram;
import generic.Instruction.OperationType;


public class Simulator {
		
	static FileInputStream inputcodeStream = null;
	public static HashMap<OperationType, String> opHashMap = new HashMap<>(){{
		put(OperationType.add, "00000");
		put(OperationType.add, "00000");
		put(OperationType.addi , "00001");
		put(OperationType.sub , "00010");
		put(OperationType.subi , "00011");
		put(OperationType.mul , "00100");
		put(OperationType.muli , "00101");
		put(OperationType.div , "001100");
		put(OperationType.divi , "00111");
		put(OperationType.and , "01000");
		put(OperationType.andi , "01001");
		put(OperationType.or , "01010");
		put(OperationType.ori , "01011");
		put(OperationType.xor , "01100");
		put(OperationType.xori , "01101");
		put(OperationType.slt , "01110");
		put(OperationType.slti , "01111");
		put(OperationType.sll , "10000");
		put(OperationType.slli , "10001");
		put(OperationType.srl , "10010");
		put(OperationType.srli , "10011");
		put(OperationType.sra , "10100");
		put(OperationType.srai , "10101");
	}};
		
	
	//start of code for assembler
	//Here we use the parseDataSection to get the address from where the code(instructions) start
	//feed it to parseCodeSection
	public static void setupSimulation(String assemblyProgramFile)
	{	
		int firstCodeAddress = ParsedProgram.parseDataSection(assemblyProgramFile);
		ParsedProgram.parseCodeSection(assemblyProgramFile, firstCodeAddress);
		ParsedProgram.printState();
	}

	public static String integerToBinaryString(int number, int numBits )
	{
        // Convert the integer to a binary string
        String binary = Integer.toBinaryString(number);

        // Pad the binary string with leading zeros to achieve the desired bit length
        if (binary.length() < numBits) {
            int numLeadingZeros = numBits - binary.length();
            String leadingZeros = "0".repeat(numLeadingZeros);
            binary = leadingZeros + binary;
        }

        return binary;
    }

	public static void assemble(String objectProgramFile)
	{
		//TODO your assembler code
		//1. open the objectProgramFile in binary mode
		//2. write the firstCodeAddress to the file
		//3. write the data to the file
		//4. assemble one instruction at a time, and write to the file
		//5. close the file

		FileOutputStream file;
		try 
		{
			file = new FileOutputStream(objectProgramFile);

			//Start with writing the firstCodeAddress to the file
			file.write(ByteBuffer.allocate(4).putInt(ParsedProgram.firstCodeAddress).array());

			//For writing the data to the file
			for(int i=0; i<ParsedProgram.data.size();i++)
				file.write(ByteBuffer.allocate(4).putInt(ParsedProgram.data.get(i)).array());
			

			//assemble one instruction at a time, and write to the file
			for (var instruction : ParsedProgram.code) 
			{
				//initialise the string
				String binary_string_inst = "";
				
				//add the optcode to the string
				binary_string_inst += opHashMap.get(instruction.operationType);

				//Getting the interger value of the optcode for cases because the 
				//optcode is designed in such a way that similar operations occur together
				int op=Integer.parseInt(opHashMap.get(instruction.operationType),2);
				int pc = instruction.getProgramCounter();
				
				/////// we will start here
				if (op <= 20 && op % 2 == 0) {
					// R3 Type
					line += convert_1(instruction.getSourceOperand1(), 5);
					line += convert_1(instruction.getSourceOperand2(), 5);
					line += convert_1(instruction.getDestinationOperand(), 5);
					line += toBinaryOfSpecificPrecision(0, 12);
					
				}
				else if (op == 29) {
					line += toBinaryOfSpecificPrecision(0, 27);
				}
				else if (op == 24) {
					// RI Type
					if (instruction.destinationOperand.getOperandType() == Operand.OperandType.Register) {
						line += convert(instruction.getDestinationOperand(), 5);
						line += toBinaryOfSpecificPrecision(0, 22);
					} else {
						line += toBinaryOfSpecificPrecision(0, 5);
						int value = Integer.parseInt(convert(instruction.getDestinationOperand(), 5), 2) - pc;
						String bin = toBinaryOfSpecificPrecision(value, 22);
						line += bin.substring(bin.length() - 22);
					}
				}
				else {
					// R2I Type
					if (op >= 25 && op <= 28) {
						int value = Integer.parseInt(convert(instruction.getDestinationOperand(), 5), 2) - pc;
						line += convert(instruction.getSourceOperand1(), 5);
						line += convert(instruction.getSourceOperand2(), 5);
						String bin = toBinaryOfSpecificPrecision(value, 17);
						line += bin.substring(bin.length() - 17);
					} else {
						line += convert(instruction.getSourceOperand1(), 5);
						line += convert(instruction.getDestinationOperand(), 5);
						line += convert(instruction.getSourceOperand2(), 17);
					}
				}
				int instInteger = (int) Long.parseLong(line, 2);
				byte[] instBinary = ByteBuffer.allocate(4).putInt(instInteger).array();
				file.write(instBinary);
			}
			file.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
