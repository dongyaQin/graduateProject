package text;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class DrawingTool extends JFrame {

 MyPanel mp = null;

 public  DrawingTool() {
  mp=new MyPanel();
  
  this.add(mp);
  
  this.setSize(800, 800);
  this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  this.setVisible(true);
 }
 public static void main(String[] args) {
	 DrawingTool demo18_1=new DrawingTool();

 }

}

// 定义一个MyPanel（是我自己的面板，是用于绘图和显示绘图的区域）
class MyPanel extends JPanel {
 // 覆盖JPanel的paint方法
 // Graphics是绘图的重要类，你可以把他理解成一只画笔（可以画各种各样的）
 public void paint(Graphics g) {
  // 调用父类函数完成初始化（这句话不能少,现在似乎可以不加，最好加上）
    super.paint(g);
//  // 画一个圆
//  g.drawOval(20, 20, 40, 40);
//  //g.drawOval(70, 20, 40, 40);
//  
//  //画出直线
    g.setColor(Color.green);
  g.drawLine(40, 40, 90, 90);
//  
//  //画出矩形边框
//  g.drawRect(100, 100, 50, 30);
  
  //画出填充矩形
    //设置颜色,若每次颜色不一样，每次都要设置
     g.setColor(Color.green);
  g.fillRect(150, 150, 60, 80);
  g.setColor(Color.yellow);
  g.fillRect(250, 250, 60, 80);
//  
//    //在面板上画出图片（要先准备张图片）
//    Image im=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/33.jpg"));
//    //显示图片 this指的是这里的JPanel 
//    g.drawImage(im, 90, 90, 356, 521, this);
    
    //设置画笔的颜色
//    g.setColor(Color.PINK);
//    g.setFont(new Font("华文琥珀", Font.BOLD, 55));
//    //如何画字
//    g.drawString("林峰万岁", 100, 200);    
 }
 
  

}