import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.awt.*;


interface Shape
{
	void draw(Graphics g, int x1, int y1, int x2);
	void putLabel(Graphics g, String s, int x1, int y1, int x2);
}
