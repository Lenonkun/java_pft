package ru.stqa.pft.rest;

import org.testng.annotations.Test;

import java.io.IOException;

public class StatusIssueTest extends TestBase{
    @Test
    public void testIssueStatus () throws IOException {

        skipIfNotFixed(8);
        System.out.println("Issue fixed");

    }
}
