package main;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

public class Fragment {
//	private List<Fingerprint> list;
//	private List<Integer> constants;
	
    protected int prime = 31;
	//true fields start here
	private Fingerprint[] fingerprints;

	public Fragment(Fingerprint[] fingerprints) {
	    this.fingerprints = fingerprints;
	}

    public boolean match(Fragment input) {
        if(input.length() > this.length())
            return input.match(this);
        int num_substrings = this.length() - input.length();
        Hashtable<Integer,Fragment> hash = new Hashtable<Integer,Fragment>();
        for(int i=0;i<num_substrings;i++) {
            Fragment sub = new Fragment(Arrays.copyOfRange(this.fingerprints, i, i+input.length()));
            hash.put(sub.hashCode(), sub);
        }
        return hash.containsKey(input.hashCode());
    }
    
    public int hashCode() {
    int result = 1;
    for (int i = 0; i < fingerprints.length; i++) {
        result = (10*result) + fingerprints[i].hashCode();
    }
    return result;
    }
    
    public int length() {
    	return fingerprints.length;
    }
    //using HashTable for simplicity; will consider 
    //using array or wrapped arrays.
    //input must always be smaller than 'container'
    
    	
    public boolean equals(Object o) {
        Fragment input;
        if (!(o instanceof Fragment)) {
            return false;
        } 
        input = (Fragment) o;
        if(input.length() == this.length())
            return this.hashCode() == input.hashCode();
        if(input.length() > this.length())
             return input.match(this);
        else return this.match(input);
    }
}
