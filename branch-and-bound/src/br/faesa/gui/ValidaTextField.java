package br.faesa.gui;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;

public class ValidaTextField implements KeyListener, FocusListener{
	
	JTextField textField;
	String initialValue;
	
	public ValidaTextField(JTextField textField) {
		super();
		this.textField = textField;
		this.textField.addKeyListener(this);
		this.textField.addFocusListener(this);
	}

	public void keyTyped(KeyEvent e) {
		validateField(e);
	}

	public void keyPressed(KeyEvent e) {
		validateField(e);		
	}

	public void keyReleased(KeyEvent e) {
		validateField(e);		
	}

	public void focusGained(FocusEvent e) {
		if(textField.isEnabled() && this.textField.isEditable()){
			this.initialValue = textField.getText();
			this.textField.setText("");
		}
	}

	public void focusLost(FocusEvent e) {
		if(textField.isEnabled() && this.textField.isEditable()){
			if(this.textField.getText().equals("")){
				this.textField.setText(this.initialValue);
			}
		}
	}

	private void validateField(KeyEvent e) {
		String tecla = e.getKeyChar()+"";
		String texto = textField.getText();
		try{
			Double.parseDouble(texto);
		}catch(NumberFormatException ex){
			textField.setText((texto.startsWith("-")?"-":"")+texto.replaceAll("-",""));	
		}	
		if(!(texto.indexOf(".") < 0 && tecla.equals("."))&& !tecla.equals("-") && tecla.replaceAll("[[\\p{Punct}][\\w][�|�]&&[\\D]]","").equals("")){
			e.consume();
		}
	}
}
