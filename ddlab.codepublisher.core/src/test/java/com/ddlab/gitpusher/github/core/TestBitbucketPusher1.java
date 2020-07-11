package com.ddlab.gitpusher.github.core;

import com.ddlab.gitpusher.bitbucket.core.BitBucketHandlerImpl;
import com.ddlab.gitpusher.bitbucket.core.BitBucketPusherImpl;
import com.ddlab.gitpusher.core.IGitHandler;
import com.ddlab.gitpusher.core.IGitPusher;
import com.ddlab.gitpusher.core.UserAccount;

import java.io.File;

public class TestBitbucketPusher1 {
  private static String USERNAME = "sambittechy@gmail.com";
  private static String PASSWORD = "abcd@1234";
  private static File PROJECTDIR = new File("E:/sure-delete-1/temp1");
  private static String PROJECTDESC = "Some short description";

  public static void main(String[] args) throws Exception {
    UserAccount userAccount = new UserAccount(USERNAME, PASSWORD);
    IGitHandler gitHubHandler = new BitBucketHandlerImpl(userAccount);
    IGitPusher pusher = new BitBucketPusherImpl(gitHubHandler);
    try {
      pusher.pushCodeDirectly(PROJECTDIR, PROJECTDESC);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
