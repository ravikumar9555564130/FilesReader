package com.utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import com.constant.Helpable;

public class Util implements Helpable {
	public static boolean isExtMatch(String fileName)
	{
		boolean isExtMatch = false;

		for (String ext : supportedFileFormat) {
			if (fileName.endsWith(ext)) {
				isExtMatch = true;
				break;
			}
		}

		return isExtMatch;
	}

	public static boolean validateDirectory(String directoryPath) {
		File directoryFileObj = new File(directoryPath);

		if (!directoryFileObj.exists())
			return false;

		if (!directoryFileObj.isDirectory())
			return false;

		return true;
	}

	public static Properties readPorp(String filePath) {
		FileReader reader;
		Properties p = new Properties();
		try {
			reader = new FileReader(filePath);
			try {
				p.load(reader);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return p;
	}
}
