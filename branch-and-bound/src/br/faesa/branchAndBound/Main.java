package br.faesa.branchAndBound;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;

import br.faesa.gui.Window;

public class Main {

	public static void main(String[] args) 
	{
		try {
			UIManager.setLookAndFeel(new PlasticXPLookAndFeel());
		} catch (UnsupportedLookAndFeelException e) {}
		new Window();
	}

}
