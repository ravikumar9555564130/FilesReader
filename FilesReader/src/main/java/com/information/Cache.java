package com.information;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.settings.Setting;
import com.utilities.Util;

public class Cache extends Thread {
	private long lastCleanupTime = -1;
	private boolean isToBeStopped = false;
	private Setting configuration = null;
	private Properties properties = null;
	private Map<String, Long> cacheMap = new ConcurrentHashMap<>();

	public Cache() {

		configuration = Setting.getConfiguration();

		if (configuration.getRefreshInterval() > configuration.getTimeToLive())
			configuration.setRefreshInterval(configuration.getTimeToLive());

		cacheMap = new HashMap<String, Long>();
	}

	public void put(String fileName, long timeStamp) {
		cacheMap.put(fileName, timeStamp);
	}

	public boolean isToBeStopped() {
		return isToBeStopped;
	}

	public void setToBeStopped(boolean isToBeStopped) {
		this.isToBeStopped = isToBeStopped;
	}

	public void run() {
		System.out.println("TTL Cache has been invoked");

		while (true) {
			try {
				if (lastCleanupTime == -1) {
					lastCleanupTime = System.currentTimeMillis();
				} else if ((System.currentTimeMillis() - lastCleanupTime) >= configuration.getTimeToLive()) {
					cleanup();
				}

				if (isToBeStopped) {
					return;
				}

				System.out.println("Cache is sleeping(" + configuration.getRefreshInterval() + " ms) for next cleanup");

				Thread.sleep(configuration.getRefreshInterval());

			} catch (InterruptedException ie) {
				isToBeStopped = true;
			} catch (Exception e) {
				isToBeStopped = true;
			}
		}
	}

	public Long get(String fileName) {
		Long timeStamp = cacheMap.get(fileName);

		if (timeStamp == null)
			return null;
		else {
			return timeStamp;
		}
	}

	public void remove(String key) {
		cacheMap.remove(key);
	}

	public int size() {
		return cacheMap.size();
	}

	private void cleanup() {
		if (size() <= 0)
			return;

		Stream<File> filePathStream = cacheMap.keySet().stream().map(filePath -> new File(filePath)).collect(Collectors.toList())
				.stream();

		filePathStream.filter(fileObj -> fileObj.exists()).forEach(fileObj -> {

			long timeStamp = get(fileObj.getAbsolutePath());

			if ((System.currentTimeMillis() - fileObj.lastModified()) >= configuration.getTimeToLive()) {
				System.out.println("Removing file from cache due to modified file = " + fileObj.getAbsolutePath());
				remove(fileObj.getAbsolutePath());
			}

			if (fileObj.lastModified() > timeStamp) {
				System.out.println("Removing file from cache due to timeout = " + fileObj.getAbsolutePath());
				remove(fileObj.getAbsolutePath());
			}

		});

		properties = Util.readPorp(configuration.getConfigProp());

		if (properties != null && properties.getProperty("sortedOrder") != null) {
			configuration.setSortedOrder(properties.getProperty("sortedOrder"));
		}

	}
}
