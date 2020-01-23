package com.pruebas.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Solution {

	private static int valueChange = 1;

	public static int[] solution(int K, int M, int[] A) {
		return solutionLambda(K, M, A);
    // return solutionClassic(K, M, A);
	}

	public static int[] solutionLambda(int K, int M, int[] A) {
		int[] resul = new int[0];

		if (A.length >= K) {
			AtomicInteger index = new AtomicInteger();

			//  Generate list candidates and increment values by range in each  
			List<List<Integer>> listCandidates = arrayToMultiList(A, A.length - K + 1).stream()
					.map(candidate -> replaceRange(candidate, index.getAndIncrement(), K)).collect(Collectors.toList());

			// Get list Leader, to Treeset, and map to int[]
			resul = new TreeSet<>(
					listCandidates.stream().map(Solution::getLeader).filter(Objects::nonNull).collect(Collectors.toList()))
							.stream().mapToInt(Integer::intValue).toArray();
		}

		return resul;
	}

	private static List<List<Integer>> arrayToMultiList(int[] arraySource, int numCopy){
		return Collections.nCopies(numCopy, Arrays.stream(arraySource).boxed().collect(Collectors.toList()));
	}
	
	private static List<Integer> replaceRange(List<Integer> list, int indexStart, int indexFinish) {
		AtomicInteger indexCurrent = new AtomicInteger();
		return list.stream()
				.map(value -> isInRange(indexCurrent.getAndIncrement(), indexStart, indexFinish) ? value + valueChange : value)
				.collect(Collectors.toList());
	}
	
	private static Integer getLeader(List<Integer> list) {
		return list.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting())).entrySet()
				.stream().max((num1, num2) -> num1.getValue().compareTo(num2.getValue()))
				.filter(num -> num.getValue() > (list.size() / 2)).map(Map.Entry::getKey).orElse(null);
	}

	private static boolean isInRange(Integer indexCurrent, Integer indexStart, Integer indexFinish) {
		return ((indexCurrent >= indexStart) && (indexCurrent < indexStart + indexFinish));
	}


	
	// Alternative classic solution
	public static int[] solutionClassic(int K, int M, int[] A) {
		TreeSet<Integer> resul = new TreeSet<>();
		int[] aux;

		for (int i = 0; i + K <= A.length; i++) {
			aux = A.clone();

			// Suma +1 al subconjunto
			for (int j = i; j < i + K; j++) {
				aux[j]++;
			}

			// Buscamos Leader y agregamos en caso que haya
			Arrays.sort(aux);
			int count = 1;
			for (int k = 1; k < aux.length; k++) {
				count = (aux[k] == aux[k - 1]) ? count + 1 : 1;
				if (count > aux.length / 2) {
					resul.add(aux[k]);
					break;
				}
			}
		}

		return resul.stream().mapToInt(Integer::intValue).toArray();
	}

}
