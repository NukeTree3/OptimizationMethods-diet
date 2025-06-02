package com.nuketree3.example.optimizationmethodsdiet.helpers;

import com.nuketree3.example.optimizationmethodsdiet.emuns.Gender;
import com.nuketree3.example.optimizationmethodsdiet.emuns.UserActivityType;
import com.nuketree3.example.optimizationmethodsdiet.models.Product;
import com.nuketree3.example.optimizationmethodsdiet.models.UserParam;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Simplex {

    /*
    Нормы БЖУ рассчитываются следующим образом:

    Определяется индивидуальная норма калорий. Для этого можно воспользоваться формулой Миффлина-Сан Жеора или онлайн-калькулятором. 1
    Суточные калории распределяются между нутриентами по желаемому процентному соотношению. Например, если нужно поддерживать текущий вес, то соотношение должно быть 15–20% белка, 30% жиров и 50–55% углеводов. 1
    Калораж каждого нутриента делится на его показатель энергетической ценности. 1 г белков = 4 ккал, 1 г жиров = 9 ккал, 1 г углеводов = 4 ккал. 1
    Пример расчёта: 1

    Предположим, что суточный калораж составил 2000 ккал в сутки, а желаемое соотношение нутриентов — 15% белка, 30% жиров и 55% углеводов. Тогда расчёт граммовки нутриентов будет следующим:

    Белки: (2000 * 0,15) / 4 = 75 г. 1
    Жиры: (2000 * 0,3) / 9 = 67 г. 1
    Углеводы: (2000 * 0,55) / 4 = 275 г. 1
    Потребность человека в белках, жирах и углеводах определяется его возрастом, полом, состоянием здоровья и физической активностью. Если есть сомнения, рекомендуется обратиться за помощью к специалисту — нутрициологу
    */

    public void goSimplex(List<Product> products, UserParam userParam) {

        double[][] temp = simplex(products, userParam);
//        for (double[] a : temp){
//            for(double d : a){
//                System.out.print(d+" ");
//            }
//            System.out.println();
//        }
//        System.out.println();

        extractSolution(temp, products);
        System.out.println("Минимальная стоимость = " + temp[0][temp[0].length-1]);
        for(int i = 0; i < 5; i ++){
            System.out.println(temp[i][temp[0].length-1]);
        }
    }

    public static double[][] simplex(List<Product> products, UserParam userParam) {
        double[][] simplex = createSimplexTable(products, userParam);
//        HashMap<Integer, Integer> fines = new HashMap<>();
//        double fine = 10.0;
        final int zRowIndex = simplex.length - 1; // Индекс строки целевой функции

        // Вывод начальной таблицы для отладки
        //System.out.println("Начальная симплекс-таблица:");
        //printSimplexTable(simplex);
        //System.out.println("_______________________________");

        while (true) {
            double minZ = 0;
            int numberOfColumn = -1;

            // Находим наиболее отрицательный коэффициент в Z-строке
            for (int i = 0; i < simplex[0].length - 1; i++) { // Исключаем RHS
                if (simplex[zRowIndex][i] < minZ) {
                    minZ = simplex[zRowIndex][i];
                    numberOfColumn = i;
                }
            }

            // Если все элементы в строке Z неотрицательны, то решение оптимально
            if (numberOfColumn == -1) {
                System.out.println("Оптимальное решение найдено.");
                break;
            }

            int numberOfRow = -1;
            double minNotNegative = Double.MAX_VALUE;

            // Ищем ведущую строку
            for (int i = 0; i < zRowIndex; i++) { // Исключаем строку целевой функции
                if (simplex[i][numberOfColumn] > 0) {
                    double ratio = simplex[i][simplex[0].length - 1] / simplex[i][numberOfColumn];
                    if (ratio < minNotNegative) {
                        minNotNegative = ratio;
                        numberOfRow = i;
                    }
                }
            }

            // Если не нашли подходящую строку, задача неограничена или нет допустимого решения
            if (numberOfRow == -1) {
                System.out.println("Задача неограничена или нет допустимого решения.");
                return simplex; // Или выбросить исключение
            }

            // Нормализуем ведущую строку
            double pivot = simplex[numberOfRow][numberOfColumn];
            for (int i = 0; i < simplex[0].length; i++) {
                simplex[numberOfRow][i] /= pivot;
            }

            // Обнуляем остальные элементы в ведущем столбце
            for (int i = 0; i < simplex.length; i++) {
                if (i != numberOfRow) {
                    double factor = simplex[i][numberOfColumn];
                    for (int j = 0; j < simplex[0].length; j++) {
                        simplex[i][j] -= factor * simplex[numberOfRow][j];
                    }
                }
            }

            // Проверяем, является ли текущее решение оптимальным
            boolean flag = true;
            for (int i = 0; i < simplex[0].length - 1; i++) { // Исключаем RHS
                System.out.println(simplex[zRowIndex][i]);
                if (simplex[zRowIndex][i] > 0) {
                    flag = false;
                    break;
                }
            }

            if (flag) {
                System.out.println("Оптимальное решение найдено.");
                break;
            }

            // Вывод таблицы после каждой итерации для отладки
            System.out.println("Симплекс-таблица после итерации:");
            //printSimplexTable(simplex);

        }

        // Вывод финального решения
        System.out.println("\nФинальное решение:");
        for (int i = 0; i < zRowIndex; i++) { // Перебираем строки ограничений
            boolean isBasic = false;
            int basicVarIndex = -1;

            for (int j = 0; j < simplex[0].length - 1; j++) { // Перебираем столбцы (кроме RHS)
                // Проверяем, является ли столбец единичным вектором
                int countNonZero = 0;
                for (int k = 0; k < simplex.length; k++) {
                    if (k == i && Math.abs(simplex[k][j] - 1) < 1e-6) {
                        countNonZero++;
                    } else if (k != i && Math.abs(simplex[k][j]) < 1e-6) {
                        //ok
                    } else {
                        countNonZero = 10;
                    }
                }
                if (countNonZero == 1) {
                    isBasic = true;
                    basicVarIndex = j;
                }
            }

            if (isBasic) {
                System.out.printf("x%d = %.2f\n", basicVarIndex + 1, simplex[i][simplex[0].length - 1]);
                //System.out.println(products.get(basicVarIndex-1) + " " + simplex[i][simplex[0].length - 1]);
            } else {
                System.out.printf("x%d = 0\n", i+1);
            }

        }

        System.out.printf("Минимальное значение целевой функции: %.2f\n", simplex[zRowIndex][simplex[0].length - 1]);

        return simplex;
    }

    // Вспомогательная функция для вывода симплекс-таблицы
    private static void printSimplexTable(double[][] simplex) {
        for (double[] row : simplex) {
            System.out.print("[");
            for (double element : row) {
                System.out.print(element);
                System.out.print(",");
            }
            System.out.print("]");
            System.out.println();
        }
        System.out.println();
    }

    public static double[][] createSimplexTable(List<Product> products, UserParam userParam) {

        double calories = targetOfCalories(userParam);
        int proteins = (int) (calories * 0.15) / 4;
        int carbohydrates = (int) (calories * 0.55) / 4;
        int fats = (int) (calories * 0.3) / 9;

        double[][] simplexTable = new double[products.size() + 8 + 1][9];
        int fectPerem = 8;

        for (int i = 0; i < simplexTable.length; i++) {
            if (i < products.size()) {
                simplexTable[i][8] = - products.get(i).getProteins();
                simplexTable[i][7] = - products.get(i).getCarbohydrates();
                simplexTable[i][6] = - products.get(i).getFats();
                simplexTable[i][5] = - products.get(i).getCalories();
                simplexTable[i][4] = products.get(i).getProteins();
                simplexTable[i][3] = products.get(i).getCarbohydrates();
                simplexTable[i][2] = products.get(i).getFats();
                simplexTable[i][1] = products.get(i).getCalories();
                simplexTable[i][0] = products.get(i).getPrice();
            } else {
                simplexTable[i][fectPerem] = 1;
                simplexTable[i][0] = 0;
                fectPerem--;
            }
        }

        simplexTable[simplexTable.length - 1][0] = 0;
        simplexTable[simplexTable.length - 1][1] = calories * 1.15;
        simplexTable[simplexTable.length - 1][2] = fats * 1.15;
        simplexTable[simplexTable.length - 1][3] = carbohydrates * 1.15;
        simplexTable[simplexTable.length - 1][4] = proteins * 1.15;
        simplexTable[simplexTable.length - 1][5] = calories * 0.85;
        simplexTable[simplexTable.length - 1][6] = fats * 0.85;
        simplexTable[simplexTable.length - 1][7] = carbohydrates * 0.85;
        simplexTable[simplexTable.length - 1][8] = proteins * 0.85;

        return rotate90Clockwise(simplexTable);
    }

    public static double[][] rotate90Clockwise(double[][] matrix) {
        if (matrix == null || matrix.length == 0) {
            return new double[0][0];
        }

        int rows = matrix.length;
        int cols = matrix[0].length;

        double[][] rotated = new double[cols][rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                rotated[cols - 1- j][i] = matrix[i][j];
            }
        }

        return rotated;
    }

    public static double targetOfCalories(UserParam userParam) {
        if (userParam.getGender().equals(Gender.MALE)) {
            return ((6.5 * userParam.getUserHeight()) + (10 * userParam.getUserWeight()) - (5 * userParam.getUserAge()) + 5) * coefficientOfPhysicalActivity(userParam.getActivityType());
        } else {
            return ((6.5 * userParam.getUserHeight()) + (10 * userParam.getUserWeight()) - (5 * userParam.getUserAge()) - 161) * coefficientOfPhysicalActivity(userParam.getActivityType());
        }
    }

    public static double coefficientOfPhysicalActivity(UserActivityType activityType) {
        switch (activityType) {
            case VERY_LOW_ACTIVITY -> {
                return 1.2;
            }
            case LOW_ACTIVITY -> {
                return 1.375;
            }
            case MEDIUM_ACTIVITY -> {
                return 1.550;
            }
            case HIGH_ACTIVITY -> {
                return 1.725;
            }
            case VERY_HIGH_ACTIVITY -> {
                return 1.9;
            }
        }
        return 0;
    }

    public static Map<Integer, Double> extractSolution(double[][] simplexTable, List<Product> products) {
        Map<Integer, Double> solution = new HashMap<>();
        int numProducts = products.size();
        int rhsCol = simplexTable[0].length - 1; // Последний столбец (RHS)

        // Проходим по всем переменным (столбцам, кроме RHS)
        for (int col = 0; col < rhsCol; col++) {
            // Проверяем, является ли столбец базисным (имеет одну 1 и остальные 0)
            int oneRow = -1;
            boolean isBasic = true;

            for (int row = 0; row < simplexTable.length - 1; row++) { // Исключаем Z-строку
                double val = simplexTable[row][col];

                if (Math.abs(val - 1.0) < 1e-6) { // Нашли 1
                    if (oneRow == -1) {
                        oneRow = row;
                    } else {
                        isBasic = false; // Больше одной единицы — не базисный
                        break;
                    }
                } else if (Math.abs(val) > 1e-6) { // Нашли ненулевой элемент
                    isBasic = false;
                    break;
                }
            }

            // Если столбец базисный, записываем его значение из RHS
            if (isBasic && oneRow != -1 && col < numProducts) {
                double amount = simplexTable[oneRow][rhsCol];
                System.out.println(products.get(col).getName() + " " + amount);
                solution.put(col, amount); // col = индекс продукта
            }
        }

        return solution;
    }

}


//    public static double[][] simplex(List<Product> products, UserParam userParam) {
//        double[][] simplex = createSimplexTable(products, userParam);
//
//        for (double[] a : simplex){
//            for(double d : a){
//                System.out.print(d+" ");
//            }
//            System.out.println();
//        }
//        System.out.println();
//        while (true) {
//            double minZ = Double.MAX_VALUE;
//            int numberOfColumn = 0;
//
//            for (int i = 0; i < simplex.length; i++) {
//                if (simplex[4][i] < minZ && simplex[4][i] != 0) {
//                    minZ = simplex[4][i];
//                    numberOfColumn = i;
//                }
//            }
//
//            int numberOfRow = 0;
//            double minNotNegative = Double.MAX_VALUE;
//            for (int i = 0; i < 5; i++) {
//                if (simplex[i][numberOfColumn] > 0 && simplex[i][simplex.length - 1] / simplex[i][numberOfColumn] < minNotNegative && simplex[i][simplex.length - 1] / simplex[i][numberOfColumn] > 0) {
//                    //System.out.println(minNotNegative + " " + simplex[i][numberOfColumn]);
//                    minNotNegative = simplex[i][simplex.length - 1] / simplex[i][numberOfColumn];
//                    numberOfRow = i;
//                }
//            }
//
//            for (int i = 0; i < simplex.length; i++) {
//                simplex[numberOfRow][i] = simplex[numberOfRow][i] / simplex[numberOfRow][numberOfColumn];
//            }
//
//            for (int i = 0; i < simplex.length; i++) {
//                if (i != numberOfRow) {
//                    double factor = simplex[i][numberOfColumn];
//                    for (int j = 0; j < simplex[i].length; j++) {
//                        simplex[i][j] -= factor * simplex[numberOfRow][j];
//                    }
//                }
//            }
//
//            boolean flag = true;
//            for (int i = 0; i < simplex[0].length; i++) {
//                if(simplex[4][i] > 0) {
//                    flag = false;
//                    break;
//                }
//            }
//
//            if(flag){
//                break;
//            }
//
//        }
//
//        return simplex;
//    }
