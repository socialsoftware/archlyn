package plugin.classes;
import org.eclipse.core.resources.IProject;
import org.eclipse.swt.widgets.Label;

import plugin.parts.PluginView;
import plugin.views.DataView;
import plugin.views.MainActivityView;
import plugin.views.MainTaskView;
import plugin.views.MetadataView;

public class Share {
	
	public static Status status;
	public static Project project;
	public static PluginView pluginView;
	public static MetadataView metadataView;
	public static DataView dataView;
	public static MainActivityView mainActivityView;
	public static MainTaskView mainTaskView;
	public static String pluginFolderName = "pluginFolder";
    public static Label message;
	
	public static void print(String text) {
		System.out.println(text);
	}
	
	public static IProject findProject(String text, IProject[] projects) {
		for (IProject ip: projects) {
			if(ip.getName().equals(text)) {
				return ip;
			}
		}
		return null;
	}
}