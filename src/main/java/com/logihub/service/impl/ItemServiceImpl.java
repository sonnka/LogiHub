package com.logihub.service.impl;

import com.logihub.exception.InvoiceException;
import com.logihub.exception.UserException;
import com.logihub.model.entity.Invoice;
import com.logihub.model.entity.Item;
import com.logihub.model.request.ItemRequest;
import com.logihub.repository.InvoiceRepository;
import com.logihub.repository.ItemRepository;
import com.logihub.service.ItemService;
import com.logihub.util.AuthUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final InvoiceRepository invoiceRepository;
    private final AuthUtil authUtil;

    @Override
    public void addItems(String email, Long userId, Long invoiceId, List<ItemRequest> itemRequest)
            throws UserException, InvoiceException {
        authUtil.findTruckManagerByEmailAndId(email, userId);

        var invoice = invoiceRepository.findById(invoiceId).orElseThrow(
                () -> new InvoiceException(InvoiceException.InvoiceExceptionProfile.INVOICE_NOT_FOUND)
        );

        List<Item> items = itemRequest.stream().map(i -> toItem(i, invoice)).toList();

        itemRepository.saveAll(items);
    }

    private Item toItem(ItemRequest itemRequest, Invoice invoice) {
        Item item = new Item();
        item.setName(itemRequest.getName());
        item.setPrice(itemRequest.getPrice());
        item.setAmount(itemRequest.getAmount());
        item.setWeight(itemRequest.getWeight());
        item.setTotalPrice(itemRequest.getPrice() * itemRequest.getAmount());
        item.setTotalWeight(itemRequest.getWeight() * itemRequest.getAmount());
        item.setInvoice(invoice);
        return item;
    }
}
