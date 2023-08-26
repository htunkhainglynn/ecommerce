package com.project.ecommerce.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.project.ecommerce.dto.ProductVariantDto;
import com.project.ecommerce.exception.ProductException;
import com.project.ecommerce.utils.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class CloudinaryService {

    private final Cloudinary cloudinary;

    @Autowired
    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public void uploadAndSaveUrl(Integer productSku, ProductVariantDto productVariant, HttpServletRequest request) throws Exception {
        String folderName = createFolder(productSku);  // same folder doesn't create twice

        Result<MultipartFile> imageResult = retrieveImage(request, productVariant);

        if (imageResult.isSuccess()) {
            MultipartFile imageFile = imageResult.getValue();
            productVariant.setImageUrl(uploadFile(folderName, imageFile));
        } else {
            Exception error = imageResult.getError();
            // Handle the error as needed
            throw new ProductException("Image retrieval failed: " + error.getMessage());
        }

    }

    /*
    * There are two methods to retrieve image from request because I use uploadAndSaveUrl() for productDto and productVariantDto.
    * First method is for productDto.productVariantDtoList, so image file name looks like this -> productVariants[0].imageFile.
    * Second method is for productVariantDto & uploadProfilePic, image file name looks like this -> imageFile.
    * So, I solve this problem by using railway oriented programming
    * https://naveenkumarmuguda.medium.com/railway-oriented-programming-a-powerful-functional-programming-pattern-ab454e467f31
    * */
    private Result<MultipartFile> retrieveImage(HttpServletRequest request, ProductVariantDto productVariant) {
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;

        MultipartFile firstMethodFile = multipartHttpServletRequest.getFile("productVariants[" + productVariant.getId() + "].imageFile");
        MultipartFile secondMethodFile = multipartHttpServletRequest.getFile("imageFile");

        if (firstMethodFile != null && !firstMethodFile.isEmpty()) {
            return Result.success(firstMethodFile);
        } else if (secondMethodFile != null && !secondMethodFile.isEmpty()) {
            return Result.success(secondMethodFile);
        } else {
            return Result.failure(new Exception("No valid image file found"));
        }
    }

    public String uploadFile(String folderName, MultipartFile multipartFile) {
        try {
            File file = convertMultiPartToFile(multipartFile);

            Map<String, String> uploadOptions = new HashMap<>();
            uploadOptions.put("folder", folderName);

            String url = cloudinary.uploader().upload(file, uploadOptions).get("url").toString();
            boolean isDeleted = file.delete();  // delete image after upload

            if (isDeleted) {
                log.info("File successfully deleted");
            } else {
                log.warn("File doesn't exist");
            }

            return url;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    private String createFolder(Integer productSku) throws Exception {
        String folderName = "product_" + productSku; // Unique folder name
        cloudinary.api().createFolder(folderName, ObjectUtils.emptyMap());
        return folderName;
    }

    public void deleteFolder(String folderName) throws Exception {
        // delete all contents in folder
        cloudinary.api().deleteResourcesByPrefix(folderName, ObjectUtils.emptyMap());

        // delete folder
        cloudinary.api().deleteFolder(folderName, ObjectUtils.emptyMap());
    }

    // delete an image
    public void deleteImage(String url) {
        try {
            /* Extract the folder name using regular expression
            * Example url : http://res.cloudinary.com/dk9fdcnnp/image/upload/v1629781233/product_1/1_1_1.jpg
            * */
            String pattern = "/v\\d+/(.*?)/[^/]+\\.\\w+";
            Pattern regexPattern = Pattern.compile(pattern);
            Matcher matcher = regexPattern.matcher(url);

            if (matcher.find()) {
                String folderName = matcher.group(1);

                // Extract the public ID
                String publicId = url.substring(url.lastIndexOf('/') + 1, url.lastIndexOf('.'));

                // Construct full public ID
                String fullPublicId = folderName + "/" + publicId;

                // Delete the image using full public ID
                cloudinary.uploader().destroy(fullPublicId, ObjectUtils.emptyMap());
            } else {
                throw new RuntimeException("Folder name not found in URL");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
