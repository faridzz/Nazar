package org.example.nazar.service.scraper.siteslogics.mobile;

import lombok.extern.slf4j.Slf4j;
import org.example.nazar.dto.MobileDTO;
import org.example.nazar.exception.NotFoundException; // فرض بر وجود این کلاس استثنا
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class GetAllProductOfABrand {

//    public static void main(String[] args) {
//        GetAllProductOfABrand scraper = new GetAllProductOfABrand();
//        try {
//            List<MobileDTO> mobileDTOs = scraper.getAllProductNamesAndUrl("https://www.mobile.ir/brands/117-xiaomi.aspx");
//            log.info("فهرست محصولات برند شیائومی استخراج شد: {}", mobileDTOs);
//        } catch (NotFoundException e) {
//            log.error("خطا در هنگام استخراج اطلاعات محصولات برند شیائومی: {}", e.getMessage());
//        }
//    }

    /**
     * این متد لیستی از اشیاء MobileDTO را با اطلاعات نام و لینک تمام محصولات یک برند از یک آدرس اینترنتی خاص برمی‌گرداند.
     *
     * @param siteUrl آدرس اینترنتی صفحه محصولات برند
     * @return لیستی از اشیاء MobileDTO حاوی نام و لینک محصولات
     * @throws NotFoundException در صورت بروز خطا در فرایند استخراج اطلاعات
     */
    private List<MobileDTO> getAllProductNamesAndUrl(String siteUrl) throws NotFoundException {
        Document doc;
        List<MobileDTO> mobileDTOList = new ArrayList<>();

        try {
            // برقراری ارتباط با سایت و دریافت سند HTML
            doc = Jsoup.connect(siteUrl).get();
        } catch (IOException e) {
            // مدیریت خطای برقراری ارتباط با سایت
            throw new NotFoundException("خطا در برقراری ارتباط با سایت: " + e.getMessage());
        }

        // انتخاب عنصر حاوی لیست محصولات برند
        Element productList = doc.selectFirst(".brandmodels");
        if (productList == null) {
            // در صورت عدم وجود عنصر مورد نظر، خطا پرتاب می‌شود
            throw new NotFoundException("عنصر حاوی لیست محصولات یافت نشد.");
        }

        // انتخاب تمام تگ‌های a درون عنصر لیست محصولات
        Elements aTags = productList.getElementsByTag("a");

        for (Element aTag : aTags) {
            String productName = aTag.ownText().trim(); // حذف فاصله‌های اضافی از نام محصول
            String url = aTag.attr("href");

            // بررسی وجود مقادیر معتبر برای نام و لینک محصول
            if (url != null && !url.isEmpty() && !productName.isEmpty()) {
                MobileDTO mobileDTO = new MobileDTO();
                mobileDTO.setUrl(url);
                mobileDTO.setTitle(productName);
                mobileDTOList.add(mobileDTO);
            }
        }

        return mobileDTOList;
    }
}
