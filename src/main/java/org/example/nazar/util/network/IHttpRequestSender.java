package org.example.nazar.util.network;

import java.io.IOException;

// اینترفیس برای ارسال درخواست
public interface IHttpRequestSender {
    String sendRequest(String url) throws IOException, InterruptedException;
}