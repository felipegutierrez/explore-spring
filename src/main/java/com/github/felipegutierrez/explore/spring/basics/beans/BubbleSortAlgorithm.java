package com.github.felipegutierrez.explore.spring.basics.beans;

import org.springframework.stereotype.Component;

@Component
public class BubbleSortAlgorithm implements SortAlgorithm {
    @Override
    public int[] sort(int[] numbers) {
        bubbleSort(numbers);
        return numbers;
    }

    public void bubbleSort(int[] arr) {
        int i = 0, n = arr.length;
        boolean swapNeeded = true;
        while (i < n - 1 && swapNeeded) {
            swapNeeded = false;
            for (int j = 1; j < n - i; j++) {
                if (arr[j - 1] > arr[j]) {
                    int temp = arr[j - 1];
                    arr[j - 1] = arr[j];
                    arr[j] = temp;
                    swapNeeded = true;
                }
            }
            if (!swapNeeded) {
                break;
            }
            i++;
        }
    }
}
