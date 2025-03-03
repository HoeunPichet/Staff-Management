import Helper.Payable;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.awt.*;
import java.util.*;
import java.util.regex.Pattern;

public class Main extends StaffMember {
    Main(int id, String name, String address){
        super(id, name, address);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean isOption = true;
        ArrayList<HashMap<String, Object>> dataList = new ArrayList<>();
        int autoId = 1;

        while(isOption) {
            System.out.println("\n");
            Table tbOption = getTableOption();
            System.out.println(tbOption.render());

            try {
                System.out.println("-".repeat(50));
                System.out.print("-> Choose an option: ");
                String option = scanner.nextLine().replaceAll(" ", "");
                int choice = Integer.parseInt(option);

                switch (choice) {
                    case 1 -> {
                        String info = insertEmployee(autoId);
                        Optional<String> optional = Optional.ofNullable(info);
                        if (optional.isPresent()) {
                            String[] data = info.split("--");
                            StaffMember staff = new Main(autoId, data[0], data[1]);
                            HashMap<String, Object> hashData = new HashMap<>();
                            hashData.put("type", data[2]);
                            hashData.put("staff", staff);

                            if (data[2].equals("Volunteer")) {
                                Payable detail = new Volunteer(autoId, data[0], data[1], Double.parseDouble(data[3]));
                                hashData.put("detail", detail);
                            } else if (data[2].equals("Salaries Employee")) {
                                Payable detail = new SalariedEmployee(autoId, data[0], data[1], Double.parseDouble(data[3]), Double.parseDouble(data[4]));
                                hashData.put("detail", detail);
                            } else {
                                Payable detail = new HourlySalaryEmployee(autoId, data[0], data[1], Integer.parseInt(data[3]), Double.parseDouble(data[4]));
                                hashData.put("detail", detail);
                            }

                            dataList.add(hashData);
                            autoId += 1;
                            System.out.println(green + "✅ Insert successfully!" + reset);
                        }
                    }
                    case 2 -> {
                        String info = updateEmployee(dataList);
                        Optional<String> optional = Optional.ofNullable(info);
                        if (optional.isPresent()) {
                            String[] data = info.split("--");
                            int id = Integer.parseInt(data[1]);
                            StaffMember staff = new Main(id, data[3], data[4]);
                            HashMap<String, Object> hashData = new HashMap<>();
                            hashData.put("type", data[2]);
                            hashData.put("staff", staff);

                            if (data[2].equals("Volunteer")) {
                                double salary = Double.parseDouble(data[5].replaceAll("\\$", ""));
                                Payable detail = new Volunteer(id, data[2], data[4], salary);
                                hashData.put("detail", detail);
                            } else if (data[2].equals("Salaries Employee")) {
                                double salary = Double.parseDouble(data[5].replaceAll("\\$", ""));
                                double bonus = Double.parseDouble(data[6].replaceAll("\\$", ""));
                                Payable detail = new SalariedEmployee(id, data[2], data[4], salary, bonus);
                                hashData.put("detail", detail);
                            } else {
                                int hour = Integer.parseInt(data[5]);
                                double rate = Double.parseDouble(data[6].replaceAll("\\$", ""));
                                Payable detail = new HourlySalaryEmployee(id, data[2], data[4], hour, rate);
                                hashData.put("detail", detail);
                            }
                            dataList.set(Integer.parseInt(data[0]), hashData);
                        }
                    }
                    case 3 -> displayEmployee(dataList);
                    case 4 -> removeEmployee(dataList);
                    case 5 -> {
                        System.out.println("\n\n" + "=".repeat(40));
                        System.out.println(" ".repeat(3) + cyan + "🥲Thank you so much! Good Luck!🥲" + reset);
                        System.out.println("=".repeat(40));
                        isOption = false;
                    }
                    default -> System.out.println(red + "Invalid option! Please choose a valid option (1-5)!" + reset);
                }
            } catch (NumberFormatException e) {
                System.out.println(red + "Invalid option! Please choose a valid option (1-5)!" + reset);
            }
        }
        scanner.close();
    }

    private static Table getTableOption() {
        CellStyle alignCenter = new CellStyle(CellStyle.HorizontalAlign.center);
        Table tbOption = new Table(1, BorderStyle.UNICODE_ROUND_BOX_WIDE);
        tbOption.setColumnWidth(0, 50, 60);
        tbOption.addCell(cyan + "STAFF MANAGEMENT SYSTEM" + reset, alignCenter);
        tbOption.addCell(" ".repeat(3) + blue + "1. Insert Employee" + reset);
        tbOption.addCell(" ".repeat(3) + blue + "2. Update Employee" + reset);
        tbOption.addCell(" ".repeat(3) + blue + "3. Display Employee" + reset);
        tbOption.addCell(" ".repeat(3) + blue + "4. Remove Employee" + reset);
        tbOption.addCell(" ".repeat(3) + blue + "5. Exit" + reset);
        return tbOption;
    }

    private static String insertEmployee(int id) {
        Scanner scanner = new Scanner(System.in);

        String infomation = null;
        boolean isType = true;

        System.out.println("\n" + "=".repeat(20) + cyan + " INSERT EMPLOYEE " + reset + "=".repeat(20));
        while(isType) {
            System.out.println(yellow + "\nChoose a type of employee" + reset);
            CellStyle alignCenter = new CellStyle(CellStyle.HorizontalAlign.center);
            Table tbType = new Table(4, BorderStyle.UNICODE_ROUND_BOX_WIDE, ShownBorders.ALL);
            tbType.addCell(blue + " ".repeat(2) + "1. Volunteer" + " ".repeat(2) + reset, alignCenter);
            tbType.addCell(blue + " ".repeat(2) + "2. Salaries Employee" + " ".repeat(2) + reset, alignCenter);
            tbType.addCell(blue + " ".repeat(2) + "3. Hourly Employee" + " ".repeat(2) + reset, alignCenter);
            tbType.addCell(blue + " ".repeat(2) + "4. Back" + " ".repeat(2) + reset, alignCenter);
            System.out.println(tbType.render());

            try {
                System.out.print("=> Enter Type Number: ");
                String type = scanner.nextLine().replaceAll(" ", "");
                int typeNumber = Integer.parseInt(type);
                if (typeNumber >= 1 && typeNumber <= 4) {
                    isType = false;
                    if (typeNumber != 4) {
                        System.out.println("ID: " + id);
                        boolean isName = true;
                        while(isName) {
                            System.out.print("=> Enter Name: ");
                            String name = scanner.nextLine();
                            if (Pattern.matches("^[a-zA-Z\\s]+$", name) && !name.isBlank()) {
                                infomation = name;
                                boolean isAddress = true;
                                while(isAddress) {
                                    System.out.print("=> Enter Address: ");
                                    String address = scanner.nextLine();
                                    if (!address.isBlank()) {
                                        infomation += "--" + address;
                                        isAddress = false;
                                    } else {
                                        System.out.println(red + "Invalid input! Please input properly!" + reset);
                                    }
                                }
                                isName = false;
                            } else {
                                System.out.println(red + "Invalid input! Name is allowed only letter!" + reset);
                            }
                        }

                        switch (typeNumber) {
                            case 1 -> {
                                infomation += "--" + "Volunteer";
                                boolean isSalary = true;
                                while(isSalary) {
                                    System.out.print("=> Enter Salary: ");
                                    String salary = scanner.nextLine().replaceAll(" ", "");
                                    if(isMoney(salary)) {
                                        if(salary.length() <= 20) {
                                            infomation += "--" + salary;
                                            isSalary = false;
                                        } else {
                                            System.out.println(red + "Value cannot be greater than 20 characters!" + reset);
                                        }
                                    } else {
                                        System.out.println(red + "Input invalid! Please input properly!" + reset);
                                    }
                                }
                            }
                            case 2 -> {
                                infomation += "--" + "Salaries Employee";
                                boolean isSalary = true;
                                while(isSalary) {
                                    System.out.print("=> Enter Salary: ");
                                    String salary = scanner.nextLine().replaceAll(" ", "");
                                    if(isMoney(salary)) {
                                        if(salary.length() <= 20) {
                                            infomation += "--" + salary;
                                            boolean isBonus = true;
                                            while(isBonus) {
                                                System.out.print("=> Enter Bonus: ");
                                                String bonus = scanner.nextLine().replaceAll(" ", "");
                                                if(isMoney(bonus)) {
                                                    if(bonus.length() <= 20) {
                                                        infomation += "--" + bonus;
                                                        isBonus = false;
                                                    } else {
                                                        System.out.println(red + "Value cannot be greater than 20 characters!" + reset);
                                                    }
                                                } else {
                                                    System.out.println(red + "Input invalid! Please input properly!" + reset);
                                                }
                                            }
                                            isSalary = false;
                                        } else {
                                            System.out.println(red + "Value cannot be greater than 20 characters!" + reset);
                                        }
                                    } else {
                                        System.out.println(red + "Input invalid! Please input properly!" + reset);
                                    }
                                }
                            }
                            case 3 -> {
                                infomation += "--" + "Hourly Employee";
                                boolean isHourWorked = true;
                                while(isHourWorked) {
                                    System.out.print("=> Enter Hour Worked: ");
                                    String hourWorked = scanner.nextLine().replaceAll(" ", "");
                                    if(isNumber(hourWorked)) {
                                        if(hourWorked.length() <= 20) {
                                            infomation += "--" + hourWorked;
                                            boolean isRate = true;
                                            while(isRate) {
                                                System.out.print("=> Enter Rate: ");
                                                String rate = scanner.nextLine().replaceAll(" ", "");
                                                if(isMoney(rate)) {
                                                    if(rate.length() <= 20) {
                                                        infomation += "--" + rate;
                                                        isRate = false;
                                                    } else {
                                                        System.out.println(red + "Value cannot be greater than 20 characters!" + reset);
                                                    }
                                                } else {
                                                    System.out.println(red + "Input invalid! Please input properly!" + reset);
                                                }
                                            }
                                            isHourWorked = false;
                                        } else {
                                            System.out.println(red + "Value cannot be greater than 20 characters!" + reset);
                                        }
                                    } else {
                                        System.out.println(red + "Input invalid! Please input properly!" + reset);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    System.out.println(red + "Invalid option! Please choose a valid option (1-4)!" + reset);
                }
            } catch (NumberFormatException e) {
                System.out.println(red + "Invalid option! Please choose a valid option (1-4)!" + reset);
            }
        }
        return infomation;
    }

    private static void displayEmployee(ArrayList<HashMap<String, Object>> dataList) {
        CellStyle alignCenter = new CellStyle(CellStyle.HorizontalAlign.center);

        int currentPage = 1;
        boolean isDisplay = true;

        if(!dataList.isEmpty()) {
            while(isDisplay) {
                Table tbData = new Table(9, BorderStyle.UNICODE_ROUND_BOX_WIDE, ShownBorders.ALL);
                tbData.addCell("Type", alignCenter);
                tbData.addCell("ID", alignCenter);
                tbData.addCell("Name", alignCenter);
                tbData.addCell("Address", alignCenter);
                tbData.addCell("Salary", alignCenter);
                tbData.addCell("Bonus", alignCenter);
                tbData.addCell("Hour", alignCenter);
                tbData.addCell("Rate", alignCenter);
                tbData.addCell("Pay", alignCenter);

                int totalItem = dataList.size();
                int perPage = 3;
                int pages = (totalItem % perPage) > 0 ? (totalItem / perPage + 1) : totalItem / perPage;
                int showFrom = currentPage * perPage - perPage + 1;
                int showTo = currentPage * perPage;
                showTo = showTo > totalItem ? totalItem : showTo;

                System.out.println("\n" + "=".repeat(20) + cyan + " EMPLOYEE INFORMATION " + reset + "=".repeat(20));
                int j = 0;
                for (HashMap<String, Object> map : dataList) {
                    if (j >= (currentPage * perPage - perPage) && j < (currentPage * perPage) && j < totalItem) {
                        String[] staff = map.get("staff").toString().split("--");
                        String type = map.get("type").toString();
                        tbData.addCell(type, alignCenter);
                        for (String st : staff)
                            tbData.addCell(st, alignCenter);

                        if (type.equals("Volunteer")) {
                            tbData.addCell(map.get("detail").toString(), alignCenter);
                            tbData.addCell("---", alignCenter);
                            tbData.addCell("---", alignCenter);
                            tbData.addCell("---", alignCenter);
                            tbData.addCell(map.get("detail").toString(), alignCenter);
                        } else if (type.equals("Salaries Employee")) {
                            String[] detail = map.get("detail").toString().split("--");
                            tbData.addCell(detail[0], alignCenter);
                            tbData.addCell(detail[1], alignCenter);
                            tbData.addCell("---", alignCenter);
                            tbData.addCell("---", alignCenter);
                            tbData.addCell(detail[2], alignCenter);
                        } else {
                            String[] detail = map.get("detail").toString().split("--");
                            tbData.addCell("---", alignCenter);
                            tbData.addCell("---", alignCenter);
                            tbData.addCell(detail[0], alignCenter);
                            tbData.addCell(detail[1], alignCenter);
                            tbData.addCell(detail[2], alignCenter);
                        }
                    }
                    j++;
                }
                System.out.println(tbData.render());
                System.out.print(blue + "Page: " + currentPage + " of " + pages);
                System.out.println("\t\tShowing: " + showFrom + " to " + showTo + " of " + totalItem + " rows" + reset);

                System.out.println("\n1. First Page \t2. Next Page \t3. Previous Page \t4. Last Page \t5. Exit");
                System.out.print("=> Enter your choice: ");
                String paging = new Scanner(System.in).nextLine().replaceAll(" ", "");
                int pagingNumber = Pattern.matches("^[1-5]$", paging) ? Integer.parseInt(paging) : 0;
                switch (pagingNumber) {
                    case 1 -> currentPage = 1;
                    case 2 -> {
                        if (totalItem - (perPage * currentPage) >= 1)
                            currentPage += 1;
                    }
                    case 3 -> {
                        if (currentPage > 1)
                            currentPage -= 1;
                    }
                    case 4 -> {
                        if (totalItem % perPage > 0)
                            currentPage = totalItem / perPage + 1;
                        else
                            currentPage = totalItem / perPage;
                    }
                    case 5 -> isDisplay = false;
                    default -> System.out.println(red + "Invalid option! Please choose a valid option (1-5)!" + reset);
                }
            }
        } else {
            System.out.println(red + "No data to show! Please insert first!" + reset);
        }
    }

    private static String updateEmployee(ArrayList<HashMap<String, Object>> dataList) {
        String information = null;
        if(!dataList.isEmpty()) {
            while(true) {
                System.out.println("\n" + "=".repeat(20) + cyan + " UPDATE EMPLOYEE " + reset + "=".repeat(20));
                System.out.print("=> Enter or Search ID to update: ");
                try {
                    int id = Integer.parseInt(new Scanner(System.in).nextLine().replaceAll(" ", ""));
                    CellStyle alignCenter = new CellStyle(CellStyle.HorizontalAlign.center);
                    System.out.println();
                    int index = 0;

                    for (HashMap<String, Object> map : dataList) {
                        String[] staff = map.get("staff").toString().split("--");
                        int staffId = Integer.parseInt(staff[0]);
                        if (staffId == id) {
                            String[] detail = map.get("detail").toString().split("--");
                            String type = map.get("type").toString();

                            if (type.equals("Volunteer")) {
                                information = null;
                                while (true) {
                                    Table tbVol = new Table(6, BorderStyle.UNICODE_ROUND_BOX_WIDE, ShownBorders.ALL);
                                    tbVol.addCell("Type", alignCenter);
                                    tbVol.addCell("ID", alignCenter);
                                    tbVol.addCell("Name", alignCenter);
                                    tbVol.addCell("Address", alignCenter);
                                    tbVol.addCell("Salary", alignCenter);
                                    tbVol.addCell("Pay", alignCenter);

                                    String[] existedInfo = null;

                                    if (information == null) {
                                        tbVol.addCell(type, alignCenter);
                                        tbVol.addCell("" + id, alignCenter);
                                        tbVol.addCell(staff[1], alignCenter);
                                        tbVol.addCell(staff[2], alignCenter);
                                        tbVol.addCell(detail[0], alignCenter);
                                        tbVol.addCell(detail[0], alignCenter);
                                    } else {
                                        existedInfo = information.split("--");
                                        tbVol.addCell(existedInfo[2], alignCenter);
                                        tbVol.addCell(existedInfo[1], alignCenter);
                                        tbVol.addCell(existedInfo[3], alignCenter);
                                        tbVol.addCell(existedInfo[4], alignCenter);
                                        tbVol.addCell("$" + Double.parseDouble(existedInfo[5].replaceAll("\\$", "")), alignCenter);
                                        tbVol.addCell("$" + Double.parseDouble(existedInfo[5].replaceAll("\\$", "")), alignCenter);
                                    }

                                    System.out.println(tbVol.render());

                                    try {
                                        System.out.println("1. Name \t2. Address \t3. Salary \t0. Cancel");
                                        System.out.print("Choose one column to update: ");
                                        int option = Integer.parseInt(new Scanner(System.in).nextLine().replaceAll(" ", ""));

                                        if (option >= 0 && option <= 3) {
                                            switch (option) {
                                                case 1 -> {
                                                    String info = updateName(id);
                                                    if (info != null) {
                                                        if (existedInfo == null) {
                                                            information = index + "--" + id + "--" + type + "--" + info + "--" + staff[2] + "--" + detail[0];
                                                        } else {
                                                            information = index + "--" + id + "--" + type + "--" + info + "--" + existedInfo[4] + "--" + existedInfo[5];
                                                        }
                                                    }
                                                }
                                                case 2 -> {
                                                    String info = updateAddress(id);
                                                    if (info != null) {
                                                        if (existedInfo == null) {
                                                            information = index + "--" + id + "--" + type + "--" + staff[1] + "--" + info + "--" + detail[0];
                                                        } else {
                                                            information = index + "--" + id + "--" + type + "--" + existedInfo[3] + "--" + info + "--" + existedInfo[5];
                                                        }
                                                    }
                                                }
                                                case 3 -> {
                                                    String info = updateMoney(id, "salary");
                                                    if (info != null) {
                                                        if (existedInfo == null) {
                                                            information = index + "--" + id + "--" + type + "--" + staff[1] + "--" + staff[2] + "--" + info;
                                                        } else {
                                                            information = index + "--" + id + "--" + type + "--" + existedInfo[3] + "--" + existedInfo[4] + "--" + info;
                                                        }
                                                    }
                                                }
                                                default -> {
                                                    return information;
                                                }
                                            }
                                        } else {
                                            System.out.println(red + "Invalid option! Please choose a valid option (0-3)!" + reset);
                                        }
                                    } catch (NumberFormatException e) {
                                        System.out.println(red + "Invalid option! Please choose a valid option (0-3)!" + reset);
                                    }
                                }
                            } else if (type.equals("Salaries Employee")) {
                                information = null;
                                while (true) {
                                    Table tbSal = new Table(7, BorderStyle.UNICODE_ROUND_BOX_WIDE, ShownBorders.ALL);
                                    tbSal.addCell("Type", alignCenter);
                                    tbSal.addCell("ID", alignCenter);
                                    tbSal.addCell("Name", alignCenter);
                                    tbSal.addCell("Address", alignCenter);
                                    tbSal.addCell("Salary", alignCenter);
                                    tbSal.addCell("Bonus", alignCenter);
                                    tbSal.addCell("Pay", alignCenter);

                                    String[] existedInfo = null;

                                    if (information == null) {
                                        double pays = Double.parseDouble(detail[0].replaceAll("\\$", "")) + Double.parseDouble(detail[1].replaceAll("\\$", ""));
                                        tbSal.addCell(type, alignCenter);
                                        tbSal.addCell("" + id, alignCenter);
                                        tbSal.addCell(staff[1], alignCenter);
                                        tbSal.addCell(staff[2], alignCenter);
                                        tbSal.addCell(detail[0], alignCenter);
                                        tbSal.addCell(detail[1], alignCenter);
                                        tbSal.addCell("$" + pays, alignCenter);
                                    } else {
                                        existedInfo = information.split("--");
                                        double pays = Double.parseDouble(existedInfo[5].replaceAll("\\$", "")) + Double.parseDouble(existedInfo[6].replaceAll("\\$", ""));
                                        tbSal.addCell(existedInfo[2], alignCenter);
                                        tbSal.addCell(existedInfo[1], alignCenter);
                                        tbSal.addCell(existedInfo[3], alignCenter);
                                        tbSal.addCell(existedInfo[4], alignCenter);
                                        tbSal.addCell("$" + Double.parseDouble(existedInfo[5].replaceAll("\\$", "")), alignCenter);
                                        tbSal.addCell("$" + Double.parseDouble(existedInfo[6].replaceAll("\\$", "")), alignCenter);
                                        tbSal.addCell("$" + pays, alignCenter);
                                    }

                                    System.out.println(tbSal.render());

                                    try {
                                        System.out.println("1. Name \t2. Address \t3. Salary \t4. Bonus \t0. Cancel");
                                        System.out.print("Choose one column to update: ");
                                        int option = Integer.parseInt(new Scanner(System.in).nextLine().replaceAll(" ", ""));

                                        if (option >= 0 && option <= 4) {
                                            switch (option) {
                                                case 1 -> {
                                                    String info = updateName(id);
                                                    if (info != null) {
                                                        if (existedInfo == null) {
                                                            information = index + "--" + id + "--" + type + "--" + info + "--" + staff[2] + "--" + detail[0] + "--" + detail[1];
                                                        } else {
                                                            information = index + "--" + id + "--" + type + "--" + info + "--" + existedInfo[4] + "--" + existedInfo[5] + "--" + existedInfo[6];
                                                        }
                                                    }
                                                }
                                                case 2 -> {
                                                    String info = updateAddress(id);
                                                    if (info != null) {
                                                        if (existedInfo == null) {
                                                            information = index + "--" + id + "--" + type + "--" + staff[1] + "--" + info + "--" + detail[0] + "--" + detail[1];
                                                        } else {
                                                            information = index + "--" + id + "--" + type + "--" + existedInfo[3] + "--" + info + "--" + existedInfo[5] + "--" + existedInfo[6];
                                                        }
                                                    }
                                                }
                                                case 3 -> {
                                                    String info = updateMoney(id, "salary");
                                                    if (info != null) {
                                                        if (existedInfo == null) {
                                                            information = index + "--" + id + "--" + type + "--" + staff[1] + "--" + staff[2] + "--" + info + "--" + detail[1];
                                                        } else {
                                                            information = index + "--" + id + "--" + type + "--" + existedInfo[3] + "--" + existedInfo[4] + "--" + info + "--" + existedInfo[6];
                                                        }
                                                    }
                                                }
                                                case 4 -> {
                                                    String info = updateMoney(id, "bonus");
                                                    if (info != null) {
                                                        if (existedInfo == null) {
                                                            information = index + "--" + id + "--" + type + "--" + staff[1] + "--" + staff[2] + "--" + detail[0] + "--" + info;
                                                        } else {
                                                            information = index + "--" + id + "--" + type + "--" + existedInfo[3] + "--" + existedInfo[4] + "--" + existedInfo[5] + "--" + info;
                                                        }
                                                    }
                                                }
                                                default -> {
                                                    return information;
                                                }
                                            }
                                        } else {
                                            System.out.println(red + "Invalid option! Please choose a valid option (0-4)!" + reset);
                                        }
                                    } catch (NumberFormatException e) {
                                        System.out.println(red + "Invalid option! Please choose a valid option (0-4)!" + reset);
                                    }
                                }
                            } else {
                                information = null;
                                while (true) {
                                    Table tbHour = new Table(7, BorderStyle.UNICODE_ROUND_BOX_WIDE, ShownBorders.ALL);
                                    tbHour.addCell("Type", alignCenter);
                                    tbHour.addCell("ID", alignCenter);
                                    tbHour.addCell("Name", alignCenter);
                                    tbHour.addCell("Address", alignCenter);
                                    tbHour.addCell("Hour", alignCenter);
                                    tbHour.addCell("Rate", alignCenter);
                                    tbHour.addCell("Pay", alignCenter);

                                    String[] existedInfo = null;

                                    if (information == null) {
                                        double pays = Integer.parseInt(detail[0]) * Double.parseDouble(detail[1].replaceAll("\\$", ""));
                                        tbHour.addCell(type, alignCenter);
                                        tbHour.addCell("" + id, alignCenter);
                                        tbHour.addCell(staff[1], alignCenter);
                                        tbHour.addCell(staff[2], alignCenter);
                                        tbHour.addCell(detail[0], alignCenter);
                                        tbHour.addCell(detail[1], alignCenter);
                                        tbHour.addCell("$" + pays, alignCenter);
                                    } else {
                                        existedInfo = information.split("--");
                                        double pays = Integer.parseInt(existedInfo[5]) * Double.parseDouble(existedInfo[6].replaceAll("\\$", ""));
                                        tbHour.addCell(existedInfo[2], alignCenter);
                                        tbHour.addCell(existedInfo[1], alignCenter);
                                        tbHour.addCell(existedInfo[3], alignCenter);
                                        tbHour.addCell(existedInfo[4], alignCenter);
                                        tbHour.addCell(existedInfo[5], alignCenter);
                                        tbHour.addCell("$" + Double.parseDouble(existedInfo[6].replaceAll("\\$", "")), alignCenter);
                                        tbHour.addCell("$" + pays, alignCenter);
                                    }

                                    System.out.println(tbHour.render());

                                    try {
                                        System.out.println("1. Name \t2. Address \t3. Hour \t4. Rate \t0. Cancel");
                                        System.out.print("Choose one column to update: ");
                                        int option = Integer.parseInt(new Scanner(System.in).nextLine().replaceAll(" ", ""));

                                        if (option >= 0 && option <= 4) {
                                            switch (option) {
                                                case 1 -> {
                                                    String info = updateName(id);
                                                    if (info != null) {
                                                        if (existedInfo == null) {
                                                            information = index + "--" + id + "--" + type + "--" + info + "--" + staff[2] + "--" + detail[0] + "--" + detail[1];
                                                        } else {
                                                            information = index + "--" + id + "--" + type + "--" + info + "--" + existedInfo[4] + "--" + existedInfo[5] + "--" + existedInfo[6];
                                                        }
                                                    }
                                                }
                                                case 2 -> {
                                                    String info = updateAddress(id);
                                                    if (info != null) {
                                                        if (existedInfo == null) {
                                                            information = index + "--" + id + "--" + type + "--" + staff[1] + "--" + info + "--" + detail[0] + "--" + detail[1];
                                                        } else {
                                                            information = index + "--" + id + "--" + type + "--" + existedInfo[3] + "--" + info + "--" + existedInfo[5] + "--" + existedInfo[6];
                                                        }
                                                    }
                                                }
                                                case 3 -> {
                                                    String info = updateHour(id);
                                                    if (info != null) {
                                                        if (existedInfo == null) {
                                                            information = index + "--" + id + "--" + type + "--" + staff[1] + "--" + staff[2] + "--" + info + "--" + detail[1];
                                                        } else {
                                                            information = index + "--" + id + "--" + type + "--" + existedInfo[3] + "--" + existedInfo[4] + "--" + info + "--" + existedInfo[6];
                                                        }
                                                    }
                                                }
                                                case 4 -> {
                                                    String info = updateMoney(id, "rate");
                                                    if (info != null) {
                                                        if (existedInfo == null) {
                                                            information = index + "--" + id + "--" + type + "--" + staff[1] + "--" + staff[2] + "--" + detail[0] + "--" + info;
                                                        } else {
                                                            information = index + "--" + id + "--" + type + "--" + existedInfo[3] + "--" + existedInfo[4] + "--" + existedInfo[5] + "--" + info;
                                                        }
                                                    }
                                                }
                                                default -> {
                                                    return information;
                                                }
                                            }
                                        } else {
                                            System.out.println(red + "Invalid option! Please choose a valid option (0-4)!" + reset);
                                        }
                                    } catch (NumberFormatException e) {
                                        System.out.println(red + "Invalid option! Please choose a valid option (0-4)!" + reset);
                                    }
                                }
                            }
                        }
                        index++;
                    }

                    System.out.println(red + "ID " + id +" not found!" + reset);
                    break;
                } catch (NumberFormatException e) {
                    System.out.println(red + "Invalid input! ID is allowed only number!" + reset);
                }
            }
        } else {
            System.out.println(red + "No data to update! Please insert first!" + reset);
        }

        return information;
    }

    private static void removeEmployee(ArrayList<HashMap<String, Object>> dataList) {
        if(!dataList.isEmpty()) {
            while(true) {
                System.out.println("\n" + "=".repeat(20) + cyan + " REMOVE EMPLOYEE " + reset + "=".repeat(20));
                System.out.print("=> Enter ID to remove: ");
                try {
                    int id = Integer.parseInt(new Scanner(System.in).nextLine().replaceAll(" ", ""));
                    int index = 0, notFound = 0;
                    for (HashMap<String, Object> map : dataList) {
                        String[] staff = map.get("staff").toString().split("--");
                        int staffId = Integer.parseInt(staff[0]);
                        if (staffId == id) {
                            dataList.remove(index);
                            notFound++;
                            System.out.println(green + "✅ ID " + id + " is removed successfully!" + reset);
                            break;
                        }
                        index++;
                    }

                    if (notFound == 0) System.out.println(red + "ID " + id +" not found!" + reset);
                    break;
                } catch (NumberFormatException e) {
                    System.out.println(red + "Invalid input! ID is allowed only number!" + reset);
                }
            }
        } else {
            System.out.println(red + "No data to remove! Please insert first!" + reset);
        }
    }

    private static String updateName (int id) {
        boolean isName = true;
        String info = null;
        while(isName) {
            System.out.print("=> Change name to : ");
            String name = new Scanner(System.in).nextLine();
            if (Pattern.matches("^[a-zA-Z\\s]+$", name) && !name.isBlank()) {
                info = name;
                System.out.println(green + "✅ ID " + id + " is updated successfully!\n" + reset);
                isName = false;
            } else {
                System.out.println(red + "Invalid input! Name is allowed only letter!" + reset);
            }
        }
        return info;
    }

    private static String updateAddress (int id) {
        String info = null;
        boolean isAddress = true;
        while(isAddress) {
            System.out.print("=> Change address to : ");
            String address = new Scanner(System.in).nextLine();
            if (!address.isBlank()) {
                info = address;
                System.out.println(green + "✅ ID " + id + " is updated successfully!\n" + reset);
                isAddress = false;
            } else {
                System.out.println(red + "Invalid input! Please input properly!" + reset);
            }
        }
        return info;
    }

    private static String updateMoney (int id, String title) {
        String info = null;
        boolean isSalary = true;
        while(isSalary) {
            System.out.print("=> Change " + title + " to : ");
            String salary = new Scanner(System.in).nextLine();
            if(isMoney(salary)) {
                if(salary.length() <= 20) {
                    info = salary;
                    System.out.println(green + "✅ ID " + id + " is updated successfully!\n" + reset);
                    isSalary = false;
                } else {
                    System.out.println(red + "Value cannot be greater than 20 characters!" + reset);
                }
            } else {
                System.out.println(red + "Input invalid! Please input properly!" + reset);
            }
        }
        return info;
    }

    private static String updateHour (int id) {
        String info = null;
        boolean isSalary = true;
        while(isSalary) {
            System.out.print("=> Change hour to : ");
            String salary = new Scanner(System.in).nextLine();
            if(isNumber(salary)) {
                if(salary.length() <= 20) {
                    info = salary;
                    System.out.println(green + "✅ ID " + id + " is updated successfully!\n" + reset);
                    isSalary = false;
                } else {
                    System.out.println(red + "Value cannot be greater than 20 characters!" + reset);
                }
            } else {
                System.out.println(red + "Input invalid! Please input properly!" + reset);
            }
        }
        return info;
    }

    private static boolean isNumber(String number) {
        return Pattern.matches("^[0-9]+$", number);
    }

    private static boolean isMoney(String money) {
        return Pattern.matches("^[0-9]+$", money) || Pattern.matches("^([0-9]+).([0-9])+$", money);
    }
}