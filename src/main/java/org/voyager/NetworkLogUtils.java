package org.voyager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v130.network.Network;
import org.openqa.selenium.devtools.v130.network.model.RequestWillBeSent;
import org.openqa.selenium.devtools.v130.network.model.ResourceType;
import org.openqa.selenium.devtools.v130.network.model.ResponseReceived;
import org.voyager.exception.NetworkLogException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

public class NetworkLogUtils {

    private final LinkedHashMap<String, NetworkLog> networkLogMap;
    private final DevTools devTools;
    List<ResourceType> requiredResourceTypes;

    public NetworkLogUtils(WebDriver driver) {
        this(driver, null);
    }

    public NetworkLogUtils(WebDriver driver, List<ResourceType> requiredResourceTypes) {
        this.requiredResourceTypes = requiredResourceTypes;
        if (this.requiredResourceTypes == null) {
            this.requiredResourceTypes = new ArrayList<>();
            this.requiredResourceTypes.add(ResourceType.XHR);
        }
        networkLogMap = new LinkedHashMap<>();
        this.devTools = ((HasDevTools)driver).getDevTools();
        this.devTools.createSession();
        this.devTools.send(Network.enable( Optional.empty(), Optional.empty(), Optional.empty()));
        devTools.addListener(Network.requestWillBeSent(), this::requestWillBeSentConsumer);
        devTools.addListener(Network.responseReceived(), this::responseReceivedConsumer);
    }

    public LinkedHashMap<String, NetworkLog> getNetworkLogMap() { return networkLogMap; }

    private boolean isResourceTypeAllowed(ResourceType type) {
        return requiredResourceTypes.stream()
                .anyMatch(requiredResourceTypes::contains);
    }

    private void requestWillBeSentConsumer(RequestWillBeSent requestWillBeSent) {
        try {
            if (!requestWillBeSent
                    .getType()
                    .map(this::isResourceTypeAllowed)
                    .orElse(false)) {
                return;
            }

            NetworkLog networkLog = new NetworkLog();
            networkLog.setApiUrl(requestWillBeSent.getRequest().getUrl());
            networkLog.setApiMethod(requestWillBeSent.getRequest().getMethod());
            networkLog.setRequestId(requestWillBeSent.getRequestId().toString());
            networkLog.setStartTimeStamp(requestWillBeSent.getTimestamp().toString());
            networkLog.setUiUrl(requestWillBeSent.getDocumentURL());
            networkLog.setCurrentTime(Instant.now().toEpochMilli());
            networkLog.setRequestHeader(requestWillBeSent.getRequest().getHeaders().toJson());
            if (requestWillBeSent.getRequest().getHasPostData().isPresent()) {
                //This PostData is deprecated. So, we need to use PostDataEntries in future
                String requestData = requestWillBeSent.getRequest()
                        .getPostData()
                        .orElse(null);
                networkLog.setPostData(requestData);
//                    List<String> postDataList = requestWillBeSent
//                            .getRequest()
//                            .getPostDataEntries()
//                            .orElse(new ArrayList<>())
//                            .stream()
//                            .map(PostDataEntry::getBytes)
//                            .flatMap(Optional::stream)
//                            .toList();
//                    networkLog.setPostData(postDataList.get(0));
            }
            networkLogMap.put(networkLog.getRequestId(), networkLog);
        } catch (Exception ex) {
            throw new NetworkLogException(ex.getMessage());
        }
    }

    private static <T> T nvl(T ifThisIsNull, T replaceThis) {
        return ifThisIsNull == null ? replaceThis : ifThisIsNull;
    }

    private void responseReceivedConsumer(ResponseReceived responseReceived) {
        try {
            if (!isResourceTypeAllowed(responseReceived
                    .getType())) {
                return;
            }
            if (networkLogMap == null) {
                return;
            }
            String requestId = responseReceived.getRequestId().toString();
            NetworkLog networkLog = networkLogMap.get(requestId);
            if (networkLog == null) {
                return;
            }
            networkLog.setApiStatus(responseReceived.getResponse().getStatus() + "");
            networkLog.setDownloadedData(networkLog.getDownloadedData());
            networkLog.setEndTimeStamp(responseReceived.getTimestamp().toString());
            networkLog.setTimeStampDiff(new BigDecimal(nvl(networkLog.getEndTimeStamp(), "0")).subtract(new BigDecimal(nvl(networkLog.getStartTimeStamp(), "0"))));
            networkLog.setResponseBody(devTools.send(Network.getResponseBody(responseReceived.getRequestId())).getBody());
            networkLog.setResponseHeader(responseReceived.getResponse().getHeaders().toJson());
        } catch (Exception ex) {
            //Can be ignored
        }
    }

    public void stopNetworkLog() {
        try {
            devTools.send(org.openqa.selenium.devtools.v131.network.Network.disable());
        } catch (Exception ex) {
            throw new NetworkLogException(ex.getMessage());
        }
    }
}