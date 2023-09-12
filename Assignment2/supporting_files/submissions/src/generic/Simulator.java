package generic;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import generic.Instruction.OperationType;

public class Simulator {
		
	static FileInputStream inputcodeStream = null;
	//A hash map for opcode of commands
	public static HashMap<OperationType, String> opHashMap = new HashMap<OperationType, String>(){{
		put(OperationType.add, "00000");
		put(OperationType.addi , "00001");
		put(OperationType.sub , "00010");
		put(OperationType.subi , "00011");
		put(OperationType.mul , "00100");
		put(OperationType.muli , "00101");
		put(OperationType.div , "00110");
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
		

	public static void setupSimulation(String assemblyProgramFile)
	{	
		int firstCodeAddress = ParsedProgram.parseDataSection(assemblyProgramFile);
		ParsedProgram.parseCodeSection(assemblyProgramFile, firstCodeAddress);
		ParsedProgram.printState();
	}

	//A function to convert given interger to binary(in string) with specified number of bits
	//padded 0 if required
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

		//generating an output stream to write into a file
		FileOutputStream file;
		try 
		{
			//creating a file at specified output location
			file = new FileOutputStream(objectProgramFile);

			//Start with writing the firstCodeAddress to the file
			//the address from where the instruction starts
			//bytebuffer() to alloate a memory of 4bytes
			//putInt() to convert the input int into binary representation
			//array() returns a byte array of whatever integer was input
			//write function writes it into the file
			file.write(ByteBuffer.allocate(4).putInt(ParsedProgram.firstCodeAddress).array());

			//For writing the data to the file
			for(int i=0; i<ParsedProgram.data.size();i++)
				file.write(ByteBuffer.allocate(4).putInt(ParsedProgram.data.get(i)).array());
			

			//assemble one instruction at a time, and write to the file
			for (generic.Instruction instruction : ParsedProgram.code) 
			{
				//initialise the string
				String binary_string_inst = "";
				
				//add the optcode to the string using HashMap
				binary_string_inst += opHashMap.get(instruction.getOperationType());

				//getting the program counter for calculating offsets for labels
				int pc = instruction.getProgramCounter();
				
				switch(instruction.getOperationType())
				{
					//R3I type
					case add : 
					case sub : 
					case mul : 
					case div : 
					case and : 
					case or : 
					case xor : 
					case slt : 
					case sll : 
					case srl : 
					case sra :{
						//all three operands are registers
						binary_string_inst += integerToBinaryString(instruction.getSourceOperand1().getValue(), 5);
						binary_string_inst += integerToBinaryString(instruction.getSourceOperand2().getValue(), 5);
						binary_string_inst += integerToBinaryString(instruction.getDestinationOperand().getValue(), 5);
						binary_string_inst += integerToBinaryString(0,12);
						break;	
					}
					//end operation
					case end:{
							binary_string_inst += integerToBinaryString(0,27);
							break;
					}
					//jmp operation
					case jmp:{
						//if jump to a register(due to the condition mentioned in ParsedProgram.java)
						if (instruction.destinationOperand.getOperandType() == Operand.OperandType.Register) 
						{
							binary_string_inst += integerToBinaryString(instruction.getDestinationOperand().getValue(), 5);
							binary_string_inst += integerToBinaryString(0, 22);
						} 
						// jmp to label
						else
						{
							//since there is no destination operand
							binary_string_inst += integerToBinaryString(0, 5);
							//subracting pc so that we can get the offset
							int offset = ParsedProgram.symtab.get(instruction.getDestinationOperand().getLabelValue()) - pc;
							//converting the offset into string
							String string_offset = integerToBinaryString(offset, 22);
							//since only 22 bits of space is available for encoding, we use this if..else block
							if(string_offset.length()>22)
								//this is because sometimes the length of the offset is larger tham 22.
								binary_string_inst += string_offset.substring(string_offset.length() - 22);
							else
								binary_string_inst += string_offset;
						}
						break;
					}
					//R2I Branch instruction
					case beq : 
					case bne : 
					case blt : 
					case bgt :{
						binary_string_inst += integerToBinaryString(instruction.getSourceOperand1().getValue(), 5);
						binary_string_inst += integerToBinaryString(instruction.getSourceOperand2().getValue(), 5);
						//subracting pc so that we can get the offset
						int offset =ParsedProgram.symtab.get(instruction.getDestinationOperand().getLabelValue()) - pc;
						//converting the offset into string
						String string_offset = integerToBinaryString(offset, 17);
						//since only 17 bits of space is available for encoding, we use this if..else block
						if(string_offset.length()>17)
							binary_string_inst += string_offset.substring(string_offset.length() - 17);
						else
							binary_string_inst += string_offset;
						break;
					}
					case addi :
					case subi :
					case muli :
					case divi : 
					case andi : 
					case ori : 
					case xori : 
					case slti : 
					case slli : 
					case srli : 
					case srai :
					case load :
					case store :{
						binary_string_inst += integerToBinaryString(instruction.getSourceOperand1().getValue(), 5);
						binary_string_inst += integerToBinaryString(instruction.getDestinationOperand().getValue(), 5);
						//no need to subtract pc since the second source operand is either an immediate or 
						//label with $ sign which convert the labe into offset
						binary_string_inst += integerToBinaryString(instruction.getSourceOperand2().getValue(), 17);
						break;
					}
				}
				// System.out.println(binary_string_inst);
				//Parsing the string into interger
				//using parseLong to prevent loss of precision
				int instInteger = (int) Long.parseLong(binary_string_inst, 2);
				//bytebuffer allocates 4 bytes into which the instInterger converted into binary is put
				//lastly array() returns the bytearray of corresponding interger which is written into the file
				//4byte array example[23,0,0,0]
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
