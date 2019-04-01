package com.information;

public class Model {
	String fileName;
	int words;
	int vowels;
	int specialChars;

	public Model(String fileName)
	{
		this.fileName = fileName;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getWords() {
		return words;
	}

	public void setWords(int words) {
		this.words = words;
	}
	
	public void addWords(int words)
	{
		this.words = this.words + words;
	}
	public int getVowels() {
		return vowels;
	}
	
	public void addVowel()
	{
		this.vowels++;
	}

	public void addSpecialChar()
	{
		this.specialChars++;
	}
	
	public void setVowels(int vowels) {
		this.vowels = vowels;
	}

	public int getSpecialChars() {
		return specialChars;
	}

	public void setSpecialChars(int specialChars) {
		this.specialChars = specialChars;
	}

	@Override
	public String toString() {
		return "FileModel [fileName=" + fileName + ", words=" + words + ", vowels=" + vowels + ", specialChars="
				+ specialChars + "]";
	}
	
   public String printReport()
   {
	   StringBuffer buffer = new StringBuffer();
	   
	   buffer.append("Words=" + words).append("\n");
	   buffer.append("Vowels=" + vowels).append("\n");
	   buffer.append("Special Characters=" + specialChars).append("\n");
	   
	   return buffer.toString();
   }

}
