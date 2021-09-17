package in.koshurtech.zstmx.javaClasses;

public class sensorInfoView {
    private String header;
    private String body;
    private boolean isClickable;

    public boolean isClickable() {
        return isClickable;
    }

    public sensorInfoView(String header, String body, boolean isClickable){
        this.header = header;
        this.body = body;
        this.isClickable = isClickable;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
