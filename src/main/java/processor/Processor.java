package processor;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import memory.MainMemory;
import memory.MemorySystem;


public class Processor {
	
	// Local variable to store memory system
	private MemorySystem memory;
	
	/** Default constructor, with no cache memory
	 */
	public Processor() {
		this.memory = new MainMemory();
	}
	
	/**
	 * Constructor that allows to assign a custom memory system to the processor.
	 * 
	 * @param mem the memory system
	 */
	public Processor(MemorySystem mem) {
		this.memory = mem;
	}
	
	/**
	 * Cleans the memory system and leaves it empty (i.e. cache with all entries set to invalid)
	 */
	public void cleanMemory() {
		this.memory.clean();
	}
		
	
	/**
	 * Reduces a number to a maximum of 3 digits, and assigns it the corresponding
	 * unit to maintain the equivalence. It's the same as expressing the number
	 * as a multiple of 2^n10 (bytes, Kbytes, Mbytes, etc).<br>
	 * Example: converts 512 to "512b", and 2048 to "2Kb"
	 */
	private String numberToPow2Category(int number) {
		String category;
		if (number < 1024) {
			category = number + "b";
		} else if (number < 0x00100000) {
			category = number/1024 + "Kb";
		} else if (number < 0x40000000) {
			category = number/(0x00100000) + "Mb";
		} else {
			category = number/(0x40000000) + "Gb";
		}
		return category;
	}
	
	
	
	/**
	 * Runs a series of consecutive memory accesses on the processor; changing the total number
	 * of memory references (loop_size) and the distance in memory between two consecutive ones (loop_step).
	 * For each power-of-2 loop_size starting from 16 up to the parameter specified 'max_size',
	 * the method will run a loop of memory access for every possible power-of-2 loop_step.<br>
	 * This results in a loop for every combination of loop_step and loop_size, where the first one
	 * is smaller than the second one, and both are powers of two.<br>
	 * For each combination, the average access time will be calculated and stored in a CategoryDataset for
	 * further processing.<br>
	 * The second parameter allows to adjust the accuracy of the results by setting how many iterations of
	 * each loop will be performed.
	 * 
	 * @param max_size Size of the bigger loop to execute. 2Mb (0x200000) recommended.
	 * @param iterations Number of iterations each loop will be performed (high numbers will take longer time).
	 * @return A CategoryDataset with each loop_size as a series. The loop_step is the category of the points of
	 * each series, and the actual data for the range axis is the average access time.
	 */
	public CategoryDataset runSimulatedLoopTest(int max_size, int iterations) {
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		// If any parameter is zero or negative, the operation is cancelled
		if (max_size <= 0  || iterations <= 0)
			return dataset;
		
		int loop_step, loop_size;
		
		// Double for loop to change loop_size and loop_step. Bit shifting is used to perform multiplication by 2
		for (loop_size = 16; loop_size <= max_size; loop_size <<= 1) {
			String serie = numberToPow2Category(loop_size);
			for (loop_step = 4; loop_step <= loop_size >>> 1; loop_step <<= 1) {
				String category = numberToPow2Category(loop_step);
				
				// For each combination of loop_size and loop_step, we iterate as many times as specified
				for (int i = 0; i < iterations; i++) {
					for (int address = 0; address < loop_size; address += loop_step) {
						this.memory.access(address).getAccessTime();
					}
				}
				// Calculate average access time, and clean memory for next loop
				float averagTime = this.memory.getAverageAccessTime();
				this.memory.clean();
				
				dataset.addValue(averagTime, serie, category);		
			}
		}
		return dataset;
	}
	
	
	
	/**
	 * Runs a series of consecutive memory accesses on the processor; changing the total number
	 * of memory references (loop_size) and the distance in memory between two consecutive ones (loop_step).
	 * For each power-of-2 loop_size starting from 16 up to the parameter specified 'max_size',
	 * the method will run a loop of memory access for every possible power-of-2 loop_step.<br>
	 * This results in a loop for every combination of loop_step and loop_size, where the first one
	 * is smaller than the second one, and both are powers of two.<br>
	 * For each combination, the average access time will be calculated and stored in a CategoryDataset for
	 * further processing.<br>
	 * Each loop will be run two times, but the data will be taken only for the second run. This allow to speculate
	 * the average access times assuming that every iteration past the first one is exactly the same.<br>
	 * 
	 * @param max_size Size of the bigger loop to execute. 2Mb (0x200000) recommended.
	 * @return A CategoryDataset with each loop_size as a series. The loop_step is the category of the points of
	 * each series, and the actual data for the range axis is the average access time.
	 */
	public CategoryDataset runEspeculatedLoopTest(int max_size) {
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		// If parameter is not valid, return an empty dataset
		if (max_size <= 0)
			return dataset;
		
		int loop_step, loop_size;

		// Double for loop to change loop_size and loop_step. Bit shifting is used to perform multiplication by 2
		for (loop_size = 16; loop_size <= max_size; loop_size <<= 1) {
			String serie = numberToPow2Category(loop_size);
			for (loop_step = 4; loop_step <= loop_size >>> 1; loop_step <<= 1) {
				String category = numberToPow2Category(loop_step);
				
				// First run of the loop, so that it enters steady regimen
				for (int address = 0; address < loop_size; address += loop_step) {
					this.memory.access(address).getAccessTime();
				}
				
				// Prepare constants to count references and time in second interation
				float time = 0;
				int references = 0;	
				for (int address = 0; address < loop_size; address += loop_step) {
					time += this.memory.access(address).getAccessTime();
					references++;
				}
				// Calculate average time of second run and clean memory
				float averagTime = time/(float)references;
				this.memory.clean();
				
				dataset.addValue(averagTime, serie, category);		
			}
		}
		return dataset;
	}
		
}

/**
 * Table with hexadecimal values for all power of 2
 * 
 * 0x00000000		0 
 * 0x00000001		1
 * 0x00000002		2
 * 0x00000004		4
 * 0x00000008		8
 * 0x00000010		16
 * 0x00000020		32
 * 0x00000040		64
 * 0x00000080		128
 * 0x00000100		256
 * 0x00000200		512
 * 0x00000400		1K
 * 0x00000800		2K
 * 0x00001000		4K
 * 0x00002000		8K
 * 0x00004000		16K
 * 0x00008000		32K
 * 0x00010000		64K
 * 0x00020000		128K
 * 0x00040000		256K
 * 0x00080000		512K
 * 0x00100000		1M
 * 0x00200000		2M	
 * 0x00400000		4M
 * 0x00800000		8M
 * 0x01000000		16M
 * 0x02000000		32M
 * 0x04000000		64M	
 * 0x08000000		128M
 * 0x10000000		256M
 * 0x20000000		512M
 * 0x40000000		1G
 * 0x80000000		2G
 * 0xFFFFFFFF		4G-1
 */


