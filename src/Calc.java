import java.util.HashMap;
import java.util.Scanner;

public class Calc {
    public static void main(String[] args) {
        System.out.println("Введите выражение (a + b, a - b, a * b, a / b):");
        String signs = new String(new char[]{'+', '-', '*', '/'});
        String signsRome = new String(new char[]{'M', 'D', 'C', 'L', 'X', 'V', 'I'});
        HashMap<String, Integer> romeDict = new HashMap<>();
        romeDict.put("I", 1);
        romeDict.put("V", 5);
        romeDict.put("X", 10);
        romeDict.put("L", 50);
        romeDict.put("C", 100);
        romeDict.put("D", 500);
        romeDict.put("M", 1000);
        Scanner in = new Scanner(System.in); //создаем сканер in
        String x = "";//создаем новую строку x(будет введена)
        x = in.nextLine();//"сканируем" строку и сохраняем в x
        if(checkRome(x, signsRome)){
            System.out.println(strRome(romeNum(x, signs, romeDict, signsRome), romeDict, signsRome));
        } else {
            System.out.println(arabNum(x, signs, signsRome));
        }
        //System.out.println(strRome(romeNum(x, signs, romeDict, signsRome), romeDict, signsRome));
    }
    static boolean checkRome(String a, String b){
        int k=0;
        for(int i=0; i<7; i++) {
            if (a.charAt(0) == b.charAt(i)) {
                return true;
            }
        }
        return false;
    }
    static int arabNum(String str, String strOp, String signsRome){//
        int z1=0;
        int z2=0;
        char[] a = str.toCharArray();
        char[] b = strOp.toCharArray();
        int n = findX(a, b);
        String splitX1 = splitX(0, n-1, str, signsRome);//слева от знака операции
        String splitX2 = splitX(n+2, str.length(), str, signsRome);//справа от знака
        z1 = Integer.parseInt(splitX1);
        z2 = Integer.parseInt(splitX2);
        return resultOp(z1, z2, str, n);
    }
    static String splitX(int nStart, int nEnd, String str, String b){
        String c = "";
        char[] ch = str.toCharArray();
        for(int i=nStart; i<nEnd; i++){
            c += ch[i];
        }
        if(!checkRome(c, b)){
            try {
                Integer.parseInt(c);
            } catch (NumberFormatException ex) {
                System.out.println(ex.getMessage());
                System.exit(0);
            }
        }
        return c;
    }
    static int findX(char[] str, char[] strOp){//находим индекс (+,-,*,/)
        int numX=0;
        int z=0;
        for(int i=0; i<str.length; i++){
            for (char c : strOp) {
                if (str[i] == c) {
                    z++;
                    numX = i;
                    //z - количество повторов, должна быть ровно одна операция
                    try {
                        if (z > 1) {
                            throw new Exception("Не верная команда!z>1");
                        }
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                        System.exit(0);
                    }
                }
            }
        }
        return numX;
    }
    static int resultOp(int z1, int z2, String str, int n){
        int result=0;
        String s=Character.toString(str.charAt(n));//s=операция(+,-,*,/)
        switch (s) {
            case "+" -> result = z1 + z2;
            case "-" -> result = z1 - z2;
            case "*" -> result = z1 * z2;
            case "/" -> result = z1 / z2;
        }
        return result;
    }
    static int romeNum(String str, String strOp, HashMap<String, Integer> x, String romeSigns){//найденую операцию +,-,*,/ выполняем с римскими цифрами
        char[] a = str.toCharArray();
        char[] b = strOp.toCharArray();
        int n = findX(a, b);
        String splitX1 = splitX(0, n-1, str, romeSigns);//слева от знака операции
        String splitX2 = splitX(n+2, str.length(), str, romeSigns);//справа от знака
        int z1 = romeToArabNum(splitX1, x);
        int z2 = romeToArabNum(splitX2, x);
        /*
        try {
            if(z1>10 | z2>10 | z1<0 | z2<0){//по условиям задачи не больше 10, и положительно
                throw new Exception("Не верная команда!z1>10,z2>10,Rome");
            }
        }catch(Exception ex) {
            System.out.println(ex.getMessage());
            System.exit(0);
        }
        */
        return resultOp(z1, z2, str, n);
    }
    static int romeToArabNum(String str, HashMap<String, Integer> x){
        char[] romeStrArr = str.toCharArray();
        int sum=0;
        int t0=0;
        int t1=0;
        int k=1;
        for(int i=0; i<str.length(); i++){//проверяем строку на римские цифры
            String s=Character.toString(romeStrArr[i]);
            if(x.containsKey(s)){
                t0 = x.get(s);
                if(t0 > t1){
                    sum += t0 - 2*t1;
                    t1 = t0;
                } else if(t0 == t1) {
                    sum += t0;
                    k++;//не больше 3 одинаковых подряд
                } else {
                    sum += t0;
                    t1 = t0;
                }
            }
        }
        return sum;
    }
    static String strRome(int z, HashMap<String, Integer> x, String signsRome){
        char[] sRome = signsRome.toCharArray();
        StringBuilder q = new StringBuilder();
        int k=0;
        for(int f=0; f<7; f++){
            int n=0;
            int ds = x.get(String.valueOf(sRome[f]));
            n = z / ds; //
            if (n == 1) {
                q.append(sRome[f]);
                k++;
                z -= x.get(String.valueOf(sRome[f]));
            } else if (n > 3 & f > 0) {
                z -= x.get(String.valueOf(sRome[f-1]));
                z += x.get(String.valueOf(sRome[f]));
                if (!q.isEmpty()) {
                    q.replace(k-1, q.length(), Character.toString(sRome[f]));
                    q.append(sRome[f-2]);
                    k++;
                } else {
                    q.append(sRome[f]);
                    q.append(sRome[f-1]);
                    k+=2;
                }
            } else if (n == 3 | n == 2){
                for(int i=0; i<n; i++){
                    q.append(sRome[f]);
                    k++;
                    z -= x.get(String.valueOf(sRome[f]));
                }
            }
        }
        return q.toString();
    }
}