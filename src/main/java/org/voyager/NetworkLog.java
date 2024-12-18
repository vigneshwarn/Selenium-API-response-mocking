package org.voyager;

import java.math.BigDecimal;
import java.util.Map;

public class NetworkLog {
    private String apiUrl;
    private String uiUrl;
    private String postData;
    private String apiMethod;
    private String apiStatus;
    private String type;
    private String requestId;
    private String responseBody;
    private Map<String,Object> requestHeader;
    private Map<String,Object> responseHeader;
    private Long currentTime;

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getUiUrl() {
        return uiUrl;
    }

    public void setUiUrl(String uiUrl) {
        this.uiUrl = uiUrl;
    }

    public String getPostData() {
        return postData;
    }

    public void setPostData(String postData) {
        this.postData = postData;
    }

    public String getApiMethod() {
        return apiMethod;
    }

    public void setApiMethod(String apiMethod) {
        this.apiMethod = apiMethod;
    }

    public String getApiStatus() {
        return apiStatus;
    }

    public void setApiStatus(String apiStatus) {
        this.apiStatus = apiStatus;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public Map<String, Object> getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(Map<String, Object> requestHeader) {
        this.requestHeader = requestHeader;
    }

    public Map<String, Object> getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(Map<String, Object> responseHeader) {
        this.responseHeader = responseHeader;
    }

    public Long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Long currentTime) {
        this.currentTime = currentTime;
    }

    public String getStartTimeStamp() {
        return startTimeStamp;
    }

    public void setStartTimeStamp(String startTimeStamp) {
        this.startTimeStamp = startTimeStamp;
    }

    public String getEndTimeStamp() {
        return endTimeStamp;
    }

    public void setEndTimeStamp(String endTimeStamp) {
        this.endTimeStamp = endTimeStamp;
    }

    public BigDecimal getTimeStampDiff() {
        return timeStampDiff;
    }

    public void setTimeStampDiff(BigDecimal timeStampDiff) {
        this.timeStampDiff = timeStampDiff;
    }

    private String startTimeStamp;
    private String endTimeStamp;
    private BigDecimal timeStampDiff;

    public Double getDownloadedData() {
        return DownloadedData;
    }

    public void setDownloadedData(Double downloadedData) {
        DownloadedData = downloadedData;
    }

    private Double DownloadedData;
}