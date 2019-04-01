package com.constant;

public interface Helpable 
{
  byte NOT_STARTED = -1;
  byte RUNNING = 1;
  byte STOPPED = -2;
  byte FOUND = 3;
  byte NOT_FOUND = 4;
  byte EXIT = 5;
  String [] supportedFileFormat = {".csv", ".txt"};
  char[] vowels = {'a' , 'e', 'i' , 'o', 'u'};
  char[] specialChars = {'@', '#', '$', '*'};
  String wordSeparator = " ";
  String soruceFileExtn = ".mtd";
  String WORDS = "words";
  String VOWELS = "vowels";
  String SEPECIAL_CHARS = "specialCharacters";
}
