package processor.pipeline;

import generic.Instruction;
import generic.Simulator;
import generic.Instruction.OperationType;
import processor.Processor;

public class RegisterWrite {
	Processor containingProcessor;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;
	
	public RegisterWrite(Processor containingProcessor, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType iF_EnableLatch)
	{
		this.containingProcessor = containingProcessor;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}
	
	public void performRW()
	{
		if(MA_RW_Latch.isRW_enable())
		{
			//TODO
			
			// if instruction being processed is an end instruction, remember to call Simulator.setSimulationComplete(true);
			Instruction inst =MA_RW_Latch.getInstruction();
			OperationType op = inst.getOperationType();

			switch(op){
				case end:
					Simulator.setSimulationComplete(true);
					break;
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
					int rd_value = inst.getDestinationOperand().getValue();
					containingProcessor.getRegisterFile().setValue(rd_value,MA_RW_Latch.getAluResult());
					break;
				}
				case load:{
					int rd_value = inst.getDestinationOperand().getValue();
					containingProcessor.getRegisterFile().setValue(rd_value,MA_RW_Latch.getLoadResult());
					break;
				}
				default:
					break;
			}

			MA_RW_Latch.setRW_enable(false);
			IF_EnableLatch.setIF_enable(true);
		}
	}

}
