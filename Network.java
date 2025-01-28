package nng;

import java.util.ArrayList;

import javax.swing.JComboBox;

public class Network 
{
	// ----------------------------------------------------------------------------------------
	private final int maxNodes = 8;
	private final int maxHLB = 10;
	private boolean bias;
	private ArrayList<Integer> nodes_values = new ArrayList<Integer>(2);
	
	public Network ()
	{
		this.nodes_values.add(1);
		this.nodes_values.add(1);	
	}
	// ----------------------------------------------------------------------------------------
	public int getTotalWeigths()
	{
		int total = 0;
		
		for (int i = 0; i != nodes_values.size() - 1; i++)
		{
			total += nodes_values.get(i) * nodes_values.get(i+1);
			if (bias == true) total += nodes_values.get(i+1);
		}
		return total;
	}
	// ----------------------------------------------------------------------------------------
 	public int[] getNetwork()
	{
		int[] nn = new int[this.nodes_values.size()];
		for (int i = 0; i != this.nodes_values.size() ; i++)
			nn[i] = this.nodes_values.get(i);
		return nn;
	}
	public int getMaxNodes()
	{
		return this.maxNodes;
	}
	public int getMaxHLB()
	{
		return this.maxHLB;
	}
	// ----------------------------------------------------------------------------------------
	public boolean isBiasEnabled()
	{
		return this.bias;
	}
	public void enableBias()
	{
		this.bias = true;
	}
	public void disableBias()
	{
		this.bias = false;
	}
	// ----------------------------------------------------------------------------------------
	public void newNetwork(JComboBox<Integer> input_value, JComboBox<Integer> output_value)
	{
		this.nodes_values.clear();
		this.nodes_values.add((int) input_value.getSelectedItem());
		this.nodes_values.add((int) output_value.getSelectedItem());
	}
	public void newNetwork(JComboBox<Integer> input_value, ArrayList<JComboBox<Integer>> hidden_layers , JComboBox<Integer> output_value)
	{
		this.nodes_values.clear();
		this.nodes_values.add((int) input_value.getSelectedItem());
		for (JComboBox<Integer> value: hidden_layers)
			this.nodes_values.add((int) value.getSelectedItem());
		this.nodes_values.add((int) output_value.getSelectedItem());
	}
	// ----------------------------------------------------------------------------------------
	public int getY(int max, int index, int value)
	{
		return 150 + max/2 + (index * 65) - (value *65/2);
	}
	public int startX(int screen_width, int rows_amount)
	{
		return 20 + (screen_width / 2) - (rows_amount * 40);
	}
	// ----------------------------------------------------------------------------------------
}
