package in.koshurtech.zstmx.javaClasses;

public class resultView {
    private String head;
    private String body;
    private boolean type;


    public resultView(String head, String body, boolean type){
        this.head = head;
        this.body = body;
        this.type = type;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
