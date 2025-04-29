/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mainproject;


import java.awt.BasicStroke;
import org.jfree.chart.plot.CategoryPlot;

import org.jfree.chart.renderer.category.BarRenderer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.category.DefaultCategoryDataset;
import java.awt.Color;
import java.awt.Font;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;



/**
 *
 * @author farzaneh
 */
public class Plat_result {
    
    public Plat_result(){
        
    }

    
    public void plot_column(String name, int[] cloudletCounts, double[] fpa, double[] msa, double[] empa, double[] hghhc, double[] ibwc, double[] tnnde) {  
                DefaultCategoryDataset dataset = new DefaultCategoryDataset();  
     
        for (int k = 0; k < cloudletCounts.length; k++) {  
            dataset.addValue(fpa[k], "FPGWO", Integer.toString(cloudletCounts[k]));  
            dataset.addValue(msa[k], "MSDE", Integer.toString(cloudletCounts[k]));  
            dataset.addValue(empa[k], "EMPA", Integer.toString(cloudletCounts[k]));  
            dataset.addValue(hghhc[k], "HGHHC", Integer.toString(cloudletCounts[k]));  
            dataset.addValue(ibwc[k], "IBWC", Integer.toString(cloudletCounts[k]));  
            dataset.addValue(tnnde[k], "TNNDE", Integer.toString(cloudletCounts[k]));  
        } 
        
        JFreeChart chart = ChartFactory.createBarChart( "","Number of Task", name,
                dataset,PlotOrientation.VERTICAL,true,true,false);  
        CategoryPlot plot = chart.getCategoryPlot();
        XYPlot plot1= (XYPlot) chart.getPlot();
        
        ValueAxis yAxis = plot1.getRangeAxis();
        yAxis.setRange(150, 300);
        
        plot.setBackgroundPaint(Color.WHITE);  
        
        BarRenderer renderer = (BarRenderer) plot.getRenderer();  
        renderer.setSeriesPaint(0, Color.CYAN);  
        renderer.setSeriesPaint(1, Color.GREEN);  
        renderer.setSeriesPaint(2, Color.MAGENTA);  
        renderer.setSeriesPaint(3, Color.BLUE);  
        renderer.setSeriesPaint(4, Color.ORANGE);  
        renderer.setSeriesPaint(5, Color.RED); 
        renderer.setMaximumBarWidth(2.0);
        renderer.setItemMargin(0.0);
        Font font= new Font("Adobe Arabic" , Font.BOLD,20);
        Font num_font= new Font("Adobe Arabic" , Font.BOLD, 20);
        
        ChartFrame frame = new ChartFrame("Bar Plot", chart);  
        frame.setSize(800, 600);  
        frame.setVisible(true);  
}  
    
   public void plot_line(String name, int[] cloudletCounts, double[] fpa, double[] msa, double[] empa, double[] hghhc, double[] ibwc, double[] tnnde) {  
    XYSeriesCollection  dataset=new XYSeriesCollection();
         XYSeries series0= new XYSeries("FPGWO");
         XYSeries series1= new XYSeries("MSDE");
         XYSeries series2= new XYSeries("EMPA");
         XYSeries series3= new XYSeries("HGHHC");
         XYSeries series4= new XYSeries("IBWC");         
         XYSeries series5= new XYSeries("TNNDE");
         
         for(int k=0; k< fpa.length; k++){
             series0.add(k+1,fpa[k]);
             series1.add(k+1,msa[k]);
             series2.add(k+1,empa[k]);
             series3.add(k+1,hghhc[k]);
             series4.add(k+1,ibwc[k]);
             series5.add(k+1,tnnde[k]);
        }

        dataset.addSeries(series0);
        dataset.addSeries(series1);
        dataset.addSeries(series2);
        dataset.addSeries(series3);
        dataset.addSeries(series4);
        dataset.addSeries(series5);
    

    JFreeChart chart= ChartFactory.createXYLineChart("" , "iteration", "Objective Function Value" ,
                dataset,PlotOrientation.VERTICAL, true,true, false );
        
        XYPlot plot= (XYPlot) chart.getPlot();

        Font font= new Font("Adobe Arabic" , Font.BOLD,20);
        Font num_font= new Font("Adobe Arabic" , Font.BOLD, 20);
        plot.getDomainAxis().setLabelFont(font);
        plot.getRangeAxis().setLabelFont(font);
        plot.getDomainAxis().setTickLabelFont(num_font);
        plot.getRangeAxis().setTickLabelFont(num_font);
        LegendTitle legend= chart.getLegend();
        legend.setItemFont(font);
        
        XYLineAndShapeRenderer render= (XYLineAndShapeRenderer) plot.getRenderer();
        plot.setBackgroundPaint(Color.WHITE);
        render.setSeriesPaint(0, Color.GREEN);    
        render.setSeriesPaint(1, Color.red);         
        render.setSeriesPaint(2, Color.cyan);
        render.setSeriesPaint(3, Color.MAGENTA);
        render.setSeriesPaint(4, Color.ORANGE);
        render.setSeriesPaint(5, Color.BLUE);
        
        for(int i=0; i<6; i++){
            plot.getRenderer().setSeriesStroke(i, new BasicStroke(2.0f));
        }
        ChartFrame frame= new ChartFrame("Line plot" , chart);
        frame.setSize(800,900);
        frame.setVisible(true);
}

    
    public void plot_VM(String name, int[] vmn_Counts, double[] fpa, double[] msa, double[] empa, double[] hghhc, double[] ibwc, double[] tnnde) {  
                DefaultCategoryDataset dataset = new DefaultCategoryDataset();  
     
        for (int k = 0; k < vmn_Counts.length; k++) {  
            dataset.addValue(fpa[k], "FPGWO", Integer.toString(vmn_Counts[k]));  
            dataset.addValue(msa[k], "MSDE", Integer.toString(vmn_Counts[k]));  
            dataset.addValue(empa[k], "EMPA", Integer.toString(vmn_Counts[k]));  
            dataset.addValue(hghhc[k], "HGHHC", Integer.toString(vmn_Counts[k]));  
            dataset.addValue(ibwc[k], "IBWC", Integer.toString(vmn_Counts[k]));  
            dataset.addValue(tnnde[k], "TNNDE", Integer.toString(vmn_Counts[k]));  
        }  
        JFreeChart chart = ChartFactory.createBarChart( "","Number of VM", name,
                dataset,PlotOrientation.VERTICAL,true,true,false);  
        CategoryPlot plot = chart.getCategoryPlot();  
        plot.setBackgroundPaint(Color.WHITE);  
    
        BarRenderer renderer = (BarRenderer) plot.getRenderer();  
        renderer.setSeriesPaint(0, Color.CYAN);  
        renderer.setSeriesPaint(1, Color.GREEN);  
        renderer.setSeriesPaint(2, Color.MAGENTA);  
        renderer.setSeriesPaint(3, Color.BLUE);  
        renderer.setSeriesPaint(4, Color.ORANGE);  
        renderer.setSeriesPaint(5, Color.RED); 
        renderer.setMaximumBarWidth(2.0);
        renderer.setItemMargin(0.0);
        Font font= new Font("Adobe Arabic" , Font.BOLD,20);
        Font num_font= new Font("Adobe Arabic" , Font.BOLD, 20);
        
        ChartFrame frame = new ChartFrame("Bar Plot", chart);  
        frame.setSize(800, 600);  
        frame.setVisible(true);  
    }  
    
    
    
}
