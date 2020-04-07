package com.sunys.core.test;

import java.util.Arrays;

import org.junit.Test;

public class SortTests {

	private static int compareCount = 0;
	
	private static int replaceCount = 0;
	
	@Test
	public void margeSort() {
		int[] arr = {1,2,3,4,5,6};
		margeSort(arr, 0, arr.length);
		System.out.println(Arrays.toString(arr));
	}
	
	private void margeSort(int[] arr, int start, int len) {
		if (len <= 1) {
			return;
		}
		int arr1Len = len / 2;
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
		System.out.println(Arrays.toString(arr));
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
		System.out.println(Arrays.toString(arr));
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
			System.out.println("replace count:" + replaceCount);
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
		System.out.println(Arrays.toString(arr));
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
		System.out.println(Arrays.toString(arr));
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
		System.out.println(Arrays.toString(arr));
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
		System.out.println("compare count:" + compareCount);
		return arr[i] - arr[j];
	}
	
	private void replace(int[] arr, int i, int j) {
		replaceCount++;
		System.out.println("replace count:" + replaceCount);
		int tmp = arr[i];
		arr[i] = arr[j];
		arr[j] = tmp;
	}
}
