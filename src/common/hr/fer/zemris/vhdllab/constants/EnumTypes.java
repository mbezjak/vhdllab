package hr.fer.zemris.vhdllab.constants;

public enum EnumTypes {
	
	FT_VHDL_SOURCE("vhdl_source", Role.CIRCUIT),
	FT_VHDL_AUTOMAT("vhdl_automat", Role.CIRCUIT),
	FT_VHDL_STRUCT_SCHEMA("vhdl_struct_schema", Role.CIRCUIT),
	FT_VHDL_TB("vhdl_tb", Role.TESTBENCH),
	FT_VHDL_SIMULATION("vhdl_simulation", Role.SIMULATION),
	FT_THEME("theme", Role.NOT_VHDL),
	FT_APPLET("applet", Role.NOT_VHDL),
	FT_COMMON("common", Role.NOT_VHDL);
	
	private enum Role {CIRCUIT, TESTBENCH, SIMULATION, NOT_VHDL};
	
	private String typeName;
	private Role role;
	
	private EnumTypes(String typeName, Role role) {
		this.typeName = typeName;
		this.role = role;
	}
	
	public String type() {
		return typeName;
	}
	
	public boolean isCircuit() {
		return role.equals(Role.CIRCUIT);
	}
	
	public boolean isTestbench() {
		return role.equals(Role.TESTBENCH);
	}
	
	public boolean isSimulation() {
		return role.equals(Role.SIMULATION);
	}

	public boolean isNotVHDL() {
		return role.equals(Role.NOT_VHDL);
	}

	@Override
	public String toString() {
		return type();
	}
}