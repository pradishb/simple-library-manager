package slm;


import java.io.*;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.xml.sax.SAXException;
import org.krysalis.barcode4j.BarcodeException;
import org.krysalis.barcode4j.BarcodeGenerator;
import org.krysalis.barcode4j.BarcodeUtil;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import java.awt.image.BufferedImage;


public class BarcodeWriter{
	DefaultConfigurationBuilder builder;
	Configuration cfg;
	BarcodeGenerator gen;

	public BarcodeWriter(){
		try{
			builder = new DefaultConfigurationBuilder();
			cfg = builder.buildFromFile(new File("barcode-cfg.xml"));
			gen = BarcodeUtil.getInstance().createBarcodeGenerator(cfg);
		}
		catch(SAXException e){
			System.out.println(e.toString());
		}
		catch(ConfigurationException e){
			System.out.println(e.toString());
		}
		catch(IOException e){
			System.out.println(e.toString());	
		}
		catch(BarcodeException e){
			System.out.println(e.toString());
		}
	}

	public void write(String url,String msg){
		try{
			File folder = new File(url+"\\barcodes\\");
			folder.mkdirs();
			OutputStream out = new java.io.FileOutputStream(folder+"\\"+msg+".png");
			BitmapCanvasProvider provider = new BitmapCanvasProvider(
		    out, "image/x-png", 300, BufferedImage.TYPE_BYTE_GRAY, true, 0);
			gen.generateBarcode(provider, msg);
			provider.finish();
			out.close();
		}
		catch(IOException e){
			System.out.println(e.toString());	
		}
	}
}