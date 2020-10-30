import java.awt.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class URLFileDownloader extends URLFile {
    String path;
    String name;
    String type;

    public URLFileDownloader(String url, String path) {
        super(url);
        this.pathMaker(path);
        this.checkDirectory();
        this.downloader(this.path + "/" + this.name + this.type, this.getUrl());

    }

    public void pathMaker(String path) {
        if (path != null && this.pathValidation(path)) {
            if (path.substring(path.lastIndexOf("/")).contains(".")) {
                this.name = path.substring(path.lastIndexOf("/") + 1).split("\\.")[0];
                this.type = "." + path.substring(path.lastIndexOf("/") + 1).split("\\.")[1];
                this.path = path.substring(0, path.lastIndexOf("/"));
            } else {
                this.name = super.getName();
                this.type = super.getType();
                this.path = path;
            }
        } else {
            this.name = super.getName();
            this.type = super.getType();
            this.path = super.getPath();
        }
    }

    public void checkDirectory() {
        File file = new File(this.path);
        this.checkFile(new File((file.exists() ? this.path : super.getPath()) + "/" + this.name + this.type));
    }

    public void checkFile(File file) {
        if (file.isFile()) {
            System.out.println("переименовать r или перезаписать o");
            Scanner sc = new Scanner(System.in);
            String answer = sc.next();
            while (!(answer.equals("o") || answer.equals("r"))) {
                System.out.println("некорректный ответ");
                answer = sc.next();
            }
            if (answer.equals("r")) {
                System.out.println("Введите имя файла");
                this.name = sc.next();
            }
        }
    }

    public boolean pathValidation(String contwinpath) {
        return contwinpath.matches("(^(([a-zA-Z]://)|^(src/)))(.+)");
    }

    public void downloader(String path, String url) {
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(path)) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void opening() {
        String path = this.path + "/" + this.name + this.type;
        Desktop desktop = null;
        if (Desktop.isDesktopSupported()) {
            desktop = Desktop.getDesktop();
        }
        try {
            assert desktop != null;
            desktop.open(new File(path));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getType() {
        return type;
    }
}
