/**
 * 
 */
package hr.fer.zemris.vhdllab.applets.main;

import java.awt.Frame;

/**
 * @author Miro Bezjak
 *
 */
public class SystemContext {

	private static String userId;
	private static Frame frameOwner;
	
	public static String getUserId() {
		return userId;
	}
	public static void setUserId(String userId) {
		SystemContext.userId = userId;
	}
	public static Frame getFrameOwner() {
		return frameOwner;
	}
	public static void setFrameOwner(Frame owner) {
		SystemContext.frameOwner = owner;
	}
	
}
