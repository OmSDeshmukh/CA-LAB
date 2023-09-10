package processor.pipeline;

import generic.Instruction;

public class MA_RW_LatchType {
	
	boolean RW_enable;
	Instruction instruction;
	int AluResult;
	int LoadResult;

	public void setAluResult(int AluResult) {
		this.AluResult = AluResult;
	}

	public int getAluResult(){
		return this.AluResult;
	}

	public void setLoadResult(int LoadResult) {
		this.LoadResult = LoadResult;
	}

	public int getLoadResult(){
		return this.LoadResult;
	}

	public void setInstruction(Instruction instruction) {
		this.instruction = instruction;
	}

	public Instruction getInstruction() {
		return instruction;
	}
	
	//
	public MA_RW_LatchType()
	{
		RW_enable = false;
	}

	public boolean isRW_enable() {
		return RW_enable;
	}

	public void setRW_enable(boolean rW_enable) {
		RW_enable = rW_enable;
	}

}
