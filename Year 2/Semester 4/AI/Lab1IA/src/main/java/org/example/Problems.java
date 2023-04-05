package org.example;

import org.example.domain.Pair;
import org.example.domain.Pairs;

import java.util.*;

public class Problems {
    /**
     * Determination of the last alphabetically word in a sentence
     * @param text - a String that contains word separated by space
     * @return the last word from the text that is the greatest, alphabetically speaking
     */
    public String theFirstProblem(String text){
        String[] words = text.split(" ");
        String finalWord = words[words.length-1];
        for(int i=words.length-2; i>=0; i--){
            if(finalWord.compareTo(words[i]) < 0)
                finalWord = words[i];
        }
        return finalWord;
    }

    /**
     * Finding the euclidean distances between 2 point in plan
     * @param pair1 - identifies the first point in plan
     * @param pair2 - identifies the second point in plan
     * @return the euclidean distance between the points that are given as parameters
     */
    public Double theSecondProblem(Pair pair1, Pair pair2){
        return Math.sqrt(Math.pow(pair1.getX()-pair2.getX(),2)+Math.pow(pair1.getY()-pair2.getY(),2));
    }

    /**
     * Determining the scalar product of two sparse vectors
     * @param list1 - the first sparse vector of real numbers
     * @param list2 - the second sparse vector of real numbers
     * @return the dot product of two sparse vectors containing real numbers
     */
    public double theThirdProblem(double[][] list1, double[][] list2){
        double product = 0d;
        if(list1[0].length!=list2[0].length)
            return -1d;
        for(int i=0;i<=list1.length-1;i++)
            for(int j=0;j<=list1[i].length-1;j++)
                product+=list1[i][j]*list2[i][j];
        return product;
    }

    /**
     * Find those words that appear just once in a text
     * @param text - a String that contains word separated by space
     * @return a HashSet of words that have their frequency = 1 in the text
     */
    public Iterable<String> theForthProblem(String text){
        ArrayList<String> words = new ArrayList<>(List.of(text.split(" ")));
        int i = 0;
        ArrayList<String> once = new ArrayList<>();
        while (i < words.size()){
            boolean repeat = false;
            int j;
            List<Integer> positions = new ArrayList<>();
            for(j=i+1;j<words.size();j++)
                if(Objects.equals(words.get(j), words.get(i)))
                {
                    repeat = true;
                    positions.add(j);
                }
            if(!repeat)
                once.add(words.get(i));
            else {
                int decrease = 0;
                for (Integer pos : positions) {
                    pos -= decrease;
                    decrease ++;
                    words.remove(pos.intValue());
                }
            }
            i++;
        }
        return once;
    }

    /**
     * Find the value that is a duplicate
     * @param array - a list on integers that contains n numbers from 1...n-1, one is a duplicate
     * @return the number that appear twice in array
     */
    public int theFifthProblem(int[] array) {
        int gaussianSum = array.length*(array.length-1)/2;
        for (int j : array) {
            gaussianSum -= j;
        }
        return -gaussianSum;
    }

    /**
     * Finding the majority element in an array (appears more than half of the number of elements)
     * @param array on integer values, where one or more are duplicates
     * @return the majority element in the array
     */
    public int theSixthProblem(int[] array){
        Map<Integer,Integer> frequencyMap = new HashMap<>();
        for (int j : array)//for array[i], this for is enhanced
            if (frequencyMap.containsKey(j))
                frequencyMap.put(j, frequencyMap.get(j) + 1);
            else
                frequencyMap.put(j, 1);
        for(Map.Entry<Integer,Integer> entry: frequencyMap.entrySet())
            if(entry.getValue() > array.length / 2)
                return entry.getKey();
        return 0;
        // stream version, easier
//        List<Integer> list = Arrays.stream(array).boxed().collect(Collectors.toList());
//        return list.stream().filter(number -> Collections.frequency(list,number)>array.length/2).findFirst().get();
    }

    /**
     * Determining the k-th biggest element of a string of numbers
     * @param array - of integer values
     * @param k - the position of the element in the descending sorted array
     * @return the k-th element in the descending sorted vector, array
     */
    public int theSeventhProblem(int [] array, int k){
        int number = array[0];
        for (int value : array) {
            int greaterThan = 0;
            for (int i : array)
                if (value > i)
                    greaterThan++;
            if (greaterThan == array.length - k)
                return value;
        }
        return number;
//        stream version, easier
//        int[] sortedArray = Arrays.stream(array).sorted().toArray();
//        return sortedArray[array.length-k];
    }

    /**
     * Get the binary number of a value
     * @param n integer
     * @return binary of n
     */
    private int getBinary(int n){
       if(n/2!=0){
            int nr = getBinary(n/2);
            int rest = n%2;
            return nr*10+rest;
        }
       else {
           return n%2;
       }
    }

    /**
     * Get all binary numbers from 1 to n
     * @param n integer
     * @return String of all binary numbers from 1 to n
     */
    public String theEighthProblem(int n){
        StringBuilder numbersString = new StringBuilder();
        for (int i=1; i<=n ;i++) {
            Integer nr = getBinary(i);
            numbersString.append(nr).append(" ");
        }
        return numbersString.toString();
    }

    /**
     *
     * @param matrix of integer values
     * @param pairs of integers, that indicates the row and column in a matrix
     * @return All sums of sub-matrices determined by the paris of rows and columns
     */
    public Iterable<Integer> theNinthProblem(int[][] matrix, List<Pairs>pairs){
        List<Integer> sums = new ArrayList<>();
        for(Pairs p: pairs){
            int i1 = p.getP1().getX();
            int i2 = p.getP2().getX();
            int j1 = p.getP1().getY();
            int j2 = p.getP2().getY();
            int sum = 0;
            while(i1<=i2) {
                while (j1 <= j2) {
                    sum += matrix[i1][j1];
                    j1++;
                }
                i1++;
                j1 = p.getP1().getY();
            }
            sums.add(sum);
        }
        return sums;
    }

    /**
     * Finding the line that contains the most numbers 1
     * @param matrix - of int values
     * @return integer
     */
    public int theTenthProblem(int[][] matrix){
        int line=-1, minimum=matrix[0].length;
        for(int i=0;i<matrix.length;i++)
        {
            int j=0;
            while (j<matrix[i].length && matrix[i][j]!=1){
                j++;
            }
            if(j<minimum)
            {
                minimum=j;
                line=i;
            }
        }
        return line;
    }
}
