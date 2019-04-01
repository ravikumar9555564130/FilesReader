
package com.information;

import com.settings.Setting;

public class FileInfo {

	private Setting configuration = null; 

	public FileInfo(String dirPath) {
		configuration = Setting.getConfiguration();
		configuration.setDirectoryPath(dirPath);
	}

	private FileInfo(ReportBuilder reportBuilder) {
		configuration = Setting.getConfiguration();
		configuration.setDirectoryPath(reportBuilder.directoryPath);
		configuration.setConfigProp(reportBuilder.configPropFilepath);
		configuration.setRefreshInterval(reportBuilder.refreshInterval);
		configuration.setSortedOrder(reportBuilder.sortedOrder);
		configuration.setTimeToLive(reportBuilder.timeToLive);
	}

	
	public static class ReportBuilder {
		private String directoryPath;
		private long timeToLive = 60 * 1000;
		private long refreshInterval = 10 * 1000;
		private String sortedOrder = "vowels";
		private String configPropFilepath = "config.properties";

		public ReportBuilder(String directoryPath) {
			this.directoryPath = directoryPath;
		}

		public ReportBuilder setTimeToLive(long timeToLive) {
			this.timeToLive = timeToLive;
			return this;
		}

		public ReportBuilder setRefreshInterval(long refreshInterval) {
			this.refreshInterval = refreshInterval;
			return this;
		}

		public ReportBuilder setSortedOrder(String sortedOrder) {
			this.sortedOrder = sortedOrder;
			return this;
		}

		public ReportBuilder setConfigPropFilepath(String configPropFilepath) {
			this.configPropFilepath = configPropFilepath;
			return this;
		}

		public FileInfo build() {
			return new FileInfo(this);
		}
	}

	public void startDirectoryProcessor() 
	{
		StringBuffer errMsg = new StringBuffer();
		
		Thread processor = new Thread(new Directory(errMsg), "Directory Processor");
		processor.start();

		try
		{
		  processor.join();
		}
		catch (InterruptedException e) {
			System.out.println("Directory Processor is got interrupted. Error: " + e.getMessage());
		}

		System.out.println("Directory Processor is got exit.");
	}

	/*
	 * Following command line arguments are required At First Index: Directory Path
	 * is mandatory
	 */
	public static void main(String args[]) {
		if (args.length <= 0) {
			System.err.println("Directory Path is mandatory");
			System.exit(-1);
		}

		String directoryPath = args[0];

		long ttl = 60 * 1000;
		
		String configProp = "config.properties";
		
		//Default as taken words
		String sortedOrder = "words";

		ReportBuilder reportBuilder = new FileInfo.ReportBuilder(directoryPath);

		FileInfo report = reportBuilder
				.setTimeToLive(ttl)
				.setSortedOrder(sortedOrder)
				.setTimeToLive(ttl)
				.setConfigPropFilepath(configProp)
				.build();

		report.startDirectoryProcessor();
	}
}
