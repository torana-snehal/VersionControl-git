package com.icedq.versioncontrol.gitFlow;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.CredentialsProvider;

public class InitRepository {

	private static Git git = null;

	public static Repository getGitRepository(CredentialsProvider cp) {
		if (git == null) {
			try {
				// bitbucket- /icedq/git/bitbucket/repo
				File file = new File("/icedq/git/versioncontrol");
				
				// https://github.com/snehalpathak-torana/temp
				// Bit bucket -"https://pathaksn@bitbucket.org/toranainc/ruleversioning"
				final String REMOTE_URL = "https://github.com/torana-snehal/icedq-versioncontrol";
				
				// get git repo instance when local copy exists
				if (file.exists()) {

					try {

						git = Git.open(file);
						git.pull().setCredentialsProvider(cp).call();

					} catch (IOException e) {
						e.printStackTrace();
					}

				}
				// clone remote repo to local
				else {
					git = Git.cloneRepository()
							.setURI(REMOTE_URL)
							.setDirectory(file)
							.setCredentialsProvider(cp)
							.call();
				}

			} catch (InvalidRemoteException e) {
				e.printStackTrace();
			} catch (TransportException e) {
				e.printStackTrace();
			} catch (GitAPIException e) {
				e.printStackTrace();
			}
		}
		return git.getRepository();
	}
}
