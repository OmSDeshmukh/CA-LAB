package generic;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.util.Stack;
import java.io.IOException;
import generic.Operand.OperandType;
import generic.ParsedProgram;
import generic.Instruction.OperationType;
import java.io.BufferedOutputStream;
// HELLO ITS ME MEMEM HEHEHEH EMEME 




public class Simulator {
	static FileInputStream inputcodestream = null ;
	public static Map<Instruction.OperationType , String> opcode = new HashMap<>(){{
		put(Instruction.OperationType.add , "00000");
		put(Instruction.OperationType.addi , "00001");
		put(Instruction.OperationType.sub , "00010");
		put(Instruction.OperationType.subi , "00011");
		put(Instruction.OperationType.mul , "00100");
		put(Instruction.OperationType.muli , "00101");
		put(Instruction.OperationType.div , "00110");
		put(Instruction.OperationType.divi , "00111");
		put(Instruction.OperationType.and , "01000");
		put(Instruction.OperationType.andi , "01001");
		put(Instruction.OperationType.or , "01011");
		put(Instruction.OperationType.ori , "01011");
		put(Instruction.OperationType.xor , "01100");
		put(Instruction.OperationType.xori , "01101");
		put(Instruction.OperationType.slt , "01110");
		put(Instruction.OperationType.slti , "01111");
		put(Instruction.OperationType.sll , "10000");
		put(Instruction.OperationType.slli , "10001");
		put(Instruction.OperationType.srl , "10010");
		put(Instruction.OperationType.srli , "10011");
		put(Instruction.OperationType.sra , "10100");		
		put(Instruction.OperationType.srai , "10101");
		put(Instruction.OperationType.load , "10110");
		put(Instruction.OperationType.store , "10111");
		put(Instruction.OperationType.jmp , "11000");
		put(Instruction.OperationType.beq , "11001");
		put(Instruction.OperationType.bne , "11010");
		put(Instruction.OperationType.blt , "11011");
		put(Instruction.OperationType.bgt , "11100");
		put(Instruction.OperationType.end , "11101");
	}};

	public static void setupSimulation(String assemblyProgramFile){
		int firstCodeAddress = ParsedProgram.parseDataSection(assemblyProgramFile);
		ParsedProgram.parseCodeSection(assemblyProgramFile, firstCodeAddress);
		ParsedProgram.printState();
	}

	private static String toBinary(int num , int length){
		String binary = String.format("%" + length + "s" , Integer.toBinaryString(num).replace(' ' , '0'));
		return binary ;
	}

	private static int toSignedInteger(String binary){
		int n = 32 - binary.length();
		char[] sign_ext = new char[n];
		Arrays.fill(sign_ext , binary.charAt(0));
		int signedInteger = (int) Long.parseLong(new String(sign_ext) + binary , 2);
		return signedInteger;
	}

	private static String toBinary(int n){
		Stack<Integer> bits = new Stack<>();
		do{
			bits.push(n%2);
			n /= 2 ;
		}while(n != 0);

		StringBuilder builder = new StringBuilder();
		while(n!=0){
			while(!bits.isEmpty()){
				builder.append(bits.pop());
			}
		}
		return " " + builder.toString();
	}

	private static String convert(Operand inst , int precesion){
		if(inst == null){
			return toBinary(0 , precesion);
		}
		if(inst.getOperandType() == Operand.OperandType.Label){
			return toBinary(ParsedProgram.symtab.get(inst.getLabelValue()) , precesion);
		}
		return toBinary(inst.getValue() , precesion);
	}

	public static void assemble(String objectProgramFile){
		try{
		//1.open file in binary mode 
		FileOutputStream file ;
		BufferedOutputStream bfile = new BufferedOutputStream(file);
		
		//2.wrtie firstcode adress to file 
		byte[] adressCode = ByteBuffer.allocate(4).putInt(ParsedProgram.firstCodeAddress).array();
		bfile.write(adressCode);
		
		//3.write data to file
		for(var value : ParsedProgram.data){
			byte[] dataValue = ByteBuffer.allocate(4).putInt(value).array();
			bfile.write(dataValue);
		}

		//4.assembling one by one instruction 
		for(var inst : ParsedProgram.code){
			String binary_line = "";
			binary_line += opcode.get(inst.getOperationType());
			int opCode = Integer.parseInt(binary_line, 2);

			int pc = inst.getProgramCounter();

			switch(inst.getOperationType()){
				//R3 type
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
				case sra : {
					binary_line += convert(inst.getSourceOperand1(), 5);
					binary_line += convert(inst.getSourceOperand2(), 5);
					binary_line += convert(inst.getDestinationOperand(), 5);
					binary_line += convert(0 , 12);
				}
				//R2I type
				case beq : 
				case bne : 
				case blt : 
				case bgt : {
					int val = Integer.parseInt(convert(inst.getDestinationOperand(),5) , 2) - pc ;
					binary_line += convert(inst.getSourceOperand1(),5);
					binary_line += convert(inst.getSourceOperand2(),5);
					String bin = toBinary(val , 17);
					binary_line += bin.substring(bin.length() -17);
				}
				//R2I type
				case addi : 
				case subi : 
				case muli : 
				case divi : 
				case andi : 
				case ori: 
				case xori : 
				case slti : 
				case slli : 
				case srli : 
				case srai :
				case load : 
				case store : {
					binary_line += convert(inst.getSourceOperand1(),5);
					binary_line += convert(inst.getDestinationOperand(), 5);
					binary_line += convert(inst.getSourceOperand2(),17);

				}
				// R1 type
				case jmp : {
					binary_line += toBinary(0 , 5);
					int val = Integer.parseInt(convert(inst.getDestinationOperand(),5) ,2 ) - pc ;
					String bin = toBinary(val , 22);
					binary_line += bin.substring(bin.length()-22);
				}
				// R1
				case end: {
					binary_line += toBinary(0 , 27);
				}
			}

			int InstInteger = (int) Long.parseLong(binary_line , 2);
			byte[] instBinary = ByteBuffer.allocate(4).putInt(InstInteger).array();
			bfile.write(instBinary); 

		}
		bfile.close();
		}
		catch(FileNotFoundException e){
			e.printStackTrace();
		}
		catch(IOException e){
			e.printStackTrace();
		}	
	}

}



















	
	// static FileInputStream inputcodeStream = null;
	// public static HashMap<OperationType, String> opHashMap = new HashMap<OperationType, String>(){{
	// 	put(OperationType.add, "00000");
	// 	put(OperationType.addi , "00001");
	// 	put(OperationType.sub , "00010");
	// 	put(OperationType.subi , "00011");
	// 	put(OperationType.mul , "00100");
	// 	put(OperationType.muli , "00101");
	// 	put(OperationType.div , "001100");
	// 	put(OperationType.divi , "00111");
	// 	put(OperationType.and , "01000");
	// 	put(OperationType.andi , "01001");
	// 	put(OperationType.or , "01010");
	// 	put(OperationType.ori , "01011");
	// 	put(OperationType.xor , "01100");
	// 	put(OperationType.xori , "01101");
	// 	put(OperationType.slt , "01110");
	// 	put(OperationType.slti , "01111");
	// 	put(OperationType.sll , "10000");
	// 	put(OperationType.slli , "10001");
	// 	put(OperationType.srl , "10010");
	// 	put(OperationType.srli , "10011");
	// 	put(OperationType.sra , "10100");
	// 	put(OperationType.srai , "10101");
	// 	put(OperationType.load, "10110");
	// 	put(OperationType.store, "10111");
	// 	put(OperationType.jmp, "11000");
	// 	put(OperationType.beq, "11001");
	// 	put(OperationType.bne, "11010");
	// 	put(OperationType.blt, "11011");
	// 	put(OperationType.bgt, "11100");
	// 	put(OperationType.end, "11101");
	// }};
		
	
	// //start of code for assembler
	// //Here we use the parseDataSection to get the address from where the code(instructions) start
	// //feed it to parseCodeSection
	// public static void setupSimulation(String assemblyProgramFile)
	// {	
	// 	int firstCodeAddress = ParsedProgram.parseDataSection(assemblyProgramFile);
	// 	ParsedProgram.parseCodeSection(assemblyProgramFile, firstCodeAddress);
	// 	ParsedProgram.printState();
	// }

	// public static String integerToBinaryString(int number, int numBits )
	// {
    //     // Convert the integer to a binary string
    //     String binary = Integer.toBinaryString(number);

    //     // Pad the binary string with leading zeros to achieve the desired bit length
    //     if (binary.length() < numBits) {
    //         int numLeadingZeros = numBits - binary.length();
    //         String leadingZeros = "0".repeat(numLeadingZeros);
    //         binary = leadingZeros + binary;
    //     }

    //     return binary;
    // }

	// public static void assemble(String objectProgramFile)
	// {
	// 	//TODO your assembler code
	// 	//1. open the objectProgramFile in binary mode
	// 	//2. write the firstCodeAddress to the file
	// 	//3. write the data to the file
	// 	//4. assemble one instruction at a time, and write to the file
	// 	//5. close the file

	// 	FileOutputStream file;
	// 	try 
	// 	{
	// 		file = new FileOutputStream(objectProgramFile);

	// 		//Start with writing the firstCodeAddress to the file
	// 		file.write(ByteBuffer.allocate(4).putInt(ParsedProgram.firstCodeAddress).array());

	// 		//For writing the data to the file
	// 		for(int i=0; i<ParsedProgram.data.size();i++)
	// 			file.write(ByteBuffer.allocate(4).putInt(ParsedProgram.data.get(i)).array());
			

	// 		//assemble one instruction at a time, and write to the file
	// 		for (generic.Instruction instruction : ParsedProgram.code) 
	// 		{
	// 			//initialise the string
	// 			String binary_string_inst = "";
				
	// 			//add the optcode to the string
	// 			binary_string_inst += opHashMap.get(instruction.operationType);

	// 			//Getting the interger value of the optcode for cases because the 
	// 			//optcode is designed in such a way that similar operations occur together
	// 			int op=Integer.parseInt(opHashMap.get(instruction.operationType),2);
	// 			int pc = instruction.getProgramCounter();
				

	// 			if (op <= 20 && op % 2 == 0) 
	// 			{
	// 				// R3 Type
	// 				binary_string_inst += integerToBinaryString(instruction.getSourceOperand1().getValue(), 5);
	// 				binary_string_inst += integerToBinaryString(instruction.getSourceOperand2().getValue(), 5);
	// 				binary_string_inst += integerToBinaryString(instruction.getDestinationOperand().getValue(), 5);
	// 				binary_string_inst += integerToBinaryString(0,12);
					
	// 			}

	// 			//end operation
	// 			else if (op == 29) 
	// 			{
	// 				binary_string_inst += integerToBinaryString(0,27);
	// 			}

	// 			else if (op == 24) 
	// 			{
	// 				// RI Type
	// 				//jmp operation
	// 				//no need of this block
	// 				if (instruction.destinationOperand.getOperandType() == Operand.OperandType.Register) 
	// 				{
	// 					binary_string_inst += integerToBinaryString(instruction.getDestinationOperand().getValue(), 5);
	// 					binary_string_inst += integerToBinaryString(0, 22);
	// 				} 
	// 				else 
	// 				//solve tomorrow
	// 				{
	// 					binary_string_inst += integerToBinaryString(0, 5);
	// 					int value = Integer.parseInt(integerToBinaryString(ParsedProgram.symtab.get(instruction.getDestinationOperand().getLabelValue()), 5), 2) - pc;
	// 					//still don't understand these 2 steps
	// 					String extra = integerToBinaryString(value, 22);
	// 					binary_string_inst += extra.substring(extra.length() - 22);
	// 				}
	// 			}

	// 			else 
	// 			{
	// 				// R2I Type
	// 				if (op >= 25 && op <= 28) 
	// 				//solve tomorrow
	// 				//not understanding the need of the step
	// 				{
	// 					binary_string_inst += integerToBinaryString(instruction.getSourceOperand1().getValue(), 5);
	// 					binary_string_inst += integerToBinaryString(instruction.getSourceOperand2().getValue(), 5);
	// 					//don't understand these 3 steps
	// 					int value = Integer.parseInt(integerToBinaryString(ParsedProgram.symtab.get(instruction.getDestinationOperand().getLabelValue()), 5), 2) - pc;
	// 					String extra = integerToBinaryString(value, 17);
	// 					binary_string_inst += extra.substring(extra.length() - 17);
	// 				} 
	// 				else 
	// 				{
	// 					//don't understand these 3 steps
	// 					binary_string_inst += integerToBinaryString(instruction.getSourceOperand1().getValue(), 5);
	// 					binary_string_inst += integerToBinaryString(instruction.getDestinationOperand().getValue(), 5);
	// 					binary_string_inst += integerToBinaryString(instruction.getSourceOperand2().getValue(), 17);
	// 				}
	// 			}
	// 			int instInteger = (int) Long.parseLong(binary_string_inst, 2);
	// 			byte[] instBinary = ByteBuffer.allocate(4).putInt(instInteger).array();
	// 			file.write(instBinary);
	// 		}
	// 		file.close();
	// 	}
	// 	catch (FileNotFoundException e)
	// 	{
	// 		e.printStackTrace();
	// 	} 
	// 	catch (IOException e) 
	// 	{
	// 		e.printStackTrace();
			
	// 	}
		
	// }
	