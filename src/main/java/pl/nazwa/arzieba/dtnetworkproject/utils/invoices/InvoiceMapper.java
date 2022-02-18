package pl.nazwa.arzieba.dtnetworkproject.utils.invoices;

import org.springframework.stereotype.Component;
import pl.nazwa.arzieba.dtnetworkproject.dto.InvoiceDTO;
import pl.nazwa.arzieba.dtnetworkproject.model.ElectricalInvoice;
import pl.nazwa.arzieba.dtnetworkproject.utils.calendar.CalendarUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class InvoiceMapper {

    public static InvoiceDTO map(ElectricalInvoice invoice){

        InvoiceDTO dto = new InvoiceDTO();

        dto.setBuilding(invoice.getBuilding());
        dto.setDate(CalendarUtil.cal2string(invoice.getDate()));
        dto.setInvoiceId(invoice.getInvoiceId());
        dto.setNetValue(invoice.getNetValue());
        return dto;
    }

    public static ElectricalInvoice map(InvoiceDTO dto){

        ElectricalInvoice invoice = new ElectricalInvoice();

        invoice.setBuilding(dto.getBuilding());
        invoice.setDate(CalendarUtil.string2cal(dto.getDate()));
        invoice.setNetValue(dto.getNetValue());
        invoice.setGrossValue(InvoiceMapper.round(1.23*dto.getNetValue(),2));
        invoice.setInvoiceId(dto.getInvoiceId());
        return invoice;
    }

    public static double round(double value, int places) {

        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);

        return bd.doubleValue();
    }
}
