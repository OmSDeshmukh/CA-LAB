package processor.pipeline;

import processor.Processor;

public class InstructionFetch {
	
	Processor containingProcessor;
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;
	EX_IF_LatchType EX_IF_Latch;
	
	public InstructionFetch(Processor containingProcessor, IF_EnableLatchType iF_EnableLatch, IF_OF_LatchType iF_OF_Latch, EX_IF_LatchType eX_IF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_EnableLatch = iF_EnableLatch;
		this.IF_OF_Latch = iF_OF_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}
	
	public void performIF()
	{	
		
		if(!IF_EnableLatch.isFreeze())//provided no datalock
		{
			if(EX_IF_Latch.isIF_enable())
			{
				containingProcessor.getRegisterFile().setProgramCounter(EX_IF_Latch.getPC()-1);
				EX_IF_Latch.setIF_enable(false);
				IF_OF_Latch.setOF_enable(false);
				// System.out.println("IF: PC set to " + EX_IF_Latch.getPC());

			} // if EX_IF_Latch is enabled, set PC to EX_IF_Latch's PC and wait for next cycle (1 nop)
			else if(IF_EnableLatch.isIF_enable() || EX_IF_Latch.isIF_enable())
			{
				int currentPC = containingProcessor.getRegisterFile().getProgramCounter();
				int newInstruction = containingProcessor.getMainMemory().getWord(currentPC);
				IF_OF_Latch.setInstruction(newInstruction);
				containingProcessor.getRegisterFile().setProgramCounter(currentPC + 1);
				
				IF_EnableLatch.setIF_enable(true);
				IF_OF_Latch.setOF_enable(true);
			}
		}
		else{
			IF_EnableLatch.setFreeze(false);
		}
	}

}
