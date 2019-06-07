package Operating_System;

import java.util.ArrayList;

class VirtualFileSystem
{
	private Directory root = new Directory("root", "root", null);
	private boolean createDir = false;
	private boolean createFile = false;
	private boolean deleteDir = false;
	private boolean deleteFile = false;
	VirtualFileSystem() { }
	
	private String[] SplitPath(String path) { return path.split("/"); }
	
	private String ConcatenatePath(String[] arr, int st, int end)
	{
		StringBuilder path = new StringBuilder();
		for(int i = st; i < end; i++)
		{
			path.append(arr[i]);
			if(i != end - 1) path.append("/");
		}
		return path.toString();
	}
	
	private boolean CheckFindDir(Directory parent, String path)
	{
		return parent.directoryPath.equals(path);
	}
	
	private Directory GetParent(Directory node, String childName)
	{
		if(node.childDirectories.size() == 0) return node;
		for(int i = 0; i < node.childDirectories.size(); i++)
		{
			if(node.childDirectories.get(i).directoryPath.equals(childName))
			{
				return node.childDirectories.get(i);
			}
		}
		return node;
	}
	
	private boolean CheckSameDirName(Directory parent, String childName)
	{
		if(parent.childDirectories.size() == 0) return false;
		else
		{
			for(int i = 0; i < parent.childDirectories.size(); i++)
			{
				if(parent.childDirectories.get(i).directoryName.equals(childName) && !parent.childDirectories.get(i).deleted) return true;
			}
		}
		return false;
	}
	
	private boolean CheckSameFileName(Directory parent, String childName)
	{
		if(parent.childFiles.size() == 0) return false;
		else
		{
			for(int i = 0; i < parent.childFiles.size(); i++)
			{
				if(parent.childFiles.get(i).fileName.equals(childName) && !parent.childFiles.get(i).deleted) return true;
			}
		}
		return false;
	}
	
	private void addDirChild(Directory parent, String child)
	{
		Directory newDir = new Directory(parent.directoryPath + "/" + child, child, parent);
		parent.childDirectories.add(newDir);
	}
	
	private void DeleteDirChild(Directory node)
	{
		if(node == null) return;
		
		for(int i = 0; i < node.childDirectories.size(); i++)
		{
			node.childDirectories.get(i).deleted = true;
			DeleteDirChild(node.childDirectories.get(i));
		}
		for(int i=0; i<node.childFiles.size(); i++)
			node.childFiles.get(i).deleted = true;
	}
	
	private void addFileChild(Directory parent, String child, int fSize, ArrayList<Integer> allocBlocks)
	{
		File newDFile = new File(parent.directoryPath + "/" + child, child, fSize, allocBlocks);
		parent.childFiles.add(newDFile);
	}
	
	private void DeleteFileChild(Directory node)
	{
		for(int i = 0; i < node.childFiles.size(); i++)
		{
			node.childFiles.get(i).deleted = true;
		}
	}
	
	private void RecursiveCreateDir(Directory node, String path)
	{
		String[] arr = SplitPath(path);
		String childName = arr[arr.length - 1];
		path = ConcatenatePath(arr, 0, arr.length - 1);
		arr = SplitPath(path);
		
		if(arr.length == 1) node = root;
		else
		{
			String parentName = arr[0];
			for(int i = 1; i < arr.length; i++)
			{
				parentName += ("/" + arr[i]);
				node = GetParent(node, parentName);
			}
		}
		
		boolean found = CheckFindDir(node, path);
		boolean sameName = CheckSameDirName(node, childName);
		
		if(found)
		{
			if(sameName)
			{
				System.out.println("Folder already exists.");
			}
			else
			{
				addDirChild(node, childName);
				createDir = true;
			}
		}
		else
		{
			System.out.println("There is no path called " + path);
		}
		
	}
	
	private void RecursiveDeleteDir(Directory node, String path)
	{
		String[] arr = SplitPath(path);
		if(arr.length == 1)
		{
			if(arr[0].equals("root"))
			{
				System.out.println("root directory can not be deleted.");
			}
			else
			{
				System.out.println("Not Found.");
			}
			
		}
		else
		{
			String parentName = arr[0];
			for(int i = 1; i < arr.length; i++)
			{
				parentName += ("/" + arr[i]);
				node = GetParent(node, parentName);
			}
		}
		boolean found = CheckFindDir(node, path);
		if(found && !node.deleted)
		{
			node.deleted = true;
			DeleteDirChild(node);
			deleteDir = true;
		}
		else
		{
			System.out.println("There is no path called " + path);
		}
	}
	
	
	private void RecursiveCreateFile(Directory node, String path, int fSize, ArrayList<Integer> allocBlocks)
	{
		String[] arr = SplitPath(path);
		String childName = arr[arr.length - 1];
		path = ConcatenatePath(arr, 0, arr.length - 1);
		arr = SplitPath(path);
		
		if(arr.length == 1) node = root;
		else
		{
			String parentName = arr[0];
			for(int i = 1; i < arr.length; i++)
			{
				parentName += ("/" + arr[i]);
				node = GetParent(node, parentName);
			}
		}
		
		boolean found = CheckFindDir(node, path);
		boolean sameName = CheckSameFileName(node, childName);
		
		if(found)
		{
			if(sameName) System.out.println("File already exists.");
			else
			{
				addFileChild(node, childName, fSize, allocBlocks);
				createFile = true;
			}
		}
		else System.out.println("There is no path called " + path);
	}
	
	private void RecursiveDeleteFile(Directory node, String path)
	{
		String[] arr = SplitPath(path);
		String childName = arr[arr.length - 1];
		if(arr.length == 1)
		{
			if(arr[0].equals("root")) System.out.println("root directory can not be deleted.");
			else System.out.println("Not Found.");
			return;
		}
		else
		{
			String parentName = arr[0];
			for(int i = 1; i < arr.length; i++)
			{
				parentName += ("/" + arr[i]);
				node = GetParent(node, parentName);
			}
		}
		boolean found = CheckSameFileName(node, childName);
		if(found && !node.deleted)
		{
			node.deleted = true;
			DeleteFileChild(node);
			deleteFile = true;
		}
		else System.out.println("There is no path called " + path);
	}

	private void PrintTree(Directory node)
	{
		if(node == null) return;
		for(int j = 0; j < node.childFiles.size(); j++)
		{
			if(!node.childFiles.get(j).deleted)
			{
				int len = SplitPath(node.childFiles.get(j).filePath).length;
				while(len > 1)
				{
					System.out.print("\t");
					len--;
				}
				System.out.println(node.childFiles.get(j).fileName);
			}
		}
		for(int i = 0; i < node.childDirectories.size(); i++)
		{
			if(!node.childDirectories.get(i).deleted)
			{
				int len = SplitPath(node.childDirectories.get(i).directoryPath).length;
				while(len > 1)
				{
					System.out.print("\t");
					len--;
				}
				System.out.println(node.childDirectories.get(i).directoryName);
			}
			PrintTree(node.childDirectories.get(i));
		}
	}
	
	
	public boolean CreateDirectory(String path)
	{
		RecursiveCreateDir(root, path);
//		System.out.println(root.directoryName);
//		PrintTree(root);
		return createDir;
	}
	
	public boolean DeleteDirectory(String path)
	{
		RecursiveDeleteDir(root, path);
//		System.out.println(root.directoryName);
//		PrintTree(root);
		return deleteDir;
	}
	
	public boolean CreateFile(String path, int fSize, ArrayList<Integer> allocBlocks)
	{
		RecursiveCreateFile(root, path, fSize, allocBlocks);
//		System.out.println(root.directoryName);
//		PrintTree(root);
		return createFile;
	}
	
	public boolean DeleteFile(String path)
	{
		RecursiveDeleteFile(root, path);
//		System.out.println(root.directoryName);
//		PrintTree(root);
		return deleteFile;
	}
	
	public void DisplayStructure()
	{
		System.out.println(root.directoryName);
		PrintTree(root);
	}
}
