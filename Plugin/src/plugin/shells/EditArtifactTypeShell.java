package plugin.shells;

import static org.eclipse.swt.events.SelectionListener.widgetSelectedAdapter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import plugin.classes.ArtifactType;
import plugin.classes.Component;
import plugin.classes.Share;

public class EditArtifactTypeShell extends Shell {

	ArrayList<ArtifactType> artifactTypes;

	public EditArtifactTypeShell(Display display, TableViewer tableAT, boolean edit) {
		super(display, SWT.SHELL_TRIM);
		artifactTypes = new ArrayList<ArtifactType>();
		for(TableItem t: tableAT.getTable().getItems()) {
			if (t.getChecked() && !t.getText(0).equals("Start")) {
				artifactTypes.add(Share.status.findArtifactType(t.getText(0)));
			}
		}
		createContents(tableAT, edit);
	}

	protected void createContents(TableViewer tableAT, boolean edit) {
		if(edit) {
			setText("Edit Artifact Type");
		}else {
			setText("New Artifact Type");
		}			
		setSize(300, 300);
		setLayout(new GridLayout(1, false));

		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
		GridLayout gl_composite = new GridLayout(1, false);
		gl_composite.verticalSpacing = 15;
		composite.setLayout(gl_composite);

		Label labelChooseATname = new Label (composite, SWT.NONE);
		labelChooseATname.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		Text textChooseATname = new Text (composite, SWT.NONE);
		GridData gd_textChooseATname = new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1);
		gd_textChooseATname.minimumWidth = 140;		
		textChooseATname.setLayoutData(gd_textChooseATname);
		Label labelChooseATextension = new Label (composite, SWT.NONE);
		labelChooseATextension.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		Text textChooseATextension = new Text (composite, SWT.NONE);
		textChooseATextension.setLayoutData(gd_textChooseATname);
		Label labelChooseATreward = new Label (composite, SWT.NONE);
		labelChooseATreward.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		Text textChooseATreward = new Text (composite, SWT.NONE);
		textChooseATreward.setLayoutData(gd_textChooseATname);
		Button buttonUpdateArtifactType = new Button (composite, SWT.CENTER);
		buttonUpdateArtifactType.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));

		labelChooseATname.setText("Name");
		if(edit && artifactTypes.size() == 1) {
			textChooseATname.setText(artifactTypes.get(0).getType());
		}

		labelChooseATextension.setText("Extension");	
		if(edit && artifactTypes.size() == 1) {
			textChooseATextension.setText(artifactTypes.get(0).getExtension());
		}

		labelChooseATreward.setText("Reward");	
		if(edit && artifactTypes.size() == 1) {
			textChooseATreward.setText(artifactTypes.get(0).getReward().toString());
		}

		buttonUpdateArtifactType.setText ("OK");
		buttonUpdateArtifactType.addSelectionListener(widgetSelectedAdapter(g -> {	
			if(!edit && !textChooseATname.getText().equals("") && !textChooseATextension.getText().equals("") && !textChooseATreward.getText().equals("")) {
				ArtifactType at = new ArtifactType(textChooseATname.getText(),textChooseATextension.getText());
				try {
					if(!Double.valueOf(textChooseATreward.getText()).isNaN()) {
						at.setReward(Double.valueOf(textChooseATreward.getText()));	
					}
				}catch(NumberFormatException exception) {
				}
				Share.status.getArtifactTypes().add(at);
				if (Share.status.findComponent("/"+Share.pluginFolderName+"/package"+at.getType()) == null) {
					try {
						Files.createDirectory(Paths.get(Share.project.getLocationPlugin()+"/package"+at.getType()));
						Share.status.rescanArtifacts();
						Component c = Share.status.findComponent("/"+Share.pluginFolderName+"/package"+at.getType());
						c.setType(at);
						c.setDefaultForType(false);
						ResourcesPlugin.getWorkspace().getRoot().refreshLocal(org.eclipse.core.resources.IResource.DEPTH_INFINITE,null);
					} catch (IOException | CoreException e1) {
						e1.printStackTrace();
					}
				}	
			} else if(edit && !artifactTypes.isEmpty()) {
				if(!textChooseATname.getText().equals("") && artifactTypes.size() == 1 ) {
					ArtifactType at = artifactTypes.get(0);
					try {
						File f = new File(Share.project.getLocationPlugin()+"/package"+at.getType());
						if (f.exists()) {
							Component c = Share.status.findComponent("/"+Share.pluginFolderName+"/package"+at.getType());
							c.setPath("/"+Share.pluginFolderName+"/package"+textChooseATname.getText());
							c.setName("package"+textChooseATname.getText());
							f.renameTo(new File(Share.project.getLocationPlugin()+"/package"+textChooseATname.getText()));
							ResourcesPlugin.getWorkspace().getRoot().refreshLocal(org.eclipse.core.resources.IResource.DEPTH_INFINITE,null);
						}					
					} catch (CoreException e1) {
						e1.printStackTrace();
					}
					artifactTypes.get(0).setType(textChooseATname.getText());
				}
				if(!textChooseATextension.getText().equals("")) {
					for(ArtifactType at: artifactTypes) {
						at.setExtension(textChooseATextension.getText());
					}
				}
				if(!textChooseATreward.getText().equals("")) {
					for(ArtifactType at: artifactTypes) {
						try {
							if(!Double.valueOf(textChooseATreward.getText()).isNaN()) {
								at.setReward(Double.valueOf(textChooseATreward.getText()));	
							}
						}catch(NumberFormatException exception) {
						}
					}
				}
			}
			for(TableItem t: tableAT.getTable().getItems()) {
				if (t.getChecked()) {
					t.setChecked(false);
				}
			}
			Share.project.saveStatus();
			this.dispose();
		}));	

		this.pack();
		this.setMinimumSize(500, 300);
		this.redraw();
		this.update();
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}