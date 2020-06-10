package com.sunys.core.test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Deque;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunys.core.run.shell.Shell;
import com.sunys.core.run.shell.ShellStateType;
import com.sunys.core.util.LimitQueue;

public class SortTests {

	private static final Logger log = LoggerFactory.getLogger(SortTests.class);
	
	private static int compareCount = 0;
	
	private static int replaceCount = 0;
	
	@Test
	public void telnet() throws Exception {
		Shell shell = Shell.builder().cmdStart("telnet localhost").state()
			//登录成功
			.add(ShellStateType.BIN_BASH_NAME, ShellStateType.BIN_BASH_PATTERN)
			.addHandler(ShellStateType.BIN_BASH_NAME, (sh, str) -> {
				try {
					sh.sendCommand("ls -l", "exit");
				} catch (Exception e) {
					
				}
			})
			//输入用户名
			.add(ShellStateType.INPUT_USERNAME_NAME, ShellStateType.INPUT_USERNAME_PATTERN)
			.addHandler(ShellStateType.INPUT_USERNAME_NAME, (sh, str) -> {
				try {
					sh.sendCommand("123");
				} catch (Exception e) {
					
				}
			})
				.next(ShellStateType.INPUT_USERNAME_NAME)
				//输入密码
				.add(ShellStateType.INPUT_PASSWORD_NAME, ShellStateType.INPUT_PASSWORD_PATTERN)
				.addHandler(ShellStateType.INPUT_PASSWORD_NAME, (sh, str) -> {
					try {
						sh.sendCommand("123");
					} catch (Exception e) {
						
					}
				})
					.next(ShellStateType.INPUT_PASSWORD_NAME)
					//密码错误，退出
					.add(ShellStateType.LOGIN_FAIL_NAME, ShellStateType.LOGIN_FAIL_PATTERN)
					.addHandler(ShellStateType.LOGIN_FAIL_NAME, (sh, str) -> {
						try {
							sh.stop();
						} catch (Exception e) {
							
						}
					})
					//登录成功
					.add(ShellStateType.BIN_BASH_NAME, ShellStateType.BIN_BASH_PATTERN)
					.addHandler(ShellStateType.BIN_BASH_NAME, (sh, str) -> {
						try {
							sh.sendCommand("ls -l", "exit");
						} catch (Exception e) {
							
						}
					})
					.pre()
				.pre()
			.shellBuilder().build();
		shell.start();
	}
	
	@Test
	public void shell() throws Exception {
		Shell.cmdRun("dir");
		
		ProcessBuilder b = new ProcessBuilder("cmd.exe", "/c", "dir");
		Process start = b.start();
		InputStream is = start.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is, "gbk"));
		log.info("isAlive:{}, ready:{}", start.isAlive(), br.ready());
		String line = null;
		while ((line = br.readLine()) != null) {
			log.info(line);
		}
		start.destroyForcibly();
	}
	
	@Test
	public void queue() {
		Deque<String> queue = new LimitQueue<>(4);
		log.info(queue.pollLast());
		for (int i = 0; i < 20; i++) {
//			queue.offerFirst(i + 1 + "");
//			queue.offerLast(i + 1 + "");
			queue.offer(i + 1 + "");
		}
		log.info("queue:{}", String.join(",", queue));
		log.info(queue.pollLast());
		log.info("queue:{}", String.join(",", queue));
	}
	
	@Test
	public void aaa() {
		String s = "ddd\nfffff\nssss\n";
		log.info("s.split:{}", (Object) s.split("\n"));
		log.info("(" + s + ")");
		log.info("");
		log.info("(" + s.substring(0, s.lastIndexOf("\n")) + ")");
		log.info("");
		log.info("(" + s.substring(s.lastIndexOf("\n")) + ")");
	}
	
	@Test
	public void quickSort() {
		int[] arr = {1,2,3,4,5,6};
		partition(arr, 0, arr.length - 1);
	}
	
	private void partition(int[] arr, int start, int end) {
		if (start >= end) {
			return;
		}
		int p = end;
		for (int left = start, right = end - 1; left < right;) {
			
		}
		
	}
	
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
