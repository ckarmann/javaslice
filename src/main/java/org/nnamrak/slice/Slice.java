package org.nnamrak.slice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * A collection of methods to mimic the Python slice notation.
 *
 * This is intended to emulate completely the behaviour of the Python slice with
 * a consistent syntax for both String, List and arrays.
 *
 * As in Python, these methods make a shallow copy of the input String, List or
 * array. Modifying the resulting List or array therefore do not modify the
 * List or array from which they have been produced.
 *
 * <p>
 * import static org.nnamrak.slice.Slice.slice;
 *
 * // ...
 *
 * String str = "HelpA";
 * slice(str, 1, 3); // "el";
 *
 * // ...
 *
 * List<Character> list = Arrays.asList('a', 'b', 'c', 'd');
 * slice(list, 1, 3)); // ['b', 'c' ]
 *
 * // ...
 *
 * int[] array = new int[] { 9, 8, 7, 6);
 * slice(array, 1, 3)); // [ 8, 7 ]
 *
 * </p>
 *
 * @author Cyrille Karmann
 *
 */
public final class Slice {

	// TODO: handle negative and zero step in List and arrays.

	/**
	 * This static constant emulate the "omitted second index" of Python. It marks the end
	 * of the string.
	 */
	public static final int end = Integer.MAX_VALUE;

	/**
	 * Get the character at the specified index of the string.
	 *
	 * @param str the input string.
	 * @param index the index of the character from the start if positive, from the end if negative.
	 * @return the character at the specified index.
	 */
	public static char slice(String str, int index) {
		index = getSingleSpliceIndex(index, str.length());
		return str.charAt(index);
	}

	/**
	 * Get the substring between the specified indices of the string.
	 *
	 * @param str the input string.
	 * @param begin the beginning index of the substring.
	 * @param finish the ending index of the substring.
	 * @return the substring between the specified indices.
	 */
	public static String slice(String str, int begin, int finish) {
		int length = str.length();

		finish = clampFinish(finish, length);
		begin = clampBegin(begin, length);

		if (finish < begin) {
			return "";
		}

		return str.substring(begin, finish);
	}

	/**
	 * Extract as a String the characters of a String that are between two positions
	 *  and are separated by a certain step.
	 *
	 * @param str the input string.
	 * @param begin the index at which the elements are beginning to be extracted.
	 * @param finish the index at which the elements cease to be extracted.
	 * @param step the step between elements of the subsequence in the input string.
	 * @return the elements extracted from the String between the specified indices.
	*/
	public static String slice(String str, int begin, int finish, int step) {
		int length = str.length();

		finish = clampFinish(finish, length);
		begin = clampBegin(begin, length);

		if (finish < begin) {
			return "";
		}

		if (step == 0) {
			throw new RuntimeException("Slice step can not be zero.");
		}

		StringBuilder sb = new StringBuilder();
		if (step > 0) {
			for (int i = begin; i < finish; i+= step) {
				sb.append(str.charAt(i));
			}
		}
		else if (step < 0) {
			for (int i = finish-1; i >= begin; i+= step) {
				sb.append(str.charAt(i));
			}
		}
		return sb.toString();
	}

	/**
	 * Get the element at the specified index of the list.
	 *
	 * @param list the input list.
	 * @param index the index of the element from the start if positive, from the end if negative.
	 * @return the element at the specified index.
	 */
	public static <E> E slice(List<E> list, int index) {
		index = getSingleSpliceIndex(index, list.size());
		return list.get(index);
	}

	/**
	 * Create a List containing the elements between the specified indices of the input list.
	 *
	 * @param list the input list.
	 * @param begin the beginning index of the sublist.
	 * @param finish the ending index of the sublist.
	 * @return the sublist between the specified indices.
	 */
	public static <E> List<E> slice(List<E> list, int begin, int finish) {
		int length = list.size();

		finish = clampFinish(finish, length);
		begin = clampBegin(begin, length);

		if (finish < begin) {
			return new ArrayList<E>();
		}

		return new ArrayList<E>(list.subList(begin, finish));
	}

	/**
	 * Extract as a new List the elements of a List that are between two positions
	 *  and are separated by a certain step.
	 *
	 * @param list the input list.
	 * @param begin the index at which the elements are beginning to be extracted.
	 * @param finish the index at which the elements cease to be extracted.
	 * @param step the step between elements of the subsequence in the input list.
	 * @return a new List containing the elements extracted from the input List.
	*/
	public static <E> List<E> slice(List<E> list, int begin, int finish, int step) {
		int length = list.size();

		finish = clampFinish(finish, length);
		begin = clampBegin(begin, length);

		if (finish < begin) {
			return new ArrayList<E>();
		}

		List<E> result = new ArrayList<E>();
		for (int i = begin; i < finish; i+= step) {
			result.add(list.get(i));
		}
		return result;
	}

	/**
	 * Get the element at the specified index of the array.
	 *
	 * @param array the input array.
	 * @param index the index of the element from the start if positive, from the end if negative.
	 * @return the element at the specified index.
	 */
	public static <E> E slice(E[] array, int index) {
		index = getSingleSpliceIndex(index, array.length);
		return array[index];
	}

	/**
	 * Create an array with the elements that are between the specified indices of the input array.
	 *
	 * @param array the input array.
	 * @param begin the beginning index in the input array.
	 * @param finish the ending index in the input array.
	 * @return an array with the elements between the specified indices of the input array.
	 */
	public static <E> E[] slice(E[] array, int begin, int finish) {
		int length = array.length;

		finish = clampFinish(finish, length);
		begin = clampBegin(begin, length);

		if (finish < begin) {
			// use copyOf to obtain an empty array of same type.
			return Arrays.copyOf(array, 0);
		}

		return Arrays.copyOfRange(array, begin, finish);
	}

	/**
	 * Extract as a new array the elements of an array that are between two positions
	 *  and are separated by a certain step.
	 *
	 * @param array the input array.
	 * @param begin the index at which the elements are beginning to be extracted.
	 * @param finish the index at which the elements cease to be extracted.
	 * @param step the step between elements of the subsequence in the input array.
	 * @return a new array containing the elements extracted from the input array.
	*/
	public static <E> E[] slice(E[] array, int begin, int finish, int step) {
		int length = array.length;

		finish = clampFinish(finish, length);
		begin = clampBegin(begin, length);

		if (finish < begin) {
			// use copyOf to obtain an empty array of same type.
			return Arrays.copyOf(array, 0);
		}

		List<E> result = new ArrayList<E>();
		for (int i = begin; i < finish; i+= step) {
			result.add(array[i]);
		}
		return result.toArray(Arrays.copyOf(array, 0));
	}

	/**
	 * Get the boolean value at the specified index of the array.
	 *
	 * @param array the input array.
	 * @param index the index of the element from the start if positive, from the end if negative.
	 * @return the element at the specified index.
	 */
	public static boolean slice(boolean[] array, int index) {
		index = getSingleSpliceIndex(index, array.length);
		return array[index];
	}

	/**
	 * Create an array with the boolean values that are between the specified indices of the input array.
	 *
	 * @param array the input array.
	 * @param begin the beginning index in the input array.
	 * @param finish the ending index in the input array.
	 * @return an array with the boolean values between the specified indices of the input array.
	 */
	public static boolean[] slice(boolean[] array, int begin, int finish) {
		int length = array.length;

		finish = clampFinish(finish, length);
		begin = clampBegin(begin, length);

		if (finish < begin) {
			return new boolean[0];
		}

		return Arrays.copyOfRange(array, begin, finish);
	}

	/**
	 * Extract as a new array the boolean values of an array that are between two positions
	 *  and are separated by a certain step.
	 *
	 * @param array the input array.
	 * @param begin the index at which the elements are beginning to be extracted.
	 * @param finish the index at which the elements cease to be extracted.
	 * @param step the step between elements of the subsequence in the input array.
	 * @return a new array containing the boolean values extracted from the input array.
	*/
	public static boolean[] slice(boolean[] array, int begin, int finish, int step) {
		int length = array.length;

		finish = clampFinish(finish, length);
		begin = clampBegin(begin, length);

		if (finish < begin) {
			return new boolean[0];
		}

		boolean[] result = new boolean[(finish-begin+step-1)/step];
		for (int i = begin, j=0; i < finish; i+= step, j++) {
			result[j] = array[i];
		}
		return result;
	}

	/**
	 * Get the bye value at the specified index of the array.
	 *
	 * @param array the input array.
	 * @param index the index of the element from the start if positive, from the end if negative.
	 * @return the element at the specified index.
	 */
	public static byte slice(byte[] array, int index) {
		index = getSingleSpliceIndex(index, array.length);
		return array[index];
	}

	/**
	 * Create an array with the byte values that are between the specified indices of the input array.
	 *
	 * @param array the input array.
	 * @param begin the beginning index in the input array.
	 * @param finish the ending index in the input array.
	 * @return an array with the byte values between the specified indices of the input array.
	 */
	public static byte[] slice(byte[] array, int begin, int finish) {
		int length = array.length;

		finish = clampFinish(finish, length);
		begin = clampBegin(begin, length);

		if (finish < begin) {
			return new byte[0];
		}

		return Arrays.copyOfRange(array, begin, finish);
	}

	/**
	 * Extract as a new array the byte values of an array that are between two positions
	 *  and are separated by a certain step.
	 *
	 * @param array the input array.
	 * @param begin the index at which the elements are beginning to be extracted.
	 * @param finish the index at which the elements cease to be extracted.
	 * @param step the step between elements of the subsequence in the input array.
	 * @return a new array containing the byte values extracted from the input array.
	*/
	public static byte[] slice(byte[] array, int begin, int finish, int step) {
		int length = array.length;

		finish = clampFinish(finish, length);
		begin = clampBegin(begin, length);

		if (finish < begin) {
			return new byte[0];
		}

		byte[] result = new byte[(finish-begin+step-1)/step];
		for (int i = begin, j=0; i < finish; i+= step, j++) {
			result[j] = array[i];
		}
		return result;
	}

	/**
	 * Get the char value at the specified index of the array.
	 *
	 * @param array the input array.
	 * @param index the index of the element from the start if positive, from the end if negative.
	 * @return the element at the specified index.
	 */
	public static char slice(char[] array, int index) {
		index = getSingleSpliceIndex(index, array.length);
		return array[index];
	}

	/**
	 * Create an array with the char values that are between the specified indices of the input array.
	 *
	 * @param array the input array.
	 * @param begin the beginning index in the input array.
	 * @param finish the ending index in the input array.
	 * @return an array with the char values between the specified indices of the input array.
	 */
	public static char[] slice(char[] array, int begin, int finish) {
		int length = array.length;

		finish = clampFinish(finish, length);
		begin = clampBegin(begin, length);

		if (finish < begin) {
			return new char[0];
		}

		return Arrays.copyOfRange(array, begin, finish);
	}

	/**
	 * Extract as a new array the char values of an array that are between two positions
	 *  and are separated by a certain step.
	 *
	 * @param array the input array.
	 * @param begin the index at which the elements are beginning to be extracted.
	 * @param finish the index at which the elements cease to be extracted.
	 * @param step the step between elements of the subsequence in the input array.
	 * @return a new array containing the char values extracted from the input array.
	*/
	public static char[] slice(char[] array, int begin, int finish, int step) {
		int length = array.length;

		finish = clampFinish(finish, length);
		begin = clampBegin(begin, length);

		if (finish < begin) {
			return new char[0];
		}

		char[] result = new char[(finish-begin+step-1)/step];
		for (int i = begin, j=0; i < finish; i+= step, j++) {
			result[j] = array[i];
		}
		return result;
	}

	/**
	 * Get the short value at the specified index of the array.
	 *
	 * @param array the input array.
	 * @param index the index of the element from the start if positive, from the end if negative.
	 * @return the element at the specified index.
	 */
	public static short slice(short[] array, int index) {
		index = getSingleSpliceIndex(index, array.length);
		return array[index];
	}

	/**
	 * Create an array with the short values that are between the specified indices of the input array.
	 *
	 * @param array the input array.
	 * @param begin the beginning index in the input array.
	 * @param finish the ending index in the input array.
	 * @return an array with the short values between the specified indices of the input array.
	 */
	public static short[] slice(short[] array, int begin, int finish) {
		int length = array.length;

		finish = clampFinish(finish, length);
		begin = clampBegin(begin, length);

		if (finish < begin) {
			return new short[0];
		}

		return Arrays.copyOfRange(array, begin, finish);
	}

	/**
	 * Extract as a new array the short values of an array that are between two positions
	 *  and are separated by a certain step.
	 *
	 * @param array the input array.
	 * @param begin the index at which the elements are beginning to be extracted.
	 * @param finish the index at which the elements cease to be extracted.
	 * @param step the step between elements of the subsequence in the input array.
	 * @return a new array containing the short values extracted from the input array.
	*/
	public static short[] slice(short[] array, int begin, int finish, int step) {
		int length = array.length;

		finish = clampFinish(finish, length);
		begin = clampBegin(begin, length);

		if (finish < begin) {
			return new short[0];
		}

		short[] result = new short[(finish-begin+step-1)/step];
		for (int i = begin, j=0; i < finish; i+= step, j++) {
			result[j] = array[i];
		}
		return result;
	}

	/**
	 * Get the int value at the specified index of the array.
	 *
	 * @param array the input array.
	 * @param index the index of the element from the start if positive, from the end if negative.
	 * @return the element at the specified index.
	 */
	public static int slice(int[] array, int index) {
		index = getSingleSpliceIndex(index, array.length);
		return array[index];
	}

	/**
	 * Create an array with the int values that are between the specified indices of the input array.
	 *
	 * @param array the input array.
	 * @param begin the beginning index in the input array.
	 * @param finish the ending index in the input array.
	 * @return an array with the int values between the specified indices of the input array.
	 */
	public static int[] slice(int[] array, int begin, int finish) {
		int length = array.length;

		finish = clampFinish(finish, length);
		begin = clampBegin(begin, length);

		if (finish < begin) {
			return new int[0];
		}

		return Arrays.copyOfRange(array, begin, finish);
	}

	/**
	 * Extract as a new array the int values of an array that are between two positions
	 *  and are separated by a certain step.
	 *
	 * @param array the input array.
	 * @param begin the index at which the elements are beginning to be extracted.
	 * @param finish the index at which the elements cease to be extracted.
	 * @param step the step between elements of the subsequence in the input array.
	 * @return a new array containing the int values extracted from the input array.
	*/
	public static int[] slice(int[] array, int begin, int finish, int step) {
		int length = array.length;

		finish = clampFinish(finish, length);
		begin = clampBegin(begin, length);

		if (finish < begin) {
			return new int[0];
		}

		int[] result = new int[(finish-begin+step-1)/step];
		for (int i = begin, j=0; i < finish; i+= step, j++) {
			result[j] = array[i];
		}
		return result;

	}

	/**
	 * Get the long value at the specified index of the array.
	 *
	 * @param array the input array.
	 * @param index the index of the element from the start if positive, from the end if negative.
	 * @return the element at the specified index.
	 */
	public static long slice(long[] array, int index) {
		index = getSingleSpliceIndex(index, array.length);
		return array[index];
	}

	/**
	 * Create an array with the long values that are between the specified indices of the input array.
	 *
	 * @param array the input array.
	 * @param begin the beginning index in the input array.
	 * @param finish the ending index in the input array.
	 * @return an array with the long values between the specified indices of the input array.
	 */
	public static long[] slice(long[] array, int begin, int finish) {
		int length = array.length;

		finish = clampFinish(finish, length);
		begin = clampBegin(begin, length);

		if (finish < begin) {
			return new long[0];
		}

		return Arrays.copyOfRange(array, begin, finish);
	}

	/**
	 * Extract as a new array the long values of an array that are between two positions
	 *  and are separated by a certain step.
	 *
	 * @param array the input array.
	 * @param begin the index at which the elements are beginning to be extracted.
	 * @param finish the index at which the elements cease to be extracted.
	 * @param step the step between elements of the subsequence in the input array.
	 * @return a new array containing the long values extracted from the input array.
	*/
	public static long[] slice(long[] array, int begin, int finish, int step) {
		int length = array.length;

		finish = clampFinish(finish, length);
		begin = clampBegin(begin, length);

		if (finish < begin) {
			return new long[0];
		}

		long[] result = new long[(finish-begin+step-1)/step];
		for (int i = begin, j=0; i < finish; i+= step, j++) {
			result[j] = array[i];
		}
		return result;
	}

	/**
	 * Get the float value at the specified index of the array.
	 *
	 * @param array the input array.
	 * @param index the index of the element from the start if positive, from the end if negative.
	 * @return the element at the specified index.
	 */
	public static float slice(float[] array, int index) {
		index = getSingleSpliceIndex(index, array.length);
		return array[index];
	}

	/**
	 * Create an array with the float values that are between the specified indices of the input array.
	 *
	 * @param array the input array.
	 * @param begin the beginning index in the input array.
	 * @param finish the ending index in the input array.
	 * @return an array with the float values between the specified indices of the input array.
	 */
	public static float[] slice(float[] array, int begin, int finish) {
		int length = array.length;

		finish = clampFinish(finish, length);
		begin = clampBegin(begin, length);

		if (finish < begin) {
			return new float[0];
		}

		return Arrays.copyOfRange(array, begin, finish);
	}

	/**
	 * Extract as a new array the float values of an array that are between two positions
	 *  and are separated by a certain step.
	 *
	 * @param array the input array.
	 * @param begin the index at which the elements are beginning to be extracted.
	 * @param finish the index at which the elements cease to be extracted.
	 * @param step the step between elements of the subsequence in the input array.
	 * @return a new array containing the float values extracted from the input array.
	*/
	public static float[] slice(float[] array, int begin, int finish, int step) {
		int length = array.length;

		finish = clampFinish(finish, length);
		begin = clampBegin(begin, length);

		if (finish < begin) {
			return new float[0];
		}

		float[] result = new float[(finish-begin+step-1)/step];
		for (int i = begin, j=0; i < finish; i+= step, j++) {
			result[j] = array[i];
		}
		return result;
	}

	/**
	 * Get the double value at the specified index of the array.
	 *
	 * @param array the input array.
	 * @param index the index of the element from the start if positive, from the end if negative.
	 * @return the element at the specified index.
	 */
	public static double slice(double[] array, int index) {
		index = getSingleSpliceIndex(index, array.length);
		return array[index];
	}

	/**
	 * Create an array with the double values that are between the specified indices of the input array.
	 *
	 * @param array the input array.
	 * @param begin the beginning index in the input array.
	 * @param finish the ending index in the input array.
	 * @return an array with the double values between the specified indices of the input array.
	 */
	public static double[] slice(double[] array, int begin, int finish) {
		int length = array.length;

		finish = clampFinish(finish, length);
		begin = clampBegin(begin, length);

		if (finish < begin) {
			return new double[0];
		}

		return Arrays.copyOfRange(array, begin, finish);
	}

	/**
	 * Extract as a new array the double values of an array that are between two positions
	 *  and are separated by a certain step.
	 *
	 * @param array the input array.
	 * @param begin the index at which the elements are beginning to be extracted.
	 * @param finish the index at which the elements cease to be extracted.
	 * @param step the step between elements of the subsequence in the input array.
	 * @return a new array containing the double values extracted from the input array.
	*/
	public static double[] slice(double[] array, int begin, int finish, int step) {
		int length = array.length;

		finish = clampFinish(finish, length);
		begin = clampBegin(begin, length);

		if (finish < begin) {
			return new double[0];
		}

		double[] result = new double[(finish-begin+step-1)/step];
		for (int i = begin, j=0; i < finish; i+= step, j++) {
			result[j] = array[i];
		}
		return result;
	}

	// Helper methods to compute actual indices in sequences

	private static int getSingleSpliceIndex(int index, int length) {
		if (index < 0) {
			index = length + index;
		}
		return index;
	}

	private static int clampFinish(int finish, int length) {
		if (finish > length) {
			finish = length;
		}

		if (finish < 0) {
			finish = length + finish;
		}
		return finish;
	}

	private static int clampBegin(int begin, int length) {
		if (begin < 0) {
			begin = length + begin;
		}
		if (begin < 0) {
			begin = 0;
		}
		return begin;
	}
}
