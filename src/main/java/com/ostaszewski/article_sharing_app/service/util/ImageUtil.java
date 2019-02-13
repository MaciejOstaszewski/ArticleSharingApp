package com.ostaszewski.article_sharing_app.service.util;

import com.ostaszewski.article_sharing_app.web.rest.UploadController;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageUtil {
    public static String prepareImage(String image) {
        if (image.length() == 0) {
            image = "avatar.png";
        }
        return MvcUriComponentsBuilder.fromMethodName(UploadController.class, "getFile", image).build().toString();
    }
}
