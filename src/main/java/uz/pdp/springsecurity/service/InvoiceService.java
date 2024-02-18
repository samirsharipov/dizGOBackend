package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.Attachment;
import uz.pdp.springsecurity.entity.Branch;
import uz.pdp.springsecurity.entity.Invoice;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.InvoiceDto;
import uz.pdp.springsecurity.repository.AttachmentRepository;
import uz.pdp.springsecurity.repository.BranchRepository;
import uz.pdp.springsecurity.repository.InvoiceRepository;

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
        Optional<Attachment> optionalAttachment = attachmentRepository.findById(invoiceDto.getPhotoId());
        if (optionalAttachment.isPresent()) invoice.setPhoto(optionalAttachment.get());
        invoiceRepository.save(invoice);
        return new ApiResponse("SUCCESS", true);
    }

    public ApiResponse getOne(UUID branchId) {
        Optional<Invoice> optionalInvoice = invoiceRepository.findByBranch_Id(branchId);
        if (optionalInvoice.isEmpty()){
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
        return new ApiResponse(true, invoiceDto);
    }
}
