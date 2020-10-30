import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String url = null;
        String path = null;
        boolean open = false;

        System.out.println("Введите url:");
        Scanner sc = new Scanner(System.in);
        url = sc.next();
        System.out.println("Введите path или non:");
        String p = sc.next();
        if (!p.equals("non"))
            path = p;
        System.out.println("Введите open (-o) или non:");
        String o = sc.next();
        if (o.equals("-o"))
            open = true;



        /*String url = "https://www.goodfon.ru/";
        String path = "C://Users/DmitryMarokhonov/IdeaProjects/goodfon.html";
        boolean open = true;*/
        URLFileDownloader downloader = new URLFileDownloader(url, path);
        if (downloader.getType().equals(".html")) {
            URLFileHtml urlfile = new URLFileHtml(downloader);
        }
        if (open)
            downloader.opening();
    }
}
