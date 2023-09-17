package processor.pipeline;
// KARAYCHAY
import processor.Processor;
import generic.Instruction;
import generic.Instruction.OperationType;

public class MemoryAccess {
	Processor containingProcessor;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	
	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
	}
	
	public void performMA()
	{
		//TODO
		if(EX_MA_Latch.isMA_enable()){
			
			Instruction inst = EX_MA_Latch.geInstruction();
			OperationType opty = inst.getOperationType();
			int alu_res = EX_MA_Latch.getALUResult();
			MA_RW_Latch.setAluResult(alu_res);
			
			if(opty == OperationType.store){
				int st_val = containingProcessor.getRegisterFile().getValue(inst.getSourceOperand1().getValue());
				containingProcessor.getMainMemory().setWord(alu_res, st_val);
			}
			else if(opty == OperationType.load){
				int ld_val = containingProcessor.getMainMemory().getWord(alu_res);
				MA_RW_Latch.setLoadResult(ld_val);
			}

			MA_RW_Latch.setInstruction(inst);
			MA_RW_Latch.setRW_enable(true);
			EX_MA_Latch.setMA_enable(false);

		}
	}

}