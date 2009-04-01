package hr.fer.zemris.vhdllab.applets.editor.newtb.view.components2;

import hr.fer.zemris.vhdllab.applets.editor.newtb.enums.ChangeStateEdge;
import hr.fer.zemris.vhdllab.applets.editor.newtb.enums.Radix;
import hr.fer.zemris.vhdllab.applets.editor.newtb.enums.TimeScale;
import hr.fer.zemris.vhdllab.applets.editor.newtb.enums.VectorDirection;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.CombinatorialTestbench;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.SingleClockTestbench;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.Testbench;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.signals.ClockSignal;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.signals.ScalarSignal;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.signals.Signal;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.signals.SignalChange;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.signals.VectorSignal;
import hr.fer.zemris.vhdllab.applets.editor.newtb.util.RadixConverter;
import hr.fer.zemris.vhdllab.applets.editor.newtb.view.components2.JTestbench.Communicator;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JComponent;

/**
 * 
 * @author Nikola Santic
 * 
 */
public class JDrawArea extends JComponent {

	private static final long serialVersionUID = 1L;

	private static final int timeLineHeight = 40;
	private static final int trackHeight = 40;
	private static final int headerLength = 80;

	private static final int markerHeight = 15;
	private static final int markerWidth = 20;

	private static final int vExpandX = 10;
	private static final int vExpandY = 15;
	private static final int vExpandSize = 10;
	private static final int signalPad = 5;
	private static final int vectorPad = 8;

	protected int screenWidth;

	private int newReference = timeLineHeight;

	private long testBenchLength;
	protected long testBenchLengthScaled;
	protected long begin;
	protected long beginScaled;
	protected long length;
	protected long lengthScaled;

	protected long multiplier = 1;
	private long inputSetupTime = -1;
	private long checkOutputsTime = -1;
	private ChangeStateEdge changeStateEdge;
	private Radix radix;

	protected boolean isClockTestbench;
	private long clockUpTime = 0;
	private long periodLength;

	protected boolean markerCaught = false;

	protected int beginIndex;
	
	private List<Signal> signalList;
	private ClockSignal clockSignal;
	protected Marker marker;
	private List<VectorSignal> expanded = new ArrayList<VectorSignal>();
	private HashMap<VectorSignal, Rectangle> vExpandList = new HashMap<VectorSignal, Rectangle>();
	protected HashMap<Integer, Signal> indexSignalMap = new HashMap<Integer, Signal>();
	protected Scale scale;

	public JDrawArea(Testbench testbench) {

		this.signalList = new ArrayList<Signal>();
		this.initSignalList(testbench);
		
		marker = new Marker(testbench.getSimulationLength());
		if (testbench.getClass() == SingleClockTestbench.class) {
			isClockTestbench = true;
			changeStateEdge = ((SingleClockTestbench) testbench).getChangeStateEdge();
			if (changeStateEdge == ChangeStateEdge.rising)
				clockUpTime = ((SingleClockTestbench) testbench).getClockTimeHigh();
			inputSetupTime = ((SingleClockTestbench) testbench).getInputSetupTime();
		} else {
			isClockTestbench = false;
			checkOutputsTime = ((CombinatorialTestbench) testbench)
					.getCheckOutputsTime();
		}
		periodLength = testbench.getPeriodLength();
		addMouseListener(new MouseAdapter() {
			@Override
            public void mousePressed(MouseEvent e) {
				int reference = timeLineHeight + (isClockTestbench ? trackHeight : 0);
				if (marker.contains(e.getX(), e.getY())
						&& e.getButton() == MouseEvent.BUTTON1) {
					markerCaught = true;
				} else if (e.getY() > reference
						&& e.getButton() == MouseEvent.BUTTON1) {
					int y = (e.getY() - reference) / trackHeight;
					Signal s = indexSignalMap.get(beginIndex + y);
					if (s != null)
						clickSignal(s, e.getX(), e.getY());
				}
			}
		});

		addMouseMotionListener(new MouseAdapter() {
			@Override
            public void mouseDragged(MouseEvent e) {
				if (markerCaught) {
					int x = e.getX();
					if (x >= headerLength && x < screenWidth) {
						if (x > headerLength + scale.toPixel(testBenchLengthScaled - beginScaled))
							x = headerLength + scale.toPixel(testBenchLengthScaled - beginScaled);
						moveMarker(x - headerLength);
					}
				}
			}
		});

		addMouseListener(new MouseAdapter() {
			@Override
            public void mouseReleased(MouseEvent e) {
				if (markerCaught) {
					changeSimLength();
					markerCaught = false;
				}
			}
		});

		addMouseListener(new MouseAdapter() {
			@Override
            public void mouseClicked(MouseEvent e) {
				if (e.getY() < timeLineHeight) {
					int x = e.getX();
					if (x >= headerLength
						&& x <= screenWidth
						&& e.getClickCount() >= 2
						&& x <= headerLength + scale.toPixel(testBenchLengthScaled - beginScaled))
						moveMarker(x - headerLength);
					changeSimLength();
				}
			}
		});
	}

	private void initSignalList(Testbench tb) {
		int i = 0;
		if (tb.getClass() == SingleClockTestbench.class) {
			ClockSignal cs = ((SingleClockTestbench) tb).getClockSignal();
			clockSignal = cs;
		}
		
		for (Signal s : tb) {
			if (s.getClass() == VectorSignal.class){
				vExpandList.put((VectorSignal) s, new Rectangle(vExpandSize, vExpandSize));
				this.signalList.add(s);
				indexSignalMap.put(i, s);
				i++;
			}

			else if(s.getClass() == ScalarSignal.class) {
				this.signalList.add(s);
				indexSignalMap.put(i, s);
				i++;
			}
		}
	}

	protected void clickSignal(Signal s, int x, int y) {
		if (x < headerLength && s.getClass() == VectorSignal.class) {
			if (vExpandList.get(s).contains(x, y))
				expandSignal((VectorSignal) s);
		} else if (x > headerLength) {
			changeSignal(s, x, y);
		}
	}

	private void changeSignal(Signal s, int x, int y) {
		if (s.getClass() == ClockSignal.class)
			return;
		int reference = timeLineHeight + (isClockTestbench ? trackHeight : 0);

		x = x - headerLength;
		long time = begin + scale.toTime(x) * multiplier;
		if (time >= testBenchLength)
			return;
		if (s.getClass() == ScalarSignal.class) {
			this.getParentJTestbench().setSignalChange(s.getName(), 0, time);

		} else if (s.getClass() == VectorSignal.class) {
			if (!expanded.contains(s)) {
			} else {
				int top = getVectorBegin((y - reference) / trackHeight + beginIndex);
				int sel = (y - reference) / trackHeight + beginIndex;
				if (sel - top == 0 || sel - top > s.getDimension()) {
					//System.out
					//		.println(s.getName()
					//				+ " je vektor, otvoren, ali tko smo mi da pravimo razliku?");
					//
					// NAPOMENA : Provjeri ovaj time koji mi saljes, cini mi se da u jednom trenutku kad testbench postane velik,
					// posaljes mi negativan broj zbog overflowa
					//
					this.getParentJTestbench().clearSelectedSignal();
					this.getParentJTestbench().openSetSignalDialog(s.getName(),
							time);
				} else {
					// DOBRO PRIPAZI OVDIJE!!!
					// Zbog potrebnih prilagodbi na xilinix, promjenio sam ovaj
					// red (Davor)
					// this.getParentJTestbench().setSignalChange(s.getName(),
					// s.getDimension() - i, time);
					this.getParentJTestbench().setSignalChange(s.getName(),
							sel - top - 1, time);
					//System.out.println(s.getName() + " je vektorova komponenta " + (i - 1));
				}
			}
		}
	}

	Communicator getClickInformation(int x, int y) {
		Communicator com = new Communicator();
		com.isSignalRenderArea = x > headerLength;
		x = x - headerLength;
		com.time = begin + scale.toTime(x) * multiplier;
		Signal s = null;
		int reference = timeLineHeight + (isClockTestbench ? trackHeight : 0);
		if (y > reference) {
			y = (y - reference) / trackHeight;
			s = indexSignalMap.get(beginIndex + y);
		}
		int top = getVectorBegin(beginIndex + y);
		com.signal = s;
		com.componentIndex = y - top - 1;
		return com;
	}

	private void expandSignal(VectorSignal s) {
		if (expanded.contains(s))
			expanded.remove(s);
		else
			expanded.add(s);

		indexSignalMap.clear();
		int i = 0;
		for (Signal signal : signalList) {
			if (signal.getClass() == VectorSignal.class	&& expanded.contains(signal)) {
				for (int j = 0; j <= signal.getDimension(); j++) {
					indexSignalMap.put(i + j, signal);
				}
				i += signal.getDimension() + 1;

			} else {
				indexSignalMap.put(i , signal);
				i++;
			}
		}
		// U izradi
		// this.setSize(new Dimension(super.getSize().width, timeLineHeight +
		// trackHeight*positionMap.size() + 10));
		this.getParentJTestbench().setNOfSignalComponents(indexSignalMap.size());
		this.getParentJTestbench().repaint();
	}

	protected void moveMarker(int x) {
		repaint(marker.position - markerWidth, trackHeight - markerHeight - 2,
				2 * markerWidth, markerHeight + 2);
		marker.moveForPixels(x);
		repaint(marker.position - markerWidth, trackHeight - markerHeight - 2,
				2 * markerWidth, markerHeight + 2);
	}
	
	protected void moveMarker(long time) {
		repaint(marker.position - markerWidth, trackHeight - markerHeight - 2,
				2 * markerWidth, markerHeight + 2);
		marker.moveForTime(time);
		repaint(marker.position - markerWidth, trackHeight - markerHeight - 2,
				2 * markerWidth, markerHeight + 2);
	}

	protected void changeSimLength() {
		this.getParentJTestbench().setSimulationLength(marker.markedTime);
	}

	@Override
	public Dimension getPreferredSize() {

		return new Dimension(super.getSize().width, timeLineHeight
				+ trackHeight * indexSignalMap.size() + 10);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponents(g);
		//System.out.println("JDrawArea paintComponent: " + this.getSize());
		multiplier = TimeScale.getMultiplier(this.getParentJTestbench().model
				.getTestbench().getTimeScale());
		testBenchLength = this.getParentJTestbench().model.getTestbench()
				.getTestBenchLength();
		begin = this.getParentJTestbench().renderBeginTime;
		length = this.getParentJTestbench().renderLength;
		beginScaled = begin / multiplier;
		lengthScaled = length / multiplier;
		testBenchLengthScaled = testBenchLength / multiplier;

		Dimension size = this.getSize();
		screenWidth = size.width;
		scale = new Scale(screenWidth);
		
		radix = this.getParentJTestbench().getTestbenchRadix();

		paintTimeLine(g);
		newReference = timeLineHeight;

		if(isClockTestbench){
			paintScalarSignal(g, clockSignal);
		}
		
//		for (Signal signal : signalList) {
//			paintSignal(g, signal);
//		}
		
		beginIndex = this.getParentJTestbench().getBeginSignalIndex();
		int endIndex = beginIndex + (screenWidth - newReference) / trackHeight;

		for(int i = beginIndex ; newReference <= screenWidth - trackHeight && i < indexSignalMap.size(); i++ ){
		Signal s = indexSignalMap.get(i);

		if (s.getClass() == VectorSignal.class && expanded.contains(s)) {
				int start = i;
				Signal comp = null;
				do {
					i++;
					comp = indexSignalMap.get(i);
				} while (comp == s && i < endIndex);
				
				if(comp != s)
					paintVectorSignal(g, (VectorSignal) s, s.getDimension() + 1	- (i - start), s.getDimension());
				else if(indexSignalMap.get(start - 1) == s){
					int beg = getVectorBegin(start - 1);
					paintVectorSignal(g, (VectorSignal) s, start - beg, i - beg);
				}
				else
					paintVectorSignal(g, (VectorSignal) s, 0, i - start);
				i--;
		}
		else if(s.getClass() == VectorSignal.class){
			paintVectorMain(g, (VectorSignal) s);
			}
		else
			paintScalarSignal(g, s);
		}
		g.setColor(getBackground());
		g.fillRect(0, newReference + 1, getWidth(), getHeight() - newReference - 1);


	}
	
	private int getVectorBegin (int position){
		Signal vs = indexSignalMap.get(position);
		do{
			position--;
			} while(vs == indexSignalMap.get(position));
		return position + 1;
	}

	private void paintTimeLine(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		String timeScaleName = getParentJTestbench().model.getTestbench()
				.getTimeScale().toString();

		marker.init();

		g2.setStroke(new BasicStroke(2));
		g2.setColor(this.getBackground());
		g2.fillRect(0, 0, getWidth(), timeLineHeight);
		g2.setColor(this.getForeground());
		g2.drawRect(0, 0, getWidth(), timeLineHeight);
		g2.drawRect(0, 0, headerLength, timeLineHeight);
		g2.drawString(timeScaleName, headerLength / 2, timeLineHeight * 3 / 5);

		g2.setStroke(new BasicStroke(1));

		for (long i = beginScaled; i <= testBenchLengthScaled
				&& i <= beginScaled + lengthScaled; i += scale.scaleT) {
			if ((i - beginScaled) % (5 * scale.scaleT) == 0) {
				int ilength = Long.toString(i).length();
				g2.drawLine(headerLength + scale.toPixel(i - beginScaled),
						timeLineHeight / 2, headerLength
								+ scale.toPixel(i - beginScaled),
						timeLineHeight);
				if (i != beginScaled)
					g2.drawString(String.valueOf(i), headerLength
							+ scale.toPixel(i - beginScaled) - 6 * ilength / 2,
							timeLineHeight / 3);
			} else {
				g2.drawLine(headerLength + scale.toPixel(i - beginScaled),
						timeLineHeight, headerLength
								+ scale.toPixel(i - beginScaled),
						3 * timeLineHeight / 4);
			}
		}

		if (marker.showing)
			marker.paint(g2);

	}


	
	private void paintVectorSignal(Graphics g, VectorSignal signal, int startIndex, int endIndex) {
	if(startIndex == 0){
		paintVectorMain(g, signal);
		startIndex++;
	}
	
	if (expanded.contains(signal)) {
		
		Signal[] scalarSignals = null;
		try {
			scalarSignals = signal.getSignalComponents();
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int i = startIndex - 1; i <= endIndex - 1; i++) {
			paintScalarSignal(g, scalarSignals[i]);
		}

	}
	}

	private void paintVectorMain(Graphics g, VectorSignal signal) {
		paintSignalHeader(g, signal);

		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(2));
		List<SignalChange> changes = signal.getSignalChangeList(begin, begin
				+ length);
		g2.setColor(Color.white);
		g2.fillRect(headerLength + 1, newReference, getWidth() - headerLength,
				trackHeight);
		g2.setColor(getForeground());
		g2.drawRect(headerLength, newReference, getWidth() - headerLength,
				trackHeight);

		int fromPix = 0;
		String sigVal = RadixConverter.binToOtherString(
				signal.getSignalChange(begin).getSignalValue(),
				Radix.toInt(radix),
				signal.getDimension(),
				true);

		for (SignalChange signalChange : changes) {
			long toTime = signalChange.getTime();
			int toPix = (int) ((toTime - begin) / multiplier) * scale.scaleX
					/ scale.scaleT;
			if (fromPix == 0 && signal.getSignalChange(begin).getTime() < begin)
				paintVectorValueRight(g2, toPix, sigVal);
			else
				paintVectorValueWhole(g2, fromPix, toPix, sigVal);
			fromPix = toPix;
			sigVal = RadixConverter.binToOtherString(
					signalChange.getSignalValue(),
					Radix.toInt(radix),
					signal.getDimension(),
					true);
		}
		/* Zadnji dio */
		/* Ako nema prijelaza */
		if (changes.isEmpty() && begin + length < testBenchLength) {
			int toPix = scale.toPixel(length / multiplier);
			paintVectorValueMiddle(g2, toPix, sigVal);
		}
		/* Ako je cijeli vektor na ekranu */
		else if (begin + length >= testBenchLength && begin == 0) {
			int toPix = scale.toPixel((testBenchLength - begin) / multiplier);
			paintVectorValueWhole(g2, fromPix, toPix, sigVal);
		}
		/* Ako zadnji vektor zavrsava */
		else if (begin + length >= testBenchLength && begin > 0
				&& changes.isEmpty()) {
			int toPix = scale.toPixel((testBenchLength - begin) / multiplier);
			paintVectorValueRight(g2, toPix, sigVal);
		}
		/* Ako zadnji, cjelokupno prisutan vektor zavrsava */
		else if (begin + length >= testBenchLength && begin > 0) {
			int toPix = scale.toPixel((testBenchLength - begin) / multiplier);
			paintVectorValueWhole(g2, fromPix, toPix, sigVal);
		}
		/* Ako je nesto drugo, nadam se ,normalno */
		else {
			int toPix = scale.toPixel((length) / multiplier);
			paintVectorValueLeft(g2, fromPix, toPix, sigVal);

		}
		
		newReference += trackHeight;
	}

	private void paintVectorValueMiddle(Graphics2D g2, int toPix,
			String sigValue) {
		int[] x = { headerLength + vectorPad, headerLength + toPix,
				headerLength + toPix, headerLength + vectorPad };
		int[] y = { newReference + trackHeight - signalPad,
				newReference + trackHeight - signalPad,
				newReference + signalPad, newReference + signalPad };
		Polygon pol = new Polygon(x, y, 4);
		paintVectorShape(g2, 0, toPix, sigValue, pol);

	}

	private void paintVectorValueLeft(Graphics2D g2, int fromPix, int toPix,
			String sigValue) {
		if ((toPix - fromPix) < 16) return;
		int[] x = { headerLength + fromPix, headerLength + fromPix + vectorPad,
				headerLength + toPix, headerLength + toPix,
				headerLength + fromPix + vectorPad };
		int[] y = { newReference + trackHeight / 2,
				newReference + trackHeight - signalPad,
				newReference + trackHeight - signalPad,
				newReference + signalPad, newReference + signalPad };
		Polygon pol = new Polygon(x, y, 5);
		paintVectorShape(g2, fromPix, toPix, sigValue, pol);
	}

	private void paintVectorValueRight(Graphics2D g2, int toPix,
			String sigValue) {
		if (toPix < 16) return;

		int[] x = { headerLength + vectorPad, headerLength + toPix - vectorPad,
				headerLength + toPix, headerLength + toPix - vectorPad,
				headerLength + vectorPad };
		int[] y = { newReference + trackHeight - signalPad,
				newReference + trackHeight - signalPad,
				newReference + trackHeight / 2, newReference + signalPad,
				newReference + signalPad };
		Polygon pol = new Polygon(x, y, 5);
		paintVectorShape(g2, 0, toPix, sigValue, pol);
	}

	private void paintVectorValueWhole(Graphics2D g2, int fromPix, int toPix,
			String sigValue) {
		if ((toPix - fromPix) < 16) return;
		int[] x = { headerLength + fromPix, headerLength + fromPix + vectorPad,
				headerLength + toPix - vectorPad, headerLength + toPix,
				headerLength + toPix - vectorPad,
				headerLength + fromPix + vectorPad };
		int[] y = { newReference + trackHeight / 2,
				newReference + trackHeight - signalPad,
				newReference + trackHeight - signalPad,
				newReference + trackHeight / 2, newReference + signalPad,
				newReference + signalPad };
		Polygon pol = new Polygon(x, y, 6);
		paintVectorShape(g2, fromPix, toPix, sigValue, pol);
	}

	private void paintVectorShape(Graphics2D g2, int fromPix, int toPix,
			String sigValue, Polygon pol) {
		if (fromPix == toPix)
			return;
		g2.setStroke(new BasicStroke(1));
		g2.setColor(Color.orange);
		g2.fill(pol);
		g2.setColor(getForeground());
		g2.draw(pol);
		if (toPix - fromPix > sigValue.length() * 6)
			g2.drawString(sigValue, headerLength + (toPix + fromPix) / 2
					- sigValue.length() * 3, newReference + trackHeight * 3 / 5);
	}

	private void paintScalarSignal(Graphics g, Signal signal) {
	
		paintSignalHeader(g, signal);

		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(2));
		List<SignalChange> changes = signal.getSignalChangeList(begin, begin
				+ length);
		g2.setColor(Color.white);
		g2.fillRect(headerLength + 1, newReference, getWidth() - headerLength,
				trackHeight);
		g2.setColor(getForeground());
		g2.drawRect(headerLength, newReference, getWidth() - headerLength,
				trackHeight);
		g2.setColor(new Color(250, 250, 100));

		if (signal.getClass() != ClockSignal.class && inputSetupTime > 0) {
			int perLength = scale.toPixel(periodLength / multiplier);
			int inpLength = scale.toPixel(inputSetupTime / multiplier);
			int tbLength = scale.toPixel(testBenchLengthScaled) + headerLength;
			int upLength = scale.toPixel(clockUpTime / multiplier);

			for (int i = headerLength + perLength - upLength; i < getWidth()
					&& i <= tbLength; i += perLength) {
				g2.fillRect(i - inpLength + 3, newReference + signalPad + 5,
						inpLength - 6, trackHeight - 2 * signalPad - 10);
			}
		} else if (checkOutputsTime > 0) {
			int perLength = scale.toPixel(periodLength / multiplier);
			int checkOLength = scale.toPixel(checkOutputsTime / multiplier);
			int tbLength = scale.toPixel(testBenchLengthScaled) + headerLength;

			for (int i = headerLength + perLength + checkOLength; i < getWidth()
					&& i <= tbLength; i += perLength) {
				g2.fillRect(i - checkOLength + 3, newReference + signalPad + 5,
						checkOLength - 6, trackHeight - 2 * signalPad - 10);
			}
		}
		g2.setColor(getForeground());
		g2.setStroke(new BasicStroke(1));

		int fromPix = 0;
		int i = 0;
		String sigVal = signal.getSignalChange(begin).getSignalValue();
		
		for (SignalChange signalChange : changes) {
			long toTime = signalChange.getTime();
			int toPix = scale.toPixel((toTime - begin) / multiplier);
					
			// Horizontal Line
			g2.drawLine(headerLength + fromPix, newReference
					+ signalValue(sigVal), headerLength + toPix, newReference
					+ signalValue(sigVal));
			fromPix = toPix;
		
			// Vertical Line
			if(signal.getClass() == ClockSignal.class && (
			  (changeStateEdge == ChangeStateEdge.falling && i % 2 == 0 && i != 0)  ||
			  (changeStateEdge == ChangeStateEdge.rising && i % 2 == 1))){
				g2.setColor(Color.red);
				g2.setStroke(new BasicStroke(2));
				g2.drawLine(headerLength + fromPix, newReference + signalPad + 1,
						headerLength + fromPix, newReference + trackHeight
								- signalPad);
			}
			else	
				g2.drawLine(headerLength + fromPix, newReference + signalPad,
					headerLength + fromPix, newReference + trackHeight
							- signalPad);
			sigVal = signal.getSignalChange(toTime).getSignalValue();
			g2.setColor(getForeground());
			g2.setStroke(new BasicStroke(1));
			i++;
		}

		sigVal = signal.getSignalChange(begin + length).getSignalValue();
		// int toEnd = (int)((begin + length)/multiplier) * scale.scaleX /
		// scale.scaleT;
		// POPRAVI!
		g2.drawLine(headerLength + fromPix, newReference + signalValue(sigVal),
				getWidth(), newReference + signalValue(sigVal));
		
		newReference += trackHeight;

	}

	private void paintSignalHeader(Graphics g, Signal signal) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(getBackground());
		g2.fillRect(0, newReference, headerLength, trackHeight);
		g2.setColor(getForeground());
		g2.setStroke(new BasicStroke(2));
		g2.drawRect(1, newReference, headerLength - 1, trackHeight);
		String name = signal.getName();
		if (signal.getClass() == VectorSignal.class) {
			VectorSignal vs = (VectorSignal) signal;
			if (vs.getDirection() == VectorDirection.to)
				name += " [0:" + (vs.getDimension() - 1) + "]";
			else
				name += " [" + (vs.getDimension() - 1) + ":0]";
			Rectangle vExpand = vExpandList.get(vs);
			vExpand.setLocation(vExpandX, newReference + vExpandY);
			vExpandList.put(vs, vExpand);
			g2.setStroke(new BasicStroke(1));
			g2.setColor(Color.white);
			g2.fill(vExpand);
			g2.setColor(Color.black);
			g2.draw(vExpand);
			g2.drawLine(vExpandX + 2,
					newReference + vExpandY + vExpandSize / 2, vExpandX
							+ vExpandSize - 2, newReference + vExpandY
							+ vExpandSize / 2);
			if (!expanded.contains(vs))
				g2.drawLine(vExpandX + vExpandSize / 2, newReference + vExpandY
						+ 2, vExpandX + vExpandSize / 2, newReference
						+ vExpandY + vExpandSize - 2);
		}

		g2.drawString(name, headerLength / 3, newReference + trackHeight * 3
				/ 5);

	}

	private int signalValue(String sigVal) {
		if ("1".equals(sigVal))
			return signalPad;
		else if ("0".equals(sigVal))
			return trackHeight - signalPad;
		else
			return trackHeight / 2;
	}

	@Override
	public void validate() {
		super.validate();
	}

	protected JTestbench getParentJTestbench() {
		Container c = this;

		for (int i = 0; i < 5; i++) {
			if (c == null) {
				return null;
			}
			if (c.getClass() == JTestbench.class) {
				return (JTestbench) c;
			}
			c = c.getParent();
		}

		return null;
	}

	private class Marker {

		private Polygon polygon;
		protected int position;
		protected long markedTime;
		protected boolean showing;

		public Marker(long markedTime) {
			this.markedTime = markedTime;
			this.position = headerLength;

			int[] x = { headerLength - markerWidth / 2, headerLength,
					headerLength + markerWidth / 2 };

			int[] y = { timeLineHeight - markerHeight, timeLineHeight,
					timeLineHeight - markerHeight };

			polygon = new Polygon(x, y, 3);

		}

		public void init() {
			showing = (begin <= markedTime && markedTime <= begin + length);
			if (!showing)
				return;
			int newPos = headerLength
					+ scale.toPixel((markedTime - begin) / multiplier);
			polygon.translate(newPos - position, 0);
			position = newPos;
		}

		public void moveForPixels(int pos) {
			long newTime = begin + scale.toTime(pos) * multiplier;
			moveForTime(newTime);
		}
		
		public void moveForTime(long time){
			int left = (int)(time / multiplier) /scale.scaleT * scale.scaleT;
			int right = left + scale.scaleT;
			if((int)(time / multiplier) < (right+left) / 2)
				markedTime =  left * multiplier;
			else 
				markedTime = right * multiplier;
		}
		
		public boolean contains(int x, int y) {
			if (!showing)
				return false;
			return polygon.contains(x, y);
		}

		public void paint(Graphics2D g2) {
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setPaint(Color.lightGray);
			g2.fill(polygon);
			g2.setPaint(Color.black);
			g2.setStroke(new BasicStroke(2));
			g2.draw(polygon);
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_OFF);
		}
	}

	private class Scale {
		protected int scaleT;
		protected int scaleX;

		public Scale(int width) {
			width = width - headerLength;
			if (lengthScaled <= 100)
				scaleT = 2;
			else if (lengthScaled <= 100)
				scaleT = 2;
			else if (lengthScaled <= 500)
				scaleT = 10;
			else if (lengthScaled <= 1000)
				scaleT = 20;
			else if (lengthScaled <= 5000)
				scaleT = 100;
			else if (lengthScaled <= 10000)
				scaleT = 200;
			else if (lengthScaled <= 50000)
				scaleT = 1000;
			else
				scaleT = 1000;
			scaleX = scaleT * width / (int) lengthScaled;
			if (scaleX < 10)
				scaleX = 10;
		}
		int toPixel(long time){
			return (int) (time * scale.scaleX
			/ scale.scaleT);
		}
		
		int toTime(int pixel){
			return pixel * scale.scaleT  / scale.scaleX;
		}

	}
}
