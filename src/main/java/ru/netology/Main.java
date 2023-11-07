package ru.netology;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static ObjectMapper mapper = new ObjectMapper();
    public static void main(String[] args) {
        CloseableHttpClient client = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();
        HttpGet request = new HttpGet("https://api.nasa.gov/planetary/apod?api_key=8LeKBwaAEWTUDDqYUOIGK5pecxHevEQDEIaiHWXc");
        try {
            CloseableHttpResponse response = client.execute(request);
            NasaResp nasaResp = mapper.readValue(response.getEntity().getContent(), NasaResp.class);
            System.out.println(nasaResp.getUrl());
            response = client.execute(new HttpGet(nasaResp.getUrl()));
            String[] strings = nasaResp.getUrl().split("/");
            File file = new File(strings[strings.length-1]);
            FileOutputStream fw = new FileOutputStream(file);
            HttpEntity entity = response.getEntity();
            entity.writeTo(fw);
            fw.close();

        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}