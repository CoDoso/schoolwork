package nng;

import java.awt.event.*;
import java.util.ArrayList;
import java.awt.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class Display extends JFrame implements ActionListener
{
	// ----------------------------------------------------------------------------------------------------------------------------
	
	private JPanel panel;
	private Network nn = new Network();
	
	private           JComboBox<Integer>  inputLayerSelector   = new JComboBox<Integer>();     // https://www.youtube.com/watch?v=P4XspwuEKjY&t=69s - Dropbox Explination (only looked at the code)
	private           JComboBox<Integer>  outputLayerSelector  = new JComboBox<Integer>();
	private ArrayList<JComboBox<Integer>> hiddenLayerSelectors = new ArrayList<JComboBox<Integer>>();
	
	private JLabel inputLayerLabel;
	private JLabel outputLayerLabel;
	private JLabel hiddenLayerLabel;
	private JLabel totalWeigthsLabel;
	
	private JButton addHiddenLayerButton;
	private JButton removeHiddenLayerButton;
	private JButton biasButton;
	
	// ----------------------------------------------------------------------------------------------------------------------------
	public Display()
	{
		// ------------------------------------------------------------------
		
		setTitle("Neural Network Graph Visualizer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(80, 80);
    	setSize(1200, 700);
		setVisible(true);
		
        panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(0, 0, getSize().width, getSize().height);
        
        // ------------------------------------------------------------------
        inputLayerSelector.setBounds(50, 20, 60, 30);
        outputLayerSelector.setBounds(180, 20, 60, 30);
        inputLayerSelector.addActionListener(this); 
        outputLayerSelector.addActionListener(this); 
        for (int i = 1; i != nn.getMaxNodes()+1;i++)
        {
        	 inputLayerSelector.addItem(i);              // Input & Output 
        	outputLayerSelector.addItem(i);                  // Boxes
        }
        panel.add(inputLayerSelector);
        panel.add(outputLayerSelector);
        // ------------------------------------------------------------------
        inputLayerLabel = new JLabel("Input Layer Nodes");
        inputLayerLabel.setBounds(30, 8, 120, 10);
	    panel.add(inputLayerLabel);
	    outputLayerLabel = new JLabel("Output Layer Nodes");
	    outputLayerLabel.setBounds(160, 8, 120, 10);			 // Labels
	    panel.add(outputLayerLabel);
	    hiddenLayerLabel = new JLabel("Hidden Layer Nodes"); 
	    hiddenLayerLabel.setBounds(310, 8, 120, 10);
	    panel.add(hiddenLayerLabel);
	    totalWeigthsLabel = new JLabel("Total Weights: 1"); 
	    totalWeigthsLabel.setBounds(15, 105, 115, 40);
	    panel.add(totalWeigthsLabel);
	    // ------------------------------------------------------------------
	    addHiddenLayerButton=new JButton("+");
	    addHiddenLayerButton.setBounds(340, 20, 45, 30);
	    addHiddenLayerButton.addActionListener(this);
	    panel.add(addHiddenLayerButton);
	    removeHiddenLayerButton=new JButton("-");
		removeHiddenLayerButton.addActionListener(this);     // Buttons
	    biasButton=new JButton("Enable Bias");
	    biasButton.setBounds(15, 65, 115, 40);
	    biasButton.addActionListener(this); 
	    panel.add(biasButton);
	    // ------------------------------------------------------------------
	    this.add(panel);
	    repaint();
	}
	public void paint(Graphics nng)
	{
		super.paint(nng);
		clear(nng, new Color(0, 100, 0));
		int[] Values = getValues();
		displayTotalWeigths(nn.getTotalWeigths());
		int maxHeigth = nn.getMaxNodes() * 65;
		
		int row_x = nn.startX(getSize().width, Values.length);
		// -------------------------------------------------------------------------------------------------

		//     ----------   Nodes   ------------
		
		// ---------------- Intput --------------------
		if (nn.isBiasEnabled()) nng.fillOval(row_x, 96, 50, 50);;
		nng.setColor(Color.BLACK);
		for (int node_index = 0; node_index !=  Values[0]; node_index++)
			nng.fillOval(row_x, nn.getY(maxHeigth, node_index, Values[0]), 50, 50);
		row_x += 80;
		// ---------------- Hidden --------------------
		if (!hiddenLayerSelectors.isEmpty())
			for (int node_size = 1; node_size !=  Values.length - 1; node_size++)
			{
				nng.setColor(new Color(0, 100, 0));
				if (nn.isBiasEnabled()) nng.fillOval(row_x, 96, 50, 50);;
				nng.setColor(Color.DARK_GRAY);
				for (int node_index = 0; node_index !=  Values[node_size]; node_index++)
					nng.fillOval(row_x, nn.getY(maxHeigth, node_index, Values[node_size]), 50, 50);
				row_x += 80;
			}
		// ---------------- Output --------------------
		nng.setColor(Color.BLACK);
		for (int node_index = 0; node_index !=  Values[Values.length-1]; node_index++)
			nng.fillOval(row_x, nn.getY(maxHeigth, node_index, Values[Values.length-1]), 50, 50);
		
		// -------------------------------------------------------------------------------------------------
		row_x = nn.startX(getSize().width, Values.length) + 25;
		
		//     ----------   Lines   ------------
		
		// ---------------- Intput --------------------
		for (int node_index = 0; node_index !=  Values[0]; node_index++)
			for (int next_index = 0; next_index !=  Values[1]; next_index++)
			{
				nng.setColor(new Color(0, 100, 0));
				if (nn.isBiasEnabled()) nng.drawLine(row_x, 25+96, row_x+80, 25+nn.getY(maxHeigth, next_index, Values[1]));;
				nng.setColor(Color.BLACK);
				nng.drawLine(row_x,25+nn.getY(maxHeigth, node_index, Values[0]), row_x+80, 25+nn.getY(maxHeigth, next_index, Values[1]));
			}
		row_x += 80;
		
		// ---------------- Hidden & Output --------------------
		if (!hiddenLayerSelectors.isEmpty())
		{
			for (int node_size = 1; node_size !=  Values.length - 1; node_size++)
			{
				for (int node_index = 0; node_index !=  Values[node_size-1]; node_index++)
					for (int next_index = 0; next_index !=  Values[node_size]; next_index++)
					{
						nng.setColor(new Color(0, 100, 0));
						if (nn.isBiasEnabled()) nng.drawLine(row_x-80, 25+96, row_x, 25+nn.getY(maxHeigth, next_index, Values[node_size]));
						nng.setColor(Color.BLACK);
						nng.drawLine(row_x-80,25+nn.getY(maxHeigth, node_index, Values[node_size-1]), row_x, 25+nn.getY(maxHeigth, next_index, Values[node_size]));
					}
				row_x += 80;
			}
			row_x -= 80;
			for (int node_index = 0; node_index !=  Values[Values.length-2]; node_index++)
				for (int next_index = 0; next_index !=  Values[Values.length-1]; next_index++)
				{
					nng.setColor(new Color(0, 100, 0));
					if (nn.isBiasEnabled()) nng.drawLine(row_x, 25+96, row_x+80, 25+nn.getY(maxHeigth, next_index, Values[Values.length-1]));
					nng.setColor(Color.BLACK);
					nng.drawLine(row_x,25+nn.getY(maxHeigth, node_index, Values[Values.length-2]), row_x+80, 25+nn.getY(maxHeigth, next_index, Values[Values.length-1]));
				}
		}
		// -------------------------------------------------------------------------------------------------
	}
	// ------------------------------------------------------------------
	public void actionPerformed(ActionEvent ae) 
	{
		if (ae.getActionCommand().equals("+") && hiddenLayerSelectors.size() != nn.getMaxHLB()) addHLB();
		else if (ae.getActionCommand().equals("-") && !hiddenLayerSelectors.isEmpty()) removeHLB();
		else if (ae.getActionCommand().equals("Enable Bias"))
		{
			this.biasButton.setText("Disable Bias");
			nn.enableBias();
		}
		else if (ae.getActionCommand().equals("Disable Bias"))
		{
			this.biasButton.setText("Enable Bias");
			nn.disableBias();
		}
		repaint();
	}
	// ----------------------------------------------------------------------------------------------------------------------------
	public void clear(Graphics nng, Color prevColor)
	{
		nng.setColor(new Color(237,237,237));
		nng.fillRect(140, 150, getSize().width, getSize().height);    // Screen Clearing
		nng.setColor(prevColor);
	}
	public void displayTotalWeigths(int total_weights)
	{
		this.totalWeigthsLabel.setText("Total Weights: " + total_weights);
	}
	public void renderHLButtons()
	{
		this.panel.remove(addHiddenLayerButton);
		this.panel.remove(removeHiddenLayerButton);
		
		if (!hiddenLayerSelectors.isEmpty())
		{
			this.removeHiddenLayerButton.setBounds(380+(hiddenLayerSelectors.size() * 45), 20, 45, 20);
			this.panel.add(removeHiddenLayerButton);
		}
		
		this.hiddenLayerLabel.setBounds(310+(hiddenLayerSelectors.size() * 20), 8, 120, 10);
		this.addHiddenLayerButton.setBounds(340+(hiddenLayerSelectors.size() * 45), 20, 45, 20);	// Button realignment
		if (hiddenLayerSelectors.size() != nn.getMaxHLB())
			this.panel.add(addHiddenLayerButton);
		repaint();
	}
	public void addHLB()
	{
		JComboBox<Integer> newHiddenLayerSelector = new JComboBox<>();
		newHiddenLayerSelector.setBounds(340 + (hiddenLayerSelectors.size() * 45), 20, 40, 20);		 // new Selector
		newHiddenLayerSelector.addActionListener(this); 
        for (int i = 1; i <= nn.getMaxNodes(); i++)
            newHiddenLayerSelector.addItem(i);
        
        this.hiddenLayerSelectors.add(newHiddenLayerSelector);
        this.panel.add(hiddenLayerSelectors.get(hiddenLayerSelectors.size()-1));
        renderHLButtons();
	}
	public void removeHLB()
	{
		this.panel.remove(hiddenLayerSelectors.get(hiddenLayerSelectors.size()-1));
        this.hiddenLayerSelectors.remove(hiddenLayerSelectors.size()-1);
        renderHLButtons();
	}
	// ----------------------------------------------------------------------------------------------------------------------------
	public int[] getValues()
	{
		if (hiddenLayerSelectors.isEmpty()) 
			   nn.newNetwork(inputLayerSelector,                       outputLayerSelector);   // nn.newNetwork Call
		else   nn.newNetwork(inputLayerSelector, hiddenLayerSelectors, outputLayerSelector);
		return nn.getNetwork();
	}
	// ------------------------------------------------------------------
}
