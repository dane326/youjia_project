package com.gsh.system.utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 二维码工具类
 */
public class QRCodeUtils{

    private static final Logger logger = LoggerFactory.getLogger(QRCodeUtils.class);
    public static void main(String[] args) {
        try {
            QREncode("http://182.151.199.4:11134/aiexam/10017/","D:\\zxing1.gif");
            //QRReader(new File("D:\\zxing1.gif"));
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }


	/**
	 * 生成二维码到指定位置
	 * @param content
	 * @param FilePath
	 * @throws WriterException
	 * @throws IOException
	 */
    public static void QREncode(String content,String FilePath) throws WriterException, IOException {
        BufferedImage bufferedImage = getBufferdImage(content);
        File file = new File(FilePath);
        ImageIO.write(bufferedImage, "gif", file);//输出带logo图片
        logger.info("生成二维码成功！位置:"+file.getPath());
    }



    /**
     * 生成二维码并返回字节数组
     * @param content
     * @param ios
     * @return
     * @throws WriterException
     * @throws IOException
     */
    public static byte[] QREncode(String content,ByteArrayOutputStream ios) throws WriterException, IOException {
        BufferedImage bufferedImage = getBufferdImage(content);
        ImageIO.write(bufferedImage, "gif", ios);//输出带logo图片
        logger.info("生成二维码字节数组成功！字节长度为：" + ios.toByteArray().length);
        return ios.toByteArray();
    }

	private static BufferedImage getBufferdImage(String content) throws WriterException, IOException {
		int width = 200; // 图像宽度
        int height = 200; // 图像高度
        Map<EncodeHintType, Object> hints = new HashMap<>();
        //内容编码格式
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        // 指定纠错等级
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        //设置二维码边的空度，非负数
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
        MatrixToImageConfig matrixToImageConfig = new MatrixToImageConfig(0xFF000001, 0xFFFFFFFF);
		return MatrixToImageWriter.toBufferedImage(bitMatrix,matrixToImageConfig);
	}
	
	private static BufferedImage getBufferdImage(String content, String logoFile) throws WriterException, IOException {
		return LogoMatrix(getBufferdImage(content), new File(logoFile));
	}

    /**
     * 识别二维码
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static void QRReader(File file) throws IOException, NotFoundException {
        MultiFormatReader formatReader = new MultiFormatReader();
        //读取指定的二维码文件
        BufferedImage bufferedImage =ImageIO.read(file);
        BinaryBitmap binaryBitmap= new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(bufferedImage)));
        //定义二维码参数
        Map hints= new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        com.google.zxing.Result result = formatReader.decode(binaryBitmap, hints);
        //输出相关的二维码信息
        logger.info("解析结果："+result.toString());
        logger.info("二维码格式类型："+result.getBarcodeFormat());
        logger.info("二维码文本内容："+result.getText());
        bufferedImage.flush();
    }

    /**
     * 二维码添加logo
     * @param matrixImage 源二维码图片
     * @param logoFile logo图片
     * @return 返回带有logo的二维码图片
     * 参考：https://blog.csdn.net/weixin_39494923/article/details/79058799
     */
    public static BufferedImage LogoMatrix(BufferedImage matrixImage, File logoFile) throws IOException{
        /**
         * 读取二维码图片，并构建绘图对象
         */
        Graphics2D g2 = matrixImage.createGraphics();

        int matrixWidth = matrixImage.getWidth();
        int matrixHeigh = matrixImage.getHeight();

        /**
         * 读取Logo图片
         */
        BufferedImage logo = ImageIO.read(logoFile);

        //开始绘制图片
        g2.drawImage(logo,matrixWidth/5*2,matrixHeigh/5*2, matrixWidth/5, matrixHeigh/5, null);//绘制
        BasicStroke stroke = new BasicStroke(5,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
        g2.setStroke(stroke);// 设置笔画对象
        //指定弧度的圆角矩形
        RoundRectangle2D.Float round = new RoundRectangle2D.Float(matrixWidth/5*2, matrixHeigh/5*2, matrixWidth/5, matrixHeigh/5,20,20);
        g2.setColor(Color.white);
        g2.draw(round);// 绘制圆弧矩形

        //设置logo 有一道灰色边框
        BasicStroke stroke2 = new BasicStroke(1,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
        g2.setStroke(stroke2);// 设置笔画对象
        RoundRectangle2D.Float round2 = new RoundRectangle2D.Float(matrixWidth/5*2+2, matrixHeigh/5*2+2, matrixWidth/5-4, matrixHeigh/5-4,20,20);
        g2.setColor(new Color(128,128,128));
        g2.draw(round2);// 绘制圆弧矩形

        g2.dispose();
        matrixImage.flush() ;
        return matrixImage ;
    }
}