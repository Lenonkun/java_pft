package ru.stqa.pft.github;

import com.google.common.collect.ImmutableMap;
import com.jcabi.github.*;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Map;

public class GitHubTests {
    @Test
    public void testCommits() throws IOException {
        Github github = new RtGithub("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");
        RepoCommits commits = github.repos().get(new Coordinates.Simple("Lenonkun", "java_pft")).commits();
        for (RepoCommit commit :
                commits.iterate(new ImmutableMap.Builder<String, String>().build())) {
            System.out.println(new RepoCommit.Smart(commit).message());

        }
    }
}
