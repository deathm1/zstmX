package in.koshurtech.zstmx.javaClasses;

public class deviceProfileView {

    private String deviceMake;
    private String deviceModel;
    private int upVotes;
    private int downVotes;
    private String profileId;


    public deviceProfileView(String deviceMake, String deviceModel, int upVotes,int downVotes, String profileId){
        this.deviceMake = deviceMake;
        this.deviceModel = deviceModel;
        this.upVotes = upVotes;
        this.downVotes = downVotes;
        this.profileId = profileId;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getDeviceMake() {
        return deviceMake;
    }

    public void setDeviceMake(String deviceMake) {
        this.deviceMake = deviceMake;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public int getUpVotes() {
        return upVotes;
    }

    public void setUpVotes(int upVotes) {
        this.upVotes = upVotes;
    }

    public int getDownVotes() {
        return downVotes;
    }

    public void setDownVotes(int downVotes) {
        this.downVotes = downVotes;
    }
}
