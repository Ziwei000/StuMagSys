
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
import javax.imageio.ImageIO;
public class Imagecode {
     int w=70;//���
     int h=35;//�߶�

     Random r=new Random();
    
    public  BufferedImage CreateImage(){//����ͼƬ�ķ���
        BufferedImage img=new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);//�ڴ��д���һ��ͼƬ
        Graphics gps=img.getGraphics();//��ȡ��ǰͼƬ�Ļ���
        
        gps.setColor(new Color(240,240,240));//���û���
        
        gps.fillRect(0, 0, w, h);//���һ����ͼƬһ����С�ľ��Σ���ʵ����Ϊ�����ñ�����ɫ��
        return img;
    }
    static String vertifycode=new String ("");
    public   BufferedImage getImage(){//�õ�ͼƬ�ķ���
        BufferedImage img=CreateImage();
        Graphics gps=img.getGraphics();//��ȡ��ǰͼƬ�Ļ���

        //��ʼ������
        for(int i=0;i<4;i++){//���ַ���str�������ȡ4���ַ�
            String ch=this.getContent();
            vertifycode+=ch;
            gps.setColor(this.getColor());
            gps.setFont(this.getFont());//�������壬����ֺ�
            gps.drawString(ch, w/4*i, h-5);//������䲼��ͼƬ
        }
        System.out.println(vertifycode);
        drawLine(img);//������
        return img;
        
    }
    public static boolean Vertify(String code) {
    	if(code.equalsIgnoreCase(vertifycode))
    	return true;
		return false;
    }
    public  void saveImage(BufferedImage img,OutputStream out) throws  IOException {
        //����Ϊ�˲��Խ����ɵ�ͼƬ����f�̣���ʵ�ʵ���Ŀ����������Ҫ�����ɵ�ͼƬд��ͻ��˵�:ImageIO.write(img,"JPEG",response.getOutputStream());
        ImageIO.write(img, "JPEG", new FileOutputStream("a.jpg"));   
    }
    
    //��ͼƬ�в�����ĸ��ʮ������
    String str="abcdefghijklmnupqrstuvwxyzABCDEFGHIJKLMNUPQRSTUVWZYZ1234567890";
    public   String getContent(){
        int index=r.nextInt(str.length());//��ȡ0-str.length֮�������
        return str.charAt(index)+"";//����ָ��λ�õ��ַ�
    }
     String[] font={"����","���Ŀ���","��������","����","������κ"};//����
     int[] fontSize={24,25,26,27,28};//�ֺŴ�С
     int[] fontStyle={0,1,2,3};//������ʽ
    public   Font getFont(){//���������ʾЧ��
        int index1=r.nextInt(font.length);
        String name=font[index1];//���ѡ������
        int style=r.nextInt(4);
        int index2=r.nextInt(fontSize.length);
        int size=fontSize[index2];//���ѡ���ֺ�
        return new Font(name,style,size);//���壬����ֺ�
    }
    public   Color getColor(){//�õ���ͬ����ɫ
        int R=r.nextInt(256);//ȡֵ��Χ��0-255
        int G=r.nextInt(256);
        int B=r.nextInt(256);
        return new Color(R,G,B);
    }
    public void drawLine(BufferedImage img){//��������
        Graphics2D gs=(Graphics2D) img.getGraphics();
        gs.setColor(Color.BLACK);
        gs.setStroke(new BasicStroke(1.5F));//�����ߵĿ��
        for(int i=0;i<5;i++){//�����겻�ܳ�����ȣ������겻�ܳ����߶�
            int x1=r.nextInt(w);
            int y1=r.nextInt(h);
            int x2=r.nextInt(w);
            int y2=r.nextInt(h);
            gs.drawLine(x1, y1, x2, y2);        
        }
    }
}
