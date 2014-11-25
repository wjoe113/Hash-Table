package Minor.P3.DS;
// On my honor: 
// 
// - I have not discussed the Java language code in my program with 
// anyone other than my instructor or the teaching assistants 
// assigned to this course. 
// 
// - I have not used Java language code obtained from another student, 
// or any other unauthorized source, either modified or unmodified. 
// 
// - If any Java language code or documentation used in my program 
// was obtained from another source, such as a text book or course 
// notes, that has been clearly noted with a proper citation in 
// the comments of my program. 
// 
// - I have not designed this program in such a way as to defeat or 
// interfere with the normal operation of the Curator System. 
// 
// Joe Wileman



import java.io.FileWriter;
import java.io.IOException;

import Minor.P3.DS.Hashable;
import Minor.P3.DS.slotState;

/**
 * Hash Table main
 * @author Joe
 * @param <T>
 */
public class HashTable<T extends Hashable> {

	T[] Table; // stores data objects
	slotState[] Status; // stores corresponding status values
	int Size; // dimension of Table
	int Usage; // # of data objects stored in Table
	FileWriter Log; // target of logged output
	boolean loggingOn; // write output iff this is true

	// Construct hash table with specified size.
	// Pre:
	// Sz is a positive integer of the form 2^k.
	// Post:
	// this.Table is an array of dimension Sz; all entries are null
	// this.Status is an array of dimension Sz; all entries are EMPTY
	// this.Usage == 0
	// this.Log == null
	// this.loggingOn == false
	//
	@SuppressWarnings("unchecked")
	public HashTable(int Sz) {
		Size = Sz;
		Table = (T[]) new Hashable[Sz];
		Status = new slotState[Sz];
		for (int i = 0; i < Sz; i++) {
			Table[i] = null;
			Status[i] = slotState.EMPTY;
		}
		Usage = 0;
		Log = null;
		loggingOn = false; //switch to true when submitting
	}

	// Attempt insertion of Elem.
	// Pre:
	// Elem is a proper object of type T
	// Post:
	// If Elem already occurs in Table (according to equals()):
	// this.Table is unchanged
	// this.Usage is unchanged
	// Otherwise:
	// Elem is added to Table
	// this.Usage is incremented
	// If loggingOn == true:
	// indices accessed during search are written to Log and
	// success/failure message is written to Log
	// Return: reference to inserted object or null if insertion fails
	//
	public T Insert(T Elem) throws IOException {
		int e = (Elem.Hash()) % Size;
		int i = 1;
		int tool = e;
		int tomb = 0;
		while (Status[tool] != slotState.EMPTY) {
			if (Status[tool] == slotState.TOMBSTONE && tomb == 0) {
				tomb = tool;
			}
			if (loggingOn == true) {
				Log.write("\t" + (tool));
			}
			//System.out.print("\t" + (tool)); //system.out
			if (Elem.equals(Table[tool])) {
				if (loggingOn == true) {
					Log.write("\t" + "duplicate");
				}
				//System.out.println("\t" + "duplicate\n"); //system.out
				return null;
			}
			tool = (e + probe(i)) % Size;
			i++;
		}// empty cell
		if (tomb != 0) {
			Table[tomb] = Elem;
			Status[tomb] = slotState.FULL;
			if (loggingOn == true) {
				Log.write("\t" + (tool) + "\t" + "inserted"); // write tombstone again?
			}
			//System.out.println("\t" + "inserted\n"); //system.out
			Usage++;
			return Elem;
		} else {
			Table[tool] = Elem;
			Status[tool] = slotState.FULL;
			if (loggingOn == true) {
				Log.write("\t" + (tool) + "\t" + "inserted");
			}
			//System.out.println("\t" + (tool) + "\t" + "inserted\n"); //system.out
			Usage++;
			return Elem;
		}
	}

	// Search Table for match to Elem (according to equals()).
	// Pre:
	// Elem is a proper object of type T
	// Post:
	// No member of the hash table object is changed.
	// If loggingOn == true:
	// indices accessed during search are written to Log and
	// success/failure message is written to Log
	// Return reference to matching data object, or null if no match
	// is found.
	public T Find(T Elem) throws IOException {
		int e = (Elem.Hash()) % Size;
		int i = 1;
		int tool = e;
		while (Status[tool] != slotState.EMPTY) {
			if (Status[tool] == slotState.FULL) {
				if (Elem.equals(Table[tool])) {
					if (loggingOn == true) {
						Log.write("\t" + (tool) + "\t" + "found");
					}
					//System.out.println("\t" + (tool) + "\t" + "found\n"); //system.out
					return Elem;
				}
			}
			if (loggingOn == true) {
				Log.write("\t" + (tool));
			}
			//System.out.print("\t" + (tool)); //system.out
			tool = (e + probe(i)) % Size;
			i++;
		}
		if (loggingOn == true) {
			Log.write("\t" + (tool) + "\t" + "record not found");
		}
		//System.out.println("\t" + (tool) + "\t" + "record not found\n"); //system.out
		return null;
	}

	// Delete data object that matches Elem.
	// Pre:
	// Elem is a proper object of type t
	// Post:
	// If Elem does not occur in Table (according to equals()):
	// this.Table is unchanged
	// this.Usage is unchanged
	// Otherwise:
	// matching reference in Table is null
	// this.Usage is decremented
	// If loggingOn == true:
	// indices accessed during search are written to Log and
	// success/failure message is written to Log
	// Return reference to deleted object, or null if not found.
	public T Delete(T Elem) throws IOException {
		int i = 1;
		int e = Elem.Hash() % Size;
		int tool = e;
		while (Status[tool] != slotState.EMPTY) {
			if (Status[tool] == slotState.FULL) {
				if (Elem.equals(Table[tool])) {
					Table[tool] = null;
					Status[tool] = slotState.TOMBSTONE;
					if (loggingOn == true) {
						Log.write("\t" + tool + "\t" + "deleted");
					}
					//System.out.println("\t" + tool + "\t" + "deleted\n"); //system.out
					Usage--;
					return Elem;
				}
			}
			if (loggingOn == true) {
				Log.write("\t" + tool);
			}
			//System.out.print("\t" + tool); //system.out
			tool = (e + probe(i)) % Size;
			i++;
		}
		if (loggingOn == true) {
			Log.write("\t" + tool + "\t" + "not found");
		}
		//System.out.println("\t" + tool + "\t" + "not found\n"); //system.out
		return null;
	}

	// Reset hash table to (almost) same state as when first constructed.
	// Post:
	// this.Table is an array of dimension Sz; all entries are null
	// this.Status is an array of dimension Sz; all entries are
	// EMPTY
	// this.Opt is unchanged
	// this.Usage == 0
	// this.Log is unchanged
	// this.loggingOn is unchanged
	//
	public void Clear() {
		for (int i = 0; i < Size; i++) {
			Table[i] = null;
			Status[i] = slotState.EMPTY;
		}
		Usage = 0;
	}

	// Reset hash table's log output target.
	// Pre:
	// Log is an open on a file, or is null
	// Post:
	// If Log is null, no changes are made.
	// Otherwise: this.Log == Log
	// Return true iff this.Log has been changed and is not null.
	//
	public boolean setLog(FileWriter Log) {
		if (Log == null) {
			return false;
		}
		this.Log = Log;
		loggingOn = true;
		return true;
	}

	// Turn internal logging on.
	// Post:
	// If this.Log is not null, loggingOn == true
	// Otherwise, loggingOn == false
	// Returns final value of loggingOn.
	//
	public boolean logOn() {
		if (this.Log != null) {
			loggingOn = true;
		} else {
			loggingOn = false;
		}
		return loggingOn;
	}

	// Turn internal logging off.
	// Post:
	// loggingOn == false
	// Returns final value of loggingOn.
	//
	public boolean logOff() {
		loggingOn = false;
		return loggingOn;
	}

	// k is the iterator
	public int probe(int k) {
		int pwr2 = k * k;
		int addk = pwr2 + k;
		int end = addk / 2;
		return end;
	}

	// Displays the hash table
	// Provided by McQuain
	public void Display(FileWriter Log) throws IOException {

		if (Usage == 0) {
			Log.write("Hash table is empty.\n");
			return;
		}

		for (int pos = 0; pos < Size; pos++) {

			if (Status[pos] == slotState.FULL) {
				Log.write(pos + ":  " + Table[pos] + "\n");
			} else if (Status[pos] == slotState.TOMBSTONE) {
				Log.write(pos + ":  tombstone" + "\n");
			} else {
				Log.write(pos + ":  empty" + "\n");
			}
		}
	}
}
