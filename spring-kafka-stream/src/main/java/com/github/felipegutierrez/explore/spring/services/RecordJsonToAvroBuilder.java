package com.github.felipegutierrez.explore.spring.services;

import com.github.felipegutierrez.explore.spring.model.HadoopRecordAvro;
import com.github.felipegutierrez.explore.spring.model.LineItem;
import com.github.felipegutierrez.explore.spring.model.NotificationAvro;
import com.github.felipegutierrez.explore.spring.model.PosInvoice;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.github.felipegutierrez.explore.spring.utils.PosInvoiceConstants.HOME_DELIVERY;

@Service
public class RecordJsonToAvroBuilder {

    public NotificationAvro getNotificationAvro(PosInvoice posInvoice) {
        NotificationAvro notificationAvro = NotificationAvro.newBuilder()
                .setInvoiceNumber(posInvoice.getInvoiceNumber())
                .setCustomerCardNo(posInvoice.getCustomerCardNo())
                .setTotalAmount(posInvoice.getTotalAmount())
                .setEarnedLoyaltyPoints(posInvoice.getTotalAmount() * 0.02)
                .build();
        return notificationAvro;
    }

    public PosInvoice getMaskedInvoice(PosInvoice posInvoice) {
        posInvoice.setCustomerCardNo(null);
        if (posInvoice.getDeliveryType().equalsIgnoreCase(HOME_DELIVERY)) {
            posInvoice.getDeliveryAddress().setAddressLine(null);
            posInvoice.getDeliveryAddress().setContactNumber(null);
        }
        return posInvoice;
    }

    public List<HadoopRecordAvro> getHadoopRecords(PosInvoice posInvoice) {
        List<HadoopRecordAvro> hadoopRecordAvroList = new ArrayList<HadoopRecordAvro>();
        for (LineItem lineItem : posInvoice.getInvoiceLineItems()) {
            HadoopRecordAvro hadoopRecordAvro = HadoopRecordAvro.newBuilder()
                    .setInvoiceNumber(posInvoice.getInvoiceNumber())
                    .setCreatedTime(posInvoice.getCreatedTime())
                    .setStoreID(posInvoice.getStoreID())
                    .setPosID(posInvoice.getPosID())
                    .setCustomerType(posInvoice.getCustomerType())
                    .setPaymentMethod(posInvoice.getPaymentMethod())
                    .setDeliveryType(posInvoice.getDeliveryType())
                    .setItemCode(lineItem.getItemCode())
                    .setItemDescription(lineItem.getItemDescription())
                    .setItemPrice(lineItem.getItemPrice())
                    .setItemQty(lineItem.getItemQty())
                    .setTotalValue(lineItem.getTotalValue())
                    .build();
            if (posInvoice.getDeliveryType().equalsIgnoreCase(HOME_DELIVERY)) {
                hadoopRecordAvro.setCity(posInvoice.getDeliveryAddress().getCity());
                hadoopRecordAvro.setState(posInvoice.getDeliveryAddress().getState());
                hadoopRecordAvro.setPinCode(posInvoice.getDeliveryAddress().getPinCode());
            }
            hadoopRecordAvroList.add(hadoopRecordAvro);
        }
        return hadoopRecordAvroList;
    }
}
