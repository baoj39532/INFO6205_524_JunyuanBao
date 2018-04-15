package main;

import javax.swing.*;

import functions.GA;

import java.awt.*;
import java.io.*;

@SuppressWarnings("serial")
public class GAMainPanel extends JPanel {

	int cityNum = 50;

	public void paintComponent(Graphics g) { 
		super.paintComponent(g);
		
		try {
			GA ga = new GA(5000, 50, 1000, 50000, 0.9, 0.1, System.getProperty("user.dir") + "\\data.txt");
			ga.findBestTour();
			int[] bestTour = ga.getBestTour();
			int bestLength=ga.getBestLength();
			g.drawString("best length:"+bestLength, 30, 540);

			StringBuilder sb=new StringBuilder();
			for (int i = 0; i < cityNum; i++) {
				sb.append(bestTour[i]+"-");
	        }
			sb.delete(sb.length()-1, sb.length());
			g.drawString("best tour:"+sb.toString(), 30, 570);
			
			g.setColor(Color.RED); // 设置颜色
			// 读取数据
			int[] x;
			int[] y;

			String strbuff;
			BufferedReader data = new BufferedReader(new InputStreamReader(
					new FileInputStream(System.getProperty("user.dir") + "\\data.txt")));

			x = new int[cityNum];
			y = new int[cityNum];
			for (int i = 0; i < cityNum; i++) {
				// read data
				strbuff = data.readLine();
				// divide data by ","
				String[] strcol = strbuff.split(",");
				x[i] = Integer.valueOf(strcol[0]);// x
				y[i] = Integer.valueOf(strcol[1]);// y
				g.fillOval(x[i] / 10, y[i] / 10, 5, 5);
				g.drawString(String.valueOf(i), x[i] / 10, y[i] / 10);
			}
			data.close();
			
			g.setColor(Color.BLACK); 
			for(int j=0;j<cityNum-1;j++)
			{
				g.drawLine(x[bestTour[j]]/ 10, y[bestTour[j]]/ 10, x[bestTour[j+1]]/ 10, y[bestTour[j+1]]/ 10);
			}
			
			g.setColor(Color.YELLOW); 
			g.fillOval(x[bestTour[0]]/ 10, y[bestTour[0]]/ 10, 6, 6);
			g.fillOval(x[bestTour[cityNum-1]]/ 10, y[bestTour[cityNum-1]]/ 10, 6, 6);
			g.setColor(Color.BLACK);
			g.drawLine(x[bestTour[0]]/ 10, y[bestTour[0]]/ 10, x[bestTour[cityNum-1]]/ 10, y[bestTour[cityNum-1]]/ 10);

		} catch (Exception e) {
			e.printStackTrace(); // 异常处理？
		}
	}

	public static void main(String[] args) throws Exception {
		JFrame f = new JFrame();
		f.setTitle("GA TSP");
		f.getContentPane().add(new GAMainPanel());
		f.setSize(1920, 1080);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
}
