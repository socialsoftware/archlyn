package plugin.classes;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.FrameworkUtil;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonTranslator {

	public JsonTranslator(){
	}
	
	public void write(File f, Status status) {
		
		Gson gson = new GsonBuilder()
			    .setPrettyPrinting()
			    .disableHtmlEscaping()
			    .create();
		FileWriter fw;
		
		try {
			fw = new FileWriter(f);
			gson.toJson(status, Status.class, fw);
			fw.flush();
			fw.close();			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Status read(File f) {
		
		Gson gson = new GsonBuilder()
			    .setPrettyPrinting()
			    .disableHtmlEscaping()
			    .create();
		FileReader fr;
		Status status = null;
		
		try {
			fr = new FileReader(f);
			status = gson.fromJson(fr, Status.class);
			fr.close();	
			status.fixReferences();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return status;
	}

	public void writeCurrentProjectName(File f, String name) {
		Gson gson = new GsonBuilder()
			    .setPrettyPrinting()
			    .disableHtmlEscaping()
			    .create();
		FileWriter fw;
		
		try {
			fw = new FileWriter(f);
			gson.toJson(name, String.class, fw);
			fw.flush();
			fw.close();			
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public String readCurrentProjectName() {
		
		File f = new File(Platform.getStateLocation(FrameworkUtil.getBundle(getClass()))+"/CurrentProjectName.json");
		
		if (f.exists()) {
			Gson gson = new GsonBuilder()
				    .setPrettyPrinting()
				    .disableHtmlEscaping()
				    .create();
			FileReader fr;
			String projectName = null;
			
			try {
				fr = new FileReader(f);
				projectName = gson.fromJson(fr, String.class);
				fr.close();	
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return projectName;			
		}	
		return null;
	}	
}