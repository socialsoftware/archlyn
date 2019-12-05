package plugin.parts;

import javax.annotation.PostConstruct;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import plugin.classes.Share;
import plugin.composites.PluginComposite;

public class PluginView {
	
	private PluginComposite pluginComposite;	

	@PostConstruct
	public void createPartControl(Composite parent) {
		Share.pluginView = this;
		pluginComposite = new PluginComposite(parent, SWT.NONE);	
	}
	
	public PluginComposite getPluginComposite() {
		return pluginComposite;
	}

	public void setPluginComposite(PluginComposite pluginComposite) {
		this.pluginComposite = pluginComposite;
	}

	@Focus
	public void setFocus() {	
	}	
}