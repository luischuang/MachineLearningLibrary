package com.penghaisoft.MachineLearningLibrary.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Load {
	
	public synchronized static void loadLib(String libName) throws IOException {  
	    String systemType = System.getProperty("os.name");  
	    String libExtension = (systemType.toLowerCase().indexOf("win")!=-1) ? ".dll" : ".so";  
	      
	    String libFullName = libName + libExtension;  
	      
	    String nativeTempDir = System.getProperty("java.io.tmpdir");  
	      
	    InputStream in = null;  
	    BufferedInputStream reader = null;  
	    FileOutputStream writer = null;  
	      
	    File extractedLibFile = new File(nativeTempDir+File.separator+libFullName);  
	    if(extractedLibFile.exists()){  
	    	extractedLibFile.delete();
	    }
	    try {  
            in = Load.class.getResourceAsStream(libName+libExtension);
            Load.class.getResource(libFullName);
            reader = new BufferedInputStream(in);  
            writer = new FileOutputStream(extractedLibFile);  
              
            byte[] buffer = new byte[1024];  
              
            while (reader.read(buffer) > 0){  
                writer.write(buffer);  
                buffer = new byte[1024];  
            }  
        } catch (IOException e){  
            e.printStackTrace();  
        } finally {  
            if(in!=null)  
                in.close();  
            if(writer!=null)  
                writer.close();  
        }  
	    System.load(extractedLibFile.toString());  
	}


}
