package plugin.classes;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.PlatformUI;

public class View {

	protected IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
	protected ArrayList<Widget> widgets = new ArrayList<Widget>();
	protected ArrayList<TableViewer> viewers = new ArrayList<TableViewer>();
	protected FocusListener listenerClearText = new FocusListener() {
		@Override
		public void focusGained(FocusEvent arg0) {
			((Text) arg0.getSource()).setText("");	
		}public void focusLost(FocusEvent e){}
	};
	protected TimerTask refreshViewers = null;
	protected Timer timer;
	protected TabFolder views = null;
	protected ScrolledComposite scrolledComposite = null;
	protected Point resizePoint = null;

	public View(){	
	}

	public void create(TabFolder views) {		
		this.views = views;
	}		

	public void startRefresh() {
		resizePoint = viewers.get(0).getControl().getParent().computeSize(SWT.DEFAULT, SWT.DEFAULT);
		refreshViewers = new TimerTask() {
			public void run() {
				org.eclipse.swt.widgets.Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						for(TableViewer v: viewers) {
							if(v != null) {
								v.refresh();
								if(v.getControl().getParent().computeSize(SWT.DEFAULT, SWT.DEFAULT).y > resizePoint.y - 100) {
									resizePoint = v.getControl().getParent().computeSize(SWT.DEFAULT, SWT.DEFAULT);
									resizePoint.y = resizePoint.y + 100;
								}
							}
							scrolledComposite.layout(true, true);							
			                scrolledComposite.setMinSize(resizePoint);
			                
			                
						}
						
					}
				});
			}
		};
		timer = new Timer();
		timer.schedule(refreshViewers , 0, 3000);
	}	
	
	public void stopRefresh() {
		if(timer != null) {
			timer.cancel();
		}		
	}

	public IWorkspaceRoot getRoot() {
		return root;
	}

	public void setRoot(IWorkspaceRoot root) {
		this.root = root;
	}

	public ArrayList<Widget> getWidgets() {
		return widgets;
	}

	public void setWidgets(ArrayList<Widget> widgets) {
		this.widgets = widgets;
	}

	public ArrayList<TableViewer> getViewers() {
		return viewers;
	}

	public void setViewers(ArrayList<TableViewer> viewers) {
		this.viewers = viewers;
	}

	public FocusListener getListenerClearText() {
		return listenerClearText;
	}

	public void setListenerClearText(FocusListener listenerClearText) {
		this.listenerClearText = listenerClearText;
	}

	public TimerTask getRefreshViewers() {
		return refreshViewers;
	}

	public void setRefreshViewers(TimerTask refreshViewers) {
		this.refreshViewers = refreshViewers;
	}

	public Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	public TabFolder getViews() {
		return views;
	}

	public void setViews(TabFolder views) {
		this.views = views;
	}

	public ScrolledComposite getScrolledComposite() {
		return scrolledComposite;
	}

	public void setScrolledComposite(ScrolledComposite scrolledComposite) {
		this.scrolledComposite = scrolledComposite;
	}	
	
	public Display getDisplay() {
		return PlatformUI.getWorkbench().getDisplay();
	}
}