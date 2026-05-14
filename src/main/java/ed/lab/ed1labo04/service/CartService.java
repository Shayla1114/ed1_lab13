package ed.lab.ed1labo04.service;

import ed.lab.ed1labo04.entity.CartEntity;
import ed.lab.ed1labo04.entity.CartItemEntity;
import ed.lab.ed1labo04.entity.ProductEntity;
import ed.lab.ed1labo04.model.CartRequest;
import ed.lab.ed1labo04.model.ItemRequest;
import ed.lab.ed1labo04.repository.CartRepository;
import ed.lab.ed1labo04.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public CartEntity createCart(CartRequest request) {
        CartEntity cart = new CartEntity();
        double total = 0;

        for (ItemRequest itemReq : request.getItems()) {
            if (itemReq.getQuantity() <= 0) throw new IllegalArgumentException("Quantity must be > 0");

            ProductEntity product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found"));

            if (product.getQuantity() < itemReq.getQuantity())
                throw new IllegalArgumentException("Not enough stock for: " + product.getName());

            product.setQuantity(product.getQuantity() - itemReq.getQuantity());
            productRepository.save(product);

            CartItemEntity cartItem = new CartItemEntity();
            cartItem.setProduct(product);
            cartItem.setQuantity(itemReq.getQuantity());
            cartItem.setPriceAtPurchase(product.getPrice());

            cart.getItems().add(cartItem);
            total += product.getPrice() * itemReq.getQuantity();
        }

        cart.setTotalPrice(total);
        return cartRepository.save(cart);
    }

    public Optional<CartEntity> getCartById(Long id) {
        return cartRepository.findById(id);
    }
}