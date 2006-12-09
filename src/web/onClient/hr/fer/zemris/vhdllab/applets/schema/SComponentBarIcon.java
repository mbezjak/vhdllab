package hr.fer.zemris.vhdllab.applets.schema;

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
	
	public SComponentBarIcon(String rn) {
		refName = rn;
	}
	
	public void paintIcon(Component comp, Graphics graphics, int x, int y) {
		BufferedImage buffi = ((Graphics2D) graphics).getDeviceConfiguration().createCompatibleImage(
				WIDTH, HEIGHT, Transparency.TRANSLUCENT);
		SchemaDrawingAdapter adapter = new SchemaDrawingAdapter(new SchemaColorProvider(), buffi, 1.d);
		//adapter.setStartingCoordinates(x, y);
		try {
			adapter.setVirtualGridFactor(WIDTH / ComponentFactory.getSchemaComponent(refName).getComponentWidth());
			ComponentFactory.getSchemaComponent(refName).drawEssential(adapter);
		} catch (ComponentFactoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
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
