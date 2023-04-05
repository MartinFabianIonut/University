import org.example.Problems;
import org.example.domain.Pair;
import org.example.domain.Pairs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class Lab1Tests {
    Problems myProblems;

    @DisplayName("Testing the first problem...")
    private void testingTheFirstProblem(){
        Assertions.assertEquals(myProblems.theFirstProblem("Ana are mere rosii si galbene"), "si");
        Assertions.assertEquals(myProblems.theFirstProblem("Este o zi linistita de vara"), "zi");
        Assertions.assertEquals(myProblems.theFirstProblem("Credeam ca Zara e un brand romanesc"), "un");
        Assertions.assertEquals(myProblems.theFirstProblem("A trecut pe zerba cand culoarea semaforului era inca rosie"), "zerba");
    }
    @DisplayName("Testing the second problem...")
    private void testingTheSecondProblem(){
        Pair pair1 = new Pair(1,5);
        Pair pair2 = new Pair(4,1);
        Assertions.assertEquals(myProblems.theSecondProblem(pair1,pair2),5.0);
        pair1.setX(2);pair1.setY(2);
        pair2.setX(4);pair2.setY(4);
        Assertions.assertEquals(myProblems.theSecondProblem(pair1,pair2),2*Math.sqrt(2));
        pair1.setX(-3);pair1.setY(2);
        pair2.setX(5);pair2.setY(-15);
        Assertions.assertEquals(myProblems.theSecondProblem(pair1,pair2),Math.sqrt(353));
    }
    @DisplayName("Testing the third problem...")
    private void testingTheThirdProblem(){
        double[][] list1 = {{1,0,2,0,3}};
        double[][] list2 = {{1,2,0,3,1}};
        Assertions.assertEquals(myProblems.theThirdProblem(list1,list2),4);
        list1 = new double[][]{{1, 2,3,4,5,0,0,0,1,0,0,0,0,3}};
        list2 = new double[][]{{10,0,0,4,0,0,6,0,0,8,9,0,0,2}};
        Assertions.assertEquals(myProblems.theThirdProblem(list1,list2),32);
        list1 = new double[][]{{1,2,3,4,5,0,0,0,1,0,0,0,0,3,1,2,3,4},
                {1,2,3,4,5,0,0,0,1,0,0,0,0,3,1,2,3,4}};
        list2 = new double[][]{{0,0,0,4,0,0,6,0,0,8,9,0,0,2,1,1,1,2},
                {0,0,0,4,0,0,6,0,0,8,9,0,0,2,1,1,1,1}};
        Assertions.assertEquals(myProblems.theThirdProblem(list1,list2),68);
    }

    @DisplayName("Testing the forth problem...")
    private void testingTheForthProblem(){
        ArrayList<String> words = new ArrayList<>();
        words.add("mere");
        words.add("rosii");
        Assertions.assertEquals(myProblems.theForthProblem("ana are ana are mere rosii ana"),words);
        words.clear();
        words.add("bine");words.add("ca");words.add("vine");words.add("primavara");
        String text = "el spune bine ca vine el primavara spune el";
        Assertions.assertEquals(myProblems.theForthProblem(text),words);
        words.clear();
        words.add("noapte");words.add("de");words.add("vis");words.add("timp");words.add("preasfant");
        text = "noapte acum de vis acum este un timp un preasfant este";
        Assertions.assertEquals(myProblems.theForthProblem(text),words);
    }

    @DisplayName("Testing the fifth problem...")
    private void testingTheFifthProblem(){
        int[] array = {1,2,3,4,3};
        Assertions.assertEquals(myProblems.theFifthProblem(array),3);
        array = new int[] {1,6,7,2,5,8,3,4,6,9,10};
        Assertions.assertEquals(myProblems.theFifthProblem(array),6);
        array = new int[] {1,6,13,14,15,7,2,5,8,14,3,4,11,12,9,10};
        Assertions.assertEquals(myProblems.theFifthProblem(array),14);
    }

    @DisplayName("Testing the sixth problem...")
    private void testingTheSixthProblem(){
        int[] array = {2,8,7,2,2,5,2,3,1,2,2};
        Assertions.assertEquals(myProblems.theSixthProblem(array),2);
        array = new int[] {1,2,3,4,1,4,2,1,1,1,1,5,2,1,1,1,1,15,5,3,1,1,1};
        Assertions.assertEquals(myProblems.theSixthProblem(array),1);
        array = new int[] {5,5,5,5,5,5,5,4,4,4,4,4,4,4};
        Assertions.assertEquals(myProblems.theSixthProblem(array),0);//no one has more appearances than n/2
    }

    @DisplayName("Testing the seventh problem...")
    private void testingTheSeventhProblem(){
        Assertions.assertEquals(myProblems.theSeventhProblem(new int[]{7,4,6,3,9,1},2),7);
        Assertions.assertEquals(myProblems.theSeventhProblem(new int[]{7,4,6,3,9,1},3),6);
        Assertions.assertEquals(myProblems.theSeventhProblem(new int[]{153,42,5,345,2,535,2,1,345},3),345);
    }
    @DisplayName("Testing the eighth problem...")
    private void testingTheEighthProblem(){
        int n = 5;
        String numbers = "1 10 11 100 101 ";
        Assertions.assertEquals(myProblems.theEighthProblem(n),numbers);
        n = 10;
        numbers = "1 10 11 100 101 110 111 1000 1001 1010 ";
        Assertions.assertEquals(myProblems.theEighthProblem(n),numbers);
        n = 16;
        numbers = "1 10 11 100 101 110 111 1000 1001 1010 1011 1100 1101 1110 1111 10000 ";
        Assertions.assertEquals(myProblems.theEighthProblem(n),numbers);
    }
    @DisplayName("Testing the ninth problem...")
    private void testingTheNinthProblem(){
        int[][]matrix = {{0, 2, 5, 4, 1},
                        {4, 8, 2, 3, 7},
                        {6, 3, 4, 6, 2},
                        {7, 3, 1, 8, 3},
                        {1, 5, 7, 9, 4}};
        Pair p1=new Pair(1,1);
        Pair p2=new Pair(3,3);
        Pair p3=new Pair(2,2);
        Pair p4=new Pair(4,4);
        List<Pairs> pairs = new ArrayList<>();
        pairs.add(new Pairs(p1,p2));
        pairs.add(new Pairs(p3,p4));
        List<Integer>results = new ArrayList<>();
        results.add(38);
        results.add(44);
        Assertions.assertEquals(myProblems.theNinthProblem(matrix,pairs),results);
        pairs.clear();
        p1.setX(0);p1.setY(0);
        p2.setX(1);p2.setY(2);//21
        p3.setX(1);p3.setY(2);
        p4.setX(2);p4.setY(4);//24
        Pair p5=new Pair(3,0);
        Pair p6=new Pair(4,1);//16
        pairs.add(new Pairs(p1,p2));
        pairs.add(new Pairs(p3,p4));
        pairs.add(new Pairs(p5,p6));
        results.clear();
        results.add(21);
        results.add(24);
        results.add(16);
        Assertions.assertEquals(myProblems.theNinthProblem(matrix,pairs),results);
    }
    @DisplayName("Testing the tenth problem...")
    private void testingTheTenthProblem(){
        int[][]matrix = {{0,0,0,1,1},
                        {0,1,1,1,1},
                        {0,0,1,1,1}};
        Assertions.assertEquals(myProblems.theTenthProblem(matrix),1); // 1 represent the second row
        int[][]matrix2 = {{0,0,0,1,1,1,1,1,1,1,1},
                {0,0,0,0,0,1,1,1,1,1,1},
                {0,0,0,0,0,0,0,1,1,1,1},
                {0,0,0,0,1,1,1,1,1,1,1},
                {0,0,1,1,1,1,1,1,1,1,1}};
        Assertions.assertEquals(myProblems.theTenthProblem(matrix2),4);
        int[][]matrix3 = {{0,0,0,1,1,1,1,1,1,1,1,1,1,1},
                {0,0,0,0,0,1,1,1,1,1,1,1,1,1},
                {0,0,0,0,0,0,0,1,1,1,1,1,1,1},
                {0,0,0,0,1,1,1,1,1,1,1,1,1,1},
                {0,0,1,1,1,1,1,1,1,1,1,1,1,1},
                {0,0,0,0,0,1,1,1,1,1,1,1,1,1},
                {0,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {0,0,1,1,1,1,1,1,1,1,1,1,1,1}};
        Assertions.assertEquals(myProblems.theTenthProblem(matrix3),6);
    }

    @Test
    public void runAllTests(){
        myProblems = new Problems();
        testingTheFirstProblem();
        testingTheSecondProblem();
        testingTheThirdProblem();
        testingTheForthProblem();
        testingTheFifthProblem();
        testingTheSixthProblem();
        testingTheSeventhProblem();
        testingTheEighthProblem();
        testingTheNinthProblem();
        testingTheTenthProblem();
    }
}
