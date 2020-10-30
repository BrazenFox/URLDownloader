import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String url = null;
        String path = null;
        boolean open = false;

        System.out.println("Введите аргументы через пробелы: 1 url(https://www.goodfon.ru/page), 2 path(C://.../...), 3 open(-o)");
        Scanner sc = new Scanner(System.in);
        String arguments = sc.next();
        arguments = arguments.replaceAll("\\s+", " ");

        if (arguments.split(" ").length == 3) {
            url = arguments.split(" ")[0];
            path = arguments.split(" ")[1];
            if (arguments.split(" ")[2].equals("-o"))
                open = true;
        } else if (arguments.split(" ").length == 2) {
            url = arguments.split(" ")[0];
            if (arguments.split(" ")[1].equals("-o"))
                open = true;
            else
                path = arguments.split(" ")[1];
        } else if (arguments.split(" ").length == 1)
            url = arguments.split(" ")[0];
        else
            System.out.println("некорректные данные");

        //String url = "https://www.goodfon.ru/";
        //String path = "src/goodfon.html";
        //String path = null;
        URLFileDownloader downloader = new URLFileDownloader(url, path);
        if (downloader.getType().equals(".html")) {
            URLFileHtml urlfile = new URLFileHtml(downloader);
        }
        if (open)
            downloader.opening();
    }
}
