package com.simpleshop.config;

import com.simpleshop.entity.Category;
import com.simpleshop.entity.Product;
import com.simpleshop.repository.CategoryRepository;
import com.simpleshop.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

// runs on startup and fills db with some test data
// profile !test means this wont run during unit tests
@Component
@Profile("!test")
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public DataInitializer(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        // only insert data if db is empty
        if (categoryRepository.count() == 0) {

            Category mice       = categoryRepository.save(new Category(null, "Mice"));
            Category keyboards  = categoryRepository.save(new Category(null, "Keyboards"));
            Category headphones = categoryRepository.save(new Category(null, "Headphones"));
            Category mousepads  = categoryRepository.save(new Category(null, "Mousepads"));
            Category monitors   = categoryRepository.save(new Category(null, "Monitors"));
            Category webcams    = categoryRepository.save(new Category(null, "Webcams"));

            productRepository.save(new Product(null,
                "Logitech G Pro X Superlight 2",
                "Ultra-lightweight wireless gaming mouse, 60g, HERO 25K sensor, up to 95 hours battery life.",
                159.99, "https://sm.ign.com/t/ign_ap/review/l/logitech-g/logitech-g-pro-x-superlight-2-review_h1t7.1280.jpg", mice));

            productRepository.save(new Product(null,
                "Razer DeathAdder V3",
                "Ergonomic wired gaming mouse with Focus Pro 30K sensor, optical switches, 90M click lifespan.",
                99.99, "https://cdn.mos.cms.futurecdn.net/pireQpaXL9FdM3zge3giK9-1920-80.jpg.webp", mice));


            productRepository.save(new Product(null,
                "Logitech MX Master 3S",
                "Premium wireless productivity mouse. MagSpeed scroll wheel, 8K DPI, silent clicks, USB-C.",
                99.99, "https://i.rtings.com/assets/products/tfI79vsP/logitech-mx-master-3s/design-medium.jpg?format=auto", mice));

            productRepository.save(new Product(null,
                "Keychron Q1 Pro",
                "75% wireless mechanical keyboard, QMK/VIA compatible, gasket-mounted, hot-swappable switches.",
                199.99, "https://www.keychron.com/cdn/shop/products/Keychron-Q1-Pro-QMK-VIA-wireless-custom-mechanical-keyboard-knob-75-percent-layout-full-aluminum-grey-frame-for-Mac-Windows-Linux-with-RGB-backlight-hot-swappable-K-Pro-switch-red.jpg?v=1689309013&width=900", keyboards));

            productRepository.save(new Product(null,
                "Logitech G915 TKL",
                "Tenkeyless wireless mechanical keyboard, low-profile GL switches, RGB, 40-hour battery.",
                229.99, "https://cdn.mos.cms.futurecdn.net/aBjagukyz4zxUBTqaYqLtH-1920-80.jpg.webp", keyboards));

            productRepository.save(new Product(null,
                "Sony WH-1000XM5",
                "Industry-leading noise cancelling headphones, 30-hour battery, multipoint connection, Hi-Res Audio.",
                349.99, "https://www.sony.ee/image/ce0f6885ec1d1701f08d7b522e4f4ca3?fmt=png-alpha&wid=1578&hei=1050&bgcolor=F6F9FF", headphones));

            productRepository.save(new Product(null,
                "HyperX Cloud Alpha",
                "Gaming headset with dual chamber drivers, detachable noise-cancelling mic, memory foam cushions.",
                99.99, "https://row.hyperx.com/cdn/shop/products/hyperx_cloud_alpha_black_1_main.jpg?v=1662420668", headphones));

            productRepository.save(new Product(null,
                "SteelSeries QcK Heavy XXL",
                "Extra-large gaming mousepad 900x300mm, micro-woven cloth surface, non-slip rubber base.",
                49.99, "https://images.ctfassets.net/hmm5mo4qf4mf/3IhrqHBnRgwdfT3YWNm99f/054980aca1a2eb97d4bdb861c3c6f652/1200x_buy_qck-heavy_xxl_02.png__1920x1080_crop-fit_optimize_subsampling-2-3472.png?fm=webp&q=90&fit=scale&w=1200", mousepads));

            productRepository.save(new Product(null,
                "Razer Strider XXL",
                "Hybrid hard-soft gaming mousepad 940x410mm, water-resistant surface, ultra-low 3mm profile.",
                64.99, "https://cdn.4games.pro/storage/products/2625/063681c24505c99fa669f97aba0e188b.jpg", mousepads));

            productRepository.save(new Product(null,
                "LG 27GP850-B 27inch",
                "27-inch QHD IPS gaming monitor, 165Hz, 1ms GtG, NVIDIA G-Sync Compatible, HDR400.",
                399.99, "https://media.us.lg.com/transform/ecomm-PDPGallery-1100x730/0d1e62da-95ec-40e6-b2a3-0ef94b03aea3/md08000370-Z-01-jpg?io=transform:fill,width:900", monitors));

            productRepository.save(new Product(null,
                "Samsung Odyssey G7 32inch",
                "32-inch 4K UHD curved gaming monitor, 144Hz, 1ms, QLED, HDR600, G-Sync Compatible.",
                699.99, "https://images.samsung.com/is/image/samsung/p6pim/kz_ru/ls32dg702eixci/gallery/kz-ru-odyssey-g7-g70d-ls32dg702eixci-546642420?$Q90_1920_1280_F_PNG$", monitors));

            productRepository.save(new Product(null,
                "Logitech StreamCam",
                "Full HD 1080p/60fps streaming webcam, USB-C, vertical video support, AI-powered autofocus.",
                149.99, "https://resource.logitech.com/w_544,h_466,ar_7:6,c_pad,q_auto,f_auto,dpr_1.0/d_transparent.gif/content/dam/logitech/en/products/webcams/streamcam/gallery/streamcam-gallery-2-white.png", webcams));

            productRepository.save(new Product(null,
                "Elgato Facecam Pro",
                "4K60 true ultra HD webcam, Sony STARVIS sensor, exceptional low-light performance.",
                299.99, "https://res.cloudinary.com/elgato-pwa/image/upload/q_auto,f_auto/v1676210262/Products/10WAB9901/above-the-fold/desktop/facecam-pro-abf-desktop-01_sakqh2.jpg", webcams));
        }
    }
}
