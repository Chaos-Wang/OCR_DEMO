import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Color;

public class WaterMarkRemove {
    /**
     * 划分需要遍历去水印的部分，并去除水印
     * 范围：x[0-550],y[0-80]
     * @param frompath  源文件地址
     * @param topath    生成的去水印后文件地址
     * @throws IOException
     */

    public static void Clean( String frompath,String topath) throws IOException {
        File file1 = new File(frompath);
        BufferedImage image = ImageIO.read(file1);
        
        for(int i=0;i<550;i++)                    //x[0-550],y[0-80]根据样本可知需要读取的数据都在这个范围内
        {
            for(int j=0;j<80;j++)
            {
                //通过坐标(i,j)的像素值获得r,g,b的值
                int pixel = image.getRGB(i, j);
                int red = (pixel & 0xff0000) >> 16;      
                int green = (pixel & 0xff00) >> 8;
                int blue = (pixel & 0xff);
              
                if(red>=10&&red<=20&&green>=10&&green<=20&&blue>=10&&blue<=20)    //组成字符的像素点置为黑色
                {
                	int cover=new Color(0,0,0).getRGB();
                    image.setRGB(i,j,cover);
                }
                else 
                {
                    int white=new Color(255,255,255).getRGB();          //其他全改为白色
                    image.setRGB(i,j,white);
                }
            }
        }
        File file2=new File(topath);
        ImageIO.write(image,"png",file2);
    }
}