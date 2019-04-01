package com.creation;


import java.io.File;

import com.information.Model;

public interface InformationCreator
{
   void setNextReportGenerator(InformationCreator nextProcess);
	
   boolean generateReport(Model fileModel, File file, String dirPath);
}
