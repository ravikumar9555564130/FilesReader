package com.settings;

public class Setting
{
	private String directoryPath;
	private long timeToLive = 60 * 1000;
	private long refreshInterval = 10 * 1000;
	private String sortedOrder = "vowels";
	private String configProp = "config.properties";
	
	private static Setting configuration = null;
	
	private Setting() {}
	
	public static Setting getConfiguration()
	{
		if(configuration == null)
		{
			synchronized (Setting.class) 
			{
			  if(configuration == null)
			  {
				  configuration = new Setting();
			  }
			}
		}
		
		return configuration;
	}
	
	public String getConfigProp() {
		return configProp;
	}

	public void setConfigProp(String configProp) {
		this.configProp = configProp;
	}

	public String getDirectoryPath() {
		return directoryPath;
	}
	public void setDirectoryPath(String directoryPath) {
		this.directoryPath = directoryPath;
	}
	public long getTimeToLive() {
		return timeToLive;
	}
	public void setTimeToLive(long timeToLive) {
		this.timeToLive = timeToLive;
	}
	public long getRefreshInterval() {
		return refreshInterval;
	}
	public void setRefreshInterval(long refreshInterval) {
		this.refreshInterval = refreshInterval;
	}
	public String getSortedOrder() {
		return sortedOrder;
	}
	public void setSortedOrder(String sortedOrder) {
		this.sortedOrder = sortedOrder;
	}
	
	@Override
	public String toString() {
		return "Configuration [directoryPath=" + directoryPath + ", timeToLive=" + timeToLive + ", refreshInterval="
				+ refreshInterval + ", sortedOrder=" + sortedOrder + "]";
	}
	
	
	
}
