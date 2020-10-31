import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLFile {
    String name;
    String path;
    String type;
    String url;
    String host;

    public URLFile(String url) {
        this.nameMaker(url);
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public String getHost() {
        return host;
    }

    public void nameMaker(String url) {
        if (this.urlValidation(url)) {
            if (url.split("/").length > 3) {
                String type;
                String name = url.split("\\?")[0];
                name = name.split("/")[name.split("/").length - 1];

                if (name.split("\\.").length == 2) {
                    type = "." + name.split("\\.")[1];
                    name = name.split("\\.")[0];
                } else
                    type = ".html";
                this.name = name;
                this.type = type;
            }
            else {
                this.name = "index";
                this.type = ".html";
            }
            this.host = url.split("/")[0] + "/" + url.split("/")[1] + "/" + url.split("/")[2];
            this.path = "src";
            this.url = url;
        }else
            throw new IllegalArgumentException();
    }

    public boolean urlValidation(String url) {
        //Pattern patternURL = Pattern.compile("^(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})([/\\w .-]*)*/?$", Pattern.DOTALL);
        //Matcher matcherURL = patternURL.matcher(url);
        //return matcherURL.find();
        url = url.split("\\?")[0];
        return url.matches("^(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})([/\\w .-]*)*/?$");
    }

}
