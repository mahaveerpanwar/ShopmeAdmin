package com.shopme.admin.user.export;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class ExcelDuplicateFinder {

    public static void main(String[] args) {
        try {
            FileInputStream fis = new FileInputStream("C:\\Users\\mahpawar\\Desktop\\demoExcel.xlsx");
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);

            List<String> rows = new ArrayList<>();
            for (Row row : sheet) {
                StringBuilder rowContent = new StringBuilder();
                for (Cell cell : row) {
                    rowContent.append(cell.toString()).append(",");
                }
                rows.add(rowContent.toString());
            }

            Set<String> duplicateRows = new HashSet<>();
            Set<String> uniqueRows = new HashSet<>();
            for (String row : rows) {
                if (!uniqueRows.add(row)) {
                    duplicateRows.add(row);
                }
            }

            if (duplicateRows.isEmpty()) {
                System.out.println("No duplicate rows found.");
            } else {
                System.out.println("Duplicate rows:");
                for (String row : duplicateRows) {
                    System.out.println(row);
                }
            }

            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



//public class ExcelDuplicateFinder {
//
//    public static void main(String[] args) {
//        try {
//            // Load Excel file
//            FileInputStream fis = new FileInputStream("C:\\Users\\mahpawar\\Desktop\\demo.xlsx");
//            Workbook workbook = new XSSFWorkbook(fis);
//            Sheet sheet = workbook.getSheetAt(0);
//
//            // Store rows in a set to remove duplicates
//            Set<String> rows = new HashSet<>();
//            for (Row row : sheet) {
//                StringBuilder rowContent = new StringBuilder();
//                for (Cell cell : row) {
//                    rowContent.append(cell.toString()).append(",");
//                }
//                String trimmedRow = rowContent.toString().replaceAll(",$", ""); // Remove trailing comma
//                rows.add(trimmedRow);
//            }
//
//            // Print duplicate rows
//            Set<String> duplicates = findDuplicates(rows);
//            if (!duplicates.isEmpty()) {
//                System.out.println("Duplicate rows found:");
//                for (String duplicate : duplicates) {
//                    System.out.println(duplicate);
//                }
//            } else {
//                System.out.println("No duplicate rows found.");
//            }
//
//            workbook.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static Set<String> findDuplicates(Set<String> rows) {
//        Set<String> duplicateRows = new HashSet<>();
//        Set<String> uniqueRows = new HashSet<>();
//
//        for (String row : rows) {
//            if (!uniqueRows.add(row)) {
//                duplicateRows.add(row);
//            }
//        }
//
//        return duplicateRows;
//    }
//}

//public class ExcelDuplicateFinder {
//
//    public static void main(String[] args) {
//        try {
//            // Load Excel file
//            FileInputStream fis = new FileInputStream("C:\\Users\\mahpawar\\Desktop\\demoExcel.xlsx");
//            Workbook workbook = new XSSFWorkbook(fis);
//            Sheet sheet = workbook.getSheetAt(0);
//
//            // Map to store row content and count
//            Map<String, Integer> rowMap = new HashMap<>();
//
//            // Iterate through rows
//            for (Row row : sheet) {
//                StringBuilder rowContent = new StringBuilder();
//                for (Cell cell : row) {
//                    rowContent.append(cell.toString()).append(",");
//                }
//                String rowString = rowContent.toString();
//
//                // Increment count for this row content in the map
//                rowMap.put(rowString, rowMap.getOrDefault(rowString, 0) + 1);
//            }
//
//            // Print duplicate rows
//            for (Map.Entry<String, Integer> entry : rowMap.entrySet()) {
//                if (entry.getValue() > 1) {
//                    System.out.println("Duplicate Row: " + entry.getKey());
//                }
//            }
//
//            workbook.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}





