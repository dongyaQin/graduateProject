package test;

//�������߻���
//����������y=a*x^2+b*x+c(����a��=0); ˫���߱��ʽ x*y=c(����c��=0);
import java.awt.Graphics;
import java.awt.Color;
import java.awt.*;
import java.applet.Applet;
//import java.applet.Frame;
import java.applet.*;
public class Draw1 extends java.applet.Applet 
{
  double f(double x) 
{
return Toolkit.getDefaultToolkit().getScreenSize().width/x; /*(Math.cos(x/5) + Math.sin(x/7) + 2) * getSize().height / 4;*/
      
  }
public void init()
{
//��ʼ�����ڴ�С
  /*Frame F=new Frame();
F.setResizable(false);*/
/*Frame F=new Frame();
F.setUndecorated(false);*/
}
  public void paint(Graphics g) 
{ 
      
 //��ȡ��Ļ�ֱ���
 int FRAME_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width; 
      int FRAME_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height; 
 Frame F=new Frame();
   F.setUndecorated(false);
      //����x,y��������
      g.drawString("x����", FRAME_WIDTH-100, FRAME_HEIGHT/2-20);
      g.drawString("y����", FRAME_WIDTH/2-40, FRAME_HEIGHT-100);
 g.setColor(Color.red); //������������ɫ
 //��������
      g.drawLine(0,FRAME_HEIGHT/2,FRAME_WIDTH,FRAME_HEIGHT/2);
 g.drawLine(FRAME_WIDTH/2,0,FRAME_WIDTH/2,FRAME_HEIGHT);
 g.setColor(Color.black);//�������ߵ���ɫ
 //ͨ�����߻�������
      //for (int x = 0 ; x < getSize().width ; x++)
 for (int x = -FRAME_WIDTH/2 ;x<=FRAME_WIDTH/2 ; x++)
 {
 /*����Ĭ��ԭ��Ϊapplet���ڵ����Ͻǣ�
 ���е�����갴����(FRAME_WIDTH/2,FRAME_HEIGHT/2)ƽ��*/
   g.drawLine(x+(FRAME_WIDTH/2), (int)(f(x)+FRAME_HEIGHT/2), x + 1+(FRAME_WIDTH/2), (int)(f(x + 1)+FRAME_HEIGHT/2));
      }
  }
}