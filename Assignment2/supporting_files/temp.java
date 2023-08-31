package generic;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.HashMap;

import generic.Operand.OperandType;


public class Simulator {

	static FileInputStream inputcodeStream = null;

	public static void setupSimulation(String assemblyProgramFile) {
		int firstCodeAddress = ParsedProgram.parseDataSection(assemblyProgramFile);
		ParsedProgram.parseCodeSection(assemblyProgramFile, firstCodeAddress);
		ParsedProgram.printState();
	}

	public static void assemble(String objectProgramFile) {
		FileOutputStream file;
		try {
			file = new FileOutputStream(objectProgramFile);
			String ans = "";
			HashMap<String, String> opcodeMap = new HashMap<>();
			opcodeMap.put("add", "00000");
			opcodeMap.put("addi", "00001");
			opcodeMap.put("sub", "00010");
			opcodeMap.put("subi", "00011");
			opcodeMap.put("mul", "00100");
			opcodeMap.put("muli", "00101");
			opcodeMap.put("div", "00110");
			opcodeMap.put("divi", "00111");
			opcodeMap.put("and", "01000");
			opcodeMap.put("andi", "01001");
			opcodeMap.put("or", "01010");
			opcodeMap.put("ori", "01011");
			opcodeMap.put("xor", "01100");
			opcodeMap.put("xori", "01101");
			opcodeMap.put("slt", "01110");
			opcodeMap.put("slti", "01111");
			opcodeMap.put("sll", "10000");
			opcodeMap.put("slli", "10001");
			opcodeMap.put("srl", "10010");
			opcodeMap.put("srli", "10011");
			opcodeMap.put("sra", "10100");
			opcodeMap.put("srai", "10101");
			opcodeMap.put("load", "10110");
			opcodeMap.put("store", "10111");
			opcodeMap.put("jmp", "11000");
			opcodeMap.put("beq", "11001");
			opcodeMap.put("bne", "11010");
			opcodeMap.put("blt", "11011");
			opcodeMap.put("bgt", "11100");
			opcodeMap.put("end", "11101");
			//TODO your assembler code
			//1. open the objectProgramFile in binary mode
			//2. write the firstCodeAddress to the file
			//3. write the data to the file
			//4. assemble one instruction at a time, and write to the file
			//5. close the file
			int x = ParsedProgram.firstCodeAddress;
			file.write(ByteBuffer.allocate(4).putInt(x).array());
			for (int i = 0; i < ParsedProgram.data.toArray().length; i++) {
				x = ParsedProgram.data.get(i);
				file.write(ByteBuffer.allocate(4).putInt(x).array());
			}
			for (int i = 0; i < ParsedProgram.code.toArray().length; i++) {
				ans = "";
				Instruction current = ParsedProgram.code.get(i);
				int programCounter = current.getProgramCounter();
				if (current.getOperationType() == Instruction.OperationType.end) {
					ans += (String.format("%-32s", opcodeMap.get(current.operationType.toString())).replaceAll(" ", "0"));
				}
				switch (current.getOperationType()) {
					case add:
					case sub:
					case mul:
					case div:
					case and:
					case or:
					case xor:
					case slt:
					case sll:
					case srl:
					case sra: {
						String R3 = opcodeMap.get(current.operationType.toString());
						x = current.getSourceOperand1().getValue();
						R3 += String.format("%5s", Integer.toBinaryString(x)).replaceAll(" ", "0");
						x = current.getSourceOperand2().getValue();
						R3 += String.format("%5s", Integer.toBinaryString(x)).replaceAll(" ", "0");
						x = current.getDestinationOperand().getValue();
						R3 += String.format("%5s", Integer.toBinaryString(x)).replaceAll(" ", "0");
						while (R3.length() < 32) {
							R3 += "0";
						}
						ans += (R3);
						break;
					}
					case addi:
					case subi:
					case muli:
					case divi:
					case andi:
					case ori:
					case xori:
					case slti:
					case slli:
					case srli:
					case srai:
					case load:
					case store: {
						String R2I = opcodeMap.get(current.operationType.toString());
						x = current.getSourceOperand1().getValue();
						R2I += String.format("%5s", Integer.toBinaryString(x)).replaceAll(" ", "0");
						x = current.getDestinationOperand().getValue();
						R2I += String.format("%5s", Integer.toBinaryString(x)).replaceAll(" ", "0");
						x = current.getSourceOperand2().getValue();
						R2I += String.format("%17s", Integer.toBinaryString(x)).replaceAll(" ", "0");
						ans += (R2I);
						break;
					}
					case beq:
					case bne:
					case blt:
					case bgt: {
						String R2I_t2 = opcodeMap.get(current.operationType.toString());
						x = current.getSourceOperand1().getValue();
						R2I_t2 += String.format("%5s", Integer.toBinaryString(x)).replaceAll(" ", "0");
						x = current.getSourceOperand2().getValue();
						R2I_t2 += String.format("%5s", Integer.toBinaryString(x)).replaceAll(" ", "0");
						x = ParsedProgram.symtab.get(current.getDestinationOperand().getLabelValue());
						x -= programCounter;
//						R2I_t2 += String.format("%17s", Integer.toBinaryString(x)).replaceAll(" ", "0");

						String binaryString = Integer.toBinaryString(x);
						if (binaryString.length() > 17) {
							binaryString = binaryString.substring(binaryString.length() - 17);
						} else {
							binaryString = String.format("%17s", binaryString).replaceAll(" ", "0");
						}
						R2I_t2 += binaryString;
						System.out.println("17 bits : " + binaryString);
						ans += (R2I_t2);
						break;
					}
					case jmp: {
						String RI = opcodeMap.get(current.operationType.toString());
						if (current.destinationOperand.getOperandType() == Operand.OperandType.Register)
						{
							x = current.getDestinationOperand().getValue();
							RI+= String.format("%5s", Integer.toBinaryString(x)).replaceAll(" ", "0");
							RI += String.format("%22s", Integer.toBinaryString(0)).replaceAll(" ", "0");
						}
						// jmp to label
						else
						{
							RI+="00000";
							x = ParsedProgram.symtab.get(current.getDestinationOperand().getLabelValue()) - programCounter;
							String binaryString = Integer.toBinaryString(x);
							if (binaryString.length() > 22) {
								binaryString = binaryString.substring(binaryString.length() - 22);
							} else {
								binaryString = String.format("%22s", binaryString).replaceAll(" ", "0");
							}
							RI += binaryString;
						}

//						x = current.getDestinationOperand().getValue();
//						RI += String.format("%5s", Integer.toBinaryString(x)).replaceAll(" ", "0");
//						x = ParsedProgram.symtab.get(current.getDestinationOperand().getLabelValue());
//						x -= programCounter;
//						RI += String.format("%22s", Integer.toBinaryString(x)).replaceAll(" ", "0");
						ans += (RI);
						break;
					}
				}
				System.out.println(ans);
				int instInteger = (int) Long.parseLong(ans, 2);
				byte[] instBinary = ByteBuffer.allocate(4).putInt(instInteger).array();
				file.write(instBinary);
			}
			file.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

}