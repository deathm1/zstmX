package in.koshurtech.zstmx.javaClasses;

public class recyclerInfoView {
    private String infoHeader;
    private String infoBody;

    public recyclerInfoView(String infoBody, String infoHeader){
        this.infoBody = infoBody;
        this.infoHeader = infoHeader;
    }

    public String getInfoHeader() {
        return infoHeader;
    }

    public void setInfoHeader(String infoHeader) {
        this.infoHeader = infoHeader;
    }

    public String getInfoBody() {
        return infoBody;
    }

    public void setInfoBody(String infoBody) {
        this.infoBody = infoBody;
    }
}
