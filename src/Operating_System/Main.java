package Operating_System;

import java.io.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class Main
{
	private static int method = 0;
	private static ArrayList<String> saveintofile = new ArrayList<>();
	private static ContiguousAllocation objCon = new ContiguousAllocation();
	private static IndexedAllocation objIndex = new IndexedAllocation();
	private static LinkedAllocation objLinked = new LinkedAllocation();
	
	public static void main(String[] args) throws IOException
	{
		VirtualFileSystem objVir = new VirtualFileSystem();
		ArrayList<Integer> allocatedBlocks = new ArrayList<>();
		
		File file = new File("file.txt");
		if(!file.exists()) file.createNewFile();
		
		BufferedReader read_obj = new BufferedReader(new FileReader("file.txt"));
		
		String read_line = "";
		read_line = read_obj.readLine();
		if(read_line != null)
		{
			method = parseInt(read_line);
			read_line = read_obj.readLine();
			System.out.println(read_line);
			while((read_line = read_obj.readLine()) != null)
			{
				if((read_line).equals("files"))
				{
					break;
				}
				objVir.CreateDirectory(read_line);
			}
			if(read_line.equals("files"))
			{
				if(method == 1)
				{
					while((read_line = read_obj.readLine()) != null)
					{
						String[] str = read_line.split(" ");
						objVir.CreateFile(str[0], parseInt(str[2]), allocatedBlocks);
						objCon.mark(str[0], parseInt(str[1]), parseInt(str[2]));
					}
				}
				else if(method == 2)
				{
					while((read_line = read_obj.readLine()) != null)
					{
						String[] str = read_line.split(" ");
						ArrayList<Integer> indexedArr = new ArrayList<>();
						for(int i = 0; i < str.length - 2; i++)
						{
							indexedArr.add(parseInt(str[i + 2]));
						}
						objVir.CreateFile(str[0], parseInt(str[2]), allocatedBlocks);
						objIndex.mark(str[0], parseInt(str[1]), indexedArr);
					}
				}
				else if(method == 3)
				{
					read_line = read_obj.readLine();
					if(read_line != null)
					{
						String[] str = read_line.split(" ");
						objVir.CreateFile(str[0], parseInt(str[2]), allocatedBlocks);
						objLinked.mark(str[0], parseInt(str[1]), parseInt(str[2]));
					}
					while((read_line = read_obj.readLine()) != null)
					{
						String[] readarr = read_line.split(" ");
						try
						{
							int curindex = parseInt(readarr[0]);
							int next = parseInt(readarr[1]);
							objLinked.arrblocks[curindex].allocated = true;
							objLinked.arrblocks[curindex].next = next;
						}
						catch(NumberFormatException e)
						{
							if(readarr.length == 3)
							{
								objVir.CreateFile(readarr[0], parseInt(readarr[2]), allocatedBlocks);
							}
							else if(readarr[1].equals("nil"))
							{
								int cur = parseInt(readarr[0]);
								objLinked.arrblocks[cur].allocated = true;
								objLinked.arrblocks[cur].next = -1;
							}
						}
					}
				}
			}
		}
		
		
			Scanner read = new Scanner(System.in);
			
			System.out.println("Select allocation method...");
			System.out.println("1) Contiguous Allocation.");
			System.out.println("2) Indexed Allocation. ");
			System.out.println("3) Linked Allocation. ");
			System.out.print("please enter your choice : ");
			
			Scanner input = new Scanner(System.in);
			method = input.nextInt();
			while(true)
			{
				String command;
				System.out.print(">>");
				command = read.nextLine();
				
				String[] arr = command.split(" ");
				if(arr.length == 1)
				{
					if(arr[0].equals("DisplayDiskStatus"))
					{
						if(method == 1)
						{
							objCon.displaydiskstatus();
						}
						else if(method == 2)
						{
							objIndex.displaydiskstatus();
						}
						else if(method == 3)
						{
							objLinked.displaydiskstatus();
						}
					}
					else if(arr[0].equals("DisplayDiskStructure"))
					{
						objVir.DisplayStructure();
					}
					else if(arr[0].equals("Exit"))
					{
						savefile();
						break;
					}
					else
					{
						System.out.println("error command");
						continue;
					}
				}
				
				String type = arr[0].substring(0, 6);
				String fsType = arr[0].substring(6);
				
				if(type.equals("Create"))
				{
					if(fsType.equals("Folder"))
					{
						if(arr.length != 2)
						{
							System.out.println("There is no command called " + command);
							continue;
						}
						objVir.CreateDirectory(arr[1]);
						saveintofile.add(arr[1]);
					}
					else if(fsType.equals("File"))
					{
						if(arr.length != 3)
						{
							System.out.println("There is no command called " + command);
							continue;
						}
						int fSize = Integer.parseInt(arr[2]);
						boolean found = objVir.CreateFile(arr[1], fSize, allocatedBlocks);
						if(found)
						{
							if(method == 1)
							{
								objCon.allocation(arr[1], fSize);
							}
							else if(method == 2)
							{
								objIndex.allocation(arr[1], fSize);
							}
							else if(method == 3)
							{
								objLinked.allocation(arr[1], fSize);
							}
						}
					}
					else
					{
						System.out.println("There is no command called " + command);
					}
				}
				else if(type.equals("Delete"))
				{
					if(fsType.equals("Folder"))
					{
						objVir.DeleteDirectory(arr[1]);
						saveintofile.remove(arr[1]);
					}
					else if(fsType.equals("File"))
					{
						objVir.DeleteFile(arr[1]);
						if(method == 1)
						{
							objCon.deallocation(arr[1]);
						}
						else if(method == 2)
						{
							objIndex.deallocation(arr[1]);
						}
						else if(method == 3)
						{
							objLinked.deallocation(arr[1]);
						}
					}
					else
					{
						System.out.println("There is no command called " + command);
					}
				}
			}
		}
	
	
	private static void savefile() throws IOException
	{
		BufferedWriter obj = new BufferedWriter(new FileWriter("file.txt"));
		
		obj.write(method + "");
		obj.newLine();
		obj.write("folders");
		obj.newLine();
		for(int i = 0; i < saveintofile.size(); i++)
		{
			obj.write(saveintofile.get(i));
			obj.newLine();
		}
		obj.write("files");
		obj.newLine();
		if(method == 1)
		{
			for(int i = 0; i < objCon.wfc.size(); i++)
			{
				obj.write(objCon.wfc.get(i).fName + " " + objCon.wfc.get(i).start + " " + objCon.wfc.get(i).counter);
				obj.newLine();
			}
		}
		else if(method == 3)
		{
			for(int i = 0; i < objLinked.linkedarr.size(); i++)
			{
				obj.write(objLinked.linkedarr.get(i).filename + " " + objLinked.linkedarr.get(i).startpoint + " " + objLinked.linkedarr.get(i).endpoint);
				int strt = objLinked.linkedarr.get(i).startpoint;
				obj.newLine();
				int j = 0;
				//obj.write(objLinked.linkedarr.get(i).startpoint+" ");
				while(objLinked.arrblocks[strt].next != -1)
				{
					
					obj.write(strt + " " + objLinked.arrblocks[strt].next);
					obj.newLine();
					strt = objLinked.arrblocks[strt].next;
					
				}
				obj.write(strt + " " + "nil");
				
				
				obj.newLine();
			}
		}
		else if(method == 2)
		{
			for(int i = 0; i < objIndex.arrIndexed.size(); i++)
			{
				obj.write(objIndex.arrIndexed.get(i).fileName + " " + objIndex.arrIndexed.get(i).startBlock);
				for(int j = 0; j < objIndex.arrIndexed.get(i).indexesBlocks.size(); j++)
				{
					obj.write(" " + objIndex.arrIndexed.get(i).indexesBlocks.get(j));
				}
				;
				obj.newLine();
			}
		}
		obj.close();
		System.out.println("saving in file");
	}
}
