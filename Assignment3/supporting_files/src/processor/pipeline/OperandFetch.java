package processor.pipeline;

import generic.Instruction.OperationType;
import generic.Operand.OperandType;

import processor.Processor;

import java.util.HashMap;

import generic.Instruction;

import generic.Operand;


public class OperandFetch {
	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;

	public static HashMap<String, OperationType> OpHashMap = new HashMap<String, OperationType>() {{
		put("00000", OperationType.add);
		put("00001", OperationType.addi);
		put("00010", OperationType.sub);
		put("00011", OperationType.subi);
		put("00100", OperationType.mul);
		put("00101", OperationType.muli);
		put("00110", OperationType.div);
		put("00111", OperationType.divi);
		put("01000", OperationType.and);
		put("01001", OperationType.andi);
		put("01010", OperationType.or);
		put("01011", OperationType.ori);
		put("01100", OperationType.xor);
		put("01101", OperationType.xori);
		put("01110", OperationType.slt);
		put("01111", OperationType.slti);
		put("10000", OperationType.sll);
		put("10001", OperationType.slli);
		put("10010", OperationType.srl);
		put("10011", OperationType.srli);
		put("10100", OperationType.sra);
		put("10101", OperationType.srai);
		put("10110", OperationType.load);
		put("10111", OperationType.store);
		put("11000", OperationType.jmp);
		put("11001", OperationType.beq);
		put("11010", OperationType.bne);
		put("11011", OperationType.blt);
		put("11100", OperationType.bgt);
		put("11101", OperationType.end);
	}};
	
	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
	}

	public int twos_complement(String immd)
	{
		String temp="";
		for(int i=0;i<immd.length();i++)
		{
			if(immd.charAt(i) == 1)
				temp = "0" + temp;
			else
				temp = "1" + temp;
		}
		int temp_int = Integer.parseInt(temp,2);
		temp_int +=1;
		temp_int *=-1;
		return temp_int;
	}
	
	public void performOF()
	{
		if(IF_OF_Latch.isOF_enable())
		{
			//TODO
			//reading the instruction
			String string_instruction = Integer.toString(IF_OF_Latch.getInstruction());

			//converting to instrcution to required length(if len <32)
			for(int i=string_instruction.length();i<=32;i++)
				string_instruction = '0' + string_instruction;

			//extracting the optcode of the instruction
			String optcode = string_instruction.substring(0, 5);
			OperationType optype = OpHashMap.get(optcode);
			Instruction inst = new Instruction();
			inst.setOperationType(optype);

			switch(optype)
			{
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
					Operand rs1 = new Operand();
					rs1.setOperandType(OperandType.Register);
					rs1.setValue(Integer.parseInt(string_instruction.substring(5,10),2));
					inst.setSourceOperand1(rs1);

					Operand rs2 = new Operand();
					rs2.setOperandType(OperandType.Register);
					rs2.setValue(Integer.parseInt(string_instruction.substring(10,15),2));
					inst.setSourceOperand2(rs2);

					Operand rd = new Operand();
					rd.setOperandType(OperandType.Register);
					rd.setValue(Integer.parseInt(string_instruction.substring(15,20),2));
					inst.setDestinationOperand(rd);

					break;	
				}
				//end operation
				case end:{
						break;
				}
				//jmp operation
				case jmp:{
					Operand lable = new Operand();
					String immd =  string_instruction.substring(10,32);
					int immx;
					if(immd.charAt(0)==0)
						immx = Integer.parseInt(immd,2);
					else
						immx = twos_complement(immd);
					
					if(immx!=0)
					{
						lable.setOperandType(OperandType.Immediate);
						lable.setValue(immx);
						inst.setDestinationOperand(lable);
					}
					else
					{
						lable.setOperandType(OperandType.Register);
						lable.setValue(Integer.parseInt(string_instruction.substring(6,10),2));
						inst.setDestinationOperand(lable);
					}
					break;
				}
				//R2I
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
					Operand rs1 = new Operand();
					rs1.setOperandType(OperandType.Register);
					rs1.setValue(Integer.parseInt(string_instruction.substring(5,10),2));
					inst.setSourceOperand1(rs1);

					Operand rs2 = new Operand();
					int immx;
					String immd =  string_instruction.substring(10,32);
					if(immd.charAt(0)==0)
						immx = Integer.parseInt(immd,2);
					else
						immx = twos_complement(immd);
					rs2.setOperandType(OperandType.Immediate);
					rs2.setValue(immx);
					inst.setSourceOperand2(rs2);

					Operand rd = new Operand();
					rd.setOperandType(OperandType.Register);
					rd.setValue(Integer.parseInt(string_instruction.substring(10,15),2));
					inst.setDestinationOperand(rd);
					break;

				}
				//R2I Branch instruction
				case beq : 
				case bne : 
				case blt : 
				case bgt :{
					Operand rs1 = new Operand();
					rs1.setOperandType(OperandType.Register);
					rs1.setValue(Integer.parseInt(string_instruction.substring(5,10),2));
					inst.setSourceOperand1(rs1);

					Operand rs2 = new Operand();
					rs2.setOperandType(OperandType.Register);
					rs2.setValue(Integer.parseInt(string_instruction.substring(10,15),2));
					inst.setSourceOperand2(rs2);

					Operand rd = new Operand();
					int immx;
					String immd =  string_instruction.substring(10,32);
					if(immd.charAt(0)==0)
						immx = Integer.parseInt(immd,2);
					else
						immx = twos_complement(immd);
					rd.setOperandType(OperandType.Immediate);
					rd.setValue(immx);
					inst.setDestinationOperand(rd);

					break;					
				}
					
			}
			OF_EX_Latch.setInsturction(inst);
			IF_OF_Latch.setOF_enable(false);
			OF_EX_Latch.setEX_enable(true);
		}
	}

}
