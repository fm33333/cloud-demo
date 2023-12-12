package com.demo.feign.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.OpenAiApi;
import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import retrofit2.Retrofit;


import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.Duration;
import java.util.List;

import static com.theokanning.openai.service.OpenAiService.*;

/**
 * 测试接入openai的api
 * TODO：需要国外服务器
 */
@RestController
public class OpenAiController {

    /**
     * openai的个人密钥（推送到github后会自动失效，需要去官网重新创建）
     * https://platform.openai.com/account/api-keys
     */
    @Value("${openai.api-key}")
    private String apiKey;

    @GetMapping("/ai")
    public void test() {

        // 加代理
//        ObjectMapper mapper = defaultObjectMapper();
//        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 8080));
//        OkHttpClient httpClient = defaultClient(apiKey, Duration.ofSeconds(10000))
//                .newBuilder()
//                .proxy(proxy)
//                .build();
//        Retrofit retrofit = defaultRetrofit(httpClient, mapper);
//        OpenAiApi api = retrofit.create(OpenAiApi.class);

        OpenAiService openAiService = new OpenAiService(apiKey, Duration.ofSeconds(10000));
//        OpenAiService openAiService = new OpenAiService(api);
        CompletionRequest completionRequest = CompletionRequest.builder()
                .model("text-davinci-003")
                .prompt("请告诉我你是谁")
                .temperature(0.8)
                .maxTokens(2048)
                .topP(1D)
                .build();
        List<CompletionChoice> choices = openAiService.createCompletion(completionRequest).getChoices();
        System.out.println(choices);
        System.out.println(openAiService.listModels());
        System.out.println(openAiService.getModel("text-davinci-003"));
    }

//    public static void main(String[] args){
//        showModels();
//    }
//
//    /**
//     * 显示模型
//     * 根据不同的功能和语言选择合适的模型，可以在官网的模型概述中查看
//     * https://platform.openai.com/docs/models/overview
//     */
//    public static void showModels(){
//        //列出所有模型实例
//        System.out.println(service.listModels());
//        //检索模型,得到模型实例，提供有关模型的基本信息，例如所有者和权限，应用场景等。
//        System.out.println(service.getModel("text-davinci-003"));
//    }

}
