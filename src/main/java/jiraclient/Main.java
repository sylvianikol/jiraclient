package jiraclient;

import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        MyJiraClient myJiraClient = new MyJiraClient(
                System.getenv("username"),
                System.getenv("password"),
                System.getenv("jiraServer"));


        String issue = myJiraClient.createIssue("TEST", 10001L,
                "Do another really important Task", "Task description");

        System.out.println(issue);

        System.out.println(myJiraClient.fetchData());
    }
}
