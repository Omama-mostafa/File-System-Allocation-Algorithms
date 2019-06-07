package Operating_System;

import java.util.ArrayList;

class ContiguousAllocation
{
	Block objBlock[];
	ArrayList<WorstfitClass> wfc = new ArrayList<>();
	private int blockSize = 100;
	
	ContiguousAllocation()
	{
		objBlock = new Block[100];
		for(int i = 0; i < 100; i++)
		{
			objBlock[i] = new Block();
			objBlock[i].id = i;
		}
	}
	
	boolean allocation(String filePath, int size)
	{
		int counter = 0;
		int count = 0;
	
		WorstfitClass obj = new WorstfitClass();
		for(int i = 0; i < blockSize; i++)
		{
			if(objBlock[i].allocated) counter++;
		}
		if(counter == 0)
		{
			obj.fName = filePath;
			obj.counter = size;
			obj.start = 0;
			wfc.add(obj);
			for(int i = 0; i < size; i++)
			{
				objBlock[i].allocated = true;
			}
		}
		else
		{
			ArrayList<WorstfitClass> worstfit = new ArrayList<>();
			WorstfitClass objworstfit = new WorstfitClass();
			for(int i = 0; i < blockSize; i++)
			{
				count = 0;
				if(!objBlock[i].allocated)
				{
					objworstfit.start = i;
					int j = i;
					while((!objBlock[j].allocated) && (j < blockSize))
					{
						count++;
						j++;
						if(j == 99) break;
						
					}
					objworstfit.counter = count;
					worstfit.add(objworstfit);
					if(j == blockSize - 1) break;
				}
			}
			
			int index = GetMax(worstfit);
			if(size <= worstfit.get(index).counter)
			{
				obj.start = worstfit.get(index).start;
				obj.counter = size;
				obj.fName = filePath;
				
				wfc.add(obj);
				for(int k = obj.start; k< size+obj.start; k++)
				{
					objBlock[k].allocated = true;
					
				}
				worstfit.remove(index);
				//return index;
			}
			else
			{
				System.out.println("no space to allocate file.");
				return false;
			}
		}
		return true;
	}
	
	boolean deallocation(String filename)
	{
		int start = 0;
		int size = 0;
		for(int i = 0; i < wfc.size(); i++)
		{
			if(wfc.get(i).fName.equals(filename))
			{
				start = wfc.get(i).start;
				size = wfc.get(i).counter;
				wfc.remove(i);
				break;
			}
		}
		for(int i = start; i < size; i++)
		{
			objBlock[i].allocated = false;
		}
		return true;
	}
	void displaydiskstatus()
	{
		int countfreespace = 0;
		int allocatedspace = 0;
		ArrayList<Integer> freeblocks = new ArrayList<>();
		ArrayList<Integer> allocatedblocks = new ArrayList<>();
		for(int i = 0; i < 100; i++)
		{
			if(objBlock[i].allocated)
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
	
	private int GetMax(ArrayList<WorstfitClass> worstfit)
	{
		int max = worstfit.get(0).counter;
		int index = 0;
		for(int i = 1; i < worstfit.size(); i++)
		{
			if(worstfit.get(i).counter > max)
			{
				max = worstfit.get(i).counter;
				index = i;
			}
		}
		return index;
	}
	
	void mark(String filename, int start, int size)
	{
		WorstfitClass obj = new WorstfitClass();
		obj.counter = size;
		obj.fName = filename;
		obj.start = start;
		for(int i = start; i < size; i++)
		{
			objBlock[i].allocated = true;
		}
		
	}
	
	class Block
	{
		int id;
		boolean allocated;
		Block()
		{
			allocated = false;
			id = -1;
		}
	}
	class WorstfitClass
	{
		String fName;
		int start;
		int counter;
	}
}

