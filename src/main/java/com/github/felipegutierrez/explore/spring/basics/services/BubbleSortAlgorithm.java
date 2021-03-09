package com.github.felipegutierrez.explore.spring.basics.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BubbleSortAlgorithm implements SortAlgorithm {

    private static final Logger LOGGER = LoggerFactory.getLogger(BubbleSortAlgorithm.class);

    @Override
    public int[] sort(int[] numbers) {
        LOGGER.info("sorting array: {}", numbers);
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
