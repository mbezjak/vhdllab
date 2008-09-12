package hr.fer.zemris.vhdllab.applets.editor.newtb.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;

/**
 * @author Ivan Brkic
 * @version 1.02
 * @since 15. July 2008.
 */
public class ImpulsImageComponent extends JComponent {
	private static final long serialVersionUID = 4329423254661434581L;

//	private String scale;
	private int clockTimeHigh;
	private int clockTimeLow;
	private int inputSetupTime;
	
//	public void setScale(String scale){
//		this.scale = scale;
//	}
	
	
	public void setClockTimeHigh(int clockTimeHigh) {
		this.clockTimeHigh = clockTimeHigh;
	}


	public void setClockTimeLow(int clockTimeLow) {
		this.clockTimeLow = clockTimeLow;
	}


	public void setInputSetupTime(int inputSetupTime) {
		this.inputSetupTime = inputSetupTime;
	}
	
	private boolean risingEdge = true;
	
	public void setRisingEdge(){
		this.risingEdge = true;
	}
	
	public void setFallingEdge(){
		this.risingEdge = false;
	}
	
	public void setRisingEdge(boolean b){
		this.risingEdge = b;
	}
	
	private boolean combinatorial = true;
	
	public void setCombinatorial(){
		this.combinatorial = true;
	}
	
	public void setSingleClock(){
		this.combinatorial = false;
	}
	
	public void setCombinatorial(boolean b){
		this.combinatorial = b;
	}
	
	private int checkedOutputs;
	
	public void setCheckedOutputs(int checkedOutputs){
		this.checkedOutputs = checkedOutputs;
	}
	
	private int assignInputs;
	
	public void setAssignInputs(int assignInputs){
		this.assignInputs = assignInputs;
	}
	
	private int width = 665;
	private int height = 200;
	
	/** default constuctor */
	public ImpulsImageComponent() {
	
		this.setPreferredSize(new Dimension(width, height));
		setVisible(true);
	}

	/** constructor which sets default values to current dialog's values */
	public ImpulsImageComponent(InitTimingDialog dialog) {
		this.setPreferredSize(new Dimension(width, height));
		
		this.clockTimeHigh = dialog.getClockTimeHigh();
		this.clockTimeLow = dialog.getClockTimeLow();
		this.inputSetupTime = dialog.getInputSetupTime();
		
		this.checkedOutputs = dialog.getCheckOutputs();
		this.assignInputs = dialog.getAssignInputs();
		
		setVisible(true);
	}
	
	/** a value of height indents and voids*/
	private int indentH = 10;
	/** a value of width indents and voids*/
	private int indentW = 150;
	/** height of the impuls image */
	private int impulsImageHeight = 140;
	/** width of the impuls image */
	private int impulsImageWidth = width - 2*indentW;
	
	/** used for creating arrow */
	private int triangleHeight = 10;

	/** used for combinatorial */
	private int imageIndentW;
	/** string height */
	private int stringHeight;
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponents(g);
		
		imageIndentW = g.getFontMetrics().stringWidth("Outputs")/2;
		stringHeight = g.getFontMetrics().getHeight();
		
//		g.setColor(Color.white);
//		g.fillRect(0, 0, width, height);
		
		if (this.combinatorial){
			g.setColor(Color.BLACK);
			int sum = checkedOutputs + assignInputs;
				
			int a = Math.round(checkedOutputs * (impulsImageWidth- 2*imageIndentW) / sum);
			int b = Math.round(assignInputs * (impulsImageWidth- 2*imageIndentW) / sum);
			int impulsEndH = height - indentH;
			int impulsStartH = impulsEndH - impulsImageHeight;
			int impulsEndW = width - indentW;
			
			g.drawLine(indentW, impulsEndH, impulsEndW, impulsEndH);

			Color green = Color.GREEN.darker().darker();
			
			writeStringOnTopArrow(g, "Assign", "Inputs", green, indentW+imageIndentW);
			writeStringOnTopArrow(g, "Assign", "Inputs", green, width-indentW-imageIndentW);

			writeStringOnTopArrow(g, "Check", "Outputs", Color.BLUE, indentW+imageIndentW+a);
			
			drawColorArrow(g, (impulsStartH+impulsEndH)/2, indentW+imageIndentW, a, "Wait To Check", Color.BLUE);
			drawColorArrow(g, (impulsStartH+impulsEndH)/2, indentW+imageIndentW+a, b, "Wait To Assign", green);
			
		}else{ // Single Clock *************************
			g.setColor(Color.BLACK);
			
			int sum = clockTimeHigh + clockTimeLow;
			int impulsEndH = impulsImageHeight + indentH;
			int impulsEndW = width - indentW;
			
			int a;
			int c = Math.round(inputSetupTime * impulsImageWidth / sum);
			
			int bottomFieldHeight = height - impulsImageHeight - 3*indentH;
			int bottomColorArrowHeight = height-bottomFieldHeight/2-indentH;
			
			if (risingEdge){
// RISING EDGE ***********************				
				drawTriangle(g, indentW, indentH, 
						indentW-5, indentH+triangleHeight, 
						indentW+5, indentH+triangleHeight);
				drawTriangle(g, width-indentW, indentH, 
						impulsEndW-5, indentH+triangleHeight, 
						impulsEndW+5, indentH+triangleHeight);
				
				g.drawLine(indentW, indentH+triangleHeight, indentW, impulsEndH);
				g.drawLine(impulsEndW, indentH+triangleHeight, impulsEndW, impulsEndH);
				
				a = Math.round(clockTimeHigh * impulsImageWidth / sum);
				int b = Math.round(clockTimeLow * impulsImageWidth / sum);
				
				g.drawLine(indentW, indentH, indentW+a, indentH);
				g.drawLine(indentW+a, indentH, indentW+a, impulsEndH);
				g.drawLine(indentW+a, impulsEndH, impulsEndW, impulsEndH);
				
				drawColorArrow(g, bottomColorArrowHeight, indentW, a, 
						"Clock Time High", Color.MAGENTA);
				drawColorArrow(g, bottomColorArrowHeight, indentW+a, b, 
						"Clock Time Low", Color.BLUE);
				
				
			}else{
//	FALLING EDGE ********************************			
				drawTriangle(g, indentW, impulsEndH, 
						indentW-5, impulsEndH-triangleHeight, 
						indentW+5, impulsEndH-triangleHeight);
				drawTriangle(g, width-indentW, impulsEndH, 
						impulsEndW-5, impulsEndH-triangleHeight, 
						impulsEndW+5, impulsEndH-triangleHeight);
				
				g.drawLine(indentW, indentH, indentW, impulsEndH-triangleHeight);
				g.drawLine(width-indentW, indentH, impulsEndW, impulsEndH-triangleHeight);
				
				a = Math.round(clockTimeLow * impulsImageWidth / sum);
				int b = Math.round(clockTimeHigh * impulsImageWidth / sum);
				
				g.drawLine(indentW, impulsEndH, indentW+a, impulsEndH);
				g.drawLine(indentW+a, impulsEndH, indentW+a, indentH);
				g.drawLine(indentW+a, indentH, impulsEndW, indentH);
				
				drawColorArrow(g, bottomColorArrowHeight, indentW, a, 
						"Clock Time Low", Color.MAGENTA);
				drawColorArrow(g, bottomColorArrowHeight, indentW+a, b, 
						"Clock Time High", Color.BLUE);
			}
			
			drawColorArrow(g, impulsEndH/2, impulsEndW-c, c, 
					"Input Setup Time", Color.GRAY);
			
			g.setColor(Color.GRAY);
			g.drawLine(indentW, impulsEndH+indentH, indentW, height-indentH);
			g.drawLine(indentW+a, impulsEndH+indentH, indentW+a, height-indentH);
			g.drawLine(impulsEndW, impulsEndH+indentH, impulsEndW, height-indentH);
			g.drawLine(impulsEndW-c, 2*indentH, impulsEndW-c, impulsEndH-indentH);
		}
		
	}
	
	/** draws triangle*/
	private void drawTriangle(Graphics g, int x1, int y1, int x2, int y2, int x3, int y3){
		g.drawLine(x1, y1, x2, y2);
		g.drawLine(x1, y1, x3, y3);
		g.drawLine(x3, y3, x2, y2);
	}
	
	private int triangleBase = 10;
	
	private void drawColorArrow(Graphics g, int y, int x, int width, String text, Color c){
		g.setColor(c);
		int stringWidth = g.getFontMetrics().stringWidth(text);
		
		if (stringWidth + 10 + 2*triangleHeight + 10 < width){
			g.drawString(text, x+(width-stringWidth)/2, y+stringHeight/4);
			
			fillTriangle(g, x, y, 1);
			fillTriangle(g, x + width, y, 2);
			
			int tempWidth = (width-stringWidth)/2 - 10 -triangleHeight;
			int tempHeight = triangleBase*2/3;
			int tempY = y-triangleBase/3;
			g.fillRect(x+5+triangleHeight, tempY, tempWidth, tempHeight);
			g.fillRect(x+width-5-triangleHeight-tempWidth, tempY, tempWidth, tempHeight);
			
		}else if(width > 10 + 2*triangleHeight){
			fillTriangle(g, x, y, 1);
			fillTriangle(g, x + width, y, 2);
			
			int tempWidth = width - 10 -2*triangleHeight;
			int tempHeight = triangleBase*2/3;
			int tempY = y-triangleBase/3;
			g.fillRect(x+5+triangleHeight, tempY, tempWidth, tempHeight);
			
		}
	}
	
	/**
	 *
	 * @param type
	 * 1 - left
	 * 2 - right
	 * 3 - bottom (given the lowest end)
	 */
	private void fillTriangle(Graphics g, int x, int y, int type){
		int[] xField = new int [3];
		int[] yField = new int [3];
		yField[0] = y;
		
		if (type==1){
			xField[0] = x+5;
			xField[1] = xField[2] = xField[0] + triangleHeight;
			yField[1] = y - triangleBase/2;
			yField[2] = y + triangleBase/2;
		}else if(type==2){
			xField[0] = x - 5;
			xField[1] = xField[2] = xField[0] - triangleHeight;
			yField[1] = y - triangleBase/2;
			yField[2] = y + triangleBase/2;
		}else if(type==3){
			xField[0] = x;
			xField[1] = x - triangleBase/2;
			xField[2] = x + triangleBase/2;
			yField[1] = yField[2] = y - triangleHeight;
		}
		g.fillPolygon(xField, yField, 3);
	}
	
	private void writeStringOnTopArrow(Graphics g, String top, String bottom, Color c, int x){
		g.setColor(c);
		
		int stringWidth = g.getFontMetrics().stringWidth(top)/2;
		g.drawString(top, x-stringWidth, indentH+stringHeight/2);
		
		stringWidth = g.getFontMetrics().stringWidth(bottom)/2;
		g.drawString(bottom, x-stringWidth, indentH+3*stringHeight/2);
		
		g.drawLine(x, indentH+2*stringHeight, x, height-indentH-triangleHeight);
		fillTriangle(g, x, height-indentH, 3);
	}

}
