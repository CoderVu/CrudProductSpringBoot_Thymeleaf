package com.example.Thymeleaf_demo.Controler;

import com.example.Thymeleaf_demo.Entity.Product;
import com.example.Thymeleaf_demo.model.DAO.ProductDAO;
import com.example.Thymeleaf_demo.Services.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.Thymeleaf_demo.utils.general;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductsRepository productsRepository;
    @GetMapping({"", "/"})
    public String showProductList(Model model) {
        List<Product> products = productsRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        model.addAttribute("products", products);
        return "products/index";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        ProductDAO productDTO = new ProductDAO();
        model.addAttribute("productDTO", productDTO);
        return "products/CreateProduct";
    }

    @PostMapping("/create")
    public String createProduct(@ModelAttribute ProductDAO productDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            // Xử lý lỗi nếu có
            return "products/CreateProduct";
        }

        // Nhận hình ảnh từ ProductDTO
        MultipartFile imageFile = productDTO.getImageFile();
        String base64Image = null; // Khởi tạo base64Image với giá trị mặc định là null

        try {
            InputStream inputStream = imageFile.getInputStream();
            if (inputStream != null) {
                base64Image = general.fileToBase64(inputStream);
            } else {
                // Xử lý trường hợp không có InputStream từ MultipartFile
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Xử lý lỗi khi cố gắng lấy InputStream
            // Ví dụ: Hiển thị thông báo lỗi cho người dùng
            model.addAttribute("errorMessage", "Error occurred while processing image file.");
            return "errorPage"; // Chuyển hướng đến trang lỗi
        }


        LocalDateTime createdAt = productDTO.getCreatedAt();
        // Tiến hành lưu sản phẩm vào cơ sở dữ liệu
        Product product = new Product();
        // Gán các thuộc tính của product từ productDTO
        product.setName(productDTO.getName());
        product.setBrand(productDTO.getBrand());
        product.setCategory(productDTO.getCategory());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        product.setCreatedAt(createdAt);
        // Lưu chuỗi base64 vào thuộc tính image của Product
        product.setImageFileName(base64Image);

        // Lưu product vào cơ sở dữ liệu
        productsRepository.save(product);

        // Redirect về trang danh sách sản phẩm sau khi tạo thành công
        return "redirect:/products";
    }
    @GetMapping("/edit/{id}")
    public String showEditPage(@PathVariable("id") Long id, Model model) {
        Assert.notNull(id, "ID must not be null");
        Optional<Product> optionalProduct = productsRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();

            // Chuyển đổi dữ liệu ảnh từ base64 sang InputStream
            String base64Image = product.getImageFileName();
            InputStream inputStream = null;
            try {
                byte[] imageBytes = Base64.getDecoder().decode(base64Image);
                inputStream = new ByteArrayInputStream(imageBytes);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                // Xử lý nếu có lỗi xảy ra khi chuyển đổi dữ liệu ảnh
            }
            ProductDAO productDTO = new ProductDAO();
            productDTO.setName(product.getName());
            productDTO.setBrand(product.getBrand());
            productDTO.setCategory(product.getCategory());
            productDTO.setPrice(product.getPrice());
            productDTO.setDescription(product.getDescription());
            productDTO.setCreatedAt(product.getCreatedAt());

            // Truyền chuỗi base64Image vào model để sử dụng trong template Thymeleaf
            model.addAttribute("base64Image", base64Image);
            model.addAttribute("productDTO", productDTO);
            return "products/EditProduct";
        } else {
            return "errorPage";
        }
    }

    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable Long id, @ModelAttribute @Validated ProductDAO productDTO, BindingResult bindingResult, Model model) {
        Assert.notNull(id, "ID must not be null");
        Optional<Product> optionalProduct = productsRepository.findById(id);
        if (!optionalProduct.isPresent()) {
            return "errorPage";
        }
        if (!productDTO.isValid()) {
            // Xử lý lỗi nếu dữ liệu không hợp lệ
            model.addAttribute("productDTO", productDTO);
            return "products/EditProduct";
        }


        if (bindingResult.hasErrors()) {
            model.addAttribute("productDTO", productDTO);
            return "products/EditProduct";
        }

        Product product = optionalProduct.get();
        product.setName(productDTO.getName());
        product.setBrand(productDTO.getBrand());
        product.setCategory(productDTO.getCategory());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());

        // Cập nhật ngày tạo sản phẩm
        product.setCreatedAt(productDTO.getCreatedAt());

        // Xử lý hình ảnh
        MultipartFile imageFile = productDTO.getImageFile();
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                InputStream inputStream = imageFile.getInputStream();
                String base64Image = general.fileToBase64(inputStream);
                product.setImageFileName(base64Image);
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("errorMessage", "Error occurred while processing image file.");
                return "errorPage";
            }
        }

        productsRepository.save(product);

        return "redirect:/products";
    }


    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        // Xóa sản phẩm từ cơ sở dữ liệu
        productsRepository.deleteById(id);
        // Redirect về trang danh sách sản phẩm sau khi xóa thành công
        return "redirect:/products";
    }

}