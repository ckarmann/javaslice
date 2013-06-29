package org.nnamrak.slice;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.nnamrak.util.Slice.end;
import static org.nnamrak.util.Slice.slice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class SliceTest {

	@Test
	public void testString() {
		String word = "HelpA";
		assertEquals('A', slice(word, 4));
		assertEquals("He", slice(word, 0, 2));
		assertEquals("lp", slice(word, 2, 4));

		assertEquals("lpA", slice(word, 2, end));

		// check invariants
		assertEquals(word, slice(word, 0, 2) + slice(word, 2, end));
		assertEquals(word, slice(word, 0, 3) + slice(word, 3, end));

		assertEquals("elpA", slice(word, 1, 100));
		assertEquals("", slice(word, 10, end));
		assertEquals("", slice(word, 2, 1));

		assertEquals('A', slice(word, -1));
		assertEquals('p', slice(word, -2));
		assertEquals("pA", slice(word, -2, end));
		assertEquals("Hel", slice(word, 0, -2));

		assertEquals("HelpA", slice(word, -100, end));
		assertEquals("", slice(word, 1, -100));

		assertEquals("HelpA", slice(word, 0, end, 1));
		assertEquals("HlA", slice(word, 0, end, 2));
		assertEquals("Hp", slice(word, 0, end, 3));
		assertEquals("HA", slice(word, 0, end, 4));
		assertEquals("H", slice(word, 0, end, 5));

		assertEquals("ep", slice(word, 1, end, 2));

		assertEquals("ApleH", slice(word, 0, end, -1));
	}

	@Test(expected = StringIndexOutOfBoundsException.class)
	public void testStringException() {
		String word = "HelpA";
		slice(word, -10);
	}

	@Test(expected = RuntimeException.class)
	public void testStringException2() {
		String word = "HelpA";
		slice(word, 0, end, 0);
	}

	@Test
	public void testArray() {
		Integer[] array = new Integer[] { 42, 14, 0, -32, 1 };
		assertEquals((Integer)1, slice(array, 4));
		assertArrayEquals(new Integer[] { 42, 14 }, slice(array, 0, 2));
		assertArrayEquals(new Integer[] { 0, -32 }, slice(array, 2, 4));

		assertArrayEquals(new Integer[] { 0, -32, 1 }, slice(array, 2, end));

		// check invariants
		assertArrayEquals(array, concat(slice(array, 0, 2), slice(array, 2, end)));
		assertArrayEquals(array, concat(slice(array, 0, 3), slice(array, 3, end)));

		assertArrayEquals(new Integer[] { 14, 0, -32, 1 }, slice(array, 1, 100));
		assertArrayEquals(new Integer[0], slice(array, 10, end));
		assertArrayEquals(new Integer[0], slice(array, 2, 1));

		assertEquals((Integer)1, slice(array, -1));
		assertEquals((Integer)(-32), slice(array, -2));
		assertArrayEquals(new Integer[]{ -32, 1 }, slice(array, -2, end));
		assertArrayEquals(new Integer[]{ 42, 14, 0 }, slice(array, 0, -2));

		assertArrayEquals(array, slice(array, -100, end));
		assertArrayEquals(new Integer[0], slice(array, 1, -100));

		assertArrayEquals(array, slice(array, 0, end, 1));
		assertArrayEquals(new Integer[] { 42, 0, 1 }, slice(array, 0, end, 2));
		assertArrayEquals(new Integer[] { 42, -32 }, slice(array, 0, end, 3));
		assertArrayEquals(new Integer[] { 42, 1 }, slice(array, 0, end, 4));
		assertArrayEquals(new Integer[] { 42 }, slice(array, 0, end, 5));

		assertArrayEquals(new Integer[] { 14, -32 }, slice(array, 1, end, 2));
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testArrayException() {
		Integer[] array = new Integer[] { 42, 14, 0, -32, 1 };
		slice(array, -10);
	}

	@Test
	public void testIntArray() {
		int[] array = new int[] { 42, 14, 0, -32, 1 };
		assertEquals(1, slice(array, 4));
		assertArrayEquals(new int[] { 42, 14 }, slice(array, 0, 2));
		assertArrayEquals(new int[] { 0, -32 }, slice(array, 2, 4));

		assertArrayEquals(new int[] { 0, -32, 1 }, slice(array, 2, end));

		// check invariants
		assertArrayEquals(array, concat(slice(array, 0, 2), slice(array, 2, end)));
		assertArrayEquals(array, concat(slice(array, 0, 3), slice(array, 3, end)));

		assertArrayEquals(new int[] { 14, 0, -32, 1 }, slice(array, 1, 100));
		assertArrayEquals(new int[0], slice(array, 10, end));
		assertArrayEquals(new int[0], slice(array, 2, 1));

		assertEquals(1, slice(array, -1));
		assertEquals(-32, slice(array, -2));
		assertArrayEquals(new int[]{ -32, 1 }, slice(array, -2, end));
		assertArrayEquals(new int[]{ 42, 14, 0 }, slice(array, 0, -2));

		assertArrayEquals(array, slice(array, -100, end));
		assertArrayEquals(new int[0], slice(array, 1, -100));

		assertArrayEquals(array, slice(array, 0, end, 1));
		assertArrayEquals(new int[] { 42, 0, 1 }, slice(array, 0, end, 2));
		assertArrayEquals(new int[] { 42, -32 }, slice(array, 0, end, 3));
		assertArrayEquals(new int[] { 42, 1 }, slice(array, 0, end, 4));
		assertArrayEquals(new int[] { 42 }, slice(array, 0, end, 5));

		assertArrayEquals(new int[] { 14, -32 }, slice(array, 1, end, 2));
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testIntArrayException() {
		int[] array = new int[] { 42, 14, 0, -32, 1 };
		slice(array, -10);
	}

	@Test
	public void testList() {
		List<Integer> list = Arrays.asList(42, 14, 0, -32, 1);
		assertEquals((Integer)1, slice(list, 4));
		assertEquals(Arrays.asList(42, 14), slice(list, 0, 2));
		assertEquals(Arrays.asList(0, -32), slice(list, 2, 4));

		assertEquals(Arrays.asList(0, -32, 1), slice(list, 2, end));

		// check invariants
		assertEquals(list, concat(slice(list, 0, 2), slice(list, 2, end)));
		assertEquals(list, concat(slice(list, 0, 3), slice(list, 3, end)));

		assertEquals(Arrays.asList(14, 0, -32, 1), slice(list, 1, 100));
		assertEquals(new ArrayList<Integer>(), slice(list, 10, end));
		assertEquals(new ArrayList<Integer>(), slice(list, 2, 1));

		assertEquals((Integer)1, slice(list, -1));
		assertEquals((Integer)(-32), slice(list, -2));
		assertEquals(Arrays.asList(-32, 1 ), slice(list, -2, end));
		assertEquals(Arrays.asList(42, 14, 0 ), slice(list, 0, -2));

		assertEquals(list, slice(list, -100, end));
		assertEquals(new ArrayList<Integer>(), slice(list, 1, -100));

		assertEquals(list, slice(list, 0, end, 1));
		assertEquals(Arrays.asList(42, 0, 1), slice(list, 0, end, 2));
		assertEquals(Arrays.asList(42, -32), slice(list, 0, end, 3));
		assertEquals(Arrays.asList(42, 1), slice(list, 0, end, 4));
		assertEquals(Arrays.asList(42), slice(list, 0, end, 5));

		assertEquals(Arrays.asList(14, -32), slice(list, 1, end, 2));
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testListException() {
		List<Integer> list = Arrays.asList(42, 14, 0, -32, 1);
		slice(list, -10);
	}

	private static <E> E[] concat(E[] a, E[] b) {
		E [] result = Arrays.copyOf(a, a.length+b.length);
		System.arraycopy(b, 0, result, a.length, b.length);
		return result;
	}

	private static <E> List<E> concat(List<E> a, List<E> b) {
		List<E> result = new ArrayList<E>(a);
		result.addAll(b);
		return result;
	}

	private static int[] concat(int[] a, int[] b) {
		int [] result = Arrays.copyOf(a, a.length+b.length);
		System.arraycopy(b, 0, result, a.length, b.length);
		return result;
	}

}
