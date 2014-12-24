package test;

//任意曲线绘制
//抛物线形如y=a*x^2+b*x+c(其中a！=0); 双曲线表达式 x*y=c(其中c！=0);
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
//初始化窗口大小
  /*Frame F=new Frame();
F.setResizable(false);*/
/*Frame F=new Frame();
F.setUndecorated(false);*/
}
  public void paint(Graphics g) 
{ 
      
 //获取屏幕分辨率
 int FRAME_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width; 
      int FRAME_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height; 
 Frame F=new Frame();
   F.setUndecorated(false);
      //标明x,y轴正方向
      g.drawString("x正向", FRAME_WIDTH-100, FRAME_HEIGHT/2-20);
      g.drawString("y正向", FRAME_WIDTH/2-40, FRAME_HEIGHT-100);
 g.setColor(Color.red); //设置坐标轴颜色
 //画坐标轴
      g.drawLine(0,FRAME_HEIGHT/2,FRAME_WIDTH,FRAME_HEIGHT/2);
 g.drawLine(FRAME_WIDTH/2,0,FRAME_WIDTH/2,FRAME_HEIGHT);
 g.setColor(Color.black);//设置曲线的颜色
 //通过画线绘制曲线
      //for (int x = 0 ; x < getSize().width ; x++)
 for (int x = -FRAME_WIDTH/2 ;x<=FRAME_WIDTH/2 ; x++)
 {
 /*由于默认原点为applet窗口的左上角，
 所有点的坐标按向量(FRAME_WIDTH/2,FRAME_HEIGHT/2)平移*/
   g.drawLine(x+(FRAME_WIDTH/2), (int)(f(x)+FRAME_HEIGHT/2), x + 1+(FRAME_WIDTH/2), (int)(f(x + 1)+FRAME_HEIGHT/2));
      }
  }
}