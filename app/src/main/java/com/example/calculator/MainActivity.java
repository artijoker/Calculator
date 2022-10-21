package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private boolean isError = false;
    private boolean isFractional = false;
    private boolean isNumberEntered = false;
    private boolean isNegative = false;

    private String number = "0";

    private String expression = "";

    private String numberA = "";
    private String numberB = "";
    private String operator = "";

    private EditText numberViewDisplay;
    private EditText expressionViewDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        expressionViewDisplay = findViewById(R.id.expressionViewDisplay);
        numberViewDisplay = findViewById(R.id.numberViewDisplay);
        numberViewDisplay.setText(number);
    }

    public void clearBtnClick(View view) {
        number = "0";

        isFractional = false;
        isNumberEntered = false;
        isNegative = false;

        numberViewDisplay.setText(number);
        numberViewDisplay.setSelection(numberViewDisplay.getText().length());
    }

    public void fullResetBtnClick(View view) {

        number = "0";

        numberA = "";
        numberB = "";
        operator = "";
        expression = "";

        isFractional = false;
        isNumberEntered = false;
        isNegative = false;
        isError = false;

        numberViewDisplay.setText(number);
        numberViewDisplay.setSelection(numberViewDisplay.getText().length());

        expressionViewDisplay.setText(expression);
        expressionViewDisplay.setSelection(expressionViewDisplay.getText().length());
    }

    public void numberBtnClick(View view) {
        Button button = (Button) view;
        if (isError) {
            isError = false;
            number = button.getText().toString();
        } else {
            if (isNumberEntered) {
                if (Objects.equals(number, "0"))
                    number = button.getText().toString();
                else
                    number += button.getText();
            } else
                number = button.getText().toString();
        }

        if (Objects.equals(operator, ""))
            expressionViewDisplay.setText("");


        isNumberEntered = true;
        numberViewDisplay.setText(number);
        numberViewDisplay.setSelection(numberViewDisplay.getText().length());
    }

    public void operationBtnClick(View view) throws Exception {
        if (isError)
            return;

        Button button = (Button) view;
        if (Objects.equals(operator, "")) {
            operator = button.getText().toString();
            numberA = new BigDecimal(number).toString();
        } else {
            if (isNumberEntered) {
                numberB = number;
                BigDecimal result;
                result = getResultFractionalNumbers(numberA, numberB, operator);
                isNegative = result.compareTo(new BigDecimal("0")) < 0;
                numberA = result.toString();
            }
            operator = button.getText().toString();
        }
        number = numberA;
        expression = numberA + " " + operator;

        isNumberEntered = false;
        isFractional = false;
        numberViewDisplay.setText(number);
        numberViewDisplay.setSelection(numberViewDisplay.getText().length());

        expressionViewDisplay.setText(expression);
        expressionViewDisplay.setSelection(expressionViewDisplay.getText().length());
    }

    public void resultBtnClick(View view) throws Exception {
        if (Objects.equals(numberA, "") || isError)
            return;

        numberB = number;
        expression = numberA + " " + operator + " " + numberB + " =";
        try {
            BigDecimal result = getResultFractionalNumbers(numberA, numberB, operator);
            isNegative = result.compareTo(new BigDecimal("0")) < 0;
            number = result.toString();

            numberViewDisplay.setText(number);
            numberViewDisplay.setSelection(numberViewDisplay.getText().length());

            expressionViewDisplay.setText(expression);
            expressionViewDisplay.setSelection(expressionViewDisplay.getText().length());
        } catch (ArithmeticException ex) {
            isError = true;
            expressionViewDisplay.setText("Деление на ноль невозможно");
            numberViewDisplay.setText("");
        }


        isFractional = false;
        isNumberEntered = false;
        operator = "";
        numberA = "";
        numberB = "";
    }

    public void fractional(View view) {

        if (isFractional || isError)
            return;

        if (Objects.equals(operator, ""))
            expressionViewDisplay.setText("");

        if (isNumberEntered)
            number += ".";
        else
            number = "0.";


        isFractional = true;
        isNumberEntered = true;
        numberViewDisplay.setText(number);
        numberViewDisplay.setSelection(numberViewDisplay.getText().length());
    }

    public void changeSign(View view) {
        if (Objects.equals(number, "0") || isError)
            return;

        if (isNegative)
            number = number.substring(1);
        else
            number = "-" + number;
        isNegative = !isNegative;

        if (Objects.equals(operator, ""))
            expressionViewDisplay.setText("");

        numberViewDisplay.setText(number);
        numberViewDisplay.setSelection(numberViewDisplay.getText().length());
    }

    private BigDecimal getResultFractionalNumbers(String a, String b, String op) throws Exception {
        BigDecimal numA = new BigDecimal(a);
        BigDecimal numB = new BigDecimal(b);
        switch (op) {
            case "+":
                return numA.add(numB);
            case "-":
                return numA.subtract(numB);
            case "x":
                return numA.multiply(numB);
            case "/":
                return numA.divide(numB, 9, RoundingMode.FLOOR).stripTrailingZeros();
            default:
                throw new Exception("Unknown arithmetic operation.");
        }

    }

    private BigInteger getResultIntegerNumbers(String a, String b, String op) throws Exception {
        BigInteger numA = new BigInteger(a);
        BigInteger numB = new BigInteger(b);

        switch (op) {
            case "+":
                return numA.add(numB);
            case "-":
                return numA.subtract(numB);
            case "x":
                return numA.multiply(numB);
            case "/":
                return numA.divide(numB);
            default:
                throw new Exception();
        }

    }
}

