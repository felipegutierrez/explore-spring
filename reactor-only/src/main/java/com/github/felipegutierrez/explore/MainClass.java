package com.github.felipegutierrez.explore;

import com.github.felipegutierrez.explore.basics.FluxAndMonoBasics;

import java.util.Arrays;
import java.util.Scanner;

public class MainClass {

    public static void main(String[] args) throws InterruptedException {
        if (args != null && args.length > 0) {
            System.out.println("Executing option: ");
            Arrays.stream(args).forEach(System.out::println);
            Thread.sleep(20000);
        } else {
            Scanner keyboard = new Scanner(System.in);
            System.out.println("Enter an option: ");
            int intOption = keyboard.nextInt();
            System.out.println("option selected: " + intOption);
            switch (intOption) {
                case 0:
                    System.out.println("good bye");
                    break;
                case 1:
                    new FluxAndMonoBasics().run();
                    break;
                default:
                    break;
            }
        }
    }
}
