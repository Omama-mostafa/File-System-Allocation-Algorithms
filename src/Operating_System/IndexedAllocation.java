package Operating_System;

import java.util.ArrayList;

public class IndexedAllocation
{
	ArrayList<indexed> arrIndexed = new ArrayList<>();
	Block arrBlocks[];
	int blockSize = 100;
	
	IndexedAllocation()
	{
		arrBlocks = new Block[100];
		int j = 1;
		for(int i = 0; i < blockSize; i++)
		{
			arrBlocks[i] = new Block();
			arrBlocks[i].id += j;
			j++;
		}
	}
	
	boolean allocation(String fileNam, int size)
	{
		int freeBlocks = 0;
		for(int i = 0; i < blockSize; i++)
		{
			if(!arrBlocks[i].allocated)
			{
				freeBlocks++;
			}
		}
		
		if(freeBlocks >= size + 1)              //hna ana et2kdt n size l file as3'r mn l empty blocks l 3ndy
		{                                          // +1 3shan ana fi wa7d h5zn fi indexes l blocks bs
			indexed objIndex = new indexed();
			Block objBlock = new Block();
			int indexStartBlock = -1;
			for(int i = 0; i < blockSize; i++)
			{
				if(!arrBlocks[i].allocated)
				{
					objIndex.fileName = fileNam;
					objIndex.startBlock = i;
					
					indexStartBlock = i;
					arrBlocks[indexStartBlock].allocated = true;// h5zn index awl block l h5zn fi ba2i l blocks fl index
					
//					System.out.print("startindex :");
//					System.out.println(indexStartBlock);
					break;
				}
			}
			for(int i = indexStartBlock + 1; i < blockSize; i++)
			{
				if(size == 0)
				{
					objBlock.arrAllocatedBlocks = objIndex.indexesBlocks;
					
//					for(int k = 0; k < objIndex.indexesBlocks.size(); k++)
//					{
//						System.out.print("indexallocatedtBlock: ");
//						System.out.println(objIndex.indexesBlocks.get(k));
//					}
					objBlock.allocated = true;
					objBlock.id = indexStartBlock;
					arrBlocks[indexStartBlock] = objBlock;
					arrIndexed.add(objIndex);
					break;
				}
				else if(!arrBlocks[i].allocated)
				{
					objIndex.indexesBlocks.add(i);
					arrBlocks[i].allocated = true;
					size--;
					freeBlocks--;
				}
			}
		}
		else
		{
			System.out.print("no space for allocating this file");
			return false;
		}
		return true;
	}
	
	boolean deallocation(String filename)
	{
		int startIndexBlock = 0;
		for(int i = 0; i < arrIndexed.size(); i++)
		{
			if(arrIndexed.get(i).fileName.equals(filename))
			{
				startIndexBlock = arrIndexed.get(i).startBlock;
				//System.out.println(startIndexBlock);
				break;
			}
		}
		ArrayList<Integer> arrIndexes = new ArrayList<>();
		arrIndexes = arrBlocks[startIndexBlock].arrAllocatedBlocks;
		arrBlocks[startIndexBlock].allocated = false;
		arrBlocks[startIndexBlock].arrAllocatedBlocks = new ArrayList<>();
		for(int i = 0; i < arrIndexes.size(); i++)
		{
			if(arrIndexed.get(i).startBlock == startIndexBlock)
			{
				arrIndexed.remove(i);
				break;
			}
		}
		for(int i = 0; i < arrIndexes.size(); i++)
		{
			int index = arrIndexes.get(i);
//			System.out.println(index);
			arrBlocks[index].allocated = false;
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
			if(arrBlocks[i].allocated)
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
	
	void mark(String filename, int start, ArrayList<Integer> arr)
	{
		indexed obj = new indexed();
		obj.fileName = filename;
		obj.startBlock = start;
		obj.indexesBlocks = arr;
		for(int i = start; i < arr.size(); i++)
		{
			arrBlocks[i].allocated = true;
		}
		arrIndexed.add(obj);
	}
	class Block
	{
		int id = 0;
		boolean allocated;
		ArrayList<Integer> arrAllocatedBlocks = new ArrayList<>();
		Block()
		{
			allocated = false;
			id = -1;
		}
	}
	class indexed
	{
		
		String fileName;
		int startBlock;
		ArrayList<Integer> indexesBlocks = new ArrayList<>();
	}
}