package com.ddlab.gitpusher.github.core;

import com.ddlab.gitpusher.bitbucket.core.BitBucketHandlerImpl;
import com.ddlab.gitpusher.core.IGitHandler;
import com.ddlab.gitpusher.core.UserAccount;

public class TestBitbucketSnippetCreator1 {
  private static String userName = "sambittechy@gmail.com";
  private static String password = "pppppppppppp";
//  private static File projectDir = new File("E:/sure-delete-1/temp1");

  public static void main(String[] args) throws Exception {
    UserAccount userAccount = new UserAccount(userName, password);
    IGitHandler gitHubHandler = new BitBucketHandlerImpl(userAccount);
        String[] gists = gitHubHandler.getGists();
        for (String gist : gists) {
          System.out.println("Gist " + gist);
        }

//    File file =
//        new File(
//            "E:/java-dec-2018/tempParser1/src/main/java/com/ddlab/rnd/UserNameParser.java");
//    gitHubHandler.createGist(file, "It is a common utility for parsing");
//    System.out.println("Gist created successfully...");
  }
}
