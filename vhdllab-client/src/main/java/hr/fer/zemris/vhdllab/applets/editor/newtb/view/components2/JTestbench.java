package hr.fer.zemris.vhdllab.applets.editor.newtb.view.components2;

import hr.fer.zemris.vhdllab.applets.editor.newtb.enums.ChangeStateEdge;
import hr.fer.zemris.vhdllab.applets.editor.newtb.enums.Radix;
import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformSignalChangeException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformTestbenchException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.listeners.TestbenchListener;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.CombinatorialTestbench;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.SingleClockTestbench;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.Testbench;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.TestbenchModel;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.patterns.Pattern;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.signals.ClockSignal;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.signals.EditableSignal;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.signals.ScalarSignal;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.signals.Signal;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.signals.SignalChange;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.signals.VectorSignal;
import hr.fer.zemris.vhdllab.applets.editor.newtb.util.RadixConverter;
import hr.fer.zemris.vhdllab.applets.editor.newtb.view.PatternDialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoundedRangeModel;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * 
 * @author Davor Jurisic
 *
 */
public class JTestbench extends JPanel implements TestbenchListener {
    
    public static class Communicator {
        public Signal signal = null;
        public long time = 0;
        public boolean isSignalRenderArea = false;
        public int componentIndex = 0;
        
        @Override
        public String toString() {
            return "Signal name: " + signal.getName() + " | Time: " + time + " | Component index: " + componentIndex + " | IsRenderArea: " + isSignalRenderArea;
        }
    }
    
    private ChangeListener changeListener = null;
    
    private static final boolean DEBUG_MODE = false;
        
    private static final long serialVersionUID = 1L;
    
    private static final int initNOfPeriods = 8;
    private static final int minPeriodWidth = 100;
    
    protected TestbenchModel model = null;
    protected Communicator selectedSignal = null; 
    
    private JDrawArea drawArea = null;
    private JScrollBar horScrollBar = null;
    private JScrollBar verScrollBar = null;
    private JPopupMenu popup = null;
    private JMenuItem setValueMenuItem = null;
    private JMenuItem applyPatternMenuItem = null;
    private JMenuItem resetSignalMenuItem = null;
    private BoundedRangeModel horScrollModel = null;
    private BoundedRangeModel verScrollModel = null;
    
    private int beginSignalIndex;
    private int nOfSignalComponents;
    
    protected long renderBeginTime;
    protected long renderLength;
    private int nOfPeriods;
    
    private long lastSignalChangeTime = 0;
    private SetSignalChangeValueDialog setValueDialog = null;
    
    private Radix testbenchRadix = null;

    public JTestbench(TestbenchModel model) {
        super(new BorderLayout());
        
        this.setModel(model);
        
        this.setValueDialog = new SetSignalChangeValueDialog();
        this.testbenchRadix = Radix.Binary;
    }
    
    public void setModel(TestbenchModel model) {
        this.removeAll();
        if(model != null) {
            if(model.getTestbench().getClass() == CombinatorialTestbench.class) {
                if(model.getTestbench().getSignals().size() < 1) {
                    return;
                }
            }
            else if(model.getTestbench().getClass() == SingleClockTestbench.class) {
                if(model.getTestbench().getSignals().size() <= 1) {
                    return;
                }
            }
            
            if(this.model != null) {
                this.model.removeTestbenchListener(this);
            }
            this.model = model;
            this.model.addTestbenchListener(this);
            
            this.initGUI();
            this.initListeners();
            this.initDrawAreaData();
            
            this.lastSignalChangeTime = this.model.getTestbench().getSimulationLength();
        }
    }

    private void initListeners() {
        this.horScrollModel.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                repaint();
            }
        });
        this.verScrollModel.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                setBeginSignalIndex(verScrollModel.getValue());
            }
        });
        this.drawArea.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            @Override
            public void mouseExited(MouseEvent e) {
            }
            @Override
            public void mousePressed(MouseEvent e) {
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON3) {
                    showPopup(e.getPoint());
                }
            }           
        });
        this.setValueMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setValue();
            }
        });
        this.applyPatternMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyPattern();
            }
        });
        this.resetSignalMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                resetSignal();
            }
        });
    }

    private void initGUI() {
        this.setBorder(new LineBorder(Color.GRAY));
        
        this.drawArea = new JDrawArea(this.model.getTestbench());
        this.horScrollBar = new JScrollBar(JScrollBar.HORIZONTAL);
        this.horScrollModel = new DefaultBoundedRangeModel();
        this.horScrollBar.setModel(this.horScrollModel);
        
        this.verScrollBar = new JScrollBar(JScrollBar.VERTICAL);
        this.verScrollModel = new DefaultBoundedRangeModel();
        this.verScrollBar.setModel(verScrollModel);
        
        this.horScrollBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, verScrollBar.getPreferredSize().width));
                        
        this.add(horScrollBar, BorderLayout.SOUTH);
        this.add(verScrollBar, BorderLayout.EAST);
        this.add(drawArea, BorderLayout.CENTER);
        
        this.popup = new JPopupMenu();
        this.setValueMenuItem = new JMenuItem("Set signal value");
        this.applyPatternMenuItem = new JMenuItem("Apply pattern");
        this.resetSignalMenuItem = new JMenuItem("Reset signal");
        this.popup.add(this.setValueMenuItem);
        this.popup.add(this.applyPatternMenuItem);
        this.popup.add(this.resetSignalMenuItem);
    }
    
    private void initDrawAreaData() {
        this.renderBeginTime = 0;
        this.renderLength = 0;
        this.nOfPeriods = initNOfPeriods;
        
        this.beginSignalIndex = 0;
        this.nOfSignalComponents = this.model.getTestbench().getSignals().size();
        
        if(this.model.getTestbench().getClass() == SingleClockTestbench.class) {
            this.nOfSignalComponents -= 1;
        }
    }
    
    private void setDrawAreaData() {
        if(model == null) {
            return;
        }
        
        //
        // Horizontal scroll
        //
        long totalLength = this.model.getTestbench().getTestBenchLength();
        long periodLength = this.model.getTestbench().getPeriodLength();
        long totalPeriods = (long) Math.ceil(totalLength / periodLength);
        
        if(totalPeriods > Integer.MAX_VALUE) {
            totalPeriods = Integer.MAX_VALUE;
        }
        
        int beginPeriod = this.horScrollModel.getValue();
        if(beginPeriod + this.nOfPeriods > totalPeriods) {
            beginPeriod = (int) Math.max(totalPeriods - this.nOfPeriods, 0);
            this.horScrollModel.setValue(beginPeriod);
        }
        this.renderBeginTime = periodLength * beginPeriod;
        this.renderLength = periodLength * this.nOfPeriods;
        
        this.horScrollModel.setExtent(this.nOfPeriods);
        this.horScrollModel.setMaximum((int) (totalPeriods));
        
        //
        // Vertical scroll
        //
        this.verScrollModel.setExtent(1);
        this.verScrollModel.setMaximum(this.nOfSignalComponents);
    }

    private void setNewSimulationLength(long signalChangeTime) {
        if(model == null) {
            return;
        }
        
        if(signalChangeTime > this.lastSignalChangeTime) {
            this.lastSignalChangeTime = signalChangeTime;
            try {
                this.model.getTestbench().setSimulationLength(this.lastSignalChangeTime);
            } catch (UniformTestbenchException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void setNewTestbenchLength(long signalChangeTime) {
        if(model == null) {
            return;
        }
        
        long testbenchLength = this.model.getTestbench().getTestBenchLength();
        long periodLength = this.model.getTestbench().getPeriodLength();
        long newLength = 0;
        
        if(signalChangeTime + periodLength < testbenchLength) {
            return;
        }
        
        try {
            newLength = testbenchLength + testbenchLength;
            if(newLength > testbenchLength) {
                this.model.getTestbench().setTestBenchLength(newLength);
                return;
            }
            newLength = testbenchLength + (testbenchLength / 2);
            if(newLength > testbenchLength) {
                this.model.getTestbench().setTestBenchLength(newLength);
                return;
            }
            newLength = testbenchLength + periodLength;
            if(newLength > testbenchLength) {
                this.model.getTestbench().setTestBenchLength(newLength);
                return;
            }
        } catch (UniformTestbenchException e) {
            e.printStackTrace();
        }
    }
    
    protected void clearSelectedSignal() {
        this.selectedSignal = null;
    }
    
    protected void openSetSignalDialog(String signalName, long time) {
        if(model == null) {
            return;
        }
        
        String newSignalValue = null;
        try {
            Signal signal = this.model.getTestbench().getSignal(signalName);
            long trueTime = this.calculateTrueTime(time);
            int counter = 1;
            int componentIndex = -1;
            if(this.selectedSignal != null) {
                componentIndex = this.selectedSignal.componentIndex;
                if(DEBUG_MODE) {
                    System.out.println(this.selectedSignal);
                }
            }
            
            if(signal == null) return;
            if(signal.getClass() == ScalarSignal.class) {
                while(true) {
                    try {
                        this.setValueDialog.setVisibleRadixBox(false);
                        this.setValueDialog.setSignalValue(signal.getSignalChange(trueTime).getSignalValue());
                        if(this.setValueDialog.show() != JOptionPane.OK_OPTION) {
                            break;
                        }
                        newSignalValue = this.setValueDialog.getSignalValue();
                        ((EditableSignal)signal).setSignalChangeValue(trueTime, newSignalValue);
                        this.setNewTestbenchLength(trueTime);
                        this.setNewSimulationLength(trueTime);
                        break;
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Invalid signal value.", "Error", JOptionPane.ERROR_MESSAGE);
                        counter++;
                        if(counter > 5) break;
                    }
                }
            }
            else if(signal.getClass() == VectorSignal.class && componentIndex == -1) {
                while(true) {
                    try {
                        this.setValueDialog.setVisibleRadixBox(true);
                        this.setValueDialog.setRadix(Radix.toInt(this.testbenchRadix));
                        this.setValueDialog.setSignalValue(RadixConverter.binToOtherString(
                                signal.getSignalChange(trueTime).getSignalValue(),
                                Radix.toInt(this.testbenchRadix),
                                signal.getDimension(),
                                false)
                            );
                        if(this.setValueDialog.show() != JOptionPane.OK_OPTION) {
                            break;
                        }
                        newSignalValue = RadixConverter.otherToBinString(
                                this.setValueDialog.getSignalValue(),
                                this.setValueDialog.getRadix());
                        
                        if(newSignalValue.length() < signal.getDimension()) {
                            StringBuilder sb = new StringBuilder(newSignalValue);
                            for(int i = 0; i < signal.getDimension() - newSignalValue.length(); i++) {
                                sb.insert(0, '0');
                            }
                            newSignalValue = sb.toString();
                        }
                        ((EditableSignal)signal).setSignalChangeValue(trueTime, newSignalValue);
                        this.setNewTestbenchLength(trueTime);
                        this.setNewSimulationLength(trueTime);
                        break;
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Invalid signal value.", "Error", JOptionPane.ERROR_MESSAGE);
                        counter++;
                        if(counter > 5) break;
                    }
                }
            }
            else if(signal.getClass() == VectorSignal.class && componentIndex >= 0) {
                while(true) {
                    try {
                        this.setValueDialog.setVisibleRadixBox(false);
                        this.setValueDialog.setSignalValue(String.valueOf(signal.getSignalChange(trueTime).getSignalValue().charAt(componentIndex)));
                        if(this.setValueDialog.show() != JOptionPane.OK_OPTION) {
                            break;
                        }
                        newSignalValue = this.setValueDialog.getSignalValue();
                        ((VectorSignal)signal).setSignalChangeValue(trueTime, newSignalValue, componentIndex);
                        this.setNewTestbenchLength(trueTime);
                        this.setNewSimulationLength(trueTime);
                        break;
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Invalid signal value.", "Error", JOptionPane.ERROR_MESSAGE);
                        counter++;
                        if(counter > 5) break;
                    }
                }
            }
        } catch (UniformTestbenchException e) {
            e.printStackTrace();
        }
    }
    
    protected void setSignalChange(String signalName, int componentIndex, long time) {
        if(model == null) {
            return;
        }
        
        try {
            Testbench tb = this.model.getTestbench();
            Signal signal = tb.getSignal(signalName);
            long trueTime = this.calculateTrueTime(time);
            
            SignalChange signalChange = signal.getSignalChange(trueTime);
            char[] charArray = signalChange.getSignalValue().toCharArray();
            
            if(charArray[componentIndex] == '1') {
                charArray[componentIndex] = '0';
            } else {
                charArray[componentIndex] = '1';
            }
            
            ((EditableSignal)signal).setSignalChangeValue(trueTime, new String(charArray));
            
            this.setNewTestbenchLength(trueTime);
            this.setNewSimulationLength(trueTime);
            
        } catch (UniformSignalChangeException e) {
            e.printStackTrace();
        } catch (UniformTestbenchException e) {
            e.printStackTrace();
        }
    }
    
    protected void setSimulationLength(long simulationLength) {
        if(model == null) {
            return;
        }
        
        try {
            this.model.getTestbench().setSimulationLength(simulationLength);
        } catch (UniformTestbenchException e) {
            e.printStackTrace();
        }
    }
    
    private long calculateTrueTime(long time) {
        if(model == null) {
            return 0;
        }
        
        Testbench tb = this.model.getTestbench();
        long trueTime = 0;
        if(tb.getClass() == SingleClockTestbench.class) {
            long ist = ((SingleClockTestbench)tb).getInputSetupTime();
            long ctl = ((SingleClockTestbench)tb).getClockTimeLow();
            
            if(((SingleClockTestbench)tb).getChangeStateEdge() == ChangeStateEdge.rising) {
                if(time < ctl - ist) {
                    trueTime = 0;
                } else {
                    trueTime = time - ((time - ctl + ist) % tb.getPeriodLength());
                }
            }
            else {
                if(time < tb.getPeriodLength() - ist) {
                    trueTime = 0;
                } else {
                    trueTime = time - ((time - tb.getPeriodLength() + ist) % tb.getPeriodLength());
                }
            }
        }
        else {
            trueTime = time - (time % tb.getPeriodLength());
        }
        return trueTime;
    }
    
    private void resetSignal() {
        if(model == null) {
            return;
        }
        
        if(this.selectedSignal == null) { 
            return;
        }
        
        try {
            Signal signal = this.selectedSignal.signal;
            if(signal.getClass() == ScalarSignal.class) {
                ((EditableSignal)signal).deleteSignalChangeValues();
            }
            else if(signal.getClass() == VectorSignal.class) {
                if(this.selectedSignal.componentIndex == -1) {
                    ((EditableSignal)signal).deleteSignalChangeValues();
                } else {
                    ((VectorSignal)signal).deleteSignalChangeValues(this.selectedSignal.componentIndex);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.selectedSignal = null;
    }
    
    private void setValue() {
        if(model == null) {
            return;
        }
        
        if(this.selectedSignal == null) { 
            return;
        }
        this.openSetSignalDialog(this.selectedSignal.signal.getName(), this.selectedSignal.time);
        this.selectedSignal = null;
    }
    
    private void applyPattern() {   
        if(model == null) {
            return;
        }
        
        if(this.selectedSignal == null) { 
            return;
        }
        
        Pattern p = null;
        Testbench tb = this.model.getTestbench();
        long trueTime = this.calculateTrueTime(this.selectedSignal.time);
        
        if(this.selectedSignal.signal.getClass() == VectorSignal.class && this.selectedSignal.componentIndex == -1) {
            p = PatternDialog.getResultVector(
                    this.selectedSignal.signal.getDimension(),
                    tb.getPeriodLength()
                );
            if(p != null) {
                try {
                    List<SignalChange> signalChangeList = p.getChanges(trueTime);
                    List<SignalChange> shiftedSignalChangeList = new ArrayList<SignalChange>(signalChangeList.size());
                    for(SignalChange sc : signalChangeList) {
                        shiftedSignalChangeList.add(new SignalChange(
                                sc.getSignalDimension(),
                                sc.getSignalValue(),
                                this.calculateTrueTime(sc.getTime()))
                        );
                    }
                    ((EditableSignal)this.selectedSignal.signal).setSignalChangeValue(shiftedSignalChangeList);
                    this.setNewSimulationLength(this.selectedSignal.signal.getSignalLength());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else if(this.selectedSignal.signal.getClass() == ScalarSignal.class
                || (this.selectedSignal.signal.getClass() == VectorSignal.class && this.selectedSignal.componentIndex >= 0)) {
            p = PatternDialog.getResultScalar(
                    this.model.getTestbench().getPeriodLength()
                );
            if(p != null) {
                try {
                    List<SignalChange> signalChangeList = p.getChanges(trueTime);
                    List<SignalChange> shiftedSignalChangeList = new ArrayList<SignalChange>(signalChangeList.size());
                    for(SignalChange sc : signalChangeList) {
                        shiftedSignalChangeList.add(new SignalChange(
                                sc.getSignalDimension(),
                                sc.getSignalValue(),
                                this.calculateTrueTime(sc.getTime()))
                        );
                    }
                    if(this.selectedSignal.signal.getClass() == VectorSignal.class) {
                        ((VectorSignal)this.selectedSignal.signal).setSignalChangeValue(shiftedSignalChangeList, this.selectedSignal.componentIndex);
                        this.setNewSimulationLength(this.selectedSignal.signal.getSignalLength());
                    }
                    else {
                        ((EditableSignal)this.selectedSignal.signal).setSignalChangeValue(shiftedSignalChangeList);
                        this.setNewSimulationLength(this.selectedSignal.signal.getSignalLength());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        this.selectedSignal = null;
    }
    
    private void showPopup(Point point) {
        if(model == null) {
            return;
        }
        
        this.selectedSignal = this.drawArea.getClickInformation(point.x, point.y);
        if(this.selectedSignal == null || this.selectedSignal.signal == null) {
            return;
        }
        if(!this.selectedSignal.isSignalRenderArea || this.selectedSignal.signal.getClass() == ClockSignal.class) {
            return;
        }
        if(this.selectedSignal.time > this.model.getTestbench().getTestBenchLength()) {
            return;
        }
        
        this.popup.show(this, point.x, point.y);
    }

    @Override
    protected void paintComponent(Graphics g) {
        this.setDrawAreaData();
        super.paintComponents(g);
    }
    
    @Override
    public void validate() {
        super.validate();
    }

    @Override
    public void signalChanged(String signalName) {
        this.fireTestbenchChanged();
        this.repaint();
        
        if(DEBUG_MODE) {
            System.out.println("Signal changed: " + signalName);
        }
    }

    @Override
    public void simulationLengthChanged() {
        this.fireTestbenchChanged();
        this.drawArea.moveMarker(this.model.getTestbench().getSimulationLength());
        this.repaint();
        
        if(DEBUG_MODE) {
            System.out.println("Simulation length changed: " + this.model.getTestbench().getSimulationLength());
        }
    }

    @Override
    public void testBenchLengthChanged() {
        this.fireTestbenchChanged();
        this.repaint();
        
        if(DEBUG_MODE) {
            System.out.println("TestbenchLength changed: " + this.model.getTestbench().getTestBenchLength());
        }
    }
    
    public void ZoomIn() {
        if(model == null) {
            return;
        }
        
        if(!(this.nOfPeriods / 2 < initNOfPeriods / 2)) {
            this.nOfPeriods /= 2;
            this.repaint();
        }
    }
    
    public void ZoomOut() {
        if(model == null) {
            return;
        }
        
        int drawAreaWidth = this.drawArea.getSize().width;
        if(!((drawAreaWidth / this.nOfPeriods * 2) < minPeriodWidth)) {
            this.nOfPeriods *= 2;
            this.repaint();
        }
    }
    
    public void OptimalZoom() {
        if(model == null) {
            return;
        }
        
        this.nOfPeriods = initNOfPeriods;
        this.repaint();
    }

    protected int getBeginSignalIndex() {
        return beginSignalIndex;
    }

    private void setBeginSignalIndex(int beginSignalIndex) {
        if(model == null) {
            return;
        }
        
        if(DEBUG_MODE) {
            System.out.println("beginSignalIndex changed: " + beginSignalIndex);
        }
        this.beginSignalIndex = beginSignalIndex;
        this.repaint();
    }

    protected int getNOfSignalComponents() {
        return nOfSignalComponents;
    }

    protected void setNOfSignalComponents(int nOfSignalComponents) {
        if(model == null) {
            return;
        }
        
        if(DEBUG_MODE) {
            System.out.println("nOfSignalComponents changed: " + nOfSignalComponents);
        }
        this.nOfSignalComponents = nOfSignalComponents;
        this.verScrollModel.setMaximum(this.nOfSignalComponents);
    }

    public Radix getTestbenchRadix() {
        return testbenchRadix;
    }

    public void setTestbenchRadix(Radix testbenchRadix) {
        if(DEBUG_MODE) {
            System.out.println("testbenchRadix changed: " + testbenchRadix);
        }
        this.testbenchRadix = testbenchRadix;
        this.repaint();
    }
    
    private void fireTestbenchChanged() {
        if(this.changeListener != null) {
            this.changeListener.stateChanged(new ChangeEvent(this));
        }
    }
    
    public void setChangeListener(ChangeListener l) {
        this.changeListener = l;
    }
}
