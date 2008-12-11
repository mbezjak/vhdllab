package hr.fer.zemris.vhdllab.applets.editor.newtb.view;

import hr.fer.zemris.vhdllab.api.vhdl.CircuitInterface;
import hr.fer.zemris.vhdllab.api.vhdl.Port;
import hr.fer.zemris.vhdllab.api.vhdl.PortDirection;
import hr.fer.zemris.vhdllab.api.vhdl.TypeName;
import hr.fer.zemris.vhdllab.applets.editor.newtb.help.HelpManager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.Border;

/**
 * @author Ivan Brki�
 * @version 1.05
 * @since 15. July 2008.
 */

public class InitTimingDialog extends JDialog {
	private static final long serialVersionUID = 130368030719609828L;
	
	/** Size of a border */
	private static final int BORDER = 10;
	/** Width of all buttons */
	private static final int BUTTON_WIDTH = 70;
	/** Height of all buttons */
	private static final int BUTTON_HEIGHT = 24;
	
	/** Owner of this dialog (used to enable modal dialog) */
	private Component owner;
	
	/** JTextField that represents Initial Lenght of Testbench */
	private JTextField initialLengthOfTestbench = new JTextField("1000");
	private JLabel initialLengthOfTestbenchScale = new JLabel();
	private JLabel initialLengthOfTestbenchLabel = new JLabel("Initial Length of Test Bench ");
	
	/** OK button to end this dialog with {@link #OK_OPTION} */
	private JButton ok;
	/** Cancel button to end this dialog with {@link #CANCEL_OPTION} */
	private JButton cancel;
	
	/**
	 * Variable to indicate an option that user chose. Option can be:
	 * <ul>
	 * <li>{@link #OK_OPTION}</li>
	 * <li>{@link #CANCEL_OPTION}</li>
	 * <li>{@link #CLOSED_OPTION}</li>
	 * </ul>
	 */
	private int option = -1;
	/**
	 * Return value from class method if user closes window without selecting
	 * anything, more than likely this should be treated as
	 * <code>CANCEL_OPTION</code>.
	 */
	public static final int CLOSED_OPTION = -1;
	/** Return value from class method if CANCEL is chosen.*/
	public static final int CANCEL_OPTION = 2;
	/** Return value from class method if OK is chosen.*/
	public static final int OK_OPTION = 0;
	
	/** a radion button that represents Rising Edge */
	private JRadioButton risingEdgeButton;
	/** a radion button that represents Falling Edge */
	private JRadioButton fallingEdgeButton;
	/** TextField that represents Clock Time High value<br>
	 * default value is <code>100</code> */
	private JTextField clockTimeHigh = new JTextField("100");
	private JLabel clockTimeHighLabel = new JLabel("Clock Time High ");
	private JLabel clockTimeHighScale = new JLabel();
	/** TextField that represents Clock Time Low value<br>
	 * default value is <code>100</code> */
	private JTextField clockTimeLow = new JTextField("100");
	private JLabel clockTimeLowLabel = new JLabel("Clock Time Low ");
	private JLabel clockTimeLowScale = new JLabel();
	/** TextField that represents Input Setup Time value<br>
	 * default value is <code>75</code> */
	private JTextField inputSetupTime = new JTextField("75");
	private JLabel inputSetupTimeLabel = new JLabel("Input Setup Time ");
	private JLabel inputSetupTimeScale = new JLabel();
	/** TextField that represents Check Outputs value<br>
	 * default value is <code>50</code> */
	private JTextField checkOutputs = new JTextField("50");
	private JLabel checkOutputsLabel = new JLabel("Check Outputs ");
	private JLabel checkOutputsScale = new JLabel();
	/** TextField that represents Assign Inputs value<br>
	 * default value is <code>50</code> */
	private JTextField assignInputs = new JTextField("50");
	private JLabel assignInputsLabel = new JLabel("Assign Inputs ");
	private JLabel assignInputsScale = new JLabel();
	/** Combinatorial Timing Information Headers */
	private JLabel combinatorialTimingInformationHeader1 
		= new JLabel("Inputs are assigned, otputs are decoded then");
	private JLabel combinatorialTimingInformationHeader2
		= new JLabel("checked. A delay between inputs and outputs");
	private JLabel combinatorialTimingInformationHeader3
		= new JLabel("avoids assigment/checking conflicts.");
		
	private JPanel clockTimingInformation = new JPanel(new BorderLayout());
	private JLabel clockTimingInformationHeader = new JLabel("Inputs are assigned at \"Input Setup Time\"");
	
	/**oznaka vremenske skale <br>
	 * Mo�e poprimiti isklju�ivo vrijednosti:
	 * <ul>
	 *  <li><b> s</b>- second</li>
	 *  <li><b>ms</b> - millisecond</li>
	 *  <li><b>�s</b> - microsecond</li>
	 *  <li><b>ns</b> - nanosecond</li>
	 *  <li><b>ps</b> - picosecond</li>
	 *  <li><b>fs</b> - femtosecond</li>
	 * </ul>
	 * default value - <b>ns</b>*/
	private String timeScale;
	private List<String> timeScaleList = new ArrayList<String>();
	
	/** fills timeScaleList with values */
	private void fillTimeScaleList(){
		timeScaleList.add("s");
		timeScaleList.add("ms");
		timeScaleList.add("us");
		timeScaleList.add("ns");
		timeScaleList.add("ps");
		timeScaleList.add("fs");
		
		changeCurrentTimeScaleTo("ns");
	}
	
	/** JRadioButton that represents Single Clock */
	private JRadioButton singleClockButton;
	/** JRadioButton that represents Combinatorial Clock */
	private JRadioButton combinatorialButton;
	
	/** JComboBox that represents available signals in selected model<br>
	 *  A selected signal will be used as clock signal (Note: singleClockButton must be selected also)*/
	private JComboBox clockSignal;
	
	/** a JLabel which shows an error message if the NumberFields are not filled in 
	 * correctly <br>
	 * is hidden if the NumberFields are filled in correctly
	 */
	private JLabel errorMessage1 = new JLabel("Field(s) marked with * have to be filled with positive integer");
	private JLabel errorMessage2 = new JLabel("Input Setup Time field has to be a non negative integer smaller than Clock Time Low if Rising edge is selected");
	private JLabel errorMessage3 = new JLabel("The sum of Checked Outputs and Assign Inputs has to be less than Initial Length of Testbench ");

	
	/** a JPanel which has errorLabels */
	private JPanel message = new JPanel(new BorderLayout());
	
	/** a header JPanel that shows Signal image and errorMessage */
	private JPanel image = new JPanel(new BorderLayout());
	
	/** a ImageComponent */
	private ImpulsImageComponent impulsImage = new ImpulsImageComponent(this);
	
	/** a key listener which checks for irregularities in NumberFields */
	private KeyListener keyListener = new KeyListener(){

		@Override
		public void keyPressed(KeyEvent e) {
			check();
		}

		@Override
		public void keyReleased(KeyEvent e) {
			check();
		}

		@Override
		public void keyTyped(KeyEvent e) {
			check();
		}
		
		private void check(){
			if (isSingleClock() && isFormOK() ){
				impulsImage.setClockTimeHigh(getClockTimeHigh());
				impulsImage.setClockTimeLow(getClockTimeLow());
				impulsImage.setInputSetupTime(getInputSetupTime());
			}else if (isCombinatorial() && isFormOK() ){
				impulsImage.setCheckedOutputs(getCheckOutputs());
				impulsImage.setAssignInputs(getAssignInputs());
				impulsImage.setInputSetupTime(getInputSetupTime());
			}
			
		}
		
	};
	
	/** class constructor 
	 * @param owner the Component from which the dialog is displayed
	 * @param modal true for a modal dialog, false for one that allows others windows to be active at the same time
	 * @param ci a CircuitInterface of the VHDL project
	 * @param testbench a name of the testbench
	 * @param project the project's name */
	public InitTimingDialog(Component owner, boolean modal, CircuitInterface ci,
			String testbench, String project){
		super (JOptionPane.getFrameForComponent(owner), null, modal);
		fillTimeScaleList();
		InitTimingDialogImpl(owner, ci, testbench, project);
	}
	
	/** class constructor 
	 * @param owner the Component from which the dialog is displayed
	 * @param modal true for a modal dialog, false for one that allows others windows to be active at the same time
	 * @param ci a CircuitInterface of the VHDL project
	 * @param testbench a name of the testbench
	 * @param project the project's name */
	public InitTimingDialog(Frame owner, boolean modal,	CircuitInterface ci,
			String testbench, String project){
		super (owner, null, modal);
		fillTimeScaleList();
		InitTimingDialogImpl(owner, ci, testbench, project);
	}
	
	private void InitTimingDialogImpl(Component owner, CircuitInterface ci,
			String testbench, String project){
		this.owner = owner;
		
		{
			errorMessage1.setForeground(Color.RED);
			errorMessage1.setVisible(false);
			errorMessage2.setForeground(Color.RED);
			errorMessage2.setVisible(false);
			errorMessage3.setForeground(Color.RED);
			errorMessage3.setVisible(false);
			message.add(errorMessage1, BorderLayout.NORTH);
			message.add(errorMessage2, BorderLayout.CENTER);
			message.add(errorMessage3, BorderLayout.SOUTH);
			int width = this.getWidth();
			int height = 100;
			
			message.setPreferredSize(new Dimension(width, height));
			Border outsideMessageBorder = BorderFactory.createEmptyBorder(BORDER,
					BORDER, BORDER, BORDER);
			Border clockMessageBorder = BorderFactory
					.createTitledBorder("Errors");
			message.setBorder(BorderFactory.createCompoundBorder(
					outsideMessageBorder, clockMessageBorder));
			
			image.add(impulsImage, BorderLayout.CENTER);
			image.add(message, BorderLayout.SOUTH);
		}
		JPanel left = new JPanel(new BorderLayout());
		
		JPanel clockInformation = new JPanel(new BorderLayout());
		{
			JPanel singleClock = new JPanel(new GridLayout(1,3));
			singleClockButton = new JRadioButton("Single Clock");
			singleClockButton.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					setClockTimingInformationEnable(true);
					impulsImage.setSingleClock();
					isFormOK();
				}
				
			});
			
			List<Port> ports = ci.getPorts();
			List<String> signals = new ArrayList<String>();
			
			for (Port port : ports){
				if (port.getDirection().equals(PortDirection.IN)
						&& port.getType().getTypeName().equals(TypeName.STD_LOGIC))
					signals.add(port.getName());
			}
			
			clockSignal = new JComboBox(signals.toArray());
			
			singleClock.add(singleClockButton);
			singleClock.add(clockSignal);
			singleClock.add(new JLabel());
			
			if (clockSignal.getComponentCount() == 0) singleClockButton.setEnabled(false);
			
			combinatorialButton = new JRadioButton("Combinatorial (or internal clock)");
			combinatorialButton.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					setClockTimingInformationEnable(false);
					impulsImage.setCombinatorial();
					isFormOK();
				}
				
			});
			
			clockInformation.add(singleClock, BorderLayout.NORTH);
			clockInformation.add(combinatorialButton, BorderLayout.SOUTH);
			
			ButtonGroup clockInfoButtonGroup = new ButtonGroup();
			clockInfoButtonGroup.add(singleClockButton);
			clockInfoButtonGroup.add(combinatorialButton);
			
			Border outsideClockBorder = BorderFactory.createEmptyBorder(BORDER,
					BORDER, BORDER, BORDER);
			Border clockBorder = BorderFactory
					.createTitledBorder("Clock Information");
			clockInformation.setBorder(BorderFactory.createCompoundBorder(
					outsideClockBorder, clockBorder));
		}
	//Clock Time Information	
		{
			JPanel edges = new JPanel(new GridLayout(1,2));
			risingEdgeButton = new JRadioButton("Rising Edge");
			fallingEdgeButton = new JRadioButton("Falling Edge");
			
			risingEdgeButton.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					impulsImage.setRisingEdge();
					impulsImage.repaint();
					isFormOK();
					errorMessage2.setText("Input Setup Time field has to be a non negative integer smaller than Clock Time Low if Rising edge is selected");
				}				
			});
			
			fallingEdgeButton.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					impulsImage.setFallingEdge();
					impulsImage.repaint();
					isFormOK();
					errorMessage2.setText("Input Setup Time field has to be a non negative integer smaller than Clock Time High if Falling edge is selected"); 
				}				
			});
			
			edges.add(risingEdgeButton);
			edges.add(fallingEdgeButton);

			ButtonGroup edgesButtonGroup = new ButtonGroup();
			edgesButtonGroup.add(risingEdgeButton);
			edgesButtonGroup.add(fallingEdgeButton);
			
			JPanel clockTimes = new JPanel(new GridLayout(3,3));
		// Clock Time High	
			clockTimes.add(clockTimeHighLabel);
			clockTimes.add(clockTimeHigh);
			clockTimes.add(clockTimeHighScale);
		// Clock Time Low	
			clockTimes.add(clockTimeLowLabel);
			clockTimes.add(clockTimeLow);
			clockTimes.add(clockTimeLowScale);
		// Input Setup Time	
			clockTimes.add(inputSetupTimeLabel);
			clockTimes.add(inputSetupTime);
			clockTimes.add(inputSetupTimeScale);
			
			clockTimingInformation.add(clockTimingInformationHeader, BorderLayout.NORTH);
			clockTimingInformation.add(edges, BorderLayout.CENTER);
			clockTimingInformation.add(clockTimes, BorderLayout.SOUTH);
			Border outsideClockTimingBorder = BorderFactory.createEmptyBorder(BORDER,
					BORDER, BORDER, BORDER);
			Border clockTimingBorder = BorderFactory
					.createTitledBorder("Clock Timing Information");
			clockTimingInformation.setBorder(BorderFactory.createCompoundBorder(
					outsideClockTimingBorder, clockTimingBorder));
		}
		left.add(clockInformation, BorderLayout.NORTH);
		left.add(clockTimingInformation, BorderLayout.SOUTH);
		
		JPanel combinatorialTimingInformation = new JPanel(new GridLayout(2,1));
		{
			JPanel up = new JPanel(new GridLayout(3,1));
			up.add(combinatorialTimingInformationHeader1);
			up.add(combinatorialTimingInformationHeader2);
			up.add(combinatorialTimingInformationHeader3);
			
			JPanel down = new JPanel (new GridLayout(2,3));
			down.add(checkOutputsLabel);
			down.add(checkOutputs);
			down.add(checkOutputsScale);
			down.add(assignInputsLabel);
			down.add(assignInputs);
			down.add(assignInputsScale);
			
			combinatorialTimingInformation.add(up);
			combinatorialTimingInformation.add(down);
			
			Border outsideCombinatorialTimingInformationBorder = BorderFactory.createEmptyBorder(BORDER,
					BORDER, BORDER, BORDER);
			Border combinatorialTimingInformationBorder = BorderFactory
					.createTitledBorder("Combinatorial Timing Information");
			combinatorialTimingInformation.setBorder(BorderFactory.createCompoundBorder(
					outsideCombinatorialTimingInformationBorder, combinatorialTimingInformationBorder));
		}
		JPanel scale = new JPanel(new GridLayout(2,2));
		{
						
			final JComboBox scaleList = new JComboBox(timeScaleList.toArray());
			scaleList.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					changeCurrentTimeScaleTo(scaleList.getSelectedItem().toString());
				}
				
			});
			scaleList.setSelectedIndex(3);
			
			scale.add(initialLengthOfTestbenchLabel);
			JPanel lengthOfTestbench = new JPanel (new GridLayout(1,2));
			lengthOfTestbench.add(initialLengthOfTestbench);
			lengthOfTestbench.add(initialLengthOfTestbenchScale, BorderLayout.CENTER);
			scale.add(lengthOfTestbench);
			scale.add(new JLabel("Time Scale"));
			scale.add(scaleList);
			
			Border outsideScaleBorder = BorderFactory.createEmptyBorder(BORDER,
					BORDER, BORDER, BORDER);
			Border scaleBorder = BorderFactory
					.createTitledBorder("Time Scale Information");
			scale.setBorder(BorderFactory.createCompoundBorder(
					outsideScaleBorder, scaleBorder));
		}
		
		JPanel right = new JPanel(new BorderLayout());
		right.add(combinatorialTimingInformation, BorderLayout.NORTH);
		right.add(scale, BorderLayout.SOUTH);
		
		// setup ok and cancel buttons
		ok = new JButton("OK");
		ok.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				option = InitTimingDialog.OK_OPTION;
				close();
			}
		});

		cancel = new JButton("Cancel");
		cancel.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				option = InitTimingDialog.CANCEL_OPTION;
				close();
			}
		});
		
		JButton help = new JButton("Help");
		help.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
		help.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				HelpManager.openHelpDialog(HelpManager.getHelpCode(InitTimingDialog.class));
			}
		});

		Box actionBox = Box.createHorizontalBox();
		actionBox.add(Box.createRigidArea(new Dimension(BORDER, BUTTON_HEIGHT)));
		actionBox.add(ok);
		actionBox.add(Box.createRigidArea(new Dimension(BORDER, BUTTON_HEIGHT)));
		actionBox.add(cancel);
		actionBox.add(Box.createRigidArea(new Dimension(BORDER, BUTTON_HEIGHT)));
		actionBox.add(help);
		actionBox.setBorder(BorderFactory.createEmptyBorder(BORDER, 0, BORDER, 0));

		JPanel actionPanel = new JPanel(new BorderLayout());
		actionPanel.add(actionBox, BorderLayout.EAST);
		
		
		JPanel main = new JPanel(new BorderLayout());
		main.add(left, BorderLayout.WEST);
		main.add(right, BorderLayout.EAST);
		
		JPanel messagePanel = new JPanel(new BorderLayout());
		messagePanel.add(image, BorderLayout.NORTH);
		messagePanel.add(main, BorderLayout.CENTER);
		messagePanel.add(actionBox, BorderLayout.SOUTH);
		
		risingEdgeButton.setSelected(true);
		combinatorialButton.setSelected(true);
		setClockTimingInformationEnable(false);
		
		this.getContentPane().add(messagePanel, BorderLayout.CENTER);
		this.setTitle("Testbench " + testbench + " for " + project);
		this.setResizable(false);
		
		{
			clockTimeHigh.addKeyListener(keyListener);
			clockTimeLow.addKeyListener(keyListener);
			inputSetupTime.addKeyListener(keyListener);
			initialLengthOfTestbench.addKeyListener(keyListener);
			checkOutputs.addKeyListener(keyListener);
			assignInputs.addKeyListener(keyListener);
		}
	}
	
	/** checks if the Form is filled in correctly */
	private boolean isFormOK(){
		boolean a,b,c;
		
		boolean e, f;
		if (isSingleClock()){
			a = isFieldOK(clockTimeHigh, clockTimeHighLabel, false, 1);
			b = isFieldOK(clockTimeLow, clockTimeLowLabel, false, 1);
			c = isFieldOK(inputSetupTime, inputSetupTimeLabel, false, 2);
			
			e = isFieldOK(checkOutputs, checkOutputsLabel, true, 1);
			f = isFieldOK(assignInputs, assignInputsLabel, true, 1);
		}else{
			a = isFieldOK(clockTimeHigh, clockTimeHighLabel, true, 1);
			b = isFieldOK(clockTimeLow, clockTimeLowLabel, true, 1);
			c = isFieldOK(inputSetupTime, inputSetupTimeLabel, true, 2);
			
			e = isFieldOK(checkOutputs, checkOutputsLabel, false, 1);
			f = isFieldOK(assignInputs, assignInputsLabel, false, 1);
		}
		boolean d = 
			isFieldOK(initialLengthOfTestbench, initialLengthOfTestbenchLabel, false, 1);
		
		ok.setEnabled(false);
		if (a && b && d && e && f){
			errorMessage1.setVisible(false);
		}else{
			errorMessage1.setVisible(true);
		}
		
		if (c){
			errorMessage2.setVisible(false);
		}else{
			errorMessage2.setVisible(true);
		}
		
		try {
			if (isCombinatorial() && 
					(getCheckOutputs() + getAssignInputs())>getInitialLengthOfTestbench()){
				errorMessage3.setVisible(true);
				d = false;
			}else{
				errorMessage3.setVisible(false);
			}
		} catch (NumberFormatException e1) {
			errorMessage3.setVisible(true);
			d = false;
		}
		
		this.repaint();
		if (isSingleClock() && a && b && c && d
				|| isCombinatorial() && d && e && f){
			ok.setEnabled(true);
			return true;
		}else return false;
	}
	
	private void setClockTimingInformationEnable(boolean b){
		clockTimingInformation.setEnabled(b);
		clockTimingInformationHeader.setEnabled(b);
		risingEdgeButton.setEnabled(b);
		fallingEdgeButton.setEnabled(b);
		clockTimeHighLabel.setEnabled(b);
		clockTimeHigh.setEnabled(b);
		clockTimeHighScale.setEnabled(b);
		clockTimeLowLabel.setEnabled(b);
		clockTimeLow.setEnabled(b);
		clockTimeLowScale.setEnabled(b);
		inputSetupTimeLabel.setEnabled(b);
		inputSetupTime.setEnabled(b);
		inputSetupTimeScale.setEnabled(b);
		
		clockSignal.setEnabled(b);
		
	// Oposite
		combinatorialTimingInformationHeader1.setEnabled(!b);
		combinatorialTimingInformationHeader2.setEnabled(!b);
		combinatorialTimingInformationHeader3.setEnabled(!b);
		checkOutputs.setEnabled(!b);
		checkOutputsLabel.setEnabled(!b);
		checkOutputsScale.setEnabled(!b);
		assignInputs.setEnabled(!b);
		assignInputsLabel.setEnabled(!b);
		assignInputsScale.setEnabled(!b);
		
		this.repaint();
	}
	
	/** checks every number field if the number is correctly written 
	 * @param flag if <code>true</code> ignore the field 
	 * @param mode <ul>
	 * <li>1 - if number has to be positive integer (uses * as ending)</li>
	 * <li>2 - Input Setup time test</li>
	 * </ul>*/
	private boolean isFieldOK(JTextField field, JLabel label, boolean flag, int mode) {
		String ending =" ";
		switch(mode){
		case 1:
			ending = "*";
			break;
		}
		try{
			if (flag || (mode==1 && Integer.parseInt(field.getText()) > 0)
					|| (mode==2 && Integer.parseInt(field.getText()) >= 0) && 
							(isSingleClock() && isRisingEdgeSelected() && Integer.parseInt(field.getText()) < getClockTimeLow()) ||
								isSingleClock() && isFallingEdgeSelected() && Integer.parseInt(field.getText())< getClockTimeHigh() ){
				field.setBackground(Color.WHITE);
				String text = label.getText();
				if (text.endsWith(ending)){
					label.setText(text.substring(0, text.length()-1) + " ");
				}
				return true;
			}
		}catch(NumberFormatException ignorable){
		}
		String text = label.getText();
		if (!text.endsWith(ending)) 
			label.setText(text.substring(0, text.length()-1) + ending);
		field.setBackground(Color.PINK);
		this.repaint();
		return false;
	}
	
	/** changes current timeScale to given string */
	private void changeCurrentTimeScaleTo(String s) {
		timeScale = s;
		
		clockTimeHighScale.setText(s);
		clockTimeLowScale.setText(s);
		inputSetupTimeScale.setText(s);
		initialLengthOfTestbenchScale.setText(s);
		checkOutputsScale.setText(s);
		assignInputsScale.setText(s);
		this.repaint();
	}
	
	/**
	 * Starts a dialog and locks the control.
	 */
	public void startDialog() {
		this.pack();
		this.setLocationRelativeTo(owner);
		this.setVisible(true);
	}
	
	/**
	 * Returns what button was pressed in this dialog as an option. Option can
	 * be:
	 * <ul>
	 * <li>{@link #OK_OPTION}</li>
	 * <li>{@link #CANCEL_OPTION}</li>
	 * <li>{@link #CLOSED_OPTION}</li>
	 * </ul>
	 * 
	 * @return an option
	 */
	public int getOption() {
		return option;
	}
	
	/**
	 * Closes this dialog and releases all screen resources user by this window.
	 */
	private void close() {
		this.setVisible(false);
		this.dispose();
	}
	
// GETERI KLASE	
	/** returns <code>true</code> if Rising Edge Radio Button is selected */
	public boolean isRisingEdgeSelected(){
		return risingEdgeButton.isSelected();
	}
	
	/** returns <code>true</code> if Falling Edge Radio Button is selected */
	public boolean isFallingEdgeSelected(){
		return fallingEdgeButton.isSelected();
	}
	
	/** returns integer value of Clock Time High 
	 * @throws NumberFormatException - ignorable if the Single Clock Radio Button is not selected*/
	public int getClockTimeHigh() throws NumberFormatException{
		return Integer.parseInt(clockTimeHigh.getText());
	}
	
	/** returns integer value of Clock Time Low 
	 * @throws NumberFormatException - ignorable if the Single Clock Radio Button is not selected*/
	public int getClockTimeLow() throws NumberFormatException{
		return Integer.parseInt(clockTimeLow.getText());
	}
	
	/** returns integer value of Input Setup Time 
	 * @throws NumberFormatException - ignorable if the Single Clock Radio Button is not selected*/
	public int getInputSetupTime() throws NumberFormatException{
		return Integer.parseInt(inputSetupTime.getText());
	}
	
	/** returns <code>true</code> if Single Clock Button is selected */
	public boolean isSingleClock(){
		return singleClockButton.isSelected();
	}
	
	/** returns a String representation of the Signal selected as the Clock Signal<br>
	 * <b>NOTE: </b> SingleClock must be selected! */
	public String getClockSignal(){
		return clockSignal.getSelectedItem().toString();
	}
	
	/** returns <code>true</code> if Combinatorial Button is selected */
	public boolean isCombinatorial(){
		return combinatorialButton.isSelected();
	}
	
	/** returns integer value of Check Outputs 
	 * @throws NumberFormatException - ignorable if the Combinatorial Radio Button is not selected*/
	public int getCheckOutputs() throws NumberFormatException{
		return Integer.parseInt(checkOutputs.getText());
	}
	
	/** returns integer value of Assign Inputs 
	 * @throws NumberFormatException - ignorable if the Combinatorial Radio Button is not selected*/
	public int getAssignInputs() throws NumberFormatException{
		return Integer.parseInt(assignInputs.getText());
	}
	
	/** returns integer value of Initial Length of Testbench 
	 * @throws NumberFormatException*/
	public int getInitialLengthOfTestbench(){
		return Integer.parseInt(initialLengthOfTestbench.getText());
	}
	
	/** returns a short String representation of Time Scale being used in the testbench */
	public String getTimeScale(){
		return timeScale;
	}
	
}
