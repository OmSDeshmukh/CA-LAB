package processor.pipeline;
// UNIQUE
import java.lang.foreign.ValueLayout.OfAddress;

import processor.Processor;
import generic.Instruction;
import generic.Instruction.OperationType;
import generic.Simulator;
import generic.Operand.OperandType;

public class Execute {
	Processor containingProcessor;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;
	
	public Execute(Processor containingProcessor, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch, EX_IF_LatchType eX_IF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}
	
	public void performEX()
	{
		//TODO
		if(OF_EX_Latch.isEX_enable()){
			int op1 = OF_EX_Latch.getOp1();
			int op2 = OF_EX_Latch.getOp2();
			int imm = OF_EX_Latch.getImm();
			Instruction inst = OF_EX_Latch.getInstruction();
			int pc = containingProcessor.getRegisterFile().getProgramCounter();
			int alu_result = 0 ;
			OperationType operation = OF_EX_Latch.getInstruction().getOperationType();
			boolean nom = false ;
			switch(operation){
				case add :{
					alu_result = op1 + op2 ;
					break;
				}
				case sub :{
					alu_result = op1 - op2 ;
					break ;
				}
				case mul :{ 
					alu_result = op1 * op2 ;
					break ;
				}
				case div : {
					alu_result = op1 / op2 ;
					containingProcessor.getRegisterFile().setValue(31, op1%op2);
					break;
				}
				case addi :{ 
					alu_result = op1 + imm ;
					break ;
				}
				case subi :{
					alu_result = op1 - imm ;
					break ;
				}
				case muli :{
					alu_result = op1 * imm ;
					break ;
				}
				case divi : {
					alu_result = op1 / imm ;
					containingProcessor.getRegisterFile().setValue(31, op1%imm);
					break;
				}
				case and : {
					alu_result = op1 & op2 ; 
					break ;
				}
				case or : {
					alu_result = op1 | op2 ; 
					break ;
				}
				case xor : {
					alu_result = op1 ^ op2 ; 
					break ;
				}
				case andi : {
					alu_result = op1 & imm ; 
					break ;
				}
				case ori :{
					alu_result = op1 & imm ; 
					break ;
				}
				case xori :{
					alu_result = op1 ^ imm ; 
					break ;
				}
				case slt :{
					alu_result = (op1 < op2) ?1 : 0;
					break ;
				}
				case sll :{
					alu_result = op1 << op2 ;
					containingProcessor.getRegisterFile().setValue(31, (int) Math.pow(2, op2));
					break ;
				}
				case srl :{
					alu_result = op1 >>> op2 ;
					containingProcessor.getRegisterFile().setValue(31, (1<<(op2-1)) & op1);
					break ;
				}
				case sra : {
					alu_result = op1 >> op2 ;
					containingProcessor.getRegisterFile().setValue(31 ,(1 << (op2 - 1)) & op1);
					break ;
				}
				case slti : {
					alu_result = (op1 < imm) ?1 : 0;
					break ;
				}
				case slli :{
					alu_result = op1 << imm ;
					containingProcessor.getRegisterFile().setValue(31, (int) Math.pow(2, imm));
					break ;
				}
				case srli :{
					alu_result = op1 >>> imm ;
					containingProcessor.getRegisterFile().setValue(31, (1<<(imm-1)) & op1);
					break ;
				}
				case srai :{
					alu_result = op1 >> imm;
					containingProcessor.getRegisterFile().setValue(31 , (1 << (imm- 1)) & op1);
					break ;
				}
				case load : {
					alu_result = op1 + imm ;
					break ;
				}
				case store :{
					alu_result = op2 + imm ;
					break ;
				}
				case jmp :{
					OperandType opType = inst.getSourceOperand1().getOperandType();
					if(opType == OperandType.Register){
						imm = containingProcessor.getRegisterFile().getValue(inst.getSourceOperand1().getValue());
					}
					else{
						imm = OF_EX_Latch.getImm();
					}
					alu_result = pc + imm ;
					EX_IF_Latch.setIF_enable(true);
					EX_IF_Latch.setPC(alu_result);
					nom = true ;
					break ;
				}
				case beq :{
					if(op1 == op2){
						alu_result = pc + imm ;
						EX_IF_Latch.setIF_enable(true);
						EX_IF_Latch.setPC(alu_result);
						nom = true ;
					}
					break ;
				}
				case bne :{
					if(op1 != op2){
						alu_result = pc + imm ;
						EX_IF_Latch.setIF_enable(true);
						EX_IF_Latch.setPC(alu_result);
						nom = true ;
					}
					break ;
				}
				case blt :{
					if(op1 < op2){
						alu_result = pc + imm ;
						EX_IF_Latch.setIF_enable(true);
						EX_IF_Latch.setPC(alu_result);
						nom = true ;
					}
					break ;
				}
				case bgt :{
					if(op1 > op2){
						alu_result = pc + imm ;
						EX_IF_Latch.setIF_enable(true);
						EX_IF_Latch.setPC(alu_result);
						nom = true ;
					}
					break ;
				}
				case end : {
					Simulator.setSimulationComplete(true);
				}
				default : {break ;}
			}
			EX_MA_Latch.setInstruction(OF_EX_Latch.getInstruction());
			EX_MA_Latch.setALUResult(alu_result);
			if(nom){
				EX_MA_Latch.setMA_enable(false);
			}
			else{
				EX_MA_Latch.setMA_enable(true);
			}
			OF_EX_Latch.setEX_enable(false);
		}
	}

}
