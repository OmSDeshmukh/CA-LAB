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
	public static HashMap<OperationType, String> opHashMap = new HashMap<OperationType, String>(){{
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
		put(OperationType.load, "10110");
		put(OperationType.store, "10111");
		put(OperationType.jmp, "11000");
		put(OperationType.beq, "11001");
		put(OperationType.bne, "11010");
		put(OperationType.blt, "11011");
		put(OperationType.bgt, "11100");
		put(OperationType.end, "11101");
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
			for (generic.Instruction instruction : ParsedProgram.code) 
			{
				//initialise the string
				String binary_string_inst = "";
				
				//add the optcode to the string
				binary_string_inst += opHashMap.get(instruction.operationType);

				//Getting the interger value of the optcode for cases because the 
				//optcode is designed in such a way that similar operations occur together
				int op=Integer.parseInt(opHashMap.get(instruction.operationType),2);
				int pc = instruction.getProgramCounter();
				

				if (op <= 20 && op % 2 == 0) 
				{
					// R3 Type
					binary_string_inst += integerToBinaryString(instruction.getSourceOperand1().getValue(), 5);
					binary_string_inst += integerToBinaryString(instruction.getSourceOperand2().getValue(), 5);
					binary_string_inst += integerToBinaryString(instruction.getDestinationOperand().getValue(), 5);
					binary_string_inst += integerToBinaryString(0,12);
					
				}

				//end operation
				else if (op == 29) 
				{
					binary_string_inst += integerToBinaryString(0,27);
				}

				else if (op == 24) 
				{
					// RI Type
					//jmp operation
					//no need of this block
					if (instruction.destinationOperand.getOperandType() == Operand.OperandType.Register) 
					{
						binary_string_inst += integerToBinaryString(instruction.getDestinationOperand().getValue(), 5);
						binary_string_inst += integerToBinaryString(0, 22);
					} 
					else 
					//solve tomorrow
					{
						binary_string_inst += integerToBinaryString(0, 5);
						int value = Integer.parseInt(integerToBinaryString(ParsedProgram.symtab.get(instruction.getDestinationOperand().getLabelValue()), 5), 2) - pc;
						//still don't understand these 2 steps
						String extra = integerToBinaryString(value, 22);
						binary_string_inst += extra.substring(extra.length() - 22);
					}
				}

				else 
				{
					// R2I Type
					if (op >= 25 && op <= 28) 
					//solve tomorrow
					//not understanding the need of the step
					{
						binary_string_inst += integerToBinaryString(instruction.getSourceOperand1().getValue(), 5);
						binary_string_inst += integerToBinaryString(instruction.getSourceOperand2().getValue(), 5);
						//don't understand these 3 steps
						int value = Integer.parseInt(integerToBinaryString(ParsedProgram.symtab.get(instruction.getDestinationOperand().getLabelValue()), 5), 2) - pc;
						String extra = integerToBinaryString(value, 17);
						binary_string_inst += extra.substring(extra.length() - 17);
					} 
					else 
					{
						//don't understand these 3 steps
						binary_string_inst += integerToBinaryString(instruction.getSourceOperand1().getValue(), 5);
						binary_string_inst += integerToBinaryString(instruction.getDestinationOperand().getValue(), 5);
						binary_string_inst += integerToBinaryString(instruction.getSourceOperand2().getValue(), 17);
					}
				}
				int instInteger = (int) Long.parseLong(binary_string_inst, 2);
				byte[] instBinary = ByteBuffer.allocate(4).putInt(instInteger).array();
				file.write(instBinary);
			}
			file.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			
		}
		
	}
	
}
