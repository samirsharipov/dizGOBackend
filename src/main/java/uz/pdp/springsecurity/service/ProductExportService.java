package uz.pdp.springsecurity.service;

import com.opencsv.CSVWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.Attachment;
import uz.pdp.springsecurity.entity.AttachmentContent;
import uz.pdp.springsecurity.entity.Business;
import uz.pdp.springsecurity.entity.Product;
import uz.pdp.springsecurity.repository.AttachmentContentRepository;
import uz.pdp.springsecurity.repository.AttachmentRepository;
import uz.pdp.springsecurity.repository.BusinessRepository;
import uz.pdp.springsecurity.repository.ProductRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductExportService {
    private final BusinessRepository businessRepository;
    private final ProductRepository productRepository;
    private final AttachmentRepository attachmentRepository;
    private final AttachmentContentRepository attachmentContentRepository;
    private final AttachmentService attachmentService;

//    @Scheduled(cron = "0 0 0/1 * * *")
//    public void scheduledCSVExport() {
//        exportProducts();
//    }
//
//    public void exportProducts() {
//        List<Business> all = businessRepository.findAll();
//        List<Product> products = new ArrayList<>();
//        for (Business business : all) {
//            products.addAll(productRepository.findAllByBusiness_IdAndActiveTrueAndIsGlobalTrue(business.getId()));
//        }
//        writeProductsToCSV(products);
//
//        List<Attachment> allAttachment = attachmentRepository.findAllByName("products.csv");
//        if (!allAttachment.isEmpty()) {
//            for (Attachment attachment : allAttachment) {
//                Optional<AttachmentContent> optionalAttachmentContent = attachmentContentRepository.findByAttachmentId(attachment.getId());
//                attachmentContentRepository.delete(optionalAttachmentContent.get());
//                attachmentRepository.delete(attachment);
//            }
//        }
//
//        String fileName = "products.csv";
//        try {
//            ClassPathResource classPathResource = new ClassPathResource(fileName);
//            try (InputStream inputStream = classPathResource.getInputStream();
//                 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
//
//                byte[] buffer = new byte[1024];
//                int bytesRead;
//                while ((bytesRead = inputStream.read(buffer)) != -1) {
//                    byteArrayOutputStream.write(buffer, 0, bytesRead);
//                }
//
//                byte[] fileContent = byteArrayOutputStream.toByteArray();
//
//                Attachment attachment = new Attachment();
//                attachment.setName(fileName);
//                attachment.setFileOriginalName(fileName);
//                attachment.setContentType("text/csv");
//
//                AttachmentContent attachmentContent = new AttachmentContent();
//                attachmentContent.setMainContent(fileContent);
//                attachmentContent.setAttachment(attachmentRepository.save(attachment));
//
//                attachmentContentRepository.save(attachmentContent);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    @Scheduled(cron = "0 0 0/1 * * *")
    public void scheduledCSVExport() {
        exportProducts();
    }

    public void exportProducts() {
        List<Business> allBusinesses = businessRepository.findAll();
        List<Product> products = new ArrayList<>();
        for (Business business : allBusinesses) {
            products.addAll(productRepository.findAllByBusiness_IdAndActiveTrueAndIsGlobalTrue(business.getId()));
        }

        byte[] csvContent = createCSV(products);
        if (csvContent != null) {
            saveAttachment(csvContent);
        }
    }

    private byte[] createCSV(List<Product> products) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             OutputStreamWriter outputStreamWriter = new OutputStreamWriter(byteArrayOutputStream);
             CSVWriter csvWriter = new CSVWriter(outputStreamWriter)) {

            // Header row
            String[] header = {
                    "Product Name", "Unique SKU", "Language", "Stock Amount", "InStock", "Preorder",
                    "Length", "Width", "Height", "Weight", "HS Code-1-2", "HS Code-2-2",
                    "HS Code-3-2", "HS Code-4-4", "Key Word", "Brief Description", "Long Description",
                    "Agreement Exports ID", "Agreement Exports PID", "Agreement Local ID",
                    "Agreement Local PID", "Lang Group", "Shipping Class", "Attributes", "Sold Individually",
                    "Business ID"
            };
            csvWriter.writeNext(header);

            // Data rows
            for (Product product : products) {
                String[] data = {
                        product.getName(),
                        product.getUniqueSKU(),

//                        product.getSoldIndividualy() != null ? product.getSoldIndividualy().toString() : "",
//                        product.getLanguage() != null ? product.getLanguage().name() : "",
//                        product.getKeyWord(),
//                        product.getBriefDescription(),

                        product.getStockAmount() != null ? product.getStockAmount().toString() : "",
                        product.getInStock() != null ? product.getInStock().toString() : "",
                        product.getPreorder() != null ? product.getPreorder().toString() : "",
                        product.getLength() != null ? product.getLength().toString() : "",
                        product.getWidth() != null ? product.getWidth().toString() : "",
                        product.getHeight() != null ? product.getHeight().toString() : "",
                        product.getWeight() != null ? product.getWeight().toString() : "",
                        product.getHsCode12(),
                        product.getHsCode22(),
                        product.getHsCode32(),
                        product.getHsCode44(),
                        product.getLongDescription(),
                        product.getAgreementExportsID(),
                        product.getAgreementExportsPID(),
                        product.getAgreementLocalID(),
                        product.getAgreementLocalPID(),
                        product.getLangGroup(),
                        product.getShippingClass(),
                        product.getAttributes(),
                        product.getBusiness() != null ? product.getBusiness().getId().toString() : "",
                };
                csvWriter.writeNext(data);
            }

            csvWriter.flush();
            return byteArrayOutputStream.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveAttachment(byte[] csvContent) {
        List<Attachment> existingAttachments = attachmentRepository.findAllByName("products.csv");
        if (!existingAttachments.isEmpty()) {
            for (Attachment attachment : existingAttachments) {
                Optional<AttachmentContent> optionalAttachmentContent = attachmentContentRepository.findByAttachmentId(attachment.getId());
                optionalAttachmentContent.ifPresent(attachmentContentRepository::delete);
                attachmentRepository.delete(attachment);
            }
        }

        Attachment attachment = new Attachment();
        attachment.setName("products.csv");
        attachment.setFileOriginalName("products.csv");
        attachment.setContentType("text/csv");
        attachment.setSize(csvContent.length);

        Attachment savedAttachment = attachmentRepository.save(attachment);

        AttachmentContent attachmentContent = new AttachmentContent();
        attachmentContent.setMainContent(csvContent);
        attachmentContent.setAttachment(savedAttachment);

        attachmentContentRepository.save(attachmentContent);
    }


//    public static String writeProductsToCSV(List<Product> products) {
//        String filePath = "src/main/resources/products.csv";
//        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
//            // Header row
//            String[] header = {
//                    "Product Name", "Unique SKU", "Language", "Stock Amount", "InStock", "Preorder",
//                    "Length", "Width", "Height", "Weight", "HS Code-1-2", "HS Code-2-2",
//                    "HS Code-3-2", "HS Code-4-4", "Key Word", "Brief Description", "Long Description",
//                    "Agreement Exports ID", "Agreement Exports PID", "Agreement Local ID",
//                    "Agreement Local PID", "Lang Group", "Shipping Class", "Attributes", "Sold Individually",
//                    "Business ID"
//            };
//            writer.writeNext(header);
//
//            // Data rows
//            for (Product product : products) {
//                String[] data = {
//                        product.getName(),
//                        product.getUniqueSKU(),
//                        product.getLanguage() != null ? product.getLanguage().name() : "",
//                        product.getStockAmount() != null ? product.getStockAmount().toString() : "",
//                        product.getInStock() != null ? product.getInStock().toString() : "",
//                        product.getPreorder() != null ? product.getPreorder().toString() : "",
//                        product.getLength() != null ? product.getLength().toString() : "",
//                        product.getWidth() != null ? product.getWidth().toString() : "",
//                        product.getHeight() != null ? product.getHeight().toString() : "",
//                        product.getWeight() != null ? product.getWeight().toString() : "",
//                        product.getHsCode12(),
//                        product.getHsCode22(),
//                        product.getHsCode32(),
//                        product.getHsCode44(),
//                        product.getKeyWord(),
//                        product.getBriefDescription(),
//                        product.getLongDescription(),
//                        product.getAgreementExportsID(),
//                        product.getAgreementExportsPID(),
//                        product.getAgreementLocalID(),
//                        product.getAgreementLocalPID(),
//                        product.getLangGroup(),
//                        product.getShippingClass(),
//                        product.getAttributes(),
//                        product.getSoldIndividualy() != null ? product.getSoldIndividualy().toString() : "",
//                        product.getBusiness() != null ? product.getBusiness().getId().toString() : "",
//                };
//                writer.writeNext(data);
//            }
//            return filePath;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
}

