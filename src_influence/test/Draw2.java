package test;
import java.applet.Applet;
import java.awt.Graphics;
public class Draw2 extends Applet
{
private final int left = 150;//����x�᳤��    
private final int top = 100; //����y�᳤��
//����ϵ��
private final int a = 1;   
private final int b = 1;   
private final int c = 3;       
@Override    
public void paint(Graphics g)   
{   
//��������
g.drawLine(0, top, left*2, top);       
g.drawLine(left, 0, left, top*2);

for(int x=-100;x<100;x++)       
{ 
  
//���ڻ��ߵ�ʱ���ʧ�棬��ϵ��a��b�ֱ��100
int y1 = (a*x*x+b*x)/100+c;            
int y2 = (a*(x+1)*(x+1)+b*(x+1))/100+c;           
g.drawLine(left+x, top-y1, left+x+1, top-y2); 
} 
}
}