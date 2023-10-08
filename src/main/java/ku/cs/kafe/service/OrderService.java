//atitaya promlers 6410406924
package ku.cs.kafe.service;

import ku.cs.kafe.common.Status;
import ku.cs.kafe.entity.Menu;
import ku.cs.kafe.entity.OrderItem;
import ku.cs.kafe.entity.OrderItemKey;
import ku.cs.kafe.entity.PurchaseOrder;
import ku.cs.kafe.model.AddToCartRequest;
import ku.cs.kafe.repository.MenuRepository;
import ku.cs.kafe.repository.OrderItemRepository;
import ku.cs.kafe.repository.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Service
public class OrderService {
    @Autowired
    private PurchaseOrderRepository orderRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderItemRepository itemRepository;
    private UUID currentOrderId;
    public void createNewOrder(){
        PurchaseOrder newOrder = new PurchaseOrder();
        newOrder.setStatus(Status.ORDER);
        PurchaseOrder record = orderRepository.save(newOrder);
        currentOrderId = record.getId();
    }
    public PurchaseOrder getCurrentOrder() {
        if (currentOrderId == null)
            createNewOrder();
        return orderRepository.findById(currentOrderId).get();
    }


    public void submitOrder() {
        PurchaseOrder currentOrder =
                orderRepository.findById(currentOrderId).get();
        currentOrder.setTimestamp(LocalDateTime.now());
        currentOrder.setStatus(Status.CONFIRM);
        orderRepository.save(currentOrder);
        currentOrderId = null;
    }


    public void order(UUID menuId, AddToCartRequest request){
        if(currentOrderId==null)
            createNewOrder();
        PurchaseOrder currentOrder = orderRepository.findById(currentOrderId).get();
        Menu menu =  menuRepository.findById(menuId).get();
        OrderItem item = new OrderItem();
        item.setId(new OrderItemKey(currentOrderId,menuId));
        item.setPurchaseOrder(currentOrder);
        item.setMenu(menu);
        item.setQuantity(request.getQuantity());
        itemRepository.save(item);
    }

    public List<PurchaseOrder> getAllOrders() {
        return orderRepository.findAll();
    }

    public PurchaseOrder getOrderById(UUID orderId){
        return orderRepository.findById(orderId).get();
    }

    public void finishOrder(UUID orderId){
        PurchaseOrder record = orderRepository.findById(orderId).get();
        record.setStatus(Status.FINISH);
        orderRepository.save(record);

    }

}
