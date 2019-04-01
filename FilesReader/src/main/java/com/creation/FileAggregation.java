package com.creation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.information.Model;

public class FileAggregation implements InformationCreator
{
	private InformationCreator processFile = null;
	
	@Override
	public void setNextReportGenerator(InformationCreator processFile) {
		this.processFile = processFile;
	}

	@Override
	public boolean generateReport(Model fileModel, File file, String dirPath) 
	{
		File currentDirectory = new File(dirPath);

		String aggregateDirPath = dirPath + File.separator + currentDirectory.getName() + ".dmtd";

		File aggDirObj = new File(aggregateDirPath);

		if (!aggDirObj.exists())
			aggDirObj.mkdir();
		
		  File[] listFiles = currentDirectory.listFiles(new FileFilter() {
		  
		  public boolean accept(File pathname) { return
		  pathname.getName().endsWith(".mtd"); } });
		 

		String aggregateReportFileName = "aggrgateReport.mtd";

		Model aggrfileModel = new Model(aggregateReportFileName);

		for (File currentFile : listFiles) {
			readMTDReportFile(currentFile, aggrfileModel);
		}

		writeReportFile(aggrfileModel, aggDirObj + File.separator + aggregateReportFileName);
		
		return false;
	}
	
	private void readMTDReportFile(File file, Model fileModel)
	{
	  FileReader fileReader = null;
	  BufferedReader bufferedReader = null;
	  
	  try
	  {
		fileReader = new FileReader(file);  
		bufferedReader = new BufferedReader(fileReader);
		
		String dataLine = null;
		
		while((dataLine = bufferedReader.readLine()) != null)
		{
		  String[] arrData = dataLine.split("=");
		  
		  if(arrData[0].equals("Words"))
			  fileModel.setWords(fileModel.getWords() + Integer.parseInt(arrData[1]));
		  
		  if(arrData[0].equals("Vowels"))
			  fileModel.setVowels(fileModel.getVowels() + Integer.parseInt(arrData[1]));
		  
		  if(arrData[0].equals("Special Characters"))
			  fileModel.setSpecialChars(fileModel.getSpecialChars() + Integer.parseInt(arrData[1]));
		}
	  }
	  catch (IOException e) 
	  {
		 System.err.println("Exception to read file(" + file.getAbsolutePath() + ", : " + e.getMessage());
		 return;
	  }
	  finally {
		  closeReadResources(fileReader , bufferedReader);
	  }
	}
	
	private boolean writeReportFile(Model fileModel, String filePath) {
		
		FileWriter fileWriter = null;
		BufferedWriter writer = null;

		File file = new File(filePath);

		if (file.exists())
			file.delete();

		try {
			fileWriter = new FileWriter(file);
			writer = new BufferedWriter(fileWriter);
			writer.write(fileModel.printReport());
			writer.flush();

			System.out.println("file(" + filePath + ") is ready "+ file.getParentFile().getAbsolutePath() + ".");

			return true;
		} catch (IOException e) {
			System.err.println(filePath);
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (writer != null)
					writer.close();
				if (fileWriter != null)
					fileWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void closeReadResources(FileReader fileReader, BufferedReader bufferedReader) {
		try {
			if (bufferedReader != null)
				bufferedReader.close();

			if (fileReader != null)
				fileReader.close();
		} catch (Exception e) {
			System.out.println( e.getMessage());
		}
	}

}
