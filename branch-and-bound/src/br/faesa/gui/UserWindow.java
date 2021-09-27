package br.faesa.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import br.faesa.algorithm.BranchAndBoundComEstimativa;

public class UserWindow extends JPanel implements ActionListener, DocumentListener{

	private static final long serialVersionUID = 1L;

	//n�mero de v�rtices
	private int numVertex;
	
	//matriz de adjac�ncia
	private JTextField[][] distMatrix;
	
	//vetor com os nomes dos v�rtices
	private JLabel[] vertexNames;
	
	//vetor com as estimativas
	private JTextField[] estimatives;
	
	//combos com os nomes dos v�rtices para escolha
	private JComboBox fromCombo;
	private JComboBox toCombo;
	
	//bot�es
	private JButton execBB; 
	private JButton refreshBt;
	
	//�rea de Texto - resultado da execu��o do algoritmo
	private JTextArea resultArea;
	
	//label da estimativa
	private JLabel estLabel;
	
	private JScrollPane panelScroll;
	
	//�ndice dos v�rtices de origem e destino
	private int from, to;
	
	// nomes dos v�rtices
	String[] nomes;
	
	/**
	 * Construtor padr�o
	 */
	public UserWindow()
	{
		setLayout(null);
		
		setSize(700,1500);
		
		panelScroll = new JScrollPane(this);
		
		JLabel credLabel = new JLabel("*= Branch and Bound com Estimativa =*");
		credLabel.setForeground(Color.BLACK);
		credLabel.setFont(new Font("Arial",Font.BOLD,20));
		credLabel.setLocation(200,100);
		credLabel.setSize(600,50);

		JLabel iniLabel = new JLabel("Selecione uma op��o no Menu");
		iniLabel.setForeground(Color.RED);
		iniLabel.setFont(new Font("Arial",Font.BOLD,12));
		iniLabel.setLocation(300,160);
		iniLabel.setSize(600,50);
		
		add(credLabel);
		add(iniLabel);
		
		setVisible(true);
	}
	
	/**
	 * M�todo para utiliza��o do objeto painel
	 * @return painel com barras de rolagem (caso sejam necess�rias)
	 */
	public JScrollPane getUserWindow()
	{
		return panelScroll;
	}
	
	/**
	 * M�todo que faz um refresh no painel, ou seja, vai atualizar 
	 * a matriz de adjac�ncia de entrada, a escolha de origem e destino
	 * e as estimativas de acordo com o n�mero de v�rtices
	 * @param vertex - n�mero de vertices do grafo
	 */
	public void updateUserWindow(int vertex)
	{
		numVertex = vertex;
		
		//prepara a tela dinamicamente - barras de rolagem
		int y = (25*numVertex)+320+(10*numVertex);
		int x = 400+(25*numVertex);
		setPreferredSize(new Dimension(x,y)); 
		
		//faz as atualiza��es
		this.removeAll();
		initializeVertexNames();
		initializeDistMatrix();
	    add(initMatrixPanel());
	    add(initFromToPanel());
	    add(initEstimativePanel());
	    add(initExecButton());
	    add(initResultArea());
	    setVisible(true);
	}
	
	/**
	 * M�todo que obt�m os nomes dos V�rtices,
	 * de acordo com a entrada do usu�rio
	 */
	private void initializeVertexNames()
	{
		vertexNames = new JLabel[numVertex];
		nomes = new String[numVertex];
		
		for (int i=0; i<numVertex; i++)
		{
			String name = JOptionPane.showInputDialog(null,"Entre com o nome do v�rtice "+i,"");
			nomes[i] = name;
			vertexNames[i] = new JLabel(name + " - " +i);
			vertexNames[i].setForeground(Color.BLUE);			
		}
	}
	
	/**
	 * M�todo que cria a matriz de adjac�ncia
	 */
	private void initializeDistMatrix()
	{
		distMatrix = new JTextField[numVertex][numVertex];
		
		for (int i=0; i<numVertex; i++)
			for (int j=0; j<numVertex; j++)
			{
				distMatrix[i][j] = new JTextField("0");
				distMatrix[i][j].setColumns(3);
				distMatrix[i][j].setBackground(Color.WHITE);
				distMatrix[i][j].setHorizontalAlignment(JTextField.CENTER);
				distMatrix[i][j].addActionListener(this);
				distMatrix[i][j].getDocument().addDocumentListener(this);
				new ValidaTextField(distMatrix[i][j]);
			}	
		
		for (int i=0; i<numVertex; i++)
			distMatrix[i][i].setEditable(false);
	}
	
	/**
	 * M�todo que inicializa o painel onde se encontra a matriz
	 * de adjac�ncia e o painel da legenda com o nome dos v�rtices
	 * @return JPanel
	 */
	private JPanel initMatrixPanel()
	{
		JPanel matrixPanel = new JPanel();
		matrixPanel.setLayout(new BorderLayout());
		//matrixPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		matrixPanel.setSize(400+(25*numVertex),25+(25*numVertex));
		matrixPanel.setLocation(0,0);
		
		JLabel msgLabel = new JLabel("Digite as Dist�ncias:");
		msgLabel.setFont(new Font("Arial",Font.BOLD,12));
		msgLabel.setForeground(Color.RED);
		
		JPanel auxPanel = new JPanel();
		auxPanel.setLayout(new FlowLayout());
		auxPanel.setSize(800,25*numVertex);
		auxPanel.setLocation(0,0);
		auxPanel.add(matrixPanel());
		auxPanel.add(vertexLegendPanel());
		
		matrixPanel.add(msgLabel,BorderLayout.NORTH);
		matrixPanel.add(auxPanel,BorderLayout.CENTER);
		
		setForeground(Color.BLUE);
		
		return matrixPanel;
	}
	
	/**
	 * M�todo que inicializa o painel que cont�m o nome dos
	 * v�rtices - painel de legenda da matriz de adjac�ncia
	 * @return JPanel
	 */
	private JPanel vertexLegendPanel()
	{
		JPanel legendPanel = new JPanel();
		legendPanel.setLayout(new GridLayout(numVertex+1,1));
		legendPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		
		JLabel legend = new JLabel("Legenda:");
		legend.setForeground(Color.BLACK);
		
		legendPanel.add(legend);
		
		for (int i=0; i<numVertex; i++)
			legendPanel.add(vertexNames[i]);
		
		return legendPanel;
		
	}
	
	/**
	 * M�todo que inicializa o painel que cont�m a matriz
	 * de adjac�ncia
	 * @return JPanel
	 */
	private JPanel matrixPanel()
	{
		JPanel matrixPanel = new JPanel();
		matrixPanel.setLayout(new GridLayout(numVertex+1,numVertex));
		
		matrixPanel.add(new JLabel(""));
		
		for (int i=0; i<numVertex; i++)
		{
			JLabel vLabel = new JLabel(" "+i+" ");
			vLabel.setForeground(Color.BLUE);
			matrixPanel.add(vLabel);
		}	
		
		for (int i=0; i<numVertex; i++)
		{
			JLabel vLabel = new JLabel(" "+i+" ");
			vLabel.setForeground(Color.BLUE);
			matrixPanel.add(vLabel);
			for (int j=0; j<numVertex; j++)
				matrixPanel.add(distMatrix[i][j]);
		}	
		
		return matrixPanel;
		
	}
	
	/**
	 * M�todo que inicializa o painel que cont�m os combos list
	 * da escolha dos v�rtices de origem e destino
	 * @return JPanel
	 */
	private JPanel initFromToPanel()
	{
		JLabel fromLabel = new JLabel("Selecione a Origem:");
		fromLabel.setFont(new Font("Arial",Font.BOLD,12));
		fromLabel.setForeground(Color.RED);
		
		JLabel toLabel = new JLabel("Selecione o Destino:");
		toLabel.setFont(new Font("Arial",Font.BOLD,12));
		toLabel.setForeground(Color.RED);
		
		String[] nodeNames = new String[vertexNames.length];
		for (int i=0; i<nodeNames.length; i++)
			nodeNames[i] = vertexNames[i].getText();
		
		fromCombo = new JComboBox(nodeNames);
		toCombo = new JComboBox(nodeNames);
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2,2,5,5));
		
		panel.add(fromLabel);
		panel.add(fromCombo);
		panel.add(toLabel);
		panel.add(toCombo);
		
		refreshBt = new JButton("Atualizar");
		refreshBt.addActionListener(this);
		
		JPanel fromToPanel = new JPanel();
		//fromToPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		fromToPanel.setLayout(new BorderLayout());
		fromToPanel.add(panel,BorderLayout.NORTH);
		fromToPanel.add(refreshBt,BorderLayout.SOUTH);
		fromToPanel.setSize(300,100);
		fromToPanel.setLocation(0,25+(25*numVertex));
		
		return fromToPanel;
	}
	
	/**
	 * M�todo que inicializa o painel apra a entrada das estimativas
	 * @return JPanel
	 */
	private JPanel initEstimativePanel()
	{
		to = toCombo.getSelectedIndex();
		
		estLabel = new JLabel("Estimativas para Destino:");
		estLabel.setFont(new Font("Arial",Font.BOLD,12));
		estLabel.setForeground(Color.RED);
		estLabel.setSize(100,50);
		estLabel.setLocation(0,10);
		
		JPanel gridPanel = new JPanel();
		gridPanel.setLayout(new GridLayout(numVertex/2,4,5,0));
		gridPanel.setSize(100,15*numVertex);
		gridPanel.setLocation(0,10);
		
		estimatives = new JTextField[numVertex];
		
		JLabel[] estLabels = new JLabel[numVertex];
		for (int i=0; i<numVertex; i++)
		{
			estLabels[i] = new JLabel(vertexNames[i].getText());
		}
		
		for (int i=0; i<numVertex; i++)
		{
			estimatives[i] = new JTextField(4);
			estimatives[i].setBackground(Color.WHITE);
			new ValidaTextField(estimatives[i]);
			gridPanel.add(estLabels[i]);
			gridPanel.add(estimatives[i]);
		}
		
		estimatives[to].setEditable(false);
		
		JPanel estPanel = new JPanel();
		//estPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		estPanel.setLayout(new BorderLayout());
		estPanel.add(estLabel,BorderLayout.NORTH);
		estPanel.add(gridPanel,BorderLayout.CENTER);
		
		int alt = 25+(25*numVertex) + 100;
		estPanel.setSize(300,15+(10*numVertex));
		estPanel.setLocation(0,alt);
		
		return estPanel;
		
	}
	
	/**
	 * M�todo que inicializa o bot�o de executar
	 * @return JButton
	 */
	private JButton initExecButton()
	{
		execBB = new JButton("Executar Algoritmo");
		execBB.setSize(200,30);
		execBB.setLocation(300,25+(25*numVertex) + 100 + 15+(10*numVertex));
		execBB.addActionListener(this);
		return execBB;
	}
	
	/**
	 * M�todo que inicializa a �rea de texto onde vai aparecer o resultado
	 * do algoritmo
	 * @return
	 */
	private JScrollPane initResultArea()
	{
		resultArea = new JTextArea(10,70);
		
		JScrollPane areaSP = new JScrollPane(resultArea);
		areaSP.setLocation(0,25+(25*numVertex)+100+45+(10*numVertex));
		if (400+(25*numVertex) > 700)
			areaSP.setSize(400+(25*numVertex),150);
		else
			areaSP.setSize(770,150);
		
		return areaSP;
	}
	
	/**
	 * M�todo que faz a atualiza��o no painel de estimativas,
	 * ou seja, se o destino mudar, deixa como n�o edit�vel o campo
	 * referente ao v�rtice de destino
	 */
	private void updateEstimativesPanel()
	{
		estLabel.setText("Estimativas para "+vertexNames[to].getText()+":");
		
		for (int i=0; i<numVertex; i++)
			estimatives[i].setEditable(true);
		estimatives[to].setEditable(false);
		estimatives[to].setText("");
	}
	
	public void actionPerformed(ActionEvent evt) {
		//verifica se alguma dist�ncia mudou
		for (int i=0; i<numVertex; i++)
			for (int j=0; j<numVertex; j++)
				if (evt.getSource() == distMatrix[i][j])
				{
					/*
					 * tri�ngulo superior da matriz � igual ao tri�ngulo inferior,
					 * pois � um grafo n�o direcionado
					 */
					if (validateMatrix(distMatrix[i][j].getText()))
						distMatrix[j][i].setText(distMatrix[i][j].getText()); 
					else
						distMatrix[i][j].setText("0");
					return;
				}
		if (evt.getSource() == refreshBt)
		{
			to = toCombo.getSelectedIndex();
			from = fromCombo.getSelectedIndex();
			if (validateToFrom())
				updateEstimativesPanel();
			return;
		}
		if (evt.getSource() == execBB)
		{
			if (validateToFrom())
				executeAlgorithm();
		}
	}

	public void insertUpdate(DocumentEvent evt) {
		for (int i=0; i<numVertex; i++)
			for (int j=0; j<numVertex; j++)
				if (evt.getDocument() == distMatrix[i][j].getDocument())
				{
					/*
					 * tri�ngulo superior da matriz � igual ao tri�ngulo inferior,
					 * pois � um grafo n�o direcionado
					 */
					try{
						
						if (validateMatrix(distMatrix[i][j].getText()))
							distMatrix[j][i].setText(distMatrix[i][j].getText()); 
						else
							distMatrix[i][j].setText("0");
					}catch (Exception e){}
					return;
				}
		
	}
	
	/**
	 * M�todo que verifica os valores na matriz de adjac�ncia
	 * @param value - valor a ser verificado
	 * @return true se a valida��o estiver ok, false caso contr�rio
	 */
	private boolean validateMatrix(String value)
	{
		try{
			Integer.parseInt(value);
			return true;
		}catch (Exception e)
		{
			JOptionPane.showMessageDialog(this,"Digite algum valor inteiro","Erro",JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
	
	/**
	 * M�todo que valida os n�s de origem e destino do grafo
	 * @return true se a origem for diferente do destino, false
	 * caso contr�rio
	 */
	private boolean validateToFrom()
	{
		if (to == from)
		{
			JOptionPane.showMessageDialog(this,"A Origem deve ser diferente do Destino","Aten��o",JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return true;
	}
	
	/**
	 * M�todo que prepara todos os objetos que servem de 
	 * par�metros para a execu��o do algoritmo e tamb�m captura
	 * o resultado para mostrar ao usu�rio
	 */
	private void executeAlgorithm()
	{
		//transforma os objetos de acordo com os par�metros
		int[][] matrix = new int[numVertex][numVertex];
		for (int i=0; i<numVertex; i++)
			for (int j=0; j<numVertex; j++)
				matrix[i][j] = Integer.parseInt(distMatrix[i][j].getText());
		
		double[] est = new double[numVertex];
		for (int i=0; i<numVertex; i++)
		{
			String value = estimatives[i].getText();
			value = value.replace(',', '.');
			if (value.equals("") || i == to)
				est[i] = 0;
			else
				est[i] = Double.parseDouble(value);
		}
		
		//formata a sa�da do resultado para a tela
		String res = new BranchAndBoundComEstimativa(matrix,est,from,to, nomes).execute();
		//String result = res.split("\n")[2];
		//String[] path = result.split(" ");
		
		String msg = "Origem: " + vertexNames[from].getText() + "\n";
		msg += "Destino: " + vertexNames[to].getText() + "\n";
		msg += "Resultado da execu��o do algortimo:\n";
		msg += res;
		
		/*
		if (!result.equals(BranchAndBoundWithEstimation.noPathFound))
		{
			msg += vertexNames[Integer.parseInt(path[0])].getText();
			 
			for (int i=1; i<path.length; i++)
			{
				msg += " => " + vertexNames[Integer.parseInt(path[i])].getText();
			}
		}
		
		msg += "\n";
		*/
		
		resultArea.setText(msg);
		
	}

	public void removeUpdate(DocumentEvent evt) {
	}

	public void changedUpdate(DocumentEvent evt) {
	}

}
