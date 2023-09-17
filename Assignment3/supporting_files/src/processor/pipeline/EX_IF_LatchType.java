package processor.pipeline;
// ZALAY !!
public class EX_IF_LatchType {
	boolean IF_enable ;
	int pc ;
	public EX_IF_LatchType()
	{
		IF_enable = false ;	
	}
	
	public int getPC(){
		return pc ;
	}

	public void setPC(int pc){
		this.pc = pc ;
	}

	public boolean isIF_enable(){
		return IF_enable ;
	}

	public void setIF_enable(boolean IF_enable){
		this.IF_enable = IF_enable ;
	}
}
