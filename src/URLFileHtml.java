import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLFileHtml {
    String path;
    String name;
    String type;
    String host;
    String charSet;
    ArrayList<String> htmlBuffer = new ArrayList<>();

    public URLFileHtml(URLFileDownloader file) {
        this.path = file.getPath();
        this.name = file.getName();
        //System.out.println(this.name);
        this.type = file.getType();
        this.host = file.getHost();
        this.charSet = findCharset();
        this.createDirectory();
        this.downloaderFiles();
    }

    public void createDirectory() {
        File directory = new File(this.path + "/" + this.name + "_files");
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

    public String findCharset() {
        try {
            String charset = null;
            File file = new File(this.path + "/" + this.name + this.type);
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            while ((line = in.readLine()) != null) {
                if (line.contains("charset=")) {
                    charset = line.split("charset=")[1].split("\"")[0];
                    if (charset.equals("")) charset = line.split("charset=")[1].split("\"")[1];
                    break;
                }
            }
            if (charset == null)
                charset = "utf-8";
            return charset;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void downloaderFiles() {
        //Pattern patternImgOrLink = Pattern.compile("<img src=\"([0-9A-za-z-:.,/_'=]+)\".+?>|<link rel=.+href=\"([0-9A-za-z-.,/'=]+).+>");//[0-9A-za-z-:.,/_'=?]+
        //Pattern patternImgOrLink = Pattern.compile("<img src=\"([0-9A-za-z-:;.,/_'=]+)\".+?|<link.+href=\"([0-9A-za-z-:;_.,/'=]+).+>");//[0-9A-za-z-:.,/_'=?]+
        Pattern patternImgOrLink = Pattern.compile("<img src=\"([0-9A-za-z-_.:;&,/'=]+).*\".+|<link.+href=\"([0-9A-za-z-:;_.,/'=]+).+>");
        File file = new File(this.path + "/" + this.name + this.type);
        try {
            FileReader fileReader = new FileReader(file);
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), this.charSet); //Windows-1251 utf-8
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher matcherImgOrLink = patternImgOrLink.matcher(line);
                if (matcherImgOrLink.find()) {
                    String imgOrLink = (matcherImgOrLink.group(1) == null ? matcherImgOrLink.group(2) : matcherImgOrLink.group(1));
                    System.out.println("link: " + this.downloadSupportFiles(this.urlValidation(imgOrLink)));
                    this.htmlBuffer.add(line.replace(imgOrLink, this.downloadSupportFiles(this.urlValidation(imgOrLink))) + "\r");
                } else {
                    this.htmlBuffer.add(line + "\r");
                }
            }
            this.changingPaths(file);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changingPaths(File file) throws IOException {
        Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), this.charSet));
        for (String lineHTML : this.htmlBuffer) {
            writer.write(lineHTML);
        }
        writer.close();
    }

    public String urlValidation(String url) {
        if (url.startsWith("//")) {
            return "https:" + url;
        }
        if (!url.contains(this.host) && !url.startsWith("//") && url.startsWith("/")) {
            return this.host + url;
        }
        return url;
    }

    public String downloadSupportFiles(String url) {
        String nameFile;
        String typeFile;
        String pathFile = this.path + "/" + this.name + "_files";
        url = url.split("\\?")[0];
        //System.out.println(this.name);
        if (url.matches("^(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})([/\\w .-]*)*/?$")) {
            if (url.split("/")[url.split("/").length - 1].contains(".")) {
                nameFile = url.split("/")[url.split("/").length - 1].split("\\.")[0];
                typeFile = "." + url.split("/")[url.split("/").length - 1].split("\\.")[1];
                String path = pathFile + "/" + nameFile + typeFile;
                if (this.downloader(path, url)) {
                    return this.name + "_files/" + nameFile + typeFile;
                } else return url;
            } else {
                nameFile = url.split("/")[url.split("/").length - 1];
                typeFile = ".jpg";
                String path = pathFile + "/" + nameFile + typeFile;
                if (this.downloader(path, url)) {
                    return this.name + "_files/" + nameFile + typeFile;
                } else return url;
            }
        } else return url;
    }

    public boolean downloader(String path, String url) {
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(path)) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
