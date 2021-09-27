package br.faesa.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class Window extends JFrame implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	
	private JMenuItem newItem;
	private JMenuItem exitItem;
	private JMenuItem aboutItem;
	
	private UserWindow desktopPanel;

	
	/**
	 * Construtor padr�o
	 */
	public Window()
	{
		initWindow();
	}
	
	/**
	 * M�todo que inicializa a janela e seta os
	 * par�metros necess�rios
	 */
	private void initWindow()
	{
		setTitle("Branch and Bound com Estimativa");
		setSize(800,600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		add(initializeMenuBar(),BorderLayout.NORTH);
		
		desktopPanel = new UserWindow();
		
		add(desktopPanel.getUserWindow());
		
		setVisible(true);
	}
	
	/**
	 * M�todo que inicializa a barra de menu
	 * @return JMenuBar
	 */
	private JMenuBar initializeMenuBar()
	{
		newItem = new JMenuItem("Novo");
		newItem.addActionListener(this);
		
		exitItem = new JMenuItem("Sair");
		exitItem.addActionListener(this);
		
		aboutItem = new JMenuItem("Creditos");
		aboutItem.addActionListener(this);
		
		JMenu mainMenu = new JMenu("Menu");
		mainMenu.add(newItem);
		mainMenu.add(exitItem);
		
		JMenu helpMenu = new JMenu("Sobre");
		helpMenu.add(aboutItem);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(mainMenu);
		menuBar.add(helpMenu);
		
		return menuBar;
	}

	/**
	 * M�todo que trata dos eventos
	 */
	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() == newItem)
		{
			int numNodes = 0;
			while (numNodes == 0)
			{
				String input = JOptionPane.showInputDialog(this,"Entre com a quantidade de v�rtices:","");
				try{
					numNodes = Integer.parseInt(input);
					if (numNodes < 3 && numNodes > 20)
					{
						JOptionPane.showMessageDialog(this,"Deve entrar com um valor entre 3 e 20","Erro",JOptionPane.ERROR_MESSAGE);
						numNodes = 0;
					}
				}catch (Exception e)
				{
					JOptionPane.showMessageDialog(this,"Deve entrar com um valor entre 3 e 20","Erro",JOptionPane.ERROR_MESSAGE);
					numNodes = 0;
				}
			}
			
			desktopPanel.updateUserWindow(numNodes);
			desktopPanel.updateUI();
			return;
		}
		if (evt.getSource() == exitItem)
		{
			System.exit(0);
			return;
		}
		if (evt.getSource() == aboutItem)
		{
			String msg = "Branch And Bound\n";
			msg += "Desenvolvido por:\n";
			msg += "Larissa Rosa\n";
			msg += "Intelig�ncia Artificial\n";
			JOptionPane.showMessageDialog(null, msg);
		}
	}

}
