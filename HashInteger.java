/**
 * 
 */
package Minor.P3.DS;

/**
 * @author Joe
 *
 */
class HashInteger implements Hashable {

	Integer integer;
	
	public HashInteger(int i){
		integer = i;
	}
	
	public int Hash() {
		return integer.hashCode();
	}

	public boolean equals(Object o){
		return equals(o);
	}
}
