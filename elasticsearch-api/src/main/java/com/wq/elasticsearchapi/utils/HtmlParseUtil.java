package com.wq.elasticsearchapi.utils;

import com.wq.elasticsearchapi.pojo.Goods;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Component
public class HtmlParseUtil {

    public List<Goods> prase(String keyword) throws Exception {
        String url = "https://search.jd.com/Search?keyword="+keyword+"&enc=utf-8";
        Document document = Jsoup.parse(new URL(url), 3000);
        Element goodsList = document.getElementById("J_goodsList");
        Elements elements = goodsList.getElementsByTag("li");
        List<Goods> list = new ArrayList<>();
        for (Element element:elements){
            String image = element.getElementsByTag("img").eq(0).attr("src");
            // 为空跳出本次循环
            if (image==null||image.length()==0){
                continue;
            }
            Goods goods = new Goods();
            String prive = element.getElementsByClass("p-price").eq(0).text();
            String title = element.getElementsByClass("p-name").text();
            goods.setImage(image);
            goods.setPrice(prive);
            goods.setTitle(title);
            list.add(goods);
        }
        return list;
    }
}
