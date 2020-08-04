package com.icedq.versioncontrol.gitFlow;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.icedq.versioncontrol.fileOperations.FileOperations;
import com.icedq.versioncontrol.model.Customer;
import com.icedq.versioncontrol.model.ICERuleResponse;
import com.icedq.versioncontrol.model.RuleVersions;
import com.icedq.versioncontrol.properties.Constants;

public class GitOperations {

	Repository repository;
	private CredentialsProvider credentialProvider;
	private RuleMetadata ruleBucket;
	private ObjectMapper objMapper;
	private static String METADATA = "Metadata";
	private static String RULEDATA = "Ruledata";
	private static String FILE_EXT = "json";
	private static String README = "README.md";

	public GitOperations() throws JsonMappingException, JsonProcessingException {
		// GIT - "torana-snehal", "Torana@2020"
		// BIT Bucket - "pathaksn", "Torana@2020"
		objMapper = new ObjectMapper();
		credentialProvider = new UsernamePasswordCredentialsProvider("torana-snehal", "Torana@2020");

		// initialize repository
		repository = InitRepository.getGitRepository(credentialProvider);

		// load rule-bucket
		ruleBucket = new RuleMetadata();
		loadRuleBucket();
	}

	@SuppressWarnings("unchecked")
	private void loadRuleBucket() throws JsonMappingException, JsonProcessingException {
		String filename = METADATA + "." + FILE_EXT;
		ByteArrayOutputStream stream = getFileData(filename);

		if (stream.size() > 0) {
			ruleBucket.setRuleMetaData(objMapper.readValue(stream.toString(), Map.class));
		}
	}

	// add file to repository
	public List<String> addFileToRepo(Customer cust, ICERuleResponse rRes) {
		try {
			Git git = new Git(repository);

			// create the folder
			File ruleFile = createFile();
			File metaData = createMetadataFile();

			// append contents
			FileOperations.getInstance().writeToFile(rRes, ruleFile);

			// and then commit the changes.
			commit(git, cust);

			// add metadata contents
			List<String> latestVerlist = addNewCommitIdtoBucket(metaData, ruleFile);
			commit(git, cust);
			// push staged changes to remote repository
			push(git);
			
			return latestVerlist;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	// get file contents from repo
	public ByteArrayOutputStream getFileData(String filename) {
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		try {
			TreeWalk treeWalk = getTreeWalk();
			data = iterateTreeTogetFileContents(treeWalk, filename);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return data;
	}

	private ByteArrayOutputStream iterateTreeTogetFileContents(TreeWalk treeWalk, String filename) {

		ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
		try {
			while (treeWalk.next()) {
				if (treeWalk.getPathString().contentEquals(filename)) {
					ObjectId objectId = treeWalk.getObjectId(0);
					ObjectLoader loader = repository.open(objectId);

					// and the loader to read the file
					loader.copyTo(dataStream);
					repository.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dataStream;
	}

	public List<RuleVersions> getAllRuleVersions() {

		List<RuleVersions> dirList = new ArrayList<RuleVersions>();
	
		try {
			for (Map.Entry<String, RuleMetadata.RuleVersionData> val : ruleBucket.getRuleMetaData().entrySet()) {
				RuleMetadata.RuleVersionData data = objMapper.convertValue(val.getValue(),
						RuleMetadata.RuleVersionData.class);
				dirList.add(new RuleVersions(data.getVersion()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dirList;
	}

	private TreeWalk getTreeWalk() {

		// use a TreeWalk to iterate over all files in the Tree recursively
		TreeWalk treeWalk = null;
		RevTree tree;
		try {

			tree = getTree();
			treeWalk = new TreeWalk(repository);
			treeWalk.addTree(tree);
			treeWalk.setRecursive(true);

		} catch (MissingObjectException e) {
			e.printStackTrace();
		} catch (IncorrectObjectTypeException e) {
			e.printStackTrace();
		} catch (CorruptObjectException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return treeWalk;
	}

	private RevTree getTree() {
		Ref head;
		RevTree tree = null;

		RevCommit commit;
		try {
			head = repository.exactRef("HEAD");

			// a RevWalk allows to walk over commits based on some filtering that is defined
			RevWalk walk = new RevWalk(repository);

			commit = walk.parseCommit(head.getObjectId());

			tree = commit.getTree();

		} catch (MissingObjectException e) {
			e.printStackTrace();
		} catch (IncorrectObjectTypeException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tree;
	}

	// create file
	private File createFile() {
		try {
			// create the file
			File ruleFile = new File(repository.getDirectory().getParent(), RULEDATA + "." + FILE_EXT);

			ruleFile.createNewFile();

			return ruleFile;

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private File createMetadataFile() {
		try {
			// create the file
			File metadataFile = new File(repository.getDirectory().getParent(), METADATA + "." + FILE_EXT);

			metadataFile.createNewFile();

			return metadataFile;

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// commit
	private void commit(Git git, Customer cust) {
		try {

			git.add().addFilepattern(".").call();

			git.tag();
			CommitCommand commitcmd = git.commit();
			commitcmd.setAuthor(cust.getFirstName() + cust.getLastName(), cust.getEmail());
			commitcmd.setMessage(Constants.commitMessage);
			commitcmd.call();

		} catch (GitAPIException e) {
			e.printStackTrace();
		}
	}

	// push
	private void push(Git git) {
		try {
			git.push().setCredentialsProvider(credentialProvider).call();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
	}

	// TODO what is better throws or try catch
	private List<String> addNewCommitIdtoBucket(File file, File ruleFile)
			throws MissingObjectException, IncorrectObjectTypeException, IOException {
		String latestCommitId = getLatestCommitId();

		List<String> latestVersionList = loadBucket(latestCommitId, ruleFile);

		// add to file
		FileOperations.getInstance().writeToMetaDataFile(ruleBucket.getRuleMetaData(), file);
		
		return latestVersionList;
	}

	private List<String> loadBucket(String latestCommitId, File ruleFile) {
		RuleMetadata.RuleVersionData verData = new RuleMetadata.RuleVersionData();
		Map<String, RuleMetadata.RuleVersionData> map = ruleBucket.getRuleMetaData();
		List<String> versionList = new ArrayList<String>();
		verData.setObjectId(latestCommitId);
		verData.setFileName(ruleFile.getName());
		if (ruleBucket.isEmpty()) {
			verData.setVersion("1");
			
		} else {
			verData.setVersion(Integer.toString(map.size() + 1));
		}
		//add version no and commit Id
		versionList.add(verData.getVersion());
		versionList.add(verData.getObjectId());
		
		map.put(UUID.randomUUID().toString(), verData);
		ruleBucket.setRuleMetaData(map);
		
		return versionList;
	}

	// Get Latest commit id
	private String getLatestCommitId() throws MissingObjectException, IncorrectObjectTypeException, IOException {
		RevWalk revWalk = new RevWalk(repository);
		ObjectId head = repository.resolve("HEAD");
		if (head != null) {
			RevCommit commit = revWalk.parseCommit(head);
			return commit.getId().getName();
		}
		return "null";
	}

	public ByteArrayOutputStream readFileContentsFromCommitId(String verNo, String fileName) throws IOException {

		String id = getRuleVersion(verNo, fileName);

		Git git = new Git(repository);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		Iterable<RevCommit> logs = null;
		try {
			logs = git.log().call();
		} catch (NoHeadException e) {
			e.printStackTrace();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}

		for (RevCommit commit : logs) {
			String commitID = commit.getName();
			if (commitID != null && !commitID.isEmpty() && commitID.equals(id)) {

				ObjectId treeId = commit.getTree();
				TreeWalk treeWalk = getTreeWalk();
				treeWalk.reset(treeId);

				while (treeWalk.next()) {
					if (treeWalk.getPathString().equals(fileName)) {
						ObjectId objectId = treeWalk.getObjectId(0);
						ObjectLoader loader = repository.open(objectId);

						loader.copyTo(baos);
					}
				}
				break;
			}
		}
		return baos;
	}

	private String getRuleVersion(String ver, String fileName) {
		for (Map.Entry<String, RuleMetadata.RuleVersionData> entry : ruleBucket.getRuleMetaData().entrySet()) {
			RuleMetadata.RuleVersionData data = objMapper.convertValue(entry.getValue(),
					RuleMetadata.RuleVersionData.class);
			if (data.getVersion().equals(ver) && data.getFileName().equals(fileName)) {
				return data.getObjectId();
			}
		}
		return null;
	}

	// temp function for testing
	public void push() {
		Git git = new Git(repository);
		push(git);
	}

	public void loadObjectId() throws NoHeadException, GitAPIException, MissingObjectException,
			IncorrectObjectTypeException, CorruptObjectException, IOException {
		Git git = new Git(repository);

		RevWalk revWalk = new RevWalk(repository);
		Map<String, ObjectId> bucketObjectIds = new HashMap<>();
		for (RevCommit commit : git.log().all().call()) {
			RevTree tree = commit.getTree();

			try (final TreeWalk treeWalk = new TreeWalk(repository)) {
				treeWalk.addTree(tree);
				treeWalk.setRecursive(true);
				treeWalk.setFilter(PathFilter.create("README.md").negate());

				bucketObjectIds = new HashMap<>();

				while (treeWalk.next()) {
					if (treeWalk.isSubtree()) {
						treeWalk.enterSubtree();
					} else {
						String pathString = treeWalk.getPathString();
						if (pathString.endsWith("/InputView.json")) {
							bucketObjectIds.put(treeWalk.getPathString(), treeWalk.getObjectId(0));
							System.out.println("objectId--" + treeWalk.getObjectId(0));
						}
					}
				}
				// load

			}
		}
	}

	public void loadBucket(Map<String, ObjectId> bucketObjectIds)
			throws JsonParseException, JsonMappingException, IOException {
		if (bucketObjectIds.isEmpty()) {
			return;
		}
		for (String bucketFilePath : bucketObjectIds.keySet()) {
			final ObjectId bucketObjectId = bucketObjectIds.get(bucketFilePath);
			final Map<String, Object> bucketMeta = null;
			ObjectMapper objMap = new ObjectMapper();
			try (InputStream bucketIn = repository.newObjectReader().open(bucketObjectId).openStream()) {
				objMap.readValue(bucketIn, bucketMeta.getClass());
			}

			// final String bucketId = (String) bucketMeta.get(bucketId);
			System.out.println(bucketMeta);
		}

	}

}
