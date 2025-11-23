package com.example;

import java.sql.*;
import java.util.Properties;
import java.util.Scanner;
import com.mysql.cj.jdbc.Driver;
import java.util.logging.Logger;



/* Базовый вариант. Ввести две строки (не менее 50 символов каждая) с клавиатуры.
Необходимо вывести на экран две введенных ранее строки, вернуть подстроку по индексам (substring()),
перевести все строки в верхний и нижний регистры, найти подстроку (тоже вводить с клавиатуры)
и определить: заканчивается ли строка данной подстрокой (endsWith()).
Сложный  вариант.  Строковый тип данных. Реализовать программу с интерактивным консольным меню,
т.е. вывод списка действий по цифрам.
При этом при нажатии на цифру у нас должно выполняться определенное действие.
Задания полностью идентичны базовому варианту.
При этом в программе данные пункты должны называться следующим образом:
1.  Вывести все таблицы из MySQL.
2.  Создать таблицу в MySQL.
3.  Возвращение подстроки по индексам, результат сохранить в MySQL с последующим выводом в консоль.
4.  Перевод строк в верхний и нижний регистры, результат сохранить в MySQL с последующим выводом в консоль.
5.  Поиск подстроки и определение окончания подстроки, результат сохранить в MySQL с последующим выводом в консоль.
6.  Сохранить все данные (вышеполученные результаты) из MySQL в Excel и вывести на экран.
 */
class Main {
    protected static Scanner sc = new Scanner(System.in);
    private static final String url = "jdbc:mysql://localhost:3306/homework2";
    private static final String username = "root"; // исправлено
    private static final String password = "MyNewPass123!";
    private static Connection con;
    private static String tablename;
    private static String string1;
    private static String string2;

    public static void main(String[] args) {
        try {
            getConnection();
            System.out.println("✓ Подключение к базе данных установлено!\n");
        } catch (SQLException e) {
            System.err.println("✗ Ошибка подключения к БД: " + e.getMessage());
            return;
        }
        inputStrings();
        boolean running = true;
        while(running) {
            displayMenu();
            int selectMenu = getselectMenu();
            switch (selectMenu) {
                case 1 -> progFirst();
                case 2 -> progSecond();
                case 3 -> progFhird();
                case 4 -> progForth();
                case 5 -> progFifth();
                case 6 -> progSixth();
                case 0 -> {
                    closeConnection();
                    running = false;
                    System.out.println("==================");
                    System.out.println("Выход из программы");
                    System.out.println("==================");
                }
                default -> System.out.println("Неверный выбор! Попробуйте снова.");
            }

        }
    }

    private static void getConnection() throws SQLException {
        if (con == null || con.isClosed()) {
            DriverManager.registerDriver(new Driver());

            //try {
            //    Class.forName("com.mysql.cj.jdbc.Driver");
           // } catch (ClassNotFoundException e) {
           //     throw new RuntimeException(e);
            //}

            con = DriverManager.getConnection(url, username, password);
        }

    }
    private static void closeConnection() {
        if (con != null) {
            try { con.close(); } catch (SQLException ignored) {}
        }
    }
    private static void inputStrings() {
        System.out.println("Введите первую строку (минимум 50 символов): ");
        while (true) {
            string1 = sc.nextLine();
            if (string1.length() >= 50) break;
            else {
                System.out.println("Строка слишком короткая. Введите минимум 50 символов:");
            }
        }
        System.out.println("\nВведите вторую строку (минимум 50 символов): ");
        while (true) {
            string2 = sc.nextLine();
            if (string2.length() >= 50) break;
            else
                System.out.println("Строка слишком короткая. Введите минимум 50 символов:");
        }
        System.out.println("\n--- Введенные строки ---");
        System.out.println("Строка 1: " + string1);
        System.out.println("Строка 2: " + string2);
        System.out.println("------------------------\n");
    }

    private static void displayMenu() {
        System.out.println("==============================================================================================");
        System.out.println("Выберите действие: ");
        System.out.println("1. Вывести все таблицы из MySQL.");
        System.out.println("2. Создать таблицу в MySQL.");
        System.out.println("3. Возвращение подстроки по индексам, результат сохранить в MySQL и вывести.");
        System.out.println("4. Перевод строк в верхний и нижний регистры, результат сохранить в MySQL и вывести.");
        System.out.println("5. Поиск подстроки и определение окончания подстроки, результат сохранить в MySQL и вывести.");
        System.out.println("6. Сохранить все данные из MySQL в Excel и вывести путь к файлу.");
        System.out.println("0. Выход.");
        System.out.println("==============================================================================================");
        System.out.print("Ваш выбор: ");
    }

    private static int getselectMenu(){
        String numMenu = sc.nextLine();
        try {
            return Integer.parseInt(numMenu);
        } catch (NumberFormatException e) {
            System.out.println("Неверный формат ввода");
            return -1;
        }
    }

    private static void progFirst(){
        System.out.println("==============================================================================================");
        System.out.println("Вы выбрали действие 1 - вывести все таблицы из MySQL.");
        System.out.println("==============================================================================================");
        boolean hasTable = false;
        try (Statement stmt = con.createStatement();
        ResultSet res = stmt.executeQuery("SHOW TABLES")) {
            while (res.next()) {
                System.out.println(res.getString(1)); // ВАЖНО: добавить индекс 1
                hasTable = true;
        }
            if (!hasTable) {
                System.out.println("В БД пока что нет таблиц");
            }
        } catch (SQLException e) {
            System.err.println("✗ Ошибка при получении таблиц: " + e.getMessage());
        }
        System.out.println();

    }
    private static void progSecond(){
        System.out.println("==============================================================================================");
        System.out.println("Вы выбрали действие 2 - создать таблицу в MySQL.");
        System.out.println("==============================================================================================");
        try (Statement stmt1 = con.createStatement()) {
            System.out.println("ВВедите название будущей таблицы: ");
            tablename = sc.nextLine();
            String query = "CREATE TABLE IF NOT EXISTS " + tablename
                    + "(id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "stroki VARCHAR(255), " +
                    "indexes VARCHAR(255), " +
                    "upp VARCHAR(255), " +
                    "down VARCHAR(255), " +
                    "podStroki VARCHAR(255), " +
                    "end VARCHAR(255))";
            stmt1.executeUpdate(query);
            String insertQuery = "INSERT INTO " + tablename +
                    " (id, stroki) VALUES (?, ?)";

            try (PreparedStatement ps = con.prepareStatement(insertQuery)) {
                ps.setInt(1, 1);
                ps.setString(2, string1);
                ps.executeUpdate();
                ps.setInt(1, 2);
                ps.setString(2, string2);
                ps.executeUpdate();
            }
            System.out.println("Таблица" + tablename+ " создана!");
        } catch (SQLException e) {
            System.err.println("✗ Ошибка при создании таблицы: " + e.getMessage());
        }
        // while ()
        System.out.println();

    }

    private static void progFhird(){
        System.out.println("==============================================================================================");
        System.out.println("Вы выбрали действие 3 - возвращение подстроки по индексам.");
        System.out.println("==============================================================================================");
        System.out.println("Строка 1: " + string1);
        System.out.println("Длина: " + string1.length());
        System.out.print("Введите начальный индекс: ");
        int start = sc.nextInt();
        System.out.print("Введите конечный индекс: ");
        int end = sc.nextInt();

        System.out.println("Строка 2: " + string2);
        System.out.println("Длина: " + string2.length());
        System.out.print("Введите начальный индекс: ");
        int start2 = sc.nextInt();
        System.out.print("Введите конечный индекс: ");
        int end2 = sc.nextInt();
        String result1 = "";
        String result2 = "";

        try {
            result1 = string1.substring(start, end);
            System.out.println("\nРезультат 1: " + result1);
            result2 = string2.substring(start2, end2);  // ИСПРАВЛЕНО: использование string2
            System.out.println("Результат 2: " + result2);
        } catch (StringIndexOutOfBoundsException e) {
            System.err.println("Ошибка: неверные индексы!");
            return;
        }

        try {
            String updateQuery = "UPDATE " + tablename + " SET indexes = ? WHERE id = ?";
            try (PreparedStatement ps = con.prepareStatement(updateQuery)) {
                ps.setString(1, result1);
                ps.setInt(2, 1);
                ps.executeUpdate();

                ps.setString(1, result2);
                ps.setInt(2, 2);
                ps.executeUpdate();
            }
            System.out.println("✓ Результаты сохранены в БД");
        } catch (SQLException e) {
            System.err.println("✗ Ошибка сохранения результатов в БД: " + e.getMessage());
        }
        System.out.println();
        // flj dsdtcnb yt pf,snm!!!




    }
    private static void progForth(){
        System.out.println("==============================================================================================");
        System.out.println("Вы выбрали действие 4 - перевод строк в верхний и нижний регистры.");
        System.out.println("==============================================================================================");
        String upString1 = string1.toUpperCase();
        String downString1 = string1.toLowerCase();
        String upString2 = string2.toUpperCase();
        String downString2 = string2.toLowerCase();
        try {
            String updateQuery = "UPDATE " + tablename + " SET upp = ?, down = ? WHERE id = ?";
            try (PreparedStatement ps = con.prepareStatement(updateQuery)) {
                ps.setString(1, upString1);
                ps.setString(2, downString1);
                ps.setInt(3, 1);
                ps.executeUpdate();
                ps.setString(1, upString2);
                ps.setString(2, downString2);
                ps.setInt(3, 2);
                ps.executeUpdate();
            }

            System.out.println("\n✓ Результаты сохранены в БД");
            String selectQuery = "SELECT id, stroki, upp, down FROM " + tablename;
            try (Statement stmt = con.createStatement();
                 ResultSet rs = stmt.executeQuery(selectQuery)) {
                System.out.println("\nДанные из БД:");
                while (rs.next()) {
                    System.out.println("ID: " + rs.getInt("id"));
                    System.out.println("Строка: " + rs.getString("stroki"));
                    System.out.println("Верхний регистр: " + rs.getString("upp"));
                    System.out.println("Нижний регистр: " + rs.getString("down"));
                    System.out.println("---");
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Ошибка сохранения результатов в БД: " + e.getMessage());
        }
        System.out.println();




    }
    private static void progFifth(){
        System.out.println("==============================================================================================");
        System.out.println("Вы выбрали действие 5 - поиск подстроки и определение окончания подстроки.");
        System.out.println("==============================================================================================");
        if (tablename == null) {
            System.out.println("Сначала создайте таблицу (пункт 2).");
            return;
        }
        System.out.println("Введите желаемую подстроку 1: ");
        String end1 = sc.nextLine();
        System.out.println("Введите желаемую подстроку 2: ");
        String end2 = sc.nextLine();
        boolean contains1 = string1.contains(end1);
        boolean ends11 = string1.endsWith(end1);
        boolean contains2 = string2.contains(end2);
        boolean ends22 = string2.endsWith(end2);

        System.out.println("Строка1 содержит подстроку: " + contains1 + ", заканчивается на подстроку: " + ends11);
        System.out.println("Строка2 содержит подстроку: " + contains2 + ", заканчивается на подстроку: " + ends22);
        try {
            String updateQuery = "UPDATE `" + tablename + "` SET podStroki = ?, end = ? WHERE id = ?";
            try (PreparedStatement ps = con.prepareStatement(updateQuery)) {
                ps.setString(1, String.valueOf(ends11));
                ps.setString(2, (contains1 ? "s1:true;" : "s1:false;") + "e1:" + ends11);
                ps.setInt(3, 1);
                ps.executeUpdate();

                ps.setString(1, String.valueOf(ends22));
                ps.setString(2, (contains2 ? "s2:true;" : "s2:false;") + "e2:" + ends22);
                ps.setInt(3, 2);
                ps.executeUpdate();
            }

            System.out.println("\n✓ Результаты сохранены в БД");
            String selectQuery = "SELECT id, stroki, podStroki, end FROM `" + tablename + "` ORDER BY id";
            try (Statement stmt = con.createStatement();
                 ResultSet rs = stmt.executeQuery(selectQuery)) {
                System.out.println("\nДанные из БД:");
                while (rs.next()) {
                    System.out.println("ID: " + rs.getInt("id"));
                    System.out.println("Строка: " + rs.getString("stroki"));
                    System.out.println("Подстрока: " + rs.getString("podStroki"));
                    System.out.println("Ends/info: " + rs.getString("end"));
                    System.out.println("---");
                }
            }

        } catch (SQLException ex) {
            System.err.println("Ошибка сохранения в БД " + ex.getMessage());
        }

    }
    private static void progSixth(){
        System.out.println("==============================================================================================");
        System.out.println("Вы выбрали действие 6 - сохранение всех данных из MySQL в Excel и выведение пути к файлу.");
        System.out.println("==============================================================================================");
        if (tablename == null) {
                System.out.println("Сначала создайте таблицу (пункт 2).");
                return;
            }
            try (Statement stmt3 = con.createStatement()) {
                System.out.println("Введите будущее название файла: ");
                String name = sc.nextLine();
                if (name.isEmpty()) {
                    System.out.println("Неправильное имя файла");
                    return;
                }
                String q = "SELECT * FROM " + tablename + " INTO OUTFILE 'D:/" + name + "' CHARACTER SET CP1251";
                Statement stmt7 = con.createStatement();
                try {
                    stmt7.executeQuery(q);
                    System.out.println("Данные успешно экспортированы в файл!");
                } catch (SQLException e) {
                    System.out.println("Ошибка экспорта: " + e.getMessage());
                }
                String query1 = "SELECT * FROM " + tablename;
                PreparedStatement ps = con.prepareStatement(query1);
                ResultSet res4 = ps.executeQuery();

                System.out.println("Все данные из таблицы:");
                System.out.println("┌─────┬──────────────────────┬──────────────┬──────────────┬──────────────┬─────────────┬──────────┐");
                System.out.printf("│ %-3s │ %-20s │ %-12s │ %-12s │ %-12s │ %-11s │ %-8s │%n",
                        "ID", "Исх. строка", "Подстрока", "Верхний", "Нижний", "Поиск", "End");
                System.out.println("├─────┼──────────────────────┼──────────────┼──────────────┼──────────────┼─────────────┼──────────┤");
                while (res4.next()) {
                    System.out.println(
                            "ID" + res4.getString("id") + "\t"
                                    + res4.getString("stroki") + "\t"
                                    + res4.getString("indexes") + "\t"
                                    + res4.getString("upp") + "\t"
                                   + res4.getString("down") + "\t"
                                    + res4.getString("podStroki") + "\t"
                                    + res4.getString("end"));


                }
                System.out.println("└─────┴──────────────────────┴──────────────┴──────────────┴──────────────┴─────────────┴──────────┘");
            } catch (SQLException e) {
            System.err.println("Ошибка сохранения в БД" + e.getMessage());
        }

    }



}