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

// ����һ��MyPanel�������Լ�����壬�����ڻ�ͼ����ʾ��ͼ������
class MyPanel extends JPanel {
 // ����JPanel��paint����
 // Graphics�ǻ�ͼ����Ҫ�࣬����԰�������һֻ���ʣ����Ի����ָ����ģ�
 public void paint(Graphics g) {
  // ���ø��ຯ����ɳ�ʼ������仰������,�����ƺ����Բ��ӣ���ü��ϣ�
    super.paint(g);
//  // ��һ��Բ
//  g.drawOval(20, 20, 40, 40);
//  //g.drawOval(70, 20, 40, 40);
//  
//  //����ֱ��
    g.setColor(Color.green);
  g.drawLine(40, 40, 90, 90);
//  
//  //�������α߿�
//  g.drawRect(100, 100, 50, 30);
  
  //����������
    //������ɫ,��ÿ����ɫ��һ����ÿ�ζ�Ҫ����
     g.setColor(Color.green);
  g.fillRect(150, 150, 60, 80);
  g.setColor(Color.yellow);
  g.fillRect(250, 250, 60, 80);
//  
//    //������ϻ���ͼƬ��Ҫ��׼����ͼƬ��
//    Image im=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/33.jpg"));
//    //��ʾͼƬ thisָ���������JPanel 
//    g.drawImage(im, 90, 90, 356, 521, this);
    
    //���û��ʵ���ɫ
//    g.setColor(Color.PINK);
//    g.setFont(new Font("��������", Font.BOLD, 55));
//    //��λ���
//    g.drawString("�ַ�����", 100, 200);    
 }
 
  

}