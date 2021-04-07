import java.io.*;
import java.util.*;

public class Mallocator {
	static int IDsize = 0;
	static int memSize = 0;
	static int start = 0;
	static int end = 0;
	static int pID = 0;

	public static void main(String[] args) {
		ArrayList<Integer> startMemory = new ArrayList<Integer>();
		ArrayList<Integer> endMemory = new ArrayList<Integer>();
		ArrayList<Integer> ID = new ArrayList<Integer>();
		ArrayList<Integer> processSize = new ArrayList<Integer>();
		try {
			File minput = new File("Minput.data");
			Scanner reader = new Scanner(minput);
			if (!reader.hasNextLine()) {
				System.out.println("Minput file is empty");
				System.exit(0);
			}
			String data = reader.nextLine();
			int memorySlots = Integer.parseInt(data);
			for (int i = 0; i < memorySlots; i++) {
				data = reader.next();
				startMemory.add(Integer.parseInt(data));
				data = reader.next();
				endMemory.add(Integer.parseInt(data));
			}
			File pinput = new File("Pinput.data");
			reader = new Scanner(pinput);
			if (!reader.hasNextLine()) {
				System.out.println("There are no jobs to allocate");
				System.exit(0);
			}
			data = reader.nextLine();
			int numberOfProcesses = Integer.parseInt(data);
			for (int i = 0; i < numberOfProcesses; i++) {
				data = reader.next();
				ID.add(Integer.parseInt(data));
				data = reader.next();
				processSize.add(Integer.parseInt(data));
			}
			reader.close();
			fileFF(startMemory, endMemory, ID, processSize);
			fileBF(startMemory, endMemory, ID, processSize);
			fileWF(startMemory, endMemory, ID, processSize);
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			e.printStackTrace();
		}
	}

	public static void fileFF(ArrayList<Integer> x, ArrayList<Integer> y, ArrayList<Integer> z, ArrayList<Integer> q) {
		ArrayList<Integer> sMemory = new ArrayList<Integer>();
		ArrayList<Integer> eMemory = new ArrayList<Integer>();
		ArrayList<Integer> ID = new ArrayList<Integer>();
		ArrayList<Integer> pSize = new ArrayList<Integer>();
		ArrayList<Integer> output = new ArrayList<Integer>();
		for (int i = 0; i < x.size(); i++) {
			sMemory.add(x.get(i));
		}
		for (int i = 0; i < y.size(); i++) {
			eMemory.add(y.get(i));
		}
		for (int i = 0; i < z.size(); i++) {
			ID.add(z.get(i));
		}
		for (int i = 0; i < q.size(); i++) {
			pSize.add(q.get(i));
		}
		String allocated = "0,";
		try {
			FileWriter writer = new FileWriter("FFoutput.data");
			int k = 0;
			while (k < ID.size()) {
				if (sMemory.isEmpty()) {
					break;
				}
				IDsize = pSize.get(k);
				for (int j = 0; j < sMemory.size(); j++) {
					memSize = eMemory.get(j) - sMemory.get(j);
					if (memSize >= IDsize) {
						output.add(sMemory.get(j));
						output.add(sMemory.get(j) + IDsize);
						output.add(ID.get(k));
						sMemory.set(j, sMemory.get(j) + IDsize);
						if (sMemory.get(j) == eMemory.get(j)) {
							sMemory.remove(j);
							eMemory.remove(j);
						}
						ID.remove(k);
						pSize.remove(k);
						k = k - 1;
						break;
					}
				}
				k++;
			}
			if (!output.isEmpty()) {
				for (int i = 0; i <= output.size() / 3; i = i + 3) {
					int min = i;
					for (int j = i + 3; j < output.size(); j = j + 3) {
						if (output.get(min) > output.get(j)) {
							min = j;
						}
					}
					int tempS = output.get(i);
					int tempE = output.get(i + 1);
					int tempI = output.get(i + 2);
					output.set(i, output.get(min));
					output.set(i + 1, output.get(min + 1));
					output.set(i + 2, output.get(min + 2));
					output.set(min, tempS);
					output.set(min + 1, tempE);
					output.set(min + 2, tempI);
				}
				int i = 0;
				while (i < output.size()) {
					writer.write(output.get(i) + " " + output.get(i + 1) + " " + output.get(i + 2) + "\n");
					i = i + 3;
				}
			}
			if (!ID.isEmpty()) {
				for (int i = 0; i < ID.size(); i++) {
					allocated = ID.get(i) + ",";
				}
			}
			writer.write("-" + allocated.substring(0, allocated.length() - 1));
			writer.close();

		} catch (IOException e) {
			System.out.println("Not able to read in file.");
			e.printStackTrace();
		}
	}

	public static void fileBF(ArrayList<Integer> x, ArrayList<Integer> y, ArrayList<Integer> z, ArrayList<Integer> q) {
		ArrayList<Integer> sMemory = new ArrayList<Integer>();
		ArrayList<Integer> eMemory = new ArrayList<Integer>();
		ArrayList<Integer> ID = new ArrayList<Integer>();
		ArrayList<Integer> pSize = new ArrayList<Integer>();
		ArrayList<Integer> output = new ArrayList<Integer>();
		for (int i = 0; i < x.size(); i++) {
			sMemory.add(x.get(i));
		}
		for (int i = 0; i < y.size(); i++) {
			eMemory.add(y.get(i));
		}
		for (int i = 0; i < z.size(); i++) {
			ID.add(z.get(i));
		}
		for (int i = 0; i < q.size(); i++) {
			pSize.add(q.get(i));
		}
		String allocated = "0,";
		try {
			FileWriter writer = new FileWriter("BFoutput.data");
			int j = 0;
			while (j < ID.size()) {
				for (int i = 0; i < sMemory.size(); i++) {
					int min = i;
					for (int k = i; k < sMemory.size(); k++) {
						if (eMemory.get(k) - sMemory.get(k) < eMemory.get(min) - sMemory.get(min)) {
							min = k;
						}
					}
					int tempS = sMemory.get(min);
					int tempE = eMemory.get(min);
					sMemory.set(min, sMemory.get(i));
					sMemory.set(i, tempS);
					eMemory.set(min, eMemory.get(i));
					eMemory.set(i, tempE);
				}
				IDsize = pSize.get(j);
				for (int i = 0; i < sMemory.size(); i++) {
					memSize = eMemory.get(i) - sMemory.get(i);
					if (memSize >= IDsize) {
						output.add(sMemory.get(i));
						output.add(sMemory.get(i) + IDsize);
						output.add(ID.get(j));
						sMemory.set(i, sMemory.get(i) + IDsize);
						if (sMemory.get(i) == eMemory.get(i)) {
							sMemory.remove(i);
							eMemory.remove(i);
						}
						ID.remove(j);
						pSize.remove(j);
						j = j - 1;
						break;
					}
				}

				j++;
			}
			if (!output.isEmpty()) {
				for (int i = 0; i <= output.size() / 3; i = i + 3) {
					int min = i;
					for (int k = i + 3; k < output.size(); k = k + 3) {
						if (output.get(min) > output.get(k)) {
							min = k;
						}
					}
					int tempS = output.get(i);
					int tempE = output.get(i + 1);
					int tempI = output.get(i + 2);
					output.set(i, output.get(min));
					output.set(i + 1, output.get(min + 1));
					output.set(i + 2, output.get(min + 2));
					output.set(min, tempS);
					output.set(min + 1, tempE);
					output.set(min + 2, tempI);
				}
				int i = 0;
				while (i < output.size()) {
					writer.write(output.get(i) + " " + output.get(i + 1) + " " + output.get(i + 2) + "\n");
					i = i + 3;
				}
			}
			if (!ID.isEmpty()) {
				for (int i = 0; i < ID.size(); i++) {
					allocated = ID.get(i) + ",";
				}
			}
			writer.write("-" + allocated.substring(0, allocated.length() - 1));
			writer.close();

		} catch (IOException e) {
			System.out.println("Not able to read in file.");
			e.printStackTrace();
		}
	}

	public static void fileWF(ArrayList<Integer> x, ArrayList<Integer> y, ArrayList<Integer> z, ArrayList<Integer> q) {
		ArrayList<Integer> sMemory = new ArrayList<Integer>();
		ArrayList<Integer> eMemory = new ArrayList<Integer>();
		ArrayList<Integer> ID = new ArrayList<Integer>();
		ArrayList<Integer> pSize = new ArrayList<Integer>();
		ArrayList<Integer> output = new ArrayList<Integer>();
		for (int i = 0; i < x.size(); i++) {
			sMemory.add(x.get(i));
		}
		for (int i = 0; i < y.size(); i++) {
			eMemory.add(y.get(i));
		}
		for (int i = 0; i < z.size(); i++) {
			ID.add(z.get(i));
		}
		for (int i = 0; i < q.size(); i++) {
			pSize.add(q.get(i));
		}
		String allocated = "0,";
		try {
			FileWriter writer = new FileWriter("WFoutput.data");
			int j = 0;
			while (j < ID.size()) {
				for (int i = 0; i < sMemory.size(); i++) {
					int max = i;
					for (int k = i; k < sMemory.size(); k++) {
						if (eMemory.get(k) - sMemory.get(k) > eMemory.get(max) - sMemory.get(max)) {
							max = k;
						}
					}
					int tempS = sMemory.get(max);
					int tempE = eMemory.get(max);
					sMemory.set(max, sMemory.get(i));
					sMemory.set(i, tempS);
					eMemory.set(max, eMemory.get(i));
					eMemory.set(i, tempE);
				}
				IDsize = pSize.get(j);
				for (int i = 0; i < sMemory.size(); i++) {
					memSize = eMemory.get(i) - sMemory.get(i);
					if (memSize >= IDsize) {
						output.add(sMemory.get(i));
						output.add(sMemory.get(i) + IDsize);
						output.add(ID.get(j));
						sMemory.set(i, sMemory.get(i) + IDsize);
						if (sMemory.get(i) == eMemory.get(i)) {
							sMemory.remove(i);
							eMemory.remove(i);
						}
						ID.remove(j);
						pSize.remove(j);
						j = j - 1;
						break;
					}
				}

				j++;
			}
			if (!output.isEmpty()) {
				for (int i = 0; i <= output.size() / 3; i = i + 3) {
					int min = i;
					for (int k = i + 3; k < output.size(); k = k + 3) {
						if (output.get(min) > output.get(k)) {
							min = k;
						}
					}
					int tempS = output.get(i);
					int tempE = output.get(i + 1);
					int tempI = output.get(i + 2);
					output.set(i, output.get(min));
					output.set(i + 1, output.get(min + 1));
					output.set(i + 2, output.get(min + 2));
					output.set(min, tempS);
					output.set(min + 1, tempE);
					output.set(min + 2, tempI);
				}
				int i = 0;
				while (i < output.size()) {
					writer.write(output.get(i) + " " + output.get(i + 1) + " " + output.get(i + 2) + "\n");
					i = i + 3;
				}
			}
			if (!ID.isEmpty()) {
				for (int i = 0; i < ID.size(); i++) {
					allocated = ID.get(i) + ",";
				}
			}
			writer.write("-" + allocated.substring(0, allocated.length() - 1));
			writer.close();

		} catch (IOException e) {
			System.out.println("Not able to read in file.");
			e.printStackTrace();
		}
	}
}
