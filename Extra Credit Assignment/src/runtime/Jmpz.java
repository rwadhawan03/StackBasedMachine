package runtime;

public class Jmpz extends Instruction {
	private String targetLabel;

	Jmpz(int code, String mnemonic, String target) {
		super(code, mnemonic);
		this.targetLabel = target;
	}

	public String getTargetLabel() {
		return targetLabel;
	}

	public void setTargetLabel(String targetLabel) {
		this.targetLabel = targetLabel;
	}

	public String toString() {
		return super.toString() + " " + targetLabel;
	}

}
