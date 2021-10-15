package in.koshurtech.zstmx.javaClasses;

public class uploadInfoViewHolder {

    private String tableGroup;
    private String tableData;

    public uploadInfoViewHolder(String tableGroup,String tableData){
        this.tableGroup = tableGroup;
        this.tableData = tableData;
    }


    public String getTableGroup() {
        return tableGroup;
    }

    public void setTableGroup(String tableGroup) {
        this.tableGroup = tableGroup;
    }

    public String getTableData() {
        return tableData;
    }

    public void setTableData(String tableData) {
        this.tableData = tableData;
    }
}
