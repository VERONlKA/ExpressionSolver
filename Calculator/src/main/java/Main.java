import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        int i = 0;
        int id = 1;
        String idexp = "";
        String exp = "";
        String res = "";
        ResultSet rs = null;

        Scanner scan = new Scanner(System.in);
        do {
            System.out.println("1. Input expression\n2. Show history\n3. Edit expression\n4. Find expression by result"); // меню
            i = scan.nextInt();
            scan.nextLine();
            try {
               Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc", "root", "toor#81");
                Statement statement = connection.createStatement();
            switch (i) {
                case 1:
                    System.out.println("Input expression");
                    exp = scan.nextLine(); // сканування виразу
                    res = String.valueOf(Conventer.EvalEqn(exp));
                    String query = ("SELECT * FROM expression ORDER BY id DESC LIMIT 1;"); // запит останнього id з бази даних
                    rs = statement.executeQuery(query);
                    if (rs.next()) {
                        id = rs.getInt("id");
                        id++;} // створення нового айді для нового прикладу
                    statement.executeUpdate("insert into expression" + "(id, expression, result)" + "values('" + id + "','" + exp + "', '" + res + "')"); // запис айді, виразу і результату
                    break;
                case 2:
                    System.out.println("History of calculation");
                    rs = statement.executeQuery("select * from expression");
                    while (rs.next()) { // вивід історії калькулятора з бд
                        System.out.println(rs.getString("id")+" "+rs.getString("expression") + " = " + rs.getString("result"));
                    }
                    break;
                case 3:
                    System.out.println("Input id of expression"); // пошук виразу, який ми хочемо змінити по айді
                    idexp = scan.nextLine();
                    query = ("SELECT * FROM expression HAVING id = '" + idexp + "' ;");
                    rs = statement.executeQuery(query);
                    if (rs.next()) {
                        String output = rs.getString("expression");
                        System.out.println(output);
                    }
                    System.out.println("Input edited expression"); // редагування виразу та перезапис його у дб
                    exp = scan.nextLine();
                    res = String.valueOf(Conventer.EvalEqn(exp));
                    query = ("update expression set expression = '"+exp+"', result = '"+res+"' where id = '"+idexp+"';");
                    statement.executeUpdate(query);
                    System.out.println("Record Updated!");
                    break;
                case 4:
                    int j = 0;
                           do {
                               System.out.println("Input number for search"); // пошук по результатам
                               String str = scan.nextLine();
                               double d = Double.parseDouble(str);
                               res = Double.toString(d);
                               System.out.println("Choose operation:\n5. Search equal\n6. Search bigger\n7. Search less\n8. Back to menu");
                               j = scan.nextInt();
                               switch (j){
                               case 5:
                                   query = ("SELECT * FROM expression HAVING result = '" + res + "';"); // вивід виразів результат який рівний заданому
                                   rs = statement.executeQuery(query);
                                   while (rs.next()) {
                                       System.out.println(rs.getString("expression"));
                                   }
                                   break;
                                   case 6:
                               query = ("SELECT * FROM expression GROUP BY expression HAVING result > '" + res + "';"); // вивід виразів результат який більший заданому
                               rs = statement.executeQuery(query);
                               while (rs.next()) {
                                   System.out.println(rs.getString("expression"));
                               }
                               break;
                                   case 7:
                                       query = ("SELECT * FROM expression GROUP BY expression HAVING result < '" + res + "';");//вивід виразів результат який менший заданому
                                       rs = statement.executeQuery(query);
                                       while (rs.next()) {
                                           System.out.println(rs.getString("expression"));
                                       }
                               break;
                                   default:
                               break;}

                           }while(j<8);

            } } catch (Exception e) {
                        e.printStackTrace();
            }
        } while (i<5);
    }
}
