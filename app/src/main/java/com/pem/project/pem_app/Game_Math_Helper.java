package com.pem.project.pem_app;

import java.util.Random;

/**
 * Created by Chrissy on 01.12.2014.
 */
public class Game_Math_Helper {

    Random random;
    private int operand1;
    private int operand2;
    private String operationText;
    private int rightBox;
    private Operation operation;
    private int randWrongValue1;
    private int rightValue;
    private int randWrongValue2;


    public int getRandWrongValue1() {
        return randWrongValue1;
    }

    public int getRandWrongValue2() {
        return randWrongValue2;
    }



    public Operation getOperation() {
        return operation;
    }

    public int getRightValue() {
        return rightValue;
    }



    public enum Operation {
        SUM, DIVISION, MULTIPLICATION, SUBSTRACT
    }



    public void createOperation() {

        random = new Random();
        Operation operation = generateRandOperation();

        operand1 = random.nextInt(98)+2;
        operand2 = random.nextInt(98)+2;

        operationText= String.valueOf(operand1) + " " +
                getOperationString(operation) + " " + String.valueOf(operand2) + "?";


        rightValue = calculateRightValue(operation, operand1, operand2);

        rightBox = random.nextInt(2)+1;
        int plusminus = random.nextInt(1);

        randWrongValue1 = random.nextInt(3)+ (int) rightValue;
        randWrongValue2 = random.nextInt(5)+ (int) rightValue;
        if (plusminus == 0) {
            randWrongValue1 = randWrongValue1+2;
            randWrongValue2 = randWrongValue2+2;
        } else {
            randWrongValue1 = randWrongValue1-2;
            randWrongValue2 = randWrongValue2-2;
        }


    }

    private int calculateRightValue(Operation oper, int operand1, int operand2) {
        int calculation = 0;

        if (oper == Operation.SUM) {
            calculation = operand1 + operand2;
        } else if (oper == Operation.MULTIPLICATION) {
            calculation = operand1 * operand2;
        } else if (oper == Operation.SUBSTRACT) {
            calculation = operand1 - operand2;
        } else if (oper == Operation.DIVISION) {
            calculation = operand1 / operand2;
        }

        return calculation;
    }

    private String getOperationString(Operation oper) {
        String operationText = "";
        if (oper == Operation.SUM) {
            operationText = "+";
        } else if (oper == Operation.MULTIPLICATION) {
            operationText = "*";
        } else if (oper == Operation.SUBSTRACT) {
            operationText = "-";
        } else if (oper == Operation.DIVISION) {
            operationText = "/";
        }
        return operationText;
    }

    private Operation generateRandOperation() {


        int rand = random.nextInt(4);
        operation = null;

        switch(rand) {
            case 0:
                operation = Operation.SUM;
                break;
            case 1:
                operation = Operation.DIVISION;
                break;
            case 2:
                operation = Operation.MULTIPLICATION;
                break;
            case 3:
                operation = Operation.SUBSTRACT;
                break;
        }
        return operation;
    }

    public Random getRandom() {
        return random;
    }

    public int getOperand1() {
        return operand1;
    }

    public int getOperand2() {
        return operand2;
    }

    public String getOperationText() {
        return operationText;
    }

    public int getRightBox() {
        return rightBox;
    }
}
