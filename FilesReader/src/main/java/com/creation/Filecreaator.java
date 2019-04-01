package com.creation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.information.Model;

public class Filecreaator implements InformationCreator
{
	private InformationCreator processFile;
	
	@Override
	public void setNextReportGenerator(InformationCreator processFile) {
		this.processFile = processFile;
	}

	@Override
	public boolean generateReport(Model fileModel, File fileObj, String directoryPath)
	{
		FileWriter fileWriter = null;
		BufferedWriter writer = null;

		if (fileObj.exists())
			fileObj.delete();

		try 
		{
			fileWriter = new FileWriter(fileObj);
			writer = new BufferedWriter(fileWriter);
			writer.write(fileModel.printReport());
			writer.flush();

			System.out.println("Report file(" + fileObj.getAbsolutePath() + ") is generated " + directoryPath + ".");
			
			processFile.generateReport(fileModel, fileObj, directoryPath);
			
			return true;
			
		} 
		catch (IOException e) 
		{
			System.err.println("Exception writing file = " + fileObj.getAbsolutePath() +" \n" + e.getMessage());
			return false;
		} 
		finally 
		{
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

}
