package plugin.classes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.Milestone;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.IssueService;
import org.eclipse.egit.github.core.service.MilestoneService;
import org.eclipse.egit.github.core.service.RepositoryService;

public class GithubConnector {	
	
	private GitHubClient client;
	private Repository repository;
	private RepositoryService repositoryService;
	private IssueService issueService;
	private MilestoneService milestoneService;
	private List<Issue> issues = new ArrayList<Issue>();
	private List<Milestone> milestones = new ArrayList<Milestone>();
	
	public GithubConnector(){
	}
	
	public boolean authenticate(String name, String password){
		client = new GitHubClient();
		client.setCredentials(name, password);
		updateLocalIssues();
		
		if (client != null)
			return true;
		else
			return false;
	}
	
	public void updateLocalIssues() {
		
		repositoryService = new RepositoryService(client);
		
		try {
			for(Repository repo: repositoryService.getRepositories()) {
				System.out.println(repo.getName());
				if(repo.getName().equals(Share.project.getProject().getName())){
					repository = repo;
					break;
				}
			};
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		issueService = new IssueService(client);
		
		try {
			issues = issueService.getIssues(repository, null);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		milestoneService = new MilestoneService(client);
		
		try {
			milestones = milestoneService.getMilestones(client.getUser(), repository.getName(), IssueService.STATE_OPEN);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		print();
	}
	
	public void updateTaskUserInfo() {
		updateLocalIssues();		
		
		for(Issue i: issues) {
			for(Task t: Share.status.getTasks()) {
				if(t.getDescription().equals(i.getTitle().substring(i.getTitle().indexOf("-")+2))) {
					if(i.getAssignee() != null) {
						t.setUser(i.getAssignee().getLogin());
					}
					else {
						t.setUser("none");
					}
					t.setUserStatus(i.getState());
					
					if(t.getProduced().get(0) != null) {
						t.getProduced().get(0).setBeeingWorkedOn(true);
					}
					else {
						System.out.println("no artifact");
					}
				}
			}
		}
	}	
	
	public Issue findIssue(Task t) {
		for(Issue i: issues) {
			System.out.println("TASK" + t.getDescription() + " ISSUE " + i.getTitle());
			String activityAndTask = Share.status.getActiveActivity().getName() + " - " + t.getDescription();
			if(activityAndTask.equals(i.getTitle())) {
				System.out.println("match");
				return i;
			}
		}
		return null;
	}
	
	public void updateIssue(Task t) {
		updateLocalIssues();
		Issue i = findIssue(t);
		
		if (i != null) {
			User u = new User();
			u.setLogin(client.getUser());
			i.setAssignee(u);
			if(t.getStatus().equals("Active")) {
				i.setState(IssueService.STATE_OPEN);
			} else if(t.getStatus().equals("Complete")) {
				i.setState(IssueService.STATE_CLOSED);
			}
			
			try {
				issueService.editIssue(client.getUser(), repository.getName(), i);
			} catch (IOException e) {
				e.printStackTrace();
			}	
			
		} else {			
			i = new Issue();
			
			User u = new User();
			u.setLogin(client.getUser());
			i.setAssignee(u);
			i.setTitle(Share.status.getActiveActivity().getName() + " - " + t.getDescription());
			i.setBody(" ");
			if(t.getStatus().equals("Active")) {
				i.setState(IssueService.STATE_OPEN);
			} else if(t.getStatus().equals("Complete")) {
				i.setState(IssueService.STATE_CLOSED);
			}

			Milestone m = findMilestone(t);
			if(m != null) {
				i.setMilestone(m);
			}
			
			try {
				issueService.createIssue(client.getUser(), repository.getName(), i);				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private Milestone findMilestone(Task t) {
		Goal g = Share.status.findGoal(Share.status.getActiveActivity().getName());
		if (g != null) {
			for (Milestone m: milestones) {
				if(m.getTitle().equals(g.getDescription())) {
					return m;
				}
			}
		}
		return null;
	}

	public void createMilestone(Goal g) {
		Milestone m = new Milestone();
		m.setDescription(g.getDescription());
	    m.setDueOn(g.getDue());
		m.setTitle(g.getDescription());
		m.setState("open");
		try {
			milestoneService.createMilestone(repository, m);
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(Task t : Share.status.findTasks(g)){
			Issue i = findIssue(t);
			if(i != null) {
				i.setMilestone(m);
				try {
					issueService.editIssue(client.getUser(), repository.getName(), i);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}		
	}
	
	public void updateMilestone(Goal g) {
		Milestone m = null;
		for(Milestone mToUpdate: milestones) {
			if(mToUpdate.getTitle().equals(g.getDescription())) {
				m = mToUpdate;
				break;
			}
		}
		if(m != null) {
			m.setDescription(g.getDescription());
			m.setDueOn(g.getDue());
			m.setTitle(g.getDescription());
			m.setState(IssueService.STATE_OPEN);
			try {
				milestoneService.editMilestone(repository, m);
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}		
	}
	
	public void deleteMilestone(Goal g) {
		Milestone m = null;
		for(Milestone mToUpdate: milestones) {
			if(mToUpdate.getTitle().equals(g.getDescription())) {
				m = mToUpdate;
				break;
			}
		}
		if(m != null) {
			try {
				milestoneService.deleteMilestone(repository, m.getTitle());
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
	}	
	
	public void print() {
		System.out.println(client.getUser());
		System.out.println(repository.getName());
		System.out.println(issues);	
		System.out.println(milestones);	
	}
	
	
	public GitHubClient getClient() {
		return client;
	}

	public void setClient(GitHubClient client) {
		this.client = client;
	}

	public Repository getRepository() {
		return repository;
	}

	public void setRepository(Repository repository) {
		this.repository = repository;
	}

	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	public IssueService getIssueService() {
		return issueService;
	}

	public void setIssueService(IssueService issueService) {
		this.issueService = issueService;
	}

	public List<Issue> getIssues() {
		return issues;
	}

	public void setIssues(List<Issue> issues) {
		this.issues = issues;
	}	
}