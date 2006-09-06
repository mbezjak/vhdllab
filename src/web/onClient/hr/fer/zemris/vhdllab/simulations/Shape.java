package hr.fer.zemris.vhdllab.simulations;

import java.awt.Graphics;


interface Shape
{
	void draw(Graphics g, int x1, int y1, int x2);
	void putLabel(Graphics g, String s, int x1, int y1, int x2);
}
