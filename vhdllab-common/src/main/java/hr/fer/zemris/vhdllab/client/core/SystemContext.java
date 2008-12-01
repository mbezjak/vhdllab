package hr.fer.zemris.vhdllab.client.core;

import java.awt.Frame;

/**
 * @author Miro Bezjak
 *
 */
public class SystemContext {
	
	private static final SystemContext INSTANCE = new SystemContext();
	
	private SystemContext() {
	}
	
	public static SystemContext instance() {
		return INSTANCE;
	}

	private String userId;
	private Frame frameOwner;
	private Environment environment;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Frame getFrameOwner() {
		return frameOwner;
	}
	public void setFrameOwner(Frame owner) {
		this.frameOwner = owner;
	}
    public Environment getEnvironment() {
        return environment;
    }
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
	
}
