/*
 * Author: Dillon Barr
 * 
 * Course: CSC 245 Spring 2020, Introduction to Discrete Structures
 * 
 * File: Hmwk8.java
 * 
 * Assignment: Homework #8 (Programming portion)
 * 
 * Description: This program recursively creates the Base Factorial expression
 * of a positive base 10 value given as a command line argument. 
 */


public class Hmwk8 {

	public static void main(String[] args) {
		// This line reads in the command line argument and stores it as an integer.
		int base = Integer.valueOf(args[0]);
		if (base > 0) {
			int counter = 0;
			int starter = get_base(base);
			String output = "The Base Factorial expression of " + Integer.toString(base) + " is ";
			output += get_base_factorial(base, starter, counter);
			System.out.println(output);
		}
	}

public static String get_base_factorial(int number, int base, int counter) {
	/*
     * Purpose: This method is the recursive method that generates the Base Factorial
     * form of the integer passed in on the command line.
     *
     *
     * @param three integers are passed in. Number is the command line argument which
     * we are finding the Base Factorial form of. Base is the highest integer whos factorial
     * result is less than value of the variable number. Counter is a counter used to keep track of the
     * number of times an integers factorial result is subtracted from the variable number.
     *
     *
     * @return a string that contains the Base Factorial form found through
     * recursion.
     */
	
	// This is the "next to base case" in which we have found all the factorial values
	// that go into the number variable but are missing trailing zeros. This case continues
	// recursing until all the trailing zeros are found.
	
	if (number == 0 && base != 1) {
		return (Integer.toString(counter) + "*" + Integer.toString(base) + "! + ") + get_base_factorial(number, base - 1, 0);
	}
	
	// This is the base case where all the factorial values that go into the number variable
	// have been found and there are no more zeros to be found.
	
	if (number == 0 && base == 1) {
		return (Integer.toString(counter) + "*" + Integer.toString(base) + "!");
	}
	
	// This is one of the recursive cases in which the factorial results of base is less than
	// the number variable so we subtract the factorial result from the number, increment the counter and
	// recurse on the method again.
	
	else if (number >= factorialize(base)) {
		number -= factorialize(base);
		counter += 1;
		return get_base_factorial(number, base, counter);
	}
	
	// This case only happens providing that the factorial result of the base variable is greater than 
	// the current value of number. In that case the recursive call returns the desired string output
	// and recurses with the base variable - 1 and resets the counter variable to 0.
	
	else {
		return (Integer.toString(counter) + "*" + Integer.toString(base) + "! + ") + get_base_factorial(number, base - 1, 0);
	}
}


public static int get_base(int num) {
	/*
     * Purpose: This method finds the highest integer who's factorial result is less than the 
     * value of the number passed in from the command line arguments.
     *
     *
     * @param num is the value taken from the command line arguments. It is the number that
     * we are trying to find the Base Factorial form of.
     *
     *
     * @return an integer that is the highest integer such that the factorial result of the 
     * integer is still less than the num variable from the command line argument.
     */
	
	
	int base = 1;
	boolean less = true;
	while (less) {
		int item = factorialize(base);
		if (item < num) {
			base += 1;
		}
		else {
			less = false;
			// if this statement is reached, the value of the integer
			// has gotten too high and the factorial result is higher than the num variable
			// so one is subtracted before the loop exits.
			base -= 1;
		}
	}
	return base;
}

public static int factorialize(int number) {
	/*
     * Purpose: This method finds the factorial form of the passed in integer
     * in a recursive method.
     *
     *
     * @param num represents an integer value which we are finding the factorial
     * form of.
     *
     *
     * @return an integer that is the factorial form of the passed in number.
     */
	
	
	if (number < 0 ) {
		return -1;
	}
	else if (number == 0) {
		return 1;
	}
	else {
		return (number * factorialize(number - 1));
	}
}
}
