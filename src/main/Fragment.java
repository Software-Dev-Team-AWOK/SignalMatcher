package main;

import java.util.List;

public class Fragment {
	private List<FingerPrint> list;
	private List<Integer> constants;

	public Fragment(List<FingerPrint> list, List<Integer> constants) {
		this.list = list;
		this.constants = constants;
	}

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
	
	public int hashCode() {
		int result = 0;
		for (int i = 0; i < list.size(); i++) {
			result = result + (list.get(i).hashCode() + constants.get(i));
		}
		return result;
	}
	
	public boolean equals(Object o) {
		return this.hashCode() == o.hashCode();
	}
}
