package hr.fer.zemris.vhdllab.applets.editor.schema2.dummies;

import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IGenericValue;







public class DummyGenericValue implements IGenericValue {
	
	private Float floatek = 0.f;
	

    public IGenericValue copyCtor() {
    	DummyGenericValue dgv = new DummyGenericValue();
    	dgv.floatek = this.floatek;
    	return dgv;
    }

    public void deserialize(String code) {
		floatek = Float.parseFloat(code);
    }

    public String serialize() {
		return floatek.toString();
    }

	@Override
	public String toString() {
		return floatek.toString();
	}
    
    
}








