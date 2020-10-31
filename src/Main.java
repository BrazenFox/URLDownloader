import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        String url = null;
        String path = null;
        boolean open = false;
        switch (args.length) {
            case 1:
                url = args[0];
                run(url, path, open);
                break;
            case 2:
                url = args[0];
                if (args[1].equals("-o"))
                    open = true;
                else
                    path = args[1];
                run(url, path, open);
                break;
            case 3:
                url = args[0];
                path = args[1];
                if (args[2].equals("-o"))
                    open = true;
                run(url, path, open);
                break;
            default:
                //javac -d out ./src/*
                //java -classpath ./out Main
                //https://www.goodfon.ru/ C://Users/DmitryMarokhonov/Desktop/test -o
                run("https://sb.edu-netcracker.com/user/default.xhtml", "src", open);
                //run("https://stackoverflow.com/questions/24874404/java-regex-look-behind-group-does-not-have-obvious-maximum-length-error?lq=1",  "C://Users/DmitryMarokhonov/Desktop/test", open);
                //run("https://www.goodfon.ru/", "C://Users/DmitryMarokhonov/Desktop/test", open);
                System.out.println("некорректный ввод");
        }

    }
    public static void run(String url, String path, boolean open){
        URLFileDownloader downloader = new URLFileDownloader(url, path);

        if (downloader.getType().equals(".html")) {
            URLFileHtml urlfile = new URLFileHtml(downloader);
        }
        if (open)
            downloader.opening();
    }
}
