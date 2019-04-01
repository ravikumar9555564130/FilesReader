package com.information;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

import com.constant.Helpable;
import com.settings.Setting;
import com.utilities.Util;

public class Directory implements Runnable, Helpable
{
	private String directoryPath;
	private long startTime = -1L;
	private StringBuffer errMsg;
	private byte status = NOT_STARTED;
	private Cache reportCache;
	private HashMap<String, File> currentFileMap = new HashMap<>();
	private ExecutorService executorService = null;
    private Setting configuration = null;
    
	public Directory(StringBuffer errMsg) 
	{
		//Initializing configuration object
		this.configuration = Setting.getConfiguration();
		this.directoryPath = configuration.getDirectoryPath();
		this.errMsg = errMsg;
		Thread thread = new Cache();
		thread.start();
		reportCache = (Cache) thread;
	}

	private void stopExecutor() {
		if (executorService != null)
			executorService.shutdown();

		reportCache.setToBeStopped(true);
	}

	public void run() {
		System.out.println("Starting directory Processor");

		if (!Util.validateDirectory(directoryPath)) {
			errMsg.append("Given directory path(" + this.directoryPath + ") is invalid");
			System.err.println("Given directory is invalid");
			status = STOPPED;
			return;
		}

		startTime = System.currentTimeMillis();
		
		status = RUNNING;
		
		try {
			while (true) {

				currentFileMap.clear();

				byte searchStatus = searchFilesInReportDirectories();

				if (searchStatus == FOUND) {
					if (executorService == null) {
						
						executorService = Executors.newCachedThreadPool();
					}

					Set<String> fileSet = currentFileMap.keySet();

					for (String currentFile : fileSet) {
						File currentFileObj = currentFileMap.get(currentFile);

						System.out.println("processing file :(" + currentFile + ") ");
						executorService.execute(new Process(currentFileObj.getAbsolutePath(),
								currentFileObj.getParentFile().getAbsolutePath(), reportCache));
					}
				}

				if (searchStatus == EXIT) {
					errMsg.append("Dir Processing is stopped for :" + this.directoryPath + ".");
					System.err.println("Directory Process is stopped for Directory:" + this.directoryPath + ".");

					return;
				}

				System.out.println("Dir Processor is waiting for " + configuration.getRefreshInterval() + " ms");
				Thread.sleep(configuration.getRefreshInterval());
			}
		} catch (InterruptedException ie) {
			System.err.println("Directory Processor has been interrupted : " + ie.getMessage());
		} catch (Exception e) {
			System.err.println("Processing directory(" + directoryPath + "). Error: " + e.getMessage());
		}
		finally {
			status = STOPPED;
			stopExecutor();
		}
	}

	public void searchFileInDir(String directoryPath, List<File> files) {
		File directory = new File(directoryPath);
		File[] fileList = directory.listFiles();

		if (fileList != null) {
			for (File file : fileList) {
				if (file.isFile()) {
					files.add(file);
				} else if (file.isDirectory()) {
					searchFileInDir(file.getAbsolutePath(), files);
				}
			}
		}
	}

	private byte searchFilesInReportDirectories() 
	{
		if (!Util.validateDirectory(directoryPath))
			return EXIT;

		List<File> fileList = new ArrayList<File>();

		searchFileInDir(directoryPath, fileList);
		
		Stream<File> fileListStream = fileList.stream();
		fileListStream.filter(fileObj -> Util.isExtMatch(fileObj.getName()))
		               .filter(fileObj -> fileObj.lastModified() >= startTime)
		               .filter(fileObj -> reportCache.get(fileObj.getAbsolutePath()) == null)
		               .forEach(fileObj -> currentFileMap.put(fileObj.getAbsolutePath(), fileObj));

		if (currentFileMap.size() > 0) {
			System.out.println("Following files need to be processed + " + currentFileMap.keySet());
			return FOUND;
		} else {
			System.out.println("No files founds to be processed for directory" + directoryPath);
			return NOT_FOUND;
		}
	}
	
	public StringBuffer getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(StringBuffer errMsg) {
		this.errMsg = errMsg;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}
}
