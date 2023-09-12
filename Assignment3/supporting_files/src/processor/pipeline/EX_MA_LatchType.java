package processor.pipeline;
import generic.Operand;
import generic.Instruction;

public class EX_MA_LatchType {
	
	boolean MA_enable;
	Operand op2 ;
	Instruction instruction ;
	int alu_result ;
	
	public EX_MA_LatchType()
	{
		MA_enable = false;
	}

	public boolean isMA_enable() {
		return MA_enable;
	}

	public void setMA_enable(boolean mA_enable) {
		MA_enable = mA_enable;
	}

	public void setALUResult(int alu_result){
		this.alu_result = alu_result;
	}

	public int getALUResult(){
		return alu_result ;
	}

	public Instruction geInstruction(){
		return instruction;
	}

	public void setInstruction(Instruction instruction){
		this.instruction = instruction;
	}

	public void setOp2(Operand op2){
		this.op2 = op2 ;
	}

	public Operand getOp2(){
		return op2 ;
	}

	

}
