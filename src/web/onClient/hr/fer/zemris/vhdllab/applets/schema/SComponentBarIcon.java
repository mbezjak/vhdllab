package hr.fer.zemris.vhdllab.applets.schema;

import hr.fer.zemris.vhdllab.applets.schema.components.AbstractSchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema.components.ComponentFactory;
import hr.fer.zemris.vhdllab.applets.schema.components.ComponentFactoryException;
import hr.fer.zemris.vhdllab.applets.schema.drawings.SchemaDrawingAdapter;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import javax.swing.Icon;

public class SComponentBarIcon implements Icon {
	private static final int WIDTH = 45;
	private static final int HEIGHT = 45;

	private String refName;
	private AbstractSchemaComponent schCmp;
	
	public SComponentBarIcon(String rn) {
		refName = rn;
		if (rn != "(none)") try {
			schCmp = ComponentFactory.getSchemaComponent(refName);
		} catch (ComponentFactoryException e) {
			e.printStackTrace();
		} else {
			schCmp = null;
		}
	}
	
	public void paintIcon(Component comp, Graphics graphics, int x, int y) {		
		BufferedImage buffi = ((Graphics2D) graphics).getDeviceConfiguration().createCompatibleImage(
				WIDTH, HEIGHT, Transparency.TRANSLUCENT);
		SchemaDrawingAdapter adapter = new SchemaDrawingAdapter(new SchemaColorProvider(), buffi, 1.d);
		if (schCmp != null) {
			adapter.setVirtualGridFactor(WIDTH / schCmp.getComponentWidth());
			schCmp.drawEssential(adapter);
		} else {
			adapter.setVirtualGridFactor(1.d);
			//adapter.drawLine(5, 5, 40, 40);
		}
		graphics.drawImage(buffi, 0, 0, buffi.getWidth(), buffi.getHeight(), null);
	}
	public int getIconWidth() {
		return 45;
	}
	public int getIconHeight() {
		return 45;
	}

}
