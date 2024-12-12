package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private UUID id;
    private String firstName; // Foydalanuvchining ismi
    private String lastName; // Foydalanuvchining familiyasi
    private String sureName; // Otasining ismi
    private boolean sex; // Jinsi (true - erkak, false - ayol)
    private Date birthday; // Tug'ilgan sana
    private String email; // Email
    private String phoneNumber; // Telefon raqami
    private String username; // Login
    private String password; // Parol
    private String passportNumber; // Pasport raqami
    private String jshshsr; // JSHSHIR
    private String address; // Yashash manzili
    private UUID photoId; // Surat uchun attachment id (fayl IDsi)
    private String department; // Bo‘lim
    private String position; // Lavozim
    private UUID jobId; // Job ID
    private String branch; // Filial
    private String arrivalTime; // Kelish vaqti
    private String leaveTime; // Ketish vaqti
    private Double salaryAmount; // Maosh miqdori
    private String salaryType; // Maosh turi
    private String pinCode; // PIN kod
    private String shiftType; // Smena turi
    private Date probation; // Sinov muddati
    private UUID roleId; // Role ID
    private UUID businessId; // Business ID
    private Set<UUID> branchIds; // Filial ID lar
    private String description; // Qo'shimcha ma'lumot
    private Date dateOfEmployment; // Ishga kirish sanasi
    private String contractNumber; // Shartnoma raqami
    private UUID contractFileId; // Shartnoma fayli ID
    private List<EmergencyContactDTO> emergencyContacts; // Favqulodda aloqa ma'lumotlari
    private boolean active;
    private boolean enabled;
    private boolean grossPriceControlOneUser;
}