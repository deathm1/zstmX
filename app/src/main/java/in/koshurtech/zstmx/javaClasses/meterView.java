package in.koshurtech.zstmx.javaClasses;

public class meterView {

    private int meterProgress;
    private String meterText;
    private String cardMessage;

    public meterView(int meterProgress,
            String meterText,
            String cardMessage){
        this.meterProgress = meterProgress;
        this.meterText = meterText;
        this.cardMessage = cardMessage;
    }


    public int getMeterProgress() {
        return meterProgress;
    }

    public void setMeterProgress(int meterProgress) {
        this.meterProgress = meterProgress;
    }

    public String getMeterText() {
        return meterText;
    }

    public void setMeterText(String meterText) {
        this.meterText = meterText;
    }

    public String getCardMessage() {
        return cardMessage;
    }

    public void setCardMessage(String cardMessage) {
        this.cardMessage = cardMessage;
    }
}
