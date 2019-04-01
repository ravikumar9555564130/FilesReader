package com.creation;

import java.io.File;
import java.io.IOException;

import com.constant.Helpable;
import com.information.Model;
import com.settings.Setting;

public class MetaFiles implements InformationCreator, Helpable {
	private Setting configuration = null;
	private InformationCreator processFile;

	public MetaFiles() {
		configuration = Setting.getConfiguration();
	}

	@Override
	public void setNextReportGenerator(InformationCreator processFile) {
		this.processFile = processFile;
	}

	@Override
	public boolean generateReport(Model fileModel, File file, String directoryPath) {
		File currentDirectory = new File(directoryPath);

		String aggregateDirPath = directoryPath + File.separator + currentDirectory.getName() + ".smtd";
		
		System.out.println("Sorted Directory path = " + directoryPath + " -- " + currentDirectory.getParentFile().getName()  );

		File aggDirObj = new File(aggregateDirPath);

		if (!aggDirObj.exists())
			aggDirObj.mkdir();

		String sortedfileName = file.getName() + "_";

		if (configuration.getSortedOrder().equalsIgnoreCase(WORDS))
			sortedfileName = sortedfileName + fileModel.getWords();
		else if (configuration.getSortedOrder().equalsIgnoreCase(VOWELS))
			sortedfileName = sortedfileName + fileModel.getVowels();
		else if (configuration.getSortedOrder().equalsIgnoreCase(SEPECIAL_CHARS))
			sortedfileName = sortedfileName + fileModel.getSpecialChars();

		System.out.println("Sorted Meta Dir :" + aggregateDirPath + File.separator + sortedfileName );
		File fileObj = new File(aggregateDirPath + File.separator + sortedfileName);

		try {

			if (fileObj.exists())
				fileObj.delete();

			fileObj.createNewFile();
			
			processFile.generateReport(fileModel, file, directoryPath);
			
			return true;
		} 
		catch (IOException e) {
			System.out.println("Exception: " + file.getAbsolutePath()
					+ ", Error: " + e.getMessage());
			return false;
		}
		

	}

}
