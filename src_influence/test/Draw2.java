package test;
import java.applet.Applet;
import java.awt.Graphics;
public class Draw2 extends Applet
{
private final int left = 150;//定义x轴长度    
private final int top = 100; //定义y轴长度
//定义系数
private final int a = 1;   
private final int b = 1;   
private final int c = 3;       
@Override    
public void paint(Graphics g)   
{   
//画坐标轴
g.drawLine(0, top, left*2, top);       
g.drawLine(left, 0, left, top*2);

for(int x=-100;x<100;x++)       
{ 
  
//由于画线的时候会失真，将系数a、b分别除100
int y1 = (a*x*x+b*x)/100+c;            
int y2 = (a*(x+1)*(x+1)+b*(x+1))/100+c;           
g.drawLine(left+x, top-y1, left+x+1, top-y2); 
} 
}
}