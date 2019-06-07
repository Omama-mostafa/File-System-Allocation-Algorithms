package Operating_System;

import java.util.ArrayList;

class File extends VirtualFileSystem
{
	String fileName;
	String filePath;
	private int fileSize;
	boolean deleted;
	private ArrayList<Integer> allocatedBlocks;
	
	File(String fPath, String fName, int fSize, ArrayList<Integer>allocBlocks)
	{
		this.filePath = fPath;
		this.fileName = fName;
		this.fileSize = fSize;
		this.deleted = false;
		this.allocatedBlocks = allocBlocks;
	}
}
