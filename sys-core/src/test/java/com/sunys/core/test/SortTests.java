package com.sunys.core.test;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SortTests {

	private static final Logger log = LoggerFactory.getLogger(SortTests.class);
	
	private static int compareCount = 0;
	
	private static int replaceCount = 0;
	
	@Test
	public void binarySearch() {
		int[] arr = {0, 2, 2, 3, 4, 5, 6, 6, 7, 9};
		int value = 7;
		int index = binarySearch(arr, value);
		log.info("arr:{}, index:{}, value:{}", arr, index, value);
	}
	
	private int binarySearch(int[] arr, int value) {
		int start = 0, end = arr.length -1;
		while (start <= end) {
			int m = (start + end) / 2;
			if (value < arr[m]) {
				end = m - 1;
			} else if (value > arr[m]) {
				start = m + 1;
			} else {
				return m;
			}
		}
		return -1;
	}
	
	@Test
	public void topK() {
		int[] arr = {2, 6, 2, 7, 4, 3, 9, 6, 0, 5};
		int k = 3;
		int value = partition(arr, 0, arr.length - 1, k);
		log.info("arr:{}, k:{}, value:{}", arr, k, value);
	}
	
	private int partition(int[] arr, int start, int end, int k) {
		int p = start;
		if (start >= end) {
			return arr[p];
		}
		for (int i = start; i < end; i++) {
			if (compare(arr, i, end) > 0) {
				replace(arr, i, p);
				p++;
			}
		}
		replace(arr, p, end);
		if (p + 1 > k) {
			return partition(arr, start, p - 1, k);
		} else if (p + 1 < k) {
			return partition(arr, p + 1, end, k);
		}
		return arr[p];
	}
	
	@Test
	public void quickSort() {
//		int[] arr = {1,2,3,4,5,6};
		int[] arr = {2, 6, 2, 7, 4, 3, 9, 6, 0, 5};
		partition(arr, 0, arr.length - 1);
		log.info("arr:{}", arr);
	}
	
	/*
	 *  2   6   2   7   4   3   9   6   0   5
	 *[{2}] 6   2   7   4   3   9   6   0   5
	 *  2 [{6}] 2   7   4   3   9   6   0   5
	 *  2  {6} [2]  7   4   3   9   6   0   5
	 *  2   2  {6} [7]  4   3   9   6   0   5
	 *  2   2  {6}  7  [4]  3   9   6   0   5
	 *  2   2   4  {7}  6  [3]  9   6   0   5
	 *  2   2   4   3  {6}  7  [9]  6   0   5
	 *  2   2   4   3  {6}  7   9  [6]  0   5
	 *  2   2   4   3  {6}  7   9   6  [0]  5
	 *  2   2   4   3   0  {7}  9   6   6  [5]
	 *  2   2   4   3   0  {5}  9   6   6   7
	 */
	private void partition(int[] arr, int start, int end) {
		if (start >= end) {
			return;
		}
		//已排区间
		int p = start;
		for (int i = start; i < end; i++) {
			//如果arr[i]大于分区点值
			if (compare(arr, i, end) < 0) {
				//交换元素已排分区点和当前元素
				replace(arr, i, p);
				//分区点向后移
				p++;
			}
		}
		replace(arr, p, end);
		partition(arr, start, p - 1);
		partition(arr, p + 1, end);
	}
	
	@Test
	public void margeSort() {
		int[] arr = {1,2,3,4,5,6};
		margeSort(arr, 0, arr.length);
		log.info("arr:{}", arr);
	}
	
	private void margeSort(int[] arr, int start, int len) {
		if (len <= 1) {
			return;
		}
		int arr1Len = len >> 1;
		int arr2Len = len - arr1Len;
		int start2 = start + arr1Len;
		margeSort(arr, start, arr1Len);
		margeSort(arr, start2, arr2Len);
		marge(arr, start, arr1Len, start2, arr2Len);
	}

	private void marge(int[] arr, int arr1Start, int arr1Len, int arr2Start, int arr2Len) {
		int arr1Index = arr1Start;
		int arr2Index = arr2Start;
		int[] tmp = new int[arr1Len + arr2Len];
		int tmpIndex = 0;
		int start = 0;
		int count = 0;
		while (true) {
			if (arr1Index >= arr1Start + arr1Len) {
				start = arr2Index;
				count = tmp.length - tmpIndex;
				break;
			}
			if (arr2Index >= arr2Start + arr2Len) {
				start = arr1Index;
				count = tmp.length - tmpIndex;
				break;
			}
			if (compare(arr, arr1Index, arr2Index) > 0) {
				tmp[tmpIndex] = arr[arr1Index];
				arr1Index++;
			} else {
				tmp[tmpIndex] = arr[arr2Index];
				arr2Index++;
			}
			tmpIndex++;
		}
		for (int i = 0; i < count; i++, tmpIndex++) {
			tmp[tmpIndex] = arr[start + i];
		}
		for (int i = 0; i < tmp.length; i++) {
			arr[arr1Start + i] = tmp[i];
		}
	}
	
	@Test
	public void insertSort() {
		int[] arr = {1,2,3,4,5,6};
		for (int i = 1; i < arr.length; i++) {
			insertSort(arr, i);
		}
		log.info("arr:{}", arr);
	}
	
	private void insertSort(int[] arr, int len) {
		int start = len;
		for (int i = len - 1; i >= 0; i--) {
			if (compare(arr, i, len) > 0) {
				break;
			}
			start = i;
		}
		if (start != len) {
			insert(arr, start, len);
		}
	}
	
	@Test
	public void insertSort1() {
		int[] arr = {1,2,3,4,5,6};
		for (int i = 1; i < arr.length; i++) {
			insertSort1(arr, i);
		}
		log.info("arr:{}", arr);
	}
	
	private void insertSort1(int[] arr, int len) {
		for (int i = 0; i < len; i++) {
			if (compare(arr, i, len) < 0) {
				insert(arr, i, len);
				break;
			}
		}
	}
	
	private void insert(int[] arr, int start, int len) {
		int tmp = arr[len];
		for(int i = len; i > start; i--) {
			replaceCount++;
			log.info("replace count:" + replaceCount);
			arr[i] = arr[i - 1];
		}
		arr[start] = tmp;
	}
	
	@Test
	public void selectSort1() {
		int[] arr = {1,2,3,4,5,6};
		for (int i = 0; i < arr.length; i++) {
			selectSort1(arr, i);
		}
		log.info("arr:{}", arr);
	}
	
	private void selectSort1(int[] arr, int start) {
		int maxIndex = start;
		for (int i = start + 1; i < arr.length; i++) {
			if (compare(arr, maxIndex, i) < 0) {
				maxIndex = i;
			}
		}
		if (maxIndex != start) {
			replace(arr, start, maxIndex);
		}
	}
	
	@Test
	public void selectSort() {
		int[] arr = {1,2,3,4,5,6};
		for (int i = 0; i < arr.length; i++) {
			selectSort(arr, i);
		}
		log.info("arr:{}", arr);
	}
	
	private void selectSort(int arr[], int start) {
		for (int i = start + 1; i < arr.length; i++) {
			if (compare(arr, start, i) < 0) {
				replace(arr, start, i);
			}
		}
	}

	@Test
	public void bubbleSort() {
		int[] arr = {1,2,3,4,5,6};
		for (int i = 0; i < arr.length; i++) {
			boolean flag = bubbleSort(arr, arr.length - i - 1);
			if (!flag) {
				break;
			}
		}
		log.info("arr:{}", arr);
	}
	
	private boolean bubbleSort(int[] arr, int end) {
		boolean flag = false;
		for (int i = 0; i < end; i++) {
			if (compare(arr, i, i + 1) < 0) {
				replace(arr, i, i + 1);
				flag = true;
			}
		}
		return flag;
	}
	
	private int compare(int[] arr, int i, int j) {
		compareCount++;
		log.info("compare count:" + compareCount);
		return arr[i] - arr[j];
	}
	
	private void replace(int[] arr, int i, int j) {
		replaceCount++;
		log.info("replace count:" + replaceCount);
		int tmp = arr[i];
		arr[i] = arr[j];
		arr[j] = tmp;
	}
}
