package Operating_System;

import java.util.ArrayList;

class Directory
{
	String directoryPath;
	String directoryName;
	ArrayList<File> childFiles;
	ArrayList<Directory> childDirectories;
	Directory parentDirectory;
	boolean deleted;
	
	Directory(String dPath, String dName, Directory pDir)
	{
		this.directoryPath = dPath;
		this.directoryName = dName;
		this.childFiles = new ArrayList<>();
		this.childDirectories = new ArrayList<>();
		this.parentDirectory = pDir;
		this.deleted = false;
	}
}
