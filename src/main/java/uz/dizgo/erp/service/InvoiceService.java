package uz.dizgo.erp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.dizgo.erp.entity.Attachment;
import uz.dizgo.erp.entity.Branch;
import uz.dizgo.erp.entity.Invoice;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.payload.InvoiceDto;
import uz.dizgo.erp.repository.AttachmentRepository;
import uz.dizgo.erp.repository.BranchRepository;
import uz.dizgo.erp.repository.InvoiceRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final AttachmentRepository attachmentRepository;
    private final BranchRepository branchRepository;

    public void create(Branch branch) {
        invoiceRepository.save(new Invoice(
                branch,
                branch.getName(),
                "Xush kelibsiz",
                "Xaridingiz uchun rahmat"
        ));
    }

    public ApiResponse edit(UUID branchId, InvoiceDto invoiceDto) {
        Optional<Invoice> optionalInvoice = invoiceRepository.findByBranch_Id(branchId);
        if (optionalInvoice.isEmpty()) return new ApiResponse("INVOICE NOT FOUND", false);
        Invoice invoice = optionalInvoice.get();
        invoice.setName(invoiceDto.getName());
        invoice.setDescription(invoiceDto.getDescription());
        invoice.setFooter(invoiceDto.getFooter());
        if (invoiceDto.getPhotoId() != null) {
            Optional<Attachment> optionalAttachment = attachmentRepository.findById(invoiceDto.getPhotoId());
            optionalAttachment.ifPresent(invoice::setPhoto);
        }
        invoiceRepository.save(invoice);
        return new ApiResponse("SUCCESS", true);
    }

    public ApiResponse getOne(UUID branchId) {
        Optional<Invoice> optionalInvoice = invoiceRepository.findByBranch_Id(branchId);
        if (optionalInvoice.isEmpty()) {
            Optional<Branch> optionalBranch = branchRepository.findById(branchId);
            if (optionalBranch.isEmpty()) return new ApiResponse("BRANCH NOT FOUND", false);
            create(optionalBranch.get());
            return getOne(branchId);
        }
        Invoice invoice = optionalInvoice.get();
        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setName(invoice.getName());
        invoiceDto.setDescription(invoice.getDescription());
        invoiceDto.setFooter(invoice.getFooter());
        if (invoice.getPhoto() != null) invoiceDto.setPhotoId(invoice.getPhoto().getId());
        return new ApiResponse("success",true, invoiceDto);
    }
}
