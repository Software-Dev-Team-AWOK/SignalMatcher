package main;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

public class Fragment {
//	private List<FingerPrint> list;
//	private List<Integer> constants;
	
	private int[] list;
	private int[] constants;

	public Fragment(int[] list, int[] constants) {
		this.list = list;
		this.constants = constants;
	}
	
	
	public static void main(String[] args) {
		int random[] = {1,2,3,4,5,6,7,8,9,10};			//random numbers
		int[] list1 = {1,4,5,2,5,7,3,19,20,12};			//exact same list one
		int[] list2 = {1,4,5,2,5,7,3,19,20,12};			//exact same list two
		
		Fragment fragment1 = new Fragment(list1,random);
		Fragment fragment2 = new Fragment(list2,random);
		Fragment fragment3 = new Fragment(Arrays.copyOfRange(list2, 0, 5), random);
		System.out.println(fragment1.equals(fragment2));
		System.out.println(fragment3.equals(fragment2));
	}
/*
	public List<FingerPrint> getList() {
		return list;
	}

	public void setList(List<FingerPrint> list) {
		this.list = list;
	}
	
	public List<Integer> getConstants() {
		return constants;
	}

	public void setConstants(List<Integer> constants) {
		this.constants = constants;
	}
	*/
	public int hashCode() {
		int result = 0;
		for (int i = 0; i < list.length; i++) {
			result = result + (list[i] * constants[i]);
		}
		return result;
	}
	
	public int length() {
		return list.length;
	}
	
	public boolean equals(Object o) {
		Fragment input;
		
		
		
		if (!(o instanceof Fragment)) {
			
			return false;
			
		} 
		
		input = (Fragment) o;
		int num_substrings = input.length()-this.length();
		Hashtable<Integer, Fragment> hash = new Hashtable<Integer, Fragment>();
		
		for (int i = 0; i<num_substrings; i++) {
			System.out.println("Home hash value is :" + this.hashCode());
			System.out.println(Arrays.toString(Arrays.copyOfRange( input.list, i, i+this.length()))+ "with hash value : " + new Fragment(Arrays.copyOfRange( input.list, i, i+this.length()),constants).hashCode());
			
			hash.put(new Fragment(Arrays.copyOfRange( input.list, i, i+this.length()),constants).hashCode() % num_substrings, new Fragment(Arrays.copyOfRange( input.list, i, i+this.length()),constants));
		}

		
		if(this.length() == input.length()) {
			
			return this.hashCode() == o.hashCode();
			
		} else {
			/*
			for (int i = 0; i<num_substrings; i++) {
				System.out.println("Home hash value is :" + this.hashCode()%num_substrings);
				System.out.println(Arrays.toString(Arrays.copyOfRange( input.list, i, i+this.length()))+ "with hash value : " + new Fragment(Arrays.copyOfRange( input.list, i, i+this.length()),constants).hashCode()%num_substrings);
				if (hash.get(new Fragment(Arrays.copyOfRange( input.list, i, i+this.length()),constants)) == this.hashCode() % num_substrings) {
					System.out.println(hash.get(new Fragment(Arrays.copyOfRange( input.list, i, i+this.length()),constants)));
					return true;
				}
				
			}
			
			return false;
			*/
			return hash.containsKey(this.hashCode()%num_substrings);
		}
	}
	
}
