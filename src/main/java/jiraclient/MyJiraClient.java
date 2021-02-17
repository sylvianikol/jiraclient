package jiraclient;

import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.BasicIssue;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.IssueType;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.jira.rest.client.api.domain.input.IssueInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInputBuilder;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import io.atlassian.util.concurrent.Promise;

import java.net.URI;
import java.util.concurrent.ExecutionException;

public class MyJiraClient {

    private String username;
    private String password;
    private String jiraUrl;
    private JiraRestClient restClient;

    public MyJiraClient() {
    }

    public MyJiraClient(String username, String password, String jiraUrl) {
        this.username = username;
        this.password = password;
        this.jiraUrl = jiraUrl;
        this.restClient = getJiraRestClient();
    }

    private JiraRestClient getJiraRestClient() {
        return new AsynchronousJiraRestClientFactory()
                .createWithBasicHttpAuthentication(getJiraUri(), this.username, this.password);
    }

    private URI getJiraUri() {
        return URI.create(this.jiraUrl);
    }

    public String createIssue(String project, Long key, String summary, String description) {
        IssueRestClient issueClient = restClient.getIssueClient();

        IssueInputBuilder issueBuilder = new IssueInputBuilder(project, key, summary);
        issueBuilder.setDescription(description);

        IssueType issueType = new IssueType(getJiraUri(), key, summary, false, description, null);

        issueBuilder.setIssueType(issueType);
        IssueInput issueInput  = issueBuilder.build();

        Promise<BasicIssue> promise = issueClient.createIssue(issueInput);
        BasicIssue basicIssue  = promise.claim();

        Promise<Issue> promiseIssue = issueClient.getIssue(basicIssue.getKey());

        Issue issue = promiseIssue.claim();
        return issue.getKey();



//        IssueRestClient issueClient = restClient.getIssueClient();
//        IssueInput newIssue = new IssueInputBuilder(
//                project, key, summary).build();
//        return issueClient.createIssue(newIssue).claim().getKey();

    }

    public String fetchData() throws ExecutionException, InterruptedException {
        String JQL = "issuetype = Bug AND status = 'In Progress'";

        SearchResult result = this.restClient.getSearchClient().searchJql(JQL).get();

        return result.toString();
    }
}
