package Operating_System;

import java.util.ArrayList;
import java.util.Random;

public class LinkedAllocation
{
	
	Block arrblocks[];
	ArrayList<Linked> linkedarr = new ArrayList<>();
	ArrayList<Integer> freeindices = new ArrayList<>();
	
	LinkedAllocation()
	{
		arrblocks = new Block[100];
		for(int i = 0; i < 100; i++)
		{
			arrblocks[i] = new Block();
		}
	}
	
	boolean allocation(String filename, int size)
	{
		int freespace = 0;
		for(int i = 0; i < 100; i++)
		{
			if(!arrblocks[i].allocated)
			{
				freespace++;
				freeindices.add(i);
			}
		}
		if(freespace == 100)
		{
			size--;
		}
		if(size > freespace)
		{
			System.out.println("no free space");
			return false;
		}
		int startpoint = 0;
		for(int i = 0; i < 100; i++)
		{
			if(!arrblocks[i].allocated)
			{
				startpoint = i;
				arrblocks[i].allocated = true;
				//int indextoremove = freeindices.indexOf(i);
				freeindices.remove(i);
				break;
			}
		}
		Linked linkedfile = new Linked();
		linkedfile.filename = filename;
		linkedfile.startpoint = startpoint;
		
		int counteroftrials = 10;
		int j = startpoint;
		int randomeindex = 0;
		int k=0;
		while(size >= 1)
		{
//			Random rn = new Random();
//			int range = freeindices.size() - 0 + 1;
//			randomeindex = rn.nextInt(range) + 0;
//
//			System.out.println(randomeindex + "random");
			
			int block = freeindices.get(k);
			freeindices.remove(k);
			arrblocks[block].allocated = true;
			size--;
			k++;
			
			
			
			arrblocks[j].next = block;
//			System.out.println(arrblocks[j].next);
			j = block;
			//int indextoremove = freeindices.indexOf(j);
//			System.out.println("----> " + indextoremove);
			//freeindices.remove(indextoremove);
			
//			if((size-1) == 0)
//			{
//
//				break;
//			}
		}
		arrblocks[j].next = -1;
		//System.out.println(arrblocks[j].next);
		linkedfile.endpoint = j;
		linkedarr.add(linkedfile);
//		System.out.println("file allocated success");
		return true;
		
	}
	
	boolean deallocation(String filename)
	{
		int startpoint = 0;
		for(int i = 0; i < linkedarr.size(); i++)
		{
			if(linkedarr.get(i).filename.equals(filename))
			{
				startpoint = linkedarr.get(i).startpoint;
				linkedarr.remove(i);
				break;
			}
		}
		int index = startpoint;
		while(arrblocks[index].next != -1)
		{
//			System.out.println("freeing indcies" + index);
			arrblocks[index].allocated = false;
			freeindices.add(index);
			index = arrblocks[index].next;
			
		}
		arrblocks[index].allocated = false;
//		System.out.println("file deallocated");
//		System.out.println(index);
		return true;
	}
	
	public void displaydiskstatus()
	{
		int countfreespace = 0;
		int allocatedspace = 0;
		ArrayList<Integer> freeblocks = new ArrayList<>();
		ArrayList<Integer> allocatedblocks = new ArrayList<>();
		for(int i = 0; i < 100; i++)
		{
			if(arrblocks[i].allocated)
			{
				allocatedspace++;
				allocatedblocks.add(i);
			}
			else
			{
				countfreespace++;
				freeblocks.add(i);
			}
		}
		System.out.println("Free Space " + countfreespace);
		System.out.println("Allocated Space " + allocatedspace);
		System.out.println("Free Blocks " + freeblocks);
		System.out.println("Allocated Blocks " + allocatedblocks);
	}
	
	void mark(String string, int start, int end)
	{
		Linked obj = new Linked();
		obj.endpoint = end;
		obj.filename = string;
		obj.startpoint = start;
		linkedarr.add(obj);
		arrblocks[start].allocated = true;
		arrblocks[end].allocated = true;
		arrblocks[end].next = -1;
	}
}

class Block
{
	boolean allocated;
	int next;
	Block()
	{
		allocated = false;
		next = -1;
	}
}
class Linked
{
	String filename;
	int startpoint;
	int endpoint;
}